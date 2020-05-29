<template>
    <v-expansion-panel>
        <v-expansion-panel-header disable-icon-rotate @click="editingTarget" class="headerHeight">
            <span class="title"> {{description.targets}}</span>
            <template v-slot:actions>
                <v-btn fab small>
                    <v-icon v-if="isEditing.target">mdi-close</v-icon>
                    <v-icon v-else>mdi-pencil</v-icon>
                </v-btn>
            </template>
        </v-expansion-panel-header>
        <p class="subtitle-1  px-6" style="opacity: 0.9" v-if="(profileDetails) && (!isEditing.target)">
            {{profileDetails.target}}</p>
        <v-expansion-panel-content
        >
            <v-row>
                <v-col xs="12"
                       md="6"
                       lg="6"
                       v-for="(item, i) in valueCheckBox"
                       :key="i"
                       class="colCheckBox"
                >
                    <div style="width: 250px">
                        <v-checkbox  v-model="checkBox" :value="item.value">
                            <template v-slot:label>
                                <div class="check-box-custom">
                                    <span class="font-weight-regular">{{item.value}}</span>
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
                        @click="$emit('close-panel', []),  save()"
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
        name: "Target",
        data: () => ({
            form: '',
            checkBox: [],
            valueCheckBox: [
                {value: 'Дружбы / общения'},
                {value: 'Создания семьи'},
                {value: 'Постоянные отношения'},
                {value: 'Провести вечер'},
                {value: 'Путешествовать вместе'},
                {value: 'Ищу спонсора'},
            ],
        }),

        mounted() {
            if (this.profileDetails.target) {
                this.checkBox = this.profileDetails.target.split(", ")
            }
        },
        methods: {
            ...mapActions(['editAbout']),

            save() {
                this.isEditing.target = false

                const target = this.checkBox.join(", ")
                if (target !== this.profileDetails.target) {

                    const obj = {
                        name: 'target',
                        profileDet: {
                            id: this.profileDetails.id,
                            target: target
                        }
                    }
                    this.editAbout(obj)
                }
            },
            editingTarget() {
                this.isEditing.target = !this.isEditing.target;

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