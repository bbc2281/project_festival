 
    document.addEventListener('DOMContentLoaded', () => {
      // 1. 사이드바 활성화 로직 (기존 로직)
      const links = document.querySelectorAll('.menu a');
      const currentPath = window.location.pathname.split("/").pop();
      links.forEach(link => {
        const href = link.getAttribute('href').split("/").pop();
        if (currentPath === href) {
          link.classList.add('active');
        } else {
          link.classList.remove('active');
        }
      });
      
      // 2. CSRF 토큰 및 헤더 가져오기
      const csrfToken = document.querySelector('meta[name="_csrf"]').content;
      const csrfHeaderName = document.querySelector('meta[name="_csrf_header"]').content;

      // 3. 탈퇴 폼 제출 이벤트 핸들러
      const deleteForm = document.getElementById('deleteMemberForm');
      const deleteMsg = document.getElementById('deleteMsg');
      const idInput = document.getElementById('member_id'); // 아이디 입력 필드
      const passInput = document.getElementById('current_pass'); // 비밀번호 입력 필드
      
      deleteForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const memberId = idInput.value.trim();
        const currentPass = passInput.value.trim();

        if (!memberId || !currentPass) {
          deleteMsg.innerText = "아이디와 비밀번호를 모두 입력해주세요.";
          return;
        }

        // 🚨 최종 확인 팝업
        if (!confirm("정말로 회원 탈퇴를 진행하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) {
            return;
        }

        deleteMsg.innerText = "탈퇴 처리 중...";
        deleteMsg.style.color = '#6b21a8'; // 처리 중 메시지는 보라색

        // API 호출을 위한 헤더 설정
        const headers = { 'Content-Type': 'application/json' };
        headers[csrfHeaderName] = csrfToken;

        try {
          // ⚠️ API 경로와 JSON 키는 백엔드와 정확히 일치해야 합니다. 
          // 경로: /api/v1/member/delete (가장 일반적인 REST 경로 가정)
          // JSON 데이터: { "member_id": "...", "current_pass": "..." }
          const res = await fetch('/api/v1/auth/withdraw', {
            method: 'DELETE', // DELETE 메서드 사용
            headers: headers,
            // 🌟 아이디와 비밀번호 모두 전송 🌟
            body: JSON.stringify({ 
                member_id: member_id, 
                member_pass: member_pass 
            }) 
          });

          // 응답 처리
          const data = await res.json().catch(() => ({}));
          
          if (res.ok && data.success) {
            // 성공
            deleteMsg.innerText = data.message || "회원탈퇴가 성공적으로 완료되었습니다.";
            alert("회원탈퇴가 완료되었습니다. 이용해주셔서 감사합니다.");
            window.location.href = '/'; // 홈으로 리다이렉트 (로그아웃 처리됨)

          } else if (res.status === 401 || res.status === 403) {
            // 인증 실패 (ID/PW 불일치)
            deleteMsg.style.color = '#ef4444';
            deleteMsg.innerText = data.message || "아이디 또는 비밀번호가 일치하지 않습니다. 다시 확인해주세요.";
            passInput.value = ''; // 비밀번호 초기화
            
          } else {
            // 기타 서버/클라이언트 오류
            deleteMsg.style.color = '#ef4444';
            deleteMsg.innerText = data.message || `회원탈퇴에 실패했습니다. (오류: ${res.status})`;
          }

        } catch (error) {
          console.error('회원탈퇴 처리 중 오류 발생:', error);
          deleteMsg.style.color = '#ef4444';
          deleteMsg.innerText = "네트워크 오류로 탈퇴에 실패했습니다.";
        }
      });
    });
