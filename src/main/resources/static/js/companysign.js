// signup.js (통합 코드)
const csrfInput = document.querySelector('input[name="_csrf"]');
const csrfToken = csrfInput ? csrfInput.value : null;
const csrfHeader = 'X-CSRF-TOKEN'; 


// 🚨🚨 중복확인 상태를 관리하는 객체 (핵심) 🚨🚨
const checkStatus = {
    'member_id': false,
    'company_reg_nums': false,
};


// 🌟 2. 사업자등록번호 유효성 검사 함수 🌟
async function verifyBusinessNumber() {
    const businessNumberInput = document.getElementById('company_reg_num'); // HTML ID에 맞게 수정 필요
    const representativeNameInput = document.getElementById('company_owner'); // 대표자명 필드 ID에 맞게 수정 필요
    const checkMessage = document.getElementById('businessCheckMessage'); // 검증 메시지를 표시할 요소 ID

    const businessNumber = businessNumberInput ? businessNumberInput.value.trim() : '';
    const representativeName = representativeNameInput ? representativeNameInput.value.trim() : '';
    
    // 필수 필드 확인
    if (!businessNumber || !representativeName) {
        checkMessage.textContent = '사업자 번호와 대표자명을 모두 입력해주세요.';
        checkMessage.style.color = 'red';
        checkStatus.business_number = false;
        return;
    }

    checkMessage.textContent = '사업자 정보 확인 중...';
    checkMessage.style.color = 'gray';

    try {
        // 백엔드 RestController (CompanyRestController.java) 호출
        const url = `/api/v1/company/verifybusiness?b_no=${encodeURIComponent(businessNumber)}&p_nm=${encodeURIComponent(representativeName)}`;

        const response = await fetch(url, { method: 'POST' }); // RestController가 POST를 사용하도록 설계했으므로 POST 사용

        const result = await response.json();

        if (response.ok) {
            checkMessage.textContent = result.message || '✅ 유효한 사업자 번호입니다.';
            checkMessage.style.color = 'green';
            checkStatus.business_number = true;
        } else {
            checkMessage.textContent = result.message || '❌ 유효하지 않거나 폐업된 사업자 번호입니다.';
            checkMessage.style.color = 'red';
            checkStatus.business_number = false;
        }

    } catch (error) {
        console.error('사업자 번호 검증 오류:', error);
        checkMessage.textContent = '서버 통신 오류가 발생했습니다.';
        checkMessage.style.color = 'orange';
        checkStatus.business_number = false;
    }
}



function checkDuplicateId(){
   const memberId = document.getElementById('member_id').value;
   const checkMessage = document.getElementById('idCheckMessage');
    
    if (!memberId) {
    checkMessage.textContent = '아이디를 입력해주세요.';
    checkMessage.style.color = 'red';
    return;
   } 

   checkMessage.textContent = 'Checking...';
   checkMessage.style.color = 'gray';      // ✅ 올바른 요소 변수명

   const url = '/api/v1/auth/checkId?member_id=' + encodeURIComponent(memberId);
   
   fetch(url)
            .then(response=>{
                 console.log('응답 상태코드:', response.status);
                 if (!response.ok) throw new Error('응답 오류: ' + response.statusText);
                 return response.json();
              })
              .then(data=>{
                const exists = data.exists;
                console.log('응답 데이터:', data);
                if (exists) {
                    checkMessage.textContent = `❌ The ID ${memberId} is already taken.`;
                    checkMessage.style.color = 'red';
                    checkStatus.member_id = false;
                } else {
                    checkMessage.textContent = `✅ The ID ${memberId} is available.`;
                    checkMessage.style.color = 'green';
                    checkStatus.member_id = true;
                }


              })
             .catch(error => {
                    console.error('fetch 에러 상세:', error);
                    checkMessage.textContent = '서버 응답 오류: ' + error.message;
                    checkMessage.style.color = 'orange';
                    });


}




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

    // 2. 🚨🚨 중복확인 완료 여부 검사
    if (!checkStatus.member_id) {
        alert('아이디 중복확인을 완료해야 가입할 수 있습니다.');
        return;
    }


    // 🌟 사업자 번호 검증 완료 여부 확인 🌟
    if (!checkStatus.business_number) {
        alert('사업자 등록 번호 유효성 검증을 완료해야 가입할 수 있습니다.');
        return;
    }


    // 🌟🌟🌟 [여기부터 주소 통합 로직 시작] 🌟🌟🌟

    const postcode = document.getElementById('postcode').value;
    const baseAddress = document.getElementById('address').value; // name: member_address_base
    const detailAddress = document.getElementById('detailAddress').value; // name: member_address_detail
    const extraAddress = document.getElementById('extraAddress').value;
    
    // 1. 주소 필드를 하나의 문자열로 합치기 (예: [우편번호] 기본주소 상세주소 참고항목)
    const fullAddress = `[${postcode}] ${baseAddress} ${detailAddress} ${extraAddress}`;
    
    // 2. 최종 DTO 필드(company_address)에 통합된 주소 할당
    data.company_address = fullAddress.trim();
    
    // 3. DTO에 없는 임시 주소 필드는 삭제하여 서버로 전송하지 않음
    delete data.member_postcode;
    delete data.member_address_base; 
    delete data.member_address_detail; 
    
    // 🌟🌟🌟 [주소 통합 로직 끝] 🌟🌟🌟

    // 3. 백엔드로 보내지 않을 필드 제거
    // 🚨 필드명 변환이 필요 없으므로, 필요 없는 필드만 제거하고 data를 그대로 전송
    delete data.member_pass2;

    // 4. API 호출
    try {
        const response = await fetch('/api/v1/auth/joincompany', {
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
                alert(result.message || `회원가입 실패(상태 코드 : ${response.status})`);
            }
            return;
        }
      
        alert(result.message || '회원가입 성공! 로그인 페이지로 이동합니다.');
        setTimeout(() => {
            window.location.href = '/auth/loginPage';
        }, 500);


    }catch (error) {
        console.error('API 통신 오류:', error);
        alert('서버와 통신하는 중 문제가 발생했습니다. 네트워크 연결을 확인하세요.');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // HTML에 정의된 ID: 'sign-form'을 사용합니다.

    const signupForm = document.getElementById('sign-form'); 
    const btnVerifyBusiness = document.getElementById('btnVerifyBusiness');
    
    const memberIdInput = document.getElementById('member_id');
    if (memberIdInput) {
        memberIdInput.addEventListener('input', () => { checkStatus.member_id = false; });
    }
    
    // 🌟 사업자 번호 입력 필드 변경 시 검증 상태 초기화 🌟
    const companyNumInput = document.getElementById('company_reg_num');
    const companyOwnerInput = document.getElementById('company_owner');


    if (companyNumInput) {
        companyNumInput.addEventListener('input', () => { checkStatus.business_number = false; });
    }


    if (signupForm) {
        // 폼 제출 이벤트를 가로채서 handleSignupSubmit 함수를 실행합니다.
        signupForm.addEventListener('submit', handleSignupSubmit);
    } else {
        console.error('ERROR: HTML에서 ID가 "sign-form"인 폼을 찾을 수 없습니다. 연결 실패!');
    }

    // 🌟 사업자 번호 검증 버튼 이벤트 바인딩 🌟
    if (btnVerifyBusiness) {
        btnVerifyBusiness.addEventListener('click', verifyBusinessNumber);
    }

    
    if (companyNumInput || companyOwnerInput) {
        const resetStatus = () => { checkStatus.business_number = false; };
        if (companyNumInput) companyNumInput.addEventListener('input', resetStatus);
        if (companyOwnerInput) companyOwnerInput.addEventListener('input', resetStatus);
    }
});