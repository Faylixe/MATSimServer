$(document).ready(function() {
  var bodyHeight = $("body").height();
  var vwptHeight = $(window).height();
  if (vwptHeight > bodyHeight) {
    $("footer").css("position","absolute").css("bottom",0);
  } 
});