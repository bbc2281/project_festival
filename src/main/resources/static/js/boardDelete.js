function confirmDelete(boardIdx) {
    if (confirm("정말 삭제하시겠습니까?")) {
        const form = document.createElement("form");
        form.method = "post";
        form.action = "/board/delete";

        const input = document.createElement("input");
        input.type = "hidden";
        input.name = "board_idx";
        input.value = boardIdx;

        form.appendChild(input);
        document.body.appendChild(form);
        form.submit();
    }
}
