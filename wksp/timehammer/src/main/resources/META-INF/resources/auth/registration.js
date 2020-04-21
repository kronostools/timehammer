$(document).ready(function() {
    const timetableId = 'timetable'

    $('.timetable').timetable({
        id: timetableId,
        title: 'Horario habitual'
    })

    function showFormErrors(formErrors) {
        // Clean previous errors
        $('#globalErrors').remove()
        $('.invalid-feedback').text('')
        $('.form-control').removeClass('is-invalid')

        // Classify errors
        const globalErrors = formErrors.filter(formError => formError.fieldId === '')
        const fieldErrors = formErrors.filter(formError => formError.fieldId !== '')

        // Process global errors
        if (globalErrors.length > 0) {
            const $globalErrorsDiv = $('<div>', { 'id': 'globalErrors', 'class': 'alert alert-danger' });
            $globalErrorsDiv.append($('<h4>', { 'text': 'Error' }))
            let $globalErrors
    
            if (globalErrors.length == 1) {
                $globalErrors = $('<p>', { 'text': globalErrors[0].errorMessage, 'class': 'my-0' })
            } else {
                $globalErrors = $('<ul>', { 'class': 'my-0' })
                globalErrors.map(e => $('<li>', { 'text': e.errorMessage })).forEach(e => $globalErrorsList.append(e))
            }
    
            $globalErrorsDiv.append($globalErrors)

            $('form').prepend($globalErrorsDiv)
        }

        // Process field errors
        if (fieldErrors.length > 0) {
            fieldErrors.forEach(e => {
                $('#' + e.fieldId).addClass('is-invalid')
                $('#' + e.fieldId + 'InvalidFeedback').text(e.errorMessage)
            })
        }

        $('html').scrollTop($('form').offset().top - 20)
    }

    $('.needs-validation').on('submit', function(event) {
        event.preventDefault()
        event.stopPropagation()

        const $loginForm = $(event.currentTarget)
        const loginFormData = {}

        $loginForm.serializeArray().map((n, i) => {
            if (n['name'].startsWith(timetableId)) {
                if (!loginFormData.timetables) {
                    loginFormData['timetables'] = [{}]
                }

                const fieldName = n['name']
                    .substring(n['name'].indexOf(timetableId) + timetableId.length)
                    .replace(/^(.)/, ($1) => $1.toLowerCase())

                loginFormData.timetables[0][fieldName] = n['value']
            } else {
                loginFormData[n['name']] = n['value']
            }
        })

        console.log(`Sending data: ${JSON.stringify(loginFormData)}`)

        $.ajax({
            url: $loginForm.attr('action'),
            method: $loginForm.attr('method'),
            contentType: 'application/json',
            data: JSON.stringify(loginFormData),
            dataType: 'json'
        }).done(function(data, textStatus, jqXHR) {
            if (data.processedSuccessfully) {
                window.location = 'registrationSuccessful'
            } else {
                showFormErrors(data.formErrors)
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            showFormErrors([{
                fieldId: '',
                errorMessage: 'Ha ocurrido un error inesperado durante el envío del formulario de registro, por favor, inténtalo de nuevo. Si el error persite, espere unos minutos antes de reintentarlo.'
            }])
        })
    });
});