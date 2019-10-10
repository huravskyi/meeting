<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-app-bar app
                   :height=defineExtended
        >
            <v-layout align-center justify-space-around row fill-height>
                <v-toolbar-title class="font-weight-bold font-italic">Новые свидания</v-toolbar-title>

                <div style="position: relative" v-model="tooltip">
                    <v-tooltip
                            v-model="show"
                            top
                            absolute

                    >
                        <span>{{frontendData.registration}}</span>
                    </v-tooltip>
                </div>


                <v-toolbar-items v-if="profile">
                    <v-tabs>
                        <v-tab>Поиск</v-tab>
                        <v-tab>Моя почта</v-tab>
                        <v-tab>Знакомства</v-tab>
                        <v-tab>Просмотры</v-tab>
                    </v-tabs>
                </v-toolbar-items>
                <div class="d-flex">
                    <template v-if="profile">
                        <div>
                            <v-menu
                                    v-model="value"
                                    close-on-click
                                    offset-y
                                    open-on-hover
                                    close-delay="600"
                            >
                                <template v-slot:activator="{ on }">
                                    <v-avatar v-on="on" color="indigo">
                                        <v-icon dark>account_circle</v-icon>
                                    </v-avatar>
                                    <v-icon class="rel" v-on="on">keyboard_arrow_down</v-icon>
                                </template>

                                <v-list>
                                    <v-list-item
                                            v-for="(item, index) in itemsMenu"
                                            :key="index"
                                            @click="selectItemMenu(item.title)"
                                    >

                                        <v-list-item-title v-if="item.title != 'Войти' && item.title != 'Выход'  ">
                                            {{item.title}}
                                        </v-list-item-title>
                                        <v-list-item-title v-if="item.title == 'Войти' ">
                                            {{item.title}}
                                        </v-list-item-title>
                                        <v-list-item-title v-if="item.title == 'Выход'">
                                            <form action='/logout' method='post'><input type='submit'
                                                                                        :value="item.title"/></form>
                                        </v-list-item-title>
                                    </v-list-item>
                                </v-list>
                            </v-menu>
                        </div>

                    </template>
                    <div
                            v-if="!profile"
                            class="d-flexalign-center"
                            style="min-width: 150px">
                        <div
                                v-if="!switchCase"
                                @click.stop="switchPath = !switchPath"
                        >
                            <router-link class="prompt" to="/login">
                                <v-icon color="blue"> mdi-exit-to-app</v-icon>
                                Войти
                            </router-link>
                        </div>
                        <div
                                v-if="switchCase"
                                @click.stop="switchPath = !switchPath"
                        >
                            <router-link class="prompt" to="/registration">
                                <v-btn color="primary"> Регистрация</v-btn>
                            </router-link>
                        </div>
                    </div>
                </div>
            </v-layout>
        </v-app-bar>

    </div>
</template>

<script>

    export default {
        name: "Navigation",
        data: () => ({
            frontendData: frontendData,
            logout: 'Войти',
            value: false,
            profile: profile,
            switchPath: '',
            show: true,

        }),
        methods: {
            selectItemMenu(title) {
                if (title === 'Войти')
                    this.$router.push("/login")
            },
        },

        watch: {
            auth(newVal, oldVal) {
            }
        },
        computed: {
            async tooltip() {
                function sleep(ms) {
                    return new Promise(resolve => setTimeout(resolve, ms));
                }

                if (this.frontendData.registration) {
                    console.log(this.show)
                    await sleep(4000)
                    this.show = false

                    return this.show
                }
            },
            defineExtended() {
                if (window.innerWidth < 350) {
                    return 90
                }
            },
            switchCase() {
                if (window.location.pathname == '/login') {
                    this.switchPath = true
                    return this.switchPath
                } else {
                    this.switchPath = false
                    return this.switchPath
                }
            },
            itemsMenu() {
                return [
                    {title: 'Click Me'},
                    {title: 'Click Me'},
                    {title: 'Click Me'},
                    {title: profile == null ? 'Войти' : 'Выход'},
                ]
            }
        },


    }
</script>

<style scoped>
    .font-italic {
        font-size: x-large;
        color: #0a70ff;
    }

    .prompt {
        text-decoration: none;
        font-size: large;
    }

    .ff {
        position: absolute;
    }

</style>