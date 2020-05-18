<template>
    <div>
        <v-card class="d-flex justify-space-between align-center "
                flat

        >
            <div class="d-flex align-center">
                <v-btn icon height="60" @click="$emit('set-selected', undefined)">
                    <v-icon size="40" color="#0a70ff">mdi-arrow-left-drop-circle-outline</v-icon>
                </v-btn>
                <div class="pl-1 d-flex align-center" >
                    <div>
                        <v-avatar v-if="user.userpic">
                            <v-img max-height="30"
                                   max-width="30"
                                   :src="user.userpic"/>
                        </v-avatar>
                        <v-avatar v-else>
                            <v-img height="30"
                                   width="30"
                                   :src="accountPreview"
                                   class="radio border_radius"
                                   style="border: #0a70ff solid 1px"/>
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
    const accountPreview = ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/accountPreview.png?alt=media&token=8c1044c0-b371-4bf2-91e6-e0e7daf87c87')

    import ButtonMenuMessage from "./ButtonMenuMessage.vue";
    import getMyAge from "../../util/helper/getAge";

    export default {
        props: ['tab', 'selected', 'chats', 'chatsBlock', 'userProfile'],
        name: "MenuMessageList",
        components: {ButtonMenuMessage},
        data: () => ({
            user: '',
            accountPreview,
        }),
        mounted() {
            this.user = this.getUser()
        },
        methods: {
            getAge(data){
               if (data !== undefined)
                return getMyAge(data)
            },
            getUser() {
                const chat = {...this.chats[this.selected]}
                let user
                _.forEach(chat.members, itm => {
                    if (itm.id !== this.userProfile.id) {
                        user = itm
                        return false
                    }
                })
                return user
            },
        }

    }
</script>

<style scoped>

</style>