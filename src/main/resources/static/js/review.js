const btn = document.getElementById(`btnAddReview`);
const textarea = document.getElementById(`reviewText`);
const uploadFile = document.getElementById(`upload_file`);

document.querySelectorAll(".review-idx").forEach(el => {
  const reviewIdx = el.dataset.reviewIdx;
  console.log("리뷰 번호:", reviewIdx);
});


document.querySelectorAll(".btnModifyReview").forEach(btn => {
  btn.addEventListener("click", () => {
    const reviewIdx = btn.dataset.reviewIdx;
    const content = btn.dataset.reviewContent;

    textarea.value = content;

    btnModify.dataset.reviewIdx = reviewIdx;
    btn.style.display = "none";
    btnModify.style.display = "inline-block";
  });
});


btn.addEventListener(`click`, ()=>{
    console.log("리뷰작성실행됨.");    
    const content = textarea.value
    if(!content){
        alert(`리뷰를 입력하세요`);
        textarea.focus();
        return;
    }

    const formData = new FormData();
    formData.append("festival_idx", festival_idx);       
    formData.append("review_content", content);      
    const file = uploadFile.files[0]; 
    if (file) formData.append("upload_file", file);      


    fetch("/review/write", {
    method: "POST",
    body: formData
    })
    .then(async response => {
    if (!response.ok) {
        const text = await response.text(); 
        console.error("서버 응답 오류:", text);
        alert("서버 오류 발생: " + response.status);
        return;
    }

    const data = await response.json(); 
    if (data.success) {
        alert("리뷰가 등록되었습니다.");
        location.reload();
    } else {
        alert(data.message);
    }
    })
    .catch(err => {
    console.error("에러", err);
    alert("요청 중 오류가 발생했습니다.");
    });
});

btnModify.addEventListener(`click`, ()=>{
  console.log("수정요청.")
  const content = textarea.value;
  if (!content) {
    alert(`리뷰를 입력하세요`);
    textarea.focus();
    return;
  }

  const formData = new FormData();
  const reviewIdx = btnModify.dataset.reviewIdx; 
  formData.append("review_idx", reviewIdx);      
  formData.append("review_content", content);
  const file = uploadFile.files[0];
  if (file) formData.append("upload_file", file);

  fetch("/review/modify", {
    method: "POST",
    body: formData
  })
    .then(async response => {
      if (!response.ok) {
        const text = await response.text();
        console.error("서버 응답 오류:", text);
        alert("서버 오류 발생: " + response.status);
        return;
      }

      const data = await response.json();
      if (data.success) {
        alert("리뷰가 수정되었습니다.");
        location.reload();
      } else {
        alert(data.message);
      }
    })
    .catch(err => {
      console.error("에러", err);
      alert("요청 중 오류가 발생했습니다.");
    });

});



