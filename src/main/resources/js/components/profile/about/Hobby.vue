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
        <div class="subtitle-1 d-flex flex-wrap px-6 py-2" style="opacity: 0.9" v-if="(profileDetails) && (!isEditing.hobby)">
            <div v-for="(textChip, inx) in checkBox"
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
    import {mapActions} from 'vuex'

    export default {
        props: ['isEditing', 'description', 'profileDetails'],
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
                return this.checkBox =this.profileDetails.hobby!==null? this.profileDetails.hobby.split(", "):[]
            }
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