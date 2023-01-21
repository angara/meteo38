
var st_list = window.localStorage.getItem("st_list");
console.log("st_list:", st_list);
if(st_list) {
  console.log("redirect to");
  window.location.assign(location.href+"?st_list="+st_list);
}