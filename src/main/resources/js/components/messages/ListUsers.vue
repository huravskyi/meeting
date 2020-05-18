<template>
    <div :class="`${isMobile? ' pt-1':''}`">
        <v-card
                :class="'mx-auto '+`${isMobile? ' pt-12':''}`"
                flat
        >
            <template v-for="(item, index) in list.content">
                <v-list-item :key="index">
                    <v-list-item-icon class="mr-3">
                        <router-link :to="/profile/+ `${getUser(item).id}`">
                            <v-badge color="pink"
                                     overlap
                                     :content="item.numberOfNewMessage"
                                     :value="item.numberOfNewMessage"
                            >
                                <v-img
                                        class="radio border_radius"
                                        max-height="60" max-width="60"
                                        :lazy-src="accountPreviewMin"
                                        :src="getMainImage(item)">
                                    <template v-slot:placeholder>
                                        <v-row
                                                class="fill-height ma-0"
                                                align="center"
                                                justify="center"
                                        >
                                            <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
                                        </v-row>

                                    </template>
                                </v-img>

                            </v-badge>
                        </router-link>
                    </v-list-item-icon>

                    <v-list-item-content>
                        <v-list-item-title>
                            <v-badge
                                    :color="`${item.online? '#21ff0b': '#ffac32'}`"
                                    dot
                                    offset-x="-3"
                                    overlap>
                                {{getUser(item).username + ', ' +
                                getAge(item)}}
                            </v-badge>

                        </v-list-item-title>
                        <v-list-item-subtitle>

                            <smile-message :message="item.lastMessage"></smile-message>

                        </v-list-item-subtitle>
                    </v-list-item-content>
                </v-list-item>
                <v-divider
                        v-if="index + 1 < list.content.length"
                        :key="index + 'dev'"
                ></v-divider>
            </template>
        </v-card>
        <audio id="audio">
            <source :src="sms"></source>
            Your browser isn't invited for super fun audio time.
        </audio>
    </div>
</template>

<script>
    import {mapState} from "vuex";

    const accountPreviewMin = ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/accountPreview-min.png?alt=media&token=209837ce-9ee9-47a7-ab45-7ba39d551f82')
    const sms = ("https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/sms1.mp3?alt=media&token=338e5d21-e7ec-4a3b-be41-f2a24378a45d")
    const accountPreview = ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/accountPreview.png?alt=media&token=8c1044c0-b371-4bf2-91e6-e0e7daf87c87')
    import getMyAge from "../../util/helper/getAge";
    import SmileMessage from "./SmileMessage.vue";

    export default {
        components: {SmileMessage},
        props: ['chats', 'tab', 'chatsBlock', 'list', 'nameUser', 'isMobile', 'userName'],
        name: "ListUsers",
        data: () => ({
            sms,
            accountPreviewMin,
            accountPreview,
            messages: 0,
        }),
        mounted() {

        },
        computed: {
            ...mapState({
                userProfile: state => state.storeUserProfile.userProfile,
            }),
        },
        watch: {
            userName(val) {
                this.setName(val)
            },
        },
        methods: {
            setName(val) {
                if (val === null) {
                    if (this.tab === 0) {
                        this.list.content = this.chats
                    } else {
                        this.list.content = this.chatsBlock
                    }
                } else {
                    if (this.tab === 0) {
                        this.list.content = this.chats
                    } else {
                        this.list.content = this.chatsBlock
                    }

                    const newListContent = []
                    let user
                    this.list.content.forEach((item, index) => {
                        _.forEach(item.members, itm => {
                            if (itm.id !== this.userProfile.id) {
                                user = itm
                                return false
                            }
                        })
                        if (user.username.toLowerCase().match(val.toLowerCase())) {
                            newListContent.push(this.list.content[index])
                        }
                    })
                    this.list.content = newListContent
                }
            },
            getLastMessage(item) {
                if (item.messages.length === 0) {
                    return item.lastMessage
                } else {
                    return item.messages[item.messages.length - 1].content.content
                }
            },
            getMainImage(item) {
                for (let i = 0; i < item.members.length; i++) {
                    if (item.members[i].id !== this.userProfile.id) {
                        return item.members[i].userpic ? item.members[i].userpic : this.accountPreview
                    }
                }
            },
            getUser(item) {
                for (let i = 0; i < item.members.length; i++) {
                    if (item.members[i].id !== this.userProfile.id) {
                        return item.members[i]
                        break
                    }
                }
            },
            getAge(item) {
                let date
                _.forEach(item.members, itm => {
                    if (itm.id !== this.userProfile.id) {
                        date = itm.birthDate
                        return false
                    }
                })
                return getMyAge(date)
            },
        },
    }
</script>

<style scoped>
    .v-text-field {
        padding-top: 0px;
        margin-top: 0px;
    }

    .v-input__slot {
        margin: 0;
        padding: 0;
    }

    .new-message {
        text-align: center;
        width: auto;
        height: 18px;
        background-color: #ff1d6c;
        border-radius: 2px;
        color: white;
        padding: 2px 3px 2px 2px;
        position: absolute;
        top: 57px;
        left: 82px;
    }

    .border_radius {
        border-radius: 10px;
    }

    .isOnline {
        font-size: 18px;
        line-height: 18px;
        position: relative;
    }


    .isOnlineAbsolute {
        content: "\2022";
        font-size: 66px;
        font-weight: 800;
        left: 83px;
        line-height: 18px;
        position: absolute;
        top: 7px;
    }
</style>