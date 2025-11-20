btnSendChat.addEventListener("click", async ()=>{
    if(!isLogin) return;
    let msg = chatInput.value.trim();
    if(!msg) {
        alert("빈칸 또는 공백은 보낼 수 없습니다")
        chatInput.value = "";
        return;
    }
    chat_ok = await testMsg(msg);
    if(!chat_ok) return;

    await sendMessage();
});

chatInput.addEventListener("keydown", async (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
        if(!isLogin) return;
        let msg = chatInput.value.trim();
        if(!msg) {
            alert("빈칸 또는 공백은 보낼 수 없습니다")
            chatInput.value = "";
            return;
        }
        chat_ok = await testMsg(msg);
        if(!chat_ok) return;
        await sendMessage();
    }
});

        
//메시지 전송(발신)
function sendMessage(){
    if (!isChatAllowed()) {
        alert("채팅은 축제 시작 1달 전부터 종료 1달 후까지만 가능합니다.");
        return;
    }
    let message = chatInput.value;
    
    let chatMessage = {
        chat_type : "CHAT",
        chat_room : parseInt(roomId),
        chat_sender : parseInt(loginMemberIdx),
        chat_message : message
    };//서버로 보낼 메시지 객체(자바스크립트 객체)
    if(stompClient && stompClient.connect){// 웹소켓으로 서버와 사용자가 연결된 경우
        stompClient.send(`/app/chat/${roomId}`, {}, JSON.stringify(chatMessage));//서버로 메시지 전송

        chatInput.value = "";//입력창 초기화
    }
}
function showMessage(chatMessage) {
    let messageElement = document.createElement("div");
    // 입장메시지
    if (chatMessage.chat_type === "SYSTEM") {
        messageElement.className = "bubble system";
        messageElement.innerHTML =
            `<span>${chatMessage.chat_message}<br>${getTimeString()}</span>`;
    }
    // 본인메시지
    else if (chatMessage.chat_sender == loginMemberIdx) {
        messageElement.className = "bubble me";
        messageElement.innerHTML =
            `<span>${chatMessage.chat_message}</span>
             <small>${loginMember} · ${getTimeString()}</small>`;
    }
    // 상대메시지
    else {
        messageElement.className = "bubble other";
        messageElement.innerHTML =
            `<span>${chatMessage.chat_message}</span>
             <small>${chatMessage.sender_name} · ${getTimeString()}</small>`;
    }
    chatBox.appendChild(messageElement);
    chatBox.scrollTop = chatBox.scrollHeight;
}

function getTimeString() {
    const now = new Date();
    return now.getHours().toString().padStart(2, '0') + ':' +
           now.getMinutes().toString().padStart(2, '0');
}