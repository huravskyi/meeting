import Vue from 'vue'
import '@babel/polyfill'
import 'api/resource'
import store  from "./store/store"
import {connect} from "./util/ws"
import VueResource from 'vue-resource'
import App from '../js/pages/App.vue'
import router from '../js/router/router'
import vuetify from '../js/plugins/vuetify' // path to vuetify export
import  {Cropper} from 'vue-advanced-cropper'
import VueLodash from 'vue-lodash'
import lodash from 'lodash'


Vue.use(VueResource)
Vue.component(Cropper)

// const options = { name: 'lodash' } // customize the way you want to call it
const options = { name: 'custom' , lodash: lodash } // customize the way you want to call it

Vue.use(VueLodash, options) // options is optional

if(userProfile)
connect()

new Vue({
    store,
    vuetify,
    router,
    el: '#app',
    render: a => a(App)
})