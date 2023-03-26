var st_list = window.localStorage.getItem("st_list");
if(!st_list) { 
  st_list = "irgp,soln,uiii,npsd,istok,olha,khomutovo,khrust_park,mamai_sphera"; 
  window.localStorage.setItem("st_list",st_list); 
};
window.location.assign( window.location.protocol+"//"+window.location.host+location.pathname+"?st_list="+st_list ); 
