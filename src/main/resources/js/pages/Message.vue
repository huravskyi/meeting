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
                                :accountPreview="accountPreview"
                                :accountPreviewMin="accountPreviewMin"
                                :heightForScroll="heightForScroll"
                >
                </message-mobile>
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
    import ListUsers from "../components/messages/ListUsers.vue";
    import ListMessages from "../components/messages/ListMessages.vue";
    import ButtonMenuMessage from "../components/messages/ButtonMenuMessage.vue";
    import MessageMobile from "./MessageMobile.vue";
    import MenuMessageList from "../components/messages/MenuMessageList.vue";
    import EntryField from "../components/messages/EntryField.vue";
    import {messageMethods} from "../components/mixins/messageMixin";

    export default {
        name: "Message",
        mixins: [messageMethods],
        components: {
            EntryField, MenuMessageList,
            MessageMobile, ButtonMenuMessage,
            ListMessages, ListUsers
        },
        data() {
            return {

            }
        },
        watch: {
            chats(val) {
                Vue.set(this.tabTitle[0], 'content', this.chats)
                Vue.set(this.tabTitle[1], 'content', this.chatsBlock)
            }
        },
        methods: {
            reportWindowSize() {
                setTimeout(() => {
                    this.heightForScroll = !this.isMobile ?
                        window.innerHeight - 400 : window.innerHeight - 140
                }, 200)

            },
            setSelected(item) {
                if (item.title === 'Заблокированные') {
                    if (this.$route.query.selected) this.$router.push('message')
                }
                this.selected = undefined
            },
        }
    }
</script>
<style scoped>
</style>