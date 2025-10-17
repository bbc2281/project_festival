function sendMessage() {
  const contextPath = window.location.origin;
  const input = document.getElementById(`userInput`); //입력내용
  const messages = document.getElementById(`messages`); //답변내용
  const userMessage = input.value;

  if (userMessage == ``) {
    alert(`메세지를 입력하세요`);
    return;
  } //내용없이 보낼경우

  //사용자 메세지 출력
  messages.innerHTML += `<div><strong>질문 : </strong>${userMessage}</div>`;

  //서버에 메시지 전송
  fetch(`${contextPath}/assistant`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ message: userMessage }), //json 형태로 질문을 변환
  })
    .then((response) => response.json())
    .then((data) => {
      const aiResponse = data.response;
      messages.innerHTML += `<div><strong>답변 : </strong>${aiResponse}</div><br>`; //ai의 답변출력
    })
    .catch((error) => {
      console.log(`error`, error);
      messages.innerHTML += `<div><strong>오류가 발생했습니다.</strong></div><br>`;
    });
  input.value = ``; //입력창 초기화
}
