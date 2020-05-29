<template>
    <v-expansion-panel>
        <v-expansion-panel-header disable-icon-rotate @click="editing" class="headerHeight">
            <span class="title"> {{description.personal}}</span>
            <template v-slot:actions>
                <v-btn fab small>
                    <v-icon v-if="isEditing.personal">mdi-close</v-icon>
                    <v-icon v-else>mdi-pencil</v-icon>
                </v-btn>
            </template>
        </v-expansion-panel-header>
        <p class="subtitle-1  px-6" style="opacity: 0.9" v-if="(profileDetails) && (!isEditing.personal)">
            {{getChoice()}}</p>
        <v-expansion-panel-content
        >
            <v-row class="py-0">
                <v-col
                        class="py-0"
                >
                    <div v-for="(item, i) in items"
                         :key="i">
                        <v-row justify="start" align="center">
                            <v-col
                                    xs="4"
                                    sm="4"
                                    md="4"
                                    class="py-0"
                                    justify="center"
                                    align="start"
                            >
                                <span class="font-weight-regular ">{{item.title}}</span>
                            </v-col>
                            <v-col
                                    xs="8"
                                    sm="8"
                                    md="4"
                                    class="py-0">
                                <v-select
                                        :items=" item.items"
                                        label="Выберите"
                                        v-model="model[i]"
                                ></v-select>

                            </v-col>
                        </v-row>
                    </div>
                </v-col>
            </v-row>
            <v-card-actions>
                <div class="flex-grow-1"></div>
                <v-btn
                        color="blue lighten-1"
                        @click="$emit('close-panel', []), save()"
                >
                    Сохранить
                </v-btn>
            </v-card-actions>
        </v-expansion-panel-content>
    </v-expansion-panel>
</template>

<script>
    import {mapState, mapActions} from 'vuex'

    export default {
        props: ['isEditing', 'description', 'profileDetails'],
        name: "PersonalInformation",
        data: () => ({
            itemsPersonal: '',
            model: [],
            items: [
                {title: "Отношения:", items: []},
                {title: "Дети:", items: ['Нет детей', 'Есть дети']},
                {title: "Алкоголь:", items: ['Не пью', 'Иногда пью', 'Пью за компанию', 'Люблю выпить']},
                {title: "Курение:", items: ['Не курю', 'Иногда курю', 'Курю в компании', 'Курильщик']},
                {title: "Отношение к жизни:", items: ['Писимист', 'Оптимист', 'Реалист', 'Пофигист']},
            ],
        }),
        mounted() {
            if (this.profileDetails.personalInformation)
                this.model = this.profileDetails.personalInformation.split(", ")

            if (this.userProfile.gender ==='MALE') {
                this.items[0].items = ['Свободен', 'Женат', 'Разведеный']
            } else {
                this.items[0].items = ['Свободна', 'Замужем', 'Разведена']
            }
        },
        computed: {
            ...mapState({
                userProfile: state => state.storeUserProfile.userProfile
            }),

        },
        methods: {
            ...mapActions(['editAbout']),
            getChoice() {
                if (this.profileDetails.personalInformation)
                    return this.itemsPersonal = this.profileDetails.personalInformation.replace(/^,| , /g, "  ")
            },
            editing() {
                this.isEditing.personal = !this.isEditing.personal;

            },
            save() {
                this.isEditing.about = false

                const personalInformation = this.model.join(', ')
                if (this.profileDetails.personalInformation !== personalInformation) {
                    const obj = {
                        name: 'personalInformation',
                        profileDet: {
                            id: this.profileDetails.id,
                            personalInformation: personalInformation
                        }
                    }
                    this.editAbout(obj)
                }
            },
        },
    }
</script>

<style scoped>
    .headerHeight {
        min-height: 90px;
    }
</style>