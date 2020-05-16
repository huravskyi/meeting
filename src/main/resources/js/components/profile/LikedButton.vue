<template>
    <div class="text-center">
        <v-tooltip bottom>
            <template v-slot:activator="{ on }">
                <v-btn :class="classButton + ' ma-auto'"
                       :outlined="outlined"
                       :text="text"
                       :icon="icon"
                       :min-width="minWidth"
                       :color="colorIcon"
                       v-on="on"
                       @click="like(user)"
                       :loading="loadingButton"
                >
                    <v-icon :color="user.meLiked? '#fc3d7d':''">
                        {{user.meLiked? 'mdi-heart':'mdi-heart-outline'}}
                    </v-icon>
                    <span v-if="text" style="color: #160109" class="pl-2">Нравиться</span>

                </v-btn>
            </template>

            <span v-if="!user.meLiked">Нравиться</span>
            <span v-else>Больше не нравиться</span>
        </v-tooltip>
        <div class="text-center">
            <v-snackbar class="text-center flex-wrap"
                        v-model="snackbar"
                        top
                        :vertical="isMobile"
                        :timeout="timeout"
            >
                <div>{{ textSnackbar}}</div>
                <v-btn x-small
                       dark
                       text
                       @click="snackbar = false"
                >
                    Закрыть
                </v-btn>
            </v-snackbar>
        </div>
    </div>
</template>

<script>
    import {mapActions} from "vuex";

    export default {
        props: ['user', 'outlined', 'text', 'icon', 'minWidth', 'classButton', 'colorIcon', 'isMobile'],
        name: "LikedButton",
        data: () => ({
            timeout:1500,
            snackbar: false,
            textSnackbar: '',
            loadingButton: false
        }),
        methods: {
            ...mapActions(['setLikeAction']),

            like(user) {
                this.loadingButton = true
                let answer
                if (user.meLiked) {
                    const userPojo = {
                        id: user.id,
                        meLiked: false
                    }
                    answer = this.setLikeAction(userPojo)
                    this.snackbar = true
                    this.textSnackbar = 'Вы скрываете скрытые фото'
                } else {
                    const userPojo = {
                        id: user.id,
                        meLiked: true
                    }
                    answer = this.setLikeAction(userPojo)
                    this.snackbar = true
                    this.textSnackbar = 'Вы показываете скрытые фото'
                }
                 answer.then(result =>{
                     if (result) {
                         setTimeout(()=>{
                             this.loadingButton = false
                         },500)
                     }
                 })

            }
        }
    }
</script>

<style scoped>
    .button {
        width: 300px;
    }
</style>