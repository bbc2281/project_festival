// 메뉴 강조 (JS 유지)
document.addEventListener('DOMContentLoaded', () => {
    const links = document.querySelectorAll('.menu a');
    const currentPath = window.location.pathname.split("/").pop();
    links.forEach(link => {
        const href = link.getAttribute('href').split("/").pop();
        if (currentPath === 'info' && href === 'info') {
            link.classList.add('active');
        } else if (currentPath === href) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
});