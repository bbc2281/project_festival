document.addEventListener('DOMContentLoaded', () => {
    // 로그인 폼 및 입력 필드
    const authForm = document.getElementById('auth-form');
    const userIdInput = document.getElementById('member_id');
    const userPassInput = document.getElementById('member_pass');

    // 에러 메시지 표시용
    const errorMessageContainer = document.createElement('p');
    errorMessageContainer.className = 'text-danger mt-2';
    if (authForm) authForm.prepend(errorMessageContainer);

    // 폼이 없으면 스크립트 실행 중단
    if (!authForm) return;

    // CSRF 토큰 설정
    const csrfInput = document.querySelector('input[name="_csrf"]');
    const csrfToken = csrfInput ? csrfInput.value : null;
    const csrfHeader = 'X-CSRF-TOKEN'; 

    authForm.addEventListener('submit', async (e) => {
        e.preventDefault(); // 기본 폼 제출 막기

        // 입력값 가져오기
        const member_id = userIdInput.value.trim();
        const member_pass = userPassInput.value.trim();

        // 입력값 검증
        if (!member_id || !member_pass) {
            errorMessageContainer.textContent = '아이디와 비밀번호를 모두 입력해주세요.';
            return;
        }

        errorMessageContainer.textContent = ''; // 에러 메시지 초기화

        // API 호출을 위한 헤더 설정
        const headers = { 'Content-Type': 'application/json' };
        if (csrfToken) headers[csrfHeader] = csrfToken;

        try {
            // 로그인 REST API 호출
            const res = await fetch('/api/v1/auth/login', {
                
                 method: 'POST',
                  headers: headers,
                  body: JSON.stringify({ member_id, member_pass })
            });
            console.log('서버 응답 상태 코드:', res.status);
            // 🚨 응답을 JSON으로 파싱. JSON이 아니면 자동으로 catch 블록으로 이동.

            const data = await res.json(); 

        if (data.success) {
            // 로그인 성공
            alert(data.message || '로그인에 성공했습니다.');
            window.location.href = '/';
        } else {
            // 로그인 실패
            errorMessageContainer.textContent = data.message || `로그인에 실패했습니다. (상태 코드: ${res.status})`;
            userPassInput.value = '';
        }
            
          
        } catch (error) {
            // 네트워크 오류 (서버 접속 불가) 또는 JSON 파싱 오류 (HTML 응답을 받은 경우) 처리
            
            console.error('로그인 중 치명적인 오류 발생:', error);
            errorMessageContainer.textContent = '로그인 서버와 통신할 수 없습니다. 네트워크 연결을 확인하세요.';
            userPassInput.value = '';
            
        }
    });
});