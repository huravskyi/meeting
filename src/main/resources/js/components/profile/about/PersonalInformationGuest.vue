<template>
    <v-card width="100%">
        <v-card-title>
            <span class="title">{{description.personal}}</span>
        </v-card-title>
        <p class="subtitle-1  px-6" style="opacity: 0.9" v-if="profileUserGuest">
            {{getChoice()}}
        </p>
    </v-card>
</template>

<script>
    import {mapState} from "vuex";

    export default {
        props: ['description'],
        name: "PersonalInformationGuest",
        computed: {
            ...mapState({profileUserGuest: state => state.storeUserGuest.userGuest})
        },
        methods: {
            getChoice() {
                if (this.profileUserGuest.profileDetail.personalInformation)
                    return this.profileUserGuest.profileDetail.personalInformation.replace(/^,| , /g, "  ")
            },
        }
    }
</script>

<style scoped>

</style>