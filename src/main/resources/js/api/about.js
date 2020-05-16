import Vue from "vue";

const about = Vue.resource('/profileDetail/about{/id}')
const profile = Vue.resource('/profileDetail{/id}')

export default {
    update: object => about.update({id: object.id}, object ),
    get: id => profile.get({id}),
}
