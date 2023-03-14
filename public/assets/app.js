//
//  meteo38 client js
//

var st_list = (window.localStorage.getItem("st_list") || "").split(",");
var selected_station = "";


function save_and_reload(st_list) {
  var stl = (st_list || []).join(",");
  window.localStorage.setItem("st_list", stl);
  var url = window.location.protocol+"//"+window.location.host+window.location.pathname+"?st_list="+stl;
  window.history.replaceState(null, document.title, url);

  console.log("save st_list:", stl);
  // !!!
  htmx.ajax()

}

function update_button(sel_st) {
  if(sel_st){
    if(st_list.includes(sel_st)) {
      console.log("remove button")
      //
    } else {
      console.log("insert button")
      //
    }
  } else {
    console.log("disabled button")
    //
  }
}

function options_onchange(target){
  var idx = target.selectedIndex;
  if(idx !== undefined) {
    console.log("opt.v:", target.options[idx].value);
    selected_station = target.options[idx].value;
    update_button(selected_station);
  }
}

function insert_station() {
  var st = selected_station;
  //
  st_list = st_list.filter((val)=>{ return val != st; });
  st_list.unshift(st);
  save_and_reload(st_list);
}

function remove_station() {
  var st = selected_station;
  //
  st_list = st_list.filter((val)=>{ return val != st; });
  save_and_reload(st_list);
}


function display_options_block() {

  console.log("display_options_block");
}


//.
