document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector("form");
  if (!form) return;

  const titleInput = form.querySelector("input[name='board_title']");
  const categorySelect = form.querySelector("select[name='board_category']");
  const contentHidden = form.querySelector("#board_content_hidden");

  const editorContainer = document.querySelector("#editor");
  if (!editorContainer || editorContainer.dataset.initialized === "true") return;
  editorContainer.dataset.initialized = "true";

  let initialContent = "";
  const originalTextarea = form.querySelector("#board_content_original");
  
  if (originalTextarea && originalTextarea.value.trim()) {
    initialContent = originalTextarea.value.trim(); 
  }
  const editor = new toastui.Editor({
    el: editorContainer,
    height: "400px",
    initialEditType: "wysiwyg",
    previewStyle: "vertical",
    language: "ko-KR",
    initialValue: initialContent
  });

  // ✅ 유효성 검사
  const validateForm = () => {
    if (!titleInput.value.trim()) {
      alert("제목을 입력해주세요.");
      titleInput.focus();
      return false;
    }

    if (!categorySelect.value.trim()) {
      alert("카테고리를 선택해주세요.");
      categorySelect.focus();
      return false;
    }

    const contentHTML = editor.getHTML();
    if (!contentHTML || contentHTML.trim() === "<p><br></p>") {
      alert("내용을 입력해주세요.");
      editor.focus();
      return false;
    }

    console.log(contentHTML);
    contentHidden.value = contentHTML;
    return true;
  };

  form.addEventListener("submit", (e) => {
    if (!validateForm()) {
      e.preventDefault();
    }
  });
});
