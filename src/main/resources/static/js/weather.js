fetch(`/api/data?festival_idx=${festival_idx}`)
    .then(response => response.json())
    .then(data => {
        let festival_lat = data.festival_lat;
        let festival_lot = data.festival_lot;

        fetch(`http://localhost:8000/weather?lat=${festival_lat}&lon=${festival_lot}`)
        .then(response => {
          if (!response.ok) throw new Error('네트워크 오류');
          return response.json();
        })
        .then(data2 => {
          const items = data2.response.body.items.item;
          const summary = parseWeatherData(items);

          const today = summary[0];
          const tomorrow = summary[1];
          const dat = summary[2];

          // 오늘
          document.getElementById('weather_time_today').innerHTML = today.date;
          document.getElementById('weather_sky_today').innerHTML = weather(today.pty, today.sky);
          document.getElementById('weather_temp_min_today').innerHTML = today.min;
          document.getElementById('weather_temp_max_today').innerHTML = today.max;

          // 내일
          document.getElementById('weather_time_tomorrow').innerHTML = tomorrow.date;
          document.getElementById('weather_sky_tomorrow').innerHTML = weather(tomorrow.pty, tomorrow.sky);
          document.getElementById('weather_temp_min_tomorrow').innerHTML = tomorrow.min;
          document.getElementById('weather_temp_max_tomorrow').innerHTML = tomorrow.max;

          // 모레
          document.getElementById('weather_time_dat').innerHTML = dat.date;
          document.getElementById('weather_sky_dat').innerHTML = weather(dat.pty, dat.sky);
          document.getElementById('weather_temp_min_dat').innerHTML = dat.min;
          document.getElementById('weather_temp_max_dat').innerHTML = dat.max;
        })
        .catch(error => {
          console.error(error);
        });
    })
    .catch(error => {
        console.error(error);
    });

function parseWeatherData(items) {
  const today = new Date();
  const formatDate = (date) => {
    return date.getFullYear().toString() +
      String(date.getMonth() + 1).padStart(2, '0') +
      String(date.getDate()).padStart(2, '0');
  };

  const dates = [
    formatDate(today),
    formatDate(new Date(today.getTime() + 24 * 60 * 60 * 1000)),     // 내일
    formatDate(new Date(today.getTime() + 2 * 24 * 60 * 60 * 1000))  // 모레
  ];

  // 결과 저장소 초기화
  let weatherSummary = {
    [dates[0]]: { TMN: null, TMX: null, SKY: null, PTY: null },
    [dates[1]]: { TMN: null, TMX: null, SKY: null, PTY: null },
    [dates[2]]: { TMN: null, TMX: null, SKY: null, PTY: null }
  };

  items.forEach(item => {
    const { category, fcstDate, fcstValue } = item;

    if (dates.includes(fcstDate)) {
      if (category === "TMN") {
        // 최저기온 (낮은 값 중 가장 낮은 걸로 업데이트)
        if (weatherSummary[fcstDate].TMN === null || fcstValue < weatherSummary[fcstDate].TMN) {
          weatherSummary[fcstDate].TMN = fcstValue;
        }
      } else if (category === "TMX") {
        // 최고기온 (높은 값 중 가장 높은 걸로 업데이트)
        if (weatherSummary[fcstDate].TMX === null || fcstValue > weatherSummary[fcstDate].TMX) {
          weatherSummary[fcstDate].TMX = fcstValue;
        }
      } else if (category === "SKY") {
        // 하늘 상태 (보통 첫값 저장)
        if (weatherSummary[fcstDate].SKY === null) {
          weatherSummary[fcstDate].SKY = fcstValue;
        }
      } else if (category === "PTY") {
        // 강수 형태 (첫값 저장)
        if (weatherSummary[fcstDate].PTY === null) {
          weatherSummary[fcstDate].PTY = fcstValue;
        }
      }
    }
  });
  
  return Object.entries(weatherSummary).map(([date, info]) => ({
    date: `${date.substring(4, 6)}월 ${date.substring(6, 8)}일`,
    min: info.TMN,
    max: info.TMX,
    sky: info.SKY,
    pty: info.PTY,
  }));
}

function weather(pty, sky){
    if(pty == 0){
        return notRain(sky);
    }else if(pty == 1){
        return `🌧️ 비`;
    }else if(pty == 2){
        return `🌧️🌨️ 비/눈`;
    }else if(pty == 3){
        return `🌨️ 눈`;
    }else{
        return `🌦️ 소나기`;
    }
}

function notRain(sky){
    if(sky == 1){
        return `☀️ 맑음`;
    }else if(sky == 3){
        return `🌥️ 구름많음`;
    }else{
        return `☁️ 흐림`;
    }
}