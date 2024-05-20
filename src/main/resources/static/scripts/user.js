async function showUserDetail(container, email) {
    showLoader(container);
    const response = await fetch("/users/detail?email=" + email);
    if (!response.ok) {
        showError(container);
        return;
    }
    const json = await response.json();
    const raw = getUserTableRaw(json);
    container.appendChild(raw);
}

function getUserTableRaw(jsonData) {
    const roles = jsonData.roles.map(item => item.title.replace("ROLE_", ""))

    raw = document.createElement("tr");
    raw.innerHTML = `
            <td>${jsonData.id ?? ""}</td>
            <td>${jsonData.name ?? ""}</td>
            <td>${jsonData.lastName ?? ""}</td>
            <td>${jsonData.age ?? ""}</td>
            <td>${jsonData.email ?? ""}</td>
            <td>
                ${roles.join(" ")}
            </td>`
    return raw
}

function showLoader(element) {

}

function showError(container, details) {
    const errorElement = document.createElement("div");
    errorElement.innerHTML = "ERROR loading"
    container.appendChild(errorElement);
}