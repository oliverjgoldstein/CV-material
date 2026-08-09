$( document ).ready(function() {

    var typingTimer;                
    var doneTypingInterval = 800;  
    
    // Sets the box next the the input box after you have typed every character.


    $('#keywords').keyup(function() {
        var texts = $("#keywords").val();
        $('#type-something').html(texts);
        if( !$('#keywords').val() ) {
            $('#type-something').html("Type Something");
        }
    });

    // Find tweeters - starts the function calls.

    $('#init-ajax').click(function(){
        
        $("#reload").css("height","2px"); // Note this in case of height glitch.
        $("#about-text").hide();

        if ($('#keywords').val) {
            typingTimer = setTimeout( timerAndAjaxCall, doneTypingInterval );
            $("#status-text").html('<span class = "server-message info">Loading Tweets... <img src = "/static/img/spinner.gif" id = "spinner" /></span>');
        }

        $('#keywords').keyup(function() {
            clearTimeout(typingTimer);
            $("#status-text").text("Interupted...");
        });

    });
});
