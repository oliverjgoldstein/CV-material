var net = require('net');

var AdminServer = require('./lib/admin_server.js');
var PlayerServer = require('./lib/player_server.js');
var GameServer = require('./lib/game_server.js');

var admin_server = new AdminServer("config.json");
var player_server = new PlayerServer();
var game_server = new GameServer();

admin_server.debug  = true;
player_server.debug = true;
game_server.debug   = true;

admin_server.listen(8124);
player_server.listen(8123);
game_server.listen(8122);


admin_server.on('admin_connected', function() {

   console.log("admin connected and ready for registrations");

    // on the join event we add the player to the game
    admin_server.on('join', function(player, colour, game_id) {
        game_server.addPlayer(player, colour, game_id);
    });

    // on the player reigstering we pass this into the admin server
    // to find out what game they are playing
    player_server.on('register', function(player, student_id) {
        admin_server.register(player, student_id);
    });


    player_server.on('move', function(player, move) {
        game_server.makeMove(player, move);
    })


});


/*

var judge = null;
var admin = net.connect(8124, function() {

    var player = net.connect(8123, function() {
        var obj = {type:'JOIN', id:4};
        player.write(JSON.stringify(obj));
    });

    admin.on('data', function(data) {
        var message = JSON.parse(data.toString());
        if(message.type == 'JOIN') {
            var game_id = 5;

            // connect a new judge
            judge = net.connect(8122, function() {

                judge.on('data', function(data) {

                    var d = JSON.parse(data);
                    if(d.type == 'MOVE') {
                    }
                    else {

                        var response = {type: 'JOIN', player_id: message.id, colour: 'Red', game_id: game_id};
                        admin.write(JSON.stringify(response));
                    }
                });

                var message2 = {type:'JOIN', game_id: game_id };
                    judge.write(JSON.stringify(message2), function() {
                });
            });

        }



    });


    setTimeout(function() {
        var move = {type:'MOVE', move: {colour: 'Red', target: 7, ticket: 'Bus'}};
        player.write(JSON.stringify(move));
    }, 200);


});

*/



