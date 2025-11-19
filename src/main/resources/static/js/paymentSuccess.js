const urlParams = new URLSearchParams(window.location.search);
const paymentKey = urlParams.get("paymentKey");
const orderId = urlParams.get("orderId");
const amount = urlParams.get("amount");

async function confirm() {
    console.log("페이먼트키 : "+ paymentKey);
    console.log("orderID : " + orderId);
    console.log("amout : " + amount);
    
    const requestData = {
        paymentKey : paymentKey,
        orderId : orderId,
        amount : amount,
    };

    const response = await fetch("/payment/confirm",{
        method : "POST",
        headers : {"Content-Type" : "application/json"},
        body : JSON.stringify(requestData)
    });

    const json = await response.json();

    if(!response.ok){
        console.log(json);
        window.location.href = `/payment/fail?message=${json.message}&code=${json.code}`
    }

    console.log(json);

    console.log("sending to /payment/success", json.orderId);


    await fetch("/payment/success",{
        method : "POST",
        headers : {"Content-Type" : "application/json"},
        body: JSON.stringify({ orderId: json.orderId })
    });

}
    (async () => {
    const confirmedKey = `confirmed_${orderId}`;
    const confirmed = sessionStorage.getItem(confirmedKey);

    if (!confirmed) {
        await confirm();
        sessionStorage.setItem(confirmedKey, "true");
    }
    })();

    const paymentkeyElement = document.getElementById("paymentKey");
    const orderIdElement = document.getElementById("orderId");
    const amountElement = document.getElementById("amount");

    orderIdElement.textContent = "주문번호  : " + orderId;
    amountElement.textContent = "결제금액  : " + amount;
    paymentkeyElement.textContent = "paymentkey  : " + paymentKey;
