<template>
    <div>
        <div v-if="isMobile">
            <v-container class="pt-0">
                <message-mobile
                        v-if="selected === undefined"
                        :chats="chats"
                        :userProfile="userProfile"
                        :chatsBlock="chatsBlock"
                        :tabTitle="tabTitle"
                        :isMobile="isMobile"
                        v-on:set-tab="tab = $event"
                        v-on:set-selected="selected = $event"
                        :tab="tab"
                        :accountPreview="accountPreview"
                        :accountPreviewMin="accountPreviewMin"
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
                            :accountPreview="accountPreview"
                            :accountPreviewMin="accountPreviewMin"
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
                        <div v-if="selected !== undefined" class="d-flex justify-center ma-1">
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
                                       :heightForScroll="heightForScroll"
                        ></list-messages>
                    </div>
                    <div style="position: fixed; bottom: 0; width: calc(100% - 20px);">
                        <entry-field :displayColBlock="'none'"
                                     :cols="'2'"
                                     :class-col="'pa-0'"
                                     :sendMessage="sendMessage"
                        ></entry-field>
                    </div>
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
                                                style="min-height: 50px; "
                                                class="overflow-y-auto ma-0"
                                        >
                                            <v-row class="list-users"
                                                   :style="'height:'+heightForScroll+'px'"
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
                                                                    :accountPreview="accountPreview"
                                                                    :accountPreviewMin="accountPreviewMin"
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
                                               :heightForScroll="heightForScroll"
                                ></list-messages>

                            </div>
                        </div>
                        <v-divider></v-divider>
                        <v-divider></v-divider>

                        <entry-field v-if="selected !== undefined && tab === 0"
                                     :sendMessage="sendMessage"
                                     :sm="'2'"
                                     :md="'1'"
                        >
                        </entry-field>

                        <v-divider></v-divider>
                    </v-card>
                </v-row>
            </v-col>
        </v-container>
    </div>
</template>

<script>
    import Vue from 'vue'
    import {mapActions, mapMutations, mapState} from "vuex";
    import ListUsers from "../components/messages/ListUsers.vue";
    import ListMessages from "../components/messages/ListMessages.vue";
    import ButtonMenuMessage from "../components/messages/ButtonMenuMessage.vue";
    import MessageMobile from "./MessageMobile.vue";
    import MenuMessageList from "../components/messages/MenuMessageList.vue";
    import EntryField from "../components/messages/EntryField.vue";

    export default {
        name: "Message",
        components: {
            EntryField, MenuMessageList,
            MessageMobile, ButtonMenuMessage,
            ListMessages, ListUsers
        },
        data() {
            return {
                heightForScroll: '100px',
                userName: null,
                selected: undefined,
                tab: 0,
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
                accountPreview: state => state.storeUserProfile.accountPreview,
                accountPreviewMin: state => state.storeUserProfile.accountPreviewMin,
                chats: state => state.storeMessages.chats,
                idLastMessage: state => state.storeMessages.idLastMessage,
                chatsBlock: state => state.storeMessages.chatsBlock,
                isMobile: state => state.storeUserProfile.isMobile,
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
                window.removeEventListener('resize', this.reportWindowSize)
            }
        },
        mounted() {
            this.getMessages()
            window.addEventListener('resize', this.reportWindowSize);
            this.reportWindowSize()
        },
        methods: {
            ...mapMutations(['setIdLastMessageMutation']),
            ...mapActions(['saveMessageActions',
                'saveNewChatAndMessageActions',
                'downloadOldMessageFromDbAction',
                'getNewListMessageAction',
            ]),
            reportWindowSize() {
                setTimeout(()=>{
                    !this.isMobile ?
                        this.heightForScroll = window.innerHeight - 400 : this.heightForScroll = window.innerHeight - 225
                },500)
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
                    const messages = this.chats[this.selected].messages

                    let idMessage = this.getIdLastMessage(chat.id)

                    if (chat.currentPage + 1 < chat.totalPage) {
                        let page
                        if (chat.page) {
                            chat.page = false
                            page = 0
                        } else {
                            page = chat.currentPage+1
                        }
                        const obj = {
                            chat: chat,
                            page: page,
                            idMessage: idMessage - 1
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
            getIdLastMessage(key) {
                let id = this.idLastMessage.get(key)
                if (id !== undefined) {
                    return id
                } else {
                    id = this.chats[this.selected].messages[0].id
                    const map = {
                        key: key,
                        value: id
                    }
                    this.setIdLastMessageMutation(map)
                    return id
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
                        const map={
                            key: this.chats[this.selected].id,
                            value: undefined
                        }
                        this.setIdLastMessageMutation(map)

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
    .list-users {

    }
</style>