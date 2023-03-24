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

function update_button(sel_st) {
  var btn = document.getElementById("btn_st_action");
  if(sel_st){
    var st_list = get_st_list();
    if(st_list.includes(sel_st)) {
      btn.dataset.action = "remove"
      btn.dataset.st = sel_st
      btn.innerHTML = "&#215;"
      btn.classList.remove("invisible");
    } else {
      btn.dataset.action = "insert"
      btn.dataset.st = sel_st
      btn.innerHTML = "&plus;"
      btn.classList.remove("invisible");
    }
  } else {
    btn.dataset.action = ""
    btn.dataset.st = ""
    btn.innerHTML = "*"
    btn.classList.add("invisible");
  }
}

function options_onchange(target){
  var idx = target.selectedIndex;
  if(idx !== undefined) {
    var selected_station = target.options[idx].value;
    update_button(selected_station);
  }
}

function insert_station(st) {
  var st_list = get_st_list();
  st_list = st_list.filter((val)=>{ return val != st; });
  st_list.unshift(st);
  save_and_reload(st_list);
}

function remove_station(st) {
  var st_list = get_st_list();
  st_list = st_list.filter((val)=>{ return val != st; });
  save_and_reload(st_list);
}

function options_update(btn) {
  switch(btn.dataset.action) {
    case "insert": insert_station(btn.dataset.st); break;
    case "remove": remove_station(btn.dataset.st); break;
  };
  update_button(null);
}

function display_options_block() {
  htmx.ajax('GET', "/options", {target:"#options_block",swap:"innerHTML"}).then(() => {
    // console.log("options_block loaded")
  })
}

//.
