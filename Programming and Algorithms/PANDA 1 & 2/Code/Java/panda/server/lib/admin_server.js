var net = require('net');
var events = require('events');
var util = require('util');
var fs = require('fs');


function AdminServer(game_config_filename) {
    var self = this;
    this.admin = null;
    this.debug = false;
    this.players = [];
    this.running_games = [];
    this.registration_queue = [];
    this.waiting_games = [];

    this.server = net.createServer(function(admin) {
        if(self.admin == null) {
            self.admin = admin;
            self.emit('admin_connected');
        }

        
        self.admin.on('data', function(data) {
            self.parseInput(data);
        });

        self.admin.on('end', function() {
            self.admin = null;
            self.emit('admin_disconnected');
        });

    });


    var game_data = JSON.parse(fs.readFileSync(game_config_filename));
    this.player_config = game_data.players;
    this.game_config = game_data.games;



}
util.inherits(AdminServer, events.EventEmitter);


/**
 * Function to register a new student for a game
 * @param player
 * @param student_id
 */
AdminServer.prototype.register = function(player, student_id) {
    this.players[student_id] = player;
    var info = this.getPlayerInfo(student_id);
    if(!info) return false;

    if(!this.gameIsRunning(info.game_id)) {

        if(this.waiting_games[info.game_id] != true) {
            this.newGameInstance(info.game_id);
        }

        var register_info = {
            student_id: student_id,
            game_id: info.game_id,
            colour: info.colour
        };

        this.registration_queue.push(register_info);

    } else {
        this.addToGame(player, info.colour, info.game_id);
    }
}


/**
 * Function to add a player to a game, it emits a join event
 * @param player
 * @param colour
 * @param game_id
 */
AdminServer.prototype.addToGame = function(player, colour, game_id) {

    // send a registered message to the player


    this.emit('join', player, colour, game_id);
}


/**
 * Function to tell the admin client to create a new game
 * @param game_id
 */
AdminServer.prototype.newGameInstance = function(game_id) {
    if(!this.admin) return;
    var n_detectives = this.game_config[game_id];
    var message = {type:'INSTANCE', game_id: game_id, n_detectives: n_detectives};
    this.admin.write(JSON.stringify(message) +'\n');
    this.waiting_games[game_id] = true;
}

/**
 * Function to check if a game exists
 * @param game_id
 */
AdminServer.prototype.gameIsRunning = function(game_id) {
    return this.running_games.indexOf(game_id) > -1;
}



/**
 * Function to get the students game and colour for the current register.
 * If the student id isn't in the list then return false
 * @param student_id
 */
AdminServer.prototype.getPlayerInfo = function(student_id) {
    if(typeof this.player_config[student_id] === 'undefined') return false;
    var info = this.player_config[student_id][0];
    this.player_config[student_id].splice(0,1);
    return info;
}


/**
 * Function to get the number of detectives in a game given the game id
 * Should return false if the game doesnt exist
 * @param data
 */
AdminServer.prototype.getNumDetectives = function(game_id) {
    if(typeof this.game_config[game_id] === 'undefined') return false;
    return this.game_config[game_id];
}


/**
 * Function to parse the input that comes in from the admin client
 * @param data
 */
AdminServer.prototype.parseInput = function(data) {
    var message = JSON.parse(data.toString());
    if(message.type == 'NEW_INSTANCE') {
        this.setGameRunning(message.game_id);
    }
}

/**
 * Function to set a game as running. This will set the game to be
 * running in the running games array and process the pending registration
 * events in the registration queue
 * @param game_id
 */
AdminServer.prototype.setGameRunning = function(game_id) {

    this.running_games.push(game_id);

    for(var i = this.registration_queue.length-1; i >= 0; i--) {

        // check if the registration is for the correct game
        if(this.registration_queue[i].game_id == game_id) {

            // add the player to a game
            var info = this.registration_queue[i];
            var player = this.players[info.student_id];
            this.addToGame(player, info.colour, info.game_id);

            // remove the registration from the list
            this.registration_queue.splice(i,1);
        }
    }
}


AdminServer.prototype.listen = function(port) {
    this.server.listen(port);
}



AdminServer.prototype.close = function() {
    this.server.close();
}



AdminServer.prototype.hasPlayer = function(id) {
    return (typeof this.players[id] !== 'undefined');
}


AdminServer.prototype.write = function(data) {
    if(this.debug) console.log('[Admin Server ] ' + data);
}

module.exports = AdminServer;


