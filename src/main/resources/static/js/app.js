
// Dataset (mock)
const FESTIVALS = [
  {id:'fes1', name:'부산 불꽃축제', category:'공연', region:'부산', city:'수영구', 
   begin:'2025-10-10', end:'2025-10-12', fee:0, host:'부산광역시', 
   img:'https://images.unsplash.com/photo-1506104489822-562ca25152fe?q=80&w=1600&auto=format&fit=crop',
   info:'광안리 해변에서 펼쳐지는 초대형 불꽃 공연! 드론쇼와 음악과 함께하는 화려한 라이트쇼.',
   address:'부산 수영구 광안해변로', lat:35.153, lng:129.118, like:137
  },
  {id:'fes2', name:'진주 남강유등축제', category:'전통', region:'경남', city:'진주시',
   begin:'2025-10-01', end:'2025-10-14', fee:5000, host:'진주시', 
   img:'https://images.unsplash.com/photo-1541542684-4a66f114f297?q=80&w=1600&auto=format&fit=crop',
   info:'남강을 수놓는 수천 개의 유등. 야간 산책로와 포토존 운영.',
   address:'경남 진주시 남강로', lat:35.181, lng:128.108, like:92
  },
  {id:'fes3', name:'서울 불빛정원', category:'전시', region:'서울', city:'종로구',
   begin:'2025-12-05', end:'2026-01-10', fee:12000, host:'서울시', 
   img:'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?q=80&w=1600&auto=format&fit=crop',
   info:'도심 속 겨울 라이트업 & 마켓. 따뜻한 먹거리와 체험 부스.',
   address:'서울 종로구 세종대로', lat:37.572, lng:126.976, like:221
  },
  {id:'fes4', name:'강릉 커피축제', category:'푸드', region:'강원', city:'강릉시',
   begin:'2025-10-18', end:'2025-10-20', fee:0, host:'강릉시',
   img:'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?q=80&w=1600&auto=format&fit=crop',
   info:'스페셜티 커피 시음, 라떼아트 대회, 로스터 투어.',
   address:'강원 강릉시 경강로', lat:37.751, lng:128.892, like:64
  },
  {id:'fes5', name:'부여 서동연꽃축제', category:'자연', region:'충남', city:'부여군',
   begin:'2025-07-05', end:'2025-07-21', fee:2000, host:'부여군',
   img:'https://images.unsplash.com/photo-1501004318641-b39e6451bec6?q=80&w=1600&auto=format&fit=crop',
   info:'백제문화단지와 연계한 포토존, 야간 연꽃 라이트업.',
   address:'충남 부여군 궁남지', lat:36.275, lng:126.911, like:51
  },
  // ✅ 추가된 6번째 축제
  {id:'fes6', name:'제주 불꽃음악제', category:'공연', region:'제주', city:'제주시',
   begin:'2025-11-02', end:'2025-11-04', fee:10000, host:'제주시청',
   img:'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?q=80&w=1600&auto=format&fit=crop',
   info:'바다와 음악이 어우러지는 제주 해변 불꽃음악제. 유명 뮤지션과 함께하는 3일간의 축제.',
   address:'제주특별자치도 제주시 탑동해안로', lat:33.511, lng:126.520, like:78
  }
];

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
  FESTIVALS.slice(0,3).forEach((f,i)=>{
    const item = document.createElement('div');
    item.className = `carousel-item ${i===0?'active':''}`;
    item.innerHTML = `<img class="d-block w-100 object-fit-cover" style="height:420px" src="${f.img}">
    <div class="carousel-caption text-start bg-dark bg-opacity-50 rounded-3 p-3">
      <h5>${f.name}</h5>
      <p class="mb-0">${f.info}</p>
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
function festivalBadge(f){
  const isFree = f.fee === 0;
  return `<span class="badge ${isFree?'badge-free':'badge-paid'}">${isFree?'무료':'유료'}</span>`;
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
      <div class="mt-2">${festivalBadge(f)}</div>
      <div class="mt-auto d-flex justify-content-between align-items-center">
        <a href="festival.html?id=${f.id}" class="btn btn-outline-primary btn-sm">자세히</a>
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
function renderFestivalDetail(){
  const url = new URL(location.href);
  const id = url.searchParams.get('id');
  const f = FESTIVALS.find(x=>x.id===id) || FESTIVALS[0];
  const root = qs('#festivalDetail');
  root.innerHTML = `
  <div class="row g-4">
    <div class="col-lg-7">
      <img class="rounded-4 shadow w-100 object-fit-cover" src="${f.img}" style="height:380px">
    </div>
    <div class="col-lg-5">
      <h3 class="fw-bold">${f.name}</h3>
      <div class="text-secondary mb-2">${f.region} · ${f.city}</div>
      <div class="mb-1">기간: ${f.begin} ~ ${f.end}</div>
      <div class="mb-1">요금: ${f.fee===0?'무료':'₩'+f.fee.toLocaleString()}</div>
      <div class="mb-1">주최: ${f.host}</div>
      <div class="mb-2">주소: ${f.address}</div>
      <div class="mb-3">${festivalBadge(f)}</div>
      <div class="d-flex gap-2">
        <a class="btn btn-primary" href="https://map.naver.com/p/search/${encodeURIComponent(f.address)}" target="_blank">네이버 길찾기</a>
        <a class="btn btn-outline-secondary" href="search.html">목록으로</a>
      </div>
      <div class="alert alert-light border mt-3">💬 이 축제의 전용 채팅방은 1개로 고정됩니다. (모의)</div>
    </div>
  </div>
  <div class="mt-4">
    <h5 class="mb-3">상세 소개</h5>
    <p>${f.info}</p>
  </div>
  <div class="mt-4">
    <h5 class="mb-3">리뷰</h5>
    <div class="vstack gap-2" id="reviews">
      <div class="border rounded p-3"><b>익명</b> · 즐거웠어요! 야간 조명이 특히 예뻤어요.</div>
    </div>
  </div>`;
}

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
