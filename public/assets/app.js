//
//  meteo38 client js
//

function get_st_list() {
  var s = window.localStorage.getItem("st_list");
  if(s) { return s.split(",").filter(x => x); }
  else { return []; }
}

function load_data_block(stl) {
  htmx.ajax("GET", 
    window.location.protocol+"//"+window.location.host+"/data?st_list="+stl,
    {target:"#data-block", swap:'outerHTML'}
  );
}

function save_and_reload(st_list) {
  var stl = (st_list || []).join(",");
  window.localStorage.setItem("st_list", stl);
  load_data_block(stl);
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
  htmx.ajax('GET', "/options", {target:"#options_block",swap:"innerHTML"});
}

function st_item_click(elem) {
  var st = elem.dataset.st;
  if(!st) { 
    return; 
  }
  var sel = document.getElementById("sel_stations");
  if(sel) {
    sel.value = st;
  }
  htmx.ajax('GET', "/svgraph?st="+st, {target:"#svgraph_"+st, swap:"innerHTML"});
}

//

if(window.initial_load) {
  load_data_block(window.localStorage.getItem("st_list"));
}

//.
