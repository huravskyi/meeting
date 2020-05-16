<template>
    <v-container>
        <v-row justify="center"
               align="center"
               class="pt-1"
        >
            <v-col class="background-settings " style="max-width: 550px">
                <v-row justify="center">
                    <h1 class="title">Настройки</h1>
                </v-row>
                <v-row justify="center">
                    <v-row style="max-width: 550px"
                           align="center"
                           justify="space-between"
                           class="pa-6"
                    >
                        <div style="width: 150px">
                            Электронная почта:
                        </div>

                        <div>
                            <v-form ref="form"
                                    v-model="formEmail"
                                    style="width: 220px">
                                <v-text-field
                                        :disabled="disabledEmail"
                                        label="Email"
                                        v-model="email"
                                        required
                                        :rules="emailRules"
                                        type="email"
                                        name="email"
                                ></v-text-field>
                            </v-form>
                        </div>
                        <div style="width: 60px">
                            <v-btn :disabled="!formEmail"
                                   x-small
                                   text
                                   class="pa-0"
                                   @click="editingEmail()"
                            >
                                {{disabledEmail? 'Изменить':'Сохранить'}}
                            </v-btn>
                        </div>
                    </v-row>
                </v-row>

                <v-row justify="center">
                    <v-row style="max-width: 550px"
                           align="center"
                           justify="space-between"
                           class="pa-6"
                    >
                        <div style="width: 150px">
                            Пароль старый:
                        </div>

                        <div>
                            <v-form ref="formPassword"
                                    v-model="formPassword"
                                    style="width: 220px">
                                <input hidden type="text" name="username" autocomplete="username">
                                <v-text-field
                                        autocomplete="current-password"
                                        label="Password"
                                        v-model="password"
                                        required
                                        :rules="passwordRules"
                                        type="password"
                                        name="password"
                                        placeholder="******"
                                        :disabled="disabledPassword"
                                ></v-text-field>
                            </v-form>
                        </div>
                        <div style="width: 60px">
                            <v-btn x-small text class="pa-0"
                                   @click="editingOpen"

                            >{{disabledPassword? 'Изменить': 'Отмена'}}
                            </v-btn>
                        </div>

                        <v-expand-transition>
                            <div v-show="show" style="width: 100%">
                                <div class="d-flex justify-space-between align-center flex-wrap ">
                                    <div style="width: 150px">
                                        Новый пароль:
                                    </div>

                                    <div>
                                        <v-form ref="formPassword"
                                                v-model="formPasswordNew"
                                                style="width: 220px">
                                            <input hidden type="text" name="username" autocomplete="username">
                                            <v-text-field
                                                    autocomplete="new-password"
                                                    label="New Password"
                                                    v-model="newPassword"
                                                    required
                                                    :rules="passwordRules"
                                                    type="password"
                                                    name="password"
                                                    placeholder="******"
                                                    :disabled="disabledPassword"
                                            ></v-text-field>
                                        </v-form>
                                    </div>
                                    <div style="width: 60px">
                                        <v-btn :disabled="!formPassword || !formPasswordNew"
                                               x-small
                                               text
                                               class="pa-0"
                                               @click="editingPassword">
                                            Сохранить
                                        </v-btn>
                                    </div>
                                </div>
                            </div>
                        </v-expand-transition>
                    </v-row>
                </v-row>
                <v-divider></v-divider>
                <v-row style="background-color: #008fff" class=" pa-4 justify-space-between align-center">
                    <h3>Удалить акаунт</h3>
                    <v-btn text color="red" @click="dialog=!dialog">удалить</v-btn>
                </v-row>
            </v-col>
        </v-row>
        <v-row justify="center"
               align="center"
               class="pa-4 mb-10"
        >
            <div>
                <h3>Служба поддержки</h3>
                <div class="d-flex">
                    <p class="pr-3">Почта:</p>
                    <p>
                        <a href="mailto:datinglove.help@gmail.com?subject=Help">datinglove.help@gmail.com</a>
                    </p>
                </div>

            </div>

        </v-row>
        <v-snackbar
                v-model="modelSnackbar"
                :timeout="timeout"
                top
                absolute
        >
            {{ textSnackbar }}
            <v-btn
                    color="blue"
                    text
                    @click="modelSnackbar = false"
            >
                Close
            </v-btn>
        </v-snackbar>
        <dialog-admin :text="textDialog"
                      :dialog="dialog"
                      :delete-and-warning="deleteAccount"
                      v-on:dialog-event="dialog = $event"
        >
        </dialog-admin>
        <div class="text-center">
            <v-overlay :value="overlay">
                <v-progress-circular indeterminate size="64"><h1>Подождите изменения в процесе</h1>
                </v-progress-circular>
            </v-overlay>
        </div>
    </v-container>
</template>

<script>
    import {mapState, mapActions} from 'vuex'
    import DialogAdmin from "../components/adminPanel/DialogAdmin.vue";

    export default {
        name: "Settings",
        components: {DialogAdmin},
        data: () => ({
            timeout:2000,
            modelSnackbar: false,
            textSnackbar: 'Профиль удален',
            textDialog: 'Удалить анкету?',
            dialog: false,
            overlay: false,
            show: false,
            formEmail: false,
            formPassword: false,
            formPasswordNew: false,
            disabledPassword: true,
            disabledEmail: true,
            panel: [],
            email: null,
            password: '',
            newPassword: '',
            emailRules: [
                v => !!v || 'E-mail is required',
                v => /.+@.+\..+/.test(v) || 'E-mail должен быть действительным',
            ],
            passwordRules: [
                v => !!v || 'необходим пароль',
                v => (v && v.length >= 6 && v && v.length < 25) || 'Пароль должен быть не более 25 символов и не менее 6 символов',
            ],
        }),
        computed: {
            ...mapState({
                userProfile: state => state.storeUserProfile.userProfile,
            }),
        },
        mounted() {
            this.email = this.userProfile.email
        },
        methods: {
            ...mapActions(['changePasswordOrEmail', 'deleteAccountAction']),
            deleteAccount() {
                this.dialog = false
                const result = this.deleteAccountAction(this.userProfile.id)
                if (result) {
                    this.modelSnackbar = true
                    setTimeout(() => location.reload(), 1000)
                }
            },
            editingOpen() {
                if (this.disabledPassword) {
                    this.disabledPassword = false
                    this.show = true
                } else {
                    this.disabledPassword = true
                    this.show = false
                }
            },
            editingPassword() {
                if (this.password !== this.newPassword) {
                    const user = {
                        id: this.userProfile.id,
                        password: this.password,
                        newPassword: this.newPassword
                    }
                    let answer = this.changePasswordOrEmail(user)
                    this.disabledPassword = true
                    this.password = ''
                    this.newPassword = ''
                    answer.then(
                        result => {
                            if (result) {
                                alert("Новый пароль успешно сохранен")
                                this.show = false
                            }else{
                                alert("Ошибка старый пароль не совпадает")
                            }
                        },
                        error => {
                            alert("Rejected: " + error)
                        }
                    )
                }
            },
            editingEmail() {
                if (this.disabledEmail) {
                    this.disabledEmail = false
                } else {
                    const user = {
                        id: this.userProfile.id,
                        email: this.email
                    }
                    if (this.email !== this.userProfile.email) {
                        this.overlay = true
                        let answer = this.changePasswordOrEmail(user)
                        answer.then(
                            result => {
                                this.overlay = false
                                result ?
                                    alert("На новую почту вам придет письмо, подтвердите что бы изменить почтовый ящик")
                                    : alert("Ошибка такая почта уже зарегистрирована")

                            },
                            error => {
                                this.overlay = false
                                alert("Rejected: " + error)
                            }
                        )
                    }
                    this.disabledEmail = true
                }
            },
        },
    }
</script>

<style scoped>
    .background-settings {
        background-color: #f2f3f5;
    }
</style>