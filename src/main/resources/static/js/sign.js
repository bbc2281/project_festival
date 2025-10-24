// signup.js (통합 코드)
const csrfToken = document.querySelector('input[name="_csrf"]').value;
const csrfHeader = document.querySelector('input[name="_csrf"]').name;


// 🚨🚨 중복확인 상태를 관리하는 객체 (핵심) 🚨🚨
const checkStatus = {
    'member_id': false,
};


function checkDuplicateId(){
   const memberId = document.getElementById('member_id').value;
   const checkMessage = document.getElementById('idCheckMessage');
    
   checkMessage.textContent = 'Checking';
   checkDuplicateId.style.color = 'gray';
   const url = '/checkId?member_id=' + encodeURIComponent(memberId);
   
   fetch(url)
            .then(response=>{
                if(!response.ok){
                    throw new Error('네트워크가 불안정합니다' + response.statusText);
                }
                return response.json();
              })
              .then(data=>{
                const exists = data.exists;
                if(exists){
                    checkMessage.textContent = `❌ The ID '${memberId}' is already taken.`;
                    checkMessage.style.color = 'red';
                }
              })
              .catch(error=>{
                 console.log('error');
                 checkMessage.textContent = 'An error occurred. Please try again.';
                 checkMessage.style.color = 'orange';

              });


}






// ✅ 회원가입 폼 제출 (API 연동)
async function handleSignupSubmit(e) {
    e.preventDefault();

    const form = e.target;
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // 1. 비밀번호 일치 검증
    if (data.member_pass !== data.member_pass2) {
        alert('비밀번호와 비밀번호 확인 값이 일치하지 않습니다.');
        $('#member_pass2').focus();
        return;
    }

    // 2. 🚨🚨 중복확인 완료 여부 검사 (가장 중요!) 🚨🚨
    if (!checkStatus.member_id) {
        alert('아이디 중복확인을 완료해야 가입할 수 있습니다.');
        $('#member_id').focus();
        return;
    }
    if (!checkStatus.member_nickname) {
        alert('닉네임 중복확인을 완료해야 가입할 수 있습니다.');
        $('#member_nickname').focus();
        return;
    }

    // 3. 백엔드로 보내지 않을 필드 제거
    delete data.member_pass2; 

    // 4. API 호출
    try {
        const response = await fetch('/api/v1/auth/memberjoin', { // 🚨 백엔드 URL 확인 필요
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken,
            },
            body: JSON.stringify(data) 
        });

        const result = await response.json();

        if (response.ok && response.status === 201) {
            alert(result.message || '회원가입 성공! 로그인 페이지로 이동합니다.');
            window.location.href = '/auth/login'; 
        } else if (response.status === 400 && result.errors) {
            // 유효성 검사 실패 (MethodArgumentNotValidException)
            alert(`가입 실패: 입력 항목을 확인해주세요.\n\n오류: ${Object.values(result.errors).join(', ')}`);
        } else {
            // 기타 에러 (UserException 등)
            alert(result.message || '회원가입 중 알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
        }
    } catch (error) {
        console.error('API 통신 오류:', error);
        alert('서버와 통신하는 중 문제가 발생했습니다. 네트워크 연결을 확인하세요.');
    }
}