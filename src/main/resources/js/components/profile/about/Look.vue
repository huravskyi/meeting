<template>
    <v-expansion-panel>
        <v-expansion-panel-header disable-icon-rotate @click="editing" class="headerHeight">
            <span class="title"> {{description.look}}</span>
            <template v-slot:actions>
                <v-btn fab small>
                    <v-icon v-if="isEditing.look">mdi-close</v-icon>
                    <v-icon v-else>mdi-pencil</v-icon>
                </v-btn>
            </template>
        </v-expansion-panel-header>
        <p class="subtitle-1  px-6" style="opacity: 0.9" v-if="!isEditing.look">
            {{getLook()}}
        </p>
        <v-expansion-panel-content
        >
            <v-row class="py-0">
                <v-col
                        class="py-0"
                >
                    <div v-for="(item, i) in looks"
                         :key="i">
                        <v-row justify="start" align="center">
                            <v-col
                                    class="py-0"
                                    justify="center"
                                    align="start"
                            >
                                <span class="font-weight-regular ">{{item.title}}</span>
                            </v-col>
                            <v-col
                                    class="py-0">
                                <v-select v-if="item.title !== 'Рост:' && item.title !== 'Вес:' "
                                          :items=" item.items"
                                          label="Выберите"
                                          v-model="model[item.name]"
                                ></v-select>

                                <v-slider v-if="item.title === 'Рост:'"
                                          v-model="model[item.name]"
                                          class="align-center"
                                          :max="hMax"
                                          :min="hMin"
                                          hide-details
                                          style="min-width: 270px"
                                >
                                    <template v-slot:append>
                                        <v-text-field
                                                v-model="model[item.name]"
                                                class="mt-0 pt-0"
                                                hide-details
                                                single-line
                                                type="number"
                                                style="width: 45px"
                                        ></v-text-field>
                                        <span style="padding-top: 6px">см</span>
                                    </template>
                                </v-slider>
                                <v-slider v-if="item.title === 'Вес:'"
                                          v-model="model[item.name]"
                                          class="align-center"
                                          :max="wMax"
                                          :min="wMin"
                                          hide-details
                                          style="min-width: 270px"
                                >
                                    <template v-slot:append>
                                        <v-text-field
                                                v-model="model[item.name]"
                                                class="mt-0 pt-0"
                                                hide-details
                                                single-line
                                                type="number"
                                                style="width: 45px"
                                        ></v-text-field>
                                        <span style="padding-top: 6px">кг</span>
                                    </template>
                                </v-slider>
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
    import {mapActions} from 'vuex'

    export default {
        props: ['isEditing', 'description', 'profileDetails'],
        name: "Look",
        data: () => ({
            model: [],
            modelLook: [],

            wMin: 40,
            wMax: 150,
            hMin: 130,
            hMax: 220,

            sliderHeight: 120,
            sliderWeight: 44,
            looks: [
                {title: "Цвет волос:", items: ['Темные', 'Светлые', 'Рыжие', 'Седые', 'Бритые наголо'], name: 'hair'},
                {title: "Рост:", items: [], name: 'height'},
                {title: "Вес:", items: [], name: 'weight'},
                {title: "Цвет глаз:", items: ['Голубые', 'Зеленые', 'Карие', 'Серые'], name: 'eyes'},
                {
                    title: "Телосложение:",
                    items: ['Обычное', 'Худощавое', 'Спортивное', 'Мускулистое', 'Плотное'],
                    name: 'body'
                },
                {
                    title: "Внешность:",
                    items: ['Европейская', 'Азиатская', 'Восточная', 'Африканская', 'Татарcкая'],
                    name: 'type'
                },
            ],
            looksBuild: [
                {title: "Волосы", items: ['темные', 'светлые', 'рыжие', 'седые', 'бритые наголо']},
                {title: "рост"},
                {title: "вес"},
                {title: "с", items: ['голубыми глазами', 'зеленыеми глазами', 'карими глазами', 'серыми глазами']},
                {title: "телосложения", items: ['обычного ', 'худощавого', 'спортивного', 'мускулистого', 'плотного']},
                {title: "внешности", items: ['европейской', 'азиатской', 'восточной', 'африканской', 'татарcкой']},
            ],
        }),
        mounted() {
            if (this.profileDetails.look)
            this.model = JSON.parse(JSON.stringify(this.profileDetails.look))
        },
        methods: {
            ...mapActions(['editLook']),

            editing() {
                this.isEditing.look = !this.isEditing.look;
            },
            save() {

                if (JSON.stringify(this.model) !== JSON.stringify(this.profileDetails.look)) {
                    const look = {
                        id: this.profileDetails.id,
                        look: {
                            hair: this.model.hair,
                            height: this.model.height,
                            weight: this.model.weight,
                            eyes: this.model.eyes,
                            body: this.model.body,
                            type: this.model.type,
                        }
                    }
                    this.editLook(look)
                }
            },
            getLook() {
                let look = ''
                let tmp = 0

                for (let key in this.profileDetails.look) {
                    if (key !== 'height' && key !== 'weight') {
                        let index = this.getIndex(this.profileDetails.look[key], tmp)
                        if (index !== -1) {
                            look += this.looksBuild[tmp].title + ' ' + this.looksBuild[tmp].items[index] + ', '
                        }
                    } else {
                        if (key === 'height') {
                            look += this.model[key] < 131 ? '' : this.model[key] + ' см, '
                        } else {
                            look += this.model[key] < 41 ? '' : this.model[key] + ' кг, '
                        }
                    }
                    tmp++
                }
                return look
            },
            getIndex(item, ind) {
                return this.looks[ind].items.findIndex(i => i === item)

            }
        }

    }
</script>

<style scoped>
    .headerHeight {
        min-height: 90px;
    }
</style>