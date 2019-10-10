import Vue from 'vue'
import '@babel/polyfill'
import 'api/resource'
import store  from "./store/store";
import VueResource from 'vue-resource'
import App from 'pages/App.vue'
import router from 'router/router'
import vuetify from 'plugins/vuetify' // path to vuetify export





Vue.use(VueResource)



new Vue({
    store,
    vuetify,
    router,
    el: '#app',
    render: a => a(App)
})