<template>
    <v-expansion-panel>
        <v-expansion-panel-header disable-icon-rotate @click="editingTarget" class="headerHeight">
            <span class="title"> {{description.hobby}}</span>
            <template v-slot:actions>
                <v-btn fab small>
                    <v-icon v-if="isEditing.hobby">mdi-close</v-icon>
                    <v-icon v-else>mdi-pencil</v-icon>
                </v-btn>
            </template>
        </v-expansion-panel-header>
        <p class="subtitle-1  px-6" style="opacity: 0.9" v-if="(profileDetails) && (!isEditing.hobby)">
            {{profileDetails.hobby}}</p>

        <v-expansion-panel-content
        >
            <v-row>
                <v-col
                        xs="12"
                        md="6"
                        lg="6"
                        v-for="(item, i) in valueCheckBox"
                        :key="i"
                        class="colCheckBox"
                >
                    <div style="width: 250px">
                        <v-checkbox v-model="checkBox" :value="item.value">
                            <template v-slot:label>
                                <div class="check-box-custom">
                                    <span class="font-weight-regular ">{{item.value}}</span>
                                </div>
                            </template>
                        </v-checkbox>
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
        props: ['isEditing', 'description'],
        name: "Hobby",
        data: () => ({
            checkBox: [],
            valueCheckBox: [
                {value: 'Спорт'},
                {value: 'IT'},
                {value: 'Финансы и инвестиции'},
                {value: 'Наука'},
                {value: 'Путешествия'},
                {value: 'Бары и рестораны'},
                {value: 'Искусство'},
                {value: 'Экстрим'},
                {value: 'Кино'},
                {value: 'Музыка'},
                {value: 'Литература'},
                {value: 'Шопинг'},
                {value: 'Танцы'},
                {value: 'Машины'},
                {value: 'Кулинария'},

            ],
        }),
        mounted() {
            if (this.profileDetails.hobby) {
                this.checkBox = this.profileDetails.hobby.split(", ")
            }
        },
        computed: {
            ...mapState({
                profileDetails: state => state.storeProfileDetails.profileDetails
            }),
        },
        methods: {
            ...mapActions(['editAbout']),
            save() {
                this.isEditing.hobby = false

                const hobby = this.checkBox.join(', ')
                const obj = {
                    name: 'hobby',

                    profileDet: {
                        id: this.profileDetails.id,
                        hobby
                    }
                }
                this.editAbout(obj)
            },
            editingTarget() {
                if (!this.isEditing.target) {
                    this.isEditing.target = true
                } else {
                    this.isEditing.target = false
                }

            },
        }
    }
</script>

<style scoped>
    .colCheckBox {
        height: 40px;
    }
    .headerHeight {
        min-height: 90px;
    }
</style>