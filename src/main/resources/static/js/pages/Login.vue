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
                >
                    <p class="title red--text" :v-model="error">{{err}}</p>
                    <h1 class="title">Войти</h1>
                    <v-text-field
                            label="Email"
                            v-model="email"
                            required
                            :rules="emailRules"
                            type="email"
                            name="email"
                    ></v-text-field>
                    <v-text-field
                            label="Password"
                            v-model="password"
                            required
                            :rules="passwordRules"
                            type="password"
                            name="password"
                    ></v-text-field>
                    <v-flex
                            class="d-flex justify-space-around pt-5"
                    >
                        <a class="prompt" href="/registration">регистрация</a>
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
        methods: {
            reg() {
                this.$router.push('/registration')
            }
        },
        data: () => ({
            frontendData: frontendData,
            err: '',
            form: false,
            isLoading: false,
            email: '',
            emailRules: [
                v => !!v || 'E-mail is required',
                v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
            ],
            password: '',
            passwordRules: [
                v => !!v || 'password is required',
                v => (v && v.length >= 6 && v && v.length < 25) || 'Password must be less than 25  characters and doesn\'t be shorter 6 characters',
            ],
            items: [
                {
                    src: 'https://img.fonwall.ru/o/kk/more-plyazh-devushka-paren.jpg',
                },
                {
                    src: 'https://firebasestorage.googleapis.com/v0/b/klinicheskij-psiholog.appspot.com/o/login_backgraund.jpg?alt=media&token=cd8938ed-4508-4848-827d-9ca72221df2a'
                },
            ]
        }),
        computed: {
            error() {
                if (window.location.search == '?error') {
                    return this.err = 'Неверный логин или пароль'
                }
                console.log(window.location)
                console.log(window.location.search)
            }
        }
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
            background-image: none;
        }
    }
</style>