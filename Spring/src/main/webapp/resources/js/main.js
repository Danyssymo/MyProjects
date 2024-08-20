function loadPage(pageNumber) {
    const container = document.getElementById('islands-container');

    // Применение анимации исчезновения
    container.style.opacity = 0;

    fetch('/pagination?page=' + pageNumber)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(html => {
            container.innerHTML = html;

            // Применение анимации появления
            setTimeout(() => {
                container.style.opacity = 1;
            }, 300);
        })
        .catch(error => console.error('Error loading page:', error));
}
