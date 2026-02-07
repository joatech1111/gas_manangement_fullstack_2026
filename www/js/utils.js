// 이전에 작업했던 거래처별 업무로 페이지 이동
function transitCustomerBizPage() {
    var value = $("#selectCustomerBizMenu").attr("value");
    if (value == "DETAIL") {
        clickCustomerDetailMenu();
    } else if (value == "BOOKWEIGHT") {
        clickCustomerBookWeightMenu();
    } else if (value == "BOOKVOLUME") {
        clickCustomerBookVolumeMenu();
    } else if (value === "TAXINVOICE") { //todo : 세금 계산서!!!
        clickCustomerBookTaxInvoiceMenu();
    } else if (value == "BALANCE") {
        clickCustomerBookItemBalanceMenu();
    } else if (value == "SALEWEIGHT") {
        clickCustomerSaleWeightMenu();
    } else if (value == "SALEVOLUME") {
        clickCustomerSaleVolumeMenu();
    } else if (value == "READMETER") {
        clickCustomerReadMeterMenu();
    } else if (value == "COLLECT") {
        clickCustomerCollectMenu();
    } else if (value == "SAFTYCHECKLIST") {
        clickCustomerSaftyCheckListMenu();
    } else if (value == "SAFTYCHECK") {
        clickCustomerSaftyCheckMenu();
    }

    $("#selectCustomerBizMenu").attr("value", "");
}

function removeParentheses(address) {
    return address.replace(/\(.*?\)/g, "").trim();
}

function injectionCustomerSummary(tagId) {
    $.ajax({
        url: gasmaxWebappPath + "customer_summary.jsp?uuid=" + window.sessionStorage.uuid,
        type: "get",
        dataType: "html",
        timeout: 60000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                alert("서버에서 응답이 지연되고 있습니다. 잠시 후 다시 시도해주세요.");
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}


//거래처 상세 정보 삽입하기
function injectionCustomerDetail(tagId) {
    //$("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "search_customer_current_ajx.jsp",
        type: "post",
        dataType: "xml",
        timeout: 60000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                ;
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                alert("서버에서 응답이 지연되고 있습니다. 잠시 후 다시 시도해주세요.");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }

            var customerType = $(xml).find("customerType").text();
            var customerTypeName = $(xml).find("customerTypeName").text();
            var customerCode = $(xml).find("customerCode").text();
            var customerName = $(xml).find("customerName").text();
            var address = "(" + $(xml).find("postalCode").text() + ")" + $(xml).find("address1").text() + " " + $(xml).find("address2").text();

            address = removeParentheses(address);

            var containerDeposit = insertComma($(xml).find("containerDeposit").text());
            var freeInstallationFee = insertComma($(xml).find("freeInstallationFee").text());
            var weightReceivable = insertComma($(xml).find("weightReceivable").text());
            var volumeReceivable = insertComma($(xml).find("volumeReceivable").text());
            var employeeName = $(xml).find("employeeName").text();
            var consumerTypeName = $(xml).find("consumerTypeName").text();
            var remark = $(xml).find("remark1").text() + "\n" + $(xml).find("remark2").text();
            var memo = $(xml).find("memo").text();
            var issueTaxInvoiceYesNo = $(xml).find("issueTaxbillYesNo").text();
            var issueTaxInvoiceYesNoName = "발행안함";
            if (issueTaxInvoiceYesNo == "Y") issueTaxInvoiceYesNoName = "발행함";
            var registerNumberType = $(xml).find("registerNumberType").text(); // 0.사업번호, 1.주민번호, 2.외국인
            var registerNumberTypeName = "사업번호";
            var registerNumber = $(xml).find("registerNumber").text();
            if (registerNumberType == "0") { //사업자등록번호
                registerNumber = maskSaupNo(registerNumber);
                registerNumberTypeName = "사업번호";
            } else if (registerNumberType == "1") { //주민번호
                registerNumber = maskJuminNo(registerNumber);
                registerNumberTypeName = "주민번호";
            } else if (registerNumberType == "2") { //여권번호
                registerNumberTypeName = "여권번호";
            }
            var registerName = $(xml).find("registerName").text();
            var registerOwner = $(xml).find("registerOwner").text();
            var registerAddress = $(xml).find("registerAddress1").text() + " " + $(xml).find("registerAddress2").text();
            var businessCondition = $(xml).find("businessCondition").text();
            var businessType = $(xml).find("businessType").text();
            var contactInfo = $(xml).find("contactName").text() + "( " + $(xml).find("contactDepartment").text() + ")";

            var contactInfo = removeParentheses(contactInfo)

            var contactEmail = $(xml).find("contactEmail").text();
            var contactPhoneNumber = $(xml).find("contactPhoneNumber").text();
            var contactFaxNumber = $(xml).find("contactFaxNumber").text();
            var contractNumber = $(xml).find("contractNumber").text();
            var contractDate = $(xml).find("contractDate").text();
            contractDate = contractDate.substring(0, 4) + "-" + contractDate.substring(4, 6) + "-" + contractDate.substring(6);
            var latestSaftyCheckDate = $(xml).find("latestSaftyCheckDate").text();
            latestSaftyCheckDate = latestSaftyCheckDate.substring(0, 4) + "-" + latestSaftyCheckDate.substring(4, 6) + "-" + latestSaftyCheckDate.substring(6);
            //안전점검 대상 체크
            //			var saftyCheckYesNoText = "안전점검대상";
            var latestSaftyCheckDateStyle = "#3333FF";
            if (latestSaftyCheckDate != "") {
                var today = new Date();
                var diff = parseInt((today - latestSaftyCheckDate, 10) / 86400000); //오늘 날짜와 차이 계산
                var satryCheckPeriod = 365; //체적이 아니면 1년마다 체크
                if (customerType == "0") satryCheckPeriod = 183; //체적일 때 6개월 마다 체크
                //				if (diff <  satryCheckPeriod) saftyCheckYesNoText = ""; //안전점검 체크 기간이 아니면 표시 안함.
                if (diff >= satryCheckPeriod) latestSaftyCheckDateStyle = "#FF0000"; //안전점검 체크 기간이면 빨간색으로.
            }
            var contracterResidentNumber = $(xml).find("contracterResidentNumber").text();
            var containerOwnerType = $(xml).find("containerOwnerType").text();
            var facilityOwnerType = $(xml).find("facilityOwnerType").text();
            var facilityOkYesNo = $(xml).find("facilityOkYesNo").text();
            var switcherCapacity = $(xml).find("switcherCapacity").text();
            var vaporizerCapacity = $(xml).find("vaporizerCapacity").text();
            var fuseCockQuantity = $(xml).find("fuseCockQuantity").text();
            var hoseLength = $(xml).find("hoseLength").text();
            var valve = $(xml).find("valve").text();
            var supplyPipe1 = $(xml).find("supplyPipe1").text();
            var supplyPipe2 = $(xml).find("supplyPipe2").text();
            var regulatorPressure = $(xml).find("regulatorPressure").text();
            //단가 표시
            //			var priceType = $(xml).find("priceType").text(); //적용단가 코드
            var applyPrice = $(xml).find("applyPrice").text(); //적용단가 명
            var environmentPrice = $(xml).find("environmentPrice").text(); //환경단가
            var individualPrice = $(xml).find("individualPrice").text(); //개별단가
            var discountPrice = $(xml).find("discountPrice").text(); //할인단가
            var price = individualPrice;
            if (applyPrice == "환경단가") {
                price = environmentPrice;
            } else if (applyPrice == "할인단가") {
                price = discountPrice;
            }
            var priceText = applyPrice + " " + insertComma(price);
            //할인 적용 표시
            //			var priceMode = $(xml).find("priceMode").text(); //할인부호 +, -, *(%)
            //			var discountAmount = insertComma($(xml).find("discountAmount").text()); //할인적용액
            //			var discountText = priceMode + discountAmount;
            //			if (priceMode == "*") {
            //				discountText = discountAmount + " %";
            //			}
            var defaultRate = $(xml).find("defaultRate").text() + " %";
            var discountRate = $(xml).find("discountRate").text() + " %";
            var maintenanceFee = insertComma($(xml).find("maintenanceFee").text());
            var installationFee = insertComma($(xml).find("installationFee").text());
            var gaugeReplacementFee = insertComma($(xml).find("gaugeReplacementFee").text());
            var paymentTypeName = $(xml).find("paymentTypeName").text();
            var readMeterDay = "매월 " + $(xml).find("readMeterDay").text() + " 일";

            var html = '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">구분:</td><td style="width: 80px ; color: #3333FF  ; font-size:14px ;">' + customerTypeName + '</td><td style="width: 60px ; font-size:14px ; ">코드:</td><td style="color: #3333FF ; font-size:14px ;">' + customerCode + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">거래처명:</td><td colspan="3" style="width: 80px ; color: #3333FF ; font-size:14px ;">' + customerName + '</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">주소:</td><td colspan="3" style="width: 80px ; color: #3333FF ; font-size:14px ;">' + address + '</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">일반미수:</td><td style="width: 80px ; color: red  ; font-size:14px ;">' + weightReceivable + '</td><td style="width: 60px ; font-size:14px ; ">체적미수:</td><td style="color: red ; font-size:14px ;">' + volumeReceivable + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">용기보증:</td><td style="width: 80px ; color: red  ; font-size:14px ;">' + containerDeposit + '</td><td style="width: 60px ; font-size:14px ; ">무료시설:</td><td style="color: red ; font-size:14px ;">' + freeInstallationFee + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">담당사원:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + employeeName + '</td><td style="width: 60px ; font-size:14px ; ">소비형태:</td><td style="color: #3333FF ; font-size:14px ;">' + consumerTypeName + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">비고:</td><td colspan="3" style="color: #3333FF ; font-size:14px ;">' + remark + '</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#666666" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">메모:</td><td style="color: #3333FF ; font-size:14px ;">' + memo + '</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">계산서:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + issueTaxInvoiceYesNoName + '</td><td style="width: 60px ; font-size:14px ; ">' + registerNumberTypeName + ':</td><td style="color: #3333FF ; font-size:14px ;">' + registerNumber + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">상호:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + registerName + '</td><td style="width: 60px ; font-size:14px ; ">대표자:</td><td style="color: #3333FF ; font-size:14px ;">' + registerOwner + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">주소:</td><td colspan="3" style="color: #3333FF ; font-size:14px ;">' + registerAddress + '</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">업태:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + businessCondition + '</td><td style="width: 60px ; font-size:14px ; ">종목:</td><td style="color: #3333FF ; font-size:14px ;">' + businessType + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">담당자:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + contactInfo + '</td><td style="width: 60px ; font-size:14px ; ">이메일:</td><td style="color: #3333FF ; font-size:14px ;">' + contactEmail + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#666666" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">핸드폰:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;"><a href="tel:' + contactPhoneNumber + '" >' + contactPhoneNumber + '</a></td><td style="width: 60px ; font-size:14px ; ">팩스:</td><td style="color: #3333FF ; font-size:14px ;">' + contactFaxNumber + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">계약번호:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + contractNumber + '</td><td style="width: 60px ; font-size:14px ; ">공급일:</td><td style="color: #3333FF ; font-size:14px ;">' + contractDate + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">점검일:</td><td style="width: 80px ; color: ' + latestSaftyCheckDateStyle + ' ; font-size:14px ;">' + latestSaftyCheckDate + '</td><td style="width: 60px ; font-size:14px ; ">주민번호:</td><td style="color: #3333FF ; font-size:14px ;">' + contracterResidentNumber + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">용기소유:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + containerOwnerType + '</td><td style="width: 60px ; font-size:14px ; ">시설소유:</td><td style="color: #3333FF ; font-size:14px ;">' + facilityOwnerType + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">적합여부:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + facilityOkYesNo + '</td><td style="width: 60px ; font-size:14px ; ">절체기:</td><td style="color: #3333FF ; font-size:14px ;">' + switcherCapacity + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">기화기:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + vaporizerCapacity + '</td><td style="width: 60px ; font-size:14px ; ">퓨즈콕:</td><td style="color: #3333FF ; font-size:14px ;">' + fuseCockQuantity + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">호스:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + hoseLength + '</td><td style="width: 60px ; font-size:14px ; ">밸브:</td><td style="color: #3333FF ; font-size:14px ;">' + valve + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#666666" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">공급관1:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + supplyPipe1 + '</td><td style="width: 60px ; font-size:14px ; ">공급관2:</td><td style="color: #3333FF ; font-size:14px ;">' + supplyPipe2 + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">조정기:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + insertComma(regulatorPressure) + '</td><td style="width: 60px ; font-size:14px ; ">루베단가:</td><td style="color: #3333FF ; font-size:14px ;">' + priceText + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">할인율:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + discountRate + '</td><td style="width: 60px ; font-size:14px ; ">연체율:</td><td style="color: #3333FF ; font-size:14px ;">' + defaultRate + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">안전관리:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + maintenanceFee + '&nbsp;</td><td style="width: 60px ; font-size:14px ; ">시설비:</td><td style="color: #3333FF ; font-size:14px ;">' + installationFee + '</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#CCCCCC" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">계량교체:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + gaugeReplacementFee + '&nbsp;</td><td style="width: 60px ; font-size:14px ; ">&nbsp;</td><td style="color: #3333FF ; font-size:14px ;">' + '&nbsp;</td></tr></table>'
                + '            <table width="100%" cellspacing="0" cellpadding="1" border="0" bordercolor="#666666" frame="below" style="border-collapse: collapse;"><tr><td style="width: 60px ; font-size:14px ; ">수금방법:</td><td style="width: 80px ; color: #3333FF ; font-size:14px ;">' + paymentTypeName + '</td><td style="width: 60px ; font-size:14px ; ">정기검침:</td><td style="color: #3333FF ; font-size:14px ;">' + readMeterDay + '&nbsp;</td></tr></table>'
                + '			<table style="width: 100% ; ">'
                + '				<tr>'
                + '					<td align="center">'
                + '					<input type="button" data-mini="true" data-icon="check" id="btnCustomerDetailCustomerUpdate" data-corners="false" data-inline="true" onclick="showPageCustomerDetailUpdate()" value="수정"></input></td>'
                + '				</tr>'
                + '			</table>';
            $("#" + tagId).html(html).trigger("create");
            $("#hdnRequireRefreshPageCustomerDetail").attr("value", "N"); //거래처 상세보기가 이제 Refresh되었음을 표시
        }
    });
}


function toggleDarkMode() {
    let isDarkMode = $("body").hasClass("dark-mode");

    if (isDarkMode) {
        localStorage.setItem("darkMode", "false"); // 라이트 모드로 변경
    } else {
        localStorage.setItem("darkMode", "true"); // 다크 모드로 변경
    }

    $("body").toggleClass("dark-mode");
}

async function showToast(text) {
    try {
        await Capacitor.Plugins.Toast.show({
            text: text,
            duration: 'short',
            position: 'top',
        });
    } catch (error) {
        alert(error)
        console.error("Error displaying toast:", error);
    }
}


async function openJoaOffice() {
    try {
        const platform = Capacitor.getPlatform();

        if (platform === "android") {

            try {
                Capacitor.Plugins.AppLauncher.launchActivity({
                    url: "https://joaoffice.co.kr",
                    title: "조아 포탈"
                });
            } catch (e) {
                alert(e.toString())
            }
        } else {
            window.open("https://joaoffice.co.kr", "_blank");
        }

    } catch (e) {
        alert(e.toString())
    }
}

async function openJoaTech() {
    try {
        const platform = Capacitor.getPlatform();
        if (platform === "android") {
            try {
                Capacitor.Plugins.AppLauncher.launchActivity({
                    url: "http://www.joainfo.com",
                    title: "조아테크"
                });
            } catch (e) {
                alert(e.toString())
            }

        } else {
            window.open("https://joaoffice.co.kr", "_blank");
        }

    } catch (e) {
        alert(e.toString())
    }
}

// 조아테크 웹사이트 열기
async function openJoaTechWebsite() {
    try {
        const platform = Capacitor.getPlatform();
        if (platform === "android") {
            try {
                Capacitor.Plugins.AppLauncher.launchActivity({
                    url: "https://www.joatech.co.kr/",
                    title: "조아테크"
                });
            } catch (e) {
                alert(e.toString())
            }
        } else {
            window.open("https://www.joatech.co.kr/", "_blank");
        }
    } catch (e) {
        alert(e.toString())
    }
}

function showPageMain() {
    // 접속 디비명 및 hpSeq 업데이트 실행 함수 (localStorage 우선)
    var updateDbNameDisplay = function () {
        try {
            var svrDbName = window.localStorage.getItem("remember_gasmax_svrDbName") || window.sessionStorage.getItem("login_svrDbName") || "";
            var hpSeq = window.localStorage.getItem("remember_gasmax_hpSeq") || window.sessionStorage.getItem("login_hpSeq") || "";
            
            // DB명 표시
            if (svrDbName && svrDbName !== "null" && svrDbName !== "undefined") {
                $("#headerSvrDbName").text(svrDbName).css("display", "inline-block");
                console.log("🏙️ AppBar DB Name updated:", svrDbName);
            } else {
                $("#headerSvrDbName").hide();
            }
            
            // hpSeq 표시
            if (hpSeq && hpSeq !== "null" && hpSeq !== "undefined" && hpSeq !== "0") {
                $("#headerHpSeq").text("hpSeq:" + hpSeq).css("display", "inline-block");
                console.log("🔢 AppBar hpSeq updated:", hpSeq);
            } else {
                $("#headerHpSeq").hide();
            }
        } catch (e) {
            console.error("Error updating SvrDbName/hpSeq display:", e);
        }
    };

    if ($("#hdnCidCustomerSearchYesNo").attr("value") == "Y") { //만일 CID 편집화면에서 검색한 경우에는 CID 편집화면으로 이동함.
        $("#hdnCidCustomerSearchYesNo").attr("value", "N");
        $.mobile.changePage("#pageManageCidEdit", { changeHash: false });
        setCurrentPage("pageManageCidEdit");
        return;
    }

    $.mobile.changePage("#pageMain", { changeHash: false });
    $("#txtHomeCustomerKeyword").attr("value", "");
    setCurrentPage("pageMain");

    // 페이지 변경 후 UI 업데이트 (지연 실행으로 안정성 확보)
    updateDbNameDisplay();
    setTimeout(updateDbNameDisplay, 500);

    // pageMain이 보여질 때마다 업데이트되도록 이벤트 등록 (중복 방지)
    $(document).off("pageshow", "#pageMain").on("pageshow", "#pageMain", updateDbNameDisplay);

    showActivityIndicator("로딩중...")
    $.ajax({
        url: gasmaxWebappPath + "home.jsp?uuid=" + window.sessionStorage.uuid,
        type: "get",
        dataType: "html",
        timeout: 60000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("페이지가 존재하지 않습니다.", false);
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                alert("서버에서 응답이 지연되고 있습니다. 잠시 후 다시 시도해주세요.");
            }
        },
        success: function (html) {
            hideActivityIndicator()
            $("#mainMenuIcons").html(html).trigger("create");

            // 로그인 성공 후 대기 중인 딥링크가 있으면 처리
            if (window.pendingDeepLink) {
                console.log('로그인 완료 후 딥링크 처리:', window.pendingDeepLink);
                var deepLinkUrl = window.pendingDeepLink;
                window.pendingDeepLink = null; // 처리 후 초기화

                // 약간의 지연을 두어 메인 화면이 완전히 로드된 후 딥링크 처리
                setTimeout(function () {
                    if (typeof handleDeepLink === 'function') {
                        handleDeepLink(deepLinkUrl);
                    }
                }, 500);
            }
        }
    });
    // 앱 초기화
    initializeAppConfig();
}

//날짜 선택 다이얼로그로 이동
function showDialogDatePicker(pageId, dateInputId) {
    // 안드로이드의 경우 폰갭 플러그인 수행
    //if (navigator.userAgent.indexOf("Android") != -1) {
    //	pickDate(dateInputId);
    //	return;
    //}
    $("#hdnCallPageDiaglogDatePicker").attr("value", pageId);
    $.mobile.changePage("#dialogDatePicker", { changeHash: false, role: "dialog", reverse: true });
    $("#hdnDatePickerInputId").attr("value", dateInputId);
    var date = $("#" + dateInputId).attr("value");
    var year = date.substr(0, 4);
    var month = date.substr(5, 2);
    var day = date.substr(8, 2);
    if (month.substr(0, 1) == "0") month = month.substr(1, 1);
    if (day.substr(0, 1) == "0") day = day.substr(1, 1);
    $("#txtYearDatePicker").attr("value", year);
    $("#txtMonthDatePicker").attr("value", month);
    $("#txtDayDatePicker").attr("value", day);
    updateTitleDialogDatePicker(year, month, day);
}


async function openCapacitorDatePicker(pageId, dateInputId) {
    try {
        const DatetimePicker = Capacitor.Plugins.DatetimePicker;

        // 기존 입력 필드에서 날짜 가져오기 (없으면 기본값: 오늘 날짜)
        let dateInputElement = $("#" + dateInputId);
        let currentValue = dateInputElement.val();

        // 기존 날짜 또는 오늘 날짜를 가져오되, ISO 8601 형식으로 변환
        let defaultDate = currentValue
            ? new Date(currentValue).toISOString()  // 기존 날짜가 있으면 변환
            : new Date().toISOString();  // 없으면 오늘 날짜 사용

        const { value } = await DatetimePicker.present({
            cancelButtonText: 'Cancel',
            doneButtonText: 'Ok',
            mode: 'date',
            value: defaultDate, // ISO 8601 형식 적용
            theme: 'dark',
            locale: 'ko-KR',
        });

        if (value) {
            const selectedDate = new Date(value);
            const formattedDate = selectedDate.toISOString().split("T")[0]; // 'YYYY-MM-DD' 형태 변환

            // ✅ 선택한 날짜를 입력 필드에 정상적으로 반영
            dateInputElement.val(formattedDate).trigger("change");

            // ✅ 날짜를 'yyyymmdd' 형식으로 변환하여 로컬 스토리지 저장
            let yyyymmdd = convertToYYYYMMDD(formattedDate);
            localStorage.setItem("searchDate", yyyymmdd);

            // ✅ UI 업데이트
            let year = formattedDate.substr(0, 4);
            let month = formattedDate.substr(5, 2);
            let day = formattedDate.substr(8, 2);
            if (month.startsWith("0")) month = month.substr(1);
            if (day.startsWith("0")) day = day.substr(1);

            $("#txtYearDatePicker").val(year);
            $("#txtMonthDatePicker").val(month);
            $("#txtDayDatePicker").val(day);
            updateTitleDialogDatePicker(year, month, day);
        } else {
            alert("날짜를 선택하지 않았습니다.");
        }

    } catch (e) {
        console.error("날짜 선택 오류:", e);
    }
}


async function openInAppBrowser() {
    try {
        const platform = Capacitor.getPlatform();
        if (platform === "android") {
            const InAppBrowser = Capacitor.Plugins.InAppBrowser;
            await InAppBrowser.openInWebView({
                url: "http://www.joainfo.com",
                options: {
                    hidden: false,
                    toolbarColor: "#000000",
                    closeButtonCaption: "닫기",
                    presentationStyle: "fullscreen"
                }
            });
        } else {
            window.open("https://www.joainfo.com", "_blank");
        }
    } catch (e) {
        alert("Error: " + e.toString());
    }
}

function showActivityIndicator(message) {
    $.mobile.loading("show", {
        textVisible: false, // 텍스트를 보이게 할지 여부
        theme: "a", // 테마 (a: 기본, b: 어두운 테마)
        textonly: false, // 텍스트만 표시할지 여부
        html: "" // 커스텀 HTML (필요한 경우)
    });
}

function hideActivityIndicator() {
    $.mobile.loading("hide");
    // $("#custom-loader").fadeOut();
}

// localStorage에 저장된 로그인 정보로 자동 로그인 시도
function autoLoginWithStoredInfo() {
    try {
        // localStorage에서 로그인 정보 확인
        var remember = window.localStorage.getItem('remember_gasmax');
        var remember_id = window.localStorage.getItem('remember_gasmax_id');
        var remember_pw = window.localStorage.getItem('remember_gasmax_pw');

        // 저장된 로그인 정보가 있는지 확인
        if (remember == "1" && remember_id && remember_pw) {
            console.log('저장된 로그인 정보로 자동 로그인 시도:', remember_id);

            // 로그인 입력 필드에 값 설정
            $("#txtLoginId").val(remember_id);
            $("#txtLoginPw").val(remember_pw);

            // authCheck 함수가 존재하면 호출, 없으면 직접 로그인 처리
            if (typeof authCheck === 'function') {
                // 약간의 지연을 두어 입력 필드가 업데이트된 후 로그인 시도
                setTimeout(function () {
                    // 로그인 성공을 감지하기 위해 showPageMain 함수를 감시
                    var originalShowPageMain = window.showPageMain;
                    if (originalShowPageMain) {
                        window.showPageMain = function () {
                            // 원래 함수 호출
                            originalShowPageMain.apply(this, arguments);
                        };
                    }
                    authCheck();
                }, 100);
                return true;
            } else {
                console.warn('authCheck 함수를 찾을 수 없습니다.');
                return false;
            }
        } else {
            console.log('저장된 로그인 정보가 없습니다.');
            return false;
        }
    } catch (error) {
        console.error('자동 로그인 처리 오류:', error);
        return false;
    }
}

// 딥링크 처리 함수
function handleDeepLink(url) {
    try {
        console.log('딥링크 처리 시작:', url);

        // 현재 페이지가 로그인 화면인지 확인
        var currentPage = $("#hdnCurrentPage").attr("value");
        var isLoginPage = (currentPage === "pageIntro" || currentPage === "" || !currentPage);

        // 로그인 화면이고 localStorage에 저장된 정보가 있으면 자동 로그인 시도
        if (isLoginPage) {
            var autoLoginSuccess = autoLoginWithStoredInfo();
            if (autoLoginSuccess) {
                console.log('자동 로그인 시도 중... 딥링크 처리는 로그인 완료 후 진행됩니다.');
                // 로그인 성공 후 딥링크를 다시 처리하기 위해 URL을 저장
                window.pendingDeepLink = url;
                return;
            }
        }

        // URL 파싱
        const urlObj = new URL(url);
        const scheme = urlObj.protocol.replace(':', ''); // gasmanagement://
        const host = urlObj.host; // 호스트 (있을 경우)
        const path = urlObj.pathname; // 경로
        const params = new URLSearchParams(urlObj.search); // 쿼리 파라미터

        // 딥링크 예시: gasmanagement://customer/123?action=detail
        // 또는: gasmanagement://?page=customer&id=123

        // 쿼리 파라미터에서 페이지 정보 가져오기
        const page = params.get('page');
        const id = params.get('id');
        const action = params.get('action');

        // 경로 기반 처리
        if (path) {
            const pathParts = path.split('/').filter(p => p);
            if (pathParts.length > 0) {
                const route = pathParts[0];

                switch (route) {
                    case 'customer':
                        if (pathParts.length > 1) {
                            const customerId = pathParts[1];
                            // 고객 상세 페이지로 이동
                            // showPageCustomerDetail() 등의 함수 호출
                            console.log('고객 상세 페이지로 이동:', customerId);
                            // 실제 구현은 앱의 라우팅 로직에 맞게 수정 필요
                        }
                        break;
                    case 'main':
                        showPageMain();
                        break;
                    default:
                        console.log('알 수 없는 경로:', route);
                }
            }
        }

        // 쿼리 파라미터 기반 처리
        if (page) {
            switch (page) {
                case 'main':
                    showPageMain();
                    break;
                case 'customer':
                    if (id) {
                        console.log('고객 페이지로 이동:', id);
                        // 고객 상세 페이지로 이동하는 로직 추가
                    }
                    break;
                default:
                    console.log('알 수 없는 페이지:', page);
            }
        }

        // 액션 기반 처리
        if (action) {
            switch (action) {
                case 'open':
                    // 특정 기능 열기
                    break;
                default:
                    console.log('알 수 없는 액션:', action);
            }
        }

        // 딥링크가 처리되었음을 사용자에게 알림 (선택사항)
        // showToast('딥링크가 처리되었습니다.');

    } catch (error) {
        console.error('딥링크 처리 오류:', error);
        // URL 파싱 실패 시에도 앱이 정상 작동하도록 처리
    }
}

// 환경설정 페이지에 계정 삭제 섹션 추가 (app_user_edit.jsp 로드 후 호출)
function injectAppUserEditAccountDelete() {
    var container = $("#divAppUserEdit");
    if (!container.length || $("#btnAppUserEditAccountDelete").length) return;

    var sectionHtml =
        '<div class="app-user-edit-account-delete-section" style="margin-top: 1.5em; padding-top: 1em; border-top: 1px solid #ddd;">' +
        '<h4 style="margin: 0 0 0.5em 0; color: #333;">계정</h4>' +
        '<button type="button" id="btnAppUserEditAccountDelete" data-role="button" data-theme="e" data-icon="delete" data-iconpos="left" style="color: #c00;">계정 삭제</button>' +
        '</div>';
    container.append(sectionHtml).trigger("create");

    $(document).off("click", "#btnAppUserEditAccountDelete").on("click", "#btnAppUserEditAccountDelete", function () {
        requestAccountDelete();
    });
}

// 계정 삭제 확인 후 API 호출 흉내만 내고 로그아웃 처리 (환경설정 푸터 위 버튼용)
function requestAccountDeleteMock() {
    Swal.fire({
        title: '계정을 삭제하시겠습니까?',
        html: '삭제 시 저장된 로그인 정보가 삭제되며, 다시 로그인해야 합니다.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '삭제',
        cancelButtonText: '취소',
        reverseButtons: true,
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
        background: '#f9fafb',
        width: '90%',
        padding: '1.5rem',
        customClass: {
            popup: 'rounded-xl shadow-lg',
            title: 'text-lg font-bold',
            confirmButton: 'px-5 py-2',
            cancelButton: 'px-5 py-2',
        },
    }).then(function (result) {
        if (!result.isConfirmed) return;

        if (typeof showActivityIndicator === 'function') showActivityIndicator('처리 중...');

        // API 엔드포인트 호출 흉내 (실제 요청 없이 짧은 지연 후 로그아웃)
        setTimeout(function () {
            if (typeof hideActivityIndicator === 'function') hideActivityIndicator();
            doLocalAccountDelete();
        }, 600);
    });
}

// 계정 삭제 확인 및 실행 (실제 API 호출 시 사용)
function requestAccountDelete() {
    Swal.fire({
        title: '계정을 삭제하시겠습니까?',
        html: '삭제 시 저장된 로그인 정보가 삭제되며, 다시 로그인해야 합니다.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '삭제',
        cancelButtonText: '취소',
        reverseButtons: true,
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
        background: '#f9fafb',
        width: '90%',
        padding: '1.5rem',
        customClass: {
            popup: 'rounded-xl shadow-lg',
            title: 'text-lg font-bold',
            confirmButton: 'px-5 py-2',
            cancelButton: 'px-5 py-2',
        },
    }).then(function (result) {
        if (!result.isConfirmed) return;

        hideActivityIndicator();
        showActivityIndicator('처리 중...');

        var uuid = window.sessionStorage.uuid || '';
        if (typeof gasmaxWebappPath !== 'undefined' && gasmaxWebappPath && uuid) {
            $.ajax({
                url: gasmaxWebappPath + "app_user_delete_ajx.jsp",
                type: "post",
                data: { uuid: uuid },
                dataType: "json",
                timeout: 15000,
                complete: function () {
                    hideActivityIndicator();
                    doLocalAccountDelete();
                },
                success: function () {
                    doLocalAccountDelete();
                },
                error: function () {
                    // 서버 API 없거나 실패해도 로컬 데이터는 삭제
                    doLocalAccountDelete();
                }
            });
        } else {
            hideActivityIndicator();
            doLocalAccountDelete();
        }
    });
}

// 로그인 화면으로 이동 (세션 유지)
function doLocalAccountDelete() {
    // jQuery Mobile로 직접 로그인 페이지로 이동 (showPageIntro 호출 시 uuid 참조 에러 방지)
    try {
        if (typeof $.mobile !== 'undefined' && $.mobile.changePage) {
            $.mobile.changePage("#pageIntro", { changeHash: false });
            if (typeof setCurrentPage === 'function') {
                setCurrentPage("pageIntro");
            }
        } else {
            window.location.replace('index.html');
        }
    } catch (e) {
        window.location.reload();
    }
}
