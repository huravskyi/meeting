<template>
    <v-col>
        <v-row class="justify-center align-center">
            <v-card color="#f5f5f5"
                    width="350"
            >
                <div class="d-flex flex-no-wrap justify-start">
                    <router-link :to="'/profile/'+ item.userView.id">
                        <v-avatar
                                class="ma-3 mr-0"
                                size="125"
                                tile
                        >
                            <v-img :src="getImage(item.userView.userpic)"
                                   :lazy-src="accountPreviewMin"

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
                        </v-avatar>
                    </router-link>
                    <div>
                        <div class="subtitle-2 pl-4 align-center">{{getLastTimeVisit(item.lastVisitUser)}}
                        </div>

                        <v-card-title width="100"
                                      class="subtitle-1 font-weight-medium"
                        >
                            <v-badge :color="`${item.online? '#21ff0b': '#ffac32'}`"
                                     dot
                            >
                                <router-link style="text-decoration: none" :to="'/profile/'+ item.userView.id">
                                    <span style="color: #0a70ff">{{item.userView.username + ', ' + getAge(item.userView)}}</span>
                                </router-link>
                            </v-badge>
                        </v-card-title>
                        <v-card-subtitle>Киев Украина</v-card-subtitle>
                    </div>
                </div>
            </v-card>
        </v-row>
        <lazy-loader :getUsersListAction="getUsersListAction"
                     :list="usersListWhoViews"
                     :functionLoader="getFunctionLazyLoader"
        ></lazy-loader>
    </v-col>
</template>

<script>
    const accountPreview = require('../../image/accountPreview.png')
    const accountPreviewMin = require('../../image/accountPreview-min.png')

    import LazyLoader from "./LazyLoader.vue";
    export default {
        components: {LazyLoader},
        props: ['item', 'getLastTimeVisit', 'getAge', 'getUsersListAction', 'usersListWhoViews', 'getFunctionLazyLoader'],
        name: "PageViewsRow",
        data:()=>({
            accountPreview,
            accountPreviewMin,
        }),
        methods:{
            getImage(image){
                if (image){
                    return image
                }else {
                    return accountPreview
                }

            }
        }
    }
</script>

<style scoped>

</style>