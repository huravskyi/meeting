<template>
    <v-card width="100%">
        <v-card-title>
            <span class="title">{{description.personal}}</span>
        </v-card-title>
        <p class="subtitle-1  px-6" style="opacity: 0.9" v-if="profileDetailGuest">
            {{getChoice()}}
        </p>
    </v-card>
</template>

<script>
    export default {
        props: ['description', 'profileDetailGuest'],
        name: "PersonalInformationGuest",
        methods: {
            getChoice() {
                if (this.profileDetailGuest.personalInformation) {
                    const model = this.profileDetailGuest.personalInformation.split(", ")
                    let textPersonal = ''
                    model.forEach(value => {
                        if (value !== '') {
                            textPersonal += value + ', '
                        }
                    })
                    textPersonal = textPersonal.trim().slice(0, -1) + '.'
                    return textPersonal
                }
            },
        }
    }
</script>

<style scoped>

</style>