'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let id = null;
let fullname = null;
let support = false;

function connect(event) {
    id = document.querySelector('#id').value.trim();
    fullname = document.querySelector('#fullname').value.trim();
    support = document.querySelector('#support').checked;
    if (id && fullname) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    if (support) {
        // register the connected user
        stompClient.send("/app/support.addUser",
            {},
            JSON.stringify({supportSpecialistId: id, fullName: fullname, status: 'ONLINE'})
        );
    } else {
        // register the connected user
        stompClient.send("/app/user.addUser",
            {},
            JSON.stringify({customerId: id, fullName: fullname, status: 'ONLINE'})
        );
        findNextAvailableSupportSpecialist().then();
    }

    document.querySelector('#connected-user-fullname').textContent = fullname;
}

async function findNextAvailableSupportSpecialist() {
    const connectedUsersResponse = await fetch('/support-specialist');
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';
    let connectedUser = await connectedUsersResponse.json();
    appendUserElement(connectedUser, connectedUsersList);
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.supportSpecialistId;

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.fullName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.fullName;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    connectedUsersList.appendChild(listItem);
}

function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function onLogout() {
    if (support) {
        stompClient.send("/app/support.disconnectUser",
            {},
            JSON.stringify({supportSpecialistId: id, fullName: fullname, status: 'OFFLINE'})
        );
    }
    else {
        stompClient.send("/app/user.disconnectUser",
            {},
            JSON.stringify({customerId: id, fullName: fullname, status: 'OFFLINE'})
        );
    }

    window.location.reload();
}


usernameForm.addEventListener('submit', connect, true); // step 1
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();
