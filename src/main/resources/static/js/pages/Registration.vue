<template>
    <div class="login-background">
        <v-container>
            <v-layout>
                <v-form
                        ref="form"
                        v-model="form"
                        class="form-login ma-auto ma-sm-0"
                        action="/registration"
                        method="post"
                        align="center"
                >
                    <div :v-model="error" class="py-6">
                        <p class="red--text">{{err}}</p>
                    </div>
                    <h1 class="title" align="center">Регистрация</h1>
                    <v-text-field
                            type="text"
                            name="username"
                            :counter="12"
                            :rules="nameRules"
                            label="Имя"
                            required
                    ></v-text-field>

                    <v-text-field
                            label="Password"
                            required
                            type="password"
                            name="password"
                            :rules="passwordRules"
                    ></v-text-field>
                    <v-text-field
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
                            @keyup.enter="submit"
                            type="submit"
                            @click.stop="isLoading = !isLoading"
                    >Зарегистрироваться
                    </v-btn>
                </v-form>

            </v-layout>

        </v-container>
    </div>

</template>

<script>
    export default {
        name: "Registration",
        data: () => ({
            err:'',
            form:false,
            isLoading:false,
            name: '',
            nameRules: [
                v => !!v || 'name is required',
                v => (v && v.length > 3) || 'Name must be valid',
            ],
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

        }),
        computed: {
            error() {
                if (window.location.search == '?notFound') {
                    return this.err = 'Эта почта не зарегистрирована'
                }
                if (window.location.search == '?exist') {
                    return this.err = 'Эта почта уже используеться'
                }
                console.log(window.location)
                console.log(window.location.search)
            }
        }
    }
</script>

<style scoped>
    .form-login {
        width: 350px;
        height: 450px;
        background-color: white;
        padding: 25px;
        border-radius: 15px;

    }

    .login-background {
        height: 100%;
        background-image: url("https://firebasestorage.googleapis.com/v0/b/klinicheskij-psiholog.appspot.com/o/login_backgraund.jpg?alt=media&token=cd8938ed-4508-4848-827d-9ca72221df2a");
        background-position: 40% 20%;
    }
</style>