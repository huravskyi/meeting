<template>
    <div>
        <div v-if="isMobile">
            <v-container class="pt-0">
                <message-mobile v-if="selected === undefined"
                                :chats="chats"
                                :userProfile="userProfile"
                                :chatsBlock="chatsBlock"
                                :tabTitle="tabTitle"
                                :isMobile="isMobile"
                                v-on:set-tab="tab = $event"
                                v-on:set-selected="selected = $event"
                                :tab="tab"
                >
                </message-mobile>
                <div v-else>
                    <menu-message-list
                            v-on:set-selected="selected = $event"
                            :userProfile="userProfile"
                            :tab="tab"
                            :selected="selected"
                            :chats="chats"
                            :chatsBlock="chatsBlock"
                    >
                    </menu-message-list>
                    <div>
                        <button-menu-message :userProfile="userProfile"
                                             :tab="tab"
                                             :selected="selected"
                                             :chats="chats"
                                             :chatsBlock="chatsBlock"
                                             v-on:set-selected="selected = $event"
                                             v-if="selected !== undefined">
                        </button-menu-message>
                    </div>

                    <div v-if="tab !== 1" class="flex-grow-1 flex-shrink-1 flex-nowrap">
                        <div class="d-flex justify-center ma-1" v-if="selected !== undefined ">

                            <v-tooltip bottom v-if="checkTotalPage()">
                                <template v-slot:activator="{ on }">
                                    <v-btn icon
                                           v-on="on"
                                           @click="downloadOldMessage()">
                                        <v-icon size="50"> mdi-download</v-icon>
                                    </v-btn>
                                </template>
                                <span>Загрузить больше сообщений</span>
                            </v-tooltip>

                        </div>
                        <list-messages v-if="selected !== undefined"
                                       :chatBlock="chatsBlock"
                                       :chats="chats"
                                       :selected="selected"
                                       :userProfile="userProfile"
                                       :tab="tab"
                                       :isLoading="isLoading"
                                       :isMobile="isMobile"
                        ></list-messages>
                    </div>
                    <past-smile-and-send-message :displayColBlock="'none'"
                                                 :cols="'2'"
                                                 :class-col="'pa-0'"
                                                 :sendMessage="sendMessage"
                    ></past-smile-and-send-message>

                </div>
            </v-container>
        </div>


        <v-container v-else>
            <v-col>
                <v-row>
                    <v-card width="100%"
                    >
                        <v-tabs style="position: relative"
                                v-model="tab"
                                icons-and-text
                                background-color="#0a70ff"
                                dark
                                height="56"
                        >
                            <v-tab style="min-width: 180px;"
                                   v-for="(item, i) in tabTitle"
                                   :key="i"
                                   @click="setSelected(item)"
                            >
                                {{item.title}}
                                <v-icon>{{item.icon}}</v-icon>
                            </v-tab>

                            <v-divider vertical></v-divider>
                            <button-menu-message :userProfile="userProfile"
                                                 :tab="tab"
                                                 :selected="selected"
                                                 :chats="chats"
                                                 :chatsBlock="chatsBlock"
                                                 v-on:set-selected="selected = $event"
                                                 v-if="selected !== undefined">
                            </button-menu-message>
                        </v-tabs>
                        <v-divider></v-divider>
                        <div class="d-flex flex-nowrap">
                            <div>
                                <v-tabs-items v-model="tab">
                                    <v-tab-item
                                            v-for="(list, i) in tabTitle"
                                            :key="i+'list-users'"
                                    >
                                        <v-col style="height: 50px; padding: 0 0 0 7px;  ">
                                            <v-text-field
                                                    v-model="userName"
                                                    label="Введите имя"
                                                    prepend-icon="mdi-account-search-outline "
                                                    clearable
                                            ></v-text-field>
                                        </v-col>
                                        <v-container
                                                style="min-height: 280px; "
                                                class="overflow-y-auto ma-0"
                                        >
                                            <v-row style="height: 330px; width: 290px"
                                            >
                                                <v-list three-line>
                                                    <v-list-item-group
                                                            v-model="selected"
                                                            active-class="blue--text"
                                                    >
                                                        <list-users :list="list"
                                                                    :chats="chats"
                                                                    :tab="tab"
                                                                    :userName="userName"
                                                                    :chatsBlock="chatsBlock"
                                                                    style="width:280px">
                                                        </list-users>

                                                    </v-list-item-group>
                                                </v-list>
                                            </v-row>
                                        </v-container>
                                    </v-tab-item>
                                </v-tabs-items>
                            </div>
                            <div v-if="tab !== 1" class="flex-grow-1 flex-shrink-1 flex-nowrap pr-9">
                                <div class="d-flex justify-center ma-1" v-if="selected !== undefined ">

                                    <v-tooltip bottom v-if="checkTotalPage()">
                                        <template v-slot:activator="{ on }">
                                            <v-btn icon
                                                   v-on="on"
                                                   @click="downloadOldMessage()">
                                                <v-icon size="50"> mdi-download</v-icon>
                                            </v-btn>
                                        </template>
                                        <span>Загрузить больше сообщений</span>
                                    </v-tooltip>

                                </div>

                                <list-messages v-if="selected !== undefined"
                                               :chatBlock="chatsBlock"
                                               :chats="chats"
                                               :selected="selected"
                                               :userProfile="userProfile"
                                               :tab="tab"
                                               :isLoading="isLoading"
                                               :isMobile="isMobile"
                                ></list-messages>

                            </div>
                        </div>
                        <v-divider></v-divider>
                        <v-divider></v-divider>

                        <past-smile-and-send-message v-if="selected !== undefined && tab === 0"
                                                     :sendMessage="sendMessage"
                                                     :sm="'2'"
                                                     :md="'1'"
                        >
                        </past-smile-and-send-message>

                        <v-divider></v-divider>
                    </v-card>
                </v-row>
            </v-col>
        </v-container>
    </div>
</template>

<script>
    import Vue from 'vue'
    import {mapActions, mapState, mapGetters} from "vuex";
    import ListUsers from "../components/messages/ListUsers.vue";
    import ListMessages from "../components/messages/ListMessages.vue";
    import ButtonMenuMessage from "../components/messages/ButtonMenuMessage.vue";
    import MessageMobile from "./MessageMobile.vue";
    import MenuMessageList from "../components/messages/MenuMessageList.vue";
    import PastSmileAndSendMessage from "../components/messages/PastSmileAndSendMessage.vue";

    export default {
        name: "Message",
        components: {
            PastSmileAndSendMessage, MenuMessageList,
            MessageMobile, ButtonMenuMessage,
            ListMessages, ListUsers
        },
        data() {
            return {
                userName: null,
                isMobile: false,
                selected: undefined,
                tab: null,
                isLoading: false,
                tabTitle: [
                    {
                        title: ' Все переписки',
                        icon: 'mdi-email-open-multiple-outline',
                        content: []
                    },
                    {
                        title: 'Заблокированные',
                        icon: 'mdi-block-helper',
                        content: []
                    },
                ],
                tex: [],
            }
        },
        computed: {
            ...mapState({
                userProfile: state => state.storeUserProfile.userProfile,
                chats: state => state.storeMessages.chats,
                chatsBlock: state => state.storeMessages.chatsBlock,
            }),
        },
        watch: {
            chats(val) {
                Vue.set(this.tabTitle[0], 'content', this.chats)
                Vue.set(this.tabTitle[1], 'content', this.chatsBlock)
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
            this.getMessages()
        },
        methods: {
            ...mapActions(['saveMessageActions',
                'saveNewChatAndMessageActions',
                'downloadOldMessageFromDbAction',
                'getNewListMessageAction',
            ]),
            onResize() {
                this.isMobile = window.innerWidth < 766
            },
            checkTotalPage() {
                if (this.chats[this.selected].messages !== undefined) {
                    if (this.chats[this.selected].currentPage === undefined) return false
                    return this.chats[this.selected].totalPage === 0 ? false :
                        this.chats[this.selected].totalPage - 1 !== this.chats[this.selected].currentPage
                }
            },
            setSelected(item) {
                if (item.title === 'Заблокированные') {
                    if (this.$route.query.selected) this.$router.push('message')
                }
                this.selected = undefined
            },
            downloadOldMessage() {
                if (this.chats[this.selected].id !== undefined) {
                    const chat = this.chats[this.selected]
                    if (chat.currentPage + 1 < chat.totalPage) {
                        const obj = {
                            chat: chat,
                            page: chat.currentPage + 1
                        }
                        this.isLoading = true
                        this.downloadOldMessageFromDbAction(obj).then(result => {
                            if (result) setTimeout(() => {
                                this.isLoading = false
                            }, 1000)
                        })
                    }
                }
            },
            sendMessage(textarea) {
                if (textarea !== '') {
                    const content = textarea
                    const chat = {...this.chats[this.selected]}
                    const message = {
                        content: {content: content},
                        chat: {id: chat.id}
                    }

                    if (message.chat.id === undefined) {
                        const chatNew = {
                            members: this.getMembersId(chat),
                            messages: [message]
                        }
                        this.saveNewChatAndMessageActions(chatNew)
                    } else {
                        this.saveMessageActions(message)
                    }
                    if (chat.numberOfNewMessage > 5) {
                        this.getNewListMessageAction(chat.id)
                    }
                    return true
                }
            },

            getMembersId(chat) {
                const membersId = []
                chat.members.forEach((item, index) => {
                    if (item.id !== this.userProfile.id) membersId[index] = {id: item.id}
                })
                return membersId
            },
            getMessages() {
                Vue.set(this.tabTitle[0], 'content', this.chats)
                Vue.set(this.tabTitle[1], 'content', this.chatsBlock)
                if (this.$route.query.selected) {
                    this.selected = Number(this.$route.query.selected)
                }
            },
        }
    }
</script>

<style scoped>
</style>