fetch('/js/analytics.json')
  .then(res => res.json())
  .then(async res => {
    const tbody = document.querySelector('.summary-table tbody');
    tbody.innerHTML = '';

    const labels = [];
    const visitorData = [];

    for (const element of res) {
      const date =
        element.date.slice(0, 4) +
        '-' +
        element.date.slice(4, 6) +
        '-' +
        element.date.slice(6);

      // 날짜별 게시글/댓글 기본값 0
      let countBoard = 0;
      let countReview = 0;

      try {
        const response = await fetch(`/admin/analytics?date=${date}`);
        if (response.ok) {
          const data = await response.json();
          countBoard = data.boardCount ?? 0;
          countReview = data.reviewCount ?? 0;
        } else {
          console.warn(`날짜별 조회 실패: ${response.status}`);
        }
      } catch (err) {
        console.error('날짜별 게시글 조회 실패', err);
      }

      // 테이블 행 추가
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${date}</td>
        <td>${element.active1DayUsers}</td>
        <td>${element.screenPageViews}</td>
        <td>${countBoard}</td>
        <td>${countReview}</td>
      `;
      tbody.appendChild(tr);

      // 그래프용 데이터 추가
      labels.push(date.slice(5)); // "MM-DD" 형식
      visitorData.push(element.screenPageViews);
    }//for문 종료

    // ✅ Chart.js 그래프 생성
    const ctx = document.getElementById('visitorChart');
    new Chart(ctx, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: '방문자 수',
          data: visitorData,
          borderColor: '#ff89b5',
          backgroundColor: 'rgba(255,137,181,0.3)',
          fill: true,
          tension: 0.3,
          pointRadius: 5
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false }
        },
        scales: {
          x: { grid: { display: false } },
          y: {
            ticks: { stepSize: 5 },
            beginAtZero: true
          }
        }
      }
    });
  })
  .catch(err => {
    console.error('방문자 조회 json 로드 실패', err);
  });