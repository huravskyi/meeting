export default function setMessageToChat(chats, obj, add) {
    const index = chats.findIndex(item => item.id === obj.chatId)
    const targetMessages = chats[index].messages
        .concat(obj.data.messages)
        .reduce((res, val) => {
            res[val.id] = val
            return res
        }, {})
    chats[index].messages = Object.values(targetMessages)

    if (add === 'new' || add === 'all') {
        chats[index].currentPageNotViewed = obj.data.currentPageNotViewed
        chats[index].totalPageNotViewed = obj.data.totalPageNotViewed
    }
    if (add === 'old' || add === 'all') {
        chats[index].currentPage = obj.data.currentPage
        chats[index].totalPage = obj.data.totalPage
    }
    return chats
}