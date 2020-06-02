import Vue from 'vue'
import '@babel/polyfill'
import 'api/resource'
import store from "./store/store"
import {connect} from "./util/ws"
import VueResource from 'vue-resource'
import App from '../js/pages/App.vue'
import router from '../js/router/router'
import vuetify from '../js/plugins/vuetify'

Vue.use(VueResource)

if (userProfile)
    setTimeout(()=>{
        connect()
    },5000)

new Vue({
    store,
    vuetify,
    router,
    el: '#app',
    render: a => a(App)
})