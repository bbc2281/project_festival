document.addEventListener('DOMContentLoaded', () => {
    const editButton = document.getElementById('edit');
    const reviewsButton = document.getElementById('myreview');

    // 1. 회원정보 수정 버튼 클릭 이벤트
    if (editButton) {
        editButton.addEventListener('click', () => {
            console.log('Navigating to /mypage/mypageedit');
            window.location.href = '/mypage/mypageedit';
        });
    }

    // 2. 내 리뷰 보기 버튼 클릭 이벤트
    if (reviewsButton) {
        reviewsButton.addEventListener('click', () => {
            console.log('Navigating to /mypage/myreview');
            window.location.href = '/mypage/mypagereview';
        });
    }
});
