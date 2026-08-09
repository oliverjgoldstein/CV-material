// Hopefully easier to read in this format

$(document).ready(function(){

    $('#positivity').click(function () {
        $("#positivity-text").toggleClass("green");
    });

    $('#negativity').click(function () {
        $("#negativity-text").toggleClass("green");
    });

    $('#retweet-box').click(function () {
        $("#re-count-text").toggleClass("green");
    });

    $('#live').click(function () {
        $("#live-text").toggleClass("green");
    });
  
    $('#past-week').click(function () {
        $("#past-week-text").toggleClass("green");
    });

    $('#retweets').click(function () {
        $("#retweet-text").toggleClass("green");
    });

    $('#user-box').click(function () {
        $("#user-text").toggleClass("green");
    });

    $('#attitude-box').click(function () {
        $("#attitude-text").toggleClass("green");
    });

    $('#tweet-box').click(function () {
        $("#tweet-text").toggleClass("green");
    });

    $('#sentiment-box').click(function () {
        $("#sentiment-text").toggleClass("green");
    });

    $('#location-box').click(function () {
        $("#location-text").toggleClass("green");
    });

    $('#date-box').click(function () {
        $("#date-text").toggleClass("green");
    });

});