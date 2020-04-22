$.widget('timehammer.timetable', {
    // default options
    options: {
        id: 'timetable',
        title: '',
        days: [{
            idSuffix: 'Mon',
            workText: 'LUNES',
            lunchText: 'comida',
            lunchOptional: false
        }, {
            idSuffix: 'Tue',
            workText: 'MARTES',
            lunchText: 'comida',
            lunchOptional: false
        }, {
            idSuffix: 'Wed',
            workText: 'MIÉRCOLES',
            lunchText: 'comida',
            lunchOptional: false
        }, {
            idSuffix: 'Thu',
            workText: 'JUEVES',
            lunchText: 'comida',
            lunchOptional: false
        }, {
            idSuffix: 'Fri',
            workText: 'VIERNES',
            lunchText: 'comida',
            lunchOptional: true
        }],
        timeValues: [
            '07:00', '07:05', '07:10', '07:15', '07:20', '07:25', '07:30', '07:35', '07:40', '07:45', '07:50', '07:55',
            '08:00', '08:05', '08:10', '08:15', '08:20', '08:25', '08:30', '08:35', '08:40', '08:45', '08:50', '08:55',
            '09:00', '09:05', '09:10', '09:15', '09:20', '09:25', '09:30', '09:35', '09:40', '09:45', '09:50', '09:55',
            '10:00', '10:05', '10:10', '10:15', '10:20', '10:25', '10:30', '10:35', '10:40', '10:45', '10:50', '10:55',
            '11:00', '11:05', '11:10', '11:15', '11:20', '11:25', '11:30', '11:35', '11:40', '11:45', '11:50', '11:55',
            '12:00', '12:05', '12:10', '12:15', '12:20', '12:25', '12:30', '12:35', '12:40', '12:45', '12:50', '12:55',
            '13:00', '13:05', '13:10', '13:15', '13:20', '13:25', '13:30', '13:35', '13:40', '13:45', '13:50', '13:55',
            '14:00', '14:05', '14:10', '14:15', '14:20', '14:25', '14:30', '14:35', '14:40', '14:45', '14:50', '14:55',
            '15:00', '15:05', '15:10', '15:15', '15:20', '15:25', '15:30', '15:35', '15:40', '15:45', '15:50', '15:55',
            '16:00', '16:05', '16:10', '16:15', '16:20', '16:25', '16:30', '16:35', '16:40', '16:45', '16:50', '16:55',
            '17:00', '17:05', '17:10', '17:15', '17:20', '17:25', '17:30', '17:35', '17:40', '17:45', '17:50', '17:55',
            '18:00', '18:05', '18:10', '18:15', '18:20', '18:25', '18:30', '18:35', '18:40', '18:45', '18:50', '18:55',
            '19:00', '19:05', '19:10', '19:15', '19:20', '19:25', '19:30'
        ],
        lunchDurationValues: [
            15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90
        ],
        initialLunchTimeStart: '13:00',
        initialLunchTimeDuration: 60,
        copyPasteStatus: 'NONE',
        copyPasteCopiedFrom: '',
        copyPasteType: '',
        copyPasteCopiedValue: ''
    },

    // The constructor
    _create: function() {
        const self = this

        const $element = this.element

        $element.attr('id', this.options.id)

        this._configureTimeValues()

        $element.append($('<h3>', { text: this.options.title }));

        this.options.days.forEach((day, i, arr) => {
            $element.append(self._getFormGroup(day.idSuffix, day.workText, day.lunchText, day.lunchOptional))

            if (i < (arr.length - 1)) {
                $element.append($('<hr>'))
            }
        })

        this._initializeSliders()
        this._initializeToolbars()
        this._initializeCopyPaste()
        this._initializeOptionalLunchs()
    },

    _configureTimeValues: function() {
        const timeValuesMin = $element.data('time-values-min')
        const timeValuesMax = $element.data('time-values-max')
        const timeFormat = $element.data('time-format')
        const intervalQty = parseInt($element.data('interval-qty'))
        const intervalUnit = $element.data('interval-unit')

        var timeValues = []
        var currentMoment = moment(timeValuesMin, timeFormat)
        const maxMoment = moment(timeValuesMax, timeFormat)

        while (currentMoment.isBefore(maxMoment)) {
            timeValues.push(currentMoment.format(timeFormat))
            currentMoment.add(intervalQty, intervalUnit)
        }

        this.options.timeValues = timeValues
    },

    _getFormGroup: function(idSuffix, workText, lunchText, lunchOptional) {
        const $formGroup = $('<div>', { 'class': 'form-group' })

        // Work row
        const $workRow = $('<div>', { 'class': 'row mb-2 align-items-center' })
        $workRow.append($('<label>', { 'for': `${this.options.id}WorkTime${idSuffix}`, 'class': 'col-sm-3 my-0', text: workText }))

        const $workRowCol = $('<div>', { 'class': 'col-sm-9' })

        const $workRowColRow = $('<div>', { 'class': 'row align-items-center' })
        $workRowColRow.append($('<div>', { 'class': 'col-2 col-sm-2 col-md-2 px-0 text-center toolbar work-toolbar' }))
        $workRowColRow.append($('<div>', { 'class': 'col' }).append($('<input>', { type: 'text', 'class': 'rangeSlider workTimeSlider', id: `${this.options.id}WorkTime${idSuffix}`, name: `${this.options.id}WorkTime${idSuffix}`, value: '', 'data-lunchrangeslider': `${this.options.id}LunchTime${idSuffix}` })))

        $workRowCol.append($workRowColRow)

        $workRow.append($workRowCol)

        // Lunch row
        const $lunchRow = $('<div>', { 'class': 'row align-items-center' })

        const $lunchText = $('<label>', { 'for': `${this.options.id}LunchTime${idSuffix}`, 'class': 'col-sm-3 mb-1 mb-sm-0 text-muted' }).append($('<span>', { text: lunchText }))

        if (lunchOptional) {
            $lunchText
                .addClass('lunch-optional')
                .attr('data-idsuffix', idSuffix)
                .prepend($('<i>', { 'class': 'fas fa-ban mr-1' }))
        }

        $lunchRow.append($lunchText)

        const $lunchRowCol = this._getLunchRowCol(idSuffix)

        $lunchRow.append($lunchRowCol)

        // Append work and lunch rows
        $formGroup.append($workRow).append($lunchRow)

        return $formGroup
    },

    _getLunchRowCol: function(idSuffix) {
        const $lunchRowCol = $('<div>', { 'class': 'col-sm-9 lunch-area' })

        const $lunchRowColRow = $('<div>', { 'class': 'row align-items-center' })
        $lunchRowColRow.append($('<div>', { 'class': 'col-2 col-sm-2 col-md-2 px-0 text-center toolbar lunch-toolbar' }))
        $lunchRowColRow.append($('<div>', { 'class': 'col' }).append($('<input>', { type: 'text', 'class': 'rangeSlider lunchTimeSlider', id: `${this.options.id}LunchTime${idSuffix}`, name: `${this.options.id}LunchTime${idSuffix}`, value: '' })))

        $lunchRowCol.append($lunchRowColRow)

        return $lunchRowCol
    },

    _initializeToolbars: function($parent = undefined) {
        if ($parent) {
            $parent.find('.work-toolbar').append(this._getCopyI('work'))
            $parent.find('.lunch-toolbar').append(this._getCopyI('lunch'))
        } else {
            $('.work-toolbar').append(this._getCopyI('work'))
            $('.lunch-toolbar').append(this._getCopyI('lunch'))
        }
    },

    _initializeSliders: function($parent = undefined) {
        const self = this

        const $workTimeSliders = $parent ? $parent.find('.workTimeSlider') : $('.workTimeSlider')

        $workTimeSliders.ionRangeSlider({
            type: 'double',
            skin: 'flat',
            values: this.options.timeValues,
            from_max: this.options.timeValues.indexOf('10:30'),
            to_min: this.options.timeValues.indexOf('14:00'),
            decorate_both: false,
            onUpdate: function(data) {
                const lunchTimeSliderId = data.input.data('lunchrangeslider')

                self._updateLunchTimeSliderLimits(lunchTimeSliderId, data.from, data.to)
            },
            onChange: function(data) {
                const lunchTimeSliderId = data.input.data('lunchrangeslider')

                self._updateLunchTimeSliderLimits(lunchTimeSliderId, data.from, data.to)
                self._updateCopyPasteStatus(data.input.attr('id'))
            }
        });

        const $lunchTimeSliders = $parent ? $parent.find('.lunchTimeSlider') : $('.lunchTimeSlider')

        $lunchTimeSliders.ionRangeSlider({
            type: 'double',
            skin: 'flat',
            values: this.options.timeValues,
            from: this.options.timeValues.indexOf(this.options.initialLunchTimeStart),
            to: this.options.timeValues.indexOf(this.options.initialLunchTimeStart) + (this.options.initialLunchTimeDuration / 5),
            drag_interval: true,
            min_interval: 30 / 5,
            max_interval: 90 / 5,
            onChange: function (data) {
                const lunchTimeSliderId = data.input.data('lunchrangeslider')

                self._updateCopyPasteStatus(data.input.attr('id'))
            }
        });
    },

    _initializeCopyPaste: function() {
        const self = this

        $(this.element).on('click', '.copy-paste-tools', function(e) {
            e.preventDefault()
            e.stopPropagation()
            const $clickedElement = $(e.currentTarget)

            if ($clickedElement.hasClass('copied')) {
                $clickedElement.removeClass('copied')
                self.options.copyPasteStatus = 'NONE'
                self._resetCopyPaste(false)
            } else if ($clickedElement.hasClass('paste')) {
                const copyPasteCopiedValueParts = self.options.copyPasteCopiedValue.split(';')
                const from = self.options.timeValues.indexOf(copyPasteCopiedValueParts[0])
                const to = self.options.timeValues.indexOf(copyPasteCopiedValueParts[1])

                const rangeSliderInstance = self._getRangeSliderFromClickeElement($clickedElement).data('ionRangeSlider')

                rangeSliderInstance.update({
                    from: from,
                    to: to
                })

                if (self.options.copyPasteStatus === 'COPIED') {
                    self._resetCopyPaste(false)
                }
            } else {
                const $row = $clickedElement.closest('.row')

                const $rangeSliderElement = $row.find('.rangeSlider')

                if ($rangeSliderElement.hasClass('workTimeSlider')) {
                    clickedCopyPasteType = 'work'
                } else {
                    clickedCopyPasteType = 'lunch'
                }

                self._resetCopyPaste()

                $row.find(`.copy-paste-${clickedCopyPasteType}`).addClass('copied')
                self.options.copyPasteStatus = 'COPIED'
                self.options.copyPasteType = clickedCopyPasteType
                self.options.copyPasteCopiedFrom = $rangeSliderElement.attr('id')
                self.options.copyPasteCopiedValue = $rangeSliderElement.val()
             }

            self._updateCopyPasteButtons()
        });
    },

    _initializeOptionalLunchs: function() {
        const self = this

        $('.lunch-optional').on('click', function(e) {
            event.preventDefault()
            event.stopPropagation()

            const $clickedElement = $(e.currentTarget)

            if ($clickedElement.hasClass('disabled')) {
                $clickedElement.removeClass('disabled')
                $clickedElement.find('span').text('comida')

                const $row = $clickedElement.closest('.row')

                $row.append(self._getLunchRowCol($clickedElement.data('idsuffix')))
                self._initializeToolbars($row)
                self._initializeSliders($row)

                $clickedElement.closest('.form-group').find('.workTimeSlider').data('ionRangeSlider').update()
            } else {
                $clickedElement.addClass('disabled')
                $clickedElement.find('span').text('sin comida')

                $clickedElement.closest('.row').find('.lunch-area').remove()
            }
        })
    },

    _updateLunchTimeSliderLimits: function(lunchTimeSliderId, from, to) {
        var lunchTimeSliderInstance = $(`#${lunchTimeSliderId}`).data('ionRangeSlider');

        if (lunchTimeSliderInstance) {
            var initialFrom = lunchTimeSliderInstance.old_from
            var initialTo = lunchTimeSliderInstance.old_to

            var newLimits = {
                from_min: from,
                from_max: to,
                to_min: from,
                to_max: to,
                from: initialFrom,
                to: initialTo
            };

            if (from > initialFrom) {
                const diff = from - initialFrom
                newLimits.from += diff
                newLimits.to += diff
            }

            if (to < initialTo) {
                const diff = to - initialTo
                newLimits.from += diff
                newLimits.to += diff
            }

            lunchTimeSliderInstance.update(newLimits);
        }
    },

    _updateCopyPasteStatus: function(workTimeSliderId) {
        if (workTimeSliderId === this.options.copyPasteCopiedFrom) {
            this._resetCopyPaste()
        }
    },

    _resetCopyPaste: function(toCopyPasteButtons = true) {
        this.options.copyPasteStatus = 'NONE'
        this.options.copyPasteType = ''
        this.options.copyPasteCopiedFrom = ''
        this.options.copyPasteCopiedValue = ''

        if (toCopyPasteButtons) {
            this._updateCopyPasteButtons()
        }
    },

    _updateCopyPasteButtons: function() {
        if (this.options.copyPasteStatus === 'COPIED') {
            $(`#${this.options.id} .copy-paste-${this.options.copyPasteType}:not('.copied')`).replaceWith(this._getPasteI(this.options.copyPasteType))

            const copyPasteInvertedType = this.options.copyPasteType === 'work' ? 'time' : 'work'
            $(`#${this.options.id} .copy-paste-${copyPasteInvertedType}`).replaceWith(this._getCopyI(copyPasteInvertedType))
        } else if (this.options.copyPasteStatus === 'NONE') {
            $(`#${this.options.id} .copy-paste-work`).replaceWith(this._getCopyI('work'))
            $(`#${this.options.id} .copy-paste-lunch`).replaceWith(this._getCopyI('lunch'))
        }
    },

    _getCopyI: function(type) {
        return $('<i>', { 'class': `far fa-copy copy-paste-tools copy-paste-${type}`, 'title': 'Copiar' })
    },

    _getPasteI: function(type) {
        return $('<i>', { 'class': `fas fa-paste copy-paste-tools copy-paste-${type} paste`, 'title': 'Pegar' })
    },

    _getRangeSliderFromClickeElement: function(clickedElement) {
         return clickedElement.closest('.row').find('.rangeSlider')
     },

    // Events bound via _on are removed automatically
    // revert other modifications here
    _destroy: function() {
        // remove generated elements
        this.changer.remove();

        this.element
            .removeClass( "custom-colorize" )
            .enableSelection()
            .css( "background-color", "transparent" );
    },

    // _setOptions is called with a hash of all options that are changing
    // always refresh when changing options
    _setOptions: function() {
        // _super and _superApply handle keeping the right this-context
        this._superApply(arguments);
        this._refresh();
    },

    // _setOption is called for each individual option that is changing
    _setOption: function(key, value) {
        // prevent invalid color values
        if (/red|green|blue/.test(key) && (value < 0 || value > 255)) {
            return;
        }

        this._super(key, value);
    }
});