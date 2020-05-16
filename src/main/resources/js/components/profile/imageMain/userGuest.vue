<template>
    <div>
        <div class=" upload mx-auto">
            <v-img class="upload preview-image"
                   :lazy-src="accountPreview_min"
                   :src="img">
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
        </div>
        <div class="button-wrapper">
            <router-link to="/message?selected=0">
                <v-btn class="button"
                       color="#0a70ff"
                       dark
                       large
                       @click="writeNewMessage(profileUserGuest)"
                >
                    Написать сообщение
                </v-btn>
            </router-link>
        </div>

        <liked-button class="mt-2"
                      :class-button="'button'"
                      :outlined="true"
                      :user="profileUserGuest"
                      :icon="false"
                      :minWidth="0"
                      :text="true"
                      :colorIcon="'blue'"
                      v-if="isMobile"
                      :isMobile="isMobile"
        ></liked-button>
    </div>
</template>

<script>
    import {mapMutations, mapState} from "vuex";
    import LikedButton from "../LikedButton.vue";

    export default {
        components: {LikedButton},
        props: ['img', 'isMobile', 'profileUserGuest', 'accountPreview_min'],
        name: "userGuest",
        computed: {
            ...mapState({
                userProfile: state => state.storeUserProfile.userProfile,
                chats: state => state.storeMessages.chats,
            }),
        },
        methods: {
            ...mapMutations(['writeNewMessageAndNewChatMutation','setToUpChatMutation']),

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
        }
    }
</script>

<style scoped>
    .button-wrapper {
        display: flex;
        justify-content: center;
        margin-top: 17px;
    }

    .button {
        width: 300px;
    }

    .preview-image {
        border-radius: 10px;
    }

    .upload {
        min-height: 300px;
        min-width: 300px;
        background-color: #3c3f41;
        border-radius: 10px;

    }
</style>