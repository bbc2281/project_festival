console.log('FesLite funding JS loaded');
const festivalName = document.getElementById("festivalName").innerText;
const festivalIdxStr = document.getElementById("funding_festival_idx").textContent;
const festivalIdx = parseInt(festivalIdxStr, 10);


/* =========================
   1) 홈: 카테고리 필터 (기존 유지)
   ========================= */
document.addEventListener("DOMContentLoaded", () => {
  const tabs = document.querySelectorAll("#categoryTabs .nav-link");
  const cards = document.querySelectorAll(".funding-card");

  if (!tabs.length || !cards.length) return;

  tabs.forEach(tab => {
    tab.addEventListener("click", () => {
      tabs.forEach(t => t.classList.remove("active"));
      tab.classList.add("active");

      const category = tab.dataset.category;
      cards.forEach(card => {
        const match = category === "all" || card.dataset.category.includes(category);
        card.style.display = match ? "" : "none";
      });
    });
  });
});

/* ================================================
   2) 상세: 진행률/게이지 + 내 후원금 표시 (보강 버전)
   ================================================ */
function _initFundingDetail(detail) {
  const percentEl = document.getElementById("percent");
  const barEl = document.getElementById("bar");
  const raisedEl = document.getElementById("raised");
  const goalEl = document.getElementById("goal");
  const ddayEl = document.getElementById("dday");
  const btnFund = document.getElementById("btnFund");

  // 필요한 엘리먼트 없으면 중단
  if (!percentEl || !barEl || !raisedEl || !goalEl || !ddayEl) {
    console.warn("funding detail elements not found");
    return;
  }

  // 숫자 안전 변환
  const toNum = v => {
    const n = typeof v === "number" ? v : parseInt(String(v).replace(/[^\d-]/g, ""), 10);
    return Number.isFinite(n) ? n : 0;
  };

  let raised = toNum(detail.raised);
  const goal = Math.max(1, toNum(detail.goal)); // 0분모 방지
  let myDonation = 0; // 내가 후원한 누적 금액

  // 초기 세팅
  goalEl.textContent = goal.toLocaleString();
  _updateUI(false);

  // D-day 계산
  try {
    const end = new Date(detail.end);
    const today = new Date();
    const diff = Math.ceil((end - today) / (1000 * 60 * 60 * 24));
    ddayEl.textContent = diff > 0 ? diff : 0;
  } catch (e) {
    ddayEl.textContent = "0";
  }

  // === 내 후원금 표시 영역 (없으면 생성, 있으면 재사용)
  const asideBody = document.querySelector(".funding-sticky .card-body");
  if (asideBody) {
    let myAmtEl = asideBody.querySelector("#myDonationWrap");
    if (!myAmtEl) {
      myAmtEl = document.createElement("div");
      myAmtEl.id = "myDonationWrap";
      myAmtEl.className = "mt-2 text-center fw-semibold text-primary";
      myAmtEl.innerHTML = `💰 나의 후원금: <span id="myDonation">0</span>원`;
      asideBody.appendChild(myAmtEl);
    }
  }

  // === 버튼 금액 적용
  document.querySelectorAll(".amt").forEach(btn => {
    btn.addEventListener("click", () => {
      const amt = toNum(btn.dataset.amt);
      if (amt > 0) _applyAmount(amt);
    });
  });

  // === 직접 입력 후 적용
  const customInput = document.getElementById("customAmt");
  const btnApplyAmt = document.getElementById("btnApplyAmt");
  if (btnApplyAmt && customInput) {
    btnApplyAmt.addEventListener("click", () => {
      const amt = toNum(customInput.value);
      if (amt <= 0) {
        alert("금액을 입력해주세요!");
        return;
      }
      _applyAmount(amt);
      customInput.value = "";
    });
  }

  // === 내부 함수들
  function _applyAmount(amt) {
    raised += amt;
    myDonation += amt;
    _updateUI(true);
  }

  function _updateUI(animated) {
    const percent = Math.min(100, Math.floor((raised / goal) * 100));
    percentEl.textContent = String(percent);
    raisedEl.textContent = raised.toLocaleString();

    const myDonationEl = document.getElementById("myDonation");
    if (myDonationEl) myDonationEl.textContent = myDonation.toLocaleString();

    if (animated) {
      barEl.style.transition = "width 0.8s ease";
    }
    barEl.style.width = `${percent}%`;
    barEl.setAttribute("aria-valuenow", String(percent));
    barEl.setAttribute("aria-valuemin", "0");
    barEl.setAttribute("aria-valuemax", "100");
  }

  // === 후원 버튼 (알림)
  if (btnFund) {
    btnFund.addEventListener("click", async () => {
      const amount = myDonation;
      const requestData = {
        amount ,
        orderName : `${festivalName} 후원`,
        festivalIdx : festivalIdx
      }
      const response = await fetch("/payment/order",{
        method : "POST",
        headers : {"Content-type" : "application/json"},
        body : JSON.stringify(requestData)
      });

      const result = await response.json();
      const orderId = result.orderId;

      console.log("오더아이디" + orderId);
      console.log("페스티벌idx" + festivalIdx);

     window.location.href = `/payment/main?orderId=${orderId}&festivalIdx=${festivalIdx}`
    });
  }
}

/* 전역 등록: 상세 초기화가 확실히 호출되도록 보장  */
window.initFundingDetail = _initFundingDetail;

console.log("Funding detail interaction script loaded");

