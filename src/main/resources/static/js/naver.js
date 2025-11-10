const naver_login = new naver_id_login("6ODyCh148nT7pPRiBkqK", "http://localhost:8080/api/v1/auth/login/naver");
const state = naver_login.getUniqState();
naver_login.response_type = "code";
naver_login.setButton("white", 2,40);
naver_login.setDomain("http://localhost:8080");
naver_login.setState(state);
naver_login.setPopup();
naver_login.init_naver_id_login();
document.getElementById(`my-naver-btn`).addEventListener(`click`, (e)=>{
    e.preventDefault();
    document.querySelector("#naver_id_login a").click();
});

function naverSignInCallback() {
    const userData = {
        email: naverLogin.getProfileData('email'),
        nickname: naverLogin.getProfileData('nickname'),
        id: naverLogin.getProfileData('id'),
        name: neverLogin.getProfileData('name'),
        gender: neverLogin.getProfileData('gender'),
        birthday: neverLogin.getProfileData('birthday'),
        birthyear: neverLogin.getProfileData('birthyear'),
        mobile: neverLogin.getProfileData('mobile')
    };

    // 서버로 사용자 정보 전송 (fetch 사용)
    fetch('/login/naver', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/'; // 로그인 후 리다이렉트
        } else {
            return response.text().then(text => { throw new Error(text) });
        }
    })
    .catch(error => {
        alert('로그인 처리 중 오류가 발생했습니다: ' + error.message);
    });
}