from fastapi import FastAPI, Query, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import requests
from datetime import datetime, timedelta
import math


app = FastAPI()

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

API_KEY = "JWVjDL8GTl2lYwy_Br5d0Q"

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
        f"&nx={nx}&ny={ny}&authKey={API_KEY}"
    )

    weather_resp = requests.get(weather_url)
    if weather_resp.status_code != 200:
        raise HTTPException(status_code=weather_resp.status_code, detail="기상청 날씨 API 호출 실패")

    return weather_resp.json()
