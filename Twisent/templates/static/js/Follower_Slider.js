$(function() {
    $( "#slider-range" ).slider({
      range: true,
      min: 0,
      max: 100000,
      step: 100,
      values: [ 0, 100000 ],
      slide: function( event, ui ) {
        $('#upper-bound').html(ui.values[1]);
        $('#lower-bound').html(ui.values[0]);
        if((ui.values[ 0 ] == ui.values[ 1 ]) && ((ui.values[0] == 0) || (ui.values[0] == 100000))) {
            $( "#follower-number" ).val("No Minimum - No Maximum");
        } else if((ui.values[ 0 ] == 0) && (ui.values[ 1 ] == 100000)) {
            $( "#follower-number" ).val( "No Minimum - No Maximum");
        } else if((ui.values[ 0 ] == 0) && (ui.values[ 1 ] != 100000)) {
            $( "#follower-number" ).val( "No Minimum - "+ ui.values[1]);
        } else if((ui.values[ 0 ] != 0) && (ui.values[ 1 ] == 100000)) {
            $( "#follower-number" ).val(ui.values[0] + " - No Maximum");
        } else {
            $( "#follower-number" ).val( "" + ui.values[ 0 ] + " - " + ui.values[ 1 ] );
        }
      }
    });
    $( "#follower-number" ).val("No Minimum - No Maximum");
});

$( function() {
    $( "#slider-days" ).slider({
      range: "min",
      value: 7,
      min: 1,
      max: 7,
      slide: function( event, ui ) {
        $('#day-count').html(ui.value);
        $( "#past-day-count" ).val( "How far in the past? Up to " + ui.value + " Days" );
      }
    });
    $('#day-count').html($( "#slider-days" ).slider( "value" ));
    $( "#past-day-count" ).val( "How far in the past? Up to " + $( "#slider-days" ).slider( "value" ) + " Days" );
  } );