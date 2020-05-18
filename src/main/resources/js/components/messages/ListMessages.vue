<template>
    <div>
        <v-row class="flex-nowrap">

            <v-container id="scrollElement"
                         style="min-height: 200px;  "
                         class="overflow-y-auto ma-0"
            >
                <v-row v-scroll
                       style="height: 350px"
                       id="scroll_container"
                >
                    <v-col class="flex-nowrap">

                        <div v-if="!getMessagesText">
                            <v-col>
                                <v-row align-content="center" justify="center">
                                    <div>Начните общаться прямо сейчас!</div>
                                </v-row>
                            </v-col>
                        </div>
                        <div v-else class="list-group-wrapper">
                            <transition name="fade">
                                <div class="loading" :style="`${isMobile?'left:30%':''}`" v-show="isLoading">
                                    <v-img width="26" height="26" :src="load"/>
                                    ... Loading
                                </div>
                            </transition>

                            <transition-group class="list_mess" name="list" tag="ul">
                                <li v-for="(message, i) in getMessagesText"
                                    class="list-item"
                                    :key="message.id"
                                >
                                    <div class="day my-6" v-if="getYearMonthDay(message.creationDate, i)">
                                        <h5>
                                            {{getYearMonthDay(message.creationDate, i)}}
                                        </h5>
                                    </div>

                                    <div class="d-flex justify-end"
                                         v-if="userProfile.id === message.author.id"
                                    >
                                        <div class="message flex-nowrap "
                                             style="color: white; background-color:#0a70ff">
                                            <div class="message_world">

                                                <smile-message :message="message.content.content"
                                                               :messageMedia="message.content"
                                                ></smile-message>

                                            </div>
                                            <div class="ml-3 time">
                                                {{getTime(message.creationDate)}}
                                            </div>

                                            <v-icon color="white" v-if="message.delivered && !message.viewed">
                                                mdi-check
                                            </v-icon>
                                            <v-icon color="white" v-if="message.viewed" style="opacity: 0.8">
                                                mdi-check-all
                                            </v-icon>
                                        </div>
                                        <router-link :to="/profile/+ `${message.author.id}`">
                                            <v-avatar size="40" class="mx-2">
                                                <v-img v-if="getMainImage(message)"
                                                       :src="getMainImage(message)"></v-img>
                                                <v-icon color="#1976d2" v-else size="40">account_circle</v-icon>
                                            </v-avatar>
                                        </router-link>
                                    </div>
                                    <div class="d-flex justify-start "
                                         v-else>
                                        <router-link :to="/profile/+ `${message.author.id}`">
                                            <v-avatar size="40" class="mx-2">
                                                <v-img v-if="getMainImage(message)"
                                                       :src="getMainImage(message)"></v-img>
                                                <v-icon v-else color="#1976d2" size="40">account_circle</v-icon>
                                            </v-avatar>
                                        </router-link>

                                        <div class="message  flex-nowrap mr-12">
                                            <div class="message_world" :id="getId(message)">

                                                <smile-message :message="message.content.content"
                                                               :messageMedia="message.content">
                                                </smile-message>

                                            </div>
                                            <div class="ml-3 time">{{getTime(message.creationDate)}}</div>
                                        </div>
                                    </div>
                                </li>
                            </transition-group>

                        </div>
                        <div id="bottomList"></div>

                        <v-snackbar
                                v-model="snackbar"
                                color="error"
                                :timeout="timeout"
                                top
                                vertical
                        >
                            Вы не можете писать этому пользователю
                            <v-btn dark
                                   text
                                   @click="snackbar = false"
                            >
                                Close
                            </v-btn>
                        </v-snackbar>


                        <div v-if="selected === undefined"
                             class="d-flex justify-center">
                            <div style="width: 200px">
                                <v-icon style="opacity: 0.7" size="200">mdi-wechat</v-icon>
                                <p class="headline font-italic ma-auto">Выберите диалог</p>
                            </div>
                        </div>
                    </v-col>
                </v-row>
            </v-container>
        </v-row>
    </div>
</template>

<script>
    import {mapActions, mapMutations, mapState} from "vuex";
    import scrollToElement from "../../util/helper/scrollToElement";
    import SmileMessage from "./SmileMessage.vue";

    const load = ("https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/load.gif?alt=media&token=8923efec-c9c1-4234-9ea7-ed1235077fa8")
    export default {
        components: {SmileMessage},
        props: ['selected', 'chats', 'userProfile', 'tab', 'isLoading', 'chatBlock', 'isMobile'],
        name: "ListMessages",
        data: () => ({
            scrollRow: 200,
            load,
            snackbar: false,
            timeout: 2000,
            timeLast: '',
            see: false,
            listId: new Set(),
            count: 0,
            element: null,
        }),
        watch: {
            blockAction() {
                this.setBlockMutation()
                this.snackbar = true
            },
            selected() {
                if (this.tab === 0) {
                    this.listId = new Set()
                    setTimeout(this.windowListener, 1000)
                }
            },
        },
        mounted() {
            this.element = document.getElementById("scrollElement")
            this.element.addEventListener('scroll', this.windowListener)

            if (this.isMobile) this.setHeight()

        },
        destroyed() {
            window.removeEventListener('scroll', this.windowListener);
        },
        computed: {
            ...mapState({
                messageIdList: state => state.storeMessages.messageIdList,
                blockAction: state => state.storeMessages.blockAction,

            }),
            getMessagesText() {
                if (this.tab === 0) {
                    const chat = this.chats[this.selected]
                    if (chat.messages.length !== 0) {
                        return chat.messages
                    } else {
                        this.getMessages(chat)
                        scrollToElement(chat, "bottomList")
                        return false
                    }
                } else if (this.tab === 1) {
                    const chatBlock = this.chatBlock[this.selected]
                    if (chatBlock.messages.length !== 0) {
                        return chatBlock.messages
                    }
                } else {
                    return false
                }
            }
        },
        created() {
            this.deferredCall = _.debounce(this.setMessageViewed, 2500)

        },
        methods: {
            ...mapActions(['setViewedAction', 'getMessageFromDbAction', 'downloadNewMessageFromDbAction']),
            ...mapMutations(['setViewedMutation', 'setBlockMutation']),

            setHeight() {
                let heightScroll = document.getElementById('scroll_container')
                let height = document.querySelector('body')
                if (height !== null && heightScroll !== null) {
                    height = height.clientHeight
                    height = height< 620? height -350: height -335
                    heightScroll.style.height = height + 'px'
                }
            },
            getMessages(chat) {
                if (this.chats[this.selected].id !== undefined) {
                    chat.currentPage = 0
                    const obj = {
                        chat: chat,
                        page: 0
                    }
                    this.getMessageFromDbAction(obj)
                }
            },
            getId(message) {
                if (!message.viewed) {
                    this.listId.add(message.id)
                    return message.id
                }
            },
            getIdElement() {
                let id
                _.forEach([...this.listId], item => {
                    id = item
                    return false
                })
                return id
            },
            deleteIdElement(idKey) {
                let del = false
                del = this.listId.delete(parseInt(idKey))
                if (del) {
                    const obj = {
                        id: idKey,
                        indexChat: this.selected
                    }
                    this.setViewedMutation(obj)

                    this.messageIdList.add(idKey)
                    this.deferredCall()
                    return del
                }
            },
            setMessageViewed() {
                const selected = this.selected
                let messages = []
                this.messageIdList.forEach(item => {
                    messages.push({id: item})
                })
                const chat = {
                    id: this.chats[selected].id,
                    messages: messages,
                    members: this.chats[selected].members
                }
                this.setViewedAction(chat)
            },
            windowListener() {
                let it = this.getIdElement()
                let element = document.getElementById(it)
                this.lazyLoader()
                if (element !== null) {
                    this.Visible(element)
                }
            },
            lazyLoader() {
                if (this.element) {
                    const el = this.element
                    const isBottomOfScreen = el.scrollTop + el.clientHeight + 30 >= el.scrollHeight
                    if (isBottomOfScreen) {
                        const chat = this.chats[this.selected]
                        if (chat.currentPageNotViewed + 1 < chat.totalPageNotViewed) {
                            const obj = {
                                chat: chat,
                                page: chat.currentPageNotViewed + 1
                            }
                            this.downloadNewMessageFromDbAction(obj).then(result => {
                                if (result) setTimeout(() => {
                                    this.isLoading = false
                                }, 1000)
                            })
                        }
                    }
                }
            },
            Visible(target) {
                let targetPosition = {
                        top: window.pageYOffset + target.getBoundingClientRect().top,
                        bottom: window.pageYOffset + target.getBoundingClientRect().bottom
                    },
                    windowPosition = {
                        top: window.pageYOffset,
                        bottom: window.pageYOffset + document.documentElement.clientHeight
                    }
                if (targetPosition.bottom > windowPosition.top &&
                    targetPosition.top + 10 < windowPosition.bottom) {
                    const result = this.deleteIdElement(target.id)
                    if (result) this.windowListener()
                }
            },
            getYearMonthDay(time, index) {
                const chat = this.chats[this.selected]
                let timeNew = time.slice(0, 11)
                let timeOld
                if (chat.messages[index - 1])
                    timeOld = chat.messages[index - 1].creationDate.slice(0, 11)
                if (timeNew !== timeOld) {
                    return timeNew
                } else {
                    return ''
                }
            },
            getTime(time) {
                let date = new Date(time);
                let d = date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds()
                return this.formatDate(date)
            },

            formatDate(date) {
                let hh = date.getHours()
                if (hh < 10) hh = '0' + hh
                let mm = date.getMinutes()
                if (mm < 10) mm = '0' + mm

                return hh + ':' + mm
            },
            getMainImage(message) {
                if (message.author.userpic === null) return false
                return message.author.userpic
            },
        },
    }
</script>

<style scoped>
    .list-enter-active, .list-leave-active {
        transition: all 0.5s;
    }

    .list-enter, .list-leave-to {
        opacity: 0;
    }

    .list_mess {
        list-style: none;
        padding-left: 0;
    }

    .loading {
        display: flex;
        color: white;
        text-align: center;
        position: absolute;
        background-color: #364454;
        z-index: 10;
        /*left: calc(30% - 0px);*/
        left: 50%;
        top: calc(50% - 18px);
        padding: 8px 10px;
        border-radius: 5px;
    }

    a {
        text-decoration: none;
    }

    .message_world {
        word-break: break-all;

    }

    .message {
        display: flex;
        align-items: flex-end;
        background: #f2f3f5; /* Цвет фона */
        color: #160109; /* Цвет текста */
        padding: 10px; /* Поля вокруг текста */
        border-radius: 10px; /* Уголки */
        width: fit-content;
        margin-bottom: 10px;
        margin-left: 5px;

    }

    .time {
        font-size: 12px;
        opacity: 0.7;
        width: 32px;
    }

    .day {
        display: flex;
        justify-content: center;
        background-color: #eaf0ff;
        width: 130px;
        border-radius: 10px;
        margin: auto;
        padding: 5px 0px 5px 0px;
    }
</style>