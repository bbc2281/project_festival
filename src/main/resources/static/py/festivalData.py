import pandas as pd
from sqlalchemy import create_engine
import logging
import numpy as np
import os
from pathlib import Path 
import sys

logging.basicConfig(level=logging.ERROR)
logger = logging.getLogger(__name__)

def prepare_festival_data():
    try:
        db_url = 'mysql+pymysql://soldesk801:rladnxo9900!@soldesk801dbserver.mysql.database.azure.com/team_sixsense?charset=utf8mb4'
        engine = create_engine(db_url, echo=False)
    except Exception as e:
        logger.error(f"DB 접속 실패: {e}")
        raise

    try:
        sql_query = (
            'SELECT f.festival_idx, m.member_idx, f.festival_name, m.member_address, '
            'm.member_gender, m.member_job, m.member_birth FROM member m JOIN review r '
            'ON m.member_idx = r.member_idx JOIN festival f ON r.festival_idx = '
            'f.festival_idx '
            
            'UNION ALL '
            
            'SELECT f.festival_idx, c_m.member_idx, f.festival_name, c_m.member_address, '
            'c_m.member_gender, c_m.member_job, c_m.member_birth FROM comment c '
            'JOIN review r ON c.review_idx = r.review_idx '
            'JOIN festival f ON r.festival_idx = f.festival_idx '
            'JOIN member c_m ON c.member_idx = c_m.member_idx' 
        )
        
        festival_df = pd.read_sql(sql_query, engine)
        
        festival_df = festival_df.drop_duplicates(subset=['festival_idx', 'member_idx']) 

        mapping = { 
            'festival_idx': '축제번호', 'member_idx': '회원번호', 'festival_name': '축제명', 
            'member_address': '주소', 'member_gender': '성별', 'member_job': '직업', 
            'member_birth': '출생년도'
        }
        
        festival_df = festival_df.rename(columns=mapping)
        festival_df['주소'] = festival_df['주소'].apply(lambda addr: ' '.join(str(addr).split()[:2]) if pd.notna(addr) else addr)
        festival_df = festival_df.sort_values('축제번호')

        today = pd.Timestamp.today()
        festival_df['출생년도'] = pd.to_datetime(festival_df['출생년도'], errors='coerce')
        festival_df.dropna(subset=['출생년도'], inplace=True)

        def calculate_age(birth):
            age = today.year - birth.year
            if (today.month, today.day) < (birth.month, birth.day):
                age -= 1
            return age

        festival_df['나이'] = festival_df['출생년도'].apply(calculate_age)
        festival_df = festival_df.drop(['출생년도', '회원번호'], axis=1)

        bins = [0, 19, 29, 39, 49, 59, np.inf]
        labels = ['10대 이하', '20대', '30대', '40대', '50대', '60대 이상']
        festival_df['나이대'] = pd.cut(festival_df['나이'], bins=bins, labels=labels, right=True)
        
        return festival_df

    except Exception as e:
        logger.error(f"데이터 처리 실패: {e}")
        raise

def analyze_festival_data(df):
    visitor_count = df.groupby(['축제번호', '축제명']).size().reset_index(name='인원수')
    
    def get_percentage_distribution(series):
        distribution = (series.value_counts(normalize=True) * 100).round(1).astype(str) + '%'
        return ', '.join([f'{k}: {v}' for k, v in distribution.items()])

    grouped_stats = df.groupby(['축제번호', '축제명']).agg(
        나이대_분포=('나이대', get_percentage_distribution),
        성별_분포=('성별', get_percentage_distribution),
        직업_분포=('직업', get_percentage_distribution),
        주소_분포=('주소', get_percentage_distribution)
    ).reset_index()

    final_df = pd.merge(visitor_count, grouped_stats.drop(columns=['축제명']), on='축제번호')
    
    final_df.columns = [
        '축제번호', '축제명', '인원수', 
        '나이대별 분포', '성별분포', '직업분포', '주소분포'
    ]
    
    final_df['나이대별 분포'] = '나이대별 분포(' + final_df['나이대별 분포'] + ')'
    final_df['성별분포'] = '성별분포(' + final_df['성별분포'] + ')'
    final_df['직업분포'] = '직업(' + final_df['직업분포'] + ')'
    final_df['주소분포'] = '주소(' + final_df['주소분포'] + ')'
    
    return final_df.sort_values(by='축제번호')

if __name__ == '__main__':
    try:
        festival_df = prepare_festival_data()
        analysis_result = analyze_festival_data(festival_df)
        
        script_dir = Path(__file__).resolve().parent
        output_dir = script_dir.parent / "csv"
        output_file_path = output_dir / '인원 통계.csv'

        if not output_dir.exists():
            try:
                os.makedirs(output_dir)
            except Exception as e:
                logger.error(f"디렉토리 생성 실패: {output_dir}. 오류: {e}")
                sys.exit(1)

        analysis_result.to_csv(output_file_path, index=False, encoding='utf-8-sig')
        
    except Exception as e:
        logger.error(f"최종 분석 실행 실패: {e}")
        sys.exit(1)