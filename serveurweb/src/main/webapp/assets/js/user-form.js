/**
 * @author Sylvain Crocquevieille
 */
function getInputBySuffix(idSuffix) {
    return document.querySelector('[id$=":' + idSuffix + '"]');
}

function buildSuggestedUsername(firstName, lastName) {
    var first = (firstName || '').trim();
    var last = (lastName || '').trim();
    if (!first || !last) {
        return '';
    }
    return (first.charAt(0) + last.charAt(0)).toUpperCase();
}

function markUsernameEdited(usernameId) {
    var username = getInputBySuffix(usernameId);
    if (username) {
        username.dataset.userEdited = 'true';
    }
}

function updateAutoUsername(firstNameId, lastNameId, usernameId) {
    var firstName = getInputBySuffix(firstNameId);
    var lastName = getInputBySuffix(lastNameId);
    var username = getInputBySuffix(usernameId);
    if (!firstName || !lastName || !username || username.dataset.userEdited === 'true') {
        return;
    }
    username.value = buildSuggestedUsername(firstName.value, lastName.value);
}
