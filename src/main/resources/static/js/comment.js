const buttons = document.querySelectorAll('.commentBtn')
const loginIdx = document.getElementById("sessionInfo").dataset.loginId;

buttons.forEach(btn => {
btn.addEventListener('click', () => {
  const reviewBox = btn.closest(".review-box");
  const reviewIdx = btn.getAttribute("data-review-idx"); 

    if (!reviewBox.querySelector(".comment-list")) {
      loadCommentList(reviewIdx,reviewBox);
    }

    
      
    const existingInput = reviewBox.querySelector(".comment-input");
    const existingSaveBtn = reviewBox.querySelector(".comment-save-btn");
    const existingList = reviewBox.querySelector(".comment-list");
    if (existingInput || existingSaveBtn || existingList) {
     existingInput?.remove();
     existingSaveBtn?.remove();
     existingList?.remove();
     return;
    }

    

    if (reviewBox.querySelector(".comment-input")) return;
    const input = document.createElement("input");
    input.type = "text";
    input.className = "form-control mt-2 comment-input";
    input.placeholder = "댓글을 입력하세요";
    reviewBox.appendChild(input); 

    if(reviewBox.querySelector(".comment-save-btn")) return;
    const saveBtn = document.createElement("button");
    saveBtn.textContent = "저장";
    saveBtn.className = "btn btn-sm btn-primary mt-2 comment-save-btn";
    reviewBox.appendChild(saveBtn);

    saveBtn.addEventListener("click", () => {
    const commentContent = input.value.trim();
    
    if (!commentContent) {
      alert("댓글을 입력하세요");
      input.focus();
      return;
    }
    
    //댓글 작성
    fetch("/comment/write", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        review_idx: reviewIdx,
        comment_content: commentContent
      })
    })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          alert("댓글 등록 완료");
          input.value = ""; 
          reviewBox.querySelector(".comment-list")?.remove();
          loadCommentList(reviewIdx,reviewBox); 
        } else {
          alert(data.message);
        }
      })
      .catch(err => {
        console.error("에러 발생:", err);
        alert("댓글 등록 중 오류가 발생했습니다.");
      });
    
  });
});
});

function loadCommentList(reviewIdx,reviewBox) {
    fetch(`/comment/list?review_idx=${reviewIdx}`)
        .then(res => res.json())
        .then(data => {
        console.log(data);
        const commentList = document.createElement("div");
        commentList.className = "comment-list mt-2";
        data.forEach(comment => {
            const reply = document.createElement("div");
            reply.className = "comment-reply";
            reply.textContent = `${comment.member_nickname} : ${comment.comment_content} ${comment.comment_regDate}`;
            
            if(comment.member_idx == loginIdx){
            const modifyButton = document.createElement("button");
            modifyButton.textContent = "수정";
            modifyButton.className = "btn btn-sm btn-outline-secondary ms-2";
            modifyButton.addEventListener("click", () => {
                  modifyProcess(comment, reply,() => {
                  reviewBox.querySelector(".comment-list")?.remove();
                  loadCommentList(reviewIdx, reviewBox);
                  });
                });
            reply.appendChild(modifyButton);
            const deleteButton = document.createElement("button");
            deleteButton.textContent = "삭제";
            deleteButton.className = "btn btn-sm btn-outline-secondary ms-2";
            deleteButton.addEventListener("click", () => {
                  deleteProcess(comment, () => {
                  reviewBox.querySelector(".comment-list")?.remove();
                  loadCommentList(reviewIdx, reviewBox);
              });
            });
            reply.appendChild(deleteButton);
            }

            commentList.appendChild(reply);
        });
        reviewBox.appendChild(commentList);
        });
}

function modifyProcess(comment,reply,reloadFn = () => {}){
    reply.innerHTML = "";

    if(reply.querySelector(".form-control"))return;
    const input = document.createElement("input");
    input.type = "text";
    input.className = "form-control";
    input.value = comment.comment_content;
    reply.appendChild(input);

    if(reply.querySelector(".btn-save"))return;
    const saveBtn = document.createElement("button");
    saveBtn.textContent = "저장";
    saveBtn.className = "btn btn-sm btn-save mt-2";
    reply.appendChild(saveBtn);

    if(reply.querySelector(".btn-cancell"))return;
    const cancellBtn = document.createElement("button");
    cancellBtn.textContent = "취소";
    cancellBtn.className = "btn btn-sm btn-cancell mt-2";
    cancellBtn.addEventListener("click", () => {
    reply.innerHTML = `${comment.member_nickname} : ${comment.comment_content} ${comment.comment_regDate}`;
    input.remove();
    saveBtn.remove();
    cancellBtn.remove();
    });
    reply.appendChild(cancellBtn);

    saveBtn.addEventListener("click",()=>{
      const newContent = input.value.trim();
      if(!newContent){
        alert("내용을 입력하세요");
        input.focus();
        return;
      }
      fetch("/comment/modify", {
        method : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          comment_idx: comment.comment_idx,
          comment_content: newContent
        })
      })
      .then(response => response.json())
      .then(data =>{
        if(data.success){
          alert("댓글 수정 완료");
          try {
            if(typeof reloadFn === "function"){
              reloadFn();
            } 
          } catch (error) {
            console.error("reloadFn오류 발생", e);
          }
        }else{
          alert(data.message);
        }
      })
      .catch(error => {
        console.error("수정 중 오류 발생", error)
        alert("댓글 수정 중 오류가 발생했습니다.")
      })
    });
}

function deleteProcess(comment,reloadFn = () => {}){
  fetch("/comment/delete", {
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify({
      comment_idx : comment.comment_idx
    })
  })
  .then(response => response.json())
  .then(data =>{
    if(data.success){
      alert("댓글 삭제 완료 ")
      try {
        if(typeof reloadFn === "function"){
          reloadFn();
        }
      } catch (error) {
        console.error("reloadFn오류 발생" ,e)
      }
    }else{
      alert(data.message);
    }
  })
  .catch(error =>{
    console.err("수정 중 오류 발생",error)
    alert("댓글 삭제 중 오류가 발생했습니다.") 
  })
}


