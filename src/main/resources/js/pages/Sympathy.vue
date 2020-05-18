<template>
    <v-container>
        <v-card flat>

            <v-tabs v-model="tab"
                    background-color="white"
                    color="deep-purple accent-4"
                    centered
            >
                <v-tab v-for="(item, i) in tabList"
                       :key="i"
                >
                    {{item.title}}
                </v-tab>
            </v-tabs>

            <v-tabs-items v-model="tab">
                <v-tab-item
                        v-for="(item, i) in tabList"
                        :key="i"
                >
                    <v-card-title style="word-break:normal"
                                  class="justify-center"
                    >
                        <p v-html="item.about"></p>
                    </v-card-title>
                    <v-col>
                        <div v-if="load">
                            <div style="height: 250px; width: 250px;" class="ma-auto">
                                <v-img width="250" height="250" :src="img"></v-img>
                            </div>
                        </div>
                        <v-row v-else>
                            <card-view v-for="(user, i) in getUsers"
                                       :key="user.id"
                                       class="ma-auto "
                                       :user="user"
                            ></card-view>
                        </v-row>
                    </v-col>
                </v-tab-item>
            </v-tabs-items>
            <lazy-loader :list="getUserList()"
                         :functionLoader="getFunction"
            ></lazy-loader>
        </v-card>
    </v-container>
</template>

<script>
    import {mapActions, mapMutations, mapState} from "vuex";
    import CardView from "../components/searchCard/CardView.vue";
    const img = ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/load.gif?alt=media&token=8923efec-c9c1-4234-9ea7-ed1235077fa8')
    import LazyLoader from "../components/pageViews/LazyLoader.vue";

    export default {
        name: "Sympathy",
        components: {LazyLoader, CardView},
        data: () => ({
            load: true,
            tab: 0,
            img,
            aboutLikeText: "В этой вкладке находятся пользователи, которым Вы&nbsp;ставите&nbsp;лайк.",
            aboutSympathyText: 'Вы поставили друг другу лайк. Начните общение прямо сейчас.',
            aboutLikeNull: 'Пока вам никто не нравиться',
            aboutSympathyNull: 'Здесь будет показываться совпадения если вы поставили друг другу Like)',
            tabList: [
                {title: 'Совпадения', about: ''},
                {title: 'Вам нравяться', about: ''},
            ]
        }),
        mounted() {
            setTimeout(this.setElementDisplayNone, 10)
            this.getUsersCoincidence()
            this.setProfileLikeNewMutation(null)
        },
        watch: {
            tab(val) {
                if (val === 1) {
                    this.getUsersMyLike()
                } else {
                    this.getUsersCoincidence()
                }
            }
        },
        computed: {
            ...mapState({
                userCoincidence: state => state.storeUserGuest.userCoincidence,
                usersLikeMy: state => state.storeUserGuest.usersLikeMy
            }),
            getUsers() {
                if (this.tab === 0) {
                    if (this.userCoincidence.length !== 0 && this.userCoincidence.listFromDto.length !== 0) {
                        const users = this.userCoincidence.listFromDto.filter(item => item.meLiked)
                        return this.setTextAbout(users, this.tab, this.aboutSympathyText, this.aboutSympathyNull)
                    } else {
                        this.$set(this.tabList[this.tab], 'about', this.aboutSympathyNull)
                    }
                } else {
                    if (this.usersLikeMy.length !== 0 && this.usersLikeMy.listFromDto.length !== 0) {
                        const users = this.usersLikeMy.listFromDto.filter(item => item.meLiked)
                        return this.setTextAbout(users, this.tab, this.aboutLikeText, this.aboutLikeNull)
                    } else {
                        this.$set(this.tabList[this.tab], 'about', this.aboutLikeNull)
                    }
                }
            }
        },
        methods: {
            ...mapMutations(['setProfileLikeNewMutation']),
            ...mapActions(['getUsersCoincidenceAction', 'getUsersMyLikeAction']),

            setTextAbout(users, tab, text, texNull) {
                users.sort((a, b) =>
                    -(this.getTimeLike(a.timeLike) - this.getTimeLike(b.timeLike)))
                if (users.length !== 0) {
                    this.$set(this.tabList[tab], 'about', text)
                } else {
                    this.$set(this.tabList[tab], 'about', texNull)
                }
                return users
            },
            getTimeLike(time) {
                return new Date(time)
            },
            getUserList() {
                if (this.tab === 0) {
                    return this.userCoincidence
                } else {
                    return this.usersLikeMy
                }
            },
            getFunction(page) {
                if (this.tab === 1) {
                    return this.getUsersMyLikeAction(page)
                } else {
                    return this.getUsersCoincidenceAction(page)
                }
            },
            setElementDisplayNone() {
                let element = document.getElementsByClassName('v-slide-group__prev v-slide-group__prev--disabled')
                if (element.length !== 0) {
                    element[0].style.display = 'none'
                }
            },
            getUsersMyLike() {
                this.load = true
                const page = 0
                this.getUsersMyLikeAction(page).then(
                    result => {
                        setTimeout(() => {
                            this.load = false
                        }, 200)
                    },
                    error => {
                        this.load = false
                        alert("Rejected: " + error)
                    }
                )
            },
            getUsersCoincidence() {
                const page = 0
                this.load = true
                this.getUsersCoincidenceAction(page).then(
                    setTimeout(() => {
                        this.load = false
                    }, 200),
                    error => {
                        this.load = false
                        alert("Rejected: " + error)
                    }
                )
            },
        }
    }
</script>

<style scoped>
    .word {
        width: 160px;
        background: #f0f0f0;
        border: 1px solid #333;
        padding: 10px;
        font-size: 18px;
    }
</style>