const GOOGLE_CLIENT_ID = "521659150531-5t1fekikuql7qalfg9h9pcea363i10jr.apps.googleusercontent.com";

window.onload = function () {
    google.accounts.id.initialize({
        client_id: GOOGLE_CLIENT_ID,
        callback: handleCredentialResponse
    });

    google.accounts.id.renderButton(
        document.getElementById("googleSignInBtn"),
        { theme: "outline", size: "large", width: "300" }
    );
};

function handleCredentialResponse(res) {
    if (!res || !res.credential) {
        alert("구글 로그인 실패: 토큰 없음");
        return;
    }

    fetch('/api/v1/auth/login/google', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id_token: res.credential })
    })
        .then(r => r.json())
        .then(d => {
            const isSuccess =
                d.status === "success" ||
                d.success === true ||
                d.message === "로그인 성공";

            if (!isSuccess) {
                alert("서버 로그인 실패: " + (d.message || "알 수 없는 오류"));
                return;
            }

            alert("구글 로그인 성공");
            location.href = "/";
        })
}
document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("my-google-btn").addEventListener("click", (e) => {
        e.preventDefault();

        const realButton = document.querySelector("#googleSignInBtn div[role='button']");
        if (realButton) {
            realButton.click();   // ← 이게 진짜 핵심!
        } else {
            alert("구글 로그인 초기화 오류: 버튼 렌더링 실패");
        }
    });
});