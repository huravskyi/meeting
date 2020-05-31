<template>
    <v-card width="100%">
        <v-card-title>
            <span class="title">{{description.targets}}</span>
        </v-card-title>
        <p class="subtitle-1  px-6" style="opacity: 0.9" v-if="profileDetailGuest">
            {{profileDetailGuest.target !==null?profileDetailGuest.target +'.':''}}
        </p>
    </v-card>
</template>

<script>
    export default {
        props: ['description', 'profileDetailGuest'],
        name: "TargetGuest",
    }
</script>

<style scoped>

</style>