<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-app-bar app
                   :height="!userProfile? defineExtended : ''"
        >
            <v-layout align-center justify-space-around row fill-height>
                <v-toolbar-title class="font-weight-bold font-italic">
                    <router-link to="/" title="На главную" style="text-decoration: none">Dating World&ensp;
                    </router-link>
                </v-toolbar-title>

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
                                         icon="notifications"
                                         :value="badgeValue"
                                         color="pink"
                                         class="badgeLike"
                                         dark
                                >
                                    {{tab.name}}
                                </v-badge>
                            </div>
                        </v-tab>
                    </v-tabs>
                </v-toolbar-items>
                <div class="d-flex">
                    <div v-if="userProfile">
                        <div v-if="isMobile">
                            <router-link style="text-decoration: none" to="/profile/my">
                                <v-avatar color="indigo">
                                    <v-img v-if="userProfile.userpic"
                                           :src="userProfile.userpic"
                                    ></v-img>
                                    <v-icon v-else dark>account_circle</v-icon>
                                </v-avatar>
                            </router-link>
                        </div>
                        <div v-else>
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
                                    <v-icon class="rel" v-on="on">keyboard_arrow_down</v-icon>
                                </template>
                                <v-list class="right_menu">
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
                    </div>
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
                    window.location.reload(true)
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

<style>


    .v-badge__wrapper .v-badge__badge .v-icon {
        font-size: 12px !important;
    }

    .v-list-item--link:before {
        background-color: #0a70ff;
    }

    .v-menu__content {
        border-radius: 10px;
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