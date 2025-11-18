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




 document.addEventListener('DOMContentLoaded', () => {
        // 사이드바 활성화 로직 (기존 코드)
        const links = document.querySelectorAll('.menu a');
        const currentPath = window.location.pathname.split("/").pop();
        links.forEach(link => {
            const href = link.getAttribute('href').split("/").pop();
            if (currentPath === href || (currentPath === '' && href === 'mypage')) {
                link.classList.add('active');
            } else {
                link.classList.remove('active');
            }
        });


        // 🚨 핵심: 비밀번호 검증 로직
        const verificationForm = document.getElementById('verificationForm');
        const passInput = document.getElementById('current_pass');
        const errorDiv = document.getElementById('passwordError');

        // CSRF 토큰 가져오기
        const csrfToken = document.querySelector('meta[name="_csrf"]').content;
        const csrfHeaderName = document.querySelector('meta[name="_csrf_header"]').content;

        if (verificationForm) {
            verificationForm.addEventListener('submit', async (e) => {
                e.preventDefault(); 
                errorDiv.textContent = '비밀번호 확인 중...'; 
                
                const current_pass = passInput.value;

                if (!current_pass) {
                    errorDiv.textContent = '비밀번호를 입력해주세요.';
                    return;
                }

                const headers = { 'Content-Type': 'application/json' };
                if (csrfToken && csrfHeaderName) headers[csrfHeaderName] = csrfToken;

                try {
                    // API 호출
                    const res = await fetch('/api/v1/auth/coverifypass', {
                        method: 'POST',
                        headers: headers,
                        // DTO와 일치하는 JSON 전송
                        body: JSON.stringify({ current_pass: current_pass }) // DTO 필드가 'password'라고 가정
                    });

                    const data = await res.json().catch(() => ({}));

                    if (res.ok && data.success) {
                        // 성공 시: /member/edit 페이지로 이동
                        window.location.href = '/company/edit'; 
                    } else {
                        // 실패 시: 에러 메시지 표시
                        errorDiv.textContent = data.message || '비밀번호가 일치하지 않습니다. 다시 확인해주세요.';
                    }

                } catch (error) {
                    console.error('비밀번호 확인 중 오류 발생:', error);
                    errorDiv.textContent = '네트워크 오류로 확인에 실패했습니다.';
                }
            });
        }
    });
