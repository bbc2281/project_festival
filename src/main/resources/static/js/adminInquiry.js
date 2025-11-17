let currentInquiryId = null;

function openInquiryModal(el) {
  const title = el.getAttribute("data-title");
  const content = el.getAttribute("data-content");
  const answer = el.getAttribute("data-answer");
  const id = el.getAttribute("data-id");

  currentInquiryId = id;

  document.getElementById("modalTitle").innerText = title;
  document.getElementById("modalContent").innerText = content;

  const answerSection = document.getElementById("answerSection");
  const answerForm = document.getElementById("answerForm");
  const submitBtn = document.getElementById("submitAnswerBtn");

  if (answer && answer.trim() !== "") {
    // 답변이 존재할 경우
    answerSection.classList.remove("d-none");
    document.getElementById("answerContent").innerText = answer;
    answerForm.classList.add("d-none");
    submitBtn.classList.add("d-none");
  } else {
    // 답변이 없을 경우
    answerSection.classList.add("d-none");
    answerForm.classList.remove("d-none");
    submitBtn.classList.remove("d-none");
    document.getElementById("answerText").value = "";
  }

  const modal = new bootstrap.Modal(document.getElementById("inquiryDetailModal"));
  modal.show();
}

document.addEventListener('DOMContentLoaded', () => {
  const buttons = document.querySelectorAll('[id^="ansBtn"]');
  buttons.forEach(button => {
    button.addEventListener('click', () => {
      const tr = button.closest('tr');
      const inquiryLink = tr.querySelector('.inquiry-link');
      if (inquiryLink) {
        openInquiryModal(inquiryLink);
      }
    });
  });
});

// 
document.getElementById("submitAnswerBtn").addEventListener("click", () => {
  const answerText = document.getElementById("answerText").value.trim();
  if (!answerText) {
    alert("답변 내용을 입력해주세요.");
    return;
  }

  fetch(`/admin/answerInquiry`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      inquiry_idx: currentInquiryId,
      inquiry_answer: answerText
    })
  })
    .then(res => res.ok ? res.text() : Promise.reject())
    .then(() => {
      alert("답변이 등록되었습니다.");
      location.reload();
    })
    .catch(() => alert("답변 등록 중 오류가 발생했습니다."));
});