<template>
    <div class="login-background">
        <v-container>
            <v-layout>
                <v-form
                        ref="form"
                        v-model="form"
                        class="form-login ma-auto ma-sm-0"
                        action="/login/forgotPassword"
                        method="post"
                        align="center"
                >
                    <h1 class="title">Введите свою почту</h1>
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
                    >Cбросить пароль
                    </v-btn>
                </v-form>
            </v-layout>
        </v-container>
    </div>
</template>

<script>
    export default {
        name: "ForgotPassword",
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
        })
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

</style>