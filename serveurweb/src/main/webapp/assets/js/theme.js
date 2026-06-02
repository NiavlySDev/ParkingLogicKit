/**
 * @author Sylvain Crocquevieille
 */
var PLK_THEME_QUERY = window.matchMedia ? window.matchMedia('(prefers-color-scheme: dark)') : null;

function applyThemePreference() {
    var root = document.documentElement;
    var storedTheme = localStorage.getItem('plk-theme');
    var darkBySystem = PLK_THEME_QUERY && PLK_THEME_QUERY.matches;

    if (storedTheme === 'dark' || (!storedTheme && darkBySystem)) {
        root.setAttribute('data-theme', 'dark');
    } else {
        root.removeAttribute('data-theme');
    }
}

(function () {
    var themeName = localStorage.getItem('plk-color-theme') || 'default';
    document.documentElement.setAttribute('data-color-theme', themeName);
    applyThemePreference();

    if (PLK_THEME_QUERY) {
        var listener = function () {
            if (!localStorage.getItem('plk-theme')) {
                applyThemePreference();
            }
        };
        if (PLK_THEME_QUERY.addEventListener) {
            PLK_THEME_QUERY.addEventListener('change', listener);
        } else if (PLK_THEME_QUERY.addListener) {
            PLK_THEME_QUERY.addListener(listener);
        }
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
