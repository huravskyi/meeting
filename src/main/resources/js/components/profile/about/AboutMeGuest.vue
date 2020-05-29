<template>
    <v-card width="100%">
        <v-card-title >
            <span class="title">{{description.titleAbout}}</span>
        </v-card-title>
        <p class="subtitle-1  px-6" style="word-break: break-all; opacity: 0.9" v-if="profileUserGuest">
            {{profileUserGuest.profileDetail.textAbout}}
        </p>
    </v-card>
</template>

<script>
    export default {
        props: ['description', 'profileUserGuest'],
        name: "AboutMeGuest",

    }
</script>

<style scoped>

</style>