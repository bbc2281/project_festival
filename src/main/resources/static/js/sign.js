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

  // ✅ 폼 제출 (동작만 막고 별도 알림 없음)
  $$('#auth-form').forEach(form => {
    form.addEventListener('submit', e => {
      e.preventDefault();
      // fetch() 또는 백엔드 로직 연결 시 이 부분에 추가하면 됨
    });
  });

  // ✅ 비밀번호 확인 (일반회원)
  const userPw = $('#member_pass');
  const userPw2 = $('#member_pass2');
  const pwHelp = $('#pw-help');
  if (userPw && userPw2 && pwHelp) {
    const check = () => {
      const a = userPw.value;
      const b = userPw2.value;
      if (!b) { pwHelp.textContent = '영문/숫자/특수문자 8~20자 권장'; pwHelp.style.color = ''; return; }
      if (a !== b) { pwHelp.textContent = '비밀번호가 일치하지 않습니다'; pwHelp.style.color = '#ef4444'; }
      else { pwHelp.textContent = '비밀번호가 일치합니다'; pwHelp.style.color = '#10b981'; }
    };
    userPw.addEventListener('input', check);
    userPw2.addEventListener('input', check);
  }

  // ✅ 비밀번호 확인 (기업회원)
  const companyPw = $('company_pass');
  const companyPw2 = $('company_pass2');
  const help2 = $('#pw-help');
  if (companyPw && companyPw2 && help2) {
    const check2 = () => {
      const a = companyPw.value;
      const b = companyPw2.value;
      if (!b) { help2.textContent = '영문/숫자/특수문자 8~20자 권장'; help2.style.color = ''; return; }
      if (a !== b) { help2.textContent = '비밀번호가 일치하지 않습니다'; help2.style.color = '#ef4444'; }
      else { help2.textContent = '비밀번호가 일치합니다'; help2.style.color = '#10b981'; }
    };
    companyPw.addEventListener('input', check2);
    companyPw2.addEventListener('input', check2);
  }

  // ✅ 중복확인 버튼 (한 줄 정렬 포함)
document.querySelectorAll('.check-btn').forEach(btn => {
  btn.style.display = 'inline-flex';
  btn.style.alignItems = 'center';
  btn.style.justifyContent = 'center';
  btn.style.whiteSpace = 'nowrap';

  btn.addEventListener('click', () => {
    btn.classList.remove('btn-secondary');
    btn.classList.add('btn-primary');
    btn.textContent = '사용 가능';
    btn.disabled = true;
    setTimeout(() => {
      btn.disabled = false;
      btn.textContent = '중복확인';
      btn.classList.remove('btn-primary');
      btn.classList.add('btn-secondary');
    }, 1200);
  });
});

});

// ✅ SNS 로그인 / 회원가입 기능
function socialLogin(provider) {
  switch(provider) {
    case 'kakao':
      window.location.href = 'https://kauth.kakao.com/oauth/authorize?client_id=YOUR_KAKAO_CLIENT_ID&redirect_uri=http://localhost:8080/auth/kakao/callback&response_type=code';
      break;
    case 'google':
      window.location.href = 'https://accounts.google.com/o/oauth2/v2/auth?client_id=YOUR_GOOGLE_CLIENT_ID&redirect_uri=http://localhost:8080/auth/google/callback&response_type=code&scope=email profile';
      break;
    case 'naver':
      window.location.href = 'https://nid.naver.com/oauth2.0/authorize?client_id=YOUR_NAVER_CLIENT_ID&redirect_uri=http://localhost:8080/auth/naver/callback&response_type=code';
      break;
  }
}
function socialSignup(provider) { socialLogin(provider); }
