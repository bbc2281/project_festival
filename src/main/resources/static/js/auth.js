document.addEventListener('DOMContentLoaded',()=>{
   const authForm = document.getElementById('auth-form');
   const userIdInput = document.getElementById('member_id');
   const userPassInput = document.getElementById('member_pass');

   const csrfTokenInput = document.querySelector('input[name="' + document.querySelector('input[type="hidden"]').name + '"]');
   
   let csrfToken = '';
   let csrfHeader = '';
   if(csrfTokenInput){
      csrfToken = csrfTokenInput.value;
      csrfHeader = 'X-CSRF-TOKEN';
   }
   const errorMessageContainer = document.createElement('p');
   errorMessageContainer.className = 'text-danger mt-2';
   
   if (authForm) {
      authForm.insertBefore(errorMessageContainer, authForm.firstChild);
   }

   if(authForm){
      
            authForm.addEventListener('submit',function(event){
              
            event.preventDefault();
      
            const member_id =userIdInput.value.trim();
            const member_pass =userPassInput.value.trim();
      
            if(!member_id ||!member_pass){
               errorMessageContainer.textContent='아이디와 비밀번호를 모두 입력해주세요';
               return;
            }
      
         
      
            errorMessageContainer.textContent='';
      
            fetch('http://localhost:8080/api/v1/auth/login',{
               method: 'POST',
               headers: {
                  'Content-Type': 'application/json',
                  ...(csrfToken && csrfHeader ? { [csrfHeader]: csrfToken } : {})
               },
               body: JSON.stringify({
                  'member_id': member_id,
                  'member_pass':member_pass
               })
            })
            .then(response => {
               if (!response.ok && response.status !== 400) {
                  throw new Error('Network response was not ok');
               }
               return response.json();
            })
            .then(data => {
               if (data.status === 'success') {
                  window.location.href = '/';
               } else {
                  errorMessageContainer.textContent = data.message || '로그인에 실패했습니다';
                  userPassInput.value='';
               }
            })
            .catch(error => {
               console.error('로그인 중 네트워크/ fetch 오류', error);
               errorMessageContainer.textContent = '로그인 서버와 접속할 수 없습니다';
            });
         });
      
        }
       
});




