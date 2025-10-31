const $ = sel => document.querySelector(sel);
const $$ = sel => Array.from(document.querySelectorAll(sel));

function openModal(id){ const el = $(id); if(!el) return; el.classList.add('show'); }
function closeModal(id){ const el = $(id); if(!el) return; el.classList.remove('show'); }

window.addEventListener('DOMContentLoaded', () => {
  // 회원유형 선택 모달
  $$('#open-signup').forEach(btn => btn.addEventListener('click', () => openModal('#signupModal')));
  $$('#close-signup').forEach(btn => btn.addEventListener('click', () => closeModal('#signupModal')));

  // 페이지 전환
  const go = (path) => { window.location.href = path; };
  $('#opt-user')?.addEventListener('click', ()=> go('signup-user.html'));
  $('#opt-company')?.addEventListener('click', ()=> go('signup-company.html'));

  // 폼 제출 방지 (데모용)
  $$('#auth-form').forEach(form => form.addEventListener('submit', e => e.preventDefault()));

  // 비밀번호 일치 확인
  const checkPw = (a,b,help)=>{
    if(!a||!b||!help) return;
    const f=()=>{
      if(!b.value){help.textContent='영문/숫자/특수문자 8~20자 권장';help.style.color='';return;}
      help.textContent=(a.value===b.value)?'비밀번호가 일치합니다':'비밀번호가 일치하지 않습니다';
      help.style.color=(a.value===b.value)?'#10b981':'#ef4444';
    };
    a.addEventListener('input',f); b.addEventListener('input',f);
  };
  checkPw($('#member_pass'),$('#member_pass2'),$('#pw-help'));
  checkPw($('#company_pass'),$('#company_pass2'),$('#pw-help'));

  // 중복확인 버튼
  $$('.check-btn').forEach(btn=>{
    btn.style.display='inline-flex';
    btn.style.alignItems='center';
    btn.style.justifyContent='center';
    btn.style.whiteSpace='nowrap';
    btn.addEventListener('click',()=>{
      btn.disabled=true;
      btn.textContent='사용 가능';
      btn.classList.remove('btn-secondary');
      btn.classList.add('btn-primary');
      setTimeout(()=>{
        btn.disabled=false;
        btn.textContent='중복확인';
        btn.classList.remove('btn-primary');
        btn.classList.add('btn-secondary');
      },1200);
    });
  });

  // ✅ 마이페이지 버튼 이동
  $$('.btn-accent').forEach(btn=>{
    if(btn.textContent.includes('마이페이지')){
      btn.addEventListener('click',()=>window.location.href='mypage.html');
    }
  });
});

// SNS 로그인
function socialLogin(provider) {
  switch(provider) {
    case 'kakao': window.location.href='https://kauth.kakao.com/oauth/...'; break;
    case 'google': window.location.href='https://accounts.google.com/o/oauth2/...'; break;
    case 'naver': window.location.href='https://nid.naver.com/oauth2.0/...'; break;
  }
}
function socialSignup(provider){ socialLogin(provider); }


