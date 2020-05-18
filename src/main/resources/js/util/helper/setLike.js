import Vue from 'vue'

export default function setLike (state, userDto) {
    if (state.listFromDto) {
        const index = state.listFromDto.findIndex(item => item.id === userDto.id)
        if (index !== -1) {
            const user = state.listFromDto[index]
            user.meLiked = userDto.meLiked
            Vue.set(state.listFromDto, index, user)
        }
    }
}