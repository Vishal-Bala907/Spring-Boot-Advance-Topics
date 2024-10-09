const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let nickname = null;
let fullname = null;
let selectedUserId = null;

function connect(event) {
	nickname = document.querySelector('#nickname').value.trim();
	fullname = document.querySelector('#fullname').value.trim();

	if (nickname && fullname) {
		usernamePage.classList.add('hidden');
		chatPage.classList.remove('hidden');

		/*new SockJS('/ws'): Here, a new SockJS instance is created. The string '/ws' is 
		the endpoint on the server where SockJS will connect. This typically corresponds 
		to a server-side endpoint configured to handle WebSocket request*/
		const socket = new SockJS('/ws');

		/*Stomp: This is another library that provides an interface to work with STOMP over 
				WebSockets. It abstracts the complexities of working with WebSocket messaging.
		  Stomp.over(socket): This method takes the SockJS object (the established socket 
							  connection) and returns a STOMP client instance that can be used 
							  to send and receive messages via the WebSocket connection.
		*/
		stompClient = Stomp.over(socket);


		/*		
		stompClient.connect(): This method initiates a connection to the STOMP broker.
			{}: This object is typically used to pass headers for the connection. 
				In this case, it’s empty, which means no specific headers are being sent 
				(such as authentication tokens).
		onConnected: This is a callback function that will be invoked once the connection 
					is successfully established. You can define this function to handle 
					actions like subscribing to topics or sending messages.
		onError: This is another callback function that will be called if there’s an error 
				during the connection attempt. You can define this function to handle error 
				cases, such as displaying an error message or attempting to reconnect.
		*/
		stompClient.connect({}, onConnected, onError);
	}
	event.preventDefault();
}

function onConnected() {
	/*	Subscribing to User-Specific Messages:
		stompClient.subscribe(...): This method subscribes to a specific destination or topic 
									to receive messages.
	/user/${nickname}/queue/messages: This is a destination where the user can receive private 
									messages. The path includes a dynamic part ${nickname}, 
									which should be replaced with the user's nickname 
									(or identifier). This allows each user to receive messages 
									directed specifically to them.
	onMessageReceived: This is the callback function that will be invoked whenever a message is 
						received on this subscribed topic. You would typically define this 
						function to handle the processing and display of incoming messages
	*/
	stompClient.subscribe(`/user/${nickname}/queue/messages`, onMessageReceived);
	stompClient.subscribe(`/user/public`, onMessageReceived);

	// register the connected user
	/*
		stompClient.send(...): This method is used to send a message to a specific destination on
								 the server.
	"/app/user.addUser": This is the endpoint on the server where the message is sent. Typically,
						 this endpoint will handle user registration or status updates.
	{}: The second argument is an object for message headers. In this case, it’s empty, meaning 
				no additional headers are sent.
	JSON.stringify(...): The third argument converts a JavaScript object containing user 
					information into a JSON string. The object includes:
	nickName: The nickname of the user.
	fullName: The full name of the user.
	status: The online status of the user, set to 'ONLINE'.
	*/
	stompClient.send("/app/user.addUser",
		{},
		JSON.stringify({ nickName: nickname, fullName: fullname, status: 'ONLINE' })
	);

	/*This line updates the content of an HTML element with the ID connected-user-fullname to display the full name of the connected user*/
	document.querySelector('#connected-user-fullname').textContent = fullname;
	findAndDisplayConnectedUsers().then();

}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();
    connectedUsers = connectedUsers.filter(user => user.nickName !== nickname);
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.nickName;

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

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}


function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';

}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === nickname) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${nickname}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: nickname,
            recipientId: selectedUserId,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(nickname, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}


async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
}

function onLogout() {
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'OFFLINE'})
    );
    window.location.reload();
}

usernameForm.addEventListener('submit', connect, true); // step 1
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();