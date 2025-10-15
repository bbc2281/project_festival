
// Dataset (mock)
const FESTIVALS = [ ];

document.addEventListener("DOMContentLoaded", function(){
  fetch("/api/festivals")
  .then(res => res.json())
  .then(festivals =>{
    festivals.forEach(festival =>{
      console.log(festival)
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
        like: 0,
      };
      FESTIVALS.push(formattedFestival);
      console.log(formattedFestival);
    })
    if (qs('#heroInner')) renderHome();
  })
  .catch(err =>{
    console.log(err);
  })

});


const NOTICES = [
  {title:"서버 점검 안내", date:"2025-10-05"},
  {title:"가을 축제 이벤트 당첨자 발표", date:"2025-10-01"},
  {title:"사이트 정식 오픈 공지", date:"2025-09-20"}
];

const NEWS = [
  {title:"드론쇼와 합작으로 볼거리 확대", url:"#"},
  {title:"유등축제 야간 교통 통제 안내", url:"#"},
  {title:"불빛정원 사전 예매 오픈", url:"#"},
  {title:"지역 상권과 함께하는 먹거리 축제", url:"#"}
];

// Utility
const qs = (s,doc=document)=>doc.querySelector(s);
const qsa = (s,doc=document)=>Array.from(doc.querySelectorAll(s));

// Render functions for each page
document.addEventListener('DOMContentLoaded', ()=>{
  if (qs('#heroInner')) renderHome();
  if (qs('#resultGrid')) initSearchPage();
  if (qs('#festivalDetail')) renderFestivalDetail();
  if (qs('#postList')) renderBoard();
  bindAuthForms();
});

function renderHome(){
  // Hero slides
  const heroInner = qs('#heroInner');
  FESTIVALS.slice(0,6).forEach((f,i)=>{
    const item = document.createElement('div');
    item.className = `carousel-item ${i===0?'active':''}`;
    item.innerHTML = `<img class="d-block w-100 object-fit-cover" style="height:420px" src="${f.img}">
    <div class="carousel-caption text-start bg-dark bg-opacity-50 rounded-3 p-3">
      <h5>${f.name}</h5>
    </div>`;
    heroInner.appendChild(item);
  });

  // Notices
  const ul = qs('#noticeList');
  NOTICES.forEach(n=>{
    const li = document.createElement('li');
    li.className='list-group-item d-flex justify-content-between align-items-center';
    li.innerHTML = `<span>${n.title}</span><span class="text-secondary small">${n.date}</span>`;
    ul.appendChild(li);
  });

  // News
  const newsWrap = qs('#newsList');
  NEWS.forEach(n=>{
    const a = document.createElement('a');
    a.href = n.url; a.target="_blank";
    a.className='d-flex align-items-center gap-2 news-item';
    a.innerHTML = `<span>🗞️</span><span>${n.title}</span>`;
    newsWrap.appendChild(a);
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
  const cats = [...new Set(FESTIVALS.map(f=>f.category))];
  const regions = [...new Set(FESTIVALS.map(f=>f.region))];
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
  const to = qs('#to').value;
  const isFree = qs('#free').checked;

  return list.filter(f=>{
    if (q && !(f.name.toLowerCase().includes(q) || f.city.toLowerCase().includes(q) || f.region.toLowerCase().includes(q))) return false;
    if (cat && cat!=='전체' && f.category!==cat) return false;
    if (region && region!=='전체' && f.region!==region) return false;
    if (isFree && f.fee!==0) return false;
    if (from && f.end < from) return false; // festival ends before range
    if (to && f.begin > to) return false;   // festival begins after range
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

function renderPager(total, size, page){
  const pages = Math.ceil(total/size)||1;
  const ul = qs('#pager'); ul.innerHTML='';
  for (let p=1;p<=pages;p++){
    const li = document.createElement('li');
    li.className = `page-item ${p===page?'active':''}`;
    li.innerHTML = `<a class="page-link" href="#">${p}</a>`;
    li.addEventListener('click', (e)=>{e.preventDefault(); renderSearch(p);});
    ul.appendChild(li);
  }
}

// Festival detail page

// function renderFestivalDetail(){
//   const url = new URL(location.href);
//   const id = url.searchParams.get('id');
//   const f = FESTIVALS.find(x=>x.id===id) || FESTIVALS[0];
//   const root = qs('#festivalDetail');
//   root.innerHTML = `
//   <div class="row g-4">
//     <div class="col-lg-7">
//       <img class="rounded-4 shadow w-100 object-fit-cover" src="${f.img}" style="height:380px">
//     </div>
//     <div class="col-lg-5">
//       <h3 class="fw-bold">${f.name}</h3>
//       <div class="text-secondary mb-2">${f.region} · ${f.city}</div>
//       <div class="mb-1">기간: ${f.begin} ~ ${f.end}</div>
//       <div class="mb-1">요금: ${f.fee===0?'무료':'₩'+f.fee.toLocaleString()}</div>
//       <div class="mb-1">주최: ${f.host}</div>
//       <div class="mb-2">주소: ${f.address}</div>
//       <div class="mb-3">${festivalBadge(f)}</div>
//       <div class="d-flex gap-2">
//         <a class="btn btn-primary" href="https://map.naver.com/p/search/${encodeURIComponent(f.address)}" target="_blank">네이버 길찾기</a>
//         <a class="btn btn-outline-secondary" href="search.html">목록으로</a>
//       </div>
//       <div class="alert alert-light border mt-3">💬 이 축제의 전용 채팅방은 1개로 고정됩니다. (모의)</div>
//     </div>
//   </div>
//   <div class="mt-4">
//     <h5 class="mb-3">상세 소개</h5>
//     <p>${f.info}</p>
//   </div>
//   <div class="mt-4">
//     <h5 class="mb-3">리뷰</h5>
//     <div class="vstack gap-2" id="reviews">
//       <div class="border rounded p-3"><b>익명</b> · 즐거웠어요! 야간 조명이 특히 예뻤어요.</div>
//     </div>
//   </div>`;
// }

// Board page content
function renderBoard(){
  const list = qs('#postList');
  const posts = [
    {title:'서버 점검 안내', author:'관리자', date:'2025-10-05'},
    {title:'가을 축제 이벤트 당첨자 발표', author:'운영팀', date:'2025-10-01'},
    {title:'사이트 정식 오픈 공지', author:'관리자', date:'2025-09-20'},
  ];
  posts.forEach(p=>{
    const li = document.createElement('li');
    li.className = 'list-group-item d-flex justify-content-between align-items-center';
    li.innerHTML = `<div><a href="#" class="text-decoration-none">${p.title}</a><div class="small text-secondary">by ${p.author}</div></div><div class="small text-secondary">${p.date}</div>`;
    list.appendChild(li);
  });
}

// Forms (mock submit)
function bindAuthForms(){
  ['loginForm','userSignup','companySignup'].forEach(id=>{
    const form = document.getElementById(id);
    if (!form) return;
    form.addEventListener('submit', (e)=>{
      e.preventDefault();
      alert('제출되었습니다. (데모)');
    });
  });
}
