document.addEventListener("DOMContentLoaded", function(event) {
    var app = new Vue({
        el: '#app',
        data: {
            form: {
                company: null,
                externalId: null,
                externalPassword: null,
                workCity: null,
                workSsid: null,
                startWorkMon: null
            },
            companies: [],
            cities: []
        },
        created() {
            this.companies.push({ value: 'COMUNYTEK', text: 'Comunytek'})
            this.cities.push({ value: 'MAD', text: 'Madrid'})
        },
        methods: {
            onSubmit: function(event) {
                alert(JSON.stringify(this.form))
            }
        }
    })
})