import userApi from "../../api/user"


export default {
    state: {
        userProfile,
    },

    mutations: {
        setProfileLikeNewMutation(state, item) {
            state.userProfile.likeNew = item
        },
        updateNameAndCityMutations(state, newProfile) {
            state.userProfile.username = newProfile.username
            state.userProfile.birthDate = newProfile.birthDate
            state.userProfile.country = newProfile.country
            state.userProfile.city = newProfile.city
            state.userProfile.region = newProfile.region
        },
    },
    actions: {
        async deleteAccountAction({commit}, id) {
            const result = await userApi.accountDelete(id)
            return !!result.ok;
        },
        async updateUserByAdminAction({commit}, user) {
            const result = await userApi.updateUserByAdmin(user)
            const data = await result.json()
            if (result.ok) {
                return data
            }
        },
        async updateNameAndCityActions({commit}, user) {
            const result = await userApi.update(user)
            const data = await result.json()
            if (result.ok) {
                commit('updateNameAndCityMutations', data)
                return true
            } else {
                return false
            }
        },
        async changePasswordOrEmail({commit}, user) {
            const result = await userApi.updatePasswordOrEmail(user)
            const data = await result.json()
            if (result.ok) {
                return data.id !== null;
            }
        },
        async sendToEmailAction() {
            await userApi.sendEmail()
                return true
        }
    },
}