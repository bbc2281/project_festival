const urlParams = new URLSearchParams(window.location.search);
const orderId = urlParams.get("orderId");

function confirm() {
  if (!orderId) {
    console.warn("orderId가 없습니다. 서버 요청 생략");
    return;
  }

  console.log("orderID : " + orderId);

  fetch("/payment/fail", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ orderId: orderId })
  }).then(res => res.json())
    .then(json => {
      console.log("서버 응답:", json);
    });
}

document.addEventListener("DOMContentLoaded", () => {
  const confirmed = sessionStorage.getItem("confirmed");

  if (!confirmed) {
    confirm();
    sessionStorage.setItem("confirmed", "true");
  }
});
