document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector("form");
  const titleInput = document.querySelector("input[name='board_title']");
  const categorySelect = document.querySelector("select[name='board_category']");
  const contentTextarea = document.querySelector("textarea[name='board_content']");

  form.addEventListener("submit", function (e) {
    if (!titleInput.value.trim()) {
      alert("제목을 입력해주세요.");
      titleInput.focus();
      e.preventDefault();
      return;
    }

    if (!categorySelect.value.trim()) {
      alert("카테고리를 선택해주세요.");
      categorySelect.focus();
      e.preventDefault();
      return;
    }

    if (!contentTextarea.value.trim()) {
      alert("내용을 입력해주세요.");
      contentTextarea.focus();
      e.preventDefault();
      return;
    }
  });
});
