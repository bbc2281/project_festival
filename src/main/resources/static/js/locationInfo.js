fetch(`/api/data?festival_idx=${festival_idx}`)
    .then(response => response.json())
    .then(data => {
        let festival_lat = data.festival_lat;
        let festival_lot = data.festival_lot;

        const containDiv = document.getElementById("locationInfo_list");
        containDiv.textContent = "로딩 중...";

        fetch(`http://localhost:8000/locationInfo?lat=${festival_lat}&lon=${festival_lot}`)
        .then(response => {
          if (!response.ok) throw new Error('네트워크 오류');
          return response.json();
        })
        .then(data2 => {
          console.log(data2)
          containDiv.innerHTML = "";
          if(data2.length > 0){
            data2.forEach(locate =>{
              const card = document.createElement("div");
              // card.classList.add("place-card");

              const wrapper = document.createElement("a");
              wrapper.href = 'https://map.naver.com/v5/search/' + locate.title;
              wrapper.style.textDecoration = 'none';
              wrapper.style.color = 'inherit';
              

              const locateSrc = document.createElement("img");
              locateSrc.src = locate.src;
              locateSrc.alt = '기본이미지';

              const locateTitle = document.createElement("div");
              locateTitle.textContent = locate.title;

              wrapper.appendChild(locateSrc);
              wrapper.appendChild(locateTitle);

              containDiv.appendChild(wrapper);
            });
          }else{
            containDiv.textContent = "추천 장소가 없습니다."
          }
          
        })
        .catch(error => {
          console.error(error);
        });
    })
    .catch(error => {
        console.error(error);
    });

