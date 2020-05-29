<template>
    <v-expansion-panel>
        <v-expansion-panel-header disable-icon-rotate @click="editing" class="headerHeight">
            <span class="title">{{description.titleAbout}}</span>
            <template v-slot:actions>
                <v-btn fab
                       small
                >
                    <v-icon v-if="isEditing.about">mdi-close</v-icon>
                    <v-icon v-else>mdi-pencil</v-icon>
                </v-btn>
            </template>
        </v-expansion-panel-header>
        <p class="subtitle-1  px-6" style="word-break: break-all; opacity: 0.9"
           v-if="(profileDetails) && (!isEditing.about)">
            {{profileDetails.textAbout}}</p>
        <v-expansion-panel-content>
            <v-text-field v-model="textAbout"
                        @keyup.enter.exact=" $emit('close-panel', []), save()"
            ></v-text-field>

            <v-card-actions>
                <div class="flex-grow-1"></div>
                <v-btn
                        color="blue lighten-1"
                        @click=" $emit('close-panel', []), save()"
                >
                    Сохранить
                </v-btn>
            </v-card-actions>
        </v-expansion-panel-content>
    </v-expansion-panel>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        props: ['isEditing', 'description', 'profileDetails'],
        name: "AboutMe",
        data: () => ({
            textAbout: '',
        }),
        mounted() {
            if (this.profileDetails.textAbout)
                this.textAbout = JSON.parse(JSON.stringify(this.profileDetails.textAbout))
        },
        methods: {
            ...mapActions(['editAbout']),

            editing() {
                this.isEditing.about = !this.isEditing.about;
            },
            save() {
                if (this.textAbout !== this.profileDetails.textAbout) {
                    this.isEditing.about = false;
                    const obj = {
                        name: 'textAbout',
                        profileDet: {
                            id: this.profileDetails.id,
                            textAbout: this.textAbout
                        }
                    };
                    this.editAbout(obj)
                }
            }
        },
    }
</script>

<style scoped>
    .headerHeight {
        min-height: 90px;
    }
</style>