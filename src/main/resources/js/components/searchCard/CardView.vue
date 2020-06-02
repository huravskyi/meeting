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
                    <router-link :to="getPath">
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
    import getAge from "../../util/helper/getAge";

    const accountPreviewMin = ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/accountPreview-min.png?alt=media&token=209837ce-9ee9-47a7-ab45-7ba39d551f82')
    const accountPreview = ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/accountPreview.png?alt=media&token=8c1044c0-b371-4bf2-91e6-e0e7daf87c87')

    import LikedButton from "../profile/LikedButton.vue";
    import {writeNewMessage} from "../mixins/writeNewMessageMixin";

    export default {
        components: {LikedButton},
        props: ['user', 'userProfile'],
        name: "CardView",
        mixins:[writeNewMessage],
        data: () => ({
            accountPreviewMin,
            accountPreview,
        }),
        methods:{
            getMainImage(user) {
                return user.userpic ? user.userpic : this.accountPreview
            },
            getAge(date) {
                return getAge(date)
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