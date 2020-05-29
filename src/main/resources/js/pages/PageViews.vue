<template>
    <div>
        <v-container>
            <v-card class="pb-12">
                <v-card-title class="justify-center"><h1 class="title">Просмотры</h1></v-card-title>
                <v-card-text class="text-center">
                    <p> Здесь показываеться кто смотрел вашу анкету</p>
                    <v-btn @click="cleanUserViewed()">Очистить просмотры</v-btn>
                </v-card-text>
                <page-views-row v-for="(item, i) in getListUsers()"
                                :key="i"
                                :item="item"
                                :getLastTimeVisit="getLastTimeVisit"
                                :getAge="getAge"
                                :usersListWhoViews="usersListWhoViews"
                                :getFunctionLazyLoader="getFunctionLazyLoader"
                >
                </page-views-row>
            </v-card>
        </v-container>
    </div>
</template>

<script>
    import {mapActions, mapMutations, mapState} from "vuex";
    import getLastVisit from "../util/helper/getLastVisit";
    import getMyAge from "../util/helper/getAge";
    import PageViewsRow from "../components/pageViews/PageViewsRow.vue";

    export default {
        name: "PageViews",
        components: {PageViewsRow},
        data: () => ({}),
        mounted() {
            this.getUsersWhoViews()
            if (this.mobileNavigationMutation)
                this.mobileNavigationMutation(true)
        },
        computed: {
            ...mapState({
                usersListWhoViews: state => state.storeUserGuest.usersListWhoViews,
                userProfile: state => state.storeUserProfile.userProfile,

            })
        },
        methods: {
            ...mapMutations(['mobileNavigationMutation']),
            ...mapActions(['getUsersListAction','cleanUserViewedAction']),
            cleanUserViewed() {
                this.cleanUserViewedAction(this.userProfile.id)
            },
            getLastTimeVisit(lastVisit) {
                return getLastVisit(lastVisit)
            },
            getUsersWhoViews() {
                if (this.usersListWhoViews == null) {
                    const page = 0
                    this.getUsersListAction(page)
                }
            },
            getFunctionLazyLoader(page) {
                return this.getUsersListAction(page)
            },

            getListUsers() {
                if (this.usersListWhoViews != null) {
                    return this.usersListWhoViews.listFromDto
                }
            },
            getAge(item) {
                return getMyAge(item.birthDate)
            },
        },

    }
</script>

<style scoped>

</style>