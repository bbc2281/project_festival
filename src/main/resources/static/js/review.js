// ===============================
// review.js (완성본)
// ===============================

// 요소 캐시
const btnAdd = document.getElementById("btnAddReview");
const textarea = document.getElementById("reviewText");
const uploadFile = document.getElementById("reviewImage");

// 이미지 미리보기 컨테이너 준비 (파일 선택창 '위'에 위치)
const reviewPreview = document.getElementById("reviewPreview") || document.createElement("div");
if (!reviewPreview.id) {
  reviewPreview.id = "reviewPreview";
  reviewPreview.className = "d-flex flex-wrap gap-2 mb-2";
  uploadFile.parentNode.insertBefore(reviewPreview, uploadFile);
}

// ===============================
// 이미지 미리보기 + 개별 삭제
// ===============================
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
      delBtn.type = "button";
      delBtn.innerHTML = '<i class="bi bi-x-lg"></i>';
      delBtn.className = "position-absolute btn btn-sm btn-danger rounded-circle";
      Object.assign(delBtn.style, {
        top: "0",
        right: "0",
        transform: "translate(40%, -40%)",
        width: "22px",
        height: "22px",
        fontSize: "10px",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        border: "none",
        backgroundColor: "#ff4d4d",
        color: "white",
        boxShadow: "0 0 4px rgba(0,0,0,0.2)",
        cursor: "pointer",
        zIndex: "10"
      });

      delBtn.addEventListener("mouseenter", () => {
        delBtn.style.backgroundColor = "#ff6666";
        delBtn.style.transform = "translate(40%, -40%) scale(1.1)";
      });
      delBtn.addEventListener("mouseleave", () => {
        delBtn.style.backgroundColor = "#ff4d4d";
        delBtn.style.transform = "translate(40%, -40%) scale(1)";
      });

      delBtn.addEventListener("click", () => {
        container.remove();
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

// 공용 헬퍼: JSON/텍스트 유연 파싱
async function parseJsonFlexible(res) {
  const text = await res.text();
  try {
    return JSON.parse(text);
  } catch {
    console.error("서버가 JSON이 아닌 응답을 반환:", text);
    return { success: false, message: "서버가 JSON 응답을 반환하지 않았습니다." };
  }
}

// ===============================
// 리뷰 수정 (이벤트 위임)
//  - 아이콘(<i>) 클릭도 처리되도록 closest로 버튼 찾기
//  - 입력칸은 .review-content(span) 바로 뒤(afterend)에 삽입 → DOM 안정
// ===============================
document.addEventListener("click", async (e) => {
  const modifyBtn = e.target.closest(".btnModifyReview");
  if (!modifyBtn) return;

  const reviewBox = modifyBtn.closest(".review-box");
  const span = reviewBox.querySelector(".review-content");
  const reviewIdx = modifyBtn.dataset.reviewIdx;
  const existingImg = reviewBox.querySelector(".review-image");

  // 저장 처리
  if (modifyBtn.classList.contains("saving")) {
    const input = reviewBox.querySelector(".review-edit-input");
    if (!input) return;

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

    try {
      const res = await fetch("/review/modify", { method: "POST", body: formData });
      const data = await parseJsonFlexible(res);
      if (data.success) {
        // 화면 갱신
        span.textContent = newContent;
        span.style.display = "inline";

        // 임시 UI 제거
        input.remove();
        const preview = reviewBox.querySelector(".review-temp-preview");
        const notice = reviewBox.querySelector(".review-temp-notice");
        if (preview) preview.remove();
        if (notice) notice.remove();

        modifyBtn.textContent = "수정하기";
        modifyBtn.classList.remove("saving");
        if (existingImg) existingImg.style.display = "block";

        alert("리뷰가 수정되었습니다.");
        location.reload();
      } else {
        alert(data.message || "수정 실패");
      }
    } catch (err) {
      console.error("수정 요청 에러", err);
      alert("요청 중 오류가 발생했습니다.");
    }
    return;
  }

  // 이미 수정 모드면 중복 생성 방지
  if (reviewBox.querySelector(".review-edit-input")) return;

  // === 수정 모드 진입 ===
  const originalText = span.textContent;
  span.style.display = "none";

  // 1) 텍스트 입력칸: span 바로 뒤에 삽입 (DOM 안전)
  const input = document.createElement("input");
  input.type = "text";
  input.className = "form-control mt-2 review-edit-input";
  input.value = originalText;
  span.insertAdjacentElement("afterend", input);

  // 2) 이미지 선택 인풋: 입력칸 바로 뒤
  const imgInput = document.createElement("input");
  imgInput.type = "file";
  imgInput.className = "form-control mb-2 review-modify-file";
  imgInput.name = "upload_file";
  input.insertAdjacentElement("afterend", imgInput);

  // 3) 기존 이미지 미리보기 + 삭제 안내
  if (existingImg) {
    const preview = document.createElement("img");
    existingImg.style.display = "none";
    preview.src = existingImg.src;
    preview.className = "img-fluid rounded mb-2 review-temp-preview";
    preview.style.maxHeight = "120px";

    const notice = document.createElement("small");
    notice.textContent = "새 이미지를 선택하면 기존 이미지는 교체됩니다.";
    notice.className = "text-muted d-block mb-2 review-temp-notice";

    imgInput.insertAdjacentElement("afterend", preview);
    preview.insertAdjacentElement("afterend", notice);
  }

  // 새 이미지 선택 시 즉시 미리보기 추가
  imgInput.addEventListener("change", (ev) => {
    const file = ev.target.files?.[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (fev) => {
      // 기존 temp preview/notice 제거 후 새로 표시
      const oldPreview = reviewBox.querySelector(".review-temp-preview");
      const oldNotice = reviewBox.querySelector(".review-temp-notice");
      if (oldPreview) oldPreview.remove();
      if (oldNotice) oldNotice.remove();

      const newPreview = document.createElement("img");
      newPreview.src = fev.target.result;
      newPreview.className = "img-fluid rounded mb-2 border border-primary review-temp-preview";
      newPreview.style.maxHeight = "120px";

      const notice = document.createElement("small");
      notice.textContent = "저장 시 이 이미지로 교체됩니다.";
      notice.className = "text-muted d-block mb-2 review-temp-notice";

      imgInput.insertAdjacentElement("afterend", newPreview);
      newPreview.insertAdjacentElement("afterend", notice);
    };
    reader.readAsDataURL(file);
  });

  // 버튼 상태 전환
  modifyBtn.textContent = "저장";
  modifyBtn.classList.add("saving");
});

// ===============================
// 리뷰 등록
// ===============================
btnAdd.addEventListener("click", async () => {
  const content = (textarea.value || "").trim();
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

  try {
    const res = await fetch("/review/write", { method: "POST", body: formData });
    if (!res.ok) {
      const text = await res.text();
      console.error("서버 응답 오류:", text);
      alert("서버 오류 발생: " + res.status);
      return;
    }
    const data = await parseJsonFlexible(res);
    if (data.success) {
      alert("리뷰가 등록되었습니다.");
      location.reload();
    } else {
      alert(data.message || "등록 실패");
    }
  } catch (err) {
    console.error("등록 에러", err);
    alert("요청 중 오류가 발생했습니다.");
  }
});

// ===============================
// 리뷰 삭제 (이벤트 위임 + closest로 보강)
// ===============================
document.addEventListener("click", async (e) => {
  const delBtn = e.target.closest(".btnDeleteReview");
  if (!delBtn) return;

  if (!confirm("정말 삭제하시겠습니까?")) return;

  try {
    const res = await fetch("/review/delete", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ review_idx: delBtn.dataset.reviewIdx })
    });
    if (!res.ok) {
      const text = await res.text();
      console.error("서버 응답 오류", text);
      alert("서버 오류 발생 " + res.status);
      return;
    }
    const data = await parseJsonFlexible(res);
    if (data.success) {
      alert("리뷰 삭제가 완료되었습니다.");
      location.reload();
    } else {
      alert(data.message || "삭제 실패");
    }
  } catch (err) {
    console.error("삭제 에러", err);
    alert("요청 중 오류가 발생했습니다.");
  }
});

// ===============================
// 리뷰 개수 카운트 표시 (즉시 실행 버전)
// ===============================
(function updateReviewCount() {
  const reviewBoxes = document.querySelectorAll(".review-box");
  const reviewCount = document.getElementById("reviewCount");
  if (reviewBoxes && reviewCount) {
    reviewCount.textContent = `(${reviewBoxes.length})`;
  }
})();

