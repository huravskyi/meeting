<template>
    <v-card class="pa-2 mt-0"
            width="100%"
    >
        <div>
            <v-sheet
                    class="mx-auto"
                    max-width="680"
            >
                <v-slide-group show-arrows
                               center-active>
                    <v-slide-item
                            v-for="(item, i) in !myProfile? profileUserGuest? profileUserGuest.images: '' : images"
                            :key="i"
                            v-slot:default="{ active, toggle }"
                    >
                        <v-card width="126"
                                height="122"
                                style="border: dodgerblue solid 0.5px "
                                class="mr-5"
                                @click="toggle"
                                @click.stop="(dialog = true) &&  (carouselModel = i)"
                        >
                            <div>
                                <div class=" row text-center">
                                    <v-col class="text-center  pt-2">
                                        <v-avatar tile size="105" :style="getOpacity(item)">
                                            <v-img class="preview-image"
                                                   :src="item.urlLink"
                                                   :title="`${item.hide?'Фото закрыто пользователем':'фото открыто'}`">
                                                <menu-image
                                                        :imageAction="imageAction"
                                                        :image="item"
                                                        :myProfile="myProfile"
                                                >
                                                </menu-image>
                                            </v-img>
                                        </v-avatar>
                                    </v-col>
                                </div>
                            </div>
                        </v-card>
                    </v-slide-item>
                </v-slide-group>
            </v-sheet>

            <v-dialog
                    v-model="dialog"
                    width="100%"
            >
                <v-card>
                    <v-carousel v-model="carouselModel" hide-delimiters>
                        <carousel-card
                                v-for="(image,i) in !myProfile? profileUserGuest? profileUserGuest.images: '' : images"
                                :key="i"
                                :image="image"
                                v-on:close-slider="dialog = $event"
                                :isMobile="isMobile"
                        >
                        </carousel-card>
                    </v-carousel>
                </v-card>
            </v-dialog>
        </div>
    </v-card>
</template>

<script>
    import {mapState, mapActions} from 'vuex'
    import CarouselCard from "./imageSlide/CarouselCard.vue";
    import MenuImage from "./imageSlide/MenuImage.vue";

    export default {
        components: {MenuImage, CarouselCard},
        props: ['isMobile', 'myProfile'],
        name: "ImageSlide",

        data: () => ({
            carouselModel: null,
            dialog: false,
            widthSheet: '',
            disabledMain: true,
        }),
        computed: {
            ...mapState({
                images: state => state.storeImage.images,
                profileUserGuest: state => state.storeUserGuest.userGuest
            }),
        },
        methods: {
            ...mapActions(['delImageAction', 'editMainAction', 'changeHideStatusAction']),

            getOpacity(image) {
                if (image.hide && this.myProfile) {
                    return 'opacity: 0.5;'
                }
            },

            imageAction(action, image) {
                switch (action) {
                    case 'main':
                        this.editMainImage(image)
                        break
                    case 'hide':
                        this.changeHide(image)
                        break
                    case 'del':
                        this.deleteImage(image)
                        break
                }
            },
            changeHide(img) {
                const image = {
                    id: img.id,
                    hide: !img.hide,
                    main: false
                }
                this.changeHideStatusAction(image)
            },
            editMainImage(img) {
                const image = {
                    id: img.id,
                    hide: false,
                    main: true
                }
                this.editMainAction(image)
            },
            deleteImage(image) {
                this.delImageAction(image)
            },
        }
    }
</script>

<style scoped>

</style>