<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">

    <v-app app>

        <navigation></navigation>

        <v-content>
            <router-view></router-view>
        </v-content>
    </v-app>
</template>

<script>
    import Navigation from "./Navigation.vue";


    export default {
        name: "App",
        components: {Navigation},
        data() {
            return {

            }
        }
    }
</script>

<style scoped>
</style>