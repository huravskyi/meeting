<template>
    <v-card width="100%">
        <v-card-title>
            <span class="title">{{description.look}}</span>
        </v-card-title>
        <p class="subtitle-1  px-6" style="opacity: 0.9" v-if="profileDetailGuest">
            {{getLook()}}
        </p>
    </v-card>
</template>

<script>
    export default {
        props: ['description', 'profileDetailGuest'],
        name: "LookGuest",
        model: [],
        data: () => ({
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
        methods: {
            getLook() {
                this.model = JSON.parse(JSON.stringify(this.profileDetailGuest.look))

                let look = ''
                let tmp = 0
                for (let key in this.profileDetailGuest.look) {
                    if (key !== 'height' && key !== 'weight') {
                        let index = this.getIndex(this.profileDetailGuest.look[key], tmp)
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
                return  look!==''? look.trim().slice(0, -1) + '.':''
            },
            getIndex(item, ind) {
                return this.looks[ind].items.findIndex(i => i === item)

            }
        }

    }
</script>

<style scoped>

</style>