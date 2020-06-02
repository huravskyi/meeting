<template>
    <v-container v-if="selected !== undefined">
        <div>
            <menu-message-list
                    v-on:set-selected="selected = $event"
                    :userProfile="userProfile"
                    :tab="tab"
                    :selected="selected"
                    :chats="setChats()"
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
        </div>
        <div v-if="tab !== 1" class="flex-grow-1 flex-shrink-1 flex-nowrap">
            <div class="d-flex justify-center ma-1">
                <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                        <v-btn icon
                               v-on="on"
                               @click="downloadOldMessage()"
                               :disabled="!checkTotalPage"
                        >
                            <v-icon size="50"> mdi-download</v-icon>
                        </v-btn>
                    </template>
                    <span>Загрузить больше сообщений</span>
                </v-tooltip>
            </div>
            <list-messages
                    :chatBlock="chatsBlock"
                    :chats="chats"
                    :selected="selected"
                    :userProfile="userProfile"
                    :tab="tab"
                    :isLoading="isLoading"
                    :isMobile="isMobile"
                    :heightForScroll="heightForScroll"
            ></list-messages>
            <div style="position: fixed; bottom: 0; width: calc(100% - 20px);">
                <entry-field :displayColBlock="'none'"
                             :cols="'2'"
                             :class-col="'pa-0'"
                             :sendMessage="sendMessage"
                ></entry-field>
            </div>
        </div>
    </v-container>
</template>

<script>
    import MenuMessageList from "../components/messages/MenuMessageList.vue";
    import ButtonMenuMessage from "../components/messages/ButtonMenuMessage.vue";
    import EntryField from "../components/messages/EntryField.vue";
    import ListMessages from "../components/messages/ListMessages.vue";
    import {mapMutations} from "vuex";
    import {messageMethods} from "../components/mixins/messageMixin";

    export default {
        components: {EntryField, ListMessages, ButtonMenuMessage, MenuMessageList},
        name: "ChatViewMessageMobile",
        mixins: [messageMethods],
        data: () => ({
            isLoading: false,
            heightForScroll: 10,
        }),
        mounted() {
            this.reportWindowSize()
            this.setSelected()
            this.mobileNavigationMutation(false)
        },
        methods: {
            ...mapMutations(['mobileNavigationMutation']),
            reportWindowSize() {
                setTimeout(() => {
                    this.heightForScroll = window.innerHeight - 240
                }, 200)

            },
            setSelected() {
                setTimeout(() => {
                    if (!this.isMobile) this.$router.push('/message')
                }, 200)
                this.tab = Number(this.$route.query.tab)
                const id = Number(this.$route.params.id)

                if (this.$route.query.selected === undefined) {
                    if (this.tab === 0) {
                        this.selected = this.chats.findIndex(chat => chat.id === id)
                    } else {
                        this.selected = this.chatsBlock.findIndex(chat => chat.id === id)
                    }
                }
            },
            setChats() {
                return this.tab === 0 ? this.chats : this.chatsBlock
            }
        }
    }
</script>

<style scoped>

</style>