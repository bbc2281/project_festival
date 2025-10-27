   document.getElementById("btnSendChat").addEventListener("click", async ()=>{
      let msg = chatInput.value.trim();
      chat_ok = await testMsg(msg);
      if(!chat_ok) return;

      await sendMessage();
    });

    chatInput.addEventListener("keydown", async (e) => {
      if (e.key === "Enter" && !e.shiftKey) {
        let msg = chatInput.value.trim();
        chat_ok = await testMsg(msg);
        if(!chat_ok) return;
        await sendMessage();
      }
    });

        
//메시지 전송(발신)
function sendMessage(){
    let message = chatInput.value;
    
    let chatMessage = {
        chat_type : "CHAT",
        chat_room : parseInt(roomId),
        chat_sender : parseInt(loginMemberIdx),
        chat_message : message
    };//서버로 보낼 메시지 객체(자바스크립트 객체)
    if(stompClient && stompClient.connect){// 웹소켓으로 서버와 사용자가 연결된 경우
        console.log(`메시지 전송: ${chatMessage}`);
        stompClient.send(`/app/chat/${roomId}`, {}, JSON.stringify(chatMessage));//서버로 메시지 전송

        chatInput.value = "";//입력창 초기화
    }
}
    function showMessage(chatMessage) {
    let messageElement = document.createElement("div");
    messageElement.classList.add("message");//클래스 추가

    if (chatMessage.chat_sender == loginMemberIdx) {//내가 보낸 메시지
        messageElement.classList.add("my-message");
        messageElement.innerHTML = `<strong>${loginMember}</strong>: ${chatMessage.chat_message}`;
    } else {//다른 사람이 보낸 메시지
        messageElement.classList.add("other-message");
        messageElement.innerHTML = `<strong>${chatMessage.sender_email}</strong>: ${chatMessage.chat_message}`;
    }
            
    chatBox.appendChild(messageElement);
    chatBox.scrollTop = chatBox.scrollHeight;
}