var expect = require('chai').expect;
var PlayerServer = require('../lib/player_server.js');
var net = require('net');


describe('PlayerServer', function() {
    var player_server = null;

    beforeEach(function() {
        player_server = new PlayerServer();
        player_server.listen(8124);
    });

    afterEach(function() {
        player_server.close();
        player_server = null;
    });


    describe('#register', function() {
        it('Should emit a register event when a player registers', function(done) {
            var event_emitted = false;

            setTimeout(function() {
                expect(event_emitted).to.be.true;
            }, 1000);

            var client = net.connect(8124, function() {
                var data = {type:'REGISTER'};
                client.write(JSON.stringify(data));
            });

            player_server.on('register', function() {
                event_emitted = true;
                done();
            });
        });

        it('Should have the id of the student as the data' , function(done) {
            var id = '234mc';

            var client = net.connect(8124, function() {
                var data = {type:'REGISTER', student_id:id};
                client.write(JSON.stringify(data));
            });

            player_server.on('register', function(client, sent_id) {
                expect(sent_id).to.be.equal(id);
                done();
            });
        });
    });


    describe('#move', function() {
        it('Should emit a move event when a player makes a move', function(done) {
            var event_emitted = false;

            setTimeout(function() {
                expect(event_emitted).to.be.true;
            }, 1000);

            var client = net.connect(8124, function() {
                var data = {type:'MOVE'};
                client.write(JSON.stringify(data));
            });

            player_server.on('move', function() {
                event_emitted = true;
                done();
            });
        });
    })
});
