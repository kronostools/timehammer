$(document).ready(function() {
    // begin-copy [https://stackoverflow.com/a/21152762/1979915]
    var queryParams = {}
    if (location.search) {
        location.search.substr(1).split("&").forEach(function(item) {
            var s = item.split("="),
                k = s[0],
                v = s[1] && decodeURIComponent(s[1]); //  null-coalescing / short-circuit

            (queryParams[k] = queryParams[k] || []).push(v) // null-coalescing / short-circuit
        })
    }
    // end-copy

    if (!queryParams.internalId) {
        window.location = '../error_400.html'
    }

    const internalId = queryParams.internalId[0]

    const loadingElem = $('#loading').loading()
    const loading = loadingElem.loading('instance')

    $('#internalId').val(internalId)

    function showFormErrors(formErrors, disable = false) {
        // Clean previous errors
        $('#globalErrors').remove()
        $('.invalid-feedback').text('')
        $('.form-control').removeClass('is-invalid')

        // Classify errors
        const globalErrors = formErrors.filter(formError => formError.fieldName === '' || formError.fieldName === null)
        const fieldErrors = formErrors.filter(formError => formError.fieldName !== '' && formError.fieldName !== null)

        // Process global errors
        if (globalErrors.length > 0) {
            const $globalErrorsDiv = $('<div>', { 'id': 'globalErrors', 'class': 'alert alert-danger' })
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

            if (disabled) {
                $('#submitButton').attr('disabled', 'disabled')
            } else {
                $('#submitButton').removeAttr('disabled')
            }
        }

        // Process field errors
        if (fieldErrors.length > 0) {
            fieldErrors.forEach(e => {
                $('#' + e.fieldName).addClass('is-invalid')
                $('#' + e.fieldName + 'InvalidFeedback').text(e.errorMessage)
            })
        }

        $('html').scrollTop($('form').offset().top - 20)
    }

    loading.show()
    
    // BEGIN UPDATE PASSWORD SUMMARY STREAM
    const updatePasswordRequestSummarySource = new EventSource(`/updatePasswordSummary/stream/${internalId}`)

    updatePasswordRequestSummarySource.addEventListener('open', (event) => {
        console.info(`Connected to updatePasswordRequestSummary stream! (subscriber id: ${internalId})`)

        loading.hide()
    })

    updatePasswordRequestSummarySource.addEventListener('error', (event) => {
        console.error('Error connecting to updatePasswordRequestSummary stream, closing it ...')
        updatePasswordRequestSummarySource.close()

        unexpectedErrorPreparingUpdatePasswordForm()

        console.info('Closed updatePasswordRequestStream stream')
    })

    updatePasswordRequestSummarySource.addEventListener('message', (event) => {
        const updatePasswordRequestSummaryEvent = JSON.parse(event.data)

        console.debug(`Received updatePasswordRequestSummary event: ${event.data}`)

        loading.hide()

        if (isUpdatePasswordRequestExpired(updatePasswordRequestSummaryEvent)) {
            window.location = 'updatePasswordExpired.html'
        } else if (isUpdatePasswordRequestSuccessful(updatePasswordRequestSummaryEvent)) {
            window.location = 'updatePasswordSuccessful.html'
        } else {
            showFormErrors(updatePasswordRequestSummaryEvent.validationErrors)
        }
    })

    const isUpdatePasswordRequestExpired = function(updatePasswordRequest) {
        return updatePasswordRequest.validationErrors.some((n, i) => n.errorCode === 500)
    }

    const isUpdatePasswordRequestSuccessful = function(updatePasswordRequest) {
        return updatePasswordRequest.validationErrors.length === 0
    }
    // END UPDATE PASSWORD SUMMARY STREAM

    $('.needs-validation').on('submit', function(event) {
        event.preventDefault()
        event.stopPropagation()

        const $form = $(event.currentTarget)
        const formData = {
            updatePasswordRequest: {
            }
        }

        $form.serializeArray().map((n, i) => {
            if (n['name'] === 'internalId') {
                formData[n['name']] = n['value']
            } else {
                formData.updatePasswordRequest[n['name']] = n['value']
            }
        })

        console.debug(`Sending data: ${JSON.stringify(formData)}`)

        loading.show()

        $.ajax({
            url: $form.attr('action'),
            method: $form.attr('method'),
            contentType: 'application/json',
            data: JSON.stringify(formData),
            dataType: 'json'
        }).done(function(data, textStatus, jqXHR) {
            if (data.result) {
                console.debug('Update password request sent successfully!')
            } else {
                unexpectedErrorSendingUpdatePasswordForm()
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            unexpectedErrorSendingUpdatePasswordForm()
        })
    })

    function unexpectedErrorSendingUpdatePasswordForm() {
        loading.hide()

        showFormErrors([{
            fieldName: '',
            errorMessage: 'Ha ocurrido un error inesperado durante el envío del formulario, por favor, inténtalo de nuevo. Si el error persite, espere unos minutos antes de reintentarlo.'
        }])
    }

    function unexpectedErrorPreparingUpdatePasswordForm() {
        loading.hide()

        showFormErrors([{
            fieldName: '',
            errorMessage: 'Ha ocurrido un error inesperado durante la recuperación de datos para el formulario, por favor, refresque la página. Si el error persite, espere unos minutos antes de reintentarlo.'
        }], true)
    }
})