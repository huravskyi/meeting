<template>
    <div class="login-background">
        <v-container>
            <v-layout>
                <v-form
                        ref="form"
                        v-model="form"
                        class="form-login ma-auto ma-sm-0"
                        action="/login"
                        method="post"
                        align="center"
                        autocomplete="on"
                >
                    <p class="title red--text" :v-model="error">{{err}}</p>
                    <h1 class="title">Войти</h1>
                    <v-text-field
                            autocomplete="email"
                            label="Email"
                            v-model="email"
                            required
                            :rules="emailRules"
                            type="email"
                            name="email"
                    ></v-text-field>
                    <v-text-field autocomplete="current-password"
                                  :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
                                  @click:append="show = !show"
                                  :type="show ? 'text' : 'password'"
                                  label="Пароль"
                                  v-model="password"
                                  required
                                  :rules="passwordRules"
                                  name="password"
                    ></v-text-field>
                    <v-flex
                            class="d-flex justify-space-around pt-5"
                    >
                        <router-link class="prompt" to="/registration">регистрация</router-link>
                        <router-link class="prompt" to="/login/forgotPassword">забыли пароль?</router-link>
                    </v-flex>
                    <v-btn
                            :disabled="!form"
                            :loading="isLoading"
                            color="primary"
                            style="min-width: 200px"
                            class="mt-10"
                            @keyup.enter="submit"
                            type="submit"
                            @click.stop="isLoading = !isLoading"
                    >Войти
                    </v-btn>
                </v-form>
            </v-layout>
        </v-container>
    </div>
</template>

<script>

    export default {
        name: "Auth",
        data: () => ({
            show:false,
            err: '',
            form: false,
            isLoading: false,

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
        }),
        computed: {
            error() {
                if (window.location.search === '?error') {
                    return this.err = 'Неверный логин или пароль'
                }
            }
        },
        methods: {
            reg() {
                this.$router.push('/registration')
            }
        },
    }
</script>

<style scoped>
    .form-login {
        width: 320px;
        height: 440px;
        background-color: white;
        padding: 25px;
        border-radius: 15px;
    }

    .prompt {
        font-size: small;
        text-decoration: none;
    }


    .login-background {
        height: 100%;
        background-image: url("https://img.fonwall.ru/o/kk/more-plyazh-devushka-paren.jpg");
        background-position: 40% 20%;
    }

    @media screen and (max-width: 600px) {
        .login-background {
            background-position: 76% 20%
        }

        .form-login {
            background-image: url("https://img.fonwall.ru/o/kk/more-plyazh-devushka-paren.jpg");
            background-position: inherit;
        }
    }
</style>