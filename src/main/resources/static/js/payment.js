const button = document.getElementById("paymentButton");
const params = new URLSearchParams(window.location.search);
const orderId = params.get("orderId");
const festivalIdx = params.get("funding_festival_idx");
const paymentWidgetDiv = document.getElementById("payment-method");

const widgetClientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
const customerKey = "Maoj7euHxHftQw9uzP7VC";
const paymentWidget = PaymentWidget(widgetClientKey, customerKey);

const requestData = {
  orderId : orderId,
};

let result = null;



(async () => {
  const response = await fetch("/payment/info", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ orderId })
  });

  result = await response.json(); 
  const PaymentMethodWidget = paymentWidget.renderPaymentMethods(
    "#payment-method",
    { value: result.amount },
    { variantKey: "DEFAULT" }
  );

  paymentWidget.renderAgreement(
    "#agreement",
    { variantKey: "AGREEMENT" }
  );
})();

  
button.addEventListener("click",()=>{

console.log("결제 토스 위젯 실행");

console.log(result);

  paymentWidget.requestPayment({
    orderId : orderId,
    orderName : result.orderName,
    successUrl: "http://localhost:8080/payment/success",
    failUrl: "http://localhost:8080/payment/fail",
    customerEmail : result.customerEmail,
    customerName : result.customerName,
    customerEmail : result.customerEmail
  });

});






