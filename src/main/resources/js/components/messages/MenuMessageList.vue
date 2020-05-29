<template>
    <div>
        <v-card class="d-flex justify-space-between align-center "
                flat
        >
            <div class="d-flex align-center">
                <v-btn icon height="60" @click="setSelectAndMobileNavigation()">
                    <v-icon size="40" color="#0a70ff">mdi-arrow-left-drop-circle-outline</v-icon>
                </v-btn>
                <div class="pl-1 d-flex align-center" >
                    <div>
                        <v-avatar>
                            <v-img max-height="30"
                                   max-width="30"
                                   :lazy-src="accountPreviewMin"
                                   :src="user.userpic? user.userpic: accountPreview"/>
                        </v-avatar>
                    </div>
                    <div style="color: blue">
                        {{user.username +', '}} {{getAge(user.birthDate)}}
                    </div>
                </div>
            </div>
        </v-card>
        <v-divider></v-divider>
        <v-divider></v-divider>
    </div>
</template>
<script>
    import getMyAge from "../../util/helper/getAge";
    import {mapMutations} from "vuex";

    export default {
        props: ['tab', 'selected', 'chats', 'chatsBlock', 'userProfile', 'accountPreview', 'accountPreviewMin'],
        name: "MenuMessageList",
        data: () => ({
            user: '',
        }),
        mounted() {
            this.user = this.getUser()
        },
        methods: {
            ...mapMutations(['mobileNavigationMutation']),
            setSelectAndMobileNavigation(){
                this.$emit('set-selected', undefined)
                this.mobileNavigationMutation(true)
            },
            getAge(data){
               if (data !== undefined)
                return getMyAge(data)
            },
            getUser() {
                const chat = {...this.chats[this.selected]}
                let user
                chat.members.forEach( itm => {
                    if (itm.id !== this.userProfile.id) {
                        user = itm
                    }
                })
                return user
            },
        }

    }
</script>

<style scoped>

</style>