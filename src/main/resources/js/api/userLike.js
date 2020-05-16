import Vue from "vue";
const coincidence = Vue.resource('/userLike/coincidence')
const myLikes = Vue.resource('/userLike/myLikes')
const like = Vue.resource('/userLike/like{/id}')



export default {
    setLikeUser: userDto => like.update({id: userDto.id}, userDto),
    getUserCoincidence: page => coincidence.get({page: page}),
    getMyLikeUsers: page => myLikes.get({page: page}),
}
