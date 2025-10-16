fetch(`/map/data?festival_idx=${festival_idx}`)
    .then(response => response.json())
    .then(data => {
        let festival_lat = data.festival_lat;
        let festival_lot = data.festival_lot;
        let mapOptions = {
            center: new naver.maps.LatLng(festival_lat, festival_lot),
            zoom: 17
        };

        let map = new naver.maps.Map('map', mapOptions);

        naver.maps.Event.addListener(map, 'click', function(e){
            let lat = e.coord.lat();
            let lng = e.coord.lng();
            let url = `https://map.naver.com/v5/search/${lat},${lng}`;
            window.open(url, '_blank');
        });
    })
    .catch(error => {
        console.error(error);
    });