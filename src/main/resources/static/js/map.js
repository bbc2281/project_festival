fetch(`/api/data?festival_idx=${festival_idx}`)
    .then(response => response.json())
    .then(data => {
        let festival_lat = data.festival_lat;
        let festival_lot = data.festival_lot;
        let mapOptions = {
            center: new naver.maps.LatLng(festival_lat, festival_lot),
            zoom: 17,
            maxZoom: 19,
            minZoom: 15,
            zoomControl: true,
            zoomControlOptions: {
                position: naver.maps.Position.TOP_RIGHT
            }
        };

        let map = new naver.maps.Map('map', mapOptions);

        let marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(festival_lat, festival_lot),
            map : map
        });

        naver.maps.Event.addListener(map, 'click', function(e){
            let url = `https://map.naver.com/v5/search/${festival_lat},${festival_lot}`;
            window.open(url, '_blank');
        });
    })
    .catch(error => {
        console.error(error);
    });