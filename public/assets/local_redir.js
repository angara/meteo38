var st_list = window.localStorage.getItem("st_list");
if(st_list) { window.location.assign( window.location.protocol+"//"+window.location.host+location.pathname+"?st_list="+st_list ); };
