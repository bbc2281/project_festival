import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys



driver = webdriver.Chrome() #크롬드라이버 객체 생성(크롬 브라우저 제어)

driver.get('https://search.naver.com/search.naver?ssc=tab.news.all&where=news&sm=tab_jum&query=%EC%84%9C%EC%9A%B8+%EC%B6%95%EC%A0%9C')

elements = driver.find_elements(By.CLASS_NAME, 'g142CmJWlznnvvbvmk68')

news_titles =[]
news_links= []


for news in elements :
    news_titles.append(news.text) #뉴스 소제목
    news_links.append(news.get_attribute('href')) #뉴스 링크
    
# 스크립트에서 사용하기위해 js스타일로 변환
js_news = 'const NEWS = [\n' + ',\n'.join(
    f'  {{ title: "{title}", url: "{link}" }}' for title, link in zip(news_titles, news_links)
) + '\n];'

# print(js_news)

with open('js/test.js', 'w', encoding='utf-8') as f:
    f.write(js_news)


