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

            const company_idx = document.getElementById('company_idx').value;
            const member_id = document.getElementById('member_id').value.trim();
            const company_name = document.getElementById('company_name').value.trim();
            const company_owner = document.getElementById('company_owner').value.trim();
            const member_email = document.getElementById('member_email').value.trim();
            const company_phone = document.getElementById('company_phone').value.trim();
            const company_address = document.getElementById('company_address').value.trim();
            const company_account = document.getElementById('company_account').value.trim();

            // 수정 데이터 생성
            const updateData = {
                company_idx,
                member_id,
                company_name,
                company_owner,
                member_email,
                company_phone,
                company_address,
                company_account
            };

            try {
                const response = await fetch('/api/v1/auth/modifyCompany', {
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
                    window.location.href = '/company/mypage';
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
