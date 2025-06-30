'use strict'
var PlayerServer = require('./player_server.js');
var GameServer   = require('./game_server.js');

/**
 * Constructor for the class. Needs to initialise the players server
 * and game server, create the list of colours and initialise a game id
 * @constructor
 */
function SimpleServer() {

    this.player_server = new PlayerServer();
    this.game_server   = new GameServer();
    this.colours = ['Black', 'Blue', 'Green', 'Red', 'White', 'Yellow'];
    this.game_id = -1;

}

/**
 * Function that will start up the server. It should set the game_server
 * and the player_server to listen on the correct ports. It should also
 * connect up the events emitted by the player server so that the correct
 * information is passed onto the game server
 * @param player_port
 * @param game_port
 */
SimpleServer.prototype.start = function(player_port, game_port) {
    this.player_server.listen(player_port);
    this.game_server.listen(game_port);

    var self = this;
    this.game_server.on('initialised', function(initialised_game_id) {

        self.game_id = initialised_game_id;
        console.log("Game initialised with id: " + self.game_id);

        self.player_server.on('register', function(player, student_id) {
            var colour = self.getNextColour();
            console.log("Player joined with colour: " + colour);
            self.game_server.addPlayer(player, colour, self.game_id);
        });

        self.player_server.on('move', function(player, move) {
            self.game_server.makeMove(player, move);
        });
    });
}


/**
 * Function to close down the player server and the game server
 */
SimpleServer.prototype.close = function() {
    this.player_server.close();
    this.game_server.close();
}

/**
 * Function to retrieve the id of the game being played
 * @returns {number|*}
 */
SimpleServer.prototype.gameId = function() {
    return this.game_id;
}


/**
 * Function to extract the next colour out of the arrays of colours
 * When a colour is extracted, that colour is removed from the list
 * @returns {*}
 */
SimpleServer.prototype.getNextColour = function() {
    return this.colours.shift();
}


module.exports = SimpleServer;

