import requests
from bs4 import BeautifulSoup
import json

query = '서울 축제'
url = f'https://search.naver.com/search.naver?ssc=tab.news.all&where=news&sm=tab_jum&query={query}'

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36'
}

response = requests.get(url, headers=headers)

soup = BeautifulSoup(response.content, 'html.parser')
# 발표당일 클래스명 확인(확인위치 최하단 a링크)
# elements = soup.select('.g142CmJWlznnvvbvmk68')
elements = soup.select('.VVZqvAlvnADQu8BVMc2n')

print(elements)

news_titles = []
news_links = []

for element in elements:
    title = element.text
    link = element['href']
    news_titles.append(title)
    news_links.append(link)

# JSON 형식으로 변환
js_news = [{"title": title, "url": link} for title, link in zip(news_titles, news_links)]

# 파일 저장
with open('src/main/resources/static/js/news.json', 'w', encoding='utf-8') as f:
    json.dump(js_news, f, ensure_ascii=False, indent=2)
