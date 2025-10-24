const $ = sel => document.querySelector(sel);
const $$ = sel => Array.from(document.querySelectorAll(sel));

window.addEventListener('DOMContentLoaded', () => {
    
    // 페이지 전환 로직
    const go = (path) => { window.location.href = path; };

    // 일반회원 옵션 카드 클릭 시 이동
    // '/auth/memberjoin' 경로로 이동
    $('#opt-user')?.addEventListener('click', (e) => {
        // 혹시 모를 a 태그 등 기본 동작 방지 (option-card는 div지만 안전을 위해)
        e.preventDefault(); 
        go('/auth/memberjoin'); 
    });

    // 기업회원 옵션 카드 클릭 시 이동
    // '/auth/companyjoin' 경로로 이동
    $('#opt-company')?.addEventListener('click', (e) => {
        e.preventDefault();
        go('/auth/companyjoin');
    });

    
});
