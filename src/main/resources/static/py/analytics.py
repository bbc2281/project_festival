import os
import json
from google.analytics.data_v1beta import BetaAnalyticsDataClient
from google.analytics.data_v1beta.types import DateRange, Metric, Dimension, RunReportRequest
from google.oauth2 import service_account
from pprint import pprint

# ------------------ 설정 ------------------

# JSON 키 파일 상대경로 계산
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
key_file_location = os.path.join(BASE_DIR, '..', 'google', 'meta-method-473006-t6-8a42068b9616.json')
key_file_location = os.path.abspath(key_file_location)

# GA4 Property ID (형태: 123456789)
PROPERTY_ID = "511737711" 

# ------------------ 클라이언트 생성 ------------------
credentials = service_account.Credentials.from_service_account_file(key_file_location)
client = BetaAnalyticsDataClient(credentials=credentials)

# ------------------ 리포트 요청 ------------------
request = RunReportRequest(
    property=f"properties/{PROPERTY_ID}",
    date_ranges=[DateRange(start_date="7daysAgo", end_date="today")],
    metrics=[Metric(name="active1DayUsers"), Metric(name="screenPageViews")],
    dimensions=[Dimension(name="date")]
)

response = client.run_report(request)

# ------------------ 결과 출력 ------------------
pprint(response)
# 파일 저장
data = []
for row in response.rows:
    record = {}
    for dim, dim_value in zip(response.dimension_headers, row.dimension_values):
        record[dim.name] = dim_value.value
    for met, met_value in zip(response.metric_headers, row.metric_values):
        record[met.name] = met_value.value
    data.append(record)

# JSON 저장
with open('src/main/resources/static/js/analytics.json', 'w', encoding='utf-8') as f:
    json.dump(data, f, ensure_ascii=False, indent=2)