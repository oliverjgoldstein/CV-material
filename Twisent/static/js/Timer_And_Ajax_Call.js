
function timerAndAjaxCall() {
    
    // Get the parameters to send to the server.
    tweet_count              = 0;
    var keyword              = document.getElementById("keywords").value;
    var positivity           = document.getElementById("positivity").checked;
    var negativity           = document.getElementById("negativity").checked;
    var live_stream          = document.getElementById("live").checked;
    var past_stream          = document.getElementById("past-week").checked;
    var upper_bound          = document.getElementById("upper-bound").innerHTML;
    var lower_bound          = document.getElementById("lower-bound").innerHTML;
    var days_in_past         = document.getElementById("day-count").innerHTML;
    var retweets             = document.getElementById("retweets").checked;
    var ajax_new_request     = true; // This value tells the backend whether to do a new search or just update current search.
    var session_id           = readCookie("session_id");

    var Page_Data = {
                    "keyword"               : keyword,
                    "positivity"            : positivity, 
                    "negativity"            : negativity, 
                    "live_stream"           : live_stream, 
                    "past_stream"           : past_stream,
                    "upper_bound"           : upper_bound,
                    "lower_bound"           : lower_bound,
                    "retweets"              : retweets,
                    "ajax_new_request"      : ajax_new_request,
                    "session_id"            : session_id,
                    "days_in_past"          : days_in_past
    };

    document.getElementById("time").innerHTML   = "";
    document.getElementById("reload").innerHTML = "";

    if( timer_started === true ) {
        clearInterval(timer);
        timer_started = false;
    }

    // Disables the button and input field.
    document.getElementById('keywords').disabled        = true;
    document.getElementById('init-ajax').disabled       = true;
    document.getElementById("init-ajax").style.cursor   = "default";

    // Animates the input fields - makes them grey and spins the logo.
    $("#init-ajax").animate({backgroundColor: "#f0f0f0" });
    $("#logo").toggleClass("loading");
    
    time                        = initial_time;
    timer_started               = true;

    ajaxRequest(Page_Data);

    Page_Data.ajax_new_request  = false;



    timer = setInterval(function(){

        document.getElementById("time").innerHTML = time;

        if( time % refresh_interval == 0 ) {
            ajaxRequest(Page_Data);
        }

        if(time < 0) {
            document.getElementById("time").innerHTML = "";
            clearInterval(timer);
        }

        time -= 1;

    }, 1000);



    
    // After 4 seconds allow them to make a new query.
    setTimeout(function( ) {
        document.getElementById('keywords').disabled                = false;
        document.getElementById('init-ajax').disabled               = false;
        document.getElementById("init-ajax").style.cursor           = "pointer";
        document.getElementById('init-ajax').style.color            = "white";
        document.getElementById('init-ajax').style.backgroundColor  = "";
        document.getElementById('init-ajax').style.borderColor      = "";
        $("#init-ajax").stop();
        $("#logo").toggleClass("loading");
    }, (refresh_interval * 1000) );

    // The timer expires after 20 seconds.
    setTimeout(function( ) {
        timer_started = false;
    }, search_time);
}



