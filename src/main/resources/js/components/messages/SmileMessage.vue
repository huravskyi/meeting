<template>
    <div>
        <div v-if="messageMedia === undefined || messageMedia.link === null" v-html="getHtml(message)">
        </div>
        <media v-else :message="messageMedia"></media>
    </div>
</template>

<script>
    import showSmile from "../../util/helper/showSmile";
    import Media from "../media/Media.vue";

    export default {
        components: {Media},
        props: ['message', 'messageMedia'],
        name: "SmileMessage",
        methods: {
            getHtml(message) {
                return showSmile(message, 'chatMessTextarea', 'past', false)
            },
        },
    }
</script>

<style scoped>
    .message_world {
        word-break: break-all;

    }
</style>