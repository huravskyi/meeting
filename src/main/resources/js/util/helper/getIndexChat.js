
export default function getIndexForChatBlock(chatsList, user) {
    let indexChat
    _.forEach(chatsList, (element, index) => {
        let count = 0
        element.members.forEach(itemUser => {
            if (itemUser.id === user.id || itemUser.id === userProfile.id) {
                count++
            }
        })
        if (count > 1) {
            indexChat = index
             return false
        }
    })
    return indexChat
}
