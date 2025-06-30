var expect = require('chai').expect;
var AdminServer = require('../lib/admin_server.js');
var net = require('net');


describe('AdminServer', function() {
    var admin_server = null;

    beforeEach(function() {
        admin_server = new AdminServer('config.json');
        admin_server.listen(8124);
    });

    afterEach(function() {
        admin_server.close();
        admin_server = null;
    });

    describe('#gameExists()', function() {
        it('Should return false if a game does not exist', function() {
            var res = admin_server.gameIsRunning(7);
            expect(res).to.be.false;
        });

        it('Should return true if a game does exist', function() {
            admin_server.running_games.push(7);
            var res=  admin_server.gameIsRunning(7);
            expect(res).to.be.true;
        });
    });

    describe('#newGameInstance()', function() {
        it('Should write to the admin socket that it wants a new game to be created', function(done) {

            var admin = net.createConnection(8124);
            admin_server.on('admin_connected', function() {
                admin.on('data', function(data) {
                    var message = JSON.parse(data);
                    expect(message.type).to.be.equal('INSTANCE');
                    expect(message.n_detectives).to.be.equal(3);
                    expect(message.game_id).to.be.equal(5);
                    done();
                });
                admin_server.newGameInstance(5);
            });
        });
    });

    describe('#register()', function() {
        it('Should accept a player and student id and add it to its list of students', function() {
            var client = 'client';
            var student_id = 'om0000';
            admin_server.register(client, student_id);
            expect(admin_server.players[student_id]).to.be.equal(client);
        });

        it('Should add the registration into a queue if the game is not running', function(done) {
            var client = 'client';
            var student_id = 'om0000';
            admin_server.register(client, student_id);

            var queue_size = admin_server.registration_queue.length;
            expect(queue_size).to.be.equal(1);
            done();
        });

        it('Should contain the correct information in the registration queue', function(done) {
            var client = 'client';
            var student_id = 'om0000';
            admin_server.register(client, student_id);

            var info = admin_server.registration_queue[0];
            expect(info.student_id).to.be.equal(student_id);
            expect(info.game_id).to.be.equal(5);
            expect(info.colour).to.be.equal('Red');
            done();
        });

        it('Should not add to the queue if the game is ready', function(done) {
            var client = 'client';
            var student_id = 'om0000';
            admin_server.running_games.push(5);
            admin_server.register(client, student_id);

            var queue_size = admin_server.registration_queue.length;
            expect(queue_size).to.be.equal(0);
            done();
        });

        it('Should emit a join event if a game is running', function(done) {
            var event_emitted = false;

            setTimeout(function() {
                expect(event_emitted).to.be.true;
            }, 1000);

            admin_server.on('join', function() {
                event_emitted = true;
                done();
            });

            var client = 'client';
            var student_id = 'om0000';
            admin_server.running_games.push(5);
            admin_server.register(client, student_id);
        });

        it('The join event should contain the player socket, the game_id and the colour', function(done) {

            admin_server.on('join', function(player, colour, game_id) {
                expect(colour).to.be.equal('Red');
                expect(game_id).to.be.equal(5);
                done();
            });

            var client = 'client';
            var student_id = 'om0000';
            admin_server.running_games.push(5);
            admin_server.register(client, student_id);
        });
    });



    describe('gameRunning', function() {
        it('Should get a message from the client saying instance created with a game id', function () {
            var admin = net.createConnection(8124);
            var game_id = 5;
            admin_server.on('admin_connected', function () {
                var message = {'type': 'NEW_INSTANCE', game_id: game_id};
                admin.write(JSON.stringify(message), function () {
                    setTimeout(function () {
                        var index = admin_server.running_games.indexOf(game_id);
                        expect(index).to.be.equal(0);
                    }, 100);
                });
            });
        });
    });

    describe('#setGameRunning()', function() {
        it('Should process the registration queue for the given game id', function(done) {
            var admin = net.createConnection(8124);
            var game_id = 5;

            // add a regsitration
            var client = 'client';
            var student_id = 'om0000';
            admin_server.register(client, student_id);

            admin_server.setGameRunning(game_id);
            var queue_size = admin_server.registration_queue.length;
            expect(queue_size).to.be.equal(0);
            done();
        });
    });


    describe('#getGameInfo()', function() {
       it('Should except a game id and return the number of detectives', function() {
           var n_players = admin_server.getNumDetectives(5);
           expect(n_players).to.be.equal(3);
       });

        it('Should return false if the game id doesnt exits', function() {
            var n_players = admin_server.getNumDetectives(15);
            expect(n_players).to.be.false;
        });
    });



    describe('#getPlayerInfo()', function() {

        it('Should except a student id and get a game and colour', function() {
            var game_info = admin_server.getPlayerInfo('om0000');
            expect(game_info.game_id).to.be.equal(5);
            expect(game_info.colour).to.be.equal('Red');
        });


        it('Should remove the extracted info after it has been asked for', function() {
            admin_server.getPlayerInfo('om0000');
            var game_info = admin_server.getPlayerInfo('om0000');
            expect(game_info.game_id).to.be.equal(5);
            expect(game_info.colour).to.not.be.equal('Red');
        });

        it('Should return false if the student id does not exist', function() {
            var game_info = admin_server.getPlayerInfo('om000');
            expect(game_info).to.be.false;
        });
    });

    /*


    describe('join', function() {
        it('Should expect a join event fired from it when an admin sends a join message', function(done) {
            var event_emitted = false;

            setTimeout(function() {
                expect(event_emitted).to.be.true;
            }, 1000);

            var client = net.connect(8124, function() {
                var data = {type:'REGISTER'};
                client.write(JSON.stringify(data));
            });

            admin_server.on('register', function() {
                event_emitted = true;
                done();
            });
        });

        it('Should contain a player id and a colour that that player is using', function(done) {
            var id = '234mk';
            var colour = 'Red';
            var game_id = 5;
            var admin = net.connect(8124, function() {
                var data = {type:'JOIN', player_id:id, colour: colour, game_id: game_id};
                admin.write(JSON.stringify(data));
            });

            
            admin_server.on('join', function(player, sent_col, sent_game_id) {
                expect(sent_col).to.be.equal(colour);
                expect(sent_game_id).to.be.equal(game_id);
                done();
            });
        });
    });

    describe('#addPlayer()', function() {




        it('Should add a player to its list of connected players, based on the id', function(done) {
            var client = {};
            var id = 56;
            admin_server.addPlayer(client, id);
            expect(admin_server.hasPlayer(id)).to.be.true;
            done();
        });


        it('Should send a message to the admin that a player has joined', function(done) {
            var client = {};
            var id = 56;

            var admin = net.connect(8124, function() {
                admin_server.addPlayer(client, id);
            });

            admin.on('data', function(data) {
                var ob = JSON.parse(data);
                expect(ob.id).to.be.equal(56);
                expect(ob.type).to.be.equal('JOIN');
                done();
            });

        });


    });
    */
});

