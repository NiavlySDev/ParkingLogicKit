/**
 * @author Sylvain Crocquevieille
 */
function applyThemePreference() {
    var root = document.documentElement;
    var storedTheme = localStorage.getItem('plk-theme');

    if (storedTheme === 'dark') {
        root.setAttribute('data-theme', 'dark');
    } else {
        root.removeAttribute('data-theme');
    }
}

(function () {
    var themeName = localStorage.getItem('plk-color-theme') || 'default';
    document.documentElement.setAttribute('data-color-theme', themeName);
    applyThemePreference();
})();

function toggleTheme() {
    var root = document.documentElement;
    if (root.getAttribute('data-theme') === 'dark') {
        root.removeAttribute('data-theme');
        localStorage.setItem('plk-theme', 'light');
    } else {
        root.setAttribute('data-theme', 'dark');
        localStorage.setItem('plk-theme', 'dark');
    }
}

function setColorTheme(themeName) {
    document.documentElement.setAttribute('data-color-theme', themeName);
    localStorage.setItem('plk-color-theme', themeName);
}
