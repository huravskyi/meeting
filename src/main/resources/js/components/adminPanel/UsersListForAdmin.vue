<template>
    <div>
        <v-row>
            <v-col class="d-flex">
                <v-select v-model="modelSelected"
                          :items="items"
                          item-text="name"
                          item-value="event"
                          return-object
                          outlined
                ></v-select>
            </v-col>
            <v-col v-if="modelSelected.type !== 'Long'">
                <v-form v-model="validForm">
                    <v-text-field v-model="userField"
                                  :placeholder="modelSelected.placeholder"
                                  :type="modelSelected.type"
                                  :rules="modelSelected.rules"
                    ></v-text-field>
                </v-form>
            </v-col>
            <v-col>
                <v-btn height="56" @click="getUsersList()">{{modelSelected.nameBtn}}</v-btn>
            </v-col>
        </v-row>
        <v-row>
            <div class="mb-5 d-flex" >
                <div class="ma-auto" v-if="idAdmins" >ID Администраторов: </div>
                <template v-for="(id, i) in idAdmins">
                    <div class="ma-2" :key="i">
                        <v-btn @click="getUserById(id)"
                        >
                            user ID {{id}}
                        </v-btn>
                    </div>
                </template>
            </div>
        </v-row>
        <v-row>
            <v-card v-if="user !== null"
                    class="mb-5"
                    color="#f5f5f5">
                <v-row>
                    <v-col>
                        <v-img height="400"
                               width="400"
                               :lazy-src="accountPreviewMin"
                               :src="getMainImage(user)"
                               class="ml-3"
                        >
                        </v-img>
                    </v-col>
                    <v-col>
                        <v-card-text>
                            <v-simple-table
                            >
                                <template v-slot:default>
                                    <tbody>
                                    <tr v-for="item in userValue"
                                        :key="item.name"
                                    >
                                        <td :style="'color:'+ item.color ">{{ item.name }}</td>
                                        <td :style="'color:'+ item.color ">
                                            <router-link
                                                    v-if="item.router"
                                                    :to="'/profile/'+item.to">
                                                {{item.value}}
                                            </router-link>
                                            <div v-else-if="!item.check">{{item.value}}</div>

                                            <div v-if="item.check">
                                                <div v-for="(check, i) in checkboxSet"
                                                     :key="i"
                                                >
                                                    <v-checkbox :disabled="check.disabled"
                                                                v-model="user.roles"
                                                                :label="check.checkbox"
                                                                :value="check.checkbox"
                                                    >
                                                    </v-checkbox>
                                                </div>
                                            </div>
                                        </td>
                                        <td v-if="item.switch">
                                            <v-switch v-model="item.value"></v-switch>
                                        </td>
                                    </tr>
                                    </tbody>
                                </template>
                            </v-simple-table>
                        </v-card-text>
                        <v-card-actions>
                            <v-btn :loading="loading" class="ml-2" @click="saveNewValue()">
                                Применить
                            </v-btn>
                        </v-card-actions>
                    </v-col>
                </v-row>
            </v-card>
        </v-row>
    </div>
</template>

<script>
    import userApi from "../../api/user";
    import getMyAge from "../../util/helper/getAge";
    import {mapActions} from "vuex";

    const accountPreviewMin = ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/accountPreview-min.png?alt=media&token=209837ce-9ee9-47a7-ab45-7ba39d551f82')
    const accountPreview = ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/accountPreview.png?alt=media&token=8c1044c0-b371-4bf2-91e6-e0e7daf87c87')

    export default {
        name: "UsersListForAdmin",
        data: () => ({
            idAdmins: null,
            loading: false,
            accountPreviewMin,
            accountPreview,
            checkboxSet: [{disabled: true, checkbox: "USER"}, {disabled: false, checkbox: "ADMIN"}],
            user: null,
            validForm: false,
            userField: null,
            type: null,
            placeholder: null,
            modelSelected:
                {
                    type: 'number',
                    placeholder: 'Введите Id пользователя',
                    name: 'Поиск по ID',
                    nameBtn: 'Получить пользователя',
                    rules: [v => !!v || 'ID is required']
                },
            items: [
                {
                    type: 'number',
                    placeholder: 'Введите Id пользователя',
                    name: 'Поиск по ID',
                    nameBtn: 'Получить пользователя',
                    rules: [v => !!v || 'ID is required']
                },
                {
                    type: 'email',
                    placeholder: 'Введите email пользователя',
                    name: 'Поиск по почте',
                    nameBtn: 'Получить пользователя',
                    rules: [
                        v => !!v || 'E-mail is required',
                        v => /.+@.+\..+/.test(v) || 'E-mail должен быть действительным'
                    ]
                },
                {
                    type: 'Long',
                    name: 'Все ID администраторов',
                    nameBtn: 'Получить администраторов',
                },
            ],
            userValue: [],
        }),
        methods: {
            ...mapActions(['updateUserByAdminAction']),
            getMainImage(user) {
                return user.userpic ? user.userpic : this.accountPreview
            },
            saveNewValue() {
                this.loading = true
                const user = {
                    id: this.user.id,
                    roles: this.user.roles,
                    blocked: this.userValue[4].value
                }
                this.updateUserByAdminAction(user).then(value => {
                    setTimeout(() => {
                        this.loading = false
                    }, 1000)
                })
            },
            getAge(date) {
                return getMyAge(date)
            },
            async getUserById(id) {
                const form = {
                    type: 'number',
                    model: id
                }
                const result = await userApi.getUserForAdmin(form)
                this.user = await result.json()
                this.getUser(this.user)
            },
            async getUsersList() {
                if (this.modelSelected.type === 'Long') {
                    const result = await userApi.getAllAdminsId()
                    this.idAdmins = await result.json()
                } else if (this.validForm) {
                    const form = {
                        type: this.modelSelected.type,
                        model: this.userField
                    }
                    const result = await userApi.getUserForAdmin(form)
                    if (result.body === '') {
                        this.user = null
                    } else if (result.ok) {
                        this.user = await result.json()
                        this.getUser(this.user)
                    }
                }
            },
            getUser(user) {
                this.userValue = [
                    {name: 'ID пользователя:', value: user.id, router: true, to: user.id},
                    {name: 'Имя:', value: user.username},
                    {name: 'Возраст:', value: this.getAge(user.birthDate)},
                    {name: 'Active:', value: user.active},
                    {name: 'Blocked:', value: user.blocked, switch: true},
                    {name: 'Дата регистрации:', value: user.dateOfRegistration},
                    {name: 'Последний визит:', value: user.lastVisit},
                    {name: 'Почта:', value: user.email},
                    {name: 'Пол:', value: user.gender},
                    {name: 'Online:', value: user.isOnline},
                    {name: 'Предупреждений:', value: user.warning, color: user.warning > 1 ? 'red' : ''},
                    {name: 'Roles:', value: user.roles, check: true},
                ]
            },
        }
    }
</script>

<style scoped>
    td {
        padding-top: 0;
        padding-bottom: 0;
    }
</style>