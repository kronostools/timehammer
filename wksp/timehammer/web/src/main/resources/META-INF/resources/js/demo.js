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

    const changeScheduleButtonClass = (scheduleButton, clazz) => {
        scheduleButton.classList.remove('btn-success')
        scheduleButton.classList.remove('btn-danger')
        scheduleButton.classList.remove('btn-primary')
        scheduleButton.classList.add(clazz)
    }

    schedulesSummaryEventSource.addEventListener('message', (event) => {
        const scheduleSummaryEvent = JSON.parse(event.data)
        const scheduleStartTimestamp = moment(scheduleSummaryEvent.startTimestamp, TIMESTAMP_JSON_FORMAT)
        const scheduleEndTimestamp = moment(scheduleSummaryEvent.endTimestamp, TIMESTAMP_JSON_FORMAT)
        const scheduleDuration = moment.duration(scheduleEndTimestamp.diff(scheduleStartTimestamp)).humanize()

        var scheduleText

        if (scheduleSummaryEvent.batched) {
            scheduleText = `${scheduleDuration} [O: ${scheduleSummaryEvent.itemsProcessedOk} - K: ${scheduleSummaryEvent.itemsProcessedKo} - T: ${scheduleSummaryEvent.totalItemsProcessed}]`
        } else {
            scheduleText = `${scheduleDuration}`
        }

        console.debug(`Received schedule summary event: ${event.data}`)

        const targetButton = document.getElementById(scheduleSummaryEvent.name)

        if (scheduleSummaryEvent.processedSuccessfully) {
            changeScheduleButtonClass(targetButton, 'btn-success')
        } else {
            changeScheduleButtonClass(targetButton, 'btn-danger')
        }

        Array.from(targetButton.parentNode.getElementsByClassName('schedule-duration'))
            .map(e => e.textContent = scheduleText)

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
    const scheduleButtons = document.getElementsByClassName('schedule-button')
    const scheduleButtonClick$ = fromEvent(scheduleButtons, 'click')

    const triggerSchedule = (e) => {
        const clickedButton = e.currentTarget
        const scheduleName = clickedButton.getAttribute('data-schedule')

        const targetButton = document.getElementById(scheduleName)
        targetButton.setAttribute('disabled', 'disabled')

        changeScheduleButtonClass(targetButton, 'btn-primary')

        Array.from(targetButton.getElementsByTagName('i'))
            .map(e => {
                e.classList.remove('fa-play')
                e.classList.add('fa-sync')
                e.classList.add('fa-spin')
            })

        console.debug(`Triggering schedule '${scheduleName}'`)

        return rxjs.ajax.ajax({
            url: `/demo/triggerSchedule/${scheduleName}`,
            method: 'POST'
        })
    }

    scheduleButtonClick$
        .pipe(flatMap(triggerSchedule))
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