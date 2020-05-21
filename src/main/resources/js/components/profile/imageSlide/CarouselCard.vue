<template>
    <v-carousel-item>
        <v-sheet
                height="100%"
                tile
        >
            <v-row class="fill-height"
                   align="center"
                   justify="center">

                <div >
                    <v-btn absolute
                           style="z-index: 1000"
                           icon
                           right
                           top
                           x-large
                           @click="$emit('close-slider', false)"
                    >
                        <v-icon>close</v-icon>
                    </v-btn>
                    <v-img :lazy-src="image.urlLink"
                           :src="image.urlLink"
                           :max-height="!isMobile? '500': getSize"
                           :min-height="!isMobile? '500': '310'"
                           :max-width="!isMobile? '500': getSize"
                           :min-width="!isMobile? '500': '310'"
                           class="image_slade"
                    ></v-img>
                </div>

            </v-row>
        </v-sheet>
    </v-carousel-item>
</template>

<script>
    export default {
        props: ['image', 'isMobile', ],
        name: "CarouselCard",
        computed:{
            getSize(){
                return  ''+window.innerWidth
            }
        }
    }
</script>

<style scoped>
</style>