/**
 * @author Sylvain Crocquevieille
 */
(function () {
    var themeName = localStorage.getItem('plk-color-theme') || 'default';
    document.documentElement.setAttribute('data-color-theme', themeName);

    if (localStorage.getItem('plk-theme') === 'dark') {
        document.documentElement.setAttribute('data-theme', 'dark');
    }
})();

function toggleTheme() {
    var root = document.documentElement;
    if (root.getAttribute('data-theme') === 'dark') {
        root.removeAttribute('data-theme');
        localStorage.removeItem('plk-theme');
    } else {
        root.setAttribute('data-theme', 'dark');
        localStorage.setItem('plk-theme', 'dark');
    }
}

function setColorTheme(themeName) {
    document.documentElement.setAttribute('data-color-theme', themeName);
    localStorage.setItem('plk-color-theme', themeName);
}
