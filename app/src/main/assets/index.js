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

    getLocation()
}

function getLocation() {
    if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition)
    }
}

function showPosition(position) {
    var lat = position.coords.latitude;
    var lng = position.coords.longitude;

    //마커 설정
    marker = new google.maps.Marker({
      position: {lat,lng},
      map: map,
    });

    marker.addListener("click", (function(marker){
        if (marker.getAnimation() !== null) {
          marker.setAnimation(null);
        } else {
          marker.setAnimation(google.maps.Animation.BOUNCE);
        }
    }));

//    lat += 2
//    lng += 2
//
//    const marker1 = new google.maps.Marker({
//      position: {lat,lng},
//      map: map,
//      icon: image
//    });
//
//    marker1.addListener("click", toggleBounce)

    map.setCenter(new google.maps.LatLng(lat, lng));
}

function toggleBounce() {
    if (marker.getAnimation() !== null) {
      marker.setAnimation(null);
    } else {
      marker.setAnimation(google.maps.Animation.BOUNCE);
    }
}

function getLocations(data){
    let jsonArray = JSON.parse(data)

//    for(var i= 0 ; i< jsonArray.locations.length; i++) {
//        let location = jsonArray.locations[i];
//        let lat = location.lat
//        let lng = location.lng
//
//        createMarker(lat,lng)
//    }

}

function createMarker(lat,lng){
    let marker = new google.maps.Marker({
        position: {lat,lng},
        map: map,
        icon: image
    });
}

window.initMap = initMap;