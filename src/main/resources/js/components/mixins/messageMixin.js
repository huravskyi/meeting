import {mapActions, mapMutations, mapState} from "vuex";
import Vue from "vue";

export const messageMethods = {
    data: () => ({
        selected: undefined,
        tab: 0,
        isLoading: false,
        heightForScroll: 100,
        userName: null,
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
    }),
    watch: {
        tab(val) {
            this.setTabMutation(val)
        }
    },
    computed: {
        ...mapState({
            userProfile: state => state.storeUserProfile.userProfile,
            accountPreview: state => state.storeUserProfile.accountPreview,
            accountPreviewMin: state => state.storeUserProfile.accountPreviewMin,
            chats: state => state.storeMessages.chats,
            tabVuex: state => state.storeMessages.tabVuex,
            idLastMessage: state => state.storeMessages.idLastMessage,
            chatsBlock: state => state.storeMessages.chatsBlock,
            isMobile: state => state.storeUserProfile.isMobile,
        }),
        checkTotalPage() {
            if (this.chats[this.selected].messages !== undefined) {
                if (this.chats[this.selected].currentPage === undefined) return false
                return this.chats[this.selected].totalPage === 0 ? false :
                    this.chats[this.selected].totalPage - 1 !== this.chats[this.selected].currentPage
            }
        },
    },
    beforeDestroy() {
        if (typeof window !== 'undefined') {
            window.removeEventListener('resize', this.reportWindowSize)
        }
    },
    mounted() {
        window.addEventListener('resize', this.reportWindowSize);
        this.reportWindowSize()
        this.getMessages()
        this.tab = this.tabVuex
    },
    methods: {
        ...mapMutations(['setIdLastMessageMutation', 'setTabMutation']),
        ...mapActions(['saveMessageActions',
            'saveNewChatAndMessageActions',
            'downloadOldMessageFromDbAction',
            'getNewListMessageAction',
        ]),
        getMessages() {
            Vue.set(this.tabTitle[0], 'content', this.chats)
            Vue.set(this.tabTitle[1], 'content', this.chatsBlock)
            if (this.$route.query.selected) {
                this.selected = Number(this.$route.query.selected)
            }
        },

        downloadOldMessage() {
            if (this.chats[this.selected].id !== undefined) {
                const chat = this.chats[this.selected]

                let idMessage = this.getIdLastMessage(chat.id)

                if (chat.currentPage + 1 < chat.totalPage) {
                    let page
                    if (chat.page) {
                        chat.page = false
                        page = 0
                    } else {
                        page = chat.currentPage + 1
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
                    const map = {
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
    }
}