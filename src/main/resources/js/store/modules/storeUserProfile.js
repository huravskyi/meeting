import userApi from "../../api/user"


export default {
    state: {
        isMobile:false,
        userProfile,
        accountPreview: ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/accountPreview.png?alt=media&token=8c1044c0-b371-4bf2-91e6-e0e7daf87c87'),
        accountPreviewMin:('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/accountPreview-min.png?alt=media&token=209837ce-9ee9-47a7-ab45-7ba39d551f82')
    },

    mutations: {
        logoutUserMutation(state, user){
            state.userProfile = user
        },
        setIsMobileMutation(state, isMobile){
            state.isMobile = isMobile
        },
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