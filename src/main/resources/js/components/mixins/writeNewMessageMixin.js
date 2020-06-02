import {mapMutations, mapState} from "vuex";

export const writeNewMessage = {
    computed: {
        ...mapState({
            chats: state => state.storeMessages.chats,
            isMobile: state => state.storeUserProfile.isMobile,
        }),
        getPath(){
            return this.isMobile? { path: '/message/0', query: { tab: '0', selected:'0' }}:'/message?selected=0'
        }
    },
    methods: {
        ...mapMutations(['writeNewMessageAndNewChatMutation', 'setToUpChatMutation', 'mobileNavigationMutation']),
        checkMessage(userTo) {
            if (this.chats) {
                for (let i = 0; i < this.chats.length; i++) {
                    for (let y = 0; y < this.chats[i].members.length; y++) {
                        if (this.chats[i].members[y].id === userTo.id) {
                            return i
                            break
                        }
                    }
                }
                return undefined
            } else {
                return undefined
            }
        },
        writeNewMessage(userTo) {
            let index = this.checkMessage(userTo)
            const userFrom = this.userProfile
            if (index === undefined) {
                const chat = {
                    members: [userTo, userFrom],
                    messages: [],
                    numberOfNewMessage: 0
                }
                this.writeNewMessageAndNewChatMutation(chat)
            }else {
                this.setToUpChatMutation(index)
            }
            if (this.isMobile){
                this.mobileNavigationMutation(false)
            }
        },
    }
}