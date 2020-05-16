<template>
    <iframe
            :src="targetSrc"
            frameborder="0"
            allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
            allowfullscreen
            height="100%"
            width="100%"
    ></iframe>
</template>

<script>
    export default {
        name: 'YouTub',
        props: ['src'],
        computed: {
            targetSrc() {
                let parts = this.src.split('/')
                return `https://www.youtube.com/embed/${parts[parts.length - 1]}`
            }
        },
    }
</script>

<style scoped>
</style>