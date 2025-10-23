async function sendMsg(msg) {
    if (!msg) return false;

    try {
    const response = await fetch("http://localhost:8000/checkWord", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ word: msg })
    });

    const data = await response.json();

    if (response.ok) {
        return true;
    } else {
        alert(`❗❗ ${data.detail} ❗❗`);
        return false;
    }
    
    } catch (e) {
        alert("서버 오류가 발생했습니다.");
        return false;
    }
}