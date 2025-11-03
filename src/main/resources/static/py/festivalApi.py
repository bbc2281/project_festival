import requests
import pandas as pd
from sqlalchemy import create_engine
from sqlalchemy import text
import datetime
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)
apiUrl = 'http://openapi.seoul.go.kr:8088/6342694e7a6262633132344e68487057/json/culturalEventInfo/1/395/축제/'

#api 접속
try:
    response = requests.get(apiUrl)
    data = response.json()
    rows = data['culturalEventInfo']['row']
    df = pd.DataFrame(rows)
except Exception as e:
    logger.error(f"데이터 처리 실패: {e}")
    raise

try:
    #db 접속
    db_url = 'mysql+pymysql://soldesk801:rladnxo9900!@soldesk801dbserver.mysql.database.azure.com/team_sixsense?charset=utf8mb4'
    engine = create_engine(db_url, echo=True)
except requests.RequestException as e:
    logger.error(f"DB 접속 실패: {e}")
    raise

try:
    #api 컬럼명 변경 
    mapping = {
        'CODENAME': 'festival_category_name',
        'GUNAME': 'region_name',
        'TITLE': 'festival_name',
        'USE_FEE': 'festival_fee',
        'STRTDATE': 'festival_begin_date',
        'END_DATE': 'festival_end_date',
        'ORG_NAME': 'festival_host',
        'PROGRAM': 'festival_info',
        'MAIN_IMG': 'festival_img_path',
        'ORG_LINK': 'festival_link',
        'PLACE': 'festival_address',
        'LAT': 'festival_lat',
        'LOT': 'festival_lot'
    }
    df = df.rename(columns=mapping)

    #컬럼 내용 변경
    df['festival_category_name'] = df['festival_category_name'].str.replace('축제-', '', regex=False)
    df['festival_fee'] = df['festival_fee'].str.replace('없음', '무료', regex=False)
    df.loc[df['festival_fee'] == '', 'festival_fee'] = '무료'
    df.loc[df['festival_info'] == '', 'festival_info'] = '상세내용은 링크를 참조해 주세요'
    df.loc[df['region_name'] == '', 'region_name'] = '기타'
    df['festival_lat'] = df['festival_lat'].str.replace('~.*', '', regex=True)
    df['festival_lot'] = df['festival_lot'].str.replace('~.*', '', regex=True)
    df['festival_lat'] = pd.to_numeric(df['festival_lat'], errors='coerce')
    df['festival_lot'] = pd.to_numeric(df['festival_lot'], errors='coerce')
    df['festival_info'] = df['festival_info'].str.replace('？', '', regex=False)

    #사용할 컬럼만 필터링
    db_cols = [
        'festival_category_name', 'region_name', 'festival_name', 'festival_fee',
        'festival_begin_date', 'festival_end_date', 'festival_host',
        'festival_info', 'festival_img_path', 'festival_link', 'festival_address',
        'festival_lat', 'festival_lot'
    ]

    for col in db_cols:
        if col not in df.columns:
            df[col] = None  # 누락된 컬럼은 None으로

    df = df[db_cols]  # 순서도 동일하게 맞춤

    #카테고리, 지역 테이블용 더미
    df_category = []
    df_region = []

    #카테고리,지역 테이블 삽입용 데이터프레임
    df_category = df[['festival_category_name']].drop_duplicates()
    df_region = df[['region_name']].drop_duplicates()

    df_region['is_etc'] = (df['region_name'] == '기타').astype(int)
    df_region = df_region.sort_values(by=['is_etc', 'region_name'], ascending=[True, True])
    df_region = df_region.drop(columns='is_etc')
    df_category['is_etc'] = (df['festival_category_name'] == '기타').astype(int)
    df_category = df_category.sort_values(by=['is_etc', 'festival_category_name'], ascending=[True, True])
    df_category = df_category.drop(columns='is_etc')

    #카테고리,지역 테이블 조회
    festival_category = pd.read_sql('SELECT * FROM festival_category', engine)
    region = pd.read_sql('SELECT * FROM region', engine)

    #이미 삽입된 데이터 중복 제거
    df_category = df_category[~df_category['festival_category_name'].isin(festival_category['festival_category_name'])]
    df_region = df_region[~df_region['region_name'].isin(region['region_name'])]

    df_category.to_sql('festival_category', con=engine, if_exists='append', index=False)
    df_region.to_sql('region', con=engine, if_exists='append', index=False)

    #변경된 카테고리,지역 테이블 조회
    festival_category = pd.read_sql('SELECT * FROM festival_category', engine)
    region = pd.read_sql('SELECT * FROM region', engine)

    # 카테고리명 → idx
    df = df.merge(festival_category, left_on='festival_category_name', right_on='festival_category_name', how='left')
    # 지역명 → idx
    df = df.merge(region, left_on='region_name', right_on='region_name', how='left')

    #최종 컬럼
    final_cols = [
        'festival_category_idx', 'region_idx', 'festival_name', 'festival_fee',
        'festival_begin_date', 'festival_end_date', 'festival_host',
        'festival_info', 'festival_img_path', 'festival_link', 'festival_address',
        'festival_lat', 'festival_lot'
    ]
    df = df[final_cols]

    #df['festival_category_idx'] = df['festival_category_idx'].fillna(2)
    df['region_idx'] = df['region_idx'].fillna(26)

    #기존 데이터 조회
    existing_df = pd.read_sql('SELECT festival_name FROM festival', engine) 
    #이미 삽입된 중복 데이터 제거(축제 이름 기준)
    df = df[~df['festival_name'].isin(existing_df['festival_name'])]
    #종료날짜가 지난 데이터 제거
    today = pd.Timestamp(datetime.date.today()) # 오늘날짜
    three_years_ago = today - datetime.timedelta(days=365*3) # 3년 전 날짜

    with engine.connect() as conn: # DB에서 지난 데이터 삭제
        delete_query = text("""
            DELETE FROM festival
            WHERE festival_end_date IS NOT NULL AND festival_end_date < :three_years_ago
        """)
        conn.execute(delete_query, {"three_years_ago": three_years_ago})
        conn.commit()

    df['festival_end_date'] = pd.to_datetime(df['festival_end_date'], errors='coerce') # datime으로 변환
    df = df[(df['festival_end_date'].isna()) | (df['festival_end_date'] >= today)] # df에서 이미 종료된 축제 데이터 삭제


    #DB에 데이터 삽입
    df.to_sql('festival', con=engine, if_exists='append', index=False)
except Exception as e:
    logger.error(f"DB 작업 실패: {e}")
    raise