function setUserSetting(key, value) {
    localStorage.setItem('plk-setting-' + key, value);
    applyUserSettings();
}

function readUserSetting(key, fallbackValue) {
    return localStorage.getItem('plk-setting-' + key) || fallbackValue;
}

function applyUserSettings() {
    var density = readUserSetting('density', 'normal');
    var fontScale = readUserSetting('font-scale', '1');

    document.documentElement.setAttribute('data-density', density);
    document.documentElement.setAttribute('data-font-scale', fontScale);
    document.documentElement.style.setProperty('--user-font-scale', fontScale);
    document.documentElement.setAttribute('data-reduced-motion', readUserSetting('reduced-motion', 'false'));
    document.documentElement.setAttribute('data-show-help', readUserSetting('show-help', 'true'));
}

applyUserSettings();
