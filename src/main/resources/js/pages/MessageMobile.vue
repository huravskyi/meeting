<template>
    <div>
        <v-row align="center" style="z-index: 1000; position: fixed; background-color: white; width: 100%">
            <div :style="'width: inherit; display:' + displayMenuNameAction">
                <v-list class="pt-0" style="width: 100%">
                    <v-list-group no-action
                                  sub-group
                                  :prepend-icon="menuIconAction"
                                  v-model="open"
                    >
                        <template v-slot:activator>
                            <v-list-item-content>
                                <v-list-item-title>{{menuNameAction}}</v-list-item-title>
                            </v-list-item-content>
                        </template>

                        <v-list-item
                                v-for="(item, i) in tabTitle"
                                :key="i"
                                link
                        >
                            <v-list-item-content>
                                <v-list-item-title>
                                    <div class="d-flex" @click="chooseChat(item, i)">
                                        <v-icon class="pr-3" size="17">{{item.icon}}</v-icon>
                                        {{item.title}}
                                    </div>
                                </v-list-item-title>
                            </v-list-item-content>
                        </v-list-item>
                    </v-list-group>
                    <v-divider></v-divider>
                </v-list>
                <div class="d-flex  align-center justify-center button_icon">
                    <v-btn icon
                           @click="displayMenuNameAction = 'none' , displayBackspace = 'flex '"
                    >
                        <v-icon>mdi-magnify</v-icon>
                    </v-btn>
                </div>
            </div>
            <div :style="'width: inherit; display:' + displayBackspace">
                <v-row style="height: 50px; padding: 0 0 0 0;  ">
                    <v-btn icon
                           height="64"
                           width="64"
                           @click="displayMenuNameAction = 'flex' , displayBackspace = 'none', name = ''"
                    >
                        <v-icon>mdi-keyboard-backspace</v-icon>
                    </v-btn>
                    <v-text-field class="mr-5"
                                  v-model="name"
                                  label="Введите имя"
                                  clearable
                    ></v-text-field>
                </v-row>
            </div>
        </v-row>
        <v-list two-line-line>
            <v-list-item-group
                    v-model="selectedGroup"
                    active-class="blue--text"
            >
                <list-users
                        :userName="name"
                        :isMobile="isMobile"
                        :nameUser="name"
                        :list="tabTitle[tab]"
                        :chats="chats"
                        :tab="tab"
                        :chatsBlock="chatsBlock"
                        :accountPreview="accountPreview"
                        :accountPreviewMin="accountPreviewMin"
                >
                </list-users>
            </v-list-item-group>
        </v-list>
    </div>
</template>

<script>
    import ListUsers from "../components/messages/ListUsers.vue";

    export default {
        components: {ListUsers},
        props: ['chats', 'userProfile', 'tab', 'chatsBlock', 'tabTitle', 'isMobile', 'accountPreviewMin', 'accountPreview'],
        name: "MessageMobile",
        data: () => ({
            selectedGroup: undefined,
            name: '',
            displayMenuNameAction: 'flex',
            displayBackspace: 'none',
            menuIconAction: '',
            menuNameAction: '',
            open: false,
        }),
        watch:{
            selectedGroup(val){
                this.$emit('set-selected', this.selectedGroup)
            }
        },
        mounted() {
            this.menuIconAction = this.tabTitle[this.tab].icon
            this.menuNameAction = this.tabTitle[this.tab].title
        },
        methods: {
            chooseChat(item, index) {
                this.menuIconAction = item.icon
                this.menuNameAction = item.title
                this.open = false
                this.$emit('set-tab', index)
            }
        }

    }
</script>

<style scoped>
    .button_icon {
        height: 64px;
        width: 64px;
    }

    .v-list-item-title {
        text-decoration: none;
    }

</style>