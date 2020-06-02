<template>
    <div class="menu_messager">
        <div class="text-center">
            <v-menu offset-y bottom left transition="slide-x-transition">
                <template v-slot:activator="{ on }">
                    <v-btn text icon v-on="on">
                        <div class="d-flex flex-column">
                            <div class="d-flex flex-column justify-center align-center">
                                <v-icon size="44">mdi-menu</v-icon>
                            </div>
                            <span style="font-size: 11px">menu</span>
                        </div>
                    </v-btn>
                </template>
                <v-list>
                    <v-list-item
                            v-for="(item, index) in items"
                            :key="index"
                            @click="onClick(item)"
                    >
                        <v-list-item-icon>
                            <v-icon>{{ item.icon }}</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title>{{ item.title }}</v-list-item-title>
                    </v-list-item>
                </v-list>
            </v-menu>
        </div>
    </div>
</template>

<script>
    import {mapActions} from "vuex";

    export default {
        props: ['selected', 'chats', 'userProfile', 'tab', 'chatsBlock'],
        name: "ButtonMenuMessage",
        data: () => ({
            items: [],
        }),
        created() {
            if (this.tab === 1) {
                this.items = [{title: 'Разблокировать', icon: 'mdi-block-helper', click: 'unblock'}]
            } else {
                this.items = [
                    {title: 'Блокировать', icon: 'mdi-block-helper', click: 'block'},
                    {title: 'Удалить переписку', icon: 'mdi-delete-outline', click: 'removeChat'},
                ]
            }
        },
        methods: {
            ...mapActions(['blockAndUnblockUserAction', 'removeChatUserAction']),
            onClick(item) {
                const index = JSON.parse(JSON.stringify(this.selected))
                this.$emit('set-selected', undefined);
                this.$router.replace('/message')
                if (window.location.search.toString().includes('selected')) this.$router.push('message')
                if (item.click === 'block') {
                    const chatAction = {
                        action: 'block',
                        userId: this.getUserId(this.chats[index])
                    }
                    this.blockAndUnblockUserAction(chatAction)
                }
                if (item.click === 'unblock') {
                    const chatAction = {
                        action: 'unblock',
                        userId: this.getUserId(this.chatsBlock[index])
                    }
                    this.blockAndUnblockUserAction(chatAction)
                }
                if (item.click === 'removeChat') {
                    this.removeChatUserAction(this.chats[index].id)
                }
            },
            getUserId(chat) {
                let id
                chat.members.forEach(item => {
                    if (item.id !== this.userProfile.id) {
                        id = item.id
                    }
                })
                return id
            }
        }
    }
</script>

<style scoped>
    .menu_messager {
        position: absolute;
        right: 15px;
        top: 16px;
    }
</style>