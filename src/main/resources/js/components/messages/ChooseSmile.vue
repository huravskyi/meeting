<template>
    <v-menu
            left
            transition="scroll-y-transition"
            v-model="value"
            close-on-click
            top
            offset-y
            radius
    >
        <template v-slot:activator="{ on }">
            <v-btn icon v-on="on">
                <v-icon size="40" style="opacity: 0.7">mood</v-icon>
            </v-btn>
        </template>
        <v-list class="smile_container">
            <template v-for="(smile, index) in smileHtml"
            >
                <v-btn icon height="46" width="46" @click="setSmile(smile)">
                    <div style="width: 36px; height: 36px; margin: 5px" :key="index" v-html="smile"></div>
                </v-btn>
            </template>
        </v-list>
    </v-menu>
</template>

<script>
    import showSmile from "../../util/helper/showSmile";

    export default {
        name: "ChooseSmile",
        data: () => ({

            itemsMenu: [
                '🙂', '😬', '😁', '😂', '😃', '😄', '😅', '😆', '😇', '😉', '😊', '🙃', '☺',
                '😋', '😌', '😍', '😘', ' 😗', '😙', '😚', '😜', '😝', '😛', '🤑', '🤓', '😎',
                '🤗', '😏', '😶', '😐', '😑', '😒', '🙄', '🤔', '😳', '😞', '😟', '😠', '😡',
                '😔', '😕', '🙁', '☹', '😣', ' 😖', '😫', '😩', '😤', '😮', '😱', '😨', '😰',
                '😯', '😦', '😧', '😢', '😥', '😪', '😓', '😭', '😵', ' 😲', '🤐', '😷', '🤒',
                '🤕', '😴',
            ],
            value: '',
            smileHtml: [],
        }),
        mounted() {
            this.itemsMenu.forEach(value => {
                let re = /26px/gi
                let smile = showSmile(value, 'chatMessTextarea', 'past', false)
                smile = smile.replace(re, 'inherit')
                this.smileHtml.push(smile)
            })
        },
        methods: {

            setSmile(smile) {
                let re = /inherit/gi
                smile = smile.replace(re, '26px')
                chatMessTextarea.innerHTML += smile
            }
        }
    }
</script>

<style scoped>
    .v-menu__content {
        background-color: white;
    }

    .smile_container {
        scroll-behavior: auto;
        display: flex;
        flex-wrap: wrap;
        width: 220px;
        height: 200px;
        padding: 10px 10px 10px 10px;
        background-color: #0a70ff;
    }
</style>