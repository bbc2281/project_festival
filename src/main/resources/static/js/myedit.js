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
        const csrfInput = document.querySelector('input[name="_csrf"]');
        if (csrfInput) {
             csrfToken = csrfInput.value;
             csrfHeaderName = 'X-CSRF-TOKEN'; 
        }
    }

    // ----------------------------------------------------------------------
    // 🌟 회원정보 수정 로직 (MyInfo Update) 🌟
    // ----------------------------------------------------------------------
    const myEditForm = document.getElementById('myedit-form');
    
    if (myEditForm) {
        
        // 1. 에러 메시지 컨테이너 생성 및 추가 (기존 HTML 구조에 맞춤)
        const editErrorMessageContainer = document.createElement('p');
        editErrorMessageContainer.style.color = 'red';
        editErrorMessageContainer.style.fontWeight = 'bold';
        editErrorMessageContainer.style.marginTop = '15px';
        myEditForm.prepend(editErrorMessageContainer);
        
        myEditForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            editErrorMessageContainer.textContent = ''; // 에러 메시지 초기화
            
            // 2. 데이터 수집
            const member_name = document.getElementById('member_name').value.trim();
            const member_email = document.getElementById('member_email').value.trim();
            const member_pass = document.getElementById('member_pass').value; 
            const member_pass2 = document.getElementById('member_pass2').value;
            const member_nickname = document.getElementById('member_nickname').value.trim();
            const member_region = document.getElementById('member_address').value.trim(); // 주소 필드
            const member_job = document.getElementById('member_job').value.trim();

            // 3. 유효성 검사 (Validation)
            
            // 필수 필드 검사
            if (!member_name || !member_email || !member_nickname || !member_region) {
                editErrorMessageContainer.textContent = '필수 정보(이름, 이메일, 닉네임, 주소)를 모두 입력해야 합니다.';
                return;
            }

            // 이메일 형식 검사
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(member_email)) {
                editErrorMessageContainer.textContent = '유효한 이메일 주소를 입력해주세요.';
                return;
            }

            // 비밀번호 변경 시 일치 여부 및 유효성 검사
            if (member_pass || member_pass2) {
                if (member_pass !== member_pass2) {
                    editErrorMessageContainer.textContent = '새로운 비밀번호와 확인 비밀번호가 일치하지 않습니다.';
                    return;
                }
                
                // 비밀번호 길이 검사 (8~20자 가정)
                if (member_pass.length < 8 || member_pass.length > 20) {
                    editErrorMessageContainer.textContent = '비밀번호는 8자 이상 20자 이하여야 합니다.';
                    return;
                }
            }
            
            // 4. 서버 전송 데이터 준비
            // member_pass가 빈 문자열이면 전송하지 않아서 기존 비밀번호를 유지하도록 함
            const updateData = {
                member_name,
                member_email,
                member_nickname,
                member_address, 
                member_job
            };
            
            if (member_pass) {
                updateData.member_pass = member_pass;
            }
            
            // 5. API 호출을 위한 헤더 설정 (CSRF 포함)
            const headers = { 'Content-Type': 'application/json' };
            if (csrfToken && csrfHeaderName) headers[csrfHeaderName] = csrfToken; 

            try {
                // REST API 엔드포인트: PUT /api/v1/member/update
                const res = await fetch('/api/v1/member/update', { 
                    method: 'PUT',
                    headers: headers,
                    body: JSON.stringify(updateData)
                });

                const data = await res.json(); 

                if (res.ok && data.success) {
                    // 성공 처리
                    console.log('회원정보가 성공적으로 수정되었습니다.');
                    // Custom Modal로 대체 필요
                    alert('회원정보가 성공적으로 수정되었습니다.'); 
                    window.location.href = '/member/mypage'; 
                } else {
                    // 서버에서 받은 오류 메시지 표시
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