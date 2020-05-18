export default function setMessage(chats, chat, index) {
    if (chats[index].messages.length === 0) {
        chats[index].currentPage = 0
        chats[index].totalPage = 2
        chats[index].numberOfNewMessage = chats[index].numberOfNewMessage + 1
    } else {
            if (chats[index].messages.currentPage === undefined) {
                chats[index].currentPage = 0
                chats[index].totalPage = 2
            }
            chats[index].numberOfNewMessage = chats[index].numberOfNewMessage + 1
            chats[index].messages.push(chat.messages[0])
    }
    return chats
}