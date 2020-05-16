import Vue from "vue";

const images = Vue.resource('/image/photo{/id}')
const imagesForAdmin = Vue.resource('/adminUser/images{/id}')
const deleteAndWarning = Vue.resource('/adminUser/deleteAndWarning')
const  toApprove = Vue.resource('/adminUser/deleteAndWarning')
const  imgByUser = Vue.resource('/adminUser/images-by-user')


export default {
    addImg: image => images.save({}, image),
    remove: image => images.remove({id: image.id}),
    editStatus: image => images.update({id: image.id}, image),
    getImageForAdmin: action => imagesForAdmin.get({event: action.event, page: action.page}),
    deleteAndWarningApi: image => deleteAndWarning.delete({image: image.id, user: image.user}),
    toApproveApi: event => imagesForAdmin.update({id:event.id}, event.tested),
    getImageByApi: id => imgByUser.get({user:id})

}