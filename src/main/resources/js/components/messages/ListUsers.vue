<template>
    <div :class="`${isMobile? ' pt-1':''}`">
        <v-card
                :class="'mx-auto '+`${isMobile? ' pt-12':''}`"
                flat
        >
            <template v-for="(item, index) in list.content?list.content:[]">
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
                                            <v-progress-circular indeterminate
                                                                 color="grey lighten-5"></v-progress-circular>
                                        </v-row>
                                    </template>
                                </v-img>
                            </v-badge>
                        </router-link>
                    </v-list-item-icon>

                    <v-list-item-content @click="hideMobileNavigation()">
                        <v-list-item-title>
                            <v-badge
                                    :color="`${getUser(item).isOnline? '#21ff0b': '#ffac32'}`"
                                    dot
                                    offset-x="-3"
                                    overlap>
                                {{getUser(item).username + ', ' +
                                getAge(item)}}
                            </v-badge>

                        </v-list-item-title>
                        <v-list-item-subtitle>
                            <smile-message
                                    :message="item.lastMessage!== undefined?item.lastMessage.content:''">
                            </smile-message>

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
    import {mapMutations, mapState} from "vuex";

    const sms = ("https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/sms1.mp3?alt=media&token=338e5d21-e7ec-4a3b-be41-f2a24378a45d")
    import getMyAge from "../../util/helper/getAge";
    import SmileMessage from "./SmileMessage.vue";

    export default {
        components: {SmileMessage},
        props: ['isMobile', 'chats', 'tab', 'chatsBlock', 'list', 'nameUser', 'isMobile', 'userName', 'accountPreview', 'accountPreviewMin'],
        name: "ListUsers",
        data: () => ({
            sms,
            messages: 0,
        }),
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
            ...mapMutations(['mobileNavigationMutation']),
            hideMobileNavigation() {
                if (this.isMobile) {
                    this.mobileNavigationMutation(false)
                }
            },
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
                        item.members.forEach(itm => {
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
                item.members.forEach(item => {
                    if (item.id !== this.userProfile.id) {
                        date = item.birthDate
                    }
                })
                return getMyAge(date)
            },
        },
    }
</script>

<style scoped>
    .border_radius {
        border-radius: 10px;
    }
</style>