document.addEventListener("DOMContentLoaded", function(event) {
    const TIMESTAMP_JSON_FORMAT = 'YYYYMMDDTHH:mm:ss.SSS'
    const TIMEMACHINE_TIMESTAMP_FORMAT = 'YYYYMMDDTHH:mm'
    const TIMESTAMP_HTML_FORMAT = 'DD/MM/YYYY HH:mm'

    const subscriberId = uuid.v4()

    // BEGIN TIME MACHINE STREAM
    const timeMachineEventSource = new EventSource(`/timemachine/stream/${subscriberId}`);

    timeMachineEventSource.addEventListener('open', (event) => {
        console.info(`Connected to timemachine stream! (subscriber id: ${subscriberId})`)
    })

    timeMachineEventSource.addEventListener('error', (event) => {
        console.error('Error connecting to timemachine stream, closing it ...')
        timeMachineEventSource.close()
        console.info('Closed timemachine stream')
    })

    timeMachineEventSource.addEventListener('message', (event) => {
        const timemachineEvent = JSON.parse(event.data)

        console.debug(`Received timemachine event: ${event.data}`)

        updateTimestamp(timemachineEvent.timestamp)
    })
    // END TIME MACHINE STREAM

    // BEGIN SCHEDULES SUMMARY STREAM
    const schedulesSummaryEventSource = new EventSource(`/schedulesSummary/stream/${subscriberId}`);

    schedulesSummaryEventSource.addEventListener('open', (event) => {
        console.info(`Connected to schedules summary stream! (subscriber id: ${subscriberId})`)
    })

    schedulesSummaryEventSource.addEventListener('error', (event) => {
        console.error('Error connecting to schedules summary stream, closing it ...')
        schedulesSummaryEventSource.close()
        console.info('Closed schedules summary stream')
    })

    schedulesSummaryEventSource.addEventListener('message', (event) => {
        const scheduleSummaryEvent = JSON.parse(event.data)
        const scheduleStartTimestamp = moment(scheduleSummaryEvent.startTimestamp, TIMESTAMP_JSON_FORMAT)
        const scheduleEndTimestamp = moment(scheduleSummaryEvent.endTimestamp, TIMESTAMP_JSON_FORMAT)
        const scheduleDuration = moment.duration(scheduleEndTimestamp.diff(scheduleStartTimestamp)).humanize()

        console.debug(`Received schedule summary event: ${event.data}`)

        const targetButton = document.getElementById(scheduleSummaryEvent.name)

        Array.from(targetButton.parentNode.getElementsByClassName('schedule-duration'))
            .map(e => e.textContent = scheduleDuration)

        Array.from(targetButton.getElementsByTagName('i'))
            .map(e => {
                e.classList.remove('fa-sync')
                e.classList.remove('fa-spin')
                e.classList.add('fa-play')
            })

        targetButton.removeAttribute('disabled', 'disabled')
    })
    // END SCHEDULES SUMMARY STREAM

    // BEGIN COMMON EVENT SOURCES
    window.addEventListener('beforeunload', (event) => {
        event.preventDefault()
        delete event['returnValue']

        console.debug('Page unload, closing timemachine stream ...')
        timeMachineEventSource.close()
        console.info('Page unload, closed timemachine stream')

        console.debug('Page unload, closing schedules summary stream ...')
        schedulesSummaryEventSource.close()
        console.info('Page unload, closed schedules summary stream')
    })
    // END COMMON EVENT SOURCES

    const flatMap = rxjs.operators.flatMap
    const map = rxjs.operators.map
    const bufferTime = rxjs.operators.bufferTime
    const fromEvent = rxjs.fromEvent

    // BEGIN TIMEMACHINE
    const timestamp = document.getElementById('timestamp')
    const visibleTimestamp = document.getElementById('visibleTimestamp')
    const dayField = document.getElementById('day')
    const monthField = document.getElementById('month')
    const yearField = document.getElementById('year')
    const hoursField = document.getElementById('hours')
    const minutesField = document.getElementById('minutes')

    const updateTimestamp = (rawTimestamp) => {
        const currentTimestamp = moment(rawTimestamp, TIMESTAMP_JSON_FORMAT)
        timestamp.value = currentTimestamp.format(TIMEMACHINE_TIMESTAMP_FORMAT)
        visibleTimestamp.textContent = currentTimestamp.format(TIMESTAMP_HTML_FORMAT)

        updateTimestampForm(currentTimestamp)
    }

    const updateTimestampForm = (currentTimestamp) => {
        dayField.value = currentTimestamp.date()
        monthField.value = currentTimestamp.month() + 1
        yearField.value = currentTimestamp.year()
        hoursField.value = currentTimestamp.hours()
        minutesField.value = currentTimestamp.minutes()

        if (currentTimestamp.format(TIMEMACHINE_TIMESTAMP_FORMAT) === timestamp.value) {
            timestampSubmitButton.setAttribute('disabled', 'disabled')
            timestampResetButton.setAttribute('disabled', 'disabled')
        } else {
            timestampSubmitButton.removeAttribute('disabled', 'disabled')
            timestampResetButton.removeAttribute('disabled', 'disabled')
        }
    }

    // Initialize options of timezone select
    rxjs.ajax.ajax('/demo/currentTimestamp')
        .subscribe(
            // ok
            (r) => updateTimestamp(r.response.timestamp),
            //ko
            (e) => console.error(`Unexpected error while getting current timestamp. Error: ${e.message}`)
        )

    const timestampSubmitButton = document.getElementById('timestampSubmit')
    const timestampResetButton = document.getElementById('timestampReset')
    const timestampSubmitButtonClick$ = fromEvent(timestampSubmitButton, 'click')

    const lpad = (value, length) => {
        return ("0000" + value).slice(-length)
    }

    const getSelectedTimestamp = () => {
        return `${lpad(yearField.value, 4)}${lpad(monthField.value, 2)}${lpad(dayField.value, 2)}T${lpad(hoursField.value, 2)}:${lpad(minutesField.value, 2)}:${lpad(0, 2)}.${lpad(0, 3)}`
    }

    const sendTimestampEvent = () => {
        return rxjs.ajax.ajax({
            url: '/demo/timeTravel',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: {
                timestamp: getSelectedTimestamp()
            }
        })
    }

    timestampSubmitButtonClick$
        .pipe(flatMap(sendTimestampEvent))
        .subscribe(
            // success
            () => {
                console.log('Timestamp event sent successfully!')
            },
            // error
            (e) => console.error(`Unexpeced error while sending timestamp event. Error: ${e.message}`)
        )

    const timestampActionButtons = document.getElementsByClassName('timestamp-action')
    const timestampActionButtonClick$ = fromEvent(timestampActionButtons, 'click')

    timestampActionButtonClick$
        .pipe(
            map(e => {
                const action = {
                    quantity: e.currentTarget.getAttribute('data-action') === 'plus' ? +1 : -1,
                    unit: e.currentTarget.getAttribute('data-unit')
                }

                console.log(`clicked action button: ${JSON.stringify(action)}`)

                return action
            }),
            map(action => {
                return moment(getSelectedTimestamp(), TIMESTAMP_JSON_FORMAT).add(action.quantity, action.unit)
            }))
        .subscribe(
            // ok
            (newTimestamp) => updateTimestampForm(newTimestamp),
            // error
            e => console.error(`Unexpected error while processing action button. Error: ${e.message}`)
        )

    const timestampElements = document.getElementsByClassName('timestamp-element')
    const timestampElementChange$ = fromEvent(timestampElements, 'change')

    timestampElementChange$
        .pipe(
            bufferTime(400),
            map(action => {
                return moment(getSelectedTimestamp(), TIMESTAMP_JSON_FORMAT).add(action.quantity, action.unit)
            }))
        .subscribe(
            // ok
            (newTimestamp) => updateTimestampForm(newTimestamp),
            // error
            e => console.error(`Unexpected error while processing action button. Error: ${e.message}`)
        )

    const timestampResetButtonClick$ = fromEvent(timestampResetButton, 'click')

    timestampResetButtonClick$
        .pipe(
            map(() => {
                return moment(timestamp.value, TIMESTAMP_JSON_FORMAT)
            }))
        .subscribe(
            // ok
            (newTimestamp) => updateTimestampForm(newTimestamp),
            // error
            e => console.error(`Unexpected error while processing click of reset button. Error: ${e.message}`)
        )
    // END TIMEMACHINE

    // BEGIN SCHEDULES
    const updateWorkersHolidaysButton = document.getElementById('updateWorkersHolidaysTest')
    const updateWorkersHolidaysButtonClick$ = fromEvent(updateWorkersHolidaysButton, 'click')

    const sendUpdateWorkersHolidays = (e) => {
        const clickedButton = e.currentTarget
        const scheduleName = clickedButton.getAttribute('data-schedule')

        const targetButton = document.getElementById(scheduleName)
        targetButton.setAttribute('disabled', 'disabled')
        Array.from(targetButton.getElementsByTagName('i'))
            .map(e => {
                e.classList.remove('fa-play')
                e.classList.add('fa-sync')
                e.classList.add('fa-spin')
            })

        console.debug(`Triggering schedule '${scheduleName}'`)

        // TODO: infer url based on scheduleName
        return rxjs.ajax.ajax('/test/sendToComunytekWorkerHoliday')
    }

    updateWorkersHolidaysButtonClick$
        .pipe(flatMap(sendUpdateWorkersHolidays))
        .subscribe(
            // success
            (r) => {
                if (r.response.result) {
                    console.info(`Schedule '${r.response.name}' was triggered successfully!`)
                } else {
                    console.error(`Error while triggering schedule '${r.response.name}'`)
                }
            },
            // error
            (e) => console.error(`Unexpected error while triggering schedule. Error: ${e.message}`)
        )
    // END SCHEDULES
})

/*
    // ---------------- OLD CODE ----------------

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
        const $workerInternalId = $form.find('#workerInternalId')
        const $refreshWorkerInfoBtn = $form.find('#refreshWorkerInfo')

        const workerInternalId = $workerInternalId.val()

        if (workerInternalId && workerInternalId.length > 0) {
            $refreshWorkerInfoBtn.addClass('fa-spin').addClass('disabled')

            $.ajax({
                url: $form.attr('action') + '/' + workerInternalId,
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
            $workerInternalId.effect('shake', { distance: 10 })
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
*/