import Vue from "vue";

const usersListViews = Vue.resource('/userView/usersListViews')
const deleteView = Vue.resource('/userView/clean-viewed{/id}')

export default {
    getUsersListWhoViews: page => usersListViews.get({page: page}),
    cleanViewed: id => deleteView.delete({id})
}