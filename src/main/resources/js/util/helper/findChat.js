export default function  findChat(chats, chat) {
    let index = undefined
    for (let i = 0; i < chats.length; i++) {
        let count = 0;
        for (let j = 0; j < chats[i].members.length; j++) {
            for (let k = 0; k < chat.members.length; k++) {
                if (chats[i].members[j].id === chat.members[k].id) {
                    count++
                }
            }
        }
        if (count > 1) {
            index = i
            break
        }
    }
    return index
}