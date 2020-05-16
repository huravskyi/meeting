<template>
    <v-menu
            ref="menu"
            v-model="menu"
            :close-on-content-click="false"
            transition="scale-transition"
            offset-y
            max-width="290px"
            min-width="290px"
    >
        <template v-slot:activator="{ on }">
            <v-text-field
                    v-model="date"
                    label="Дата рождения"
                    prepend-icon="event"
                    readonly
                    v-on="on"
                    required
                    :rules="dateRules"
                    name="date"
                    type="text"
            ></v-text-field>
        </template>
        <v-date-picker
                ref="picker"
                v-model="date"
                :max="getDate()"
                min="1950-01-01"
                @change="save"
        ></v-date-picker>
    </v-menu>
</template>

<script>
    export default {
        props: ['age', 'setLocalStorage'],
        name: "ChooseAge",
        data: () => ({
            date: '',
            menu: false,
            dateRules: [
                v => !!v || 'Дата обязательна',
            ],
        }),
        mounted() {
            if (this.age !== undefined) {
                this.date = this.age
            }
        },
        watch: {
            menu(val) {
                val && setTimeout(() => (this.$refs.picker.activePicker = 'YEAR'))
            },
        },
        methods: {
            save(date) {
                this.$refs.menu.save(date)
                this.setLocalStorage('date', this.date)

            },
            getDate() {
                return this.calculateDate()
            },
            calculateDate() {
                return formatDate(new Date)

                function formatDate(date) {

                    let dd = date.getDate();
                    if (dd < 10) dd = '0' + dd;

                    let mm = date.getMonth() + 1;
                    if (mm < 10) mm = '0' + mm;

                    let yy = date.getFullYear();
                    yy = yy - 17
                    return yy + '-' + mm + '-' + dd;
                }
            }
        }
    }
</script>

<style scoped>

</style>