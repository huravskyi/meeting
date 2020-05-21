<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-app-bar app
                   :height="!userProfile? defineExtended : ''"
        >
            <v-layout align-center justify-space-around row fill-height>
                <v-toolbar-title class="font-weight-bold font-italic">Dating World&ensp;</v-toolbar-title>

                <div class="text-center" v-model="tooltip">
                    <v-snackbar
                            v-model="show"
                            content-class="tooltip-v"
                            bottom="bottom"
                    >
                        <span>{{tip}}</span>
                    </v-snackbar>
                </div>

                <v-toolbar-items v-if="userProfile && !isMobile">
                    <v-tabs background-color="#f5f5f5"
                            :hide-slider="!activeTab"
                    >
                        <v-tab v-for="tab of getTab"
                               :key="tab.id"
                               :to="tab.to"
                        >

                            <div v-if="!tab.delivered && !tab.like">{{tab.name}}</div>
                            <div v-if="tab.delivered">
                                <v-badge v-if="getDeliveredMessages()"
                                         :content="showNumberNewMessage"
                                         :value="showNumberNewMessage"
                                         color="pink"
                                         offset-x="-1"
                                         offset-y="5"
                                         overlap>
                                    {{tab.name}}
                                </v-badge>
                            </div>
                            <div v-if="tab.like">
                                <v-badge v-if="getNewLike()"
                                         icon="mdi-bell"
                                         overlap
                                         :value="badgeValue"
                                         color="pink"
                                >
                                    {{tab.name}}
                                </v-badge>
                            </div>
                        </v-tab>
                    </v-tabs>
                </v-toolbar-items>
                <div class="d-flex">
                    <template v-if="userProfile">
                        <div>
                            <v-menu
                                    left
                                    transition="scroll-y-transition"
                                    v-model="value"
                                    close-on-click
                                    offset-y
                                    close-delay="800"
                                    radius

                            >
                                <template v-slot:activator="{ on }">
                                    <v-avatar v-on="on" color="indigo">
                                        <v-img v-if="userProfile.userpic"
                                               :src="userProfile.userpic"
                                        ></v-img>
                                        <v-icon v-else dark>account_circle</v-icon>
                                    </v-avatar>
                                    <v-icon v-if="!isMobile" class="rel" v-on="on">keyboard_arrow_down</v-icon>
                                </template>
                                <v-list v-if="!isMobile" class="right_menu">
                                    <v-list-item
                                            v-for="(item, index) in itemsMenu"
                                            :key="index"
                                            :to="item.to"
                                            @click="selectItemMenu(item)"
                                    >
                                        <v-list-item-title>
                                            {{item.title}}
                                        </v-list-item-title>
                                    </v-list-item>
                                </v-list>
                            </v-menu>
                        </div>

                    </template>
                    <div v-if="userProfile === null"
                         class="d-flex align-center"
                         style="min-width: 150px">

                        <div v-if="getPath">
                            <router-link class="prompt" to="/login">
                                <v-icon color="blue"> mdi-exit-to-app</v-icon>
                                Войти
                            </router-link>
                        </div>
                        <div v-else>
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
    import {mapState, mapActions} from 'vuex'

    export default {
        props: ['isMobile'],
        name: "Navigation",
        data: () => ({
            logout: 'Войти',
            value: '',
            numberNewMessage: 0,
            show: false,
            showNumberNewMessage: 0,
            badgeValue: true,
            tip: '',
            menu: '',
            img: null,
            del: 3,
            itemsMenu: [
                {title: 'Профиль', to: '/profile/my'},
                {title: 'Настройки', to: '/settings'},
                {title: 'Выход', to: '/logout'},
            ],
            activeTab: true,
            tabs: [
                {id: 1, name: 'Поиск', delivered: false, like: false, to: '/search'},
                {id: 2, name: 'Моя почта', delivered: true, like: false, to: '/message'},
                {id: 3, name: 'Симпатии', delivered: false, like: true, to: '/sympathy'},
                {id: 4, name: 'Просмотры', delivered: false, like: false, to: '/views'},
            ],
        }),
        methods: {
            ...mapActions(['updateChatsAction', 'getBlockChatAction']),

            getNewLike() {
                this.userProfile.likeNew === 1 ? this.badgeValue = true : this.badgeValue = false
                return true
            },
            getDeliveredMessages() {
                let count = 0
                this.chats.forEach(item => {
                    count += item.numberOfNewMessage
                })
                if (count === 0 || isNaN(count)) {
                    this.showNumberNewMessage = 0
                    return true
                } else {
                    this.showNumberNewMessage = count
                    return true
                }
            },
            selectItemMenu(item) {
                if (item.to === '/logout') {
                    location.reload()
                }
            },
        },

        watch: {
            '$route'(val) {
                if (this.$route.path.includes('message')) {
                    this.updateChatsAction()
                    this.getBlockChatAction()
                }
                this.activeTab = !(this.$route.path.includes('settings')
                    || this.$route.path.includes('profile'));
            }
        },
        computed: {
            ...mapState({
                chats: state => state.storeMessages.chats,
                userProfile: state => state.storeUserProfile.userProfile,
                images: state => state.storeImage.images,
            }),
            getTab() {
                if (userProfile.roles.includes('ADMIN')) {
                    return this.tabs = [
                        ...this.tabs,
                        {id: 5, name: 'Admin Panel', delivered: false, like: false, to: '/admin'},
                    ]
                } else {
                    return this.tabs
                }
            },
            async tooltip() {
                switch (window.location.search) {
                    case '?isActivation':
                        this.tip = 'Вы подтвердили свой аккаунт'
                        this.show = true
                        break
                    case '?isActivationerr':
                        this.tip = 'Ошибка подтверждения'
                        this.show = true
                        break
                    case '?isActivationEmail':
                        this.tip = 'Вы успешно обнавили почту'
                        this.show = true
                        break
                    case '?isActivationEmailerr':
                        this.tip = 'Что то пошло не так'
                        this.show = true
                        break
                }
                return this.show

            },
            defineExtended() {
                if (window.innerWidth < 350) return 90

            },
            getPath() {
                return this.$route.path !== '/login';
            },
        },
    }
</script>

<style scoped>
    .v-list-item--link:before {
        background-color: #0a70ff;
    }

    .v-menu__content {
        border-radius: 10px;
    }

    .new-message {
        display: flex;
        justify-content: center;
        width: 20px;
        height: 20px;
        background-color: #ff1d6c;
        border-radius: 2px;
        color: white;
        padding: 2px 2px 2px 2px;
        margin-left: 5px;
    }

    .font-italic {
        font-size: x-large;
        color: #0a70ff;
    }

    .prompt {
        text-decoration: none;
        font-size: large;
    }

    .tooltip-v {
        width: 300px;
        align-items: center;
        justify-content: center;
        display: flex;
        margin-top: 50px;

    }


</style>