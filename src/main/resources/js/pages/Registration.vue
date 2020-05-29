<template>
    <div class="login-background">
        <v-container>
            <v-layout>
                <v-card v-if="success" dark>
                    Регистрация успешна
                    <router-link to="/login">войти</router-link>
                </v-card>
                <v-form v-else
                        autocomplete="off"
                        ref="form"
                        v-model="form"
                        class=" form-login ma-auto ma-sm-0"
                        action="/registration"
                        method="post"
                        align="center"
                        name="formRegistration"
                        id="formLogin"
                >
                    <div :v-model="errorSuccess" class="py-2">
                        <p class="red--text">{{err}}</p>
                    </div>
                    <h1 class="title text--black" align="center">Регистрация</h1>
                    <v-text-field
                            type="text"
                            name="username"
                            :counter="12"
                            :rules="nameRules"
                            label="Имя"
                            required
                            v-model="valueUsername"
                            @change="setLocalStorage('username', valueUsername)"
                    ></v-text-field>

                    <v-text-field
                            autocomplete="new-password"
                            :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
                            label="Пароль"
                            required
                            :type="show ? 'text' : 'password'"
                            name="password"
                            :rules="passwordRules"
                            @click:append="show = !show"
                            v-model="valuePassword"
                            @change="setLocalStorage('password', valuePassword)"
                    ></v-text-field>

                    <form autocomplete="off">
                        <autocomplete-city locale-search="null" :rules="true" ref="formCity">
                        </autocomplete-city>
                    </form>

                    <choose-age :age="valueDate" :setLocalStorage="setLocalStorage"></choose-age>

                    <v-select
                            v-model="valueGender"
                            :items="items"
                            :rules="[v => !!v || 'Item is required']"
                            label="Пол"
                            @change="setLocalStorage('gender', valueGender)"

                    ></v-select>
                    <input hidden
                           name="gender"
                           v-model="valueGender"
                           type="text"
                           required>

                    <v-text-field
                            autocomplete="username"
                            label="Email"
                            :rules="emailRules"
                            required
                            type="email"
                            name="email"
                    ></v-text-field>
                    <v-btn
                            :disabled="!form"
                            :loading="isLoading"
                            color="primary"
                            style="min-width: 200px"
                            class="mt-10"
                            @click.stop="isLoading = !isLoading"
                            @click="submitForm()"
                    >Зарегистрироваться
                    </v-btn>
                </v-form>
            </v-layout>
        </v-container>
    </div>
</template>

<script>
    import ChooseAge from "../components/profile/ChooseAge.vue";
    import AutocompleteCity from "../components/profile/AutocompleteCity.vue";
    import ForgotPassword from "./ForgotPassword.vue";

    const getFunctionLodash = () => import("../util/helper/functionLodash");

    export default {
        name: "Registration",
        components: {ForgotPassword, AutocompleteCity, ChooseAge},
        data: () => ({
            valueUsername: '',
            valuePassword: '',
            valueDate: '',
            dateNow: '',
            show: false,
            modal: false,
            err: '',
            form: false,
            isLoading: false,
            name: '',
            cityRules: [v => !!v || 'Город обязательно',],
            nameRules: [
                v => !!v || 'имя обязательно',
                v => (v && v.length > 2 && v.length < 20) || 'Имя должно быть действительным не' +
                    'менее 3 символов и не более 20',
            ],
            email: '',
            emailRules: [
                v => !!v || 'E-mail обязательно',
                v => /.+@.+\..+/.test(v) || 'E-mail должен быть действительным',
            ],
            password: '',
            passwordRules: [
                v => !!v || 'необходим пароль',
                v => (v && v.length >= 6 && v && v.length < 25) || 'Пароль должен быть менее 25 символов и не короче 6 символов',
            ],
            valueGender: '',
            items: [
                'Мужчина',
                'Женщина'
            ],
            windowSuccess: window.location.search === '?success'
        }),
        beforeMount() {
            this.valueUsername = localStorage.getItem('username')
            this.valuePassword = localStorage.getItem('password')
            this.valueGender = localStorage.getItem('gender')
            this.valueDate = localStorage.getItem('date')
        },
        mounted() {
            if (localStorage.getItem('username') !== null) {
                this.$refs.form.validate()

            } else {
                if (!this.windowSuccess)
                    this.$refs.form.resetValidation()
            }
        },
        computed: {
            success() {
                if (this.windowSuccess) {
                    const items = ['username', 'date', 'gender', 'password']
                    items.forEach(value => {
                        localStorage.removeItem(value)
                    })
                    return true
                } else {
                    return false
                }
            },
            errorSuccess() {
                if (window.location.search === '?notFound') {
                    return this.err = 'Эта почта не зарегистрирована'
                }
                if (window.location.search === '?exist') {
                    return this.err = 'Эта почта уже используеться'
                }
            }
        },
        methods: {
            setLocalStorage(key, value) {
                localStorage.setItem(key, value)
            },
            submitForm() {
                const entries = this.$refs.formCity.entries
                let locale = this.$refs.form.inputs[2].lazyValue
                let locate
                getFunctionLodash().then(res => {
                    this.sendForm(res.getLocateUser(locale, entries))
                })
            },
            sendForm(locate) {
                let form = document.getElementById('formLogin')
                let inputCity = document.createElement('input');
                let inputRegion = document.createElement('input');
                let inputCountry = document.createElement('input');
                inputCity.name = 'city'
                inputCity.value = locate.city
                inputCity.style.opacity = '0'

                inputRegion.name = 'region'
                inputRegion.value = locate.region
                inputRegion.style.opacity = '0'

                inputCountry.name = 'country'
                inputCountry.value = locate.country
                inputCountry.style.opacity = '0'

                form.append(inputCity)
                form.append(inputRegion)
                form.append(inputCountry)

                form.append(inputCity)
                form.append(inputRegion)
                form.append(inputCountry)
                form.submit()
            }
        }
    }
</script>

<style scoped>
    .form-login {
        width: 350px;
        height: 650px;
        padding: 25px;
        border-radius: 15px;
        text-transform: uppercase;
        font-family: "Roboto Slab", serif;
        font-weight: 300;
        font-size: 30px;

    }

    .login-background {
        height: 700px;
        background-image: url("https://firebasestorage.googleapis.com/v0/b/klinicheskij-psiholog.appspot.com/o/login_backgraund.jpg?alt=media&token=cd8938ed-4508-4848-827d-9ca72221df2a");
        background-position: 40% 20%;
    }

    @media screen and (max-width: 600px) {
        .login-background {
            background-position: right;
        }

        /*.form-login {*/
        /*    background-image: url("https://firebasestorage.googleapis.com/v0/b/klinicheskij-psiholog.appspot.com/o/login_backgraund.jpg?alt=media&token=cd8938ed-4508-4848-827d-9ca72221df2a");*/
        /*    background-position: 40% 20%;*/
        /*}*/
    }
</style>