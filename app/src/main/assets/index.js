let map,infoWindow ;
const image =
         "https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png";

function initMap() {
    let options =  {
        zoom: 20,
        mapTypeId: 'roadmap',
        disableDefaultUI: true
    };

    // The map, centered at Uluru
    map = new google.maps.Map(document.getElementById("map"), options);

    infoWindow =  new google.maps.InfoWindow();

    //확대/축소 수준을 의미하는데 여기선 45도로 기울어지는 map을 막고자 설정함
    //0으로 지정한 경우 기울기 0도로 지정
    map.setTilt(0);

    initLocation()
}

function initLocation() {
    if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(initMarker)
    }
}

function initMarker(position) {
    let lat = position.coords.latitude;
    let lng = position.coords.longitude;

    //마커 설정
    marker = new google.maps.Marker({
      position: {lat,lng},
      map: map,
    });

     marker.addListener('click', (function(marker) {
           return function() {
               if (marker.getAnimation() !== null) {
                   marker.setAnimation(null);
               } else {
                   marker.setAnimation(google.maps.Animation.BOUNCE);
               }
           }
     })(marker));

    map.setCenter(new google.maps.LatLng(lat, lng));
}

function getCurrentLocation() {
    if(navigator.geolocation) {
       navigator.geolocation.getCurrentPosition(callbackCurrentLocation)
    }
}

function callbackCurrentLocation(position){
    let lat = position.coords.latitude;
    let lng = position.coords.longitude;

    let obj = new Object();
    obj.id = -1;
    obj.latitude = lat;
    obj.longitude = lng;
    obj.date = -1;

    let json = JSON.stringify(obj)

    Android.currentLocationCallback(json)
}

function showHistories(data){
    let jsonArray = JSON.parse(data)

    for(var i= 0 ; i< jsonArray.length; i++) {
        let location = jsonArray[i];
        let lat = location.latitude +i
        let lng = location.longitude +i

        createMarker(lat,lng)
    }
}

function createMarker(lat,lng){
    let marker = new google.maps.Marker({
        position: {lat,lng},
        map: map,
        icon: image
    });

    marker.addListener('click', (function(marker) {
        return function() {
            if (marker.getAnimation() !== null) {
                marker.setAnimation(null);
            } else {
                marker.setAnimation(google.maps.Animation.BOUNCE);
            }
        }
    })(marker));
}

window.initMap = initMap;