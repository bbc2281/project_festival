const btn = document.getElementById(`btnAddReview`);
const btnModify = document.getElementById(`btnModifyReview`)
const textarea = document.getElementById(`reviewText`);
const uploadFile = document.getElementById(`upload_file`);

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
})



