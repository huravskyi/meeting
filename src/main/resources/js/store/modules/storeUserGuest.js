import Vue from 'vue'
import about from "../../api/about";
import userApi from "../../api/user";
import userLikeApi from "../../api/userLike";
import userView from "../../api/userView";
import setLike from "../../util/helper/setLike";
import setUsers from "../../util/helper/setUsers";


export default {
    state: {
        userGuest: null,
        usersListWhoViews: null,
        users: [],
        userCoincidence: [],
        usersLikeMy: [],
    },
    mutations: {
        getUsersWhoViewsMutations(state, userViewDto) {
            state.usersListWhoViews = setUsers(state.usersListWhoViews, userViewDto)
            // if (state.usersListWhoViews == null) {
            //     state.usersListWhoViews = userViewDto
            // } else {
            //     let targetMessages = state.usersListWhoViews.listFromDto
            //         .concat(userViewDto.listFromDto)
            //         .reduce((res, val) => {
            //             res[val.id] = val
            //             return res
            //         }, {})
            //     targetMessages = Object.values(targetMessages)
            //     targetMessages.sort((a, b) => -(a.id - b.id))
            //     state.usersListWhoViews.listFromDto = Object.values(targetMessages)
            //     state.usersListWhoViews.currentPage = userViewDto.currentPage
            //     state.usersListWhoViews.totalPage = userViewDto.totalPage
            // }
        },
        setMyUsersFirstMutation(state, users) {
            state.users = users
        },
        setMyUsersMutation(state, users) {
            state.users = setUsers(state.users, users)
        },
        setUserGuestMutation(state, user) {
            state.userGuest = user
        },
        setLikeMutation(state, userDto) {
            setLike(state.users, userDto)
            setLike(state.usersLikeMy, userDto)
            setLike(state.userCoincidence, userDto)

            if (state.userGuest !== null) {
                if (state.userGuest.id === userDto.id) Vue.set(state.userGuest, 'meLiked', userDto.meLiked)
            }
        },
        setUserCoincidenceMutation(state, userCoincidence) {
            state.userCoincidence = setUsers(state.userCoincidence, userCoincidence)
        },
        setUserLikeMyMutation(state, usersLikeMy) {
            state.usersLikeMy = setUsers(state.usersLikeMy, usersLikeMy)
        },
        cleanUserViewedMutation(state, data){
            if(data)state.usersListWhoViews =null
        }
    },
    actions: {
        async cleanUserViewedAction({commit}, id) {
            const result = await userView.cleanViewed(id)
            const data = await result.json()
            if (result.ok) {
                commit('cleanUserViewedMutation', data)
            }
        },
        async getUsersMyLikeAction({commit}, page) {
            const result = await userLikeApi.getMyLikeUsers(page)
            const data = await result.json()
            if (result.ok) {
                commit('setUserLikeMyMutation', data)
                return true
            }
        },
        async getUsersCoincidenceAction({commit}, page) {
            const result = await userLikeApi.getUserCoincidence(page)
            const data = await result.json()
            if (result.ok) {
                commit('setUserCoincidenceMutation', data)
                return true
            }
        },
        async setLikeAction({commit}, userDto) {
            const result = await userLikeApi.setLikeUser(userDto)
            const data = await result.json()
            if (!result.ok) {
                userDto.meLiked = !userDto.meLiked
                commit('setLikeMutation', userDto)
                return false
            } else {
                commit('setLikeMutation', userDto)
                return true
            }
        },
        async getUsersListAction({commit}, page) {
            const result = await userView.getUsersListWhoViews(page)
            const data = await result.json()
            if (result.ok) {
                commit('getUsersWhoViewsMutations', data)
            }
        },
        async setProfileUserGuestAction({commit}, userId) {
            const result = await about.get(userId)
            const data = await result.json()
            if (result.ok) {
                commit('setUserGuestMutation', data)
            }
        },
        async getListUsersAction({commit}, userPage) {
            const result = await userApi.getUsers(userPage)
            const data = await result.json()
            if (result.ok) {
                if (userPage.page === 0) {
                    commit('setMyUsersFirstMutation', data)
                } else {
                    commit('setMyUsersMutation', data)
                }
            }
        },

    }
}