import Vue from 'vue'

const postLogins = Vue.resource('/login')

export default {
    post: postLogin => Vue.http.post('/login', postLogin)
}