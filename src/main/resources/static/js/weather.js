fetch(`/api/data?festival_idx=${festival_idx}`)
    .then(response => response.json())
    .then(data => {
        let festival_lat = data.festival_lat;
        let festival_lot = data.festival_lot;
        console.log(festival_lat, festival_lot);
        const api_key = '94c3ddc4782d940f73f3e3bd83269abb';
        weather_url = `http://api.openweathermap.org/data/2.5/forecast?lang=kr&lat=${festival_lat}&lon=${festival_lot}&id=524901&appid=${api_key}&cnt=29&units=metric`;
        fetch(weather_url)
            .then(response => response.json())
            .then(data2 => {
                console.log(data2);
                weather_icon = data2.list[0].weather[0].icon;
                console.log(weather_icon);
                let dates = [];
                data2.list.forEach(element => {
                    let String = element.dt_txt;
                    let date = new Date(String + " UTC");
                    dates.push(date.toLocaleString('ko-KR'));
                });
                document.getElementById(`weather_time`).innerText = dates[0];
                document.getElementById(`weather_temp`).innerText = `${data2.list[0].main.temp}℃`;
                document.getElementById(`weather_icon`).innerHTML = `<img src="https://openweathermap.org/img/wn/${weather_icon}@2x.png">`
                
            })
            .catch(error => {
                console.error(error);
            });
    })
    .catch(error => {
        console.error(error);
    });
