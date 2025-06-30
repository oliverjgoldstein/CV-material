var readline = require('readline');
var net = require('net');

var rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});




function playerActions(parts) {
    if(parts[1] == "new") {
        var player = net.createConnection(8123, function() {
           var message = {type:'REGISTER', id: parts[2]};
            player.write(JSON.stringify(message));
            player.on('data', function(data) {
                var message = JSON.parse(data.toString());
                console.log(message);
                if(message.type == 'NOTIFY_TURN') {
                    var move = {type: 'MOVE', move_type: 'MoveTicket', move : {target:34, ticket: 'Bus', colour: message.colour}};
                    player.write(JSON.stringify(move) + '\n');
                }
            });
        });
    }
}


function getCommand() {
    rl.question('What to do? ', function(answer) {
        var parts = answer.split(" ");
        if(parts[0] == 'player') playerActions(parts);

        getCommand();
    });
}


getCommand();


