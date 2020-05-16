<template>
    <v-card width="100%">
        <v-card-title>
            <span class="title">{{description.hobby}}</span>
        </v-card-title>
        <p class="subtitle-1  px-6" style="opacity: 0.9" v-if="profileUserGuest">
            {{profileUserGuest.profileDetail.hobby}}
        </p>
    </v-card>
</template>

<script>
    import {mapState} from "vuex";

    export default {
        props: ['description'],
        name: "HobbyGuest",
        computed: {
            ...mapState({profileUserGuest: state => state.storeUserGuest.userGuest})
        }
    }
</script>

<style scoped>

</style>