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
        minWorkTime: '07:00',
        maxWorkTime: '19:30',
        minLunchTime: '12:00',
        maxLunchTime: '16:00',
        initialLunchStart: '13:00',
        initialLunchEnd: '14:00',
        timeStep: 300,
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

        $element.append($('<h3>', { text: this.options.title }));

        this.options.days.forEach((day, i, arr) => {
            $element.append(self._getFormGroup(day.idSuffix, day.workText, day.lunchText, day.lunchOptional))

            if (i < (arr.length - 1)) {
                $element.append($('<hr>'))
            }
        })

        this._initializeToolbars()
        this._initializeCopyPaste()
        this._initializeOptionalLunchs()
    },

    _getFormGroup: function(idSuffix, workText, lunchText, lunchOptional) {
        const $formGroup = $('<div>', { 'class': 'form-group' })

        // Work row
        const $workRow = $('<div>', { 'class': 'row mb-2 align-items-center' })
        $workRow.append($('<label>', { 'for': `${this.options.id}WorkStart${idSuffix}`, 'class': 'col-sm-3 my-0', text: workText }))

        const $workRowCol = $('<div>', { 'class': 'col-sm-9' })

        const $workRowColRow = $('<div>', { id: `work${idSuffix}`, 'class': 'row align-items-center work' })
        $workRowColRow.append($('<div>', { 'class': 'col-2 col-sm-2 col-md-2 px-0 text-center toolbar work-toolbar' }))
        $workRowColRow.append($('<div>', { 'class': 'col' }).append($('<input>', { type: 'time', 'class': 'form-control-sm col-12 start', id: `${this.options.id}WorkStart${idSuffix}`, name: `workStart${idSuffix}`, value: this.options.minWorkTime, step: this.options.timeStep, min: this.options.minWorkTime, max: this.options.maxWorkTime })))
        $workRowColRow.append($('<div>', { 'class': 'col' }).append($('<input>', { type: 'time', 'class': 'form-control-sm col-12 end', id: `${this.options.id}WorkEnd${idSuffix}`, name: `workEnd${idSuffix}`, value: this.options.maxWorkTime, step: this.options.timeStep, min: this.options.minWorkTime, max: this.options.maxWorkTime })))

        $workRowCol.append($workRowColRow)

        $workRow.append($workRowCol)

        // Lunch row
        const $lunchRow = $('<div>', { 'class': 'row align-items-center' })

        const $lunchText = $('<label>', { 'for': `${this.options.id}LunchStart${idSuffix}`, 'class': 'col-sm-3 mb-1 mb-sm-0 text-muted' }).append($('<span>', { text: lunchText }))

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

        const $lunchRowColRow = $('<div>', { id: `lunch${idSuffix}`, 'class': 'row align-items-center lunch' })
        $lunchRowColRow.append($('<div>', { 'class': 'col-2 col-sm-2 col-md-2 px-0 text-center toolbar lunch-toolbar' }))
        $lunchRowColRow.append($('<div>', { 'class': 'col' }).append($('<input>', { type: 'time', 'class': 'form-control-sm col-12 start', id: `${this.options.id}LunchStart${idSuffix}`, name: `lunchStart${idSuffix}`, value: this.options.initialLunchStart, step: this.options.timeStep, min: this.options.minLunchTime, max: this.options.maxLunchTime })))
        $lunchRowColRow.append($('<div>', { 'class': 'col' }).append($('<input>', { type: 'time', 'class': 'form-control-sm col-12 end', id: `${this.options.id}LunchEnd${idSuffix}`, name: `lunchEnd${idSuffix}`, value: this.options.initialLunchEnd, step: this.options.timeStep, min: this.options.minLunchTime, max: this.options.maxLunchTime })))

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

                const $row = $clickedElement.closest('.row')
                $row.find('.start').val(copyPasteCopiedValueParts[0])
                $row.find('.end').val(copyPasteCopiedValueParts[1])

                if (self.options.copyPasteStatus === 'COPIED') {
                    self._resetCopyPaste(false)
                }
            } else {
                const $row = $clickedElement.closest('.row')

                if ($row.hasClass('work')) {
                    clickedCopyPasteType = 'work'
                } else {
                    clickedCopyPasteType = 'lunch'
                }

                self._resetCopyPaste()

                $row.find(`.copy-paste-${clickedCopyPasteType}`).addClass('copied')
                self.options.copyPasteStatus = 'COPIED'
                self.options.copyPasteType = clickedCopyPasteType
                self.options.copyPasteCopiedFrom = $row.attr('id')
                self.options.copyPasteCopiedValue = $row.find('.start').val() + ";" + $row.find('.end').val()
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
})