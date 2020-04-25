$(document).ready(function() {
    $('[data-toggle="tooltip"]').tooltip()

    const TIMESTAMP_FORMAT = 'YYYYMMDDTHH:mm'

    // Timestamp form

    function resetFormErrors($form) {
        $form.find('#globalErrors').remove()
    }

    function showFormErrors($form, formErrors) {
        // Clean previous errors
        $form.find('#globalErrors').remove()

        // Classify all errors as global
        const globalErrors = formErrors

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

            $form.prepend($globalErrorsDiv)
        }

        $('html').scrollTop($form.offset().top - 20)
    }

    function lpad(value, length) {
        return ("0000" + value).slice(-length)
    }

    function getNewTimestamp($form) {
        const year = lpad($form.find('#year').val(), 4)
        const month = lpad($form.find('#month').val(), 2)
        const day = lpad($form.find('#day').val(), 2)
        const hours = lpad($form.find('#hours').val(), 2)
        const minutes = lpad($form.find('#minutes').val(), 2)

        return year + month + day + 'T' + hours + ':' + minutes
    }

    function getMomentFromForm($form) {
        return moment([$form.find('#year').val(), $form.find('#month').val() - 1, $form.find('#day').val(), $form.find('#hours').val(), $form.find('#minutes').val()])
    }

    function getNewTimestampDataFromMoment(moment) {
        return {
            day: moment.date(),
            month: moment.month() + 1,
            year: moment.year(),
            hours: moment.hours(),
            minutes: moment.minutes()
        }
    }

    function executeAction($form, action, unit) {
        var newTimestamp = getMomentFromForm($form)

        if (!newTimestamp.isValid()) {
            const timestampStr = $form.find('#timestamp').val()
            newTimestamp = moment(timestampStr, TIMESTAMP_FORMAT)
        }

        if (action === 'plus') {
            newTimestamp.add(1, unit)
        } else if (action === 'minus') {
            newTimestamp.subtract(1, unit)
        }

        updateFormData($form, getNewTimestampDataFromMoment(newTimestamp))
    }

    function initializeForm($form) {
        const newTimestamp = getMomentFromForm($form)
        $form.find('#timestamp').val(newTimestamp.format(TIMESTAMP_FORMAT))

        resetFormErrors($form)
        formUpdated($form)
    }

    function updateForm($form, newTimestampData) {
        const newTimestamp = moment([newTimestampData.year, newTimestampData.month - 1, newTimestampData.day, newTimestampData.hours, newTimestampData.minutes])
        $form.find('#timestamp').val(newTimestamp.format(TIMESTAMP_FORMAT))

        updateFormData($form, newTimestampData)
    }

    function resetForm($form) {
        const timestampStr = $form.find('#timestamp').val()
        const timestamp = moment(timestampStr, TIMESTAMP_FORMAT)

        updateFormData($form, getNewTimestampDataFromMoment(timestamp))
    }

    function updateFormData($form, newTimestampData) {
        $form.find('#day').val(newTimestampData.day)
        $form.find('#month').val(newTimestampData.month)
        $form.find('#year').val(newTimestampData.year)
        $form.find('#hours').val(newTimestampData.hours)
        $form.find('#minutes').val(newTimestampData.minutes)

        resetFormErrors($form)
        formUpdated($form)
    }

    function formUpdated($form) {
        if (getNewTimestamp($form) === $form.find('#timestamp').val()) {
            $form.find('#submit').addClass('disabled')
            $form.find('#reset').addClass('disabled')
        } else {
            $form.find('#submit').removeClass('disabled')
            $form.find('#reset').removeClass('disabled')
        }
    }

    $('.timestamp-element').on('change', function(event) {
        event.preventDefault()
        event.stopPropagation()

        const $form = $(event.currentTarget).closest('form')

        formUpdated($form)
    })

    $('#reset').on('click', function(event) {
        event.preventDefault()
        event.stopPropagation()

        const $form = $(event.currentTarget).closest('form')

        resetForm($form)
    })

    $('#timezone').on('change', function(event) {
        event.preventDefault()
        event.stopPropagation()

        const $timezone = $(event.currentTarget)
        const $form = $timezone.closest('form')

        const queryParams = {
            timezone: $timezone.val()
        }

        $.ajax({
            url: $form.attr('action') + '?' + $.param(queryParams),
            method: 'GET',
        }).done(function(data, textStatus, jqXHR) {
            updateForm($form, data)
        }).fail(function(jqXHR, textStatus, errorThrown) {
            showFormErrors($form, [{
                fieldId: '',
                errorMessage: 'Ha ocurrido un error inesperado durante el envío del formulario, por favor, inténtalo de nuevo. Si el error persite, espere unos minutos antes de reintentarlo.'
            }])
        })
    })

    $('.timestamp-action').on('click', function(event) {
        event.preventDefault()
        event.stopPropagation()

        const $button = $(event.currentTarget)
        const $form = $button.closest('form')

        executeAction($form, $button.data('action'), $button.data('unit'))
    })

    $('#timestampForm').on('submit', function(event) {
        event.preventDefault()
        event.stopPropagation()

        const $form = $(event.currentTarget)
        const newTimestampFormData = {}

        $form.serializeArray().map((n, i) => {
            newTimestampFormData[n['name']] = n['value']
        })

        console.log(`Sending data: ${JSON.stringify(newTimestampFormData)}`)

        $.ajax({
            url: $form.attr('action'),
            method: $form.attr('method'),
            contentType: 'application/json',
            data: JSON.stringify(newTimestampFormData),
            dataType: 'json'
        }).done(function(data, textStatus, jqXHR) {
            if (data.processedSuccessfully) {
                initializeForm($form)
            } else {
                showFormErrors($form, data.formErrors)
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            showFormErrors($form, [{
                fieldId: '',
                errorMessage: 'Ha ocurrido un error inesperado durante el envío del formulario, por favor, inténtalo de nuevo. Si el error persite, espere unos minutos antes de reintentarlo.'
            }])
        })
    });

    initializeForm($('#timestampForm'))

    // Worker form
    $('#workersForm').on('submit', function(event) {
        event.preventDefault()
        event.stopPropagation()

        const $form = $(event.currentTarget)
        const $workerExternalId = $form.find('#workerExternalId')
        const $refreshWorkerInfoBtn = $form.find('#refreshWorkerInfo')

        const workerExternalId = $workerExternalId.val()

        if (workerExternalId && workerExternalId.length > 0) {
            $refreshWorkerInfoBtn.addClass('fa-spin').addClass('disabled')

            $.ajax({
                url: $form.attr('action') + '/' + workerExternalId,
                method: $form.attr('method'),
            }).done(function(data, textStatus, jqXHR) {
                updateWorkerStatusForm($('#workerStatusForm'), data)
            }).fail(function(jqXHR, textStatus, errorThrown) {
                showFormErrors($form, [{
                    fieldId: '',
                    errorMessage: 'Ha ocurrido un error inesperado durante el envío del formulario, por favor, inténtalo de nuevo. Si el error persite, espere unos minutos antes de reintentarlo.'
                }])
            }).always(function() {
                $refreshWorkerInfoBtn.removeClass('fa-spin').removeClass('disabled')
            })
        } else {
            $workerExternalId.effect('shake', { distance: 10 })
        }
    })

    function updateWorkerStatusForm($form, data) {
        resetFormErrors($form)

        $form.find('#statusTimestamp').val(data.timestamp)
        $form.find('#statusDayOfWeek').val(data.dayOfWeek)
        $form.find('#work').val(data.work)
        $form.find('#lunch').val(data.lunch)
        $form.find('#status').val(data.status)
        $form.find('#holidays').val(data.holidays)
    }

    // Schedules form
    $('.schedule-button').on('click', function(event) {
        event.preventDefault()
        event.stopPropagation()

        const $scheduleButton = $(event.currentTarget)
        const $scheduleButtonIcon = $scheduleButton.find('i')
        const $form = $scheduleButton.closest('form')

        const scheduleId = $scheduleButton.attr('id')

        $scheduleButton
            .removeClass('btn-success')
            .addClass('btn-warning')
        $scheduleButtonIcon
            .removeClass('fa-play')
            .addClass('fa-sync').addClass('fa-spin')

        const start = Date.now()

        $.ajax({
            url: $form.attr('action') + '/' + scheduleId,
            method: $form.attr('method')
        }).done(function(data, textStatus, jqXHR) {
            $scheduleButton
                .removeClass('btn-warning')
                .addClass('btn-success')
            $scheduleButtonIcon
                .removeClass('fa-sync').removeClass('fa-spin')
                .addClass('fa-play')
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $scheduleButton
                .removeClass('btn-warning')
                .addClass('btn-danger')
            $scheduleButtonIcon
                .removeClass('fa-sync').removeClass('fa-spin')
                .addClass('fa-play')
        }).always(function() {
            const end = Date.now()
            const $scheduleDuration = $scheduleButton.siblings('.schedule-duration')

            $scheduleDuration.text(moment.duration(end - start).locale('es').humanize())
        })
    })
})