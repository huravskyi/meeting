import Vue from "vue";

const chat = Vue.resource('/chat{/id}')
const chatBlock = Vue.resource('/chatBlock')
const messages = Vue.resource('/messages{/id}')
const newListMessage = Vue.resource('/messages/new-list')
const oldMessages = Vue.resource('/messages/old')
const newMessages = Vue.resource('/messages/new')
const messageIds = Vue.resource('/messages/messageIds')
const messageDelivered = Vue.resource('/messages/delivered{/id}')


export default {
    getNewListMessage: id => newListMessage.get({chat: id}),
    add: newChat => chat.save({}, newChat),
    getBlockChats: chats => chatBlock.get(),
    getChats: chats => chat.get(),
    save: message => messages.save({}, message),
    updateDelivered: message => messageDelivered.update({id: message.id}, {}),
    getMessagesForPage: obj => messages.get(
        {page: obj.page, chat: obj.chat.id, numberOfNewMessage: obj.chat.numberOfNewMessage}),
    setViewed: chat => messageIds.save({}, chat),
    downloadOldMessage: obj => oldMessages.get({page: obj.page, chat: obj.chat.id, idMessage: obj.idMessage}),
    downloadNewMessage: obj => newMessages.get({page: obj.page, chat: obj.chat.id})


}