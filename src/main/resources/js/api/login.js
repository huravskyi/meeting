import Vue from 'vue'

export default {
    post: form => Vue.http.post('/registrationUser', form)
}