<template>
    <v-row align="center" justify="start" style="min-height: 80px; background-color: #f5f6fa">
        <v-col :style="'display:'+displayColBlock" cols="4"></v-col>
        <v-col>
            <v-row style="" class="chat-write">
                <div
                        data-placeholder-text="Введите сообщение"
                        id="chatMessTextarea"
                        contenteditable="true"
                        style="background-color: white; justify-content: start!important;"
                        class="align-center justify-center chat-write__text"
                >
                </div>
                <div class="chat-write__btn chat-write__btn--text">
                    <choose-smile></choose-smile>
                </div>
            </v-row>
        </v-col>
        <v-col :sm="sm"
               :md="md"
               :class="classCol"
               :cols="cols"
        >
            <div style="position: relative; bottom: 2px">
                <v-btn
                        x-large
                        icon
                        color="#0a70ff"
                        @click="sendMessageAndSmile()"
                >
                    <v-icon size="50">mdi-send</v-icon>
                </v-btn>
            </div>
        </v-col>
    </v-row>
</template>

<script>
    import ChooseSmile from "./ChooseSmile.vue";
    import showSmile from "../../util/helper/showSmile";

    export default {
        components: {ChooseSmile},
        props: ['sendMessage', 'displayColBlock', 'sm', 'md', 'classCol', 'cols'],
        name: "PastSmileAndSendMessage",

        mounted() {
            chatMessTextarea.addEventListener('paste', this.onPaste);
        },
        methods: {
            sendMessageAndSmile(){
                if(this.sendMessage(showSmile(null, chatMessTextarea, 'send', false)))
                    chatMessTextarea.innerHTML = ''
            },
            onPaste(e) {
              showSmile(e, chatMessTextarea, 'past', true)
            },
        }
    }
</script>

<style scoped>
    @import url('https://fonts.googleapis.com/css2?family=Open+Sans&display=swap');

    .chat-write__btn--text {
        right: 5px;
    }

    .chat-write__btn {
        position: absolute;
        bottom: 0;
        line-height: 3.125rem;
    }

    .chat-write {
        position: relative;
        display: inline-block;
        width: 100%;
    }

    .chat-write__text {

        overflow: auto;
        min-height: 50px;
        max-height: 66px;
        margin-left: 10px;
        margin-top: 5px;
        padding: 15px 42px 15px 10px;
        resize: none;
        cursor: text;
        text-align: left;
        background-color: #fefefe;
        line-height: 1.25;
        -webkit-overflow-scrolling: touch;
        word-break: break-word !important;
        -webkit-hyphens: auto;
        -ms-hyphens: auto;
        hyphens: auto;
        word-wrap: break-word;
        font-family: 'Open Sans', sans-serif;;
        font-weight: 400;
        font-size: larger;
    }

    .chat-write__text:empty:before {
        content: attr(data-placeholder-text);
        font-size: 17px;
        color: #777;
    }

</style>