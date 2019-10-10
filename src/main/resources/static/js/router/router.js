import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from 'pages/Login.vue'
import Registration from 'pages/Registration.vue'
import ForgotPassword from 'pages/ForgotPassword.vue'



Vue.use(VueRouter);


Vue.use(VueRouter);


const routes = [

    { path: '/login', component: Login },
    { path: '/registration', component: Registration },
    { path: '/login/forgotPassword', component: ForgotPassword },

];

export default new VueRouter({
    mode: 'history',
    routes
})