
// @Param: url: A linkt to the tweet
// @Param: tagetDiv, the unique ID of the div which will contain the result
function injectTweet(url, targetDiv ) {

    jQuery.ajax({
            url: 'https://publish.twitter.com/oembed?url='+url,
            dataType: "jsonp",
            success: function(data){
                document.getElementById(targetDiv).innerHTML = data.html; 
            }
        });

    return false;
}