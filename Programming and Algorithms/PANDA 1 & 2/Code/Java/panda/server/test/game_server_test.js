var expect = require('chai').expect;
var GameServer = require('../lib/game_server.js');
var net = require('net');



describe('GameServer', function () {
    var game_server = null;

    beforeEach(function () {
        game_server = new GameServer();
        game_server.listen(8124);
    });

    afterEach(function () {
        game_server.close();
        game_server = null;
    });


    describe('initialise', function () {
        it('Should recieve a message from a judge to initialise a game', function (done) {
            var message = {type: 'INITIALISE', game_id: 5};
            var judge = net.connect(8124, function () {
                judge.write(JSON.stringify(message));

                setTimeout(function () {
                    expect(game_server.hasGame(5)).to.be.true;
                    done();
                }, 10);
            });
        });

        it('Should send a message to the judge that a game has been initialised', function (done) {
            var message = {type: 'INITIALISE', game_id: 5};
            var judge = net.connect(8124, function () {
                judge.write(JSON.stringify(message));

                judge.on('data', function (data) {
                    var resp = JSON.parse(data.toString());
                    expect(resp.type).to.be.equal('INITIALISED');
                    done();
                });
            });
        });
    });



    describe('#getGame()', function() {
        it('Should find the correct game given a judge', function(done) {
            var judge = net.createConnection(8124, function() {


                judge.on('data', function(data) {

                    var id = judge.localAddress +':' + judge.localPort;
                    var resp = JSON.parse(data.toString());
                    if(resp.type == 'INITIALISED') {
                        var game = game_server.getGameBySocketId(id);
                        expect(game).to.have.property('players');
                        expect(game).to.have.property('judge');
                        done();
                    }
                });

                var newGame = {type:'INITIALISE', game_id: 5};
                judge.write(JSON.stringify(newGame));
            });
        });

        it('Should return null if the judge is not recognised', function(done) {
            var judge = net.createConnection(8124, function() {


                judge.on('data', function(data) {

                    var id = judge.localAddress +':' + judge.remotePort;
                    var resp = JSON.parse(data.toString());
                    if(resp.type == 'INITIALISED') {
                        var game = game_server.getGameBySocketId(id);
                        expect(game).to.be.null;
                        done();
                    }
                });

                var newGame = {type:'INITIALISE', game_id: 5};
                judge.write(JSON.stringify(newGame));
            });
        });
    });


    describe('#notifyTurn()', function () {

        var player = null;
        var player_server = null;
        var socket = null;
        beforeEach(function(done) {
            player_server = net.createServer(function(client) {
                socket = client;
                done();
            }).listen(8123, function() {
                player = net.createConnection(8123, function() {
                });
            });
        });

        afterEach(function() {
            player_server.close();
            player_server = null;
            player = null;
        });

        it('Should send a message to a player to let them know that it is their turn to go',  function(done) {

            player.on('data', function(data) {
                var message = JSON.parse(data.toString());
                expect(message.type).to.be.equal('NOTIFY_TURN');
                done();
            });

            game_server.notifyTurn(socket, 56);
        });

        it('Should send a message to a player that contains their location', function(done) {

            player.on('data', function(data) {
                var message = JSON.parse(data.toString());
                expect(message.location).to.be.equal(56);
                done();
            });

            game_server.notifyTurn(socket, 56);
        });


        it('Should translate a message from the judge all the way through to the player', function (done) {
            var judge = net.createConnection(8124, function() {

                judge.on('data', function(data) {
                    var resp = JSON.parse(data.toString());
                    if(resp.type == 'INITIALISED') {
                        game_server.addPlayerToGame(socket, 'Red', 5);
                        var notify = {type: 'NOTIFY_TURN', colour: 'Red', location: 56};
                        judge.write(JSON.stringify(notify));
                    }
                });

                player.on('data', function(data) {
                    var message = JSON.parse(data.toString());
                    if(message.type == 'NOTIFY_TURN') {
                        expect(message.type).to.be.equal('NOTIFY_TURN');
                        expect(message.location).to.be.equal(56);
                        done();
                    }
                });

                var init = {type:'INITIALISE', game_id: 5};
                judge.write(JSON.stringify(init));
            });
        });
    });


    describe('notify', function() {
        var player = null;
        var player_server = null;
        var socket = null;
        beforeEach(function(done) {
            player_server = net.createServer(function(client) {
                socket = client;
                done();
            }).listen(8123, function() {
                player = net.createConnection(8123, function() {
                });
            });
        });

        afterEach(function() {
            player_server.close();
            player_server = null;
            player = null;
        });


        it('Should send a message to all the spectators of a game', function (done) {

            var judge = net.createConnection(8124, function() {

                judge.on('data', function(data) {
                    var resp = JSON.parse(data.toString());
                    if(resp.type == 'INITIALISED') {
                        game_server.addPlayerToGame(socket, 'Red', 5);
                        game_server.addPlayerToGame(socket, 'Blue', 5);
                        game_server.addPlayerToGame(socket, 'White', 5);

                        var notify = {
                            type: 'NOTIFY',
                            move : { target:5 , colour: 'Black', ticket: 'Bus' }
                        };
                        judge.write(JSON.stringify(notify));
                    }
                });

                player.on('data', function(data) {
                    var parts = data.toString().split('\n');
                    for(var i = 0; i < parts.length; i++) {
                        if(parts[i].length == 0) continue;
                        var message = JSON.parse(parts[i]);
                        if(message.type=='NOTIFY') {
                            expect(true).to.be.true;
                            done();
                        }
                    }

                });

                var init = {type:'INITIALISE', game_id: 5};
                judge.write(JSON.stringify(init));
            });
        });
    });


    describe('#ready()', function() {

        var player = null;
        var player_server = null;
        var socket = null;
        beforeEach(function(done) {
            player_server = net.createServer(function(client) {
                socket = client;
                done();
            }).listen(8123, function() {
                player = net.createConnection(8123, function() {
                });
            });
        });

        afterEach(function() {
            player_server.close();
            player_server = null;
            player = null;
        });


        it('Should recieve a message from the judge when the game is ready', function(done) {
            /*
            var judge = net.createConnection(8124, function() {
                judge.on('data', function(data) {
                    var resp = JSON.parse(data.toString());
                    if(resp.type == 'INITIALISED') {
                        game_server.addPlayerToGame(socket, 'Red', 5);
                        game_server.addPlayerToGame(socket, 'Blue', 5);
                        game_server.addPlayerToGame(socket, 'White', 5);

                        var notify = {
                            type: 'NOTIFY',
                            move : { target:5 , colour: 'Black', ticket: 'Bus' }
                        };
                        judge.write(JSON.stringify(notify));
                    }
                });

                var times_notified = 0;

                player.on('data', function(data) {
                    var parts = data.toString().split('\n');
                    for(var i = 0; i < parts.length; i++) {
                        if(parts[i].length == 0) continue;
                        var message = JSON.parse(parts[i]);
                        if(message.type == 'NOTIFY')  {
                            times_notified++;
                        }
                    }

                });

                var init = {type:'INITIALISE', game_id: 5};
                judge.write(JSON.stringify(init));

                setTimeout(function() {
                    expect(times_notified).to.be.equal(3);
                    done();
                }, 100);

            */
            done();
        });
    });


    describe('#makeMove()' , function() {

        var player = null;
        var player_server = null;
        var socket = null;
        beforeEach(function(done) {
            player_server = net.createServer(function(client) {
                socket = client;
                done();
            }).listen(8123, function() {
                player = net.createConnection(8123, function() {
                });
            });
        });

        afterEach(function() {
            player_server.close();
            player_server = null;
            player = null;
        });


        it('Should recieve a move object and send it to the correct judge', function(done) {
            var judge = net.createConnection(8124, function() {

                var init = {type:'INITIALISE', game_id: 5};
                judge.write(JSON.stringify(init));



                judge.on('data', function(data) {
                    var resp = JSON.parse(data.toString());
                    if(resp.type == 'INITIALISED') {

                        game_server.addPlayerToGame(socket, 'Red', 5);
                        var move = {target: 76, colour: 'Red', ticket: 'Bus'};
                        game_server.makeMove(socket, move);
                    }
                    else if(resp.type == 'MOVE') {
                        expect(resp.move.target).to.be.equal(76);
                        expect(resp.move.ticket).to.be.equal('Bus');
                        expect(resp.move.colour).to.be.equal('Red');
                        done();
                    }
                });

            });
        });
    });




    describe('#addSocket()', function () {

        it('Should add a judge to the list when they initialise a game', function (done) {
            var judge = net.connect(8124, function () {
                game_server.addSocket(judge, 5);
                var id = judge.remoteAddress + ':' + judge.remotePort;
                expect(game_server.sockets[id]).to.be.equal(5);
                done();
            });
        });


        it('Should not replace a judge that is already judging a game', function (done) {
            var message = {type: 'JOIN', game_id: 4};
            var judge = net.connect(8124, function () {
                game_server.addSocket(judge, 4);
                game_server.addSocket(judge, 5);
                var id = judge.remoteAddress + ':' + judge.remotePort;
                expect(game_server.sockets[id]).to.be.equal(4);
                done();
            });
        });


        /*
         it('Should send an ack to the judge after the game has been created', function(done) {
         var message = {type:'JOIN', game_id: 4};
         var judge = net.connect(8124, function() {

         judge.on('data', function(data) {
         console.log(data.toString());
         var response = JSON.parse(data.toString());
         expect(response.ok).to.be.equal(1);
         done();
         });

         judge.write(JSON.stringify(message));
         });

         });
         */
    });


    describe('#createGame()', function () {

        it('It should create a new game ', function () {
            var judge = 'judge';
            game_server.createGame(judge, 54);
            expect(game_server.hasGame(54)).to.be.true;
        });

        it('It should not create a duplicate new game ', function () {
            game_server.createGame('hello', 54);
            game_server.createGame('goodbye', 54);
            expect(game_server.games[54].judge).to.be.equal('hello');
        });
    });


    describe('#addPlayerToGame()', function () {

        var player = null;
        var player_server = null;
        var socket = null;
        beforeEach(function(done) {
            player_server = net.createServer(function(client) {
                socket = client;
                done();
            }).listen(8123, function() {
                player = net.createConnection(8123, function() {
                });
            });
        });

        afterEach(function() {
            player_server.close();
            player_server = null;
            player = null;
        });



        it('Should add a player to a game given a game id and colour', function () {
            game_server.createGame('judge', 5);
            var res = game_server.addPlayerToGame(socket, 'Red', 5);
            expect(game_server.games[5].players['Red']).to.be.equal(socket);
            expect(res).to.be.true;
        });


        it('Should return false if the game does not exist', function () {
            var res = game_server.addPlayerToGame(socket, 'Red', 5);
            expect(res).to.be.false;
        });


        it('Should add the player to the list of players given its port address', function () {
            game_server.createGame('judge', 5);
            var res = game_server.addPlayerToGame(socket, 'Red', 5);
            expect(game_server.games[5].players['Red']).to.be.equal(socket);
            expect(res).to.be.true;
        });


        it('Should add a client to the list of spectators given the socket id', function(done) {
            var judge = net.createConnection(8124, function() {
                judge.on('data', function(data) {
                    var message = JSON.parse(data.toString());
                    if(message.type == 'JOIN') {
                        expect(message.colour).to.be.equal('Red');
                        done();
                    }
                    else if(message.type == 'INITIALISED') {
                        game_server.addPlayer(socket, 'Red', 5);
                        expect(game_server.games[5].spectators[game_server.socketId(socket)]).to.be.equal(socket);
                    }
                });

                var init = {type: 'INITIALISE', game_id:5};
                judge.write(JSON.stringify(init));


            });
        });


        it('Should send a registered message to the joined player with the players colour', function(done) {

            var judge = net.createConnection(8124, function() {

                player.on('data', function(data) {
                    var resp = JSON.parse(data.toString());
                    if(resp.type == 'REGISTERED') {
                        expect(resp.colour).to.be.equal('Red');
                        done();
                    }
                });


                judge.on('data', function (data) {
                    var message = JSON.parse(data.toString());
                    if (message.type == 'INITIALISED') {
                        game_server.addPlayer(socket, 'Red', 5);
                    }
                });



                var init = {type: 'INITIALISE', game_id: 5};
                judge.write(JSON.stringify(init));
            });

        });


        it('Should send a message to the judge that a player has been added with ' +
        'that players colour', function(done) {
            var judge = net.createConnection(8124, function() {
                judge.on('data', function(data) {
                    var message = JSON.parse(data.toString());
                    if(message.type == 'JOIN') {
                        expect(message.colour).to.be.equal('Red');
                        done();
                    }
                    else if(message.type == 'INITIALISED') {
                        game_server.addPlayer(socket, 'Red', 5);
                    }
                });

                var init = {type: 'INITIALISE', game_id:5};
                judge.write(JSON.stringify(init));


            });
        });
    });


});
