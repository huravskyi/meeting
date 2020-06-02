import SockJS from 'sockjs-client'
import {Client} from '@stomp/stompjs';


let stompClient = null;
const handlers = [];

export function connect() {
    stompClient = new Client();

    stompClient.webSocketFactory = function () {
        return new SockJS("/websocket");
    };
    stompClient.onConnect = function (frame) {
        frameHandler(frame)
    };
    stompClient.activate();
    stompClient.debug = function(str) {};

    stompClient.onWebSocketClose = function(frame) {
        onWebSocketClose(frame)
    };
}
function onWebSocketClose(frame) {
    window.location.reload()
}

export function addHandler(handler) {
    handlers.push(handler)
}

function frameHandler(frame) {
    stompClient.subscribe('/topic/activity'+ userProfile.id, message => {
        handlers.forEach(handler => handler(JSON.parse(message.body)))
    })
}
export function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
}