document.addEventListener("DOMContentLoaded", function(event) {
    Vue.component('timehammer-timetable', {
        props: ['value'],
        data: function() {
            return {
            }
        },
        template: `
            <div>
            <b-card>
                <b-form-group id="workMonGroup" label="LUN" label-cols="2" label-cols-md="2" label-size="sm" label-class="font-weight-bold" class="mb-0">
                    <b-form-row>
                        <b-col cols="2" align="center">
                            <i class="fas fa-camera"></i>
                        </b-col>
                        <b-col cols="4">
                            <b-form-input id="startWorkMon" type="time" size="sm" v-bind:value="value" v-on:input="$emit('input', $event)"></b-form-input>
                        </b-col>
                        <b-col cols="2" align="center">
                            <span>-</span>
                        </b-col>
                        <b-col cols="4">
                            <b-form-input id="endWorkMon" type="time" size="sm" v-bind:value="value" v-on:input="$emit('input', $event)"></b-form-input>
                        </b-col>
                    </b-form-row>
                    <b-form-row>
                        <b-col cols="2" align="center">
                            <i class="fas fa-play"></i>
                        </b-col>
                        <b-col cols="4">
                            <b-form-input id="startLunchMon" type="time" size="sm" v-bind:value="value" v-on:input="$emit('input', $event)"></b-form-input>
                        </b-col>
                        <b-col cols="2" align="center">
                            <span>-</span>
                        </b-col>
                        <b-col cols="4">
                            <b-form-input id="endLunchMon" type="time" size="sm" v-bind:value="value" v-on:input="$emit('input', $event)"></b-form-input>
                        </b-col>
                    </b-form-row>
                </b-form-group>
            </b-card>
            <b-card>
                <b-form-group id="workTueGroup" label="MAR" label-cols="2" label-cols-md="2" label-size="sm" label-class="font-weight-bold" class="mb-0">
                    <b-form-row>
                        <b-col cols="2" align="center">
                            <i class="fas fa-camera"></i>
                        </b-col>
                        <b-col cols="4">
                            <b-form-input id="startWorkTue" type="time" size="sm" v-bind:value="value" v-on:input="$emit('input', $event)"></b-form-input>
                        </b-col>
                        <b-col cols="2" align="center">
                            <span>-</span>
                        </b-col>
                        <b-col cols="4">
                            <b-form-input id="endWorkTue" type="time" size="sm" v-bind:value="value" v-on:input="$emit('input', $event)"></b-form-input>
                        </b-col>
                    </b-form-row>
                    <b-form-row>
                        <b-col cols="2" align="center">
                            <i class="fas fa-play"></i>
                        </b-col>
                        <b-col cols="4">
                            <b-form-input id="startLunchTue" type="time" size="sm" v-bind:value="value" v-on:input="$emit('input', $event)"></b-form-input>
                        </b-col>
                        <b-col cols="2" align="center">
                            <span>-</span>
                        </b-col>
                        <b-col cols="4">
                            <b-form-input id="endLunchTue" type="time" size="sm" v-bind:value="value" v-on:input="$emit('input', $event)"></b-form-input>
                        </b-col>
                    </b-form-row>
                </b-form-group>
            </b-card>
            </div>
        `
    })
})