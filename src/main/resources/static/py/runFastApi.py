from fastapi import FastAPI, Query, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
import requests
from datetime import datetime, timedelta
import math
from googleapiclient import discovery
from pydantic import BaseModel
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import NoSuchElementException, TimeoutException
import time
import re

app = FastAPI()

weather_api_key = "JWVjDL8GTl2lYwy_Br5d0Q"
moderate_api_key = 'AIzaSyAQ3GMnUAXXYelHFqnoxUnb2PcEPsjt51w'

origins = [
    "http://localhost:8080",
    "http://localhost:3000"
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

def dfs_xy_conv(lat: float, lon: float):
    RE = 6371.00877  # 지구 반경(km)
    GRID = 5.0
    SLAT1 = 30.0
    SLAT2 = 60.0
    OLON = 126.0
    OLAT = 38.0
    XO = 43
    YO = 136

    DEGRAD = math.pi / 180.0
    re = RE / GRID
    slat1 = SLAT1 * DEGRAD
    slat2 = SLAT2 * DEGRAD
    olon = OLON * DEGRAD
    olat = OLAT * DEGRAD

    sn = math.tan(math.pi * 0.25 + slat2 * 0.5) / math.tan(math.pi * 0.25 + slat1 * 0.5)
    sn = math.log(math.cos(slat1) / math.cos(slat2)) / math.log(sn)
    sf = math.tan(math.pi * 0.25 + slat1 * 0.5)
    sf = (math.pow(sf, sn) * math.cos(slat1)) / sn
    ro = math.tan(math.pi * 0.25 + olat * 0.5)
    ro = (re * sf) / math.pow(ro, sn)

    ra = math.tan(math.pi * 0.25 + lat * DEGRAD * 0.5)
    rra = (re * sf) / math.pow(ra, sn)
    theta = lon * DEGRAD - olon
    theta *= sn

    nx = int(rra * math.sin(theta) + XO + 0.5)
    ny = int(ro - rra * math.cos(theta) + YO + 0.5)

    return nx, ny

@app.get("/weather")
def get_weather(lat: float = Query(...), lon: float = Query(...)):
    # 1. 좌표 변환 함수 호출
    nx, ny = dfs_xy_conv(lat, lon)

    # 2. 단기예보 API 호출
    now = datetime.now()
    base_date = ""
    base_time = ""

    if now.hour < 2 :
        base_date = (now - timedelta(days=1)).strftime("%Y%m%d")
        base_time = "2300"
    else :
        base_date = now.strftime("%Y%m%d")
        base_time = "0200"
        
    weather_url = (
        f"https://apihub.kma.go.kr/api/typ02/openApi/VilageFcstInfoService_2.0/getVilageFcst"
        f"?pageNo=1&numOfRows=850&dataType=JSON&base_date={base_date}&base_time={base_time}"
        f"&nx={nx}&ny={ny}&authKey={weather_api_key}"
    )

    weather_resp = requests.get(weather_url)
    if weather_resp.status_code != 200:
        raise HTTPException(status_code=weather_resp.status_code, detail="기상청 날씨 API 호출 실패")

    return weather_resp.json()

@app.get("/locationInfo")
def receive_festival(lat: float = Query(...), lon: float = Query(...)):
    lat = round(lat, 12)
    lon = round(lon, 12)

    options = Options()
    #options.add_argument('--headless=new')
    options.add_argument('--disable-gpu')
    options.add_argument('--no-sandbox')
    options.add_argument('--disable-dev-shm-usage')
    options.add_argument('--window-size=1920,1080')

    driver = None
    
    driver = webdriver.Chrome(options=options)
    
    url = f"https://map.naver.com/v5/search/{lat},{lon}"
    print(url)
    driver.set_window_position(0, 1000)
    driver.set_window_size(800, 600)    
    driver.minimize_window()
    driver.get(url)
    # 잠깐 대기 (동적 렌더링)
    time.sleep(1.5)

    # 현재 주소창에 표시된 URL 가져오기
    current_url = driver.current_url
    print("최종 URL:", current_url)
    driver.get(current_url)
    wait = WebDriverWait(driver, 5)

    # 필요한 요소가 로드될 때까지 기다림
    try:
        wait.until(EC.presence_of_all_elements_located((By.CLASS_NAME, "end_area")))
    except TimeoutException:
        # 요소가 없으면 빈 리스트 반환
        return JSONResponse(content=[], status_code=201)

    elements = driver.find_elements(By.CLASS_NAME, 'link_space')

    img_src_list =[]
    title_list =[]

    for element in elements :
        title = element.find_element(By.CLASS_NAME, 'space_title')
        try:
            img_src = element.find_element(By.TAG_NAME, 'img').get_attribute('src')
        except NoSuchElementException:
            img_src = None  # 또는 'null', '' 등 원하는 기본값

        if img_src is not None:
            img_src_list.append(img_src)
        else :
            img_src_list.append('null')
        title_list.append(title.text)
    
    js_info = [{"title": title, "src": link} for title, link in zip(title_list, img_src_list)]
    driver.quit()
    return JSONResponse(content=js_info, status_code=200)

class WordRequest(BaseModel):
    word: str

@app.post('/checkWord')
async def check_word(req: WordRequest):
    word = req.word
    if not word :
        raise HTTPException(status_code=400, detail="빈칸은 보낼 수 없습니다")
    
    #특수문자, 숫자만 보냈을 시 검사x
    if re.match(r'^[0-9\W_]+$', word):
        return {'message': 'Success'}
    
    try:
        client = discovery.build(
        "commentanalyzer",
        "v1alpha1",
        developerKey = moderate_api_key,
        discoveryServiceUrl = "https://commentanalyzer.googleapis.com/$discovery/rest?version=v1alpha1",
        static_discovery=False,
        )

        analyze_request = {
        'comment': { 'text': word },
        'requestedAttributes': {'TOXICITY': {}}
        }

        response = client.comments().analyze(body=analyze_request).execute()

        toxicity_score = response['attributeScores']['TOXICITY']['summaryScore']['value']

        if toxicity_score >= 0.6:
            raise HTTPException(status_code=400, detail='경고: 비속어는 사용할 수 없습니다')
        return {'message': 'Success'}

    except HTTPException:
        raise
    
    except Exception as e:
        print(str(e))
        raise HTTPException(status_code=500, detail='지원하지 않는 언어입니다')