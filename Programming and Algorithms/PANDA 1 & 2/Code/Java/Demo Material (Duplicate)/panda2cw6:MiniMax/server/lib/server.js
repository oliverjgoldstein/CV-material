'use strict'
var PlayerServer = require('./player_server.js');
var GameServer   = require('./game_server.js');

/**
 * Constructor for the class. Needs to initialise the players server
 * and game server, create the list of colours and initialise a game id
 * @constructor
 */

function Server() {
    // This sets the game id to -1.
    this.game_id = -1;
    this.colours = [];
    var self = this;
    // To keeep track of the object.
    var game_server = new GameServer();
    // Instantiate the game server.
    var player_server = new PlayerServer();
    // Instantiate the player server.
    // Allow the server object to contain a copy of both of these.
    this.game_server = game_server;
    this.player_server = player_server;
    // when the game server is initialised set the game id to the one passed in.
    game_server.on('initialised', function(id) {
        self.game_id = parseInt(id);
    });
    // Available colours to choose from in the beggining.
    var availableColours = ['Black', 'Blue', 'Green', 'Red', 'White', 'Yellow'];
    this.availableColours = availableColours;
    // The playercount starts off at one.
    var playerCount = 1;
    this.playerCount = playerCount;
}

/**
 * Function that will start up the server. It should set the game_server
 * and the player_server to listen on the correct ports. It should also
 * connect up the events emitted by the player server so that the correct
 * information is passed onto the game server
 * @param player_port
 * @param game_port
 */
Server.prototype.start = function(player_port, game_port) {
    // Listen to both ports using the servers listen method.
    var self = this;
    this.player_server.listen(player_port);
    this.game_server.listen(game_port);
    // When the player server emits a register call - we want to take the next colour and add it to the game.

    this.player_server.on('register', function(player, student_id) {
        var colour = self.getNextColour();
        self.playerCount++;
        self.game_server.addPlayer(player, colour, self.game_id);
    });

// Upon a move emit - make a move.
    this.player_server.on('move', function(player, move) {
        self.game_server.makeMove(player, move);
    });
}


/**
 * Function to close down the player server and the game server
 */

Server.prototype.close = function() {
    this.game_server.close();
    this.player_server.close();
}


/**
 * Function to retrieve the id of the game being played
 * @returns {number|*}
 */
Server.prototype.gameId = function() {
    return this.game_id;
}


/**
 * Function to extract the next colour out of the arrays of colours
 * When a colour is extracted, that colour is removed from the list
 * @returns {*}
 */

 // Uses array indices to return the next available colour. Indexes the array using a playerCount global.
Server.prototype.getNextColour = function() {
    var colour = this.availableColours[this.playerCount-1];
    return colour;
}

module.exports = Server;

