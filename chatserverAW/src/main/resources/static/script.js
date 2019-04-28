console.log("got here");

//var inputhandle = document.getElementById("myinput");
//var resulthandle = document.getElementById("result");
var usernamehandle = document.getElementById("username"); //quick to username box
var messageboxhandle = document.getElementById("messagebox"); //messagebox
var historyhandle = document.getElementById("history"); //all messages and log in greetings

function action() { //this is not real anymore!
    factorial(24);
}

function login() { //need to send an http request now
    var name = usernamehandle.value; 
    request({url: "login?Name=" + name, method: "POST"})
            .then(data => { //comes from login in java
                print("hello " + name);
            })
            .catch(error => {
                print("Error: " + error);
            });
}

function request(obj) {
    return new Promise((resolve, reject) => {
        let xhr = new XMLHttpRequest();
        xhr.open(obj.method || "GET", obj.url); //starting to send data to the server

        xhr.onload = () => {
            if (xhr.status >= 200 && xhr.status < 300) {
                resolve(xhr.response);
            } else {
                reject(xhr.statusText);
            }
        };
        xhr.onerror = () => reject(xhr.statusText);

        xhr.send(obj.body);
    });
}
;

// factorial done with the "promise pattern"
function factorial(n) {
    request({url: "factorial?number=" + n, method: "GET"})
            .then(data => {
                print("factorial is " + data);
            })
            .catch(error => {
                print("Error: " + error);
            });
}


//put and get shouldn't have lots of data in urls, use a form post
//form - when action is pushed, all fields are collected and sent

function sendMsg() {
    var data = new FormData(); //data is just the message
    var name = usernamehandle.value;
    var message = messageboxhandle.value; //message
    data.append("Message", message); 

    request({url: "sendMsg", method: "POST", body: data}) //try to send it back - calls the sendMsg in ChatServer.java
            .then(data => {
                print(name + ": " + message + " (js)"); //will print if it works
            })
            .catch(error => { //say error if error
                print("Error: " + error);
            });
            
}

function request(obj) {
    return new Promise((resolve, reject) => {
        let xhr = new XMLHttpRequest();
        xhr.open(obj.method || "GET", obj.url);

        xhr.onload = () => {
            if (xhr.status >= 200 && xhr.status < 300) {
                resolve(xhr.response);
            } else {
                reject(xhr.statusText);
            }
        };
        xhr.onerror = () => reject(xhr.statusText);

        xhr.send(obj.body);
    });
};





function print(s) {
    historyhandle.value += "\n" + s; //value is null??
}

console.log("finished parsing script");


