document.addEventListener("DOMContentLoaded", () => {
  const btn = document.querySelector("#btnFavorite");
  const icon = btn.querySelector("i");

  btn.addEventListener("click", () => {
    const fundingId = btn.dataset.fundingIdx;
    const festivalId = btn.dataset.festivalIdx;
    const memberId = btn.dataset.memberId;
    const companyId = btn.dataset.companyId;
    console.log(festivalId)
    console.log(memberId)
    console.log(companyId)
    console.log(fundingId)

    if (memberId == 0 && companyId == 0) {
      alert("로그인 후 이용 가능합니다.");
      return;
    }

    fetch("/festival/toggleFavorite", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ member_idx: memberId, festival_idx: festivalId, company_idx: companyId, funding_festival_idx: fundingId })
    })
    .then(res => res.json())
    .then(data => {
      if (data.status === "added") {
        icon.classList.remove("bi-heart");
        icon.classList.add("bi-heart-fill", "text-danger");
      } else if (data.status === "removed") {
        icon.classList.remove("bi-heart-fill", "text-danger");
        icon.classList.add("bi-heart");
      }
    })
    .catch(err => console.error("찜 요청오류:", err));
  });
});