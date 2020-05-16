import Vue from 'vue'

const users = Vue.resource('/user{/id}')
const removeChat = Vue.resource('/user/removeChat{/id}')
const userBlock = Vue.resource('/user/block{/id}')
const usersEOP = Vue.resource('/user/emailOrPassword{/id}')
const sendEmail = Vue.resource('/registration/sendToMail')
const userForAdmin = Vue.resource('/adminUser/user')
const updateUserForAdmin = Vue.resource('/adminUser/user-update{/id}')
const getAdmin = Vue.resource('/adminUser/get-admin')
const del = Vue.resource('/user/delete{/id}')


export default {
    sendEmail: send => sendEmail.get(),
    removeUserChat: id => removeChat.remove({id}),
    updateBlock: id => userBlock.update({id: id}, id),
    update: user => users.update({id: user.id}, user),
    updatePasswordOrEmail: user => usersEOP.update({id: user.id}, user),
    getUsers: userPage => users.get(
        {
            ageMin: userPage.user.ageMin,
            ageMax: userPage.user.ageMax,
            gender: userPage.user.gender,
            page: userPage.page,
            localeType:userPage.user.localeType,
            localeName:userPage.user.localeName,
        }
    ),
    getUserForAdmin: form => userForAdmin.get(form),
    updateUserByAdmin: user => updateUserForAdmin.update({id: user.id}, user),
    getAllAdminsId: admin => getAdmin.get(),
    accountDelete: id => del.delete({id})
}

