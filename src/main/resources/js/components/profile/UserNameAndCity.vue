<template>
    <div v-if="!myProfile">
        <div v-if="profileUserGuest">
            <v-toolbar
                    flat
                    color="#f2f3f5"
                    class="editButton"
            >
                <v-toolbar-title class="font-weight-light">
                    <div class="d-flex pt-2 flex-column align-center flex-wrap">
                        <div class="d-flex headline">
                            <v-badge class="ma-auto mr-10"
                                    :color="`${profileUserGuest.online? '#21ff0b': '#ffac32'}`"
                                    dot
                                    offset-x="-3"
                                    overlap>
                                <div>
                                    {{profileUserGuest.username + ', ' + getAge(profileUserGuest.birthDate)}}
                                </div>
                            </v-badge>
                            <liked-button v-if="!isMobile"
                                          :class-button="''"
                                          :outlined="true"
                                          :user="profileUserGuest"
                                          :icon="false"
                                          :minWidth="0"
                                          :text="true"
                                          :colorIcon="'blue'"
                                          :isMobile="isMobile"
                            ></liked-button>

                            <div v-if="isMobile" class="d-flex justify-space-between align-center">
                                <span class="subtitle-2 pl-3 align-center">был(а){{getLastTime(profileUserGuest)}}</span>
                            </div>

                            <div v-if="!isMobile"
                                 class="subtitle-2 pl-2 d-flex justify-space-between align-center">
                                <span>был(а){{getLastTime(profileUserGuest)}}</span>
                            </div>
                        </div>
                        <div class="subtitle-2" style="width: 100%">{{getCityCountryRegion(profileUserGuest)}}</div>
                    </div>
                </v-toolbar-title>
            </v-toolbar>

        </div>
    </div>


    <div v-else style="min-width: 300px; max-width: 400px;" :class="isMobile?'ma-auto':''">
        <v-toolbar
                flat
                color="#f2f3f5"
                class="editButton"
        >
            <div class="d-flex flex-column">
                <div class="d-flex">
                    <v-toolbar-title class="font-weight-light">
                        {{userProfile.username}}, {{getAge(userProfile.birthDate)}}
                    </v-toolbar-title>
                    <v-btn
                            color="#2196f3"
                            text
                            icon
                            small
                            @click="editing()"
                            class="ml-2"
                    >
                        <v-icon v-if="isEditing">mdi-close</v-icon>
                        <v-icon v-else>mdi-pencil</v-icon>
                    </v-btn>
                </div>
                <div style="width: 100%">{{getCityCountryRegion(userProfile)}}</div>
            </div>
        </v-toolbar>
        <v-expansion-panels
                v-model="panel"
                multiple
                disabled
        >
            <v-expansion-panel style="background-color: #0a70ff">
                <v-expansion-panel-content>
                    <v-card class="pa-0"
                            dark
                            color="#0a70ff"
                            tile flat
                    >
                        <v-card-text class="pa-0 pt-3">
                            <v-form ref="header"
                                    v-model="form">
                                <v-text-field
                                        v-model="username"
                                        :disabled="!isEditing"
                                        :rules="nameRules"
                                        label="Имя"
                                ></v-text-field>
                                <choose-age :age="userProfile.birthDate"></choose-age>
                            </v-form>
                            <form autocomplete="off">
                                <autocomplete-city :locale-search="getMyCityCountryRegion" ref="formCity">
                                </autocomplete-city>
                            </form>
                        </v-card-text>
                    </v-card>
                    <v-divider></v-divider>
                    <v-card-actions>
                        <div class="flex-grow-1"></div>
                        <v-btn
                                :disabled="!form"
                                color="success"
                                @click="save()"
                        >
                            Сохранить
                        </v-btn>
                    </v-card-actions>
                </v-expansion-panel-content>
            </v-expansion-panel>
        </v-expansion-panels>

        <v-snackbar v-model="modelSnackbar"
                    :timeout="timeout"
                    top
                    absolute
        >
            {{ textSnackbar }}
            <v-btn color="blue"
                   text
                   @click="modelSnackbar = false"
            >
                Close
            </v-btn>
        </v-snackbar>
    </div>
</template>

<script>
    import {mapActions, mapState} from 'vuex'
    import getAge from '../../util/helper/getAge'
    import getLastVisit from "../../util/helper/getLastVisit";
    import LikedButton from "./LikedButton.vue";
    import ChooseAge from "./ChooseAge.vue";
    import AutocompleteCity from "./AutocompleteCity.vue";

    const getFunctionLodash = () => import("../../util/helper/functionLodash");
    export default {
        components: {AutocompleteCity, ChooseAge, LikedButton},
        props: ['isMobile', 'myProfile'],
        name: "UserNameAndCity",
        data: () => ({
            timeout: 2000,
            textSnackbar: '',
            modelSnackbar: false,
            form: false,
            id: null,
            username: null,
            date: null,
            isEditing: false,
            model: null,
            entries: [],
            panel: [],
            nameRules: [
                v => !!v || 'имя обязательно',
                v => (v && v.length > 2 && v.length < 18) || 'Имя должно быть действительным не' +
                    'менее 3 символов и не более 20',
            ],
        }),

        mounted() {
            if (this.userProfile !== null) {
                this.username = this.userProfile.username
                this.date = this.userProfile.birthDate

            }
        },
        computed: {
            ...mapState({
                profileUserGuest: state => state.storeUserGuest.userGuest,
                userProfile: state => state.storeUserProfile.userProfile,
            }),
            getMyCityCountryRegion() {
                return userProfile.city ? userProfile.city : userProfile.region ? userProfile.region : userProfile.country
            }

        },
        methods: {
            ...mapActions(['updateNameAndCityActions']),
            getCityCountryRegion(user) {
                if (user.city) {
                    return user.city + ', ' + user.country
                } else if (user.region) {
                    return user.region + ', ' + user.country
                } else {
                    return user.country
                }
            },
            getAge(date) {
                if (date !== null)
                    return getAge(date)
                return 100
            },
            getLastTime(guest) {
                return getLastVisit(guest.lastVisit)
            },
            save() {
                this.entries = this.$refs.formCity.entries
                let locale = this.$refs.formCity.locale
                this.isEditing = !this.isEditing
                this.panel.splice(0, 1)
                if (locale === undefined) {
                    this.textSnackbar = 'Город должен быть указан'
                    this.modelSnackbar = true
                } else {
                    if (typeof locale === 'string') {
                        const locate = {
                            city: this.userProfile.city,
                            region: this.userProfile.region,
                            country: this.userProfile.country,
                        }
                        this.userUpdate(locate)
                    } else {
                        getFunctionLodash().then(res => {
                            this.userUpdate(res.getLocateUser(locale, this.entries))
                        })
                    }

                }
            },
            userUpdate(locate) {
                const user = {
                    id: this.userProfile.id,
                    username: this.username,
                    birthDate: this.$refs.header.inputs[1].value,
                    city: locate.city,
                    region: locate.region,
                    country: locate.country
                }

                const result = this.updateNameAndCityActions(user)
                result ? this.textSnackbar = 'Ваш профель обновлен' : this.textSnackbar = 'Что-то пошло не так! Попробуйте позже'
                this.modelSnackbar = true
            },
            editing() {
                if (this.panel.length === 0) {
                    this.isEditing = true
                    this.panel.push(0)
                } else {
                    this.panel.splice(0, 1)
                    this.isEditing = false
                }
            },
        }
    }
</script>

<style scoped>
    .editButton {
        width: fit-content;
    }
</style>