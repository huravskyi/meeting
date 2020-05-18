export default function setUsers(state, userDto) {
    if (state === null || state.length === 0) {
        state = userDto
    } else {
        let targetMessages = state.listFromDto
            .concat(userDto.listFromDto)
            .reduce((res, val) => {
                res[val.id] = val
                return res
            }, {})
        targetMessages = Object.values(targetMessages)
        targetMessages.sort((a, b) => -(a.id - b.id))
        state.listFromDto = Object.values(targetMessages)
        state.currentPage = userDto.currentPage
        state.totalPage = userDto.totalPage
    }
    return state
}