fetch('/js/analytics.json')
  .then(res => res.json())
  .then(async res => {
    const tbody = document.querySelector('.summary-table tbody');
    tbody.innerHTML = '';

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
          const data = await response.json(); // 서버에서 JSON으로 응답해야 함
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
    }
  })
  .catch(err => {
    console.error('방문자 조회 json 로드 실패', err);
  });