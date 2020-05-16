import aboutApi from "../../api/about";


export default {
    state: {
        profileDetails: userProfile === null ? null : userProfile.profileDetail
    },

    mutations: {
        editLookMutations(state, detailsLook) {
            state.profileDetails.look = detailsLook.look
        },
        editTextAboutMutations(state, obj) {
            state.profileDetails[obj.name] = obj.profileDet
        },
    },

    actions: {
        async editLook({commit}, look) {
            const result = await aboutApi.update(look)
            const data = await result.json()
            if (result.ok) {
                commit('editLookMutations', data)
            }
        },
        async editAbout({commit}, obj) {
            const result = await aboutApi.update(obj.profileDet)
            const data = await result.json()
            obj.profileDet = data[obj.name]
            if (result.ok) {
                commit('editTextAboutMutations', obj)
            }
        },
    }
}