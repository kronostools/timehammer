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

    const registrationRequestId = queryParams.internalId[0]

    const loadingElem = $('#loading').loading()
    const loading = loadingElem.loading('instance')

    $('#registrationRequestId').val(registrationRequestId)

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

    //loading.show()
    
    // BEGIN REGISTRATION SUMMARY STREAM
    const registrationRequestSummarySource = new EventSource(`/registrationSummary/stream/${registrationRequestId}`)

    registrationRequestSummarySource.addEventListener('open', (event) => {
        console.info(`Connected to registrationRequestSummary stream! (subscriber id: ${registrationRequestId})`)
    })

    registrationRequestSummarySource.addEventListener('error', (event) => {
        console.error('Error connecting to registrationRequestSummary stream, closing it ...')
        registrationRequestSummarySource.close()
        // TODO: hide loading, disable submit button, show form error suggesting reloading the page
        console.info('Closed registrationRequestStream stream')
    })

    registrationRequestSummarySource.addEventListener('message', (event) => {
        const registrationRequestSummaryEvent = JSON.parse(event.data)

        console.debug(`Received registrationRequestSummary event: ${event.data}`)

        loading.hide()

        if (isRegistrationRequestExpired(registrationRequestSummaryEvent)) {
            window.location = 'registrationExpired.html'
        } else if (isRegistrationRequestSuccessful(registrationRequestSummaryEvent)) {
            window.location = 'registrationSuccessful.html'
        } else {
            showFormErrors(registrationRequestSummaryEvent.validationErrors)
        }
    })

    const isRegistrationRequestExpired = function(registrationRequest) {
        return registrationRequest.validationErrors.some((n, i) => n.errorCode === 500)
    }

    const isRegistrationRequestSuccessful = function(registrationRequest) {
        return registrationRequest.validationErrors.length === 0
    }
    // END REGISTRATION SUMMARY STREAM

    // BEGIN CATALOGUE STREAM
    // TODO: call to get cities and fill select options in html
    // END CATALOGUE STREAM

    $('.needs-validation').on('submit', function(event) {
        event.preventDefault()
        event.stopPropagation()

        const $form = $(event.currentTarget)
        const formData = {
            registrationRequest: {
                defaultTimetable: {}
            }
        }

        $form.serializeArray().map((n, i) => {
            if (n['name'].startsWith(timetableId)) {
                const fieldName = n['name']
                    .substring(n['name'].indexOf(timetableId) + timetableId.length)
                    .replace(/^(.)/, ($1) => $1.toLowerCase())

                formData.registrationRequest.defaultTimetable[fieldName] = n['value']
            } else {
                if (n['name'] === 'registrationRequestId') {
                    formData[n['name']] = n['value']
                } else {
                    formData.registrationRequest[n['name']] = n['value']
                }
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
                console.debug('Registration request sent successfully!')
            } else {
                unexpectedError()
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            unexpectedError()
        })
    })

    function unexpectedError() {
        loading.hide()

        showFormErrors([{
            fieldId: '',
            errorMessage: 'Ha ocurrido un error inesperado durante el envío del formulario de registro, por favor, inténtalo de nuevo. Si el error persite, espere unos minutos antes de reintentarlo.'
        }])
    }
})