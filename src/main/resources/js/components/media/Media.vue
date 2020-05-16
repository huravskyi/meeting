<template>
    <div>
        <v-card v-if="type === 'href'"
        >
            <v-img v-if="message.linkCover" :src="message.linkCover" aspect-ratio="2.75"></v-img>

            <v-card-subtitle>
                <a :href="message.link" target="_blank">{{message.linkTitle || message.link}}</a>
            </v-card-subtitle>


            <v-card-text>
                <div v-if="message.linkDescription">
                    {{message.linkDescription}}
                </div>
            </v-card-text>
        </v-card>
        <v-flex v-if="type === 'image'">
            <a :href="message.link" target="_blank">
                <v-img min-height="300" v-if="message.linkCover" :src="message.linkCover" aspect-ratio="2.75"></v-img>
                <span style="color: #160109">{{getLinkCut}}</span>
            </a>
        </v-flex>

        <v-flex v-if="type === 'youtube'">
            <you-tube :src="message.link"></you-tube>
        </v-flex>
    </div>
</template>

<script>
    import YouTube from './YouTub.vue'

    export default {
        name: 'Media',
        components: {YouTube},
        props: ['message'],
        data() {
            return {
                type: 'href'
            }
        },
        computed: {
            getLinkCut() {
                if (this.message.link.length > 35) {
                    return this.message.link.slice(0, 35)
                }

            }
        },
        beforeMount() {
            const checkNotNull = this.message.link !== null
            if (checkNotNull && this.message.link.indexOf('youtu') > -1 && this.message.link.toString().length > 24) {
                this.type = 'youtube'
            } else if (checkNotNull && this.message.link.match(/\.(jpeg|jpg|gif|png)$/) !== null) {
                this.type = 'image'
            } else {
                this.type = 'href'
            }
        }
    }
</script>

<style scoped>
</style>