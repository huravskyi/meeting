<template>
    <div>
        <div v-if="!myProfile">
            <user-guest v-if="profileUserGuest"
                        :img="getMainImage()"
                        :isMobile="isMobile"
                        :profileUserGuest="profileUserGuest"
                        :accountPreview_min="accountPreview_min"
            >
            </user-guest>
        </div>

        <div v-else>
            <div v-if="!image">
                <div class="upload mx-auto">
                    <v-img class="upload preview-image " ref="input" :src="getMainImage()">
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
                </div>
                <div class="button-wrapper">
                    <button class="button" @click="$refs.file.click()">
                        <input type="file" hidden
                               ref="file" @change="uploadImage($event)" accept="image/*"/>
                        Загрузить фотографию
                    </button>
                </div>
            </div>
            <div v-else>
                <div>
                    <v-btn icon @click="image = null">
                        <v-icon size="40">mdi-close</v-icon>
                    </v-btn>
                </div>
                <div class="upload">
                    <Cropper
                            class="upload-example-cropper"
                            :src="image"
                            ref="cropper"
                            :stencilProps="{
        	                                   	minAspectRatio: 10/10,
        	                                 	maxAspectRatio: 10/10
                                               	}"
                    />
                </div>
                <div class="button-wrapper ">
                    <button class="button" @click="crop">
                        <input type="file" name="file" id="file" ref="file" hidden/>
                        Сохранить
                    </button>
                </div>
            </div>
            <div class="text-center">
                <v-snackbar class="text-center"
                            v-model="snackbar"
                            vertical
                            top
                >
                    {{ text }}
                    <v-btn small
                           dark
                           text
                           @click="snackbar = false"
                    >
                        Закрыть
                    </v-btn>
                </v-snackbar>
            </div>
            <div class="text-center">
                <v-overlay :value="overlay">
                    <v-progress-circular indeterminate size="64"><h1>Подождите фото загружаеться</h1>
                    </v-progress-circular>
                </v-overlay>
            </div>

        </div>
    </div>
</template>

<script>
    import UserGuest from "./imageMain/userGuest.vue"

    const accountPreview = ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/accountPreview.png?alt=media&token=8c1044c0-b371-4bf2-91e6-e0e7daf87c87')
    const accountPreview_min = ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/accountPreview-min.png?alt=media&token=209837ce-9ee9-47a7-ab45-7ba39d551f82')
    import {Cropper, RectangleStencil} from 'vue-advanced-cropper'
    import {mapState, mapActions} from 'vuex'

    export default {
        props: ['myProfile', 'isMobile', 'profileUserGuest'],
        components: {UserGuest, Cropper, RectangleStencil},
        name: "ImageMain",
        data: () => ({
            snackbar: false,
            text: '',

            overlay: false,
            file: '',
            coordinates: {
                width: 0,
                height: 0,
                left: 0,
                top: 0
            },
            image: null,
            img: accountPreview,
            accountPreview: accountPreview,
            accountPreview_min: accountPreview_min,
        }),

        computed: {
            ...mapState({
                userProfile: state => state.storeUserProfile.userProfile,
                images: state => state.storeImage.images,
            }),
        },
        methods: {
            ...mapActions(['addImageAction']),

            crop() {
                this.overlay = true
                if (this.overlay) {
                    const {coordinates, canvas} = this.$refs.cropper.getResult()
                    this.coordinates = coordinates
                    this.image = canvas.toDataURL('image/jpeg', 0.1)
                    this.accountPreview = this.image
                    this.image = null
                    this.saveImage()
                }
            },
            uploadImage(event) {
                let input = event.target;
                if (input.files && input.files[0]) {
                    console.log(input.files[0].size)
                    if (input.files[0].size < 2000) {
                        return this.setAlert(true)
                    }else if(input.files[0].size > 5500000){
                        return this.setAlert(false)
                    } else {
                        let reader = new FileReader()
                        reader.onload = (e) => {
                            this.image = e.target.result
                        }
                        reader.readAsDataURL(input.files[0])
                    }
                }
            },
            getMainImage() {
                if (!this.myProfile) {
                    const image = this.profileUserGuest.userpic
                    if (image !== null) {
                        return image
                    } else {
                        return this.accountPreview
                    }
                } else {
                    const image = this.userProfile.userpic
                    if (image !== null) {
                        return image
                    } else {
                        return this.accountPreview
                    }
                }
            },
            saveImage() {
                const image = {
                    name: this.accountPreview
                }
                let answer = this.addImageAction(image)
                answer.then(
                    result => {

                        result ? this.successImage() : this.failureImage()
                    },
                    error => {
                        this.overlay = false
                        alert("Rejected: " + error)
                    }
                )
            },
            successImage() {
                this.overlay = false
                this.snackbar = true
                this.text = 'Фотография успешно загружена'
            },
            failureImage() {
                this.overlay = false
                this.snackbar = true
                this.text = 'Вы не можете загрузить больше пяти фотографий'
            },
            setAlert(event) {
                this.snackbar = true
                event?this.text = 'Фотография должна быть больше 2KB':this.text = 'Фотография не должна быть больше 5MB'
            }
        }
    }
</script>

<style scoped>
    .preview-image {
        border-radius: 10px;

    }

    .upload {
        min-height: 300px;
        min-width: 300px;
        background-color: #3c3f41;
        border-radius: 10px;

    }

    .upload-example-cropper {
        border: solid 1px #EEE;
        min-height: 300px;
        width: 100%;
    }

    .button-wrapper {
        display: flex;
        justify-content: center;
        margin-top: 17px;
    }

    .button {
        color: white;
        font-size: 16px;
        padding: 10px 20px;
        background: #3fb37f;
        cursor: pointer;
        transition: background 0.5s;
    }

    .button:hover {
        background: #38d890;
    }

</style>