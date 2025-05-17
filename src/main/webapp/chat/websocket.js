var wsUri = 'ws://' + document.location.host +
        document.location.pathname.substr(0, document.location.pathname.indexOf("/faces")) + '/websocket';

var websocket = new WebSocket(wsUri);
var textField = document.getElementById("texto");
var users = document.getElementById("users");
var chatlog = document.getElementById("chatlog");
var output = document.getElementById("output");
var username;

function join() {
    username = textField.value;
    websocket.send(username + " joined");
    document.getElementById("unirse").style.visibility = "hidden";
    document.getElementById("enviar").style.visibility = "visible";
    document.getElementById("desconectar").style.visibility = "visible";
}

function send_message() {
    websocket.send(username + ": " + textField.value);
}

function disconnect() {
    websocket.close();
    document.getElementById("enviar").style.visibility = "hidden";
    document.getElementById("desconectar").style.visibility = "hidden";
}

websocket.onopen = function (evt) {
    writeToScreen("CONNECTED");
};

websocket.onclose = function (evt) {
    writeToScreen("DISCONNECTED");
};

websocket.onmessage = function (evt) {
    writeToScreen("RECEIVED: " + evt.data);
    if (evt.data.indexOf("joined") !== -1) {
        users.value += evt.data.split(" joined")[0] + "\n";
    } else {
        chatlog.value += evt.data + "\n";
    }
};

websocket.onerror = function (evt) {
    writeToScreen("ERROR: " + evt.data);
};

function writeToScreen(message) {
    let pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    output.appendChild(pre);
}
