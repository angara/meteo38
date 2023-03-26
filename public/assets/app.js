//
//  meteo38 client js
//

var global_st_list = (window.localStorage.getItem("st_list") || "").split(",");
var selected_station = "";

function get_st_list() {
  return (window.localStorage.getItem("st_list") || "").split(",");
}

function save_and_reload(st_list) {
  var stl = (st_list || []).join(",");
  window.localStorage.setItem("st_list", stl);
  var url = window.location.protocol+"//"+window.location.host+"/?st_list="+stl;
  window.history.replaceState(null, document.title, url);
  htmx.ajax("GET", 
    window.location.protocol+"//"+window.location.host+"/data?st_list="+stl,
    {target:"#data-block", sawp:"outerHTML"}
  );
}

function stlist_update(action) {
  var sel = document.getElementById("sel_stations");
  var st = sel.options[sel.selectedIndex].value;

  if(!st) { return; }

  var st_list = get_st_list();
  st_list = st_list.filter((val)=>{ return val != st; });

  switch(action) {
    case "top": st_list.unshift(st); break;
    case "bottom": st_list.push(st); break;
    case "remove": break;
  };
  save_and_reload(st_list);
}

function display_options_block() {
  if( document.getElementById("sel_stations") ) {
    document.getElementById("options_block").innerHTML = "";
    return;
  }

  htmx.ajax('GET', "/options", {target:"#options_block",swap:"innerHTML"}).then(() => {
    // console.log("options_block loaded")
  })
}

function select_options(elem) {
  var sel = document.getElementById("sel_stations");
  var st = elem.dataset.st;
  if(sel && st) {
    sel.value = st;
  }
}

//.
