<template>
    <v-card width="100%">
        <v-card-title>
            <span class="title">{{description.hobby}}</span>
        </v-card-title>
        <div class="subtitle-1 d-flex flex-wrap px-6 py-2" style="opacity: 0.9" v-if="profileDetailGuest">
            <div v-for="(textChip, inx) in getCheckBox"
            >
                <v-chip :key="inx"
                        class="ma-2"
                        color="primary"
                        outlined
                >
                    {{textChip}}
                </v-chip>
            </div>
        </div>
    </v-card>
</template>

<script>
    export default {
        props: ['description', 'profileDetailGuest'],
        name: "HobbyGuest",
        data:()=>({
            checkBox:[]
        }),
        computed: {
            getCheckBox() {
                if (this.profileDetailGuest)
                return this.checkBox = this.profileDetailGuest.hobby.split(", ")
            }
        },
    }
</script>

<style scoped>

</style>