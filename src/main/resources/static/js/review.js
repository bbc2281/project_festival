const btnAdd = document.getElementById("btnAddReview");
const textarea = document.getElementById("reviewText");
const uploadFile =  document.getElementById("reviewImage");
const reviewPreview = document.getElementById("reviewPreview") || document.createElement("div");


if (!reviewPreview.id) {
  reviewPreview.id = "reviewPreview";
  reviewPreview.className = "d-flex flex-wrap gap-2 mb-2";
  uploadFile.parentNode.insertBefore(reviewPreview, uploadFile.nextSibling);
}

// 이미지 미리보기
// 이미지 미리보기 + 개별 삭제
uploadFile.addEventListener("change", (e) => {
  reviewPreview.innerHTML = "";
  const files = Array.from(e.target.files);

  files.forEach((file, index) => {
    const reader = new FileReader();
    reader.onload = (ev) => {
      const container = document.createElement("div");
      container.className = "position-relative d-inline-block me-2 mb-2";

      const img = document.createElement("img");
      img.src = ev.target.result;
      img.style.height = "90px";
      img.style.borderRadius = "8px";
      img.style.boxShadow = "0 0 5px rgba(0,0,0,0.2)";
      img.style.objectFit = "cover";

      const delBtn = document.createElement("button");
      delBtn.innerHTML = "✕";
      delBtn.className = "btn btn-sm btn-danger position-absolute top-0 end-0 translate-middle rounded-circle";
      delBtn.style.fontSize = "10px";
      delBtn.style.padding = "2px 5px";

      delBtn.addEventListener("click", () => {
        container.remove();
        // input 파일에서 해당 이미지 제거
        const dt = new DataTransfer();
        files.forEach((f, i) => {
          if (i !== index) dt.items.add(f);
        });
        uploadFile.files = dt.files;
      });

      container.appendChild(img);
      container.appendChild(delBtn);
      reviewPreview.appendChild(container);
    };
    reader.readAsDataURL(file);
  });
});

document.querySelectorAll(".btnModifyReview").forEach(btn => {
  btn.addEventListener("click", () => {
    const reviewBox = btn.closest(".review-box");
    const span = reviewBox.querySelector(".review-content");
    const reviewIdx = btn.dataset.reviewIdx;
    const existingImg = reviewBox.querySelector(".review-image");

    console.log("리뷰아이디엑스 : " ,reviewIdx);

    // 이미 수정 중이면 저장 처리
    if (btn.classList.contains("saving")) {
    console.log("저장처리 들어왔는지?")
      const input = reviewBox.querySelector("input");
      const newContent = input.value.trim();
      if (!newContent) {
        alert("내용을 입력하세요");
        input.focus();
        return;
      }

      const formData = new FormData();
      formData.append("review_idx", reviewIdx);
      formData.append("review_content", newContent);

        const imgInput = reviewBox.querySelector(".review-modify-file");
        if (imgInput && imgInput.files.length > 0) {
        formData.append("upload_file", imgInput.files[0]);
        }
        console.log("수정되는 이미지 잡히는지 ?" + imgInput)
        
      fetch("/review/modify", {
        method: "POST",
        body: formData
      })
        .then(async res => {
          const data = await res.json();
          if (data.success) {
            span.textContent = newContent;
            span.style.display = "inline";
            input.remove();
            btn.textContent = "수정";
            btn.classList.remove("saving");
              if (existingImg) {
                existingImg.style.display = "block";
                 }
            
            alert("리뷰가 수정되었습니다.");
            location.reload();
          } else {
            alert(data.message || "수정 실패");
          }
        })
        .catch(err => {
          console.error("에러", err);
          alert("요청 중 오류가 발생했습니다.");
        });

      return; // 저장 처리 후 종료
    }

    // 수정 모드 진입
    const originalText = span.textContent;    
    span.style.display = "none";
    const input = document.createElement("input");
    input.type = "text";
    input.className = "form-control mt-2";
    input.value = originalText;
    reviewBox.insertBefore(input, btn);

    const imgInput = document.createElement("input");
    imgInput.type = "file";
    imgInput.className = "form-control mb-2 review-modify-file"; 
    imgInput.name = "upload_file";
    reviewBox.insertBefore(imgInput, btn);

    imgInput.addEventListener("change", (e) => {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = (ev) => {
        const newPreview = document.createElement("img");
        newPreview.src = ev.target.result;
        newPreview.className = "img-fluid rounded mb-2 border border-primary";
        newPreview.style.maxHeight = "120px";


        

        const notice = document.createElement("small");
        notice.textContent = "저장 시 기존 이미지는 새 이미지로 교체됩니다.";
        notice.className = "text-muted d-block mb-2";

        if (existingImg) existingImg.style.opacity = "0.5";

        reviewBox.insertBefore(newPreview, btn);
        reviewBox.insertBefore(notice, btn);
        };
        reader.readAsDataURL(file);
    }
    });




    if (existingImg) {
    const preview = document.createElement("img");
    existingImg.style.display = "none"
    preview.src = existingImg.src;
    preview.className = "img-fluid rounded mb-2";
    preview.style.maxHeight = "120px";

    const delBtn = document.createElement("button");
    delBtn.textContent = "✕ ";
    delBtn.className = "btn btn-sm btn-outline-danger mb-2";
    delBtn.addEventListener("click", () => {
        const formData = new FormData();
        formData.append("review_idx", reviewIdx);

    fetch("/review/delete-image", {
        method: "POST",
        body: formData
    })
    .then(res => res.json())
    .then(data => {
    if (data.success) {
        preview.remove();
        delBtn.remove();
        alert("이미지가 삭제되었습니다.");
    } else {
        alert(data.message || "삭제 실패");
    }
    })
    .catch(err => {
    console.error("이미지 삭제 오류", err);
    alert("요청 중 오류 발생");
    });
 
    });
    reviewBox.insertBefore(preview, btn);
    reviewBox.insertBefore(delBtn, btn);
    }

    btn.textContent = "저장";
    btn.classList.add("saving");
  });
});


// 리뷰 등록
btnAdd.addEventListener("click", () => {
  const content = textarea.value.trim();
  if (!content) {
    alert("리뷰를 입력하세요");
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
    .then(async res => {
      if (!res.ok) {
        const text = await res.text();
        console.error("서버 응답 오류:", text);
        alert("서버 오류 발생: " + res.status);
        return;
      }
      const data = await res.json();
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

document.querySelectorAll(".btnDeleteReview").forEach(btn => {
    btn.addEventListener("click" ,() => {

        fetch("/review/delete",{
            method : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ review_idx: btn.dataset.reviewIdx })
        })
        .then(async response => {
            if (!response.ok){
                const text = await response.text();
                console.error("서버 응답 오류" + text)
                alert("서버 오류 발생 " + response.status);
                return;
            }
            const data = await response.json();
            if(data.success) {
                alert("리뷰 삭제가 완료 되었습니다.")
                location.reload();
            }else{
                alert(data.message);
            }
        })
        .catch(err => {
            console.error("에러" , err)
            alert("요청 중 오류가 발생헀습니다.")
        });
    });
});