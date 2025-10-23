document.addEventListener('DOMContentLoaded',()=>{
   const authForm = document.getElementById('auth-form');
   const userIdInput = document.getElementById('member_id');
   const userPassInput = document.getElementById('member_pass');
   
   const csrfElement = document.querySelector('meta[name="_csrf"]');

   const csrfToken = csrfElement ? csrfElement.value : '';
   const csrfHeader = csrfElement ? csrfElement.name : '';
   
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
      
            if(member_pass.length < 6){
               errorMessageContainer.textContent='비밀번호는 최소 6자 이상이여야 합니다';
               return;
            }
      
            errorMessageContainer.textContent='';
      
            fetch('/api/v1/auth/login',{
               method: 'POST',
               headers: {
                  'Content-Type': 'application/json',
                  [csrfHeader]: csrfToken
               },
               body: JSON.stringify({
                  memberid:member_id,
                  member_pass:member_pass
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




