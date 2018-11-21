// Require HTTP module (to start server) and Socket.IO
// this is basically for connection between server and client(web/mobile)
var http = require('http'), io = require('socket.io');

// Start the server at port 4001
var server = http.createServer(function (req, res) {
   
    // Set headers  in case of server successfully connected
    console.log("Server connected to port 4001");
    res.writeHead(200, { 'Content-Type': 'text/html' });
    res.end('Socket Connecdted');

});

//listen the selected server to given port
server.listen(4001);

// Create a Socket.IO instance, passing it our server
// Now all events will be managed by this socket instance
var socket = io.listen(server);

// Add a connect listener on socket
socket.on('connection', function (client) {

    // this method will be eused for sending random number json to client
    // in every 1 sec
    setInterval(broadcast, 1000);

    // Success!  Now listen to messages to be received
    client.on('request_from_client', function (event) {
        console.log('Received message from client!', event);
    });

    client.on('disconnect', function () {
        clearInterval(interval);
        console.log('Server has disconnected');
    });


});

// Every three seconds broadcast "{ message: 'random number' }" to all connected clients
var broadcast = function () {
    var json = JSON.stringify({
        message: sendNumber() //'Hello hello!'
    });
    console.log("data to send = "+json);
    // client will recieve json response in 'message'
    socket.emit('message',  json);
}

function sendNumber() {
  var json = JSON.stringify({ message: Math.round(Math.random() * 0xFFFFFF) });
  return json;
}
