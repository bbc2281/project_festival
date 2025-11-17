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