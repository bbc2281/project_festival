document.addEventListener('DOMContentLoaded', () => {
    const editButton = document.getElementById('edit');
    const reviewsButton = document.getElementById('myreview');

    // 1. 회원정보 수정 버튼 클릭 이벤트
    if (editButton) {
        editButton.addEventListener('click', () => {
            console.log('Navigating to /mypage/mypageedit');
            window.location.href = '/member/edit';
        });
    }

    // 2. 내 리뷰 보기 버튼 클릭 이벤트
    if (reviewsButton) {
        reviewsButton.addEventListener('click', () => {
            console.log('Navigating to /mypage/myreview');
            window.location.href = '/member/review';
        });
    }
});



// mypage-edit
const csrfInput = document.querySelector('input[name="_csrf"]');
const csrfToken = csrfInput ? csrfInput.value : null;
const csrfHeader = 'X-CSRF-TOKEN'; 



// ✅ 회원가입 폼 제출 (API 연동)
async function handleSignupSubmit(e) {
    e.preventDefault();

    const form = e.target;
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries()); // 🚨 data 객체에 member_id, member_pass가 직접 들어있음
    
    // 1. 비밀번호 일치 검증
    // 🚨 data.member_pass 필드 사용 (HTML name="member_pass"와 일치)
    if(!data.member_pass2 || !data.member_pass2.trim()){
    alert('비밀번호 확인입력을 해주세요.');
    document.getElementById('member_pass2').focus();
    return;
   }

   
    if (data.member_pass !== data.member_pass2) {
        alert('비밀번호와 비밀번호 확인 값이 일치하지 않습니다.');
        document.getElementById('member_pass2').focus();
        return;
    }


    // 3. 백엔드로 보내지 않을 필드 제거
    // 🚨 필드명 변환이 필요 없으므로, 필요 없는 필드만 제거하고 data를 그대로 전송
    delete data.member_pass2;

    // 4. API 호출
    try {
        const response = await fetch('/api/v1/auth/modify', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken,
            },
            body: JSON.stringify(data) // 🚨 HTML 이름이 맞으므로, 변환 없이 data 그대로 전송!
        });

        const result = await response.json().catch(()=>({}));

        if(!response.ok){

            if(result.errors){
                const message = Object.entries(result.errors)
                .map(([field, msg])=> `${field}: ${msg}`)
                .join('\n');
                alert(message);
            }else{
                alert(result.message || `회원정보 수정 오류 (상태 코드 : ${response.status})`);
            }
            return;
        }
      
        alert(result.message || '회원정부 수정 성공! 마이패이지로 이동합니다.');
        setTimeout(() => {
            window.location.href = '/member/mypage';
        }, 500);


    }catch (error) {
        console.error('API 통신 오류:', error);
        alert('서버와 통신하는 중 문제가 발생했습니다. 네트워크 연결을 확인하세요.');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // HTML에 정의된 ID: 'sign-form'을 사용합니다.

    const signupForm = document.getElementById('myedit-form'); 
    
    if (signupForm) {
        // 폼 제출 이벤트를 가로채서 handleSignupSubmit 함수를 실행합니다.
        signupForm.addEventListener('submit', handleSignupSubmit);
    } else {
        console.error('ERROR: HTML에서 ID가 "myedit-form"인 폼을 찾을 수 없습니다. 연결 실패!');
    }
});