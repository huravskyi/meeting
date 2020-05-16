import chatApi from '../../api/chat.js'
import scrollToElement from "../../helper/scrollToElement";
import Vue from 'vue'
import userApi from "../../api/user";
import getIndexForChatBlock from "../../helper/getIndexChat";
import setMessage from "../../helper/setMessage";
import setMessageToChat from "../../helper/setMessagesToChat";
import findChat from "../../helper/findChat";

export default {
    state: {
        chats: chats,
        chatsBlock,
        messageIdList: new Set(),
        blockAction: false,
    },
    mutations: {
        setBlockMutation(state) {
            state.blockAction = false
        },
        createChatDtoMutation(state, chatDto) {
            chatDto.messages = []
            state.chats.unshift(chatDto)
        },
        createChatAndMessageSocketMutation(state, chat) {
            const indexChat = state.chats.findIndex(item => item.id === chat.id)
            if (indexChat !== -1) {
                state.chats = setMessage(state.chats, chat, indexChat)
            } else {
                chat.currentPage = 0
                chat.totalPage = 1
                chat.numberOfNewMessage = 1
                chat.messages = []
                state.chats.unshift(chat)
                try {
                    const audio = document.getElementById("audio")
                    audio.play()
                } catch (e) {
                    console.log('error  audio')

                }
            }
        },
        updateViewedMessageMutation(state, chat) {
            const indexChat = state.chats.findIndex(item => item.id === chat.id)

            state.chats[indexChat].messages.forEach((itemOld, indexMessage) => {
                _.forEach(chat.messages, itemNew => {
                    if (itemNew.id === itemOld.id) {
                        state.chats[indexChat].messages[indexMessage].viewed = true
                        return false
                    }
                })
            })
        },

        updateDeliveredAllMessageMutation(state, chatId) {
            const index = state.chats.findIndex(item => item.id === chatId)
            state.chats[index].messages.forEach(item => {
                item.delivered = true
            })

        },
        setToUpChatMutation(state, index) {
            const chat = Object.assign({}, state.chats[index])
            const chats = [
                ...state.chats.slice(0, index),
                ...state.chats.slice(index + 1)
            ]
            chats.unshift(chat)
            state.chats = chats

        },
        writeNewMessageAndNewChatMutation(state, chat) {
            state.chats.unshift(chat)
        },
        saveChatAndMessageMutation(state, chat) {
            let indexChats = findChat(state.chats, chat)
            if (indexChats !== undefined) {
                state.chats.splice(indexChats, 1, chat)
            }
            let indexChatsBlock = findChat(state.chatsBlock, chat)
            if (indexChatsBlock !== undefined) {
                state.chatsBlock.splice(indexChatsBlock, 1)
            }
        },
        addMessageSocketMutation(state, message) {
            const index = state.chats.findIndex(item =>
                message.chat.id === undefined ? item.id === message.chat : item.id === message.chat.id)
            if (index === -1) {
            } else {
                state.chats[index].lastMessage = message.content.content
                if (state.chats[index].messages.length !== 0) {
                    if ((state.chats[index].totalPageNotViewed - state.chats[index].currentPageNotViewed) <= 1) {
                        state.chats[index].messages.push(message)
                    }
                }
                state.chats[index].numberOfNewMessage = state.chats[index].numberOfNewMessage + 1
                try {
                    const audio = document.getElementById("audio")
                    audio.play()
                } catch (e) {
                    console.log('error  audio')
                }
            }
        },
        addMessageMutation(state, message) {
            const index = state.chats.findIndex(item =>
                message.chat.id === undefined ? item.id === message.chat : item.id === message.chat.id)
            state.chats[index].lastMessage = message.content.content
            const targetMessages = state.chats[index].messages
                .concat(message)
                .reduce((res, val) => {
                    res[val.id] = val
                    return res
                }, {})
            state.chats[index].messages = Object.values(targetMessages)
            scrollToElement(state.chats[index], "bottomList")

        },

        setViewedMutation(state, obj) {
            const index = state.chats[obj.indexChat].messages.findIndex(item => item.id == obj.id)
            Vue.set(state.chats[obj.indexChat].messages[index], 'viewed', true)
        },

        setMessageIdListMutation(state, chat) {
            const indexChat = state.chats.findIndex(item => item.id === chat.id)
            state.chats[indexChat].numberOfNewMessage
                = state.chats[indexChat].numberOfNewMessage - chat.messages.length

        },
        updateDeliveredOneMessageMutation(state, message) {
            const index = state.chats.findIndex(item =>
                message.chat.id === undefined ? item.id === message.chat : item.id === message.chat.id)
            let indexMessage = state.chats[index].messages.findIndex(item => item.id === message.id)
            if (indexMessage === -1) {
                setTimeout(() => {
                    indexMessage = state.chats[index].messages.findIndex(item => item.id === message.id)
                    Vue.set(state.chats[index].messages[indexMessage], 'delivered', true)
                }, 10000)
            } else {
                Vue.set(state.chats[index].messages[indexMessage], 'delivered', true)
            }
        },

        downloadNewMessageFromDbMutation(state, obj) {
            state.chats = setMessageToChat(state.chats, obj, 'new')
        },
        downloadOldMessageFromDbMutation(state, obj) {
            state.chats = setMessageToChat(state.chats, obj, 'old')
        },
        addMessageFromDbMutation(state, obj) {
            state.chats = setMessageToChat(state.chats, obj, 'all')
        },

        updateChatsMutation(state, chats) {
            state.chats.forEach((item, index) => {
                const chat = chats.find(element => element.id === item.id)
                if (chat) {
                    state.chats[index].members = chat.members
                    state.chats[index].lastMessage = chat.lastMessage
                }
            })
        },
        blockUserMutations(state, user) {
            let indexChat = getIndexForChatBlock(state.chats, user)
            const chatBlock = state.chats[indexChat]
            state.chats = [
                ...state.chats.slice(0, indexChat),
                ...state.chats.slice(indexChat + 1)
            ]
            state.chatsBlock.push(chatBlock)
        },
        unblockUserMutations(state, user) {
            let indexChat = getIndexForChatBlock(state.chatsBlock, user)
            const chatBlock = state.chatsBlock[indexChat]
            state.chatsBlock = [
                ...state.chatsBlock.slice(0, indexChat),
                ...state.chatsBlock.slice(indexChat + 1)
            ]
            state.chats.unshift(chatBlock)
        },
        removeChatUserMutations(state, chatId) {
            const indexChat = state.chats.findIndex(item => item.id === chatId)
            state.chats = [
                ...state.chats.slice(0, indexChat),
                ...state.chats.slice(indexChat + 1)
            ]
        },
        blockActionMutation(state) {
            state.blockAction = true
        },
        setNewListMessageAndSetCountMessage(state, obj) {
            const indexChat = state.chats.findIndex(item => item.id === obj.id)
            state.chats[indexChat].messages = obj.data.messages
            state.chats[indexChat].currentPage = obj.data.currentPage
            state.chats[indexChat].totalPage = obj.data.totalPage
            state.chats[indexChat].currentPageNotViewed = obj.data.currentPageNotViewed
            state.chats[indexChat].totalPageNotViewed = obj.data.totalPageNotViewed
            state.chats[indexChat].numberOfNewMessage = 0
        }
    }
    ,
    actions: {
        async getNewListMessageAction({commit}, chatId) {
            const result = await chatApi.getNewListMessage(chatId)
            const data = await result.json()
            const obj = {
                id: chatId,
                data: data
            }
            if (result.ok) {
                commit('setNewListMessageAndSetCountMessage', obj)
            }
        }
        ,
        async removeChatUserAction({commit}, chatId) {
            const result = await userApi.removeUserChat(chatId)
            const data = await result.json()
            if (result.ok) {
                commit('removeChatUserMutations', data)
            }
        }
        ,
        async blockAndUnblockUserAction({commit}, chatAction) {
            const result = await userApi.updateBlock(chatAction.userId)
            const data = await result.json()
            if (result.ok) {
                if (chatAction.action === 'block') {
                    commit('blockUserMutations', data)
                } else {
                    commit('unblockUserMutations', data)
                }
            }
        }
        ,
        async getBlockChatAction({commit}) {
            await chatApi.getBlockChats()

        }
        ,
        async updateChatsAction({commit}) {
            const result = await chatApi.getChats()
            const data = await result.json()
            if (result.ok) {
                commit('updateChatsMutation', data)
            }
        }
        ,
        async downloadNewMessageFromDbAction({commit, state}, object) {
            const result = await chatApi.downloadNewMessage(object)
            const data = await result.json()
            if (result.ok) {
                const obj = {
                    chatId: object.chat.id,
                    data: data
                }
                commit('downloadNewMessageFromDbMutation', obj)
                return result.ok
            }
        }
        ,
        async downloadOldMessageFromDbAction({commit, state}, object) {
            const result = await chatApi.downloadOldMessage(object)
            const data = await result.json()
            if (result.ok) {
                const obj = {
                    chatId: object.chat.id,
                    data: data
                }
                commit('downloadOldMessageFromDbMutation', obj)
                return result.ok
            }
        }
        ,
        async getMessageFromDbAction({commit, state}, object) {
            const result = await chatApi.getMessagesForPage(object)
            const data = await result.json()
            if (result.ok) {
                const obj = {
                    chatId: object.chat.id,
                    data: data
                }
                commit('addMessageFromDbMutation', obj)
                return result.ok
            }
        }
        ,
        async setViewedAction({commit, state}, chat) {
            state.messageIdList = new Set()
            const result = await chatApi.setViewed(chat)
            if (result.ok) {
                commit('setMessageIdListMutation', chat)
            }
        }
        ,
        async saveNewChatAndMessageActions({commit}, chat) {
            const result = await chatApi.add(chat)
            const data = await result.json()
            if (result.ok)
                if (data.id === null) {
                    commit('blockActionMutation')
                } else {
                    commit('saveChatAndMessageMutation', data)
                }
        }
        ,

        async saveMessageActions({commit}, message) {
            const result = await chatApi.save(message)
            const data = await result.json()
            if (result.ok) {
                if (data.id === null) {
                    commit('blockActionMutation')
                } else {
                    commit('addMessageMutation', data)
                }
            }
        }
        ,
        async deliveredMessageAction({commit}, message) {
            await chatApi.updateDelivered(message)
        }
        ,

        async deliveredChatMessageAction({commit}, chat) {
            const message = Object.assign({}, chat.messages[0])
            await chatApi.updateDelivered(message)

        }
    }
}