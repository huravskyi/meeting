<template>
    <div>
        <div class=" upload mx-auto">
            <v-img class="upload preview-image"
                   lazy-src="/js/favicon.ico"
                   :src="img"
                   title="фото"
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
        </div>
        <div class="button-wrapper">
            <router-link :to="getPath">
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
    import {mapState} from "vuex";
    import LikedButton from "../LikedButton.vue";
    import {writeNewMessage} from "../../mixins/writeNewMessageMixin";

    export default {
        components: {LikedButton},
        props: ['img', 'profileUserGuest', 'accountPreview_min'],
        name: "userGuest",
        mixins:[writeNewMessage],
        computed: {
            ...mapState({
                userProfile: state => state.storeUserProfile.userProfile,
            }),
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