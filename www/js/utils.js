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
        url: gasmaxWebappPath + "customer_summary.jsp?uuid=" +  window.sessionStorage.uuid,
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

function showPageMain() {

    if ($("#hdnCidCustomerSearchYesNo").attr("value") == "Y") { //만일 CID 편집화면에서 검색한 경우에는 CID 편집화면으로 이동함.
        $("#hdnCidCustomerSearchYesNo").attr("value", "N");
        $.mobile.changePage("#pageManageCidEdit", {changeHash: false});
        setCurrentPage("pageManageCidEdit");
        return;
    }
    $.mobile.changePage("#pageMain", {changeHash: false});
    $("#txtHomeCustomerKeyword").attr("value", "");
    setCurrentPage("pageMain");

    showActivityIndicator("로딩중...")
    $.ajax({
        url: gasmaxWebappPath + "home.jsp?uuid="+  window.sessionStorage.uuid,
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
    $.mobile.changePage("#dialogDatePicker", {changeHash: false, role: "dialog", reverse: true});
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

        const {value} = await DatetimePicker.present({
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
