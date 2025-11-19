

function cop_check() {
    
    reg_num = document.getElementById("company_reg_num").value;
    const msgSpan = document.getElementById("cop_check_msg");
    const submitBtn = document.getElementById("check_btn");

    console.log(reg_num);

    msgSpan.innerHTML = '';
    submitBtn.disabled = true;

    if(!reg_num) {
        alert("사업자등록번호를 입력해주세요.");
        return false;
    }

    var data = {
        "b_no": [reg_num]
    };
    
    

    $.ajax({
        url: "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=aa3f9ce4d1b0ceb1ae3800fcef45e728e279f5a5d1f8a2ecb47711f06ee804d3",  // serviceKey 값을 xxxxxx에 입력
        type: "POST",
        data: JSON.stringify(data), // json 을 string으로 변환하여 전송
        dataType: "JSON",
        traditional: true,
        contentType: "application/json; charset:UTF-8",
        accept: "application/json",
        success: function(result) {
            console.log(result);
            if(result.match_cnt == "1") {
                //성공
                console.log("success");
                msgSpan.innerHTML = '<span class="check-success">✅ 등록 확인</span>';
                submitBtn.disabled = false;
            } else {
                //실패
                console.log("fail");
                msgSpan.innerHTML = '';
                document.getElementById("company_reg_num").value = '';
                alert(result.data[0]["tax_type"]);
            }
        },
        error: function(result) {
            console.log("error");
            console.log(result.responseText); //responseText의 에러메세지 확인
        }
    });

}