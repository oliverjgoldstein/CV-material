
// Global Variables
var timer_started    = false;
var timer            = undefined;
var time             = 0;
var search_time      = 20000;   // In ms.
var initial_time     = 20;      // In Seconds.
var refresh_interval = 3;       // Check Timer and Ajax Call for info on how this is used.
var upper_bound      = 100000
var lower_bound      = 0;

var tweet_count      = 0;

// This sets the slider values.
window.onload = function(){ 
    document.getElementById("upper-bound").innerHTML = String(upper_bound); 
    document.getElementById("lower-bound").innerHTML = String(lower_bound);
}

function readCookie(k)
{
    return(document.cookie.match('(^|; )'+k+'=([^;]*)')||0)[2]
}