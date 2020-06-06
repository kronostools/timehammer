$.widget('timehammer.loading', {
    options: {
        id: 'loading',
        showing: false
    },

    // The constructor
    _create: function() {
        const $element = this.element

        var initialClass

        if (this.options.showing) {
            initialClass = 'show'
        } else {
            initialClass = 'hide'
        }

        const $spinnerDiv = $('<div>', { 'class': 'spinner offset-5 col-2 text-center my-auto' })
            .append($('<i>', { 'class': 'fa fa-circle-o-notch fa-spin fa-3x fa-fw', 'aria-hidden': true }))

        $element
            .addClass('timehammer-loading-fade')
            .addClass('row')
            .addClass('h-100')
            .addClass('w-100')
            .addClass(initialClass)
            .append($spinnerDiv)
    },

    show: function() {
        this.element
            .removeClass('hide')
            .addClass('show')
    },

    hide: function() {
        this.element
            .removeClass('show')
            .addClass('hide')
    }
})