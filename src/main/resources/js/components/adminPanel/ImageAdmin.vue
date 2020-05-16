<template>
    <div>
        <v-row>
            <v-col class="d-flex">
                <v-select v-model="modelSelected"
                          :items="items"
                          item-text="name"
                          item-value="event"
                          return-object
                          outlined
                ></v-select>
            </v-col>
            <v-col>
                <v-btn height="56" @click="getImage()">Получить Фотографии</v-btn>
            </v-col>
            <v-col>
                <v-text-field v-model="userId" placeholder="Введите Id пользователя" type="number"></v-text-field>
            </v-col>
            <v-col>
                <v-btn height="56" @click="getImageById()">Получить Фотографии по ID</v-btn>
            </v-col>
        </v-row>
        <v-row>
            <dialog-admin v-on:dialog-event="dialog = $event"
                          :delete-and-warning="deleteAndWarningImage"
                          :text="textDialog"
                          :dialog="dialog">
            </dialog-admin>

            <template v-for="(itemImage, i) in imagesDto.listFromDto"
            >
                <v-card class="mb-5" :key="i" color="#f5f5f5">
                    <v-row>
                        <v-col>
                            <v-img height="400"
                                   width="400"
                                   :src="itemImage.urlLink"
                                   class="ml-3"
                            >
                            </v-img>
                        </v-col>
                        <v-col>
                            <router-link :to="'/profile/'+`${itemImage.user}`">
                                <v-card-text>
                                    <p>ID пользователя: {{itemImage.user}}</p>
                                </v-card-text>
                            </router-link>


                            <v-btn color="red"
                                   width="404"
                                   class="mb-5"
                                   @click="dialogOpen(itemImage)"
                            >
                                Удалить фото и сделать предупреждение
                            </v-btn>

                            <v-btn :color="itemImage.tested? 'green': 'red'"
                                   @click="toApprove(itemImage)"
                                   width="404">
                                <div v-if="!itemImage.tested">Одобрить фото</div>
                                <div v-else>Убрать одобрение</div>
                            </v-btn>
                        </v-col>
                    </v-row>
                </v-card>
            </template>
            <lazy-loader :list="imagesDto" :function-loader="getImage"></lazy-loader>
        </v-row>
    </div>
</template>

<script>
    import {mapActions} from "vuex";
    import LazyLoader from "../pageViews/LazyLoader.vue";
    import setUsers from "../../helper/setUsers";
    import DialogAdmin from "./DialogAdmin.vue";

    export default {
        name: "ImageAdmin",
        components: {DialogAdmin, LazyLoader},
        data: () => ({
            textDialog:' Удалить фото?',
            modelSelected: {event: null, name: 'Все'},
            items: [
                {event: null, name: 'Все'},
                {event: 'true', name: 'Провереные'},
                {event: 'false', name: 'Не проверенные'}
            ],
            imagesDto: {listFromDto: []},
            event: null,
            dialog: false,
            image: null,
            userId: null,
        }),

        methods: {
            ...mapActions(['getImageAction', 'getImageByIdAction', 'deleteAndWarningAction', 'toApproveAction']),

            getImageById(){
                if(this.userId !== null){
                    this. getImageByIdAction(this.userId).then(value => {
                        this.imagesDto = value
                    })
                    this.userId = null
                }

            },
            getImage(page) {
                if (this.event === this.tab && page === undefined) {
                    this.imagesDto = this.imagesDto = {listFromDto: []}
                }
                this.event = this.tab
                if (page === undefined) {
                    page = 0
                }
                const action = {
                    page: page,
                    event: this.modelSelected.event
                }
                this.getImageAction(action).then(value => {
                    this.imagesDto = setUsers(this.imagesDto, value)
                })
            },
            dialogOpen(image){
                this.dialog = true
                this.image = image
            },
            deleteAndWarningImage() {
                this.dialog = false
                this.deleteAndWarningAction(this.image).then(resultId => {
                    const filter = this.imagesDto.listFromDto.filter(item => item.id !== resultId)
                    this.$set(this.imagesDto, 'listFromDto', filter)
                })
                this.image = null
            },
            toApprove(img) {
                const event = {
                    id: img.id,
                    tested: !img.tested
                }
                this.toApproveAction(event).then(value => {
                    const index = this.imagesDto.listFromDto.findIndex(item => item.id === event.id)
                    this.$set(this.imagesDto.listFromDto[index], 'tested', value)
                })
            }
        }
    }
</script>

<style scoped>

</style>