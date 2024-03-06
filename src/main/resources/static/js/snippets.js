$(document).ready(function() {
    $('.btn-edit').click(function() {
        var userId = $(this).data('bs-user-id');

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
    });

    $('.btn-delete').click(function() {
        var userId = $(this).data('bs-user-id');

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
    });
});