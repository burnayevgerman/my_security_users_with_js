function fillEditModal(userId) {
    $.ajax ({
        type: 'GET',
        url: '/admin/user-info/' + userId,
        success: function (user) {
            $('#hidden-edit-user-id').val(user.id);
            $('#edit-user-id').val(user.id);
            $('#edit-name').val(user.name);
            $('#edit-email').val(user.email);
            $('#edit-phone-number').val(user.phoneNumber);
            $('#edit-gender').val(user.gender);
            $('#edit-date-of-birth').val(user.dateOfBirth);
        },
        error: function (xhr, status, error) {
            // catch errors
        }
    });

    $.ajax({
        type: 'GET',
        url: '/admin/user-role/' + userId,
        success: function (role) {
            $('#edit-role').val(role);
        },
        error: function (xhr, status, error) {
            // catch errors
        }
    });
}

function fillDeleteModal(userId) {
    $.ajax ({
        type: 'GET',
        url: '/admin/user-info/' + userId,
        success: function (user) {
            $('#hidden-delete-user-id').val(user.id);
            $('#delete-user-id').val(user.id);
            $('#delete-user-name').val(user.name);
            $('#delete-user-email').val(user.email);
            $('#delete-user-phone-number').val(user.phoneNumber);
            $('#delete-user-gender').val(user.gender);
            $('#delete-user-date-of-birth').val(user.dateOfBirth);
        },
        error: function (xhr, status, error) {
            // catch errors
        }
    });

    $.ajax({
        type: 'GET',
        url: '/admin/user-role/' + userId,
        success: function (role) {
            $('#delete-user-role').val(role);
        },
        error: function (xhr, status, error) {
            // catch errors
        }
    });
}

$(document).ready(function() {
    let params = new URLSearchParams(window.location.search);

    if (params.has('update_error')) {
        $('#edit-user-modal').modal('show');
        fillEditModal(params.get('id'));
    } else if (params.has('delete_error')) {
        $('#delete-user-modal').modal('show');
        fillDeleteModal(params.get('id'));
    }

    $('.btn-edit').click(function() {
        fillEditModal( $(this).data('bs-user-id') );
    });

    $('.btn-delete').click(function() {
        fillDeleteModal( $(this).data('bs-user-id') );
    });

    if (window.location.pathname === '/admin') {
        window.history.replaceState({}, document.title, '/admin');
    }
});