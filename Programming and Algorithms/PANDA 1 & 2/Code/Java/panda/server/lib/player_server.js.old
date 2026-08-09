'use strict'
var net = require('net');
var events = require('events');
var util = require('util');


function PlayerServer() {
    var self = this;
    this.debug = false;
    this.closed = false;
    this.server = net.createServer(function(player) {
        player.on('data', function(data) {
            self.write(data.toString());
            self.onInput(player, JSON.parse(data.toString()));
        });
    });
}
util.inherits(PlayerServer, events.EventEmitter);


PlayerServer.prototype.onInput = function(player, data) {
    if(data.type == 'REGISTER') {
        this.emit('register', player, data.student_id);
    } else if (data.type == 'MOVE') {
        this.emit('move', player, data);
    }
}

PlayerServer.prototype.listen = function(port) {
    this.server.listen(port);
}

PlayerServer.prototype.close = function() {
    if(!this.closed) {
        this.server.close();
        this.closed = true;
    }
}

PlayerServer.prototype.write = function(data) {
    if(this.debug) console.log('[Player Server] ' + data);
}

module.exports = PlayerServer;