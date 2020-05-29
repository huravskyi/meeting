import Vue from 'vue'
import VueRouter from 'vue-router'

// import Login from '../pages/Login.vue'
// import Registration from '../pages/Registration.vue'
// import ForgotPassword from '../pages/ForgotPassword.vue'
// import Profile from '../pages/Profile.vue'
// import Settings from '../pages/Settings.vue'
// import Search from '../pages/Search.vue'
// import Message from '../pages/Message.vue'
// import PageViews from "../pages/PageViews.vue";
// import Sympathy from "../pages/Sympathy.vue";
// import NotFoundPage from "../pages/NotFoundPage.vue";
// import AdminPanel from "../pages/AdminPanel.vue";

const Login = () => import('../pages/Login.vue')
const Registration  = () => import( '../pages/Registration.vue')
const ForgotPassword  = () => import( '../pages/ForgotPassword.vue')
const Profile  = () => import( '../pages/Profile.vue')
const Settings  = () => import( '../pages/Settings.vue')
const Search  = () => import( '../pages/Search.vue')
const Message  = () => import( '../pages/Message.vue')
const PageViews  = () => import( "../pages/PageViews.vue")
const Sympathy  = () => import( "../pages/Sympathy.vue")
const NotFoundPage  = () => import( "../pages/NotFoundPage.vue")
const AdminPanel  = () => import( "../pages/AdminPanel.vue")

Vue.use(VueRouter);


const routes = [
    {path: '/login/PreviewImage.vue.map', redirect: '/'}, // ??
    {path: '/PreviewImage.vue.map', redirect: '/'}, // ??
    {path: '/activate/registration/*', redirect: '/'}, // ??

    {path: '/login', component: Login},
    {path: '/registration', component: Registration},
    {path: '/login/forgotPassword', component: ForgotPassword},
    {path: '/profile/:id?', component: Profile},
    {path: '/settings', component: Settings},
    {path: '/search', component: Search},
    {path: '/message', component: Message},
    {path: '/views', component: PageViews},
    {path: '/sympathy', component: Sympathy},
    {path: '/admin', component: AdminPanel},

    {path: '/', component: Search},
    {path: '*', component: NotFoundPage},
    {path: '/logout', component: Login},

];

export default new VueRouter({
    mode: 'history',
    routes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        } else {
            return {x: 0, y: 0}
        }
    }
})