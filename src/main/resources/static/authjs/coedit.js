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

    // ----------------------------------------------------------------------
    // 🌟 CSRF 토큰 및 헤더 이름 가져오기 🌟
    // ----------------------------------------------------------------------
    let csrfToken = null;
    let csrfHeaderName = null;
    
    // Meta 태그에서 토큰과 헤더 이름을 읽습니다.
    const csrfMeta = document.querySelector('meta[name="_csrf"]');
    const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
    
    if (csrfMeta && csrfHeaderMeta) {
        csrfToken = csrfMeta.content;
        csrfHeaderName = csrfHeaderMeta.content;
    } 
    // 백업: 숨겨진 입력 필드에서 토큰 읽기
    else {
        // CSRF 토큰은 로그인 폼이나 다른 폼에 hidden field로 존재할 수 있음
        const csrfInput = document.querySelector('input[name="_csrf"]'); 
        if (csrfInput) {
             csrfToken = csrfInput.value;
             csrfHeaderName = 'X-CSRF-TOKEN'; 
        }
    }


    // ----------------------------------------------------------------------
    // 🌟 회원정보 수정 로직 (MyInfo Update) 🌟
    // ----------------------------------------------------------------------
    const myEditForm = document.getElementById('edit-form');
    
    if (myEditForm) {
        
        // 1. 에러 메시지 컨테이너 생성 및 추가
        const editErrorMessageContainer = document.createElement('p');
        editErrorMessageContainer.className = 'text-danger fw-bold mt-3';
        myEditForm.prepend(editErrorMessageContainer);
        
        myEditForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            editErrorMessageContainer.textContent = ''; // 에러 메시지 초기화
            
            // 2. 데이터 수집 (기존 값이 채워져 있으므로 trim()만 수행)
            const company_name = document.getElementById('company_name').value.trim();
            const member_email = document.getElementById('member_email').value.trim();
            const member_pass = document.getElementById('member_pass').value; 
            const member_pass2 = document.getElementById('member_pass2').value;
            const company_reg_num = document.getElementById('company_reg_num').value.trim();
            const company_address = document.getElementById('company_address').value.trim(); // 주소 필드
            const company_account = document.getElementById('company_account').value.trim();
            const company_phone = document.getElementById('company_phone').value.trim();

            // 3. 클라이언트 최소 유효성 검사: 비밀번호 일치 여부만 확인
            if (member_pass || member_pass2) {
                if (member_pass !== member_pass2) {
                    editErrorMessageContainer.textContent = '새로운 비밀번호와 확인 비밀번호가 일치하지 않습니다.';
                    return;
                }
            }
            
            // 4. 서버 전송 데이터 준비
            // 기존 값이 채워져 있으므로, 변경하지 않아도 기존 값이 서버로 전송됨.
            // 백엔드 DTO/Service에서 유효성 검사를 완화하고, 값이 변경되었을 때만 반영해야 함.
            const updateData = {
                company_name,
                member_email,
                company_phone,
                company_address, 
                company_reg_num,
                company_account,
            };
            
            // 🌟 비밀번호는 입력되었을 때만 전송합니다. (부분 업데이트 핵심) 🌟
            if (member_pass) {
                updateData.member_pass = member_pass;
            }
            
            // 5. API 호출을 위한 헤더 설정 (CSRF 포함)
            const headers = { 'Content-Type': 'application/json' };
            if (csrfToken && csrfHeaderName) headers[csrfHeaderName] = csrfToken; 

            try {
                // REST API 엔드포인트: PUT /api/v1/auth/modifyCom
                const res = await fetch('/api/v1/auth/modifycom', { 
                    method: 'POST',
                    headers: headers,
                    body: JSON.stringify(updateData)
                });

                const data = await res.json().catch(()=>({})); // JSON 파싱 오류 방지

                if (res.ok && data.success) {
                    // 성공 처리
                    console.log('회원정보가 성공적으로 수정되었습니다.');
                    alert(data.message || '회원정보가 성공적으로 수정되었습니다.'); 
                    window.location.href = '/company/mypage'; 
                } else if (!res.ok && data.errors) {
                     // 백엔드 DTO @Valid 오류 처리
                    const message = Object.entries(data.errors)
                        .map(([field, msg]) => `- ${field}: ${msg}`)
                        .join('\n');
                    alert('입력 오류:\n' + message);
                    editErrorMessageContainer.textContent = '입력 정보를 다시 확인해주세요.';
                    console.error('백엔드 유효성 검사 오류:', data.errors);
                }
                else {
                    // 기타 서버 오류 메시지 표시
                    editErrorMessageContainer.textContent = data.message || `회원정보 수정에 실패했습니다. (상태 코드: ${res.status})`;
                }
                
            } catch (error) {
                // 네트워크 오류 처리
                console.error('회원정보 수정 중 치명적인 오류 발생:', error);
                editErrorMessageContainer.textContent = '서버와 통신할 수 없습니다. 네트워크 연결을 확인하세요.';
            }
        });
    }

});