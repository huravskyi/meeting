<template>
    <v-btn class="menu_position" icon dark v-if="image.main">
        <v-icon>how_to_reg</v-icon>
    </v-btn>

    <v-menu v-else-if="myProfile"
            radius
            absolute
            top
            left
            transition="slide-x-transition"
    >
        <template v-slot:activator="{ on }">
            <v-btn dark
                   icon
                   v-on="on"
                   class="menu_position"
            >
                <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
        </template>

        <v-list>
            <v-list-item
                    v-for="(item, i) in menuItems"
                    :key="i"
                    @click="imageAction(item.action, image)"
                    class="title_menu"
            >
                <v-list-item-icon>
                    <v-icon class="icon_menu">{{item.icon}}</v-icon>
                </v-list-item-icon>
                <v-list-item-title>{{image.hide?"Открыть фото":item.title}}</v-list-item-title>
            </v-list-item>
        </v-list>
    </v-menu>
</template>

<script>
    export default {
        props: ['image', 'imageAction', 'myProfile'],
        name: "MenuImage",
        data: () => ({
            menuItems: [
                {title: "Сделать главным", icon: 'how_to_reg', action: 'main'},
                {title: "Скрыть фото", icon: 'lock', action: 'hide'},
                {title: "Удалить фото", icon: 'delete_forever', action: 'del'},

            ]
        }),
    }
</script>

<style scoped>
    .title_menu:hover .icon_menu {
        color: #0a70ff;
    }

    .menu_position {
        left: 72px;
        top: 70px;
        background: black;
        opacity: 0.4;
    }

    .menu_position:hover {
        opacity: 0.8;
    }
</style>