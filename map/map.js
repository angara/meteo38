

import {Map, View, Feature, Overlay} from 'ol'
import {Point} from 'ol/geom'
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer'
import {OSM} from 'ol/source'
import {Circle, Fill, Stroke, Text, Style} from 'ol/style'
import {fromLonLat} from 'ol/proj'
import {Control} from 'ol/control'
import {Vector as VectorSource} from 'ol/source'


// // // // // // // //

const ST_LINK_BASE = 'https://angara.net/meteo/st/'
const INIT_CENTER = [104.3, 52.24]
const INIT_ZOOM = 11

const logo_png = "/assets/meteo38_logo.png"

function active_stations() {
  return fetch(
    "http://rs.angara.net/meteo/api/active-stations?last-hours=4",
    {} // headers: {"Autorization" "Basic ..."}
  )
  .then((resp) => resp.json())
  .then((data) => {
    console.log("resp.json:", data);
    return data;
  })
  .catch((err) => { 
    console.warn("active-stations:", e); 
  });
}


// // // // // // // //

const labels_source = new VectorSource({features: []})

const map = new Map({
  target: 'map',
  layers: [
    new TileLayer({source: new OSM()}), 
    new VectorLayer({source: labels_source})
  ],
  view: new View({
    center: fromLonLat(INIT_CENTER),
    zoom: INIT_ZOOM
  })
})


const logo_div = document.createElement('div')
logo_div.className = 'ol-control ol-unselectable st_logo'
logo_div.innerHTML = "<a href='/'><img src='"+logo_png+"'></a>"
map.addControl(new Control({element:logo_div}))


const feature_style = new Style({
 image: new Circle({
   fill: new Fill({color: 'rgba(57255,255,240,0.3)'}),
   stroke: new Stroke({color: "rgb(57,134,174)", width: 2}),
   radius: 5,
 }),
 fill: new Fill({color: 'rgba(57255,255,240,0.3)'}),
 stroke: new Stroke({color: "rgb(57,134,174)", width: 2}),
})


function make_st_feature(st) {
  return new Feature({
    geometry: new Point(fromLonLat([st.lon, st.lat])),
    name: st.st+" - "+st.title,
    style: feature_style
  })
}


// // // // // // // //

let st_ovl_last_zindex = 1


function format_t(t, delta, ts) {
  if(t == undefined) { return ""; }

  t = Math.round((t + Number.EPSILON) * 10) / 10;

  let units = "<span class='st_label_units'>&deg;C</span>";

  let dt = "";
  if( delta > 0.1) {
    dt = "<span class='st_label_plus'>&uarr;</span>";
  } else if(delta < -0.1) {
    dt = "<span class='st_label_minus'>&darr;</span>";
  }

  let out_cls = "";
  if( Date.now() - (new Date(ts).getTime()) > (1*3600*1000)) {
    out_cls = "outdated";
  }
  
  if(t > 0) {
    return "<span class='st_label_plus "+out_cls+"'>+"+t+"</span>" + units + dt;
  } else if (t < 0) {
    return "<span class='st_label_minus "+out_cls+"'>-"+(-t)+"</span>" + units +dt;
  } else {
    return "<span class='st_label_zero "+out_cls+"'>"+t+"</span>" + units + dt;
  }
}

function format_ts(ts) {
  let dt = new Date(ts);
  if(!dt) { return ""; }
  return dt.toLocaleString("ru-RU");
}

function st_label_onclick(evt) {
  evt.currentTarget.parentElement.style.zIndex = st_ovl_last_zindex++;
}


function map_label_overlay(map, st) {
  let last = st['last'] || {};
  const id = st['st'];

  let old_ovl = map.getOverlayById(id);
  if(old_ovl) {
    map.removeOverlay(old_ovl);
  }
  let elem = document.createElement("div");
  elem.className = "st_label";
  let st_link = ST_LINK_BASE+st['st'];
  elem.insertAdjacentHTML("afterbegin", 
    "<div class='st_label_title'>"+
      "<a href='"+st_link+"' target='_blank'>"+st['title']+"</a></div>"+
    "<div class='st_label_val' title='"+format_ts(last['t_ts'])+"'>"+ 
      format_t(last['t'], last['t_delta'], last['t_ts']) +"</div>"
  );
  elem.onclick = st_label_onclick;
  let ovl = new Overlay({
    id: id,
    position: fromLonLat([st['lon'], st['lat']]),
    positioning: 'bottom-center', // 'center-left',
    className: "ol-overlay-container st_label_ovl",
    element: elem,
    autoPan: false
  });
  map.addOverlay(ovl);
}

function refresh_stations(data) {
  for(let st of data['stations']) {
    map_label_overlay(map, st);
  }
}


// // // // // // // //

active_stations().then((data) => {
  // NOTE: labels added only once at start
  for(let st of data['stations']) {
    labels_source.addFeature(make_st_feature(st));
  }
  refresh_stations(data);
})

setInterval(function() {
  active_stations().then((data) => {
    refresh_stations(data);
  });
}, (10*60*1000)); // 10 minutes refresh

//.
