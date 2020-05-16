import imageApi from "../../api/image";
import userProf from "../modules/storeUserProfile"

export default {
    state: {
        images: userProfile === null ? null : userProfile.images
    },

    mutations: {
        addImageMutations(state, image) {
            state.images.push(image)
            if (image.main === true) {
                userProf.state.userProfile.userpic = image.urlLink
            }
        },
        delImageMutations(state, image) {
            const index = state.images.findIndex(item => item.id === image.id)
            state.images = [
                ...state.images.slice(0, index),
                ...state.images.slice(index + 1)
            ]
        },
        editMainMutations(state, image) {
            let index = state.images.findIndex(item => item.main === true);
            state.images[index].main = false;

            index = state.images.findIndex(item => item.id === image.id);
            state.images[index].main = image.main
            state.images[index].hide = image.hide
            userProf.state.userProfile.userpic = image.urlLink

        },
        changeHideMutations(state, image) {
            let index = state.images.findIndex(item => item.id === image.id);
            state.images[index].hide = image.hide
        }
    },

    actions: {
        async getImageByIdAction({commit}, id){
            const result = await imageApi.getImageByApi(id)
            const data = await result.json()
            if (result.ok) {
                return data
            }
        },
        async toApproveAction({commit}, event){
            const result = await imageApi.toApproveApi(event)
            const data = await result.json()
            if (result.ok) {
                return data
            }
        },
        async deleteAndWarningAction({commit},image){
            const result = await imageApi.deleteAndWarningApi(image)
            const data = await result.json()
            if (result.ok) {
                return  data
            }
        },
        async getImageAction({commit},action){
            const result = await imageApi.getImageForAdmin(action)
            const data = await result.json()
            if (result.ok) {
                return  data
            }
        },

        async addImageAction({commit}, imageFile) {
            const result = await imageApi.addImg(imageFile)
            const data = await result.json()
            if (result.ok) {
                if (data.id !== null) {
                    commit('addImageMutations', data)
                    return true
                } else {
                    return false
                }
            }

        },
        async delImageAction({commit}, image) {
            const result = await imageApi.remove(image)
            if (result.ok) {
                commit('delImageMutations', image)
            }

        },

        async editMainAction({commit}, image) {
            const result = await imageApi.editStatus(image)
            const data = await result.json()
            if (result.ok)
                commit('editMainMutations', data)
        },
        async changeHideStatusAction({commit}, image) {
            const result = await imageApi.editStatus(image)
            const data = await result.json()
            if (result.ok)
                commit('changeHideMutations', data)
        },

    }
}