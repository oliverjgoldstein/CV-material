$(document).ready(function(){
  
  var clogin = $("#page-one");
  var cregister = $("#page-two");

  // $("#wrapper").height(800).css({});
  
  /* display the options page */
  $("#next-page").on("click", function(e){
    e.preventDefault();
    var newheight = cregister.height();
    $(cregister).css("display", "block");
    
    $(clogin).stop().animate({
      "left": "-1480px"
    }, 800, function(){ /* callback */ });
    
    $(cregister).stop().animate({
      "left": "0px"
    }, 800, function(){ $(clogin).css("display", "none"); });
    
    $("#wrapper").stop().animate({
      "height": newheight+"px"
    }, 550, function(){ /* callback */ });
  });
  
  /* display the find tweet page */
  $("#show-page-one").on("click", function(e){
    e.preventDefault();
    var newheight = clogin.height()+400;
    $(clogin).css("display", "block");
    
    $(clogin).stop().animate({
      "left": "0px"
    }, 800, function() { /* callback */ });
    $(cregister).stop().animate({
      "left": "1480px"
    }, 800, function() { $(cregister).css("display", "none"); });
    
    $("#wrapper").stop().animate({
      "height": newheight+"px"
    }, 550, function(){ /* callback */ });
  });
  $("#post2").submit(function(e) {
    
  });
});