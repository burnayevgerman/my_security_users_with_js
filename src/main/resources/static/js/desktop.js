var user, usersList;

$(document).ready(async function () {
    const html = $('html');
    let response = await fetch('/user/user-info');
    user = await response.json();

    if (isAdmin(user)) {
        response = await fetch('/admin/users-list');
        usersList = await response.json();
    }

    fillHeaderAndTitle();
    fillAboutUser(user);

    if (isAdmin(user)) {
        activateMenuItem('#ref-to-admin-panel');
        fillUsersTable(usersList);

        html.on('click', '#ref-to-admin-panel', function () {
            activateMenuItem('#ref-to-admin-panel');
        });
        html.on('click', '#ref-to-about-user', function () {
            activateMenuItem('#ref-to-about-user');
        });

        html.on('click', '#nav-btn-create', resetCreateForm);

        html.on('click', '.btn-edit', async function () {
            await openEditModal( $(this).data('bs-user-id'));
        });
        html.on('click', '.btn-delete', async function () {
            await openDeleteModal( $(this).data('bs-user-id'));
        });

        html.on('click', '#btn-edit-user', sendEditForm);
        html.on('click', '#btn-delete-user', sendDeleteForm);
        html.on('click', '#btn-create-user', sendCreateForm);
    } else {
        // if ROLE_USER

        activateMenuItem('#ref-to-about-user');
        $('li #ref-to-admin-panel').remove();
        $('#admin-panel-tab-pane').remove();
        $('#edit-user-modal').remove();
        $('#delete-user-modal').remove();
    }

});

function activateMenuItem(refToItem) {
    if (refToItem === '#ref-to-admin-panel') {
        $('#admin-panel-tab-pane').show();
        $('#about-user-tab-pane').hide();

        let ref_to_admin_panel = $('#ref-to-admin-panel');
        let ref_to_about_user = $('#ref-to-about-user');

        if (!ref_to_admin_panel.hasClass('active')) {
            ref_to_admin_panel.addClass('active');
        }

        if (ref_to_about_user.hasClass('active')) {
            ref_to_about_user.removeClass('active');
        }
    } else if (refToItem === '#ref-to-about-user') {
        $('#admin-panel-tab-pane').hide();
        $('#about-user-tab-pane').show();

        let ref_to_admin_panel = $('#ref-to-admin-panel');
        let ref_to_about_user = $('#ref-to-about-user');

        if (!ref_to_about_user.hasClass('active')) {
            ref_to_about_user.addClass('active');
        }

        if (ref_to_admin_panel.hasClass('active')) {
            ref_to_admin_panel.removeClass('active');
        }
    }
}

async function openEditModal(userId) {
    let cUser = await getUserById(userId);

    if (cUser != null) {
        fillModalEdit(cUser);
    } else {
        $('#edit-user-modal .close').click();
        alert('Failed to get user information!');
    }
}

async function openDeleteModal(userId) {
    let cUser = await getUserById(userId);

    if (cUser != null) {
        fillModalDelete(cUser);
    } else {
        $('#delete-user-modal .close').click();
        alert('Failed to get user information!');
    }
}

function getGenderText(gender) {
    if (gender === 'MALE') {
        return 'Male';
    } else if (gender === 'FEMALE') {
        return 'Female';
    } else {
        return 'Not Defined';
    }
}

function getRolesText(roles) {
    viewRoles = '';

    for (role of roles) {
        viewRoles += role.viewText;

        if (role.id !== roles.length) {
            viewRoles += ', ';
        }
    }

    return viewRoles;
}

function getDateText(date) {
    if (date === null) {
        return null;
    }

    return new Date(date).toLocaleDateString();
}

function isAdmin(u) {
    return u.roles.some((role) => role.name === 'ROLE_ADMIN');
}

function getTargetRole(u) {
    return isAdmin(u) ? 'ROLE_ADMIN' : 'ROLE_USER';
}

async function getUserById(id) {
    let response = await fetch('/admin/user-info/' + id);

    if (response.ok) {
        return await response.json();
    } else {
        return null;
    }
}

function fillHeaderAndTitle() {
    if (isAdmin(user)) {
        $('title').text('Admin Panel');
    } else {
        $('title').text('User Page');
    }

    $('.fill-header-email').text(user.email);
    $('.fill-header-roles').text(getRolesText(user.roles));
}

function fillAboutUser(u) {
    $('.fill-user-id')
        .text(u.id)
        .addClass('td-id')
        .attr('user-id', u.id);
    $('.fill-user-name')
        .text(u.name)
        .addClass('td-name')
        .attr('user-id', u.id);
    $('.fill-user-email')
        .text(u.email)
        .addClass('td-email')
        .attr('user-id', u.id);
    $('.fill-user-phone')
        .text(u.phoneNumber)
        .addClass('td-phone')
        .attr('user-id', u.id);
    $('.fill-user-gender')
        .text(getGenderText(u.gender))
        .addClass('td-gender')
        .attr('user-id', u.id);
    $('.fill-user-bdate')
        .text(getDateText(u.dateOfBirth))
        .addClass('td-bdate')
        .attr('user-id', u.id);
    $('.fill-user-roles')
        .text(getRolesText(u.roles))
        .addClass('td-roles')
        .attr('user-id', u.id);
}

function fillUsersTable(uList) {
    let table = $('#users-list');

    $.each(uList, function (index, cUser) {
        let row = $('<tr>').attr('user-id', cUser.id);
        row.append($('<td>')
            .text(cUser.id)
            .addClass('td-id')
            .attr('user-id', cUser.id));
        row.append($('<td>')
            .text(cUser.name)
            .addClass('td-name')
            .attr('user-id', cUser.id));
        row.append($('<td>')
            .text(cUser.email)
            .addClass('td-email')
            .attr('user-id', cUser.id));
        row.append($('<td>')
            .text(cUser.phoneNumber)
            .addClass('td-phone')
            .attr('user-id', cUser.id));
        row.append($('<td>')
            .text(getGenderText(cUser.gender))
            .addClass('td-gender')
            .attr('user-id', cUser.id));
        row.append($('<td>')
            .text(getDateText(cUser.dateOfBirth))
            .addClass('td-bdate')
            .attr('user-id', cUser.id));
        row.append($('<td>')
            .text(getRolesText(cUser.roles))
            .addClass('td-roles')
            .attr('user-id', cUser.id));
        row.append($('<td>').html('<button class="btn-edit btn btn-info p-2"\n' +
            '                                                    data-bs-toggle="modal"\n' +
            '                                                    data-bs-target="#edit-user-modal"\n' +
            '                                                    data-bs-user-id="' + cUser.id + '">Edit</button>'));
        row.append($('<td>').html('<button class="btn-delete btn btn-danger p-2"\n' +
            '                                                    data-bs-toggle="modal"\n' +
            '                                                    data-bs-target="#delete-user-modal"\n' +
            '                                                    data-bs-user-id="' + cUser.id + '">Delete</button>'));
        table.append(row);
    });
}

function resetCreateForm() {
    $('#create-user-name').val('');
    $('#create-user-email').val('');
    $('#create-user-phone').val('');
    $('#create-user-gender option[value="ROLE_USER"]').prop('selected', true);
    $('#create-user-bdate').val('');
    $('#create-user-password').val('');
    $('#create-user-role option[value="NOT_DEFINED"]').prop('selected', true);
}

function fillModalEdit(cUser) {
    $('#edit-user-id').val(cUser['id']);
    $('#edit-user-name').val(cUser.name);
    $('#edit-user-email').val(cUser.email);
    $('#edit-user-phone').val(cUser.phoneNumber);
    $('#edit-user-gender').val(cUser.gender);
    $('#edit-user-bdate').val(cUser.dateOfBirth);
    $('#edit-user-password').val('');
    $('#edit-user-role option[value="' + getTargetRole(cUser) + '"]').prop('selected', true);

    $('#btn-edit-user').data('bs-user-id', cUser['id']);
}

function fillModalDelete(cUser) {
    $('#delete-user-id').val(cUser['id']);
    $('#delete-user-name').val(cUser.name);
    $('#delete-user-email').val(cUser.email);
    $('#delete-user-phone').val(cUser.phoneNumber);
    $('#delete-user-gender').val(cUser.gender);
    $('#delete-user-bdate').val(cUser.dateOfBirth);
    $('#delete-user-role option[value="' + getTargetRole(cUser) + '"]').prop('selected', true);

    $('#btn-delete-user').data('bs-user-id', cUser['id']);
}

function updateUser(newUser) {
    if (newUser.id === user.id) {
        user = newUser;
        fillHeaderAndTitle();
    }

    $('[user-id="' + newUser.id + '"]').each(function () {
        if ( $(this).hasClass('td-id') ) {
            $(this).text(newUser.id);
        } else if ( $(this).hasClass('td-name') ) {
            $(this).text(newUser.name);
        } else if ( $(this).hasClass('td-email') ) {
            $(this).text(newUser.email);
        } else if ( $(this).hasClass('td-phone') ) {
            $(this).text(newUser.phoneNumber);
        } else if ( $(this).hasClass('td-gender') ) {
            $(this).text(getGenderText(newUser.gender));
        } else if ( $(this).hasClass('td-bdate') ) {
            $(this).text(getDateText(newUser.dateOfBirth));
        } else if ( $(this).hasClass('td-roles') ) {
            $(this).text(getRolesText(newUser.roles));
        }
    });
}

function addUser(newUser) {
    let table = $('#users-list');
    let row = $('<tr>').attr('user-id', newUser.id);
    row.append( $('<td>')
        .text(newUser.id)
        .addClass('td-id')
        .attr('user-id', newUser.id));
    row.append( $('<td>')
        .text(newUser.name)
        .addClass('td-name')
        .attr('user-id', newUser.id));
    row.append( $('<td>')
        .text(newUser.email)
        .addClass('td-email')
        .attr('user-id', newUser.id));
    row.append( $('<td>')
        .text(newUser.phoneNumber)
        .addClass('td-phone')
        .attr('user-id', newUser.id));
    row.append( $('<td>')
        .text(getGenderText(newUser.gender))
        .addClass('td-gender')
        .attr('user-id', newUser.id));
    row.append( $('<td>')
        .text(getDateText(newUser.dateOfBirth))
        .addClass('td-bdate')
        .attr('user-id', newUser.id));
    row.append( $('<td>')
        .text(getRolesText(newUser.roles))
        .addClass('td-roles')
        .attr('user-id', newUser.id));
    row.append( $('<td>').html(function () {
        let b = $('<button>');

        b.addClass('btn-edit');
        b.addClass('btn');
        b.addClass('btn-info');
        b.addClass('p-2');

        b.attr('data-bs-toggle', 'modal');
        b.attr('data-bs-target', '#edit-user-modal');
        b.attr('data-bs-user-id', newUser.id);

        b.text('Edit');
        return b;
    }));
    row.append( $('<td>').html(function () {
        let b = $('<button>');

        b.addClass('btn-delete');
        b.addClass('btn');
        b.addClass('btn-danger');
        b.addClass('p-2');

        b.attr('data-bs-toggle', 'modal');
        b.attr('data-bs-target', '#delete-user-modal');
        b.attr('data-bs-user-id', newUser.id);

        b.text('Delete');
        return b;
    }));
    table.append(row);
}

function showElementBriefly (element) {
    element.show(500, function () {
        setTimeout(function () {
            element.hide(1000);
        }, 5000);
    });
}

async function sendEditForm() {
    let u = {};
    u.id = $('#btn-edit-user').data('bs-user-id');
    u.name = $('#edit-user-name').val();
    u.email = $('#edit-user-email').val();
    u.phoneNumber = $('#edit-user-phone').val();
    u.gender = $('#edit-user-gender').val();
    u.dateOfBirth = $('#edit-user-bdate').val();
    u.password = $('#edit-user-password').val();

    let userRequest = {
        user: u,
        targetRole: $('#edit-user-role').val()
    };

    let response = await fetch('/admin/edit', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(userRequest)
    });

    if (response.ok) {
        try {
            u = await response.json()
        } catch (err) {
            window.location.href = '/login';
            return;
        }

        updateUser(u);
        $('#edit-user-modal .close').click();
        let alert = $('#alert-update-success');
        alert.text('Success updated a user with id: ' + u.id);
        showElementBriefly(alert);
    } else {
        await openEditModal(u.id);
        showElementBriefly( $('#alert-update-error'));
    }
}

async function sendDeleteForm() {
    let id = $('#btn-delete-user').data('bs-user-id');

    let response = await fetch('/admin/delete', {
        method: 'DELETE',
        body: new URLSearchParams([['id', id]])
    });

    if (response.ok) {
        if (id === user.id) {
            window.location.href = '/login';
            return;
        }

        $('[user-id="' + id + '"]').each(function () {
            $(this).remove();
        });

        $('#delete-user-modal .close').click();
        let alert = $('#alert-delete-success');
        alert.text('Success deleted a user with id: ' + id);
        showElementBriefly(alert);
    } else {
        await openDeleteModal(id);
        showElementBriefly( $('#alert-delete-error'));
    }
}

async function sendCreateForm() {
    let newUser = {
        name: $('#create-user-name').val(),
        email: $('#create-user-email').val(),
        phoneNumber: $('#create-user-phone').val(),
        gender: $('#create-user-gender').val(),
        dateOfBirth: $('#create-user-bdate').val(),
        password: $('#create-user-password').val()
    };
    let userRequest = {
        user: newUser,
        targetRole: $('#create-user-role').val()
    };

    let response = await fetch('/admin/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(userRequest)
    });

    if (response.ok) {
        newUser = await response.json();
        addUser(newUser);

        $('#nav-btn-table').click();

        let alert = $('#alert-create-success');
        alert.text('Success created a user with id: ' + newUser.id);
        showElementBriefly(alert);
    } else {
        showElementBriefly( $('#alert-create-failed'));
    }
}