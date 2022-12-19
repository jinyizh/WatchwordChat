"use strict";

let ws = null;
let wsOpen = false;

let usernameTA = document.getElementById("usernameTA"); // alias text area
usernameTA.addEventListener("keypress", handleKeyPressCB);

let wordTA = document.getElementById("wordTA"); // watchword text area
wordTA.addEventListener("keypress", handleKeyPressCB);

let messageList = document.getElementById("columnRight"); // right panel

let messageTA = document.getElementById("messageTA"); // bottom panel
messageTA.addEventListener("keypress", handleKeyPressCB);

let createBtn = document.getElementById("createBtn"); // "start" button
createBtn.addEventListener("click", handleKeyPressCB);

let sendBtn = document.getElementById("sendBtn"); // "send" button
sendBtn.addEventListener("click", handleKeyPressCB);

// <enter> or "click" to create / join a word / send text messages
function handleKeyPressCB(event) {
    if (event.keyCode === 13 || event.type === "click") {
        let username = usernameTA.value;
        let word = wordTA.value
        let message = messageTA.value;
        event.preventDefault();
        if (username === "" || username === "" || username == null) {
            alert("Please enter a valid alias! See the rules for details.");
            usernameTA.select();
            return;
        }
        if (word === "" || word === 0 || word == null) {
            alert("Please enter a valid watchword! See the rules for details.");
            wordTA.select();
            return;
        }
        for (let char of username) {
            if (char === ' ') {
                alert("Please enter a valid alias! See the rules for details.");
                return;;
            }
        }
        for (let letter of word) {
            if (letter < 'a' || letter > 'z') {
                alert("Please enter a valid watchword! See the rules for details.");
                return;
            }
        }
        if (ws == null) {
            ws = new WebSocket("ws://localhost:8080");
            ws.onopen = handleConnectCB;
            ws.onmessage = handleMessageFromWsCB;
        } else if (wsOpen) {
            ws.send(`${username} ${word} ${message}`);
            messageTA.value = ""; // clear the text
        }
    }
}

function handleConnectCB() {
    wsOpen = true;
    let username = usernameTA.value;
    let word = wordTA.value;
    ws.send(`join ${username} ${word}`);
}

// parse the message
function handleMessageFromWsCB(event) {
    let obj = event.data;
    let myMsgObj = JSON.parse(obj); // turn it into JSON
    console.log(myMsgObj);
    let time = new Date();
    let timeString = time.toLocaleTimeString();
    if (myMsgObj.type === "join") {
        let newMessage = document.createElement('p');
        let word = wordTA.value;
        newMessage.innerHTML = timeString + "</br>" + myMsgObj.user + " says " + word + "!";
        messageList.appendChild(newMessage);
        newMessage.scrollIntoView(false);
    }
    else if (myMsgObj.type === "message") {
        let newMessage = document.createElement('p');
        newMessage.innerHTML = timeString + "</br>" + myMsgObj.user + ": " + myMsgObj.message;
        messageList.appendChild(newMessage);
        newMessage.scrollIntoView(false);
    }
    else if ((myMsgObj).type === "leave") {
        let newMessage = document.createElement('p');
        newMessage.innerHTML = timeString + "</br>" + myMsgObj.user + " has left.";
        messageList.appendChild(newMessage);
        newMessage.scrollIntoView(false);
    }
}

// send leave message when close / refresh:
// {"type": "leave", watchword: "word", user: "user"}
window.addEventListener("beforeunload", () => {
    let username = usernameTA.value;
    let word = wordTA.value;
    ws.send(`leave ${username} ${word}`);
})