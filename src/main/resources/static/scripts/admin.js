async function showAdminPanelUserDetail(container) {
    showLoader(container);
    container.innerHTML = "";
    const response = await fetch("/users/");
    if (!response.ok) {
        showError(container);
        return;
    }
    const json = await response.json();
    json.forEach(item => {
        const raw = getUserTableRaw(item);
        console.log(raw);
        container.appendChild(raw);
    })
}

function getUserTableRaw(jsonData) {
    const roles = jsonData.roles.map(item => item.title.replace("ROLE_", ""))

    raw = document.createElement("tr");
    console.log(jsonData)
    raw.innerHTML = `
            <td>${jsonData.id ?? ""}</td>
            <td>${jsonData.name ?? ""}</td>
            <td>${jsonData.lastName ?? ""}</td>
            <td>${jsonData.age ?? ""}</td>
            <td>${jsonData.email ?? ""}</td>
            <td>
                ${roles.join(" ")}
            </td>
            <td>
                <button type="button" class="btn btn-info" onclick="editUser('${jsonData.email}')">
                    Edit
                </button>                               
            </td>
            <td>
                <button type="button" class="btn btn-danger" onclick="deleteUser('${jsonData.email}')">
                    Delete
                </button>  
            </td>`
    return raw
}

async function editUser(email) {
    const modalWindow = document.getElementById('editUserModal');
    modalWindow.innerHTML = "";
    modalWindow.appendChild(getModalContent());
    const myModal = new bootstrap.Modal(modalWindow, {})
    let response = await fetch("/users/detail?email=" + email);
    if (!response.ok) {
        showError(modalWindow);
        return;
    }
    const userInfo = await response.json();

    response = await fetch("/roles/");

    if (!response.ok) {
        showError(modalWindow);
        return;
    }
    const roles = await response.json();

    const id = document.getElementById("input-id");
    id.value = userInfo.id;
    const emailElement = document.getElementById("email");
    emailElement.value = userInfo.email;
    const name = document.getElementById("first-name");
    name.value = userInfo.name;
    const lastName = document.getElementById("last-name");
    lastName.value = userInfo.lastName;
    const age = document.getElementById("age");
    age.value = userInfo.age;
    const password = document.getElementById("password");
    const rolesElement = document.getElementById("roles");
    roles.forEach(item => {
        const option = document.createElement("option");
        option.id = "role-id-" + item.id;
        option.value = item.id;
        option.text = item.title;
        rolesElement.appendChild(option);
    });
    userInfo.roles.forEach(item => {
        document.getElementById(`role-id-${item.id}`).setAttribute("selected", "true");
    })

    document.getElementById("edit-user-form").addEventListener("submit", (event) => {
        submitEditForm(event);
        myModal.hide();
    })

    myModal.show();
}

async function submitEditForm(event) {
    event.preventDefault();
    const form = event.target;
    console.log(Array.from(
        document.getElementById("roles").querySelectorAll("[selected=true]"))
        .map(item => item.value));
    const userInfo = {
        name: document.getElementById("first-name").value,
        lastName: document.getElementById("last-name").value,
        age: document.getElementById("age").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        roles: Array.from(
            document.getElementById("roles").options)
            .filter(item => item.selected).map(item => ({id: +item.value}))
    }
    console.log(JSON.stringify(userInfo));
    const response = await fetch("/users", {
        method: 'PATCH',
        body: JSON.stringify(userInfo),
        headers: {
            "Content-Type": "application/json",
        },
    });
    if (response.ok) {
        showAdminPanelUserDetail(document.getElementById("userinfo-container__body"))
    }
}

function getModalContent() {
    const content = document.createElement("div");
    content.className = "modal-dialog modal-dialog-centered";
    content.innerHTML = `
    <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editUserModalLabel">Edit user</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="edit-user-form">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="input-id"
                                   class="form-label">
                                <b>ID</b>
                            </label>
                            <input readonly type="text" class="form-control"
                                   id="input-id"
                                   name="id"
                            >
                        </div>
                        <div class="mb-3">
                            <label for="first-name"
                                   class="form-label"><b>First name</b></label>
                            <input type="text" class="form-control"
                                   id="first-name"
                                   name="name"
                            >
                        </div>
                        <div class="mb-3">
                            <label for="last-name"
                                   class="form-label"><b>Last name</b></label>
                            <input type="text" class="form-control"
                                   id="last-name"
                                   name="lastName"
                            >
                        </div>
                        <div class="mb-3">
                            <label for="age"
                                   class="form-label"><b>Age</b></label>
                            <input type="number" class="form-control"
                                   id="age" name="age"
                            >
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label"><b>Email</b></label>
                            <input type="email" class="form-control"
                                   id="email" name="email"
                            >
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label"><b>Password</b></label>
                            <input type="password" class="form-control"
                                   id="password"
                                   name="password">
                        </div>

                        <label for="roles">Role</label>
                        <select id="roles" class="form-control"
                                name="roles"
                                multiple="multiple">
                        </select>
                        <br/>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <input type="submit" class="btn btn-primary" value="Edit">
                    </div>
                </form>

            </div>
    `
    return content;
}

async function deleteUser(email) {
    const response = await fetch("/users", {
        method: 'DELETE',
        body: `email=${email}`,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    if (!response.ok) {
        alert("something is wrong");
        return;
    }
    showAdminPanelUserDetail(document.getElementById("userinfo-container__body"))
}

function showLoader(element) {

}

function showError(container, details) {
    const errorElement = document.createElement("div");
    errorElement.innerHTML = "ERROR loading"
    container.appendChild(errorElement);
}

async function createNewUser(event) {
    event.preventDefault();
    const userInfo = {
        name: document.getElementById("first-name-new-user").value,
        lastName: document.getElementById("last-name-new-user").value,
        age: document.getElementById("age-new-user").value,
        email: document.getElementById("email-new-user").value,
        password: document.getElementById("password-new-user").value,
        roles: Array.from(
            document.getElementById("roles-new-user").options)
            .filter(item => item.selected).map(item => ({id: +item.value}))
    }
    const response = await fetch("/users", {
        method: 'POST',
        body: JSON.stringify(userInfo),
        headers: {
            "Content-Type": "application/json",
        },
    })
    if (!response.ok) {
        showCreateUserError();
        return;
    }

    clearNewUserFormAndDisplaySuccessMessage()
}

function clearNewUserFormAndDisplaySuccessMessage() {
    document.getElementById("first-name-new-user").value = "";
    document.getElementById("last-name-new-user").value = "";
    document.getElementById("age-new-user").value = "";
    document.getElementById("email-new-user").value = "";
    document.getElementById("password-new-user").value = "";
    document.getElementById("roles-new-user").value = "";
    alert("User successfully added");
    showAdminPanelUserDetail(document.getElementById("userinfo-container__body"))
}

function showCreateUserError() {
    alert("ERROR adding user");
}