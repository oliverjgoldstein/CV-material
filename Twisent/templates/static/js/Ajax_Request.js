
// @Param: page_data: An associative array of parameters (an object with different properties)
function ajaxRequest( PageData ) {

    jQuery.ajax({

        type: "POST",
        data: {
            keyword             : PageData.keyword,
            positivity          : PageData.positivity,
            negativity          : PageData.negativity,
            live_stream         : PageData.live_stream,
            past_stream         : PageData.past_stream,
            upper_bound         : PageData.upper_bound,
            lower_bound         : PageData.lower_bound,
            retweets            : PageData.retweets,
            ajax_new_request    : PageData.ajax_new_request,
            session_id          : PageData.session_id,
            days_in_past        : PageData.days_in_past
         },

        success: function(data) {
            json_decode(data);
        },
    });

    return false;
}