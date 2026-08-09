
function append( element, text ) {
    //element.innerHTML += text;
    //does real appending - preserving styles etc...

    var temp = document.createElement('div');
    temp.innerHTML = text;

    while (temp.firstChild){
        element.appendChild(temp.firstChild);
    }

}

function is_checked( element ) {
    return document.getElementById(element).checked;
}

function retweeter_find() {
    alert("Not available in demo mode. Contact Us For This Feature :)");
    return false;
}

// 100 - No data returned yet.
// 101 - Server Error
// 102 - All results returned.
// 103 - Query Complete
// 104 - Ajax Post Error
// 105 - Please enter a sentiment
// 106 - Please enter a stream type
// 107 - Please enter a longer query
// 108 - Please enter a correct slider value

// This parses the data and prints it to the screen.
function json_decode(data) {
    

    // This removes slashes from within the JSON strings.
    data = data.replace(/\\\\u/, " un1cod3 ");
    data = data.replace(/\\/g, "");
    // data = data.replace("un1cod3", "\\U+");
    if ( data.substr(0, 1) == '"' ) {
        // This removes the first and last quote characters inherited from db query.
        data = data.substr(1, (String(data).length - 2));
    }



    var reload = document.getElementById('reload');
    var status = document.getElementById('status-text');

    //debug
    //data = 0;

    if( data == 100 ) {
        status.innerHTML = '<br /><span class = "server-message info">Still Loading... <img src = "/static/img/spinner.gif" id = "spinner" /></span>';
    } else if( data == 101 ) {
        status.innerHTML = '<br /><span class = "server-message bad">Server Error.</span>';
    } else if( data == 102 ) {
        status.innerHTML = '<br /><span class = "server-message good">All results returned. <img src = "/static/img/down_arrow.gif" id = "spinner" style = "margin-bottom:-3px;" /></span>';
    } else if( data == 103 ) {
        status.innerHTML = '<br /><span class = "server-message good">Query Complete.</span>';
    } else if( data == 104 ) {
        status.innerHTML = '<br /><span class = "server-message bad">Ajax Post Error.</span>';
    } else if( data == 105 ) {
        status.innerHTML = '<br /><span class = "server-message bad">Please enter a sentiment.</span>';
    } else if( data == 106 ) {
        status.innerHTML = '<br /><span class = "server-message bad">Please enter a stream type.</span>';
    } else if( data == 107 ) {
        status.innerHTML = '<br /><span class = "server-message bad">Please enter a longer query.</span>';
    } else if( data == 108 ) {
        status.innerHTML = '<br /><span class = "server-message bad">Please enter a correct slider value.</span>';
    } else {
        try {
            var tweet_data = JSON.parse(data);
            //DEBUG
            /*var tweet_data = {
                tweets: 
                [
                    [{
                        sentiment : "Negative Sentiment", 
                        stream_type: "Live Stream", 
                        is_retweet: true,
                        tweet_id: "1", 
                        user_img_https_link: "",
                        user_description: ""
                    }]
                ]
            };*/
        } catch (err) {
            status.innerHTML = '<br /><span class = "server-message bad">Parsing Error!</span>';
        }
    }
    try {
        var number_of_tweets = tweet_data.tweets.length;
    } catch (err) {
        console.log("No tweets to display yet.");
    }

    if( number_of_tweets != undefined ) {

        for( var tweet_looper = 0; tweet_looper < number_of_tweets; tweet_looper++ ) {
            tweet_count += 1;
            append_data = "";
    
            if( tweet_data.tweets[tweet_looper][0].sentiment == "Negative Sentiment" ) {
               // append_data += '<div class = "negative box">';
            } else {
               // append_data += '<div class = "positive box">';
            }
    
            
            append_data += '<div class = "main">';
    
            append_data += '<div class = "tweet-data">';

            // This is the text area:
            link_data    = 'https://twitter.com/anytweet/status/' + tweet_data.tweets[tweet_looper][0].tweet_id;
            append_data += '<a href = "' + link_data + '" target = "_blank" class = "tweet_url">';
            append_data += '<br /><img class = "tweeter-profile-img" src = "' + tweet_data.tweets[tweet_looper][0].user_img_https_link + '" />';
            
            append_data += '<div class="pic-div">'

            // This is where the images on the left appear:
            if( tweet_data.tweets[tweet_looper][0].stream_type == "Live Stream" ) {
                left_img     = true;
                append_data += '<img src = "/static/img/live_tweet.png" class = "live-tweet" />';
            }
    
            if( tweet_data.tweets[tweet_looper][0].is_retweet == true ) {
                left_img     = true;
                append_data += '<img src = "/static/img/is_retweet.svg" class = "retweet" />';
            }
            append_data += '</div>'

            append_data += '<table>';
            append_data += '<tr><td><span>Author: </span></td><td><span class= "tweet-author">' + tweet_data.tweets[tweet_looper][0].name + '</span></td></tr>';    
            
            if( is_checked("attitude-box") == true ) {
                if( tweet_data.tweets[tweet_looper][0].sentiment == "Negative Sentiment" ) {
                    emoji_type   = " :( (Sad Emoji Substitution) ";
                    append_data += '<tr><td><span>Attitude: </span></td><td><span class = "negative-text"> Negative</span></td></tr>';
                } else {
                    emoji_type   = " :) (Happy Emoji Substitution) ";
                    append_data += '<tr><td><span>Attitude: </span></td><td><span class = "positive-text"> Positive</span></td></tr>';
                }
            }
            
    

    

    
            append_data += `<tr><td><span class = "tweet-text">Tweet:</span></td><td> <span class = "tweet-content" id="tweet-${String(tweet_count)}"></span></td></tr>` //+ (tweet_data.tweets[tweet_looper][0].tweet).replace("un1cod3", emoji_type) + "
            injectTweet(link_data, `tweet-${String(tweet_count)}`)
            if ( String(tweet_data.tweets[tweet_looper][0].tweet_location).trim() ) {
                tweet_location = tweet_data.tweets[tweet_looper][0].location;
            } else {
                tweet_location = "Unknown";
            }
    
            if( is_checked("location-box") == true ) {
                append_data += '<tr><td><span class = "location">Location:</span></td><td> ' + tweet_location + '</td></tr>';
            }
    
            if( is_checked("date-box") == true ) {
                append_data += '<tr><td><span class = "date">Date Posted:</span></td><td> ' + tweet_data.tweets[tweet_looper][0].date + '</td></tr>';
            }
    
            if ( tweet_data.tweets[tweet_looper][0].retweet_count > 0 ) {
                retweet_count = String( tweet_data.tweets[tweet_looper][0].retweet_count ) + '<a href = "#"><button class = "other-retweeters" onclick = "retweeter_find()">Display Status Retweeters</button></a>';
            } else {
                retweet_count = "No Retweets";
            }
    
            if( is_checked("retweet-box") == true ) {
                append_data += '<tr><td><span class = "re-count">Retweet Count <span style = "font-size:8px">(Original Tweet)</span>:</span></td><td> ' + retweet_count + '</td></tr>';
            }
    
            append_data += '</table>';
    
            append_data += '<div style = "display:none;">Originator ID: '+ tweet_data.tweets[tweet_looper][0].originator_id + '</div>';
    
            if( is_checked("user-box") == true ) {
                append_data += `<input class="user-box-toggle" id="user-box-toggle-${String(tweet_count)}" type="checkbox" > <label class="user-box-toggle-label" for="user-box-toggle-${String(tweet_count)}">User info</label>`
                append_data += '<div class = "user-box">';
                append_data += '<br />';
                append_data += '<table style = "margin-top:-23px;">';
                append_data += '<tr><td><tr><td><span class = "name">Name:</span></td><td> '                             + tweet_data.tweets[tweet_looper][0].name;
                append_data += '</td></tr><tr><td><span class = "followers">Followers:</span></td><td> '                 + tweet_data.tweets[tweet_looper][0].followers;
    
                if ( String(tweet_data.tweets[tweet_looper][0].user_description).trim() ) {
                    user_description = (tweet_data.tweets[tweet_looper][0].user_description).replace("un1cod3", emoji_type);
                } else {
                    user_description = "Unknown";
                }
    
                append_data += '</td></tr><tr><td><span class = "prof-desc">Profile Description:</span></td><td> '   + user_description;
                append_data += '</td></tr><tr><td><span class = "screen-name">Screen name:</span></td><td> '   + tweet_data.tweets[tweet_looper][0].screen_name;
                append_data += '</td></tr></table></div>';
            }
    
            append_data += '</a>';

            append_data += '</div>';
            append_data += '</div>';
            append_data += '</div>';
            append_data += '</div>';
            append( reload, append_data );
        }
    }
}



