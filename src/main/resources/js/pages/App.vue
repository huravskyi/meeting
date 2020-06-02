<template>
    <v-app app>
        <navigation v-if="mobileNavigation" :isMobile="isMobile"></navigation>

        <v-content>
            <router-view
                    v-if="userProfile === null || userProfile.active && !userProfile.blocked"></router-view>

            <v-alert v-else-if="userProfile.blocked"
                     type="warning">
                <p> Ваша анкета заблокирована или удалена</p>
            </v-alert>

            <v-alert v-else
                     type="warning">
                <p>Вы должны подтвердить почту</p>
                <p> Письмо на почту не пришло ?
                    <v-btn :disabled="disabledSendMail"
                           @click="sendToMail()">отправить
                    </v-btn>
                    еще раз
                    <span>{{time}}</span>
                </p>
            </v-alert>
        </v-content>
        <Footer v-if="!isMobile" :isMobile="isMobile"></Footer>
        <mobile-navigation v-if="isMobile && userProfile !== null && mobileNavigation"
                           :chats="chats"
                           :userProfile="userProfile"
        ></mobile-navigation>
    </v-app>
</template>

<script>
    import Navigation from "./Navigation.vue";
    import MobileNavigation from "./MobileNavigation.vue";
    import Footer from "../footer/Footer.vue";
    import {addHandler} from "../util/ws";

    import {mapMutations, mapState, mapActions} from 'vuex'

    export default {
        name: "App",
        components: {Footer, MobileNavigation, Navigation},
        data: () => ({
            isMobile: false,
            disabledSendMail: false,
            time: '',
        }),
        watch: {
            '$route'(val) {
                if (this.isMobile)
                    if (val.query.tab === undefined) {
                        this.mobileNavigationMutation(true)
                    }else {
                        this.mobileNavigationMutation(false)
                    }
            }
        },
        beforeDestroy() {
            if (typeof window !== 'undefined') {
                window.removeEventListener('resize', this.onResize, {passive: true})
            }
        },

        mounted() {
            this.onResize()
            window.addEventListener('resize', this.onResize, {passive: true})

        },
        computed: {
            ...mapState({
                userProfile: state => state.storeUserProfile.userProfile,
                chats: state => state.storeMessages.chats,
                mobileNavigation: state => state.storeMessages.mobileNavigation,
            }),
        },

        methods: {
            ...mapMutations([
                'mobileNavigationMutation',
                'setProfileLikeNewMutation',
                'addMessageSocketMutation',
                'updateDeliveredAllMessageMutation',
                'updateDeliveredOneMessageMutation',
                'updateViewedMessageMutation',
                'createChatAndMessageSocketMutation',
                'createChatDtoMutation',
                'setIsMobileMutation'
            ]),
            ...mapActions(['deliveredMessageAction',
                'deliveredChatMessageAction', 'sendToEmailAction']),
            timer() {
                this.time--
                if (this.time <= 0) {
                    this.disabledSendMail = false
                    this.time = ''
                    setTimeout(function () {
                    }, 1000);
                } else {
                    setTimeout(this.timer, 1000);
                }
            },
            sendToMail() {
                if (!this.disabledSendMail) {
                    this.disabledSendMail = true
                    this.time = 30
                    setTimeout(this.timer, 1000)
                    this.sendToEmailAction() ?
                        alert('Письмо отправлено на почту') : alert('Что то пошло не так попробуйте позже')

                }
            },
            onResize() {
                this.isMobile = window.innerWidth < 766
                this.setIsMobileMutation(this.isMobile)
            },
        },
        created() {
            addHandler(data => {
                if (data.objectType === 'MESSAGE') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addMessageSocketMutation(data.body)
                            this.deliveredMessageAction(data.body)
                            break
                        case 'DELIVEREDAll':
                            this.updateDeliveredAllMessageMutation(data.body)
                            break
                        case 'DELIVERED':
                            this.updateDeliveredOneMessageMutation(data.body)
                            break
                        case 'VIEWED':
                            this.updateViewedMessageMutation(data.body)
                            break
                        case 'REMOVE':
                            break
                        default:
                            console.error(`Looks like the event type if unknown "${data.eventType}"`)
                    }
                } else if (data.objectType === 'CHAT') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.deliveredChatMessageAction(data.body)
                            this.createChatAndMessageSocketMutation(data.body)
                            break
                        case 'DELIVERED':
                            this.updateDeliveredAllMessageMutation(data.body)
                            break
                        default:
                            console.error(`Looks like the event type if unknown "${data.eventType}"`)
                    }

                } else if (data.objectType === 'CHATDTO') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.deliveredChatMessageAction(data.body)
                            this.createChatDtoMutation(data.body)
                            break
                        default:
                            console.error(`Looks like the event type if unknown "${data.eventType}"`)
                    }

                } else if (data.objectType === 'NEWLIKE') {
                    switch (data.eventType) {
                        case 'UPDATE':
                            this.setProfileLikeNewMutation(data.body)
                            break
                        default:
                            console.error(`Looks like the event type if unknown "${data.eventType}"`)
                    }

                } else {
                    console.error(`Looks like the object type if unknown "${data.objectType}"`)

                }
            })
        },
    }
</script>

<style scoped>
</style>