import Vue from 'vue';
import Vuex from 'vuex';
import storeProfileDetails from "./modules/storeProfileDetails";
import storeImage from "./modules/storeImage";
import storeUserProfile from "./modules/storeUserProfile";
import storeUserGuest from "./modules/storeUserGuest";
import storeMessages from "./modules/storeMessages";

Vue.use(Vuex);

export default new Vuex.Store({
    modules:{
        storeProfileDetails,
        storeImage,
        storeUserProfile,
        storeUserGuest,
        storeMessages
    },
    state: {},
    mutations: {},
    actions: {},
    getters:{},
})