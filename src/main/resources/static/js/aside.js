// 메뉴 강조 (JS 유지)
document.addEventListener('DOMContentLoaded', () => {
    const links = document.querySelectorAll('.menu a');
    let currentPath = window.location.pathname.split("/").pop();
    links.forEach(link => {
        if(currentPath === 'company'){
            currentPath = 'member';
        }
        let href = link.getAttribute('href').split("/").pop();
        if(href === 'company'){
            href = 'member'
        }
        if (currentPath === 'info' && href === 'info') {
            link.classList.add('active');
        } else if (currentPath === href) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
});