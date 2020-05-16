<template>
    <transition name="list">
        <v-card id="rounded-card"
                max-width="280"
                height="400"
                color="white"
                class="mb-7"
        >
            <router-link
                    :to="`profile/${user.id}`"
            >
                <v-img class="rounded-card"
                       min-width="280"
                       min-height="280"
                       :lazy-src="accountPreviewMin"
                       :src="getMainImage(user)"
                >
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
            </router-link>

            <v-card-actions style="height: 50px; background-color: #0a70ff">

                <liked-button
                        :class-button="''"
                        :outlined="false"
                        :user="user"
                        :icon="true"
                        :minWidth="60"
                        :text="false"
                        :colorIcon="'white'"
                        :is-mobile="true"
                ></liked-button>

                <v-divider vertical dark></v-divider>
                <v-spacer></v-spacer>
                <div>
                    <router-link to="/message?selected=0">
                        <v-btn text
                               icon
                               class="white--text"
                               min-width="140"
                               @click="writeNewMessage(user)"
                        >
                            <v-icon class="button_hover" >mdi-email-outline</v-icon>
                            <v-spacer></v-spacer>
                            <span>Написать</span>
                        </v-btn>
                    </router-link>
                </div>
            </v-card-actions>
            <router-link class="link"
                         :to="`profile/${user.id}`"
            >
                <v-card-title class="py-1">
                    <v-badge
                            :color="`${user.online? '#21ff0b': '#ffac32'}`"
                            dot
                            offset-x="-3"
                            overlap>
                        {{user.username + ', ' +
                        getAge(user.birthDate)}}
                    </v-badge>
                </v-card-title>
                <v-card-text>
                    <v-list-item-subtitle style="opacity: 0.8">
                        {{user.profileDetail.textAbout}}
                    </v-list-item-subtitle>
                </v-card-text>
            </router-link>
        </v-card>
    </transition>
</template>

<script>
    import getAge from "../../helper/getAge";

    const accountPreviewMin = require('../../image/accountPreview-min.png')
    const accountPreview = require('../../image/accountPreview.png')

    import LikedButton from "../profile/LikedButton.vue";
    import {mapMutations, mapState} from "vuex";

    export default {
        components: {LikedButton},
        props: ['user', 'userProfile'],
        name: "CardView",
        data: () => ({
            accountPreviewMin,
            accountPreview,
        }),
        computed: {
            ...mapState({
                chats: state => state.storeMessages.chats,
            }),
        },
        methods: {
            ...mapMutations(['writeNewMessageAndNewChatMutation', 'setToUpChatMutation']),
            getMainImage(user) {
                return user.userpic ? user.userpic : this.accountPreview
            },
            getAge(date) {
                return getAge(date)
            },
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
                console.log(index)
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
            },
        }
    }
</script>

<style scoped>
    .list-enter-active, .list-leave-active {
        transition: all 1s;
    }

    .list-enter, .list-leave-to {
        opacity: 0;
        transform: translateY(30px);
    }
    #rounded-card {
        border-radius: 10px;
    }

    .rounded-card {
        border-radius: 10px 10px 0 0;
    }

    .link {
        text-decoration: none;
        color: black;
    }
</style>