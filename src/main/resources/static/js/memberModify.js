document.addEventListener('DOMContentLoaded', () => {
    const csrfMeta = document.querySelector('meta[name="_csrf"]');
    const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
    const csrfToken = csrfMeta ? csrfMeta.content : null; 
    const csrfHeader = csrfHeaderMeta ? csrfHeaderMeta.content : 'X-CSRF-TOKEN';

    const myEditForm = document.getElementById('myedit-form');
    const errorMsg = document.getElementById('editErrorMessage');

    if (myEditForm) {
        myEditForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            errorMsg.textContent = '';

            const member_idx = document.getElementById('member_idx').value;
            const member_id = document.getElementById('member_id').value.trim();
            const member_name = document.getElementById('member_name').value.trim();
            const member_nickname = document.getElementById('member_nickname').value.trim();
            const member_email = document.getElementById('member_email').value.trim();
            const member_pass = document.getElementById('member_pass').value;
            const member_pass2 = document.getElementById('member_pass2').value;
            const member_phone = document.getElementById('member_phone').value.trim();
            const member_address = document.getElementById('member_address').value.trim();
            const member_gender = document.getElementById('member_gender').value.trim();
            const member_job = document.getElementById('member_job').value.trim();

            // 비밀번호 일치 확인
            if (member_pass || member_pass2) {
                if (member_pass !== member_pass2) {
                    errorMsg.textContent = '새로운 비밀번호와 확인 비밀번호가 일치하지 않습니다.';
                    return;
                }
            }

            // 수정 데이터 생성
            const updateData = {
                member_idx,
                member_id,
                member_name,
                member_nickname,
                member_email,
                member_phone,
                member_address,
                member_gender,
                member_job,
            };

            if(member_pass) {
                updateData.member_pass = member_pass;
            }

            try {
                const response = await fetch('/api/v1/auth/modify', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken,
                    },
                    body: JSON.stringify(updateData),
                });
                const result = await response.json();

                if(response.ok && result.success) {
                    alert(result.message || '회원정보가 성공적으로 수정되었습니다.');
                    window.location.href = '/member/mypage';
                } else {
                    errorMsg.textContent = result.message || '회원정보 수정에 실패했습니다.';
                }
            } catch(error) {
                console.error('회원정보 수정 중 오류:', error);
                errorMsg.textContent = '서버와 통신할 수 없습니다. 네트워크를 확인하세요.';
            }
        });
    }
});
