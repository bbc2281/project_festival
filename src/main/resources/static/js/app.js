 window.logout = async function() {
        try {
            const csrfInput = document.querySelector('input[name="_csrf"]');
            const csrfToken = csrfInput ? csrfInput.value : null;
            const csrfHeader = 'X-CSRF-TOKEN'; 
            
            // API 호출을 위한 헤더 설정 (로그인 로직과 동일)
            const headers = { 'Content-Type': 'application/json' };
            if (csrfToken) headers[csrfHeader] = csrfToken;

            // 로그아웃 REST API 호출
            const res = await fetch('/api/v1/auth/logout', {
                method: 'POST',
                headers: headers,
                // 로그아웃은 body가 필요하지 않습니다.
            });

            console.log('로그아웃 서버 응답 상태 코드:', res.status);
            const data = await res.json();

            if (data.success) {
                alert(data.message || '로그아웃 성공!');
                window.location.href = '/';
            } else {
                alert('로그아웃 실패: ' + (data.message || '서버 오류'));
            }

        } catch (error) {
            console.error('로그아웃 중 오류 발생:', error);
            alert('네트워크 오류로 로그아웃에 실패했습니다.');
        }
    }



// Dataset (mock)
const FESTIVALS = [ ];
const NOTICES = [ ];

document.addEventListener("DOMContentLoaded", function(){
  fetch("/api/festivals")
  .then(res => res.json())
  .then(festivals =>{
    festivals.forEach(festival =>{
      
      const formattedFestival = {
        id: festival.festival_idx,
        name: festival.festival_name || '이름 없음',
        category: festival.festival_category_name || '기타',
        region: '서울',
        city: festival.region_name || '',
        begin: festival.festival_begin_date || '',   // 날짜 가공이 필요할 수 있음
        end: festival.festival_end_date || '',     // 종료일이 없을 경우 빈값
        fee: festival.festival_fee,     // 요금 정보 없을 경우 기본값
        host: festival.festival_host || '서울시',
        img: festival.festival_img_path || 'https://via.placeholder.com/400x300?text=No+Image',
        info: festival.festival_info || '장소 정보 없음',
        address: festival.festival_address || '',
        lat: parseFloat(festival.LAT) || 0,
        lng: parseFloat(festival.LOT) || 0,
        like: festival.festival_like || 0,
      };
      
      FESTIVALS.push(formattedFestival);
      
    })
    if (qs('#heroInner')) renderHome();
    if (qs('#resultGrid')) initSearchPage();
    if (qs('#festivalDetail')) renderFestivalDetail();
    if (qs('#postList')) renderBoard();
    // bindAuthForms();
  })
  .catch(err =>{
    console.log("축제정보 호출 오류",err);
  });

  // 홈화면 공지사항 호출
  fetch("/index/board")
  .then(response => response.json())
  .then(BoardList =>{
    BoardList.forEach(board => {
      const board_date = new Date(board.board_regDate)
      formattedDate = board_date.toISOString().split('T')[0]
      const formattedBoard = {
        title : board.board_title,
        date :  formattedDate,
        cat : board.board_category
      };
      // 공지사항 추가
      NOTICES.push(formattedBoard);
    })
  })
  .catch(err =>{
    console.log("공지사항 호출 오류",err);
  });
});





// Utility
const qs = (s,doc=document)=>doc.querySelector(s);
const qsa = (s,doc=document)=>Array.from(doc.querySelectorAll(s));


function renderHome() {
  const heroInner = document.querySelector('#heroInner');
  heroInner.innerHTML = ''; // 기존 내용 초기화

  const itemsPerSlide = 3; // 한 슬라이드에 3개씩 표시
  const totalSlides = Math.ceil(FESTIVALS.length / itemsPerSlide);

  for (let i = 0; i < totalSlides; i++) {
    const item = document.createElement('div');
    item.className = `carousel-item ${i === 0 ? 'active' : ''}`;

    // 내부 슬라이드(이미지 3개) 묶음
    const innerRow = document.createElement('div');
    innerRow.className = 'd-flex justify-content-center gap-3 p-3';

    FESTIVALS.slice(i * itemsPerSlide, (i + 1) * itemsPerSlide).forEach(f => {
      const card = document.createElement('div');
      card.className = 'flex-fill text-center';
      card.innerHTML = `
        <div class="rounded-4 overflow-hidden shadow-sm bg-dark bg-opacity-25">
          <img src="${f.img}" class="w-100 object-fit-cover" style="height:280px; border-radius: 12px;">
          <div class="p-2 text-white fw-semibold">${f.name}</div>
        </div>
      `;
      innerRow.appendChild(card);
    });

    item.appendChild(innerRow);
    heroInner.appendChild(item);
  }


  // Notices
  const ul = qs('#noticeList');
  NOTICES.slice(0,6).forEach(n=>{
    const li = document.createElement('li');
    li.className='list-group-item d-flex justify-content-between align-items-center';
    li.innerHTML = `<span>[${n.cat}]</span><span>${n.title}</span><span class="text-secondary small">${n.date}</span>`;
    ul.appendChild(li);
  });

  // News
  const newsWrap = qs('#newsList');
  fetch('/js/news.json')
  .then(response => response.json())
  .then(newsList => {
    newsList.slice(0,5).forEach(n =>{
      const a = document.createElement('a');
      a.href = n.url; a.target="_blank";
      a.className='d-flex align-items-center gap-2 news-item';
      a.innerHTML = `<span>🗞️</span><span>${n.title}</span>`;
      newsWrap.appendChild(a);
    })
  })
  .catch(err => {
    console.log("뉴스정보 호출 오류",err)
  });
  // ✅ Recommended festivals (6개로 표시)
  const grid = qs('#homeGrid');
  FESTIVALS.slice(0,6).forEach(f=>grid.appendChild(festivalCard(f)));
}


function festivalCard(f){
  const col = document.createElement('div');
  col.className='col-12 col-sm-6 col-lg-4';
  col.innerHTML = `<div class="card h-100 shadow-sm card-fes">
    <img src="${f.img}" class="card-img-top" style="height:170px; object-fit:cover;">
    <div class="card-body d-flex flex-column">
      <h6 class="card-title">${f.name}</h6>
      <div class="text-secondary small">${f.region} · ${f.city}</div>
      <div class="small mt-1">${f.begin} ~ ${f.end}</div>
      <div class="mt-2">${f.fee}</div>
      <div class="mt-auto d-flex justify-content-between align-items-center">
        <a href="festivalInfo?id=${f.id}" class="btn btn-outline-primary btn-sm">자세히</a>
        <span class="text-secondary small">❤️ ${f.like}</span>
      </div>
    </div>
  </div>`;
  return col;
}

// Search page
function initSearchPage(){
  // Populate selects
  const cats = [...new Set(FESTIVALS.map(f=>f.category))].sort((a, b) => {
    if (a === "기타") return 1;      // a가 "기타"면 뒤로
    if (b === "기타") return -1;     // b가 "기타"면 a가 앞에
    return a.localeCompare(b, 'ko-KR'); // 가나다순 정렬 기타 맨뒤로
  });
  const regions = [...new Set(FESTIVALS.map(f => f.city))]
  .sort((a, b) => {
    if (a === "기타") return 1;
    if (b === "기타") return -1;   
    return a.localeCompare(b, 'ko-KR'); 
  });
  
  fillOptions(qs('#cat'), ['전체', ...cats]);
  fillOptions(qs('#region'), ['전체', ...regions]);

  qs('#btnSearch').addEventListener('click', ()=>renderSearch(1));
  qs('#btnReset').addEventListener('click', ()=>{
    qs('#q').value=''; qs('#cat').selectedIndex=0; qs('#region').selectedIndex=0;
    qs('#from').value=''; qs('#to').value=''; qs('#free').checked=false;
    renderSearch(1);
  });
  qs('#sort').addEventListener('change', ()=>renderSearch(1));
  renderSearch(1);
  
}

function fillOptions(sel, arr){
  sel.innerHTML = arr.map(v=>`<option value="${v}">${v}</option>`).join('');
}

function applyFilters(list){
  const q = qs('#q').value.trim().toLowerCase();
  const cat = qs('#cat').value;
  const region = qs('#region').value;
  const from = qs('#from').value;
  const isFree = qs('#free').checked;
  
  return list.filter(f=>{
    if (q && !(f.name.toLowerCase().includes(q) || f.city.toLowerCase().includes(q) || f.region.toLowerCase().includes(q))) return false;
    if (cat && cat!=='전체' && f.category!==cat) return false;
    if (region && region!=='전체' && f.city!==region) return false;
    if (isFree && f.fee!=='무료') return false;
    if (from && f.end < from) return false; // festival ends before range
    return true;
  });
}

function sortList(list){
  const type = qs('#sort').value;
  const now = new Date().toISOString().slice(0,10);
  if (type==='new') return list.slice().sort((a,b)=>b.begin.localeCompare(a.begin));
  if (type==='near') return list.slice().sort((a,b)=>Math.abs(a.begin.localeCompare(now)) - Math.abs(b.begin.localeCompare(now)));
  return list.slice().sort((a,b)=>b.like-a.like); // recommended by like
}


function renderSearch(page=1){
  const PAGE = 6;
  const filtered = sortList(applyFilters(FESTIVALS));
  qs('#count').textContent = filtered.length;
  const start = (page-1)*PAGE;
  const items = filtered.slice(start, start+PAGE);
  const grid = qs('#resultGrid'); grid.innerHTML='';
  items.forEach(f=>grid.appendChild(festivalCard(f)));
  renderPager(filtered.length, PAGE, page);
}

function renderPager(total, size, page) {
  const pages = Math.ceil(total / size) || 1;
  const ul = document.querySelector('#pager');
  ul.innerHTML = '';

  const groupSize = 10;
  const currentGroup = Math.ceil(page / groupSize);
  const startPage = (currentGroup - 1) * groupSize + 1;
  const endPage = Math.min(startPage + groupSize - 1, pages);

  // 이전 버튼
  if (startPage > 1) {
    const prevLi = document.createElement('li');
    prevLi.className = 'page-item';
    prevLi.innerHTML = `<a class="page-link" href="#">«</a>`;
    prevLi.addEventListener('click', (e) => {
      e.preventDefault();
      renderSearch(startPage - 1);
    });
    ul.appendChild(prevLi);
  }

  // 페이지 번호
  for (let p = startPage; p <= endPage; p++) {
    const li = document.createElement('li');
    li.className = `page-item ${p === page ? 'active' : ''}`;
    li.innerHTML = `<a class="page-link" href="#">${p}</a>`;
    li.addEventListener('click', (e) => {
      e.preventDefault();
      renderSearch(p);
    });
    ul.appendChild(li);
  }

  // 다음 버튼
  if (endPage < pages) {
    const nextLi = document.createElement('li');
    nextLi.className = 'page-item';
    nextLi.innerHTML = `<a class="page-link" href="#">»</a>`;
    nextLi.addEventListener('click', (e) => {
      e.preventDefault();
      renderSearch(endPage + 1);
    });
    ul.appendChild(nextLi);
  }
}


