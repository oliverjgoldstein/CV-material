'use strict'
var net = require('net');
var events = require('events');
var util = require('util');
var eventEmitter = new events.EventEmitter();


function PlayerServer() {
    var self = this;
    this.server = net.createServer(function(player) {
        player.on('data', function(data) {
            self.onInput(player, data);
        });
    });
}

util.inherits(PlayerServer, events.EventEmitter);


PlayerServer.prototype.onInput = function(player, data) {
    var self = this;
    var message = JSON.parse(data);
    // We use JSON to parse the data and emit a corresponding register or move event.

    if(message.type == "REGISTER") {
        self.emit('register', player, message.student_id);
    }
    else if(message.type == "MOVE") {
        self.emit('move', player, message);
    }
}


PlayerServer.prototype.listen = function(port) {
    // We listen to the port here.
    this.server.listen(port);
}

PlayerServer.prototype.close = function() {
    // We close the connection.
    this.server.close();
}

module.exports = PlayerServer;