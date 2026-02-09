// ★ 모든 AJAX 호출에 uuid와 hpSeq를 자동으로 추가
$.ajaxPrefilter(function(options, originalOptions, jqXHR) {
	var uuid = $("#hdnUuid").val() || $("#hdnUuid").attr("value") || "";
	var hpSeq = window.sessionStorage.getItem("login_hpSeq") || "";
	if (uuid || hpSeq) {
		var separator = (options.data && options.data.length > 0) ? "&" : "";
		var extra = "";
		if (uuid && options.data && options.data.indexOf("uuid=") === -1) {
			extra += separator + "uuid=" + encodeURIComponent(uuid);
			separator = "&";
		}
		if (hpSeq && (!options.data || options.data.indexOf("hpSeq=") === -1)) {
			extra += separator + "hpSeq=" + encodeURIComponent(hpSeq);
		}
		options.data = (options.data || "") + extra;
	}
});

//처음 시작 로그인 페이지로 이동 appExit가 true 이면 앱을 종료한다.
function showPageIntro(appExit) {
    try {
        window.sessionStorage["current_AreaSeq"] = "";
        $("#divMultiUserSelect").empty();
        // uuid는 hdnUuid가 가장 신뢰도 높음 (웹/앱 모두 init에서 세팅)
        var macNumber = $("#hdnUuid").val() || $("#hdnUuid").attr("value") || window.sessionStorage.uuid || "";
        getMultiAppUser(macNumber);
        $.mobile.changePage("#pageIntro", { changeHash: false });
        setCurrentPage("pageIntro");
        $("#loginMessage").html("").trigger("create");
    } catch (e) {
        alert(e.toString())
    }
}

//메시지 html 가져오기
function getResultMessage(message, pendingIcon) {
    if (pendingIcon === true) {
        return `
            <div style="display: flex; align-items: center; width: 100%; padding: 10px; background-color: #e6eec8; border-radius: 6px; border: 1px solid #c3cfa2;">
                <img src="images/ajax-loader.gif" style="width: 20px; height: 20px; margin-right: 10px;">
                <span style="font-size: 16px; font-weight: 500; color: #333;">${message}</span>
            </div>
        `;
    } else {
        return `
            <div style="display: flex; align-items: center; width: 100%; padding: 10px; background-color: #e6eec8; border-radius: 6px; border: 1px solid #c3cfa2;">
                <span style="font-size: 16px; font-weight: 500; color: #333;">${message}</span>
            </div>
        `;
    }
}


//동일 휴대폰 사용자코드 여부
function getMultiAppUser() {
    var macNumber = arguments[0];
    if (macNumber == undefined) macNumber = "";
    var appVersion = $("#hdnAppVersion").val();

    $.ajax({
        url: gasmaxWebappPath + "app_user_multi_check_ajx.jsp",
        type: "post",
        data: "macNumber=" + sec(macNumber)
            + "&appVersion=" + appVersion
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result, errorText, errorThrown) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Text:" + errorText
                    + " --Error Result:" + result);
            }
            $("#loginMessage").html("").trigger("create");
        },
        success: function (xml) {
            console.log("📥 [getMultiAppUser] Response received");
            var totalRowCount = parseInt($(xml).find("totalRowCount").text());
            console.log("📊 [getMultiAppUser] totalRowCount:", totalRowCount);

            // ⭐ 첫 번째 회사의 areaCode 및 svrDbName을 저장 (로그인 시 사용)
            var firstAppUser = $(xml).find("AppUser").first();
            if (firstAppUser.length > 0) {
                var firstAreaCode = firstAppUser.find("areaCode").text();
                var firstSvrDbName = firstAppUser.find("svrDbName").text() || firstAppUser.find("SVR_DBName").text() || "";
                window.sessionStorage.setItem("login_areaCode", firstAreaCode);
                window.sessionStorage.setItem("login_svrDbName", firstSvrDbName);
                console.log("💾 [getMultiAppUser] Saved areaCode and svrDbName to sessionStorage:", firstAreaCode, firstSvrDbName);
            }

            /*if ($(xml).find("session").text() == "X"){
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }*/

            var resultText = $(xml).find("Result").text();
            console.log("📊 [getMultiAppUser] Result:", resultText);
            if (resultText == "N") {
                console.log("❌ [getMultiAppUser] Result is N, exiting");
                return;
            }
            if (totalRowCount > 1) {
                console.log("🏢 [getMultiAppUser] Multiple companies found, creating button list");

                // 과거 키(remember_gasmax_co)가 areaSeq였을 가능성이 있어, areaCode 전용 키를 새로 사용
                var rememberedAreaCode =
                    window.localStorage.getItem("remember_gasmax_areaCode")
                    || window.localStorage.getItem("remember_gasmax_co")
                    || "";

                var optionCount = 0;
                var firstAreaCode = "";

                // 버튼 목록(회사 선택) 생성
                var html = '<div id="loginAreaCodeButtons" class="login-company-select" data-role="controlgroup" data-mini="true">';
                html += '<div class="login-section-title">회사 선택</div>';

                $(xml).find("AppUser").each(function () {
                    var areaCode = ($(this).find("areaCode").text() || "").trim(); //업체코드
                    var areaName = ($(this).find("areaName").text() || "").trim(); //회사명
                    var areaSeq = ($(this).find("areaSeq").text() || "").trim(); //HP_SEQ
                    var hpSeq = areaSeq; // HP_SEQ는 areaSeq와 동일
                    var svrDbName = ($(this).find("svrDbName").text() || $(this).find("SVR_DBName").text() || $(this).find("dbCatalogName").text() || "").trim(); //서버DB명

                    if (!firstAreaCode) firstAreaCode = areaCode;

                    // 업체명 + hpSeq + areaCode 완전 노출
                    var btnText = areaName + "  [hpSeq: " + hpSeq + ", areaCode: " + areaCode + "]";
                    html += '<a href="#" class="btnLoginAreaCode" data-hpseq="' + hpSeq + '" data-areacode="' + areaCode + '" data-areaseq="' + areaSeq + '" data-svrdbname="' + svrDbName + '" data-role="button">' + btnText + "</a>";
                    optionCount++;
                });

                html += "</div>";

                $("#divMultiUserSelect").html(html).trigger("create");
                console.log("✅ [getMultiAppUser] Button list created with " + optionCount + " options");

                function applyLoginAreaCodeSelection(selectedHpSeq, selectedAreaCode, selectedSvrDbName) {
                    if (!selectedAreaCode) return;
                    window.sessionStorage.setItem("login_hpSeq", selectedHpSeq || "");
                    window.sessionStorage.setItem("login_areaCode", selectedAreaCode);
                    window.sessionStorage.setItem("login_svrDbName", selectedSvrDbName || "");
                    window.localStorage.setItem("remember_gasmax_hpSeq", selectedHpSeq || "");
                    window.localStorage.setItem("remember_gasmax_areaCode", selectedAreaCode);
                    window.localStorage.setItem("remember_gasmax_svrDbName", selectedSvrDbName || "");

                    // 선택 표시 (jQM active 스타일)
                    $("#loginAreaCodeButtons .btnLoginAreaCode").removeClass("ui-btn-active");
                    $("#loginAreaCodeButtons .btnLoginAreaCode[data-areacode='" + selectedAreaCode + "']").addClass("ui-btn-active");
                    console.log("🔄 [Button Select] Selected hpSeq:", selectedHpSeq, "areaCode:", selectedAreaCode, "svrDbName:", selectedSvrDbName);
                }

                // 버튼 클릭 시 선택값 저장
                $(document)
                    .off("click", "#loginAreaCodeButtons .btnLoginAreaCode")
                    .on("click", "#loginAreaCodeButtons .btnLoginAreaCode", function (e) {
                        e.preventDefault();
                        var selectedHpSeq = $(this).attr("data-hpseq") || $(this).attr("data-areaseq") || "";
                        var selectedAreaCode = $(this).attr("data-areacode") || "";
                        var selectedSvrDbName = $(this).attr("data-svrdbname") || "";
                        applyLoginAreaCodeSelection(selectedHpSeq, selectedAreaCode, selectedSvrDbName);
                    });

                // 최초 선택값(기억값 우선) 저장 및 UI 표시
                var initialAreaCode = rememberedAreaCode || firstAreaCode;
                var initialHpSeq = "";
                var initialSvrDbName = window.localStorage.getItem("remember_gasmax_svrDbName") || (initialAreaCode === firstAreaCode ? ($(xml).find("AppUser").first().find("svrDbName").text() || $(xml).find("AppUser").first().find("SVR_DBName").text() || "") : "");

                // initialAreaCode에 해당하는 hpSeq 찾기
                $(xml).find("AppUser").each(function () {
                    if (($(this).find("areaCode").text() || "").trim() === initialAreaCode) {
                        initialHpSeq = ($(this).find("areaSeq").text() || "").trim();
                        return false;
                    }
                });

                if (initialAreaCode) {
                    applyLoginAreaCodeSelection(initialHpSeq, initialAreaCode, initialSvrDbName);
                }
            } else {
                console.log("ℹ️ [getMultiAppUser] Single company or no companies, no dropdown needed");
                // 단일 회사일 때 hpSeq와 areaCode를 localStorage에 저장
                if (totalRowCount == 1) {
                    var firstUser = $(xml).find("AppUser").first();
                    var firstHpSeq = (firstUser.find("areaSeq").text() || "").trim();
                    var firstAreaCode = (firstUser.find("areaCode").text() || "").trim();
                    var firstSvrDbName = (firstUser.find("svrDbName").text() || firstUser.find("SVR_DBName").text() || firstUser.find("dbCatalogName").text() || "").trim();
                    if (firstAreaCode) {
                        window.sessionStorage.setItem("login_hpSeq", firstHpSeq);
                        window.sessionStorage.setItem("login_areaCode", firstAreaCode);
                        window.sessionStorage.setItem("login_svrDbName", firstSvrDbName);
                        window.localStorage.setItem("remember_gasmax_hpSeq", firstHpSeq);
                        window.localStorage.setItem("remember_gasmax_areaCode", firstAreaCode);
                        window.localStorage.setItem("remember_gasmax_svrDbName", firstSvrDbName);
                        console.log("💾 [getMultiAppUser] Single company - Saved hpSeq:", firstHpSeq, "areaCode:", firstAreaCode, "svrDbName:", firstSvrDbName);
                    }
                    var firstUser = $(xml).find("AppUser").first();
                    var singleAreaCode = firstUser.find("areaCode").text();
                    var singleAreaName = firstUser.find("areaName").text();
                    var singleSvrDbName = firstUser.find("svrDbName").text() || firstUser.find("SVR_DBName").text() || "";
                    console.log("📌 [getMultiAppUser] Single company: areaCode=" + singleAreaCode + ", areaName=" + singleAreaName + ", svrDbName=" + singleSvrDbName);
                    window.localStorage["single_company_areaCode"] = singleAreaCode;
                    window.localStorage["single_company_svrDbName"] = singleSvrDbName;
                    window.sessionStorage.setItem("login_areaCode", singleAreaCode);
                    window.sessionStorage.setItem("login_svrDbName", singleSvrDbName);
                    window.localStorage.setItem("remember_gasmax_areaCode", singleAreaCode);
                    window.localStorage.setItem("remember_gasmax_svrDbName", singleSvrDbName);
                }
            }
        }
    });
}


//사용자 인증 체크
function authCheck() {
    $("#loginMessage").html(getResultMessage("접속 중입니다.", true)).trigger("create");
    var loginId = $.trim($("#txtLoginId").val() || "");
    var loginPw = $.trim($("#txtLoginPw").val() || "");

    // 입력값이 없으면 서버 호출 전에 알림 표시
    if (!loginId || !loginPw) {
        var emptyMessage = "아이디 또는 비밀번호가 입력되지 않았습니다.";
        if (window.Swal && Swal.mixin) {
            Swal.mixin({
                toast: true,
                position: 'top',
                showConfirmButton: false,
                timer: 1800,
                timerProgressBar: true
            }).fire({
                icon: 'warning',
                title: emptyMessage
            });
        } else {
            alert(emptyMessage);
        }
        $("#loginMessage").html("").trigger("create");
        return;
    }
    //var mustErase ;
    //$("#hdnUuid").attr("value", "356455042867040");
    var uuid = $("#hdnUuid").attr("value");


    var mobileNumber = $("#hdnMobileNumber").attr("value");
    var remember = $("#ckbRememberLogin").attr("checked");

    // 선택된 hpSeq 및 svrDbName 가져오기 (areaCode 대신 hpSeq 사용)
    console.log("🔍 [authCheck] === Get Login Info Start ===");

    var hpSeq = "";
    var areaCode = ""; // 하위 호환성을 위해 유지
    var svrDbName = "";

    // 두 가지 버튼 그룹 모두 확인 (#loginAreaCodeButtons와 #multiUserButtons)
    var activeBtn = $("#loginAreaCodeButtons .btnLoginAreaCode.ui-btn-active");
    if (activeBtn.length === 0) {
        activeBtn = $("#multiUserButtons .btnMultiAppUser.ui-btn-active");
    }

    if (activeBtn.length > 0) {
        hpSeq = activeBtn.attr("data-hpseq") || activeBtn.attr("data-areaseq") || "";
        areaCode = activeBtn.attr("data-areacode") || "";
        svrDbName = activeBtn.attr("data-svrdbname") || "";

        // 값이 있으면 저장
        if (hpSeq) {
            try {
                window.sessionStorage.setItem("login_hpSeq", hpSeq);
                window.localStorage.setItem("remember_gasmax_hpSeq", hpSeq);
            } catch (e) { }
        }
        if (areaCode) {
            try {
                window.sessionStorage.setItem("login_areaCode", areaCode);
                window.sessionStorage.setItem("login_svrDbName", svrDbName);
            } catch (e) { }
        }
        console.log("✅ [authCheck] hpSeq from active button:", hpSeq, "areaCode:", areaCode, "svrDbName:", svrDbName);
    } else {
        hpSeq = window.sessionStorage.getItem("login_hpSeq") || "";
        areaCode = window.sessionStorage.getItem("login_areaCode") || "";
        svrDbName = window.sessionStorage.getItem("login_svrDbName") || "";
        console.log("📥 [authCheck] Login Info from sessionStorage fallback:", hpSeq, areaCode, svrDbName);
    }

    if (!hpSeq || hpSeq == "" || hpSeq == "null") {
        hpSeq = "0";
    }

    console.log("🔍 [authCheck] Final hpSeq to send:", hpSeq);
    console.log("🔍 [authCheck] === Get Login Info End ===");


    //핸드폰 장비 정보에 +82 부분을 0으로 바꿔 010으로 번호가 시작하도록 변경
    var prefixMobileNumber = mobileNumber.substring(0, 1);
    if (prefixMobileNumber == "+") {
        if (mobileNumber.length > 3) {
            mobileNumber = "0" + mobileNumber.substring(3);
        }
    }


    $.ajax({
        url: gasmaxWebappPath + "auth_check_s3_ajx_v2.jsp",
        type: "get",
        data: "loginId=" + loginId
            + "&loginPw=" + sec(loginPw)
            + "&uuid=" + sec(uuid)
            + "&mobileNumber=" + sec(mobileNumber)
            + "&hpSeq=" + hpSeq
            + "&areaCode=" + areaCode
            + "&svrDbName=" + svrDbName
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {

            alert(JSON.stringify(result));
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");

            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
            $("#loginMessage").html("").trigger("create");
            // alert("3245345345345error occured. Status:" + result.status
            //     + " --Status Text:" + result.statusText
            //     + " --Error Result:" + result);
        },
        success: function (xml) {

            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }

            var result = $(xml).find("result").text();
            var areaCode = $(xml).find("areaCode").text();
            var svrDbName = $(xml).find("svrDbName").text();
            var gasType = $(xml).find("gasType").text();
            var signImagePath = $(xml).find("signImagePath").text();
            var menuPermission = $(xml).find("menuPermission").text();

            // SVR_DBName 저장 (상단 표시용)
            if (svrDbName) {
                window.localStorage.setItem("remember_gasmax_svrDbName", svrDbName);
                window.sessionStorage.setItem("login_svrDbName", svrDbName);
                console.log("💾 Saved svrDbName to storage:", svrDbName);
            }

            // hpSeq 저장 (상단 표시용) - 로그인 시 전달한 hpSeq 유지
            var currentHpSeq = window.sessionStorage.getItem("login_hpSeq") || window.localStorage.getItem("remember_gasmax_hpSeq") || "";
            if (currentHpSeq && currentHpSeq !== "0" && currentHpSeq !== "null") {
                window.localStorage.setItem("remember_gasmax_hpSeq", currentHpSeq);
                window.sessionStorage.setItem("login_hpSeq", currentHpSeq);
                console.log("💾 Saved hpSeq to storage:", currentHpSeq);
            }

            let sessionToken = $(xml).find("sessionToken").text();
            //alert(sessionToken);
            sessionStorage.setItem("sessionToken", sessionToken); // ✅ 세션

            $.ajaxSetup({
                timeout: 600000,
                beforeSend: function (jqXHR, settings) {
                    const uuid = window.sessionStorage.uuid.toLowerCase() || '';
                    //const uuid = "9faefa603c520f39"
                    const sessionToken = sessionStorage.getItem("sessionToken"); // ✅ sessionToken 가져오기- 유저정보..
                    // ✅ URL에 sessionToken 추가 (기존 JSESSIONID 제거)
                    if (sessionToken) {
                        if (settings.url.indexOf("?") === -1) {
                            settings.url += "?sessionToken=" + encodeURIComponent(sessionToken);
                        } else {
                            settings.url += "&sessionToken=" + encodeURIComponent(sessionToken);
                        }
                    }

                    // ✅ 기존 uuid 추가 로직 유지
                    if (typeof settings.data === "string") {
                        settings.data += (settings.data ? "&" : "") + "uuid=" + encodeURIComponent(uuid);
                    } else if (typeof settings.data === "object") {
                        settings.data = settings.data || {};
                        settings.data.uuid = uuid;
                        settings.data = $.param(settings.data);
                    }
                }
            });


            $("#hdnCurrentAreaSeq").attr("value", $(xml).find("areaSeq").text()); //로그인 사용자의 순번
            $("#hdnCurrentCustomerAreaCode").attr("value", areaCode); //접속한 사용자의 영업소 코드로 초기화
            $("#hdnGasType").attr("value", gasType); //접속한 사용자의 GasType 초기화
            $("#hdnSignImagePath").attr("value", signImagePath); //접속한 사용자의 서명저장서버경로 초기화

            if (result == "Y") { //로그인 성공!!
                //local storage 에 저장하기
                if (remember == "checked") {
                    window.localStorage["remember_gasmax"] = "1";
                    window.localStorage["remember_gasmax_id"] = loginId;
                    window.localStorage["remember_gasmax_pw"] = loginPw;
                } else {
                    window.localStorage["remember_gasmax"] = "0";
                    window.localStorage.removeItem("remember_gasmax_id");
                    window.localStorage.removeItem("remember_gasmax_pw");
                }
                window.sessionStorage["current_AreaSeq"] = $(xml).find("areaSeq").text();

                //todo: 메뉴권한 초기화하기
                console.log("menuPermission===>", menuPermission);

                var menuPermissionCustomerBook = menuPermission.substr(3, 1);	// 거래장부
                var menuPermissionSale = menuPermission.substr(4, 1);			// 판매등록/현황
                var menuPermissionReadMeter = menuPermission.substr(5, 1);		// 검침등록/현황
                var menuPermissionCollect = menuPermission.substr(6, 1);			// 수금등록/현황
                var menuPermissionSaftyCheck = menuPermission.substr(7, 1);		// 안전점검
                var menuPermissionCid = menuPermission.substr(8, 1);				// CID 수신
                var menuPermissionCustomerInsert = menuPermission.substr(9, 1);	// 거래처등록/수정
                var menuPermissionUnpaid = menuPermission.substr(10, 1);			// 미수현황
                $("#hdnMenuPermissionCustomerBook").attr("value", menuPermissionCustomerBook);
                $("#hdnMenuPermissionSale").attr("value", menuPermissionSale);
                $("#hdnMenuPermissionReadMeter").attr("value", menuPermissionReadMeter);
                $("#hdnMenuPermissionCollect").attr("value", menuPermissionCollect);
                $("#hdnMenuPermissionSaftyCheck").attr("value", menuPermissionSaftyCheck);
                $("#hdnMenuPermissionCid").attr("value", menuPermissionCid);
                $("#hdnMenuPermissionCustomerInsert").attr("value", menuPermissionCustomerInsert);
                $("#hdnMenuPermissionUnpaid").attr("value", menuPermissionUnpaid);

                //메인페이지로 이동.
                showPageMain();
            } else { //로그인 실패
                var errorCode = $(xml).find("errorCode").text();
                var errorMessage = $(xml).find("errorMessage").text();
                if (errorCode == "M") { // M: uuid가 null인경우 앱을 다시 실행하도록 종료시킴.
                    alert(errorMessage);
                    navigator.app.exitApp();
                }
                //todo : 애러 메세지 처리 부분..
                // $.toast({
                //     hideAfter: 1500,
                //     text: errorMessage,
                //     icon: 'warning', // Type of toast icon
                //     showHideTransition: 'fade', // fade, slide or plain
                //     allowToastClose: true, // Boolean value true or false
                //     stack: 5, // false if there should be only one toast at a time or a number representing the maximum number of toasts to be shown at a time
                //     position: 'bottom-right', // bottom-left or bottom-right or bottom-center or top-left or top-right or top-center or mid-center or an object representing the left, right, top, bottom values
                //     textAlign: 'left',  // Text alignment i.e. left, right or center
                //     loader: true,  // Whether to show loader or not. True by default
                //     loaderBg: '#9598a6',  // Background color of the toast loader
                //     beforeShow: function () {
                //     }, // will be triggered before the toast is shown
                //     afterShown: function () {
                //     }, // will be triggered after the toat has been shown
                //     beforeHide: function () {
                //     }, // will be triggered before the toast gets hidden
                //     afterHidden: function () {
                //     }  // will be triggered after the toast has been hidden
                // });

                var message = (errorMessage && errorMessage.trim() !== '')
                    ? errorMessage
                    : "해당 사용자의 정보가 없습니다.";

                // 로그인 실패 메시지는 알림(토스트/모달)로만 표시
                if (window.Swal && Swal.mixin) {
                    Swal.mixin({
                        toast: true,
                        position: 'top',
                        showConfirmButton: false,
                        timer: 1800,
                        timerProgressBar: true
                    }).fire({
                        icon: 'warning',
                        title: message
                    });
                } else {
                    alert(message);
                }

                // 기존 빨간 박스 영역은 사용하지 않음
                $("#divLoginFailMessage").hide().empty();
            }
            $("#loginMessage").html("").trigger("create");
        }
    });
}

//회원가입 신청 페이지로 이동
function showPageJoin() {
    $.mobile.changePage("#pageJoin", { changeHash: false });
    setCurrentPage("pageJoin");
    injectionAppUserInsert("divJoin");

    setTimeout(function () {
        $("#txtPasswordAppUserInsert").focus();
        setTimeout(function () {
            $("#txtAreaNameAppUserInsert").focus();
        }, 250); // 0.5초 후 업체명 필드로 포커스 이동
    }, 250); // 0.3초 후 비밀번호 필드에 먼저 포커스
}


//메인 페이지로 이동


// 앱 설정 초기화하기
function initializeAppConfig() {
    $("#hdnStartDateCustomerBiz").attr("value", addDay(getToday(""), -30, "-"));
    $("#hdnEndDateCustomerBiz").attr("value", getToday("-"));
    $("#hdnStartDateCustomerBookWeight").attr("value", firstDay(getToday(""), "-"));
    $("#hdnEndDateCustomerBookWeight").attr("value", getToday("-"));
    $("#hdnStartDateCustomerBookVolume").attr("value", addDay(getToday(""), -90, "-"));
    $("#hdnEndDateCustomerBookVolume").attr("value", getToday("-"));
    $("#hdnStartDateCustomerBookTaxInvoice").attr("value", addDay(getToday(""), -90, "-"));
    $("#hdnEndDateCustomerBookTaxInvoice").attr("value", getToday("-"));
    $("#hdnStartDateCustomerBookItemBalance").attr("value", firstDay(getToday(""), "-"));
    $("#hdnEndDateCustomerBookItemBalance").attr("value", getToday("-"));
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerDetail");
    $("#hdnCurrentPageCustomerBookSub").attr("value", "");
    $("#hdnCurrentPageCustomerSaleSub").attr("value", "pageCustomerSaleWeightInsert");
    $("#hdnCurrentPageCustomerSaftyCheckSub").attr("value", "pageCustomerSaftyCheckList");
    $("#hdnCurrentCustomerCode").attr("value", "");
    $("#hdnCurrentCustomerType").attr("value", "");
    $("#hdnTapholdCustomerCode").attr("value", "");
    $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "");

    setAllCustomerBizRequireRefresh();

    //세션에 기존 거래처별 정보 클리어
    $.ajax({
        url: gasmaxWebappPath + "reset_session_customer_ajx.jsp",
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
        }
    });
}

//환경설정 페이지로 이동
function showPageAppUserEdit() {
    $.mobile.changePage("#pageAppUserEdit", { changeHash: false });
    setCurrentPage("pageAppUserEdit");
    $.ajax({
        url: gasmaxWebappPath + "app_user_edit.jsp?uuid=" + (window.sessionStorage.uuid || "") + "&darkMode=" + localStorage.getItem("darkMode"),
        type:
            "get",
        dataType:
            "html",
        timeout:
            60000,
        error:

            function (result) {
                if (result.status == 200) {
                    var html = getResultMessage("페이지가 존재하지 않습니다.", false);
                    $("#" + tagId).html(html).trigger("create");
                } else if (result.status == 0) {
                    console.log("서버 응답 지연 (timeout 또는 연결 실패)");
                }
            }

        ,
        success: function (html) {
            $("#divAppUserEdit").html(html).trigger("create");
        }
    })
        ;
}

// 환경설정 영업소 선택 변경 시
function changeAreaCodeAppUserEdit() {
    $("#hdnAreaNameAppUserEdit").attr("value", $("#selectAreaCodeAppUserEdit option:selected").attr("value2"));
    var areaCode = $("#selectAreaCodeAppUserEdit").attr("value");
    var userEmployeeCode = $("#userEmployeeCodeAppUserEdit").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "app_user_update_search_employee_ajx.jsp",
        type: "post",
        data: "areaCode=" + areaCode
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("EmployeeCode").each(function () {
                rowCount++;
                var employeeCode = $(this).find("employeeCode").text(); //코드
                var employeeName = $(this).find("employeeName").text(); //이름
                var selectedStr = "";
                if (employeeCode == userEmployeeCode) {
                    selectedStr = "selected";
                }
                html += '<option value="' + employeeCode + '" value2="' + employeeName + '" ' + selectedStr + ' > [ ' + employeeCode + ' ] ' + employeeName + '</option>';
            });
            $("#selectEmployeeCodeAppUserEdit").html(html).selectmenu("refresh");
            $("#selectEmployeeCodeAppUserEdit").trigger("change");
        }
    });
}

// 환경설정 담당자 선택 변경 시
function changeEmployeeCodeAppUserEdit() {
    $("#hdnEmployeeNameAppUserEdit").attr("value", $("#selectEmployeeCodeAppUserEdit option:selected").attr("value2"));
}

//환경설정 저장버튼 처리
function clickSaveAppUserEdit() {
    $("#divSaveMessageAppUserEdit").html(getResultMessage("저장 중입니다.", true)).trigger("create");
    var macNumber = $("#hdnUuid").attr("value");
    if (macNumber == "") {
        $("#divSaveMessageAppUserInsert").html(getResultMessage("모바일 기기에서만 가입신청이 가능합니다.", false)).trigger("create");
        return;
    }
    var areaSeq = window.sessionStorage["current_AreaSeq"];
    if (areaSeq == undefined) {
        areaSeq = "0";
    }

    var areaCode = $("#selectAreaCodeAppUserEdit").attr("value");
    var areaName = $("#hdnAreaNameAppUserEdit").attr("value");
    var employeeCode = $("#selectEmployeeCodeAppUserEdit").attr("value");
    var employeeName = $("#hdnEmployeeNameAppUserEdit").attr("value");
    var phoneAreaNumber = $("#selectPhoneAreaNumberAppUserEdit").attr("value");
    var address = $("#txtAddressAppUserEdit").attr("value");
    var userId = $("#txtUserIdAppUserEdit").attr("value");
    if (userId == "") {
        $("#divSaveMessageAppUserEdit").html(getResultMessage("사용자명을 입력해주세요.", false)).trigger("create");
        return;
    }
    var password = $("#txtPasswordAppUserEdit").attr("value");
    if (password == "") {
        $("#divSaveMessageAppUserEdit").html(getResultMessage("비밀번호를 입력해주세요.", false)).trigger("create");
        return;
    }
    var menuPermission = $("#txtMenuPermissionAppUserEdit").attr("value");
    var gasType = $("#selectGasTypeAppUserEdit").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "app_user_update_save_s3_ajx.jsp",
        type: "post",
        data: "macNumber=" + sec(macNumber)
            + "&areaCode=" + areaCode
            + "&areaName=" + areaName
            + "&employeeCode=" + employeeCode
            + "&employeeName=" + employeeName
            + "&phoneAreaNumber=" + phoneAreaNumber
            + "&address=" + address
            + "&userId=" + userId
            + "&password=" + sec(password)
            + "&menuPermission=" + menuPermission
            + "&gasType=" + gasType
            + "&areaSeq=" + areaSeq
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var result = $(xml).find("code").text();
            var html = "";
            if (result == "Y") {
                html = getResultMessage("저장이 완료되었습니다.", false);
            } else {
                html = getResultMessage($(xml).find("description").text(), false);
            }

            initializeAppConfig();// 앱설정 초기화

            $("#hdnCurrentCustomerAreaCode").attr("value", areaCode);
            $("#hdnGasType").attr("value", gasType);

            //메뉴권한 초기화하기
            var menuPermissionCustomerBook = menuPermission.substr(3, 1);	// 거래장부
            var menuPermissionSale = menuPermission.substr(4, 1);			// 판매등록/현황
            var menuPermissionReadMeter = menuPermission.substr(5, 1);		// 검침등록/현황
            var menuPermissionCollect = menuPermission.substr(6, 1);			// 수금등록/현황
            var menuPermissionSaftyCheck = menuPermission.substr(7, 1);		// 안전점검
            var menuPermissionCid = menuPermission.substr(8, 1);				// CID 수신
            var menuPermissionCustomerInsert = menuPermission.substr(9, 1);	// 거래처등록/수정
            var menuPermissionUnpaid = menuPermission.substr(10, 1);			// 미수현황
            $("#hdnMenuPermissionCustomerBook").attr("value", menuPermissionCustomerBook);
            $("#hdnMenuPermissionSale").attr("value", menuPermissionSale);
            $("#hdnMenuPermissionReadMeter").attr("value", menuPermissionReadMeter);
            $("#hdnMenuPermissionCollect").attr("value", menuPermissionCollect);
            $("#hdnMenuPermissionSaftyCheck").attr("value", menuPermissionSaftyCheck);
            $("#hdnMenuPermissionCid").attr("value", menuPermissionCid);
            $("#hdnMenuPermissionCustomerInsert").attr("value", menuPermissionCustomerInsert);
            $("#hdnMenuPermissionUnpaid").attr("value", menuPermissionUnpaid);
            $("#divSaveMessageAppUserEdit").html(html).trigger("create");
        }
    });
}

//거래처별 업무 페이지로 이동
function showCustomerBiz() {

    var currentPageCustomerBiz = $("#hdnCurrentPageCustomerBiz").attr("value");
    var currentCustomerCode = $("#hdnCurrentCustomerCode").attr("value");
    if (currentCustomerCode == "") { //거래처가 한 번도 선택된 적이 없을 때
        showPageCustomerSearch();
    } else if (currentPageCustomerBiz == "pageCustomerDetail") { //최종적으로 선택한 거래처별 업무가 거래처 상세 일 때
        showPageCustomerDetail();
    } else if (currentPageCustomerBiz == "pageCustomerBookWeight") { //거래장부-일반장부 내역 일 때
        showPageCustomerBookWeight($('input:radio[name="rdoCustomerBookWeight"]:checked').val());
    } else if (currentPageCustomerBiz == "pageCustomerBookVolume") { //거래장부-체적장부 일 때
        showPageCustomerBookVolume($('input:radio[name="rdoCustomerBookVolume"]:checked').val());
    } else if (currentPageCustomerBiz == "pageCustomerBookTaxInvoice") { //거래장부-세금계산서 일 때
        showPageCustomerBookTaxInvoice();
    } else if (currentPageCustomerBiz == "pageCustomerBookItemBalance") { //거래장부-재고현황 일 때
        showPageCustomerBookItemBalance();
    } else if (currentPageCustomerBiz == "pageCustomerSaleWeightInsert") { //판매등록-일반판매 일 때
        showPageCustomerSaleWeightInsert();
    } else if (currentPageCustomerBiz == "pageCustomerSaleVolumeInsert") { //판매등록-체적공급 일 때
        showPageCustomerSaleVolumeInsert();
    } else if (currentPageCustomerBiz == "pageCustomerReadMeterInsert") { //검침등록 일 때
        showPageCustomerReadMeterInsert();
    } else if (currentPageCustomerBiz == "pageCustomerCollect") { //수금등록 일 때
        showPageCustomerCollect();
    } else if (currentPageCustomerBiz == "pageCustomerSaftyCheckList") { //안전점검-점검이력 일 때
        showPageCustomerSaftyCheckList();
    } else if (currentPageCustomerBiz == "pageCustomerSaftyCheckInsert") { //안전점검-점검등록 일 때
        showPageCustomerSaftyCheckInsert();
    }
}

//거래처 검색 페이지로 이동
function showPageCustomerSearch(refresh) {
    if (refresh == undefined) {
        refresh = true;
    }
    if (refresh == false) {
        $.mobile.changePage("#pageCustomerSearch", { changeHash: false });
        return;
    }
    $.mobile.changePage("#pageCustomerSearch", { changeHash: false });
    setCurrentPage("pageCustomerSearch");
    if ($("#hdnCidCustomerSearchYesNo").attr("value") == "Y") { //만일 CID 편집화면에서 검색한 경우에는 이전 페이지를 CID 편집화면으로 강제로 변경함.
        $("#hdnPreviousPage").attr("value", "pageManageCidEdit");
    }
    var previousPage = $("#hdnPreviousPage").attr("value");
    if (previousPage == "pageManageCidEdit") {
        $("#hdnCidCustomerSearchYesNo").attr("value", "Y");
    } else {
        $("#hdnCidCustomerSearchYesNo").attr("value", "N");
    }
    focusControl("txtCustomerKeyword");
}

//거래처 신규등록 페이지로 이동
function showPageCustomerSearchInsert() {
    /*
    var menuPermissionCustomerInsert = $("#hdnMenuPermissionCustomerInsert").attr("value");
    if (menuPermissionCustomerInsert == "0") {
    } else if (menuPermissionCustomerInsert == "1"){
    } else if (menuPermissionCustomerInsert == "2"){
        alert("권한이 없습니다.");
        return;
    }
    */
    if (!hasPermission("hdnMenuPermissionCustomerInsert", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    $.mobile.changePage("#pageCustomerSearchInsert", { changeHash: false });
    setCurrentPage("pageCustomerSearchInsert");
    injectionCustomerInsert("contentCustomerSearchInsert");
}

//거래처 수정 페이지로 이동
function showPageCustomerDetailUpdate() {
    /*
    var menuPermissionCustomerInsert = $("#hdnMenuPermissionCustomerInsert").attr("value");
    if (menuPermissionCustomerInsert == "0") {
    } else if (menuPermissionCustomerInsert == "1"){
    } else if (menuPermissionCustomerInsert == "2"){
        alert("권한이 없습니다.");
        return;
    }
    */
    if (!hasPermission("hdnMenuPermissionCustomerInsert", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    $.mobile.changePage("#pageCustomerDetailUpdate", { changeHash: false });
    setCurrentPage("pageCustomerDetailUpdate");
    injectionCustomerUpdate("contentCustomerDetailUpdate");
}

//거래처 상세 페이지로 이동
function showPageCustomerDetail() {
    showActivityIndicator("로딩중입니다....")
    $.mobile.changePage("#pageCustomerDetail", { changeHash: false });
    setCurrentPage("pageCustomerDetail");
    if ($("#hdnRequireRefreshPageCustomerDetail").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerDetail");
        injectionCustomerDetail("listviewCustomerDetail");
    }
    hideActivityIndicator()
    injectionFooterCustomerBiz("footerCustomerDetail", 0); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerDetail");

}

//거래처별 거래장부 페이지로 이동
function showPageCustomerBook() {
    var menuPermissionCustomerBook = $("#hdnMenuPermissionCustomerBook").attr("value");
    if (menuPermissionCustomerBook == "0") {
    } else if (menuPermissionCustomerBook == "1") {
    } else if (menuPermissionCustomerBook == "2") {
        alert("권한이 없습니다.");
        return;
    }
    var currentPageCustomerBookSub = $("#hdnCurrentPageCustomerBookSub").attr("value");
    var currentCustomerType = $("#hdnCurrentCustomerType").attr("value");
    if (currentPageCustomerBookSub == "") { //초기에
        if (currentCustomerType == "1") { //거래처 유형이 체적이라면
            showPageCustomerBookVolume("0");
        } else { //아니면 일반 장부로
            showPageCustomerBookWeight("0");
        }
    } else {
        if (currentPageCustomerBookSub == "pageCustomerBookWeight") { //거래장부-일반장부 일 때
            showPageCustomerBookWeight($('input:radio[name="rdoCustomerBookWeight"]:checked').val());
        } else if (currentPageCustomerBookSub == "pageCustomerBookVolume") { //거래장부-체적장부 일 때
            showPageCustomerBookVolume($('input:radio[name="rdoCustomerBookVolume"]:checked').val());
        } else if (currentPageCustomerBookSub == "pageCustomerBookTaxInvoice") { //거래장부-세금계산서 일 때
            showPageCustomerBookTaxInvoice();
        } else if (currentPageCustomerBookSub == "pageCustomerBookItemBalance") { //거래장부-재고현황 일 때
            showPageCustomerBookItemBalance();
        } else {
            showPageCustomerBookWeight($('input:radio[name="rdoCustomerBookWeight"]:checked').val()); // 초기 체적이 아닐경우 일반장부로
        }
    }
}

//거래처별 판매등록 페이지로 이동
function showPageCustomerSale() {
    /*
    var menuPermissionSale = $("#hdnMenuPermissionSale").attr("value");
    if (menuPermissionSale == "0") {
    } else if (menuPermissionSale == "1"){
    } else if (menuPermissionSale == "2"){
        alert("권한이 없습니다.");
        showPageCustomerDetail();
        return;
    }
    */
    if (!hasPermission("hdnMenuPermissionSale", ["0"])) {
        alert("권한이 없습니다.");
        resetActiveStateFooterCustomerBiz();
        return;
    }

    var currentCustomerType = $("#hdnCurrentCustomerType").attr("value");
    if (currentCustomerType == "1") { //체적업체일 때
        showPageCustomerSaleVolumeInsert();
    } else { // 그외의 경우 일반판매로
        showPageCustomerSaleWeightInsert();
    }
    //	var currentPageCustomerSaleSub = $("#hdnCurrentPageCustomerSaleSub").attr("value");
    //	if (currentPageCustomerSaleSub=="pageCustomerSaleWeightInsert"){ //판매등록-일반판매 일 때
    //		showPageCustomerSaleWeightInsert();
    //	}else if (currentPageCustomerSaleSub=="pageCustomerSaleVolumeInsert"){ //판매등록-체적공급 일 때
    //		showPageCustomerSaleVolumeInsert();
    //	}
}

//거래처별 수금등록 페이지로 이동
function showPageCustomerCollect() {
    /*
    var menuPermissionCollect = $("#hdnMenuPermissionCollect").attr("value");
    if (menuPermissionCollect == "0") {
    } else if (menuPermissionCollect == "1"){
    } else if (menuPermissionCollect == "2"){
        alert("권한이 없습니다.");
        showPageCustomerDetail();
        return;
    }
    */
    if (!hasPermission("hdnMenuPermissionCollect", ["0"])) {
        alert("권한이 없습니다.");
        resetActiveStateFooterCustomerBiz();
        return;
    }

    $.mobile.changePage("#pageCustomerCollect", { changeHash: false });
    setCurrentPage("pageCustomerCollect");
    if ($("#hdnRequireRefreshPageCustomerCollect").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerCollect"); //거래처 요약정보
    }
    injectionCustomerCollect("divCustomerCollect"); //거래처 등록 화면
    injectionFooterCustomerBiz("footerCustomerCollect", 4); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerCollect"); //최종 선택한 거래처별 메뉴 갱신
}

//거래처별 안전점검 페이지로 이동
function showPageCustomerSaftyCheck() {
    /*
    var menuPermissionSaftyCheck = $("#hdnMenuPermissionSaftyCheck").attr("value");
    if (menuPermissionSaftyCheck == "0") {
    } else if (menuPermissionSaftyCheck == "1"){
    } else if (menuPermissionSaftyCheck == "2"){
        alert("권한이 없습니다.");
        showPageCustomerDetail();
        return;
    }
    */
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        resetActiveStateFooterCustomerBiz();
        return;
    }

    var currentPageCustomerSaftyCheckSub = $("#hdnCurrentPageCustomerSaftyCheckSub").attr("value");
    if (currentPageCustomerSaftyCheckSub == "pageCustomerSaftyCheckList") { //안전점검-점검이력 일 때
        showPageCustomerSaftyCheckList();
    } else if (currentPageCustomerSaftyCheckSub == "pageCustomerSaftyCheckInsert") { //안전점검-소비설비 점검등록 일 때
        showPageCustomerSaftyCheckInsert();
    } else if (currentPageCustomerSaftyCheckSub == "pageCustomerSaftyCheckTankInsert") { //안전점검-저장탱크 점검등록 일 때
        showPageCustomerSaftyCheckTankInsert();
    }
}

// 현재 상태에 따라서 안전점검 신규 추가 또는 수정 페이지로 이동하기
function showPageCustomerSaftyCheckInsertEdit() {
    var insertMode = $("#hdnInsertModeCustomerSaftyCheckSign").attr("value");
    if (insertMode == "0") { //신규모드
        showPageCustomerSaftyCheckInsert(false);
    } else {//수정모드
        showPageCustomerSaftyCheckEdit("", false);
    }
}

//거래처 거래장부-일반장부 페이지로 이동
function showPageCustomerBookWeight(pageType) {
    $.mobile.changePage("#pageCustomerBookWeight", { changeHash: false });
    setCurrentPage("pageCustomerBookWeight");
    if ($("#hdnRequireRefreshPageCustomerBookWeight").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerBookWeight"); //거래처 요약정보
        injectionSearchOptionCustomerBookWeight("searchOptionCustomerBookWeight", pageType); //검색조건
    }
    $("#txtStartDateCustomerBookWeight").attr("value", $("#hdnStartDateCustomerBookWeight").attr("value"));
    $("#txtEndDateCustomerBookWeight").attr("value", $("#hdnEndDateCustomerBookWeight").attr("value"));
    searchCustomerBookWeight();
    injectionSubFooterCustomerBook("subFooterCustomerBookWeight", 1); //서브메뉴
    injectionFooterCustomerBiz("footerCustomerBookWeight", 1); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerBookWeight"); //최종 선택한 거래처별 메뉴 갱신
    $("#hdnCurrentPageCustomerBookSub").attr("value", "pageCustomerBookWeight"); //최종 선택한 거래처별 하위 메뉴 갱신
}

//거래처 거래장부-일반(중량)장부 검색 버튼 처리
function searchCustomerBookWeight() {
    var searchOption = $('input:radio[name="rdoCustomerBookWeight"]:checked').val();
    if (searchOption == "0") {
        searchCustomerBookWeightCollect();
    } else if (searchOption == "1") {
        searchCustomerBookWeightSale();
    }
}

//거래처 거래장부-일반(중량)장부-수금내역 검색
function searchCustomerBookWeightCollect() {
    var tableHeaderHtml = '<table style="border: 0px solid #999999 ; border-top: 0px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; table-layout: fixed ; ">'
        + '	<tr>'
        + '		<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">일자</span></td>'
        + '		<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; white-space: nowrap ; text-overflow: ellipsis ; overflow: hidden ; " ><span style="color:#222222 ; font-size:14px ; ">품명/비고</span></td>'
        + '		<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">出/入</span></td>'
        + '		<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">사원</span></td>'
        + '	</tr>'
        + '</table>'
        + '<table style="border: 0px solid #999999 ; border-top: 0px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; table-layout: fixed ; ">'
        + '	<tr>'
        + '		<td style="width: 90px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">입금액</span></td>'
        + '		<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">D/C</span></td>'
        + '		<td style="width: 90px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">미수금액</span></td>'
        + '		<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">잔액</span></td>'
        + '	</tr>'
        + '</table>';

    $("#tableHeaderCustomerBookWeight").html(tableHeaderHtml).trigger("create");
    $("#btnMorePageCustomerBookWeight").html("").trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    //$("#divSearchResultCustomerBookWeight").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    var customerCode = $("#hdnCurrentCustomerCode").attr("value");
    var startDate = $("#txtStartDateCustomerBookWeight").attr("value");
    var endDate = $("#txtEndDateCustomerBookWeight").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_book_weight_collect_search_ajx.jsp",
        type: "post",
        data: "customerCode=" + customerCode
            + "&startDate=" + startDate
            + "&endDate=" + endDate
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookWeight").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var carriedOverAmount = $(xml).find("carriedOverAmount").text();
            var rowCount = 0;
            html = '		<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
                + '			<tr>'
                + '				<td style="width: 100px ; text-align: left ; border-left: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; font-weight: bold ; ">이월잔액</span></td>'
                + '				<td style="width: 140px ; text-align: right ; border-right: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; font-weight: bold ; ">' + insertComma(carriedOverAmount) + '</span></td>'
                + '			</tr>'
                + '		</table>';
            $(xml).find("CustomerWeightCollect").each(function () {
                rowCount++;
                var collectDate = $(this).find("collectDate").text(); //수납일
                var collectDateMMdd = collectDate.substr(4, 2) + "-" + collectDate.substr(6, 2); //수납일 MMdd 형식
                var typeCode = $(this).find("typeCode").text(); //0.가스판매, 1.용기, 2.기구, 4.A/S, 5.수금
                var itemName = $(this).find("itemName").text(); //품명
                var itemNameStyle = "";
                if (typeCode == "5") {
                    itemNameStyle = " color:  red ; ";
                }
                var inout = $(this).find("saleQuantity").text() + "/" + $(this).find("withdrawQuantity").text(); //出/入
                var employeeName = $(this).find("employeeName").text(); //사원
                var collectAmount = $(this).find("collectAmount").text(); //입금액
                var discountAmount = $(this).find("discountAmount").text(); //D/C
                var unpaidAmount = $(this).find("unpaidAmount").text(); //미수금액
                var remainAmount = $(this).find("remainAmount").text(); //잔액
                var remark = $(this).find("remark").text(); //비고
                if (remark == "") {
                } else {
                    remark = "(" + remark + ")";
                }
                var key = $(this).find("key").text(); //key

                html += '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + key + '">'
                    + '		<table style="border: 0px solid #999999 ; border-left: 1px solid #999999 ; border-top: 0px solid #999999 ; border-right: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + collectDateMMdd + '</span></td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; white-space: nowrap ; text-overflow: ellipsis ; overflow: hidden ; " ><span style="font-size:14px ; ' + itemNameStyle + '">' + itemName + '&nbsp;</span><span style="color:#666666 ; font-size:14px ; ">' + remark + '</span></td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + inout + '</span></td>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + employeeName + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '		<table style="border: 0px solid #999999 ; border-left: 1px solid #999999 ; border-top: 0px solid #999999 ; border-right: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 90px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + insertComma(collectAmount) + '</span></td>'
                    + '				<td style="width: 60px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(discountAmount) + '</span></td>'
                    + '				<td style="width: 90px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(unpaidAmount) + '</span></td>'
                    + '				<td style="text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="font-size:14px ; ">' + insertComma(remainAmount) + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
            });
            $("#divSearchResultCustomerBookWeight").html(html).trigger("create");
            $("#footerSummaryCustomerBookWeight").html("").trigger("create");
            $("#divSearchResultCustomerBookWeight a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 상세내역으로 이동
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 상세내역으로 이동
                showPageCustomerBookWeightCollectDetail(getParentSpecifiedTagId($(event.target), "a", "id"));
            });
            $('#hdnNextPageNumberCustomerBookWeight').attr("value", '2'); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookWeight").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookWeightCollect()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookWeight").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-일반(중량)장부-수금내역 검색 페이지 추가 처리하기
function clickMorePageCustomerBookWeightCollect() {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_weight_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberCustomerBookWeight").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookWeight").append(html).trigger("create");
                $("#btnMorePageCustomerBookWeight").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerWeightCollect").each(function () {
                rowCount++;
                var collectDate = $(this).find("collectDate").text(); //수납일
                var collectDateMMdd = collectDate.substr(4, 2) + "-" + collectDate.substr(6, 2); //수납일 MMdd 형식
                var typeCode = $(this).find("typeCode").text(); //0.가스판매, 1.용기, 2.기구, 4.A/S, 5.수금
                var itemName = $(this).find("itemName").text(); //품명
                var itemNameStyle = "";
                if (typeCode == "5") {
                    itemNameStyle = " color:  red ; ";
                }
                var inout = $(this).find("saleQuantity").text() + "/" + $(this).find("withdrawQuantity").text(); //出/入
                var employeeName = $(this).find("employeeName").text(); //사원
                var collectAmount = $(this).find("collectAmount").text(); //입금액
                var discountAmount = $(this).find("discountAmount").text(); //D/C
                var unpaidAmount = $(this).find("unpaidAmount").text(); //미수금액
                var remainAmount = $(this).find("remainAmount").text(); //잔액
                var remark = $(this).find("remark").text(); //비고
                if (remark == "") {
                } else {
                    remark = "(" + remark + ")";
                }
                var key = $(this).find("key").text(); //key

                var html = '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + key + '">'
                    + '		<table style="border: 0px solid #999999 ; border-left: 1px solid #999999 ; border-top: 0px solid #999999 ; border-right: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + collectDateMMdd + '</span></td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; white-space: nowrap ; text-overflow: ellipsis ; overflow: hidden ; " ><span style="font-size:14px ; ' + itemNameStyle + '">' + itemName + '&nbsp;</span><span style="color:#666666 ; font-size:14px ; ">' + remark + '</span></td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + inout + '</span></td>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + employeeName + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '		<table style="border: 0px solid #999999 ; border-left: 1px solid #999999 ; border-top: 0px solid #999999 ; border-right: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 90px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + insertComma(collectAmount) + '</span></td>'
                    + '				<td style="width: 60px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(discountAmount) + '</span></td>'
                    + '				<td style="width: 90px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(unpaidAmount) + '</span></td>'
                    + '				<td style="text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(remainAmount) + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
                $("#divSearchResultCustomerBookWeight").append(html).trigger("create");
            });
            $("#divSearchResultCustomerBookWeight a").unbind("click"); //모든 클릭 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
            $("#divSearchResultCustomerBookWeight a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 상세내역 출력
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 상세내역 출력
                showPageCustomerBookWeightCollectDetail(getParentSpecifiedTagId($(event.target), "a", "id"));
            });
            var nextPageNumber = parseInt($('#hdnNextPageNumberCustomerBookWeight').attr("value"), 10) + 1;
            $('#hdnNextPageNumberCustomerBookWeight').attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookWeight").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookWeightCollect()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookWeight").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-일반(중량)장부-거래상세 내역 페이지로 이동
function showPageCustomerBookWeightCollectDetail(key) {
    $.mobile.changePage("#pageCustomerBookWeightCollectDetail", { changeHash: false });
    setCurrentPage("pageCustomerBookWeightCollectDetail");
    injectionCustomerSummary("customerSummaryCustomerBookWeightCollectDetail"); //거래처 요약정보
    $("#divResultMessageCustomerBookWeightCollectDetail").html("").trigger("create");
    injectionCustomerBookWeightCollectDetail("divCustomerBookWeightCollectDetail", key); //거래 상세 내역
    //	injectionSubFooterCustomerBook("subFooterCustomerBookWeightCollectDetail", 1); //서브메뉴
    //	injectionFooterCustomerBiz("footerCustomerBookWeightCollectDetail", 1); //Footer 는 navbar 선택 표시 문제로 항상 갱신
}

//거래처 거래장부-일반장부-거래상세 정보 삽입하기
function injectionCustomerBookWeightCollectDetail(tagId, key) {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_weight_collect_detail_ajx.jsp",
        type: "post",
        data: "key=" + key,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var key = $(xml).find("key").text(); //구분
            var typeCode = $(xml).find("typeCode").text(); //구분
            var typeCodeName = "";
            if (typeCode == "0") {
                typeCodeName = "가스판매";
            } else if (typeCode == "1") {
                typeCodeName = "용기판매";
            } else if (typeCode == "2") {
                typeCodeName = "기구판매";
            } else if (typeCode == "4") {
                typeCodeName = "A/S";
            } else if (typeCode == "5") {
                typeCodeName = "수금";
            }
            var collectDate = $(xml).find("collectDate").text(); //일자
            var collectDateFormat = collectDate.substr(0, 4) + "-" + collectDate.substr(4, 2) + "-" + collectDate.substr(6, 2);
            var keyStr = "'" + key + "', '" + typeCode + "', '" + collectDateFormat + "'";
            var keyPriorStr = "'" + key + "', 'prior'";
            var keyNextStr = "'" + key + "', 'next'";
            var itemName = $(xml).find("itemName").text(); //품명
            var saleQuantity = $(xml).find("saleQuantity").text(); //납품
            var withdrawQuantity = $(xml).find("withdrawQuantity").text(); //회수
            var price = $(xml).find("price").text(); //단가
            var amount = $(xml).find("amount").text(); //공급액
            var tax = $(xml).find("tax").text(); //세액
            var totalAmount = $(xml).find("totalAmount").text(); //합계
            var collectAmount = $(xml).find("collectAmount").text(); //입금액
            var discountAmount = $(xml).find("discountAmount").text(); //D/C
            var unpaidAmount = $(xml).find("unpaidAmount").text(); //미입금액
            var employeeName = $(xml).find("employeeName").text(); //사원
            var remark = $(xml).find("remark").text(); //비고
            var collectType = $(xml).find("collectType").text(); //입금구분

            var html = '<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ;">구분:</td><td style="width: 100px ; text-align: left ; font-size:14px ;">' + typeCodeName + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">일자:</td><td style="width: 100px ; text-align: left ; font-size:14px ; font-weight: bold ;">' + collectDateFormat + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">품명:</td><td style="width: 100px ; text-align: left ; font-size:14px ;">' + itemName + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">出/入:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(saleQuantity) + '/' + insertComma(withdrawQuantity) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">단가:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(price) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 2px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">공급액:</td><td style="width: 100px ; text-align: right ; font-size:14px ; font-weight: bold ;">' + insertComma(amount) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">세액:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(tax) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">합계:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(totalAmount) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">입금액:</td><td style="width: 100px ; text-align: right ; font-size:14px ; color: blue ; ">' + insertComma(collectAmount) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">D/C:</td><td style="width: 100px ; text-align: right ; font-size:14px ; font-weight: bold ; ">' + insertComma(discountAmount) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">미입금액:</td><td style="width: 100px ; text-align: right ; font-size:14px ; font-weight: bold ; color: red ;">' + insertComma(unpaidAmount) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">사원:</td><td style="width: 100px ; text-align: left ; font-size:14px ;">' + employeeName + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">비고:</td><td style="width: 100px ; text-align: left ; font-size:14px ;">' + remark + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">입금구분:</td><td style="width: 100px ; text-align: left ; font-size:14px ; color: red ; font-weight: bold ; ">' + collectType + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '			<table style="width: 100% ; ">'
                + '				<tr>'
                + '					<td align="center">'
                + '						<input type="button" data-mini="true" data-icon="arrow-l" id="btnPriorCustomerBookWeightCollectDetail" data-corners="false" data-inline="true" onclick="navigateCustomerBookWeightCollectDetail(' + keyPriorStr + ')" value="이전"></input>'
                + '						<input type="button" data-mini="true" data-icon="arrow-r" id="btnNextCustomerBookWeightCollectDetail" data-corners="false" data-inline="true" onclick="navigateCustomerBookWeightCollectDetail(' + keyNextStr + ')" value="다음"></input>'
                + '						<input type="button" data-mini="true" data-icon="check" id="btnDeleteCustomerBookWeightCollectDetail" data-corners="false" data-inline="true" onclick="deleteCustomerBookWeightCollectDetail(' + keyStr + ')" value="삭제"></input>'
                + '					</td>'
                + '				</tr>'
                + '			</table>';
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//거래처 거래장부-일반장부-거래상세 삭제버튼 처리
function deleteCustomerBookWeightCollectDetail(key, typeCode, collectDate) {
    if (typeCode == "5") { // 수금 삭제권한 체크
        /*
        var menuPermissionSale = $("#hdnMenuPermissionCollect").attr("value");
        if (menuPermissionSale == "0") {
        } else if (menuPermissionSale == "1"){
        } else if (menuPermissionSale == "2"){
            alert("권한이 없습니다.");
            return;
        }
        */
        if (!hasPermission("hdnMenuPermissionCollect", ["0"])) {
            alert("권한이 없습니다.");
            return;
        }
    } else if ((typeCode == "0") || (typeCode == "1") || (typeCode == "2")) { // 판매 삭제권한 체크
        /*
        var menuPermissionSale = $("#hdnMenuPermissionSale").attr("value");
        if (menuPermissionSale == "0") {
        } else if (menuPermissionSale == "1"){
        } else if (menuPermissionSale == "2"){
            alert("권한이 없습니다.");
            return;
        }
        */
        if (!hasPermission("hdnMenuPermissionSale", ["0"])) {
            alert("권한이 없습니다.");
            return;
        }
    } else {
        alert("삭제할 수 없습니다.");
        return;
    }
    if (!confirm("삭제하시겠습니까?")) {
        return;
    }
    $("#divResultMessageCustomerBookWeightCollectDetail").html(getResultMessage("삭제 중입니다.", true)).trigger("create");

    $.ajax({
        url: gasmaxWebappPath + "customer_book_weight_collect_detail_delete_ajx.jsp",
        data: "key=" + key
        ,
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageCustomerBookWeightCollectDetail").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                setAllCustomerBizRequireRefresh(); // 모든 거래처별 업무 Refresh 필요.
                $("#divResultMessageCustomerBookWeightCollectDetail").html("").trigger("create");
                showPageCustomerBookWeight("0");
            }
            $("#divResultMessageCustomerBookWeightCollectDetail").html(html).trigger("create");
        }
    });
}

//거래처 거래장부-일반장부-거래상세화면 이전 다음 처리를 위한 키 가져오기
function navigateCustomerBookWeightCollectDetail(key, direction) {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_weight_collect_detail_navigate_ajx.jsp",
        data: "key=" + key
            + "&direction=" + direction
        ,
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageCustomerBookWeightCollectDetail").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var key = $(xml).find("key").text();
            if (key == "X") {
                $("#divResultMessageCustomerBookWeightCollectDetail").html(getResultMessage("더 이상 자료가 없습니다.", false)).trigger("create");
            } else {
                $("#divResultMessageCustomerBookWeightCollectDetail").html("").trigger("create");
                injectionCustomerBookWeightCollectDetail("divCustomerBookWeightCollectDetail", key);
            }
        }
    });
}

//거래처 거래장부-일반(중량)장부-공급내역 검색
function searchCustomerBookWeightSale() {
    var tableHeaderHtml = '<table style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
        + '	<tr>'
        + '		<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">구분</span></td>'
        + '		<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; white-space: nowrap ; text-overflow: ellipsis ; overflow: hidden ; " ><span style="color:#222222 ; font-size:14px ; ">품명</span></td>'
        + '	</tr>'
        + '</table>'
        + '<table style="border: 0px solid #999999 ; border-top: 0px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
        + '	<tr>'
        + '		<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">出/入</span></td>'
        + '		<td style="width: 100px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">공급액</span></td>'
        + '		<td style="width: 90px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">세액</span></td>'
        + '		<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">합계</span></td>'
        + '	</tr>'
        + '</table>';

    $("#tableHeaderCustomerBookWeight").html(tableHeaderHtml).trigger("create");
    $("#btnMorePageCustomerBookWeight").html("").trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    //$("#divSearchResultCustomerBookWeight").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    var customerCode = $("#hdnCurrentCustomerCode").attr("value");
    var startDate = $("#txtStartDateCustomerBookWeight").attr("value");
    var endDate = $("#txtEndDateCustomerBookWeight").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_book_weight_sale_search_ajx.jsp",
        type: "post",
        data: "customerCode=" + customerCode
            + "&startDate=" + startDate
            + "&endDate=" + endDate
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookWeight").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var totalSupplyAmount = $(xml).find("totalSupplyAmount").text();
            var totalTaxAmount = $(xml).find("totalTaxAmount").text();
            var totalSumAmount = $(xml).find("totalSumAmount").text();
            var rowCount = 0;
            $(xml).find("CustomerWeightSale").each(function () {
                rowCount++;
                var saleType = $(this).find("saleType").text(); //구분
                var itemName = $(this).find("itemName").text(); //품명
                var inout = $(this).find("saleQuantity").text() + "/" + $(this).find("withdrawQuantity").text(); //납입
                var supplyAmount = $(this).find("supplyAmount").text(); //공급액
                var taxAmount = $(this).find("taxAmount").text(); //세액
                var sumAmount = $(this).find("sumAmount").text(); //합계

                html += '		<table style="border: 0px solid #999999 ; border-left: 1px solid #999999 ; border-top: 0px solid #999999 ; border-right: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + saleType + '</span></td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; white-space: nowrap ; text-overflow: ellipsis ; overflow: hidden ; " ><span style="color:#222222 ; font-size:14px ; ">' + itemName + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '		<table style="border: 0px solid #999999 ; border-left: 1px solid #999999 ; border-top: 0px solid #999999 ; border-right: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + inout + '</span></td>'
                    + '				<td style="width: 100px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + insertComma(supplyAmount) + '</span></td>'
                    + '				<td style="width: 90px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(taxAmount) + '</span></td>'
                    + '				<td style="text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(sumAmount) + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    ;
            });
            $("#divSearchResultCustomerBookWeight").html(html).trigger("create");
            var footerHtml = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
                + '<tr><td style="text-align: right ; font-size: 16px ; "><span> 공급액: ' + insertComma(totalSupplyAmount) + ' 세액: ' + insertComma(totalTaxAmount) + ' 합계: ' + insertComma(totalSumAmount) + ' </span></td></tr>'
                + '</table>';
            $("#footerSummaryCustomerBookWeight").html(footerHtml).trigger("create");
            $('#hdnNextPageNumberCustomerBookWeight').attr("value", '2'); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookWeight").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookWeightSale()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookWeight").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-일반(중량)장부-공급내역 검색 페이지 추가 처리하기
function clickMorePageCustomerBookWeightSale() {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_weight_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberCustomerBookWeight").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookWeight").append(html).trigger("create");
                $("#btnMorePageCustomerBookWeight").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerWeightSale").each(function () {
                rowCount++;
                var saleType = $(this).find("saleType").text(); //구분
                var itemName = $(this).find("itemName").text(); //품명
                var inout = $(this).find("saleQuantity").text() + "/" + $(this).find("withdrawQuantity").text(); //납입
                var supplyAmount = $(this).find("supplyAmount").text(); //공급액
                var taxAmount = $(this).find("taxAmount").text(); //세액
                var sumAmount = $(this).find("sumAmount").text(); //합계

                var html = '		<table style="border: 0px solid #999999 ; border-left: 1px solid #999999 ; border-top: 1px solid #999999 ; border-right: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + saleType + '</span></td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; white-space: nowrap ; text-overflow: ellipsis ; overflow: hidden ; " ><span style="color:#222222 ; font-size:14px ; ">' + itemName + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '		<table style="border: 0px solid #999999 ; border-left: 1px solid #999999 ; border-top: 1px solid #999999 ; border-right: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + inout + '</span></td>'
                    + '				<td style="width: 100px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + insertComma(supplyAmount) + '</span></td>'
                    + '				<td style="width: 90px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(taxAmount) + '</span></td>'
                    + '				<td style="text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(sumAmount) + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    ;
                $("#divSearchResultCustomerBookWeight").append(html).trigger("create");
            });

            var nextPageNumber = parseInt($('#hdnNextPageNumberCustomerBookWeight').attr("value"), 10) + 1;
            $('#hdnNextPageNumberCustomerBookWeight').attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookWeight").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookWeightSale()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookWeight").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-체적장부 페이지로 이동
function showPageCustomerBookVolume(pageType) {
    $.mobile.changePage("#pageCustomerBookVolume", { changeHash: false });
    setCurrentPage("pageCustomerBookVolume");
    if ($("#hdnRequireRefreshPageCustomerBookVolume").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerBookVolume"); //거래처 요약정보
        injectionSearchOptionCustomerBookVolume("searchOptionCustomerBookVolume", pageType); //검색조건
        $("#tableHeaderCustomerBookVolume").html("").trigger("create");
        $("#divSearchResultCustomerBookVolume").html("").trigger("create");
        $("#btnMorePageCustomerBookVolume").html("").trigger("create");
    }
    $("#footerSummaryCustomerBookVolume").html("<table><tr><td>&nbsp;</td></tr></table>").trigger("create");
    $("#txtStartDateCustomerBookVolume").attr("value", $("#hdnStartDateCustomerBookVolume").attr("value"));
    $("#txtEndDateCustomerBookVolume").attr("value", $("#hdnEndDateCustomerBookVolume").attr("value"));
    searchCustomerBookVolume(pageType);
    injectionSubFooterCustomerBook("subFooterCustomerBookVolume", 2); //서브메뉴
    injectionFooterCustomerBiz("footerCustomerBookVolume", 1); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerBookVolume"); //최종 선택한 거래처별 메뉴 갱신
    $("#hdnCurrentPageCustomerBookSub").attr("value", "pageCustomerBookVolume"); //최종 선택한 거래처별 하위 메뉴 갱신
}

//거래처 거래장부-체적장부 검색 버튼 처리
function searchCustomerBookVolume(pageType) {
    var searchOption = $('input:radio[name="rdoCustomerBookVolume"]:checked').val();
    if (searchOption == undefined) {
        searchOption = "0";
    }
    if (pageType != undefined) {
        searchOption = pageType;
    }
    if (searchOption == "0") {
        searchCustomerBookVolumeReadMeter();
    } else if (searchOption == "1") {
        searchCustomerBookVolumeCollect();
    } else if (searchOption == "2") {
        searchCustomerBookVolumeSale();
    }
}

//거래처 거래장부-체적장부-검침내역 검색
function searchCustomerBookVolumeReadMeter() {
    var tableHeaderHtml = '<tr>'
        + '	<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">검침</span></td>'
        + '	<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">당검</span></td>'
        + '	<td style="width: 40px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">사용</span></td>'
        + '	<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">단가</span></td>'
        + '	<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">금액</span></td>'
        + '	<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">수납</span></td>'
        + '</tr>';
    $("#tableHeaderCustomerBookVolume").html(tableHeaderHtml).trigger("create");
    $("#btnMorePageCustomerBookVolume").html("").trigger("create");
    //$("#divSearchResultCustomerBookVolume").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    var customerCode = $("#hdnCurrentCustomerCode").attr("value");
    var startDate = $("#txtStartDateCustomerBookVolume").attr("value");
    if (startDate == undefined) {
        startDate = $("#hdnStartDateCustomerBookVolume").attr("value");
    }
    var endDate = $("#txtEndDateCustomerBookVolume").attr("value");
    if (endDate == undefined) {
        endDate = $("#hdnEndDateCustomerBookVolume").attr("value");
    }
    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_read_meter_search_ajx.jsp",
        type: "post",
        data: "customerCode=" + customerCode
            + "&startDate=" + startDate
            + "&endDate=" + endDate
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookVolume").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
            $("#footerSummaryCustomerBookVolume").html("<table><tr><td>&nbsp;</td></tr></table>").trigger("create");
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var totalNowMonthAmount = $(xml).find("totalNowMonthAmount").text();
            var rowCount = 0;
            var totalRowCount = $(xml).find("totalRowCount").text();
            $(xml).find("CustomerVolumeReadMeter").each(function () {
                rowCount++;
                var readMeterDate = $(this).find("readMeterDate").text(); //검침일
                var readMeterDateMMdd = readMeterDate.substr(2, 2) + "-" + readMeterDate.substr(4, 2) + "-" + readMeterDate.substr(6, 2); //검침일 MMdd 형식
                var nowMonthReadMeter = $(this).find("nowMonthReadMeter").text(); //당검
                var useQuantity = $(this).find("useQuantity").text(); //사용
                var price = $(this).find("price").text(); //단가
                var nowMonthAmount = $(this).find("nowMonthAmount").text(); //당월금액
                var collectDate = $(this).find("collectDate").text(); //수납일
                var collectDateMMdd = "";
                if (nowMonthAmount == "0") {
                    collectDateMMdd = "";
                    unpaidStyle = "";
                } else if (collectDate == "") {
                    collectDateMMdd = "미수";
                } else {
                    collectDateMMdd = collectDate.substr(4, 2) + "-" + collectDate.substr(6, 2); //수납일
                }
                var unpaidAmount = $(this).find("unpaidAmount").text(); //미수잔액
                var unpaidStyle = "color: black ; ";
                if (unpaidAmount != "0") {
                    unpaidStyle = "color: red ; ";
                }
                var key = $(this).find("key").text();
                html += '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + key + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ;  table-layout: fixed ; ">'
                    + '			<tr style="height: 40px">'
                    + '			<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + readMeterDateMMdd + '</span></td>'
                    + '			<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + nowMonthReadMeter + '</span></td>'
                    + '			<td style="width: 40px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + useQuantity + '</span></td>'
                    + '			<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ;  ">' + insertComma(price) + '</span></td>'
                    + '			<td style="text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ' + unpaidStyle + '">' + insertComma(nowMonthAmount) + '</span></td>'
                    + '			<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ' + unpaidStyle + '">' + collectDateMMdd + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
            });
            $("#divSearchResultCustomerBookVolume").html(html).trigger("create");

            $("#divSearchResultCustomerBookVolume a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 상세내역으로 이동
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 상세내역 출력
                showPageCustomerBookVolumeReadMeterDetail(getParentSpecifiedTagId($(event.target), "a", "id"));
            });
            var footerHtml = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
                + '<tr><td style="text-align: right ; font-size: 16px ; "><span> 합계: (' + insertComma(totalRowCount) + " 건) 금액: " + insertComma(totalNowMonthAmount) + " 원 </span></td></tr>"
                + '</table>';
            $("#footerSummaryCustomerBookVolume").html(footerHtml).trigger("create");
            $('#hdnNextPageNumberCustomerBookVolume').attr("value", '2'); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookVolume").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookVolumeReadMeter()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookVolume").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-체적장부-검침내역 검색 페이지 추가 처리하기
function clickMorePageCustomerBookVolumeReadMeter() {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberCustomerBookVolume").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookVolume").append(html).trigger("create");
                $("#btnMorePageCustomerBookVolume").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerVolumeReadMeter").each(function () {
                rowCount++;
                var readMeterDate = $(this).find("readMeterDate").text(); //검침일
                var readMeterDateMMdd = readMeterDate.substr(2, 2) + "-" + readMeterDate.substr(4, 2) + "-" + readMeterDate.substr(6, 2); //검침일 MMdd 형식
                var nowMonthReadMeter = $(this).find("nowMonthReadMeter").text(); //당검
                var useQuantity = $(this).find("useQuantity").text(); //사용
                var price = $(this).find("price").text(); //단가
                var nowMonthAmount = $(this).find("nowMonthAmount").text(); //당월금액
                var collectDate = $(this).find("collectDate").text(); //수납일
                var collectDateMMdd = "";
                var unpaidStyle = "color: black ; ";
                if (nowMonthAmount == "0") {
                    collectDateMMdd = "";
                    unpaidStyle = "";
                } else if (collectDate == "") {
                    collectDateMMdd = "미수";
                    unpaidStyle = "color: red ; ";
                } else {
                    collectDateMMdd = collectDate.substr(4, 2) + "-" + collectDate.substr(6, 2); //수납일
                }
                var key = $(this).find("key").text();
                var html = '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + key + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ;  table-layout: fixed ; ">'
                    + '			<tr style="height: 40px">'
                    + '			<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + readMeterDateMMdd + '</span></td>'
                    + '			<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + nowMonthReadMeter + '</span></td>'
                    + '			<td style="width: 40px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + useQuantity + '</span></td>'
                    + '			<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(price) + '</span></td>'
                    + '			<td style="text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ' + unpaidStyle + '">' + insertComma(nowMonthAmount) + '</span></td>'
                    + '			<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ' + unpaidStyle + '">' + collectDateMMdd + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
                $("#divSearchResultCustomerBookVolume").append(html).trigger("create");
            });

            $("#divSearchResultCustomerBookVolume a").unbind("click"); //모든 클릭 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
            $("#divSearchResultCustomerBookVolume a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 상세내역 출력
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 상세내역 출력
                showPageCustomerBookVolumeReadMeterDetail(getParentSpecifiedTagId($(event.target), "a", "id"));
            });
            var nextPageNumber = parseInt($('#hdnNextPageNumberCustomerBookVolume').attr("value"), 10) + 1;
            $('#hdnNextPageNumberCustomerBookVolume').attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookVolume").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookVolumeReadMeter()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookVolume").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-체적장부-수금내역 검색
function searchCustomerBookVolumeCollect() {
    var tableHeaderHtml = '<tr>'
        + '	<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">일자</span></td>'
        + '	<td style="width: 35px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">방법</span></td>'
        + '	<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">수금</span></td>'
        + '	<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">D/C</span></td>'
        + '	<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">사원</span></td>'
        + '	<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">비고</span></td>'
        + '</tr>';
    $("#tableHeaderCustomerBookVolume").html(tableHeaderHtml).trigger("create");
    $("#btnMorePageCustomerBookVolume").html("").trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    //$("#divSearchResultCustomerBookVolume").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    var customerCode = $("#hdnCurrentCustomerCode").attr("value");
    var startDate = $("#txtStartDateCustomerBookVolume").attr("value");
    var endDate = $("#txtEndDateCustomerBookVolume").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_collect_search_ajx.jsp",
        type: "post",
        data: "customerCode=" + customerCode
            + "&startDate=" + startDate
            + "&endDate=" + endDate
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookVolume").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
            $("#footerSummaryCustomerBookVolume").html("<table><tr><td>&nbsp;</td></tr></table>").trigger("create");
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var totalRowCount = $(xml).find("totalRowCount").text();
            var totalCollectAmount = $(xml).find("totalCollectAmount").text();
            var totalDiscountAmount = $(xml).find("totalDiscountAmount").text();
            var rowCount = 0;
            $(xml).find("CustomerVolumeCollect").each(function () {
                rowCount++;
                var key = $(this).find("key").text(); //key
                var collectDate = $(this).find("collectDate").text(); //수납일
                var collectDateMMdd = collectDate.substr(2, 2) + "-" + collectDate.substr(4, 2) + "-" + collectDate.substr(6, 2); //수납일 MMdd 형식
                var collectTypeName = $(this).find("collectTypeName").text(); //방법
                var collectAmount = $(this).find("collectAmount").text(); //수금액
                var discountAmount = $(this).find("discountAmount").text(); //D/C
                var employeeName = $(this).find("employeeName").text(); //사원명
                var remark = $(this).find("remark").text(); //비고

                html += '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + key + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ;  table-layout: fixed ; ">'
                    + '			<tr style="height: 40px">'
                    + '			<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + collectDateMMdd + '</span></td>'
                    + '			<td style="width: 35px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + collectTypeName + '</span></td>'
                    + '			<td style="width: 70px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(collectAmount) + '</span></td>'
                    + '			<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(discountAmount) + '</span></td>'
                    + '			<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + employeeName + '</span></td>'
                    + '			<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ;" ><span style="color:#222222 ; font-size:14px ;">' + remark + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '	</a>';
            });
            $("#divSearchResultCustomerBookVolume").html(html).trigger("create");
            $("#divSearchResultCustomerBookVolume a").taphold(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 삭제하기
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 삭제하기
                deleteCustomerBookVolumeCollect(getParentSpecifiedTagId($(event.target), "a", "id"));
            });
            var footerHtml = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
                + '<tr><td style="text-align: right ; font-size: 16px ; "><span> 합계: (' + insertComma(totalRowCount) + " 건) 수금액: " + insertComma(totalCollectAmount) + " 원 D/C: " + insertComma(totalDiscountAmount) + " 원 </span></td></tr>"
                + '</table>';
            $("#footerSummaryCustomerBookVolume").html(footerHtml).trigger("create");
            $('#hdnNextPageNumberCustomerBookVolume').attr("value", '2'); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookVolume").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookVolumeCollect()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookVolume").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-체적장부-수금내역 검색 페이지 추가 처리하기
function clickMorePageCustomerBookVolumeCollect() {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberCustomerBookVolume").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookVolume").append(html).trigger("create");
                $("#btnMorePageCustomerBookVolume").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerVolumeCollect").each(function () {
                rowCount++;
                var key = $(this).find("key").text(); //key
                var collectDate = $(this).find("collectDate").text(); //수납일
                var collectDateMMdd = collectDate.substr(2, 2) + "-" + collectDate.substr(4, 2) + "-" + collectDate.substr(6, 2); //수납일 MMdd 형식
                var collectTypeName = $(this).find("collectTypeName").text(); //방법
                var collectAmount = $(this).find("collectAmount").text(); //수금액
                var discountAmount = $(this).find("discountAmount").text(); //D/C
                var employeeName = $(this).find("employeeName").text(); //사원명
                var remark = $(this).find("remark").text(); //비고

                var html = '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + key + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ;  table-layout: fixed ; ">'
                    + '			<tr style="height: 40px">'
                    + '			<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + collectDateMMdd + '</span></td>'
                    + '			<td style="width: 35px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + collectTypeName + '</span></td>'
                    + '			<td style="width: 70px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(collectAmount) + '</span></td>'
                    + '			<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(discountAmount) + '</span></td>'
                    + '			<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + employeeName + '</span></td>'
                    + '			<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ;" ><span style="color:#222222 ; font-size:14px ; ">' + remark + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '	</a>';
                $("#divSearchResultCustomerBookVolume").append(html).trigger("create");
                $("#divSearchResultCustomerBookVolume a").unbind("taphold"); //모든 클릭 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
                $("#divSearchResultCustomerBookVolume a").taphold(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 삭제하기
                    //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 삭제하기
                    deleteCustomerBookVolumeCollect(getParentSpecifiedTagId($(event.target), "a", "id"));
                });
            });

            var nextPageNumber = parseInt($('#hdnNextPageNumberCustomerBookVolume').attr("value"), 10) + 1;
            $('#hdnNextPageNumberCustomerBookVolume').attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookVolume").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookVolumeCollect()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookVolume").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-체적장부-수금내역 삭제 처리
function deleteCustomerBookVolumeCollect(key) {
    /*
    var menuPermissionSale = $("#hdnMenuPermissionCollect").attr("value");
    if (menuPermissionSale == "0") {
    } else if (menuPermissionSale == "1"){
    } else if (menuPermissionSale == "2"){
        alert("권한이 없습니다.");
        return;
    }
    */
    if (!hasPermission("hdnMenuPermissionCollect", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    if (!confirm("삭제하시겠습니까?")) {
        return;
    }
    $("#divResultMessageCustomerBookVolume").html(getResultMessage("삭제 중입니다.", true)).trigger("create");

    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_collect_delete_ajx.jsp",
        data: "key=" + key
        ,
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageCustomerBookVolume").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                setAllCustomerBizRequireRefresh(); // 모든 거래처별 업무 Refresh 필요.
                $("#divResultMessageCustomerBookVolume").html("").trigger("create");
                showPageCustomerBookVolume("1");
            }
            $("#divResultMessageCustomerBookVolume").html(html).trigger("create");
        }
    });
}

//거래처 거래장부-체적장부-공급내역 검색
function searchCustomerBookVolumeSale() {
    var tableHeaderHtml = '<tr>'
        + '	<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">일자</span></td>'
        + '	<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">품명</span></td>'
        + '	<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">出/入</span></td>'
        + '	<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">누계</span></td>'
        + '	<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">사원</span></td>'
        + '</tr>';
    $("#tableHeaderCustomerBookVolume").html(tableHeaderHtml).trigger("create");
    $("#btnMorePageCustomerBookVolume").html("").trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    //$("#divSearchResultCustomerBookVolume").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    var customerCode = $("#hdnCurrentCustomerCode").attr("value");
    var startDate = $("#txtStartDateCustomerBookVolume").attr("value");
    if (startDate == undefined) {
        startDate = $("#hdnStartDateCustomerBookVolume").attr("value");
    }
    var endDate = $("#txtEndDateCustomerBookVolume").attr("value");
    if (endDate == undefined) {
        endDate = $("#hdnEndDateCustomerBookVolume").attr("value");
    }
    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_sale_search_ajx.jsp",
        type: "post",
        data: "customerCode=" + customerCode
            + "&startDate=" + startDate
            + "&endDate=" + endDate
        ,
        dataType: "xml",
        timeout: 120000,
        async: false,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookVolume").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
            $("#footerSummaryCustomerBookVolume").html("<table><tr><td>&nbsp;</td></tr></table>").trigger("create");
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var totalRowCount = $(xml).find("totalRowCount").text();
            var totalSupplyQuantity = $(xml).find("totalSupplyQuantity").text();
            var rowCount = 0;
            $(xml).find("CustomerVolumeSale").each(function () {
                rowCount++;
                var key = $(this).find("key").text(); //key
                var saleDate = $(this).find("saleDate").text(); //공급일
                var saleDateMMdd = saleDate.substr(2, 2) + "-" + saleDate.substr(4, 2) + "-" + saleDate.substr(6, 2); //공급일
                var itemName = $(this).find("itemName").text(); //품명
                var saleQuantity = $(this).find("saleQuantity").text(); //수량
                var withrawQuantity = $(this).find("withrawQuantity").text(); //회수량
                var accumulateSupplyQuantity = $(this).find("accumulateSupplyQuantity").text(); //누계
                var employeeName = $(this).find("employeeName").text(); //사원명

                html += '	<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + key + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr style="height: 40px">'
                    + '				<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + saleDateMMdd + '</span></td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + itemName + '</span></td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(saleQuantity) + ' / ' + insertComma(withrawQuantity) + '</span></td>'
                    + '				<td style="width: 70px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(accumulateSupplyQuantity) + '</span></td>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + employeeName + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '	</a>'
                    ;
            });
            $("#divSearchResultCustomerBookVolume").html(html).trigger("create");
            $("#divSearchResultCustomerBookVolume a").taphold(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. taphold 했을 때 상세내역 출력
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 상세내역 출력
                deleteCustomerBookVolumeSale(getParentSpecifiedTagId($(event.target), "a", "id"));
            });
            var footerHtml = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
                + '<tr><td style="text-align: right ; font-size: 16px ; "><span> 합계: (' + insertComma(totalRowCount) + " 건) 공급량: " + insertComma(totalSupplyQuantity) + " kg </span></td></tr>"
                + '</table>';
            $("#footerSummaryCustomerBookVolume").html(footerHtml).trigger("create");
            $('#hdnNextPageNumberCustomerBookVolume').attr("value", '2'); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookVolume").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookVolumeSale()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookVolume").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-체적장부-공급내역 검색 페이지 추가 처리하기
function clickMorePageCustomerBookVolumeSale() {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberCustomerBookVolume").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookVolume").append(html).trigger("create");
                $("#btnMorePageCustomerBookVolume").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerVolumeSale").each(function () {
                rowCount++;
                var key = $(this).find("key").text(); //key
                var saleDate = $(this).find("saleDate").text(); //공급일
                var saleDateMMdd = saleDate.substr(2, 2) + "-" + saleDate.substr(4, 2) + "-" + saleDate.substr(6, 2); //공급일
                var itemName = $(this).find("itemName").text(); //품명
                if (itemName.length > 10) {
                    itemName = itemName.substr(0, 10) + "...";
                }
                var saleQuantity = $(this).find("saleQuantity").text(); //수량
                var withrawQuantity = $(this).find("withrawQuantity").text(); //회수량
                var accumulateSupplyQuantity = $(this).find("accumulateSupplyQuantity").text(); //누계
                var employeeName = $(this).find("employeeName").text(); //사원명

                var html = '	<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + key + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ;  table-layout: fixed ; ">'
                    + '			<tr style="height: 40px">'
                    + '				<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + saleDateMMdd + '</span></td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + itemName + '</span></td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(saleQuantity) + ' / ' + insertComma(withrawQuantity) + '</span></td>'
                    + '				<td style="width: 70px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(accumulateSupplyQuantity) + '</span></td>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + employeeName + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '	</a>'
                    ;
                $("#divSearchResultCustomerBookVolume").append(html).trigger("create");
                $("#divSearchResultCustomerBookVolume a").unbind("taphold"); //모든 taphold 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
                $("#divSearchResultCustomerBookVolume a").taphold(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. taphold 했을 때 상세내역 출력
                    //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 상세내역 출력
                    deleteCustomerBookVolumeSale(getParentSpecifiedTagId($(event.target), "a", "id"));
                });
            });

            var nextPageNumber = parseInt($('#hdnNextPageNumberCustomerBookVolume').attr("value"), 10) + 1;
            $('#hdnNextPageNumberCustomerBookVolume').attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookVolume").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookVolumeSale()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookVolume").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-일반(중량)장부-공급내역 삭제 처리하기
function deleteCustomerBookVolumeSale(key) {
    /*
    var menuPermissionSale = $("#hdnMenuPermissionSale").attr("value");
    if (menuPermissionSale == "0") {
    } else if (menuPermissionSale == "1"){
    } else if (menuPermissionSale == "2"){
        alert("권한이 없습니다.");
        return;
    }
    */
    if (!hasPermission("hdnMenuPermissionSale", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    if (!confirm("삭제하시겠습니까?")) {
        return;
    }
    $("#divResultMessageCustomerBookVolume").html(getResultMessage("삭제 중입니다.", true)).trigger("create");

    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_sale_delete_ajx.jsp",
        data: "key=" + key
        ,
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageCustomerBookVolume").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                setAllCustomerBizRequireRefresh(); // 모든 거래처별 업무 Refresh 필요.
                $("#divResultMessageCustomerBookVolume").html("").trigger("create");
                showPageCustomerBookVolume("2");
            }
            $("#divResultMessageCustomerBookVolume").html(html).trigger("create");
        }
    });
}

//거래처 거래장부-체적장부-검침상세 내역 페이지로 이동
function showPageCustomerBookVolumeReadMeterDetail(key) {
    $.mobile.changePage("#pageCustomerBookVolumeReadMeterDetail", { changeHash: false });
    setCurrentPage("pageCustomerBookVolumeReadMeterDetail");
    injectionCustomerSummary("customerSummaryCustomerBookVolumeReadMeterDetail"); //거래처 요약정보
    injectionCustomerBookVolumeReadMeterDetail("divCustomerBookVolumeReadMeterDetail", key); //검침 상세 내역
    //	injectionSubFooterCustomerBook("subFooterCustomerBookVolumeReadMeterDetail", 2); //서브메뉴
    //	injectionFooterCustomerBiz("footerCustomerBookVolumeReadMeterDetail", 1); //Footer 는 navbar 선택 표시 문제로 항상 갱신
}

//거래처 거래장부-체적장부-검침상세 정보 삽입하기
function injectionCustomerBookVolumeReadMeterDetail(tagId, key) {
    //$("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    showActivityIndicator("잠시만 기다려주세요.")
    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_read_meter_detail_ajx.jsp",
        type: "post",
        data: "key=" + key,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var key = $(xml).find("key").text();
            var keyStr = "'" + key + "'";
            var keyPriorStr = "'" + key + "', 'prior'";
            var keyNextStr = "'" + key + "', 'next'";
            var sequenceNumber = $(xml).find("sequenceNumber").text();
            var readMeterCount = sequenceNumber.substr(0, 4) + "-" + sequenceNumber.substr(4, 2) + "(" + sequenceNumber.substr(6, 2) + ")"; //검침회차
            if (sequenceNumber.length == 1) sequenceNumber = "0" + sequenceNumber;
            var readMeterDate = $(xml).find("readMeterDate").text();
            var readMeterDateFormat = readMeterDate.substr(0, 4) + "-" + readMeterDate.substr(4, 2) + "-" + readMeterDate.substr(6, 2);

            var preMonthReadMeter = $(xml).find("preMonthReadMeter").text();
            var nowMonthReadMeter = $(xml).find("nowMonthReadMeter").text();
            var useQuantity = $(xml).find("useQuantity").text();
            var price = $(xml).find("price").text();
            var useAmount = $(xml).find("useAmount").text();
            var manageAmount = $(xml).find("manageAmount").text();
            var discountAmount = $(xml).find("discountAmount").text();
            var delayAmount = $(xml).find("delayAmount").text();
            var nowMonthAmount = $(xml).find("nowMonthAmount").text();
            var remark = $(xml).find("remark").text();
            var remainQuantity = $(xml).find("remainQuantity").text();
            var collectDate = $(xml).find("collectDate").text();
            if (collectDate == "null") {
                collectDate = "";
            }
            var collectDateFormat = collectDate.substr(0, 4) + "-" + collectDate.substr(4, 2) + "-" + collectDate.substr(6, 2);
            if (collectDateFormat == "--") {
                collectDateFormat = "";
            }
            var unpaidAmount = $(xml).find("unpaidAmount").text();
            var nowMonthAmountStyle = "color: #3333FF ;";
            if (unpaidAmount != "0") nowMonthAmountStyle = "color: red ;";

            var html = '<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ;">검침회차:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + readMeterCount + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">검침일자:</td><td style="width: 100px ; text-align: right ; font-size:14px ; font-weight: bold ;">' + readMeterDateFormat + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">전월검침:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(preMonthReadMeter) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">당월검침:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(nowMonthReadMeter) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">사용량:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(useQuantity) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">단가:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(price) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 2px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">사용료:</td><td style="width: 100px ; text-align: right ; font-size:14px ; font-weight: bold ;">' + insertComma(useAmount) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">관리비:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(manageAmount) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">할인액:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(discountAmount) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">연체료:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(delayAmount) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">당월금액:</td><td style="width: 100px ; text-align: right ; font-size:14px ; font-weight: bold ; ' + nowMonthAmountStyle + ' ">' + insertComma(nowMonthAmount) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">	비고:</td><td style="width: 100px ; text-align: left ; font-size:14px ;">' + remark + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">잔량:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + insertComma(remainQuantity) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #CCCCCC ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">수납일:</td><td style="width: 100px ; text-align: right ; font-size:14px ;">' + collectDateFormat + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '<table style="width: 100% ; border: 0px solid #999999 ; border-bottom: 1px solid #666666 ; border-collapse: collapse;"><tr><td style="width: 50px ; ">&nbsp;</td><td style="width: 100px ; font-size:14px ; ">미납잔액:</td><td style="width: 100px ; text-align: right ; font-size:14px ; color: red ; font-weight: bold ; ">' + insertComma(unpaidAmount) + '</td><td style="width: 50px ; ">&nbsp;</td></tr></table>'
                + '			<table style="width: 100% ; ">'
                + '				<tr>'
                + '					<td align="center">'
                + '						<input type="button" data-mini="true" data-icon="arrow-l" id="btnPriorCustomerBookVolumeReadMeterDetail" data-corners="false" data-inline="true" onclick="navigateCustomerBookVolumeReadMeterDetail(' + keyPriorStr + ')" value="이전"></input>'
                + '						<input type="button" data-mini="true" data-icon="arrow-r" id="btnNextCustomerBookVolumeReadMeterDetail" data-corners="false" data-inline="true" onclick="navigateCustomerBookVolumeReadMeterDetail(' + keyNextStr + ')" value="다음"></input>'
                + '						<input type="button" data-mini="true" data-icon="check" id="btnDeleteCustomerBookVolumeReadMeterDetail" data-corners="false" data-inline="true" onclick="deleteCustomerBookVolumeReadMeterDetail(' + keyStr + ')" value="삭제"></input></td>'
                + '				</tr>'
                + '			</table>';
            ;
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//거래처 거래장부-체적장부-검침상세 검침 삭제
function deleteCustomerBookVolumeReadMeterDetail(key) {
    /*
    var menuPermissionSale = $("#hdnMenuPermissionReadMeter").attr("value");
    if (menuPermissionSale == "0") {
    } else if (menuPermissionSale == "1"){
    } else if (menuPermissionSale == "2"){
        alert("권한이 없습니다.");
        return;
    }
    */
    if (!hasPermission("hdnMenuPermissionReadMeter", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    if (!confirm("삭제하시겠습니까?")) {
        return;
    }
    $("#divResultMessageCustomerBookVolumeReadMeterDetail").html(getResultMessage("삭제 중입니다.", true)).trigger("create");

    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_read_meter_detail_delete_ajx.jsp",
        data: "key=" + key
        ,
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageCustomerBookVolumeReadMeterDetail").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                setAllCustomerBizRequireRefresh(); // 모든 거래처별 업무 Refresh 필요.
                $("#divResultMessageCustomerBookVolumeReadMeterDetail").html("").trigger("create");
                showPageCustomerBookVolume("0");
            }
            $("#divResultMessageCustomerBookVolumeReadMeterDetail").html(html).trigger("create");
        }
    });
}

//거래처 거래장부-검침 상세화면 이전 다음 처리를 위한 키 가져오기
function navigateCustomerBookVolumeReadMeterDetail(key, direction) {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_read_meter_detail_navigate_ajx.jsp",
        data: "key=" + key
            + "&direction=" + direction
        ,
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageCustomerBookVolumeReadMeterDetail").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var key = $(xml).find("key").text();
            if (key == "X") {
                $("#divResultMessageCustomerBookVolumeReadMeterDetail").html(getResultMessage("더 이상 자료가 없습니다.", false)).trigger("create");
            } else {
                $("#divResultMessageCustomerBookVolumeReadMeterDetail").html("").trigger("create");
                injectionCustomerBookVolumeReadMeterDetail("divCustomerBookVolumeReadMeterDetail", key);
            }
        }
    });
}

//거래처 거래장부-세금계산서 페이지로 이동
function showPageCustomerBookTaxInvoice() {
    $.mobile.changePage("#pageCustomerBookTaxInvoice", { changeHash: false });
    setCurrentPage("pageCustomerBookTaxInvoice");
    if ($("#hdnRequireRefreshPageCustomerBookTaxInvoice").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerBookTaxInvoice"); //거래처 요약정보
        injectionSearchOptionCustomerBookTaxInvoice("searchOptionCustomerBookTaxInvoice"); //검색조건
        $("#tableHeaderCustomerBookTaxInvoice").html("").trigger("create");
        $("#divSearchResultCustomerBookTaxInvoice").html("").trigger("create");
        $("#btnMorePageCustomerBookTaxInvoice").html("").trigger("create");
    }
    $("#txtStartDateCustomerBookTaxInvoice").attr("value", $("#hdnStartDateCustomerBookTaxInvoice").attr("value"));
    $("#txtEndDateCustomerBookTaxInvoice").attr("value", $("#hdnEndDateCustomerBookTaxInvoice").attr("value"));
    searchCustomerBookTaxInvoice();
    injectionSubFooterCustomerBook("subFooterCustomerBookTaxInvoice", 3); //서브메뉴
    injectionFooterCustomerBiz("footerCustomerBookTaxInvoice", 1); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerBookTaxInvoice"); //최종 선택한 거래처별 메뉴 갱신
    $("#hdnCurrentPageCustomerBookSub").attr("value", "pageCustomerBookTaxInvoice"); //최종 선택한 거래처별 하위 메뉴 갱신
}

//거래처 거래장부-세금계산서 검색
function searchCustomerBookTaxInvoice() {
    $("#btnMorePageCustomerBookTaxInvoice").html("").trigger("create");
    $("#footerSummaryCustomerBookTaxInvoice").html("").trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    //$("#divSearchResultCustomerBookTaxInvoice").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    var customerCode = $("#hdnCurrentCustomerCode").attr("value");
    //var startDate = $("#txtStartDateCustomerBookTaxInvoice").attr("value");
    //var endDate = $("#txtEndDateCustomerBookTaxInvoice").attr("value");
    var startDate = $("#hdnStartDateCustomerBookTaxInvoice").attr("value");
    var endDate = $("#hdnEndDateCustomerBookTaxInvoice").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_book_tax_invoice_search_ajx.jsp",
        type: "post",
        data: "customerCode=" + customerCode
            + "&startDate=" + startDate
            + "&endDate=" + endDate
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookTaxInvoice").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            var totalSupplyAmount = $(xml).find("totalSupplyAmount").text();
            var totalTaxAmount = $(xml).find("totalTaxAmount").text();
            var totalSumAmount = $(xml).find("totalSumAmount").text();
            $(xml).find("CustomerTaxInvoice").each(function () {
                rowCount++;
                var issueDate = $(this).find("issueDate").text(); //작성일
                //				var issueDateYYYYMMDD = issueDate.substr(0,4) + "-" + issueDate.substr(4,2) + "-" + issueDate.substr(6,2); //작성일
                var issueDateYYYYMMDD = issueDate.substr(4, 2) + "-" + issueDate.substr(6, 2); //작성일
                var amount = $(this).find("amount").text(); //공급액
                var tax = $(this).find("tax").text(); //세액
                var totalAmount = $(this).find("totalAmount").text(); //합계
                var ediStatusName = $(this).find("ediStatusName").text(); //EDI
                var ntsStatusName = $(this).find("ntsStatusName").text(); //NTS
                var dataStartDate = $(this).find("dataStartDate").text(); //매출시작일
                var dataStartDateYYYYMMDD = "";
                if (dataStartDate != "") {
                    dataStartDate.substr(0, 4) + "-" + dataStartDate.substr(4, 2) + "-" + dataStartDate.substr(6, 2); //매출시작일
                }
                var dataEndDate = $(this).find("dataEndDate").text(); //매출종료일
                var dataEndDateYYYYMMDD = "";
                if (dataEndDate != "") {
                    dataEndDate.substr(0, 4) + "-" + dataEndDate.substr(4, 2) + "-" + dataEndDate.substr(6, 2); //매출종료일
                }
                var period = dataStartDateYYYYMMDD + "~" + dataEndDateYYYYMMDD; //매출기간
                if (period == "~") {
                    period = "";
                }
                var key = $(this).find("key").text();

                html += '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + key + '" id2="' + period + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr style="height: 40px ; ">'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + issueDateYYYYMMDD + '</span></td>'
                    + '				<td style="width: 80px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(amount) + '</span></td>'
                    + '				<td style="width: 70px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(tax) + '</span></td>'
                    + '				<td style="text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="font-size:14px ; ">' + insertComma(totalAmount) + '</span></td>'
                    + '				<td style="width: 30px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + ediStatusName + '</span></td>'
                    + '				<td style="width: 30px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + ntsStatusName + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
            });
            $("#divSearchResultCustomerBookTaxInvoice").html(html).trigger("create");

            $("#divSearchResultCustomerBookTaxInvoice a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 상세내역으로 이동
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 상세내역 출력
                showPageCustomerBookTaxInvoiceDetail(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"));
            });
            var footerHtml = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
                + '<tr><td style="text-align: right ; font-size: 16px ; "><span> 공급액: ' + insertComma(totalSupplyAmount) + ' 세액: ' + insertComma(totalTaxAmount) + ' 합계: ' + insertComma(totalSumAmount) + ' </span></td></tr>'
                + '</table>';
            $("#footerSummaryCustomerBookTaxInvoice").html(footerHtml).trigger("create");

            $('#hdnNextPageNumberCustomerBookTaxInvoice').attr("value", '2'); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookTaxInvoice").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookTaxInvoice()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookTaxInvoice").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-세금계산서 검색 페이지 추가 처리하기
function clickMorePageCustomerBookTaxInvoice() {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_tax_invoice_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberCustomerBookTaxInvoice").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookTaxInvoice").append(html).trigger("create");
                $("#btnMorePageCustomerBookTaxInvoice").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerTaxInvoice").each(function () {
                rowCount++;
                var issueDate = $(this).find("issueDate").text(); //작성일
                var issueDateYYYYMMDD = issueDate.substr(0, 4) + "-" + issueDate.substr(4, 2) + "-" + issueDate.substr(6, 2); //작성일 MMdd 형식
                var amount = $(this).find("amount").text(); //공급액
                var tax = $(this).find("tax").text(); //세액
                var totalAmount = $(this).find("totalAmount").text(); //합계
                var ediStatusName = $(this).find("ediStatusName").text(); //EDI
                var ntsStatusName = $(this).find("ntsStatusName").text(); //NTS
                var dataStartDate = $(this).find("dataStartDate").text(); //매출시작일
                var dataStartDateYYYYMMDD = dataStartDate.substr(0, 4) + "-" + dataStartDate.substr(4, 2) + "-" + dataStartDate.substr(6, 2); //매출시작일
                var dataEndDate = $(this).find("dataEndDate").text(); //매출종료일
                var dataEndDateYYYYMMDD = dataEndDate.substr(0, 4) + "-" + dataEndDate.substr(4, 2) + "-" + dataEndDate.substr(6, 2); //매출종료일
                var period = dataStartDateYYYYMMDD + "~" + dataEndDateYYYYMMDD; //매출기간
                var key = $(this).find("key").text();

                var html = '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + key + '" id2="' + period + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr style="height: 40px ; ">'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + issueDateYYYYMMDD + '</span></td>'
                    + '				<td style="width: 80px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(amount) + '</span></td>'
                    + '				<td style="width: 70px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(tax) + '</span></td>'
                    + '				<td style="text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="font-size:14px ; ">' + insertComma(totalAmount) + '</span></td>'
                    + '				<td style="width: 30px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + ediStatusName + '</span></td>'
                    + '				<td style="width: 30px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + ntsStatusName + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
                $("#divSearchResultCustomerBookTaxInvoice").append(html).trigger("create");
            });
            $("#divSearchResultCustomerBookTaxInvoice a").unbind("click"); //모든 클릭 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
            $("#divSearchResultCustomerBookTaxInvoice a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 상세내역으로 이동
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 상세내역 출력
                showPageCustomerBookTaxInvoiceDetail(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"));
            });

            var nextPageNumber = parseInt($('#hdnNextPageNumberCustomerBookTaxInvoice').attr("value"), 10) + 1;
            $('#hdnNextPageNumberCustomerBookTaxInvoice').attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookTaxInvoice").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookTaxInvoice()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookTaxInvoice").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-세금계산서 상세내역 페이지로 이동
function showPageCustomerBookTaxInvoiceDetail(key, period) {
    $.mobile.changePage("#pageCustomerBookTaxInvoiceDetail", { changeHash: false });
    setCurrentPage("pageCustomerBookTaxInvoiceDetail");
    injectionCustomerSummary("customerSummaryCustomerBookTaxInvoiceDetail"); //거래처 요약정보
    $("#divResultMessageCustomerTaxInvoiceDetail").html("").trigger("create");
    injectionCustomerBookTaxInvoiceDetail("divCustomerBookTaxInvoiceDetail", key, period); //세금계산서 상세 내역
    //	injectionFooterCustomerBiz("footerCustomerBookTaxInvoiceDetail", 1); //Footer 는 navbar 선택 표시 문제로 항상 갱신
}

//거래처 거래장부-세금계산서 정보 삽입하기
function injectionCustomerBookTaxInvoiceDetail(tagId, key, period) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "customer_book_tax_invoice_detail.jsp",
        type: "post",
        data: "key=" + key
            + "&period=" + period
        ,
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}


//거래처 거래장부-검침 상세화면 이전 다음 처리를 위한 키 가져오기
function navigateCustomerBookTaxInvoiceDetail(key, direction) {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_tax_invoice_detail_navigate_ajx.jsp",
        data: "key=" + key
            + "&direction=" + direction
        ,
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageCustomerTaxInvoiceDetail").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var key = $(xml).find("key").text();
            if (key == "X") {
                $("#divResultMessageCustomerTaxInvoiceDetail").html(getResultMessage("더 이상 자료가 없습니다.", false)).trigger("create");
            } else {
                $("#divResultMessageCustomerTaxInvoiceDetail").html("").trigger("create");
                injectionCustomerBookTaxInvoiceDetail("divCustomerBookTaxInvoiceDetail", key);
            }
        }
    });
}

//거래처 거래장부-재고현황 페이지로 이동
function showPageCustomerBookItemBalance() {
    $.mobile.changePage("#pageCustomerBookItemBalance", { changeHash: false });
    setCurrentPage("pageCustomerBookItemBalance");
    if ($("#hdnRequireRefreshPageCustomerBookItemBalance").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerBookItemBalance"); //거래처 요약정보
        injectionSearchOptionCustomerBookItemBalance("searchOptionCustomerBookItemBalance"); //검색조건
        $("#tableHeaderCustomerBookItemBalance").html("").trigger("create");
        $("#divSearchResultCustomerBookItemBalance").html("").trigger("create");
        $("#btnMorePageCustomerBookItemBalance").html("").trigger("create");
    }
    $("#txtStartDateCustomerBookItemBalance").attr("value", $("#hdnStartDateCustomerBookItemBalance").attr("value"));
    $("#txtEndDateCustomerBookItemBalance").attr("value", $("#hdnEndDateCustomerBookItemBalance").attr("value"));
    searchCustomerBookItemBalance();
    injectionSubFooterCustomerBook("subFooterCustomerBookItemBalance", 4); //서브메뉴
    injectionFooterCustomerBiz("footerCustomerBookItemBalance", 1); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerBookItemBalance"); //최종 선택한 거래처별 메뉴 갱신
    $("#hdnCurrentPageCustomerBookSub").attr("value", "pageCustomerBookItemBalance"); //최종 선택한 거래처별 하위 메뉴 갱신
}

//거래처 거래장부-재고내역 검색
function searchCustomerBookItemBalance() {
    searchCustomerBookItemBalanceHPG(); //고압일 때
}

//거래처 거래장부-재고내역(고압) 검색
function searchCustomerBookItemBalanceHPG() {
    $("#btnMorePageCustomerBookItemBalance").html("").trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    //$("#divSearchResultCustomerBookItemBalance").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    var customerCode = $("#hdnCurrentCustomerCode").attr("value");
    var startDate = $("#txtStartDateCustomerBookItemBalance").attr("value");
    var endDate = $("#txtEndDateCustomerBookItemBalance").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_book_item_balance_hpg_search_ajx.jsp",
        type: "post",
        data: "customerCode=" + customerCode
            + "&startDate=" + startDate
            + "&endDate=" + endDate
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookItemBalance").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {

            //showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerItemBalanceHPG").each(function () {
                rowCount++;
                var itemCode = $(this).find("itemCode").text(); //코드
                var itemName = $(this).find("itemName").text(); //품명
                var salePrice = $(this).find("salePrice").text(); //단가
                var preBalance = $(this).find("preBalance").text(); //전재고
                var itemOutput = $(this).find("itemOutput").text(); //납품/대여
                var itemInput = $(this).find("itemInput").text(); //회수
                var balance = $(this).find("balance").text(); //재고

                html += '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + itemCode + '" id2="' + itemName + '" id3="' + preBalance + '" id4="' + salePrice + '" id5="' + insertComma(itemOutput) + '/' + insertComma(itemInput) + '" id6="' + balance + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ;  table-layout: fixed ; ">'
                    + '			<tr style="height: 40px ;">'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="font-size:14px ; ">' + itemName + '</span></td>'
                    + '				<td style="width: 60px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + insertComma(salePrice) + '</span></td>'
                    + '				<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(preBalance) + '</span></td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color: red ; font-size:14px ; ">' + insertComma(itemOutput) + '/' + insertComma(itemInput) + '</span></td>'
                    + '				<td style="width: 40px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(balance) + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
            });
            $("#divSearchResultCustomerBookItemBalance").html(html).trigger("create");

            $("#divSearchResultCustomerBookItemBalance a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 상세내역 화면으로 이동
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 상세내역 출력
                showPageCustomerBookItemBalanceHPGDetailList(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"), getParentSpecifiedTagId($(event.target), "a", "id3"), getParentSpecifiedTagId($(event.target), "a", "id4"), getParentSpecifiedTagId($(event.target), "a", "id5"), getParentSpecifiedTagId($(event.target), "a", "id6"));
            });
            $('#hdnNextPageNumberCustomerBookItemBalance').attr("value", '2'); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookItemBalance").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookItemBalanceHPG()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookItemBalance").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-재고내역(고압) 검색 페이지 추가 처리하기
function clickMorePageCustomerBookItemBalanceHPG() {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_item_balance_hpg_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberCustomerBookItemBalance").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookItemBalance").append(html).trigger("create");
                $("#btnMorePageCustomerBookItemBalance").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerItemBalanceHPG").each(function () {
                rowCount++;
                var itemCode = $(this).find("itemCode").text(); //코드
                var itemName = $(this).find("itemName").text(); //품명
                var salePrice = $(this).find("salePrice").text(); //단가
                var preBalance = $(this).find("preBalance").text(); //전재고
                var itemOutput = $(this).find("itemOutput").text(); //납품/대여
                var itemInput = $(this).find("itemInput").text(); //회수
                var balance = $(this).find("preBalance").text(); //전재고

                var html = '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + itemCode + '" id2="' + itemName + '" id3="' + balance + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ;  table-layout: fixed ; ">'
                    + '			<tr style="height: 40px ;">'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + itemName + '</span></td>'
                    + '				<td style="width: 60px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + insertComma(salePrice) + '</span></td>'
                    + '				<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(preBalance) + '</span></td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color: red ; font-size:14px ; ">' + insertComma(itemOutput) + '/' + insertComma(itemInput) + '</span></td>'
                    + '				<td style="width: 40px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(balance) + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
                $("#divSearchResultCustomerBookItemBalance").append(html).trigger("create");
            });
            $("#divSearchResultCustomerBookItemBalance a").unbind("click"); //모든 클릭 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
            $("#divSearchResultCustomerBookItemBalance a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 상세내역 화면으로 이동
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 상세내역 출력
                showPageCustomerBookItemBalanceHPGDetailList(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"), getParentSpecifiedTagId($(event.target), "a", "id3"));
            });

            var nextPageNumber = parseInt($('#hdnNextPageNumberCustomerBookItemBalance').attr("value"), 10) + 1;
            $('#hdnNextPageNumberCustomerBookItemBalance').attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookItemBalance").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookItemBalanceHPG()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookItemBalance").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-재고내역(고압) 상세내역 페이지로 이동
function showPageCustomerBookItemBalanceHPGDetailList(key, itemName, preBalance, salePrice, inout, balance) {
    $.mobile.changePage("#pageCustomerBookItemBalanceDetailList", { changeHash: false });
    setCurrentPage("pageCustomerBookItemBalanceDetailList");
    injectionCustomerSummary("customerSummaryCustomerBookItemBalanceDetailList"); //거래처 요약정보
    $("#divSearchResultCustomerBookItemBalanceDetailList").html("").trigger("create");
    $("#btnMorePageCustomerBookItemBalanceDetailList").html("").trigger("create");
    injectionSearchOptionCustomerBookItemBalanceHPGDetailList("searchOptionCustomerBookItemBalanceDetailList", itemName, salePrice, preBalance, inout, balance); //검색조건
    injectionCustomerBookItemBalanceHPGDetailList(key, preBalance); //재고 상세 내역
    //	injectionSubFooterCustomerBook("subFooterCustomerBookItemBalanceDetailList", 4); //서브메뉴
    //	injectionFooterCustomerBiz("footerCustomerBookItemBalanceDetailList", 1); //Footer 는 navbar 선택 표시 문제로 항상 갱신
}

//거래처 거래장부-재고내역(고압) 검색
function injectionCustomerBookItemBalanceHPGDetailList(key, preBalance) {
    $("#btnMorePageCustomerBookItemBalanceDetailList").html("").trigger("create");
    //$("#divSearchResultCustomerBookItemBalanceDetailList").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")

    var customerCode = $("#hdnCurrentCustomerCode").attr("value");
    var startDate = $("#txtStartDateCustomerBookItemBalance").attr("value");
    var endDate = $("#txtEndDateCustomerBookItemBalance").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_book_item_balance_hpg_detail_list_search_ajx.jsp",
        type: "post",
        data: "customerCode=" + customerCode
            + "&startDate=" + startDate
            + "&endDate=" + endDate
            + "&itemCode=" + key
            + "&preBalance=" + preBalance
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookItemBalanceDetailList").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerItemBalanceHPGDetailList").each(function () {
                rowCount++;
                var issueDate = $(this).find("issueDate").text(); //작성일
                var issueDateMMDD = issueDate.substr(4, 2) + "-" + issueDate.substr(6, 2); //작성일 MMdd 형식
                var issueType = $(this).find("issueType").text(); //구분
                var outputQuantity = $(this).find("outputQuantity").text(); //납품/대여
                var inputQuantity = $(this).find("inputQuantity").text(); //회수
                var balance = $(this).find("balance").text(); //재고
                var employeeName = $(this).find("employeeName").text(); //사원
                var place = $(this).find("place").text(); //현장

                html += '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr style="height: 40px ; ">'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + issueDateMMDD + '</span></td>'
                    + '				<td style="width: 30px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + issueType + '</span></td>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(outputQuantity) + '/' + insertComma(inputQuantity) + '</span></td>'
                    + '				<td style="width: 40px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(balance) + '</span></td>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + employeeName + '</span></td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + place + '</span></td>'
                    + '			</tr>'
                    + '		</table>';
            });
            $("#divSearchResultCustomerBookItemBalanceDetailList").html(html).trigger("create");

            $('#hdnNextPageNumberCustomerBookItemBalanceDetailList').attr("value", '2'); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookItemBalanceDetailList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookItemBalanceHPGDetailList()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookItemBalanceDetailList").html("").trigger("create");
            }
        }
    });
}

//거래처 거래장부-재고내역(고압) 검색 페이지 추가 처리하기
function clickMorePageCustomerBookItemBalanceHPGDetailList() {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_item_balance_hpg_detail_list_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberCustomerBookItemBalanceDetailList").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultCustomerBookItemBalanceDetailList").append(html).trigger("create");
                $("#btnMorePageCustomerBookItemBalanceDetailList").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerItemBalanceDetailListHPG").each(function () {
                rowCount++;
                var issueDate = $(this).find("issueDate").text(); //작성일
                var issueDateMMDD = issueDate.substr(4, 2) + "-" + issueDate.substr(6, 2); //작성일 MMdd 형식
                var issueType = $(this).find("issueType").text(); //구분
                var outputQuantity = $(this).find("itemOutput").text(); //납품/대여
                var inputQuantity = $(this).find("itemInput").text(); //회수
                var balance = $(this).find("balance").text(); //재고
                var employeeName = $(this).find("employeeName").text(); //사원
                var place = $(this).find("place").text(); //현장

                var html = '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr style="height: 40px ; ">'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + issueDateMMDD + '</span></td>'
                    + '				<td style="width: 30px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + issueType + '</span></td>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(outputQuantity) + '/' + insertComma(inputQuantity) + '</span></td>'
                    + '				<td style="width: 40px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + insertComma(balance) + '</span></td>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + employeeName + '</span></td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">' + place + '</span></td>'
                    + '			</tr>'
                    + '		</table>';
                $("#divSearchResultCustomerBookItemBalanceDetailList").append(html).trigger("create");
            });

            var nextPageNumber = parseInt($('#hdnNextPageNumberCustomerBookItemBalanceDetailList').attr("value"), 10) + 1;
            $('#hdnNextPageNumberCustomerBookItemBalanceDetailList').attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageCustomerBookItemBalanceDetailList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerBookItemBalanceHPGDetailList()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageCustomerBookItemBalanceDetailList").html("").trigger("create");
            }
        }
    });
}

//거래처 판매등록-일반판매 페이지로 이동
function showPageCustomerSaleWeightInsert() {
    var menuPermissionSale = $("#hdnMenuPermissionSale").attr("value");
    if (menuPermissionSale == "0") {
    } else if (menuPermissionSale == "1") {
    } else if (menuPermissionSale == "2") {
        alert("권한이 없습니다.");
        showPageCustomerDetail();
        return;
    }
    $.mobile.changePage("#pageCustomerSaleWeightInsert", { changeHash: false });
    setCurrentPage("pageCustomerSaleWeightInsert");
    if ($("#hdnRequireRefreshPageCustomerSaleWeightInsert").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerSaleWeightInsert"); //거래처 요약정보
        injectionCustomerSaleWeightInsert("divCustomerSaleWeightInsert"); //등록화면
    }
    injectionSubFooterCustomerSale("subFooterCustomerSaleWeightInsert", 1); //서브메뉴
    injectionFooterCustomerBiz("footerCustomerSaleWeightInsert", 2); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerSaleWeightInsert"); //최종 선택한 거래처별 메뉴 갱신
    $("#hdnCurrentPageCustomerSaleSub").attr("value", "pageCustomerSaleWeightInsert"); //최종 선택한 거래처별 하위 메뉴 갱신
}

//거래처 판매등록-일반판매 비고 검색 페이지로 이동
function showPageCustomerSaleWeightInsertRemarkSearch() {
    $.mobile.changePage("#pageCustomerSaleWeightInsertRemarkSearch", { changeHash: false });
    injectionCustomerSaleWeightInsertRemarkSearch("searchResultCustomerSaleWeightInsertRemarkSearch"); //조회화면
}

//거래처 판매등록-일반판매 품목 상세 페이지로 이동 insertMode 0:수정 1:신규
function showPageCustomerSaleWeightInsertItemDetail(insertMode, itemIndex, refresh) {
    if (refresh == undefined) {
        refresh = true;
    }
    $.mobile.changePage("#pageCustomerSaleWeightInsertItemDetail", { changeHash: false });
    if (refresh == true) {
        //		injectionCustomerSummary("customerSummaryCustomerSaleWeightInsertItemDetail"); //거래처 요약정보
        injectionCustomerSaleWeightInsertItemDetail("divCustomerSaleWeightInsertItemDetail", insertMode, itemIndex); //등록화면
    }
    //	injectionFooterCustomerBiz("footerCustomerSaleWeightInsertItemDetail", 2); //Footer 는 navbar 선택 표시 문제로 항상 갱신
}

//거래처 판매등록-일반판매 품목 검색 페이지로 이동
function showPageCustomerSaleWeightInsertItemSearch() {
    $.mobile.changePage("#pageCustomerSaleWeightInsertItemSearch", { changeHash: false });
    setCurrentPage("pageCustomerSaleWeightInsertItemSearch");
    injectionCustomerSaleWeightInsertItemSearch("searchResultCustomerSaleWeightInsertItemSearch"); //등록화면
}

//거래처 판매등록-일반판매 품목 상세 비고 검색 페이지로 이동
function showPageCustomerSaleWeightInsertItemDetailRemarkSearch() {
    $.mobile.changePage("#pageCustomerSaleWeightInsertItemDetailRemarkSearch", { changeHash: false });
    setCurrentPage("pageCustomerSaleWeightInsertItemDetailRemarkSearch");
    injectionCustomerSaleWeightInsertItemDetailRemarkSearch("searchResultCustomerSaleWeightInsertItemDetailRemarkSearch"); //조회화면
}

//거래처 판매등록-체적공급 페이지로 이동
function showPageCustomerSaleVolumeInsert() {
    var menuPermissionSale = $("#hdnMenuPermissionSale").attr("value");
    if (menuPermissionSale == "0") {
    } else if (menuPermissionSale == "1") {
    } else if (menuPermissionSale == "2") {
        alert("권한이 없습니다.");
        showPageCustomerDetail();
        return;
    }
    $.mobile.changePage("#pageCustomerSaleVolumeInsert", { changeHash: false });
    setCurrentPage("pageCustomerSaleVolumeInsert");
    if ($("#hdnRequireRefreshPageCustomerSaleVolumeInsert").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerSaleVolumeInsert"); //거래처 요약정보
    }
    injectionCustomerSaleVolumeInsert("divCustomerSaleVolumeInsert"); //등록화면
    injectionSubFooterCustomerSale("subFooterCustomerSaleVolumeInsert", 2); //서브메뉴
    injectionFooterCustomerBiz("footerCustomerSaleVolumeInsert", 2); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerSaleVolumeInsert"); //최종 선택한 거래처별 메뉴 갱신
    $("#hdnCurrentPageCustomerSaleSub").attr("value", "pageCustomerSaleVolumeInsert"); //최종 선택한 거래처별 하위 메뉴 갱신
}

//거래처 검침등록 페이지로 이동
function showPageCustomerReadMeterInsert() {
    /*
    var menuPermissionReadMeter = $("#hdnMenuPermissionReadMeter").attr("value");
    if (menuPermissionReadMeter == "0") {
    } else if (menuPermissionReadMeter == "1"){
    } else if (menuPermissionReadMeter == "2"){
        alert("권한이 없습니다.");
        showPageCustomerDetail();
        return;
    }
    */
    if (!hasPermission("hdnMenuPermissionReadMeter", ["0"])) {
        alert("권한이 없습니다.");
        resetActiveStateFooterCustomerBiz();
        return;
    }

    $.mobile.changePage("#pageCustomerReadMeterInsert", { changeHash: false });
    setCurrentPage("pageCustomerReadMeterInsert");
    if ($("#hdnRequireRefreshPageCustomerReadMeterInsert").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerReadMeterInsert"); //거래처 요약정보
        injectionCustomerReadMeterInsert("divCustomerReadMeterInsert"); //등록화면
    }
    injectionFooterCustomerBiz("footerCustomerReadMeterInsert", 3); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerReadMeterInsert"); //최종 선택한 거래처별 메뉴 갱신
}

//거래처 안전점검-점검이력 페이지로 이동
function showPageCustomerSaftyCheckList() {
    var menuPermissionSaftyCheck = $("#hdnMenuPermissionSaftyCheck").attr("value");
    if (menuPermissionSaftyCheck == "0") {
    } else if (menuPermissionSaftyCheck == "1") {
    } else if (menuPermissionSaftyCheck == "2") {
        alert("권한이 없습니다.");
        showPageCustomerDetail();
        return;
    }
    $.mobile.changePage("#pageCustomerSaftyCheckList", { changeHash: false });
    $("#divResultMessageCustomerSaftyCheckEdit").html("").trigger("create"); // 메시지 삭제
    $("#divResultMessageCustomerSaftyCheckInsert").html("").trigger("create"); // 메시지 삭제
    $("#divResultMessageCustomerSaftyCheckTankEdit").html("").trigger("create"); // 메시지 삭제
    $("#divResultMessageCustomerSaftyCheckTankInsert").html("").trigger("create"); // 메시지 삭제

    setCurrentPage("pageCustomerSaftyCheckList");
    if ($("#hdnRequireRefreshPageCustomerSaftyCheckList").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerSaftyCheckList"); //거래처 요약정보
        injectionSearchOptionCustomerSaftyCheckList("searchOptionCustomerSaftyCheckList"); //검색조건
    }
    injectionSubFooterCustomerSaftyCheck("subFooterCustomerSaftyCheckList", 1); //서브메뉴
    injectionFooterCustomerBiz("footerCustomerSaftyCheckList", 5); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerSaftyCheckList"); //최종 선택한 거래처별 메뉴 갱신
    $("#hdnCurrentPageCustomerSaftyCheckSub").attr("value", "pageCustomerSaftyCheckList"); //최종 선택한 거래처별 하위 메뉴 갱신
}

//거래처 안전점검-점검수정 페이지로 이동
function showPageCustomerSaftyCheckEdit(key, sequenceNumber) {
    $.mobile.changePage("#pageCustomerSaftyCheckEdit", { changeHash: false });
    setCurrentPage("pageCustomerSaftyCheckEdit");
    injectionCustomerSummary("customerSummaryCustomerSaftyCheckEdit"); //거래처 요약정보
    injectionCustomerSaftyCheckEdit("divCustomerSaftyCheckEdit", key); //수정 내역
    injectionSubFooterCustomerSaftyCheck("subFooterCustomerSaftyCheckEdit", 2); //서브메뉴
    injectionFooterCustomerBiz("footerCustomerSaftyCheckEdit", 5); //Footer 는 navbar 선택 표시 문제로 항상 갱신
}

//거래처 안전점검-점검등록 페이지로 이동
function showPageCustomerSaftyCheckInsert() {
    var menuPermissionSaftyCheck = $("#hdnMenuPermissionSaftyCheck").attr("value");
    if (menuPermissionSaftyCheck == "0") {
    } else if (menuPermissionSaftyCheck == "1") {
    } else if (menuPermissionSaftyCheck == "2") {
        alert("권한이 없습니다.");
        showPageCustomerDetail();
        return;
    }
    $.mobile.changePage("#pageCustomerSaftyCheckInsert", { changeHash: false });
    setCurrentPage("pageCustomerSaftyCheckInsert");
    if ($("#hdnRequireRefreshPageCustomerSaftyCheckInsert").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerSaftyCheckInsert"); //거래처 요약정보
        injectionCustomerSaftyCheckInsert("divCustomerSaftyCheckInsert"); //안전점검 내역
    }
    injectionSubFooterCustomerSaftyCheck("subFooterCustomerSaftyCheckInsert", 2); //서브메뉴
    injectionFooterCustomerBiz("footerCustomerSaftyCheckInsert", 5); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerSaftyCheckInsert"); //최종 선택한 거래처별 메뉴 갱신
    $("#hdnCurrentPageCustomerSaftyCheckSub").attr("value", "pageCustomerSaftyCheckInsert"); //최종 선택한 거래처별 하위 메뉴 갱신
}

//거래처 안전점검 등록-점검 서명 페이지로 이동
function showSignatureCustomerSaftyCheckInsert() {
    popupSignatureCustomerSaftyCheckInsert("divSignatureCustomerSaftyCheckInsert"); //안전점검 서명등록/보기
}

//거래처 안전점검 수정-점검 서명 페이지로 이동
function showSignatureCustomerSaftyCheckEdit() {
    popupSignatureCustomerSaftyCheckEdit("divSignatureCustomerSaftyCheckEdit"); //안전점검 서명등록/보기
}

//[2017.11.01][Rev3] 거래처 안전점검-저장탱크 안전점검 기능 추가
//거래처 안전점검-저장탱크 점검등록 페이지로 이동
function showPageCustomerSaftyCheckTankInsert() {
    var menuPermissionSaftyCheck = $("#hdnMenuPermissionSaftyCheck").attr("value");
    if (menuPermissionSaftyCheck == "0") {
    } else if (menuPermissionSaftyCheck == "1") {
    } else if (menuPermissionSaftyCheck == "2") {
        alert("권한이 없습니다.");
        showPageCustomerDetail();
        return;
    }
    $.mobile.changePage("#pageCustomerSaftyCheckTankInsert", { changeHash: false });
    setCurrentPage("pageCustomerSaftyCheckTankInsert");
    if ($("#hdnRequireRefreshPageCustomerSaftyCheckTankInsert").attr("value") == "Y") { //Refresh 가 필요할 경우에만 처리
        injectionCustomerSummary("customerSummaryCustomerSaftyCheckTankInsert"); //거래처 요약정보
        injectionCustomerSaftyCheckTankInsert("divCustomerSaftyCheckTankInsert"); //안전점검 내역
    }
    injectionSubFooterCustomerSaftyCheck("subFooterCustomerSaftyCheckTankInsert", 3); //서브메뉴
    injectionFooterCustomerBiz("footerCustomerSaftyCheckTankInsert", 5); //Footer 는 navbar 선택 표시 문제로 항상 갱신
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerSaftyCheckTankInsert"); //최종 선택한 거래처별 메뉴 갱신
    $("#hdnCurrentPageCustomerSaftyCheckSub").attr("value", "pageCustomerSaftyCheckTankInsert"); //최종 선택한 거래처별 하위 메뉴 갱신
}

//거래처 안전점검-저장탱크 안전점검 수정 페이지로 이동
function showPageCustomerSaftyCheckTankEdit(key, sequenceNumber) {
    $.mobile.changePage("#pageCustomerSaftyCheckTankEdit", { changeHash: false });
    setCurrentPage("pageCustomerSaftyCheckTankEdit");
    injectionCustomerSummary("customerSummaryCustomerSaftyCheckTankEdit"); //거래처 요약정보
    injectionCustomerSaftyCheckTankEdit("divCustomerSaftyCheckTankEdit", key, sequenceNumber); //수정 내역
    injectionSubFooterCustomerSaftyCheck("subFooterCustomerSaftyCheckTankEdit", 3); //서브메뉴
    injectionFooterCustomerBiz("footerCustomerSaftyCheckTankEdit", 5); //Footer 는 navbar 선택 표시 문제로 항상 갱신
}

//거래처 저장탱크 안전점검 등록-점검 서명 페이지로 이동
function showSignatureCustomerSaftyCheckTankInsert() {
    popupSignatureCustomerSaftyCheckTankInsert("divSignatureCustomerSaftyCheckTankInsert"); // 저장탱크 안전점검 서명등록/보기
}

//거래처 저장탱크 안전점검 수정-점검 서명 페이지로 이동
function showSignatureCustomerSaftyCheckTankEdit() {
    popupSignatureCustomerSaftyCheckTankEdit("divSignatureCustomerSaftyCheckTankEdit"); // 저장탱크 안전점검 서명등록/보기
}

//거래처 저장탱크 안전점검-점검등록 서명 팝업표시하기
function popupSignatureCustomerSaftyCheckTankInsert(tagId) {
    $("#" + tagId).html("").trigger("create");
    var insertMode = $("#hdnInsertModeCustomerSaftyCheckTankInsert").attr("value");		// 0:신규, 1:수정
    var signatureImage = $("#hdnSignatureImageCustomerSaftyCheckTankInsert").attr("value");
    var signatureYN = "N";
    if (signatureImage && signatureImage.length > 0) {
        signatureYN = "Y";
    }

    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_tank_sign.jsp",
        data: "insertMode=" + insertMode //0:insert 1:update
            + "&signatureYN=" + signatureYN
        ,
        type: "post",
        dataType: "html",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");

            onloadSignatureCustomerSaftyCheckTankInsert(signatureImage);
            $("#popupSignatureCustomerSaftyCheckTankInsert").popup({
                corners: false,
                history: false,
                shadow: false,
                positionTo: "window",
                overlayTheme: "a"
            });

            $("#popupSignatureCustomerSaftyCheckTankInsert").popup("open");

        }
    });
}

//거래처 저장탱크 안전점검-점검수정 서명 팝업표시하기
function popupSignatureCustomerSaftyCheckTankEdit(tagId) {
    $("#" + tagId).html("").trigger("create");
    var insertMode = $("#hdnInsertModeCustomerSaftyCheckTankEdit").attr("value");		// 0:신규, 1:수정
    var signatureImage = $("#hdnSignatureImageCustomerSaftyCheckTankEdit").attr("value");
    var signatureYN = "N";
    if (signatureImage && signatureImage.length > 0) {
        signatureYN = "Y";
    }

    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_tank_sign.jsp",
        data: "insertMode=" + insertMode //0:insert 1:update
            + "&signatureYN=" + signatureYN
        ,
        type: "post",
        dataType: "html",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");

            onloadSignatureCustomerSaftyCheckTankEdit(signatureImage);
            $("#popupSignatureCustomerSaftyCheckTankEdit").popup({
                corners: false,
                history: false,
                shadow: false,
                positionTo: "window",
                overlayTheme: "a"
            });

            $("#popupSignatureCustomerSaftyCheckTankEdit").popup("open");
        }
    });
}

//모든 거래처별 업무 페이지의 Refresh 필요 여부를 모두 Y로 변경하여 각각 Refresh 할 수 있도록 함
function setAllCustomerBizRequireRefresh() {
    $("#hdnRequireRefreshPageCustomerDetail").attr("value", "Y"); //거래처 상세보기
    $("#hdnRequireRefreshPageCustomerBookWeight").attr("value", "Y"); //거래처 거래장부-일반장부
    $("#hdnRequireRefreshPageCustomerBookVolume").attr("value", "Y"); //거래처 거래장부-체적장부
    $("#hdnRequireRefreshPageCustomerBookTaxInvoice").attr("value", "Y"); //거래처 거래장부-세금계산서
    $("#hdnRequireRefreshPageCustomerBookItemBalance").attr("value", "Y"); //거래처 거래장부-재고현황
    $("#hdnRequireRefreshPageCustomerSaleWeightInsert").attr("value", "Y"); //거래처 판매등록-일반판매
    $("#hdnRequireRefreshPageCustomerSaleVolumeInsert").attr("value", "Y"); //거래처 판매등록-체적공급
    $("#hdnRequireRefreshPageCustomerSaleContainerDetail").attr("value", "Y"); //거래처 판매등록-용기판매
    $("#hdnRequireRefreshPageCustomerSaleEquipmentDetail").attr("value", "Y"); //거래처 판매등록-기구판매
    $("#hdnRequireRefreshPageCustomerReadMeterInsert").attr("value", "Y"); //거래처 검침등록
    $("#hdnRequireRefreshPageCustomerCollect").attr("value", "Y"); //거래처 수금등록-일반수금
    $("#hdnRequireRefreshPageCustomerSaftyCheckList").attr("value", "Y"); //거래처 안전점검-점검이력
    $("#hdnRequireRefreshPageCustomerSaftyCheckInsert").attr("value", "Y"); //거래처 안전점검-소비설비 점검등록
    $("#hdnRequireRefreshPageCustomerSaftyCheckTankInsert").attr("value", "Y"); //거래처 안전점검-저장탱크 점검등록
}

//거래처 검색 화면에서 거래처를 하나 선택 했을 때
function choiceCustomer(areaCode, customerCode) {
    var previousPage = $("#hdnPreviousPage").attr("value");

    //이전 페이지가 CID 상세 페이지라면
    if (previousPage == "pageManageCidEdit") {
        $.mobile.changePage("#pageManageCidEdit", { changeHash: false });
        setCurrentPage("pageManageCidEdit");
        $("#hdnCustomerCodeManageCidEdit").attr("value", customerCode);
        var insertMode = $("#hdnInsertModeManageCidEdit").attr("value");
        $.ajax({
            url: gasmaxWebappPath + "manage_cid_customer_summary.jsp",
            data: "areaCode=" + areaCode
                + "&customerCode=" + customerCode
                + "&insertMode=" + insertMode
            ,
            type: "post",
            dataType: "html",
            timeout: 120000,
            error: function (result) {
                if (result.status == 200) {
                    var html = getResultMessage("검색된 자료가 없습니다.", false);
                    ;
                    $("#" + tagId).html(html).trigger("create");
                } else if (result.status == 0) {
                    console.log("서버 응답 지연 (timeout 또는 연결 실패)");
                }
            },
            success: function (html) {
                $("#customerSummaryManageCidEdit").html(html).trigger("create");
            }
        });
        return;
    }

    //거래처 선택이 변경되었으므로 모든 거래처별 업무 페이지의 Refresh 필요 여부를 모두 Y로 변경하여 각각 Refresh 할 수 있도록 함
    setAllCustomerBizRequireRefresh();

    //현재 선택된 거래처 코드와 영업소 코드 설정
    $("#hdnCurrentCustomerAreaCode").attr("value", areaCode);
    $("#hdnCurrentCustomerCode").attr("value", customerCode);

    //세션에 현재 거래처 코드 등록
    $.ajax({
        url: gasmaxWebappPath + "search_customer_choice.jsp",
        type: "post",
        data: "customerCode=" + customerCode + "&areaCode=" + areaCode,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else if (result.status == 200) {
                alert("해당 거래처를 조회할 수 없습니다.");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (xml) {
            var customerType = $(xml).find("customerType").text();
            $("#hdnCurrentCustomerType").attr("value", customerType);
            showCustomerBiz();
        }
    });

    //세션에 기존 거래처별 정보 클리어
    $.ajax({
        url: gasmaxWebappPath + "reset_session_customer_ajx.jsp",
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
        }
    });
}

//CID 주문정보 페이지로 이동
function showPageManageCidList(refresh) {
    var menuPermissionCid = $("#hdnMenuPermissionCid").attr("value");
    if (menuPermissionCid == "0") {
    } else if (menuPermissionCid == "1") {
    } else if (menuPermissionCid == "2") {
        alert("권한이 없습니다.");
        return;
    }
    $.mobile.changePage("#pageManageCidList", { changeHash: false });
    setCurrentPage("pageManageCidList");
    if (refresh == undefined) {
        refresh = true;
    }
    if (refresh == true) {
        injectionSearchOptionManageCidList("searchOptionManageCidList"); //검색조건
        $("#divSearchResultManageCidList").html("");
        $("#btnMorePageManageCidList").html("");
    }
    // iOS Capacitor 환경에서 Safe Area 적용
}

//CID 주문정보 검색 버튼 처리
function searchManageCidList(defaultAreaAddress, phoneAreaNumber) {
    $("#btnMorePageManageCidList").html("").trigger("create");
    var html = $("#txtCidDateManageCidList").attr("value") + ", 담당자: " + $("#selectEmployeeManageCidList").attr("value") + " 기준 CID 주문 정보 조회결과";
    $("#divSearchResultManageCidList").html(html).trigger("create");

    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    //$("#divSearchResultManageCidList").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    var cidDate = $("#txtCidDateManageCidList").attr("value");
    var employeeCode = $("#selectEmployeeManageCidList").attr("value");
    var newDelivery = $("#ckbNewDeliveryManageCidList").is(":checked");
    var delivered = $("#ckbDeliveredManageCidList").is(":checked");
    var deliveryComplete = $("#ckbDeliveryCompleteManageCidList").is(":checked");
    $.ajax({
        url: gasmaxWebappPath + "manage_cid_list_search_ajx.jsp",
        type: "post",
        data: "cidDate=" + cidDate
            + "&employeeCode=" + employeeCode
            + "&newDelivery=" + newDelivery
            + "&delivered=" + delivered
            + "&deliveryComplete=" + deliveryComplete
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultManageCidList").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CidList").each(function () {
                rowCount++;
                var sequenceNumber = $(this).find("sequenceNumber").text();
                var autoRegisterType = $(this).find("autoRegisterType").text();
                var sequenceNumberColor = "red";
                if (autoRegisterType == "0") {
                    sequenceNumberColor = "#666666";
                }
                var cidTime = $(this).find("cidTime").text();
                var hour = cidTime.substr(0, 2);
                if (parseInt(hour, 10) > 12) {
                    hour = "오후 " + (parseInt(hour, 10) - 12);
                } else if (parseInt(hour, 10) == 12) {
                    hour = "오후 " + hour;
                } else {
                    hour = "오전 " + hour;
                }
                cidTime = hour + ":" + cidTime.substr(2, 2);
                var saleTypeName = $(this).find("saleTypeName").text(); //판매구분명 배달, 수금, A/S, 점검, 시설, 기타
                var phoneNumber = $(this).find("phoneNumber").text();
                phoneNumber = phoneNumber.replace(phoneAreaNumber + "-", "");
                //				if (phoneNumber.substring(0, 1) != "0"){
                //					phoneNumber = phoneAreaNumber + "-" + phoneNumber;
                //				}
                var customerType = $(this).find("customerType").text();
                var customerCode = $(this).find("customerCode").text();
                var customerName = $(this).find("customerName").text();
                var customerNameColor = "red";
                var address = $(this).find("address").text();
                address = address.replace(defaultAreaAddress, "");
                var deliveryYesNo = $(this).find("deliveryYesNo").text(); // 0, 1
                var deliveryChecked = "";
                if (deliveryYesNo == "1") {
                    customerNameColor = "blue";
                    deliveryChecked = "checked";
                }
                var completeYesNo = $(this).find("completeYesNo").text(); // 0, 1
                var completeChecked = "";
                if (completeYesNo == "1") {
                    customerNameColor = "black";
                    completeChecked = "checked";
                }
                var employeeReadOnly = "";
                if (completeYesNo == "1") {
                    employeeReadOnly = ' disabled="disabled" ';
                }
                var employeeCode = $(this).find("employeeCode").text();
                var itemCode = $(this).find("itemCode").text();
                var remark = $(this).find("remark").text();
                var addressHtml = "";
                if ((address == "") && (remark == "")) {
                    addressHtml = "";
                } else if (address == "") {
                    addressHtml = '</span><span id="spnRemarkManageCidList' + key + '" style="font-size:14px ; color: blue ; " >☞' + remark;
                } else if (remark == "") {
                    addressHtml = address;
                } else {
                    addressHtml = address + '</span><br /><span id="spnRemarkManageCidList' + key + '" style="font-size:14px ; color: blue ; " >☞' + remark;
                }

                var key = $(this).find("key").text();
                var keyStr = "'" + key + "'";

                // 사원 목록 select option 항목 html 생성
                var selectEmployeeHtml = "";
                var employeeCount = $("#selectEmployeeManageCidList option").size();
                for (var i = 1; i < employeeCount; i++) {
                    var value = $("#selectEmployeeManageCidList option:eq(" + i + ")").val();
                    var text = $("#selectEmployeeManageCidList option:eq(" + i + ")").text();
                    if (text == "미지정") {
                        text = "";
                    }
                    var selected = "";
                    if (value == employeeCode) {
                        selected = "selected";
                    }
                    selectEmployeeHtml += '<option value="' + value + '" ' + selected + '>' + text + '</option>';
                }

                html += '	<table style="border: 0px solid #999999 ; border-bottom: 1px solid #222222 ; border-collapse: collapse ; width: 100% ; table-layout: fixed">'
                    + '		<tr>'
                    + '			<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '				<a href="#" id="' + key + '" onclick="showPageManageCidEdit(' + keyStr + ', true)"  id2="' + customerCode + '" id3="' + customerType + '">'
                    + '					<table style="border: 0px solid #999999 ; border-top: 0px solid #999999 ;  border-bottom: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed">'
                    + '						<tr>'
                    + '							<td style="text-align: left ; border-left: 0px solid #999999 ; border-right: 0px solid #999999 ; border-top: 0px solid #999999 ;  border-bottom: 0px solid #999999 ; "><span style="font-size:14px ; color: ' + sequenceNumberColor + '; "> ' + sequenceNumber + '</span><span style="font-size:14px ; color: #666666 ; "> ' + saleTypeName + ' [' + cidTime + '] </span><span id="spnPhoneNumberManageCidList' + key + '" style="font-size:14px ; " >' + phoneNumber + ' </span></td>'
                    + '						</tr>'
                    + '						<tr>'
                    + '							<td style="text-align: left ; border-left: 0px solid #999999 ; border-right: 0px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 0px solid #999999 ; "><span id="spnCustomerNameManageCidList' + key + '"  style="font-size:16px ; color: ' + customerNameColor + ' ; " >' + customerName + '&nbsp;</span></td>'
                    + '						</tr>'
                    + '						<tr>'
                    + '							<td style="text-align: left ; border-left: 0px solid #999999 ; border-right: 0px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 0px solid #222222 ; "><span id="spnAddressManageCidList' + key + '" style="font-size:14px ; color: black ; " >' + addressHtml + ' &nbsp;</span></td>'
                    + '						</tr>'
                    + '					</table>'
                    + '				</a>'
                    + '			</td>'
                    + '			<td style="width: 100px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '				<input type="hidden" id="hdnItemCodeManageCidList' + key + '" value="' + itemCode + '" />'
                    + '				<fieldset data-role="controlgroup" data-mini="true"><input type="checkbox" id="ckbDeliveryManageCidList' + key + '" ' + deliveryChecked + ' onclick="clickSaveManageCidList(' + keyStr + ')" /><label for="ckbDeliveryManageCidList' + key + '">배달</label><input type="checkbox" id="ckbCompleteManageCidList' + key + '" ' + completeChecked + ' onclick="clickSaveManageCidList(' + keyStr + ')" /><label for="ckbCompleteManageCidList' + key + '">완료</label></fieldset>'
                    + '				<select id="selectEmployeeManageCidList' + key + '"  data-mini="true" data-inset="false" style="font-size: 14px ; "' + employeeReadOnly + ' onchange="clickSaveManageCidList(' + keyStr + ')">'
                    + '					' + selectEmployeeHtml
                    + '				</select>'
                    + '			</td>'
                    + '</table>';
            });
            $("#divSearchResultManageCidList").html(html).trigger("create");
            $("#divSearchResultManageCidList a").taphold(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 꾹 누르고 있을 때 거래처별 업무 메뉴 출력
                //거래처별 업무 메뉴를 선택한 페이지 정보를 거래처 검색 페이지로 설정
                $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageManageCidList");
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id2"), getParentSpecifiedTagId($(event.target), "a", "id3"));
            });
            $('#hdnNextPageNumberManageCidList').attr("value", '2'); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                var param = "'" + defaultAreaAddress + "', '" + phoneAreaNumber + "'";
                $("#btnMorePageManageCidList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageManageCidList(' + param + ')">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageManageCidList").html("").trigger("create");
            }
        }
    });
}

//CID 검색 페이지 추가 처리하기
function clickMorePageManageCidList(defaultAreaAddress, phoneAreaNumber) {
    $.ajax({
        url: gasmaxWebappPath + "manage_cid_list_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberManageCidList").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultManageCidList").append(html).trigger("create");
                $("#btnMorePageManageCidList").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var rowCount = 0;
            $(xml).find("CidList").each(function () {
                rowCount++;
                var sequenceNumber = $(this).find("sequenceNumber").text();
                var autoRegisterType = $(this).find("autoRegisterType").text();
                var sequenceNumberColor = "red";
                if (autoRegisterType == "0") {
                    sequenceNumberColor = "#666666";
                }
                var cidTime = $(this).find("cidTime").text();
                var hour = cidTime.substr(0, 2);
                if (parseInt(hour, 10) > 12) {
                    hour = "오후 " + (parseInt(hour, 10) - 12);
                } else if (parseInt(hour, 10) == 12) {
                    hour = "오후 " + hour;
                } else {
                    hour = "오전 " + hour;
                }
                cidTime = hour + ":" + cidTime.substr(2, 2);
                var saleTypeName = $(this).find("saleTypeName").text(); //판매구분명 배달, 수금, A/S, 점검, 시설, 기타
                var phoneNumber = $(this).find("phoneNumber").text();
                phoneNumber = phoneNumber.replace(phoneAreaNumber + "-", "");
                //				if (phoneNumber.substring(0, 1) != "0"){
                //					phoneNumber = phoneAreaNumber + "-" + phoneNumber;
                //				}
                var customerType = $(this).find("customerType").text();
                var customerCode = $(this).find("customerCode").text();
                var customerName = $(this).find("customerName").text();
                var customerNameColor = "red";
                var address = $(this).find("address").text();
                address = address.replace(defaultAreaAddress, "");
                var deliveryYesNo = $(this).find("deliveryYesNo").text(); // 0, 1
                var deliveryChecked = "";
                if (deliveryYesNo == "1") {
                    customerNameColor = "blue";
                    deliveryChecked = "checked";
                }
                var completeYesNo = $(this).find("completeYesNo").text(); // 0, 1
                var completeChecked = "";
                if (completeYesNo == "1") {
                    customerNameColor = "black";
                    completeChecked = "checked";
                }
                var employeeReadOnly = "";
                if (completeYesNo == "1") {
                    employeeReadOnly = " disabled ";
                }
                var employeeCode = $(this).find("employeeCode").text();
                var itemCode = $(this).find("itemCode").text();
                var remark = $(this).find("remark").text();
                var addressHtml = "";
                if ((address == "") && (remark == "")) {
                    addressHtml = "";
                } else if (address == "") {
                    addressHtml = '</span><span id="spnRemarkManageCidList' + key + '" style="font-size:14px ; color: blue ; " >☞' + remark;
                } else if (remark == "") {
                    addressHtml = address;
                } else {
                    addressHtml = address + '</span><span id="spnRemarkManageCidList' + key + '" style="font-size:14px ; color: blue ; " >☞' + remark;
                }

                var key = $(this).find("key").text();
                var keyStr = "'" + key + "'";

                // 사원 목록 select option 항목 html 생성
                var selectEmployeeHtml = "";
                var employeeCount = $("#selectEmployeeManageCidList option").size();
                for (var i = 1; i < employeeCount; i++) {
                    var value = $("#selectEmployeeManageCidList option:eq(" + i + ")").val();
                    var text = $("#selectEmployeeManageCidList option:eq(" + i + ")").text();
                    if (text == "미지정") {
                        text = "";
                    }
                    var selected = "";
                    if (value == employeeCode) {
                        selected = "selected";
                    }
                    selectEmployeeHtml += '<option value="' + value + '" ' + selected + '>' + text + '</option>';
                }

                var html = '	<table style="border: 0px solid #999999 ; border-bottom: 1px solid #222222 ; border-collapse: collapse ; width: 100% ; table-layout: fixed">'
                    + '		<tr>'
                    + '			<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '				<a href="#" id="' + key + '" onclick="showPageManageCidEdit(' + keyStr + ', true)"  id2="' + customerCode + '" id3="' + customerType + '">'
                    + '					<table style="border: 0px solid #999999 ; border-top: 0px solid #999999 ;  border-bottom: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed">'
                    + '						<tr>'
                    + '							<td style="text-align: left ; border-left: 0px solid #999999 ; border-right: 0px solid #999999 ; border-top: 0px solid #999999 ;  border-bottom: 0px solid #999999 ; "><span style="font-size:14px ; color: ' + sequenceNumberColor + '; "> ' + sequenceNumber + '</span><span style="font-size:14px ; color: #666666 ; "> ' + saleTypeName + ' [' + cidTime + '] </span><span id="spnPhoneNumberManageCidList' + key + '" style="font-size:14px ; " >' + phoneNumber + ' </span></td>'
                    + '						</tr>'
                    + '						<tr>'
                    + '							<td style="text-align: left ; border-left: 0px solid #999999 ; border-right: 0px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 0px solid #999999 ; "><span id="spnCustomerNameManageCidList' + key + '"  style="font-size:16px ; color: ' + customerNameColor + ' ; " >' + customerName + '&nbsp;</span></td>'
                    + '						</tr>'
                    + '						<tr>'
                    + '							<td style="text-align: left ; border-left: 0px solid #999999 ; border-right: 0px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 0px solid #222222 ; "><span id="spnAddressManageCidList' + key + '" style="font-size:14px ; color: black ; " >' + addressHtml + ' &nbsp;</span></td>'
                    + '						</tr>'
                    + '					</table>'
                    + '				</a>'
                    + '			</td>'
                    + '			<td style="width: 100px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '				<input type="hidden" id="hdnItemCodeManageCidList' + key + '" value="' + itemCode + '" />'
                    + '				<fieldset data-role="controlgroup" data-mini="true"><input type="checkbox" id="ckbDeliveryManageCidList' + key + '" ' + deliveryChecked + ' onclick="clickSaveManageCidList(' + keyStr + ')" /><label for="ckbDeliveryManageCidList' + key + '">배달</label><input type="checkbox" id="ckbCompleteManageCidList' + key + '" ' + completeChecked + ' onclick="clickSaveManageCidList(' + keyStr + ')" /><label for="ckbCompleteManageCidList' + key + '">완료</label></fieldset>'
                    + '				<select id="selectEmployeeManageCidList' + key + '"  data-mini="true" data-inset="false" style="font-size: 14px ; "' + employeeReadOnly + ' onchange="clickSaveManageCidList(' + keyStr + ')">'
                    + '					' + selectEmployeeHtml
                    + '				</select>'
                    + '			</td>'
                    + '</table>';
                $("#divSearchResultManageCidList").append(html).trigger("create");
                $("#divSearchResultManageCidList a").unbind("taphold"); //모든 taphold 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
                $("#divSearchResultManageCidList a").taphold(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 꾹 누르고 있을 때 거래처별 업무 메뉴 출력
                    //거래처별 업무 메뉴를 선택한 페이지 정보를 거래처 검색 페이지로 설정
                    $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageManageCidList");
                    //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                    showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id2"), getParentSpecifiedTagId($(event.target), "a", "id3"));
                });
            });
            var nextPageNumber = parseInt($('#hdnNextPageNumberManageCidList').attr("value"), 10) + 1;
            $('#hdnNextPageNumberManageCidList').attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                var param = "'" + defaultAreaAddress + "', '" + phoneAreaNumber + "'";
                $("#btnMorePageManageCidList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageManageCidList(' + param + ')">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageManageCidList").html("").trigger("create");
            }
        }
    });
}

//CID 상세내역 페이지로 이동
function showPageManageCidEdit(key, refresh) {
    var menuPermissionCid = $("#hdnMenuPermissionCid").attr("value");
    if (menuPermissionCid == "0") {
    } else if (menuPermissionCid == "1") {
        if ((key == undefined) || (key == "")) { //신규추가 불가
            alert("권한이 없습니다.");
            return;
        }
    } else if (menuPermissionCid == "2") {
        if ((key == undefined) || (key == "")) { //신규추가 불가
            alert("권한이 없습니다.");
            return;
        }
    }
    $.mobile.changePage("#pageManageCidEdit", { changeHash: false });
    setCurrentPage("pageManageCidEdit");
    if (refresh == undefined) {
        refresh = true;
    }
    if (refresh == true) {
        injectionManageCidEdit("searchOptionManageCidEdit", key); //상세화면
    }
}

//CID 품목 검색 페이지로 이동
function showPageManageCidEditItemSearch() {
    $.mobile.changePage("#pageManageCidEditItemSearch", { changeHash: false });
    setCurrentPage("pageManageCidEditItemSearch");
    injectionManageCidEditItemSearch("searchResultManageCidEditItemSearch"); //조회화면
}

//CID 비고 검색 페이지로 이동
function showPageManageCidEditRemarkSearch() {
    $.mobile.changePage("#pageManageCidEditRemarkSearch", { changeHash: false });
    setCurrentPage("pageManageCidEditRemarkSearch");
    injectionManageCidEditRemarkSearch("searchResultManageCidEditRemarkSearch"); //조회화면
}

//판매현황 페이지로 이동
function showPageManageSaleList(refresh) {
    var menuPermissionSale = $("#hdnMenuPermissionSale").attr("value");
    if (menuPermissionSale == "0") {
    } else if (menuPermissionSale == "1") {
    } else if (menuPermissionSale == "2") {
        alert("권한이 없습니다.");
        return;
    }

    if (refresh == undefined) {
        refresh = true;
    }
    if (refresh == false) {
        $.mobile.changePage("#pageManageSaleList", { changeHash: false });
        return;
    }

    $.mobile.changePage("#pageManageSaleList", { changeHash: false });
    setCurrentPage("pageManageSaleList");
    var html = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
        + '	<tr><td style="text-align: right ; font-size: 16px ; "><span> 합계: ( 0 건) 입금액: 0 원<br />외상(미수): 0 원 DC: 0 원</span></td></tr>'
        + '</table>';
    $("#footerManageSaleList").html(html).trigger("create");

    injectionSearchOptionManageSaleList("searchOptionManageSaleList"); //검색조건
    $("#divSearchResultManageSaleList").html("").trigger("create");
    $("#btnMorePageManageSaleList").html("").trigger("create");
}

//판매현황 검색 버튼 처리
function searchManageSaleList() {
    $("#btnMorePageManageSaleList").html("").trigger("create");
    //$("#divSearchResultManageSaleList").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    var searchOption = $('input:radio[name="radioSearchOptionManageSaleList"]:checked').val();
    var employeeCode = $("#selectEmployeeManageSaleList").attr("value");
    var collectTypeCode = $("#selectPayTypeManageSaleList").attr("value");
    var keyword = $("#txtKeywordManageSaleList").attr("value");
    var startDate = $("#txtStartDateManageSaleList").attr("value");
    var endDate = $("#txtEndDateManageSaleList").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "manage_sale_list_search_ajx.jsp",
        type: "post",
        data: "keyword=" + keyword
            + "&searchOption=" + searchOption
            + "&employeeCode=" + employeeCode
            + "&collectTypeCode=" + collectTypeCode
            + "&startDate=" + startDate
            + "&endDate=" + endDate
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultManageSaleList").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var totalRowCount = $(xml).find("totalRowCount").text();
            var totalCollectAmount = $(xml).find("totalCollectAmount").text();
            var totalUnpaidAmount = $(xml).find("totalUnpaidAmount").text();
            var totalDiscountAmount = $(xml).find("totalDiscountAmount").text();
            var rowCount = 0;
            $(xml).find("SaleList").each(function () {
                rowCount++;
                var saleTypeName = $(this).find("saleType").text();
                var saleType = "1";
                if (saleTypeName == "일반") {
                    saleType = "0";
                }
                var saleTypeHtml = getSaleTypeHtml(saleTypeName, false);
                var saleDate = $(this).find("saleDate").text();
                var saleDateMMdd = saleDate.substr(4, 2) + "-" + saleDate.substr(6, 2);
                var customerCode = $(this).find("customerCode").text();
                var customerName = $(this).find("customerName").text();
                var saleAmount = $(this).find("saleAmount").text(); //합계금액
                var unpaidAmount = $(this).find("unpaidAmount").text(); //외상액(미수액)
                var saleAmountStyle = "red";
                if (unpaidAmount == "0") {
                    saleAmountStyle = "#222222";
                }
                var saleQuantity = $(this).find("saleQuantity").text(); //납품
                var withdrawQuantity = $(this).find("withdrawQuantity").text(); //회수
                //				var collectType = $(this).find("collectType").text();
                //				var collectTypeHtml = getPayTypeHtml(collectType, false);
                var itemName = $(this).find("itemName").text();
                var remark = $(this).find("remark").text();
                var employeeName = $(this).find("employeeName").text();
                html += '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + customerCode + '" id2="' + saleType + '">'
                    + '		<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; ">'
                    + '			<tr>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="font-size:14px ; ">' + saleDateMMdd + '</span></td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">' + saleTypeHtml + '<span style="font-size:14px ; "> ' + customerName + ' ' + remark + ' </span></td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ;">' + employeeName + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '		<table style="border: 0px solid #999999 ; border-bottom: 1px solid #222222 ; border-collapse: collapse ; width: 100% ; ">'
                    + '			<tr>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + itemName + ' </span></td>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + saleQuantity + '/' + withdrawQuantity + ' </span></td>'
                    //						+ '				<td style="width: 120px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color: ' + saleAmountStyle + ' ; font-size:14px ;"> ' + insertComma(saleAmount) + '원 </span>' + collectTypeHtml + '</td>'
                    + '				<td style="width: 120px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color: ' + saleAmountStyle + ' ; font-size:14px ;"> ' + insertComma(saleAmount) + '</span>' + '</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
            });
            $("#divSearchResultManageSaleList").html(html).trigger("create");
            $("#divSearchResultManageSaleList a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 거래처별 업무 메뉴 출력
                //거래처별 업무 메뉴를 선택한 페이지 정보를 판매 현황 페이지로 설정
                $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageManageSaleList");
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"));
            });
            var footerHtml = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
                + '<tr><td style="text-align: right ; font-size: 16px ; "><span> 합계: (' + insertComma(totalRowCount) + " 건) 입금액: " + insertComma(totalCollectAmount) + " 원<br/>외상(미수): " + insertComma(totalUnpaidAmount) + " 원 DC: " + insertComma(totalDiscountAmount) + " 원</span></td></tr>"
                + '</table>';
            $("#footerManageSaleList").html(footerHtml).trigger("create");
            $('#hdnNextPageNumberManageSaleList').attr("value", '2'); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageManageSaleList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageManageSaleList()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageManageSaleList").html("").trigger("create");
            }
        }
    });
}

//판매현황 검색 페이지 추가 처리하기
function clickMorePageManageSaleList() {
    $.ajax({
        url: gasmaxWebappPath + "manage_sale_list_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberManageSaleList").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultManageSaleList").append(html).trigger("create");
                $("#btnMorePageManageSaleList").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var rowCount = 0;
            $(xml).find("SaleList").each(function () {
                rowCount++;
                var saleTypeName = $(this).find("saleType").text();
                var saleType = "1";
                if (saleTypeName == "일반") {
                    saleType = "0";
                }
                var saleTypeHtml = getSaleTypeHtml(saleTypeName, false);
                var saleDate = $(this).find("saleDate").text();
                var saleDateMMdd = saleDate.substr(4, 2) + "-" + saleDate.substr(6, 2);
                var customerCode = $(this).find("customerCode").text();
                var customerName = $(this).find("customerName").text();
                var saleAmount = $(this).find("saleAmount").text(); //합계금액
                var unpaidAmount = $(this).find("unpaidAmount").text(); //외상액(미수액)
                var saleAmountStyle = "red";
                if (unpaidAmount == "0") {
                    saleAmountStyle = "#222222";
                }
                var saleQuantity = $(this).find("saleQuantity").text(); //납품
                var withdrawQuantity = $(this).find("withdrawQuantity").text(); //회수
                //				var collectType = $(this).find("collectType").text();
                //				var collectTypeHtml = getPayTypeHtml(collectType, false);
                var itemName = $(this).find("itemName").text();
                var remark = $(this).find("remark").text();
                var employeeName = $(this).find("employeeName").text();
                var html = '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + customerCode + '" id2="' + saleType + '">'
                    + '		<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; ">'
                    + '			<tr>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="font-size:14px ; ">' + saleDateMMdd + '</span></td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">' + saleTypeHtml + '<span style="font-size:14px ; "> ' + customerName + ' ' + remark + ' </span></td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ;">' + employeeName + '</span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '		<table style="border: 0px solid #999999 ; border-bottom: 1px solid #222222 ; border-collapse: collapse ; width: 100% ; ">'
                    + '			<tr>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + itemName + ' </span></td>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + saleQuantity + '/' + withdrawQuantity + ' </span></td>'
                    //							+ '				<td style="width: 120px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color: ' + saleAmountStyle + ' ; font-size:14px ;"> ' + insertComma(saleAmount) + '원 </span>' + collectTypeHtml + '</td>'
                    + '				<td style="width: 120px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color: ' + saleAmountStyle + ' ; font-size:14px ;"> ' + insertComma(saleAmount) + '</span>' + '</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
                $("#divSearchResultManageSaleList").append(html).trigger("create");
            });

            $("#divSearchResultManageSaleList a").unbind("click"); //모든 클릭 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
            $("#divSearchResultManageSaleList a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 거래처별 업무 메뉴 출력
                //거래처별 업무 메뉴를 선택한 페이지 정보를 판매 현황 페이지로 설정
                $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageManageSaleList");
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"));
            });
            var nextPageNumber = parseInt($('#hdnNextPageNumberManageSaleList').attr("value"), 10) + 1;
            $('#hdnNextPageNumberManageSaleList').attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageManageSaleList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageManageSaleList()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageManageSaleList").html("").trigger("create");
            }
        }
    });
}

//미수현황 페이지로 이동
function showPageManageUnpaidList(refresh) {
    var menuPermissionUnpaid = $("#hdnMenuPermissionUnpaid").attr("value");
    if (menuPermissionUnpaid == "0") {
    } else if (menuPermissionUnpaid == "1") {
    } else if (menuPermissionUnpaid == "2") {
        alert("권한이 없습니다.");
        return;
    }

    if (refresh == undefined) {
        refresh = true;
    }
    if (refresh == false) {
        $.mobile.changePage("#pageManageUnpaidList", { changeHash: false });
        return;
    }

    $.mobile.changePage("#pageManageUnpaidList", { changeHash: false });
    setCurrentPage("pageManageUnpaidList");
    var html = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
        + '	<tr><td style="text-align: right ; font-size: 16px ; "><span> 합계: ( 0 건) 0 원</span></td></tr>'
        + '</table>';
    $("#footerManageUnpaidList").html(html).trigger("create");
    injectionSearchOptionManageUnpaidList("searchOptionManageUnpaidList"); //검색조건
    $("#divSearchResultManageUnpaidList").html("").trigger("create");
    $("#btnMorePageManageUnpaidList").html("").trigger("create");
}

//미수현황 검색 버튼 처리
function searchManageUnpaidList() {
    $("#btnMorePageManageUnpaidList").html("").trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    //hideActivityIndicator()
    //$("#divSearchResultManageUnpaidList").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    var searchOption = $('input:radio[name="radioSearchOptionManageUnpaidList"]:checked').val();
    var employeeCode = $("#selectEmployeeManageUnpaid").attr("value");
    var collectTypeCode = $("#selectCollectTypeManageUnpaid").attr("value");
    var keyword = $("#txtKeywordManageUnpaidList").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "manage_unpaid_list_search_ajx.jsp",
        type: "post",
        data: "keyword=" + keyword
            + "&searchOption=" + searchOption
            + "&employeeCode=" + employeeCode
            + "&collectTypeCode=" + collectTypeCode,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultManageUnpaidList").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var totalRowCount = $(xml).find("totalRowCount").text();
            var totalUnpaid = $(xml).find("totalUnpaidAmount").text();
            var rowCount = 0;
            $(xml).find("UnpaidList").each(function () {
                rowCount++;
                var unpaidTypeName = $(this).find("unpaidTypeName").text();
                var unpaidType = "1";
                if (unpaidTypeName == "일반") {
                    unpaidType = "0";
                }
                var unpaidTypeHtml = getUnpaidTypeHtml(unpaidTypeName, false);
                var customerCode = $(this).find("customerCode").text();
                var customerName = $(this).find("customerName").text();
                var weightUnpaid = $(this).find("weightUnpaid").text();
                var volumeUnpaid = $(this).find("volumeUnpaid").text();
                //				var collectTypeCode = $(this).find("collectTypeCode").text();
                //				var collectTypeHtml = getCollectTypeHtml(collectTypeCode, false);
                var unpaidAmount = parseInt(weightUnpaid, 10) + parseInt(volumeUnpaid, 10);
                var unpaid = insertComma(unpaidAmount);
                var address1 = $(this).find("address1").text();
                var address2 = $(this).find("address2").text();
                var employeeName = $(this).find("employeeName").text();
                html += '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + customerCode + '" id2="' + unpaidType + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; "">'
                    + '			<tr>'
                    + '				<td style="border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					' + unpaidTypeHtml
                    + '					<span style="font-size:16px ;">' + customerName + '</span>'
                    + '				</td>'
                    + '				<td style="width: 80px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    //					+ '					' + collectTypeHtml
                    + '					<span style="color: red ; font-size:14px ;">' + unpaid + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '			<tr>'
                    + '				<td style="border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					<span style="color:#222222 ; font-size:14px ;">' + address1 + ' ' + address2 + '&nbsp;</span>'
                    + '				</td>'
                    + '				<td style="width: 90px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    //					+ '					<img src="images/lbl_employee.png" />'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + employeeName + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
            });
            $("#divSearchResultManageUnpaidList").html(html).trigger("create");
            $("#divSearchResultManageUnpaidList a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 거래처별 업무 메뉴 출력
                //거래처별 업무 메뉴를 선택한 페이지 정보를 미수 현황 페이지로 설정
                $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageManageUnpaidList");
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"));
            });
            var footerHtml = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
                + '<tr><td style="text-align: right ; font-size: 16px ; "><span> 합계: (' + insertComma(totalRowCount) + " 건) " + insertComma(totalUnpaid) + " 원</span></td></tr>"
                + '</table>';
            $("#footerManageUnpaidList").html(footerHtml).trigger("create");
            $("#hdnNextPageNumberManageUnpaidList").attr("value", "2"); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageManageUnpaidList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageManageUnpaidList()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageManageUnpaidList").html("").trigger("create");
            }
        }
    });
}

//미수현황 검색 페이지 추가 처리하기
function clickMorePageManageUnpaidList() {
    $.ajax({
        url: gasmaxWebappPath + "manage_unpaid_list_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberManageUnpaidList").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultManageUnpaidList").append(html).trigger("create");
                $("#btnMorePageManageUnpaidList").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var rowCount = 0;
            $(xml).find("UnpaidList").each(function () {
                rowCount++;
                var unpaidTypeName = $(this).find("unpaidTypeName").text();
                var unpaidType = "1";
                if (unpaidTypeName == "일반") {
                    unpaidType = "0";
                }
                var unpaidTypeHtml = getUnpaidTypeHtml(unpaidTypeName, false);
                var customerCode = $(this).find("customerCode").text();
                var customerName = $(this).find("customerName").text();
                var weightUnpaid = $(this).find("weightUnpaid").text();
                var volumeUnpaid = $(this).find("volumeUnpaid").text();
                //				var collectTypeCode = $(this).find("collectTypeCode").text();
                //				var collectTypeHtml = getCollectTypeHtml(collectTypeCode, false);
                var unpaidAmount = parseInt(weightUnpaid, 10) + parseInt(volumeUnpaid, 10);
                var unpaid = insertComma(unpaidAmount);
                var address1 = $(this).find("address1").text();
                var address2 = $(this).find("address2").text();
                var employeeName = $(this).find("employeeName").text();
                var html = '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + customerCode + '" id2="' + unpaidType + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; "">'
                    + '			<tr>'
                    + '				<td style="border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					' + unpaidTypeHtml
                    + '					<span style="font-size:16px ;">' + customerName + '</span>'
                    + '				</td>'
                    + '				<td style="width: 80px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    //				+ '					' + collectTypeHtml
                    + '					<span style="color: red ; font-size:14px ;">' + unpaid + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '			<tr>'
                    + '				<td style="border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					<span style="color:#222222 ; font-size:14px ;">' + address1 + ' ' + address2 + '&nbsp;</span>'
                    + '				</td>'
                    + '				<td style="width: 90px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    //				+ '					<img src="images/lbl_employee.png" />'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + employeeName + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
                $("#divSearchResultManageUnpaidList").append(html).trigger("create");
            });

            $("#divSearchResultManageUnpaidList a").unbind("click"); //모든 클릭 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
            $("#divSearchResultManageUnpaidList a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 거래처별 업무 메뉴 출력
                //거래처별 업무 메뉴를 선택한 페이지 정보를 미수 현황 페이지로 설정
                $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageManageUnpaidList");
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"));
            });
            var nextPageNumber = parseInt($("#hdnNextPageNumberManageUnpaidList").attr("value"), 10) + 1;
            $("#hdnNextPageNumberManageUnpaidList").attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageManageUnpaidList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageManageUnpaidList()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageManageUnpaidList").html("").trigger("create");
            }
        }
    });
}

//수금현황 페이지로 이동
function showPageManageCollectList(refresh) {
    var menuPermissionCollect = $("#hdnMenuPermissionCollect").attr("value");
    if (menuPermissionCollect == "0") {
    } else if (menuPermissionCollect == "1") {
    } else if (menuPermissionCollect == "2") {
        alert("권한이 없습니다.");
        return;
    }

    if (refresh == undefined) {
        refresh = true;
    }
    if (refresh == false) {
        $.mobile.changePage("#pageManageCollectList", { changeHash: false });
        return;
    }

    $.mobile.changePage("#pageManageCollectList", { changeHash: false });
    setCurrentPage("pageManageCollectList");
    var html = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
        + '	<tr><td style="text-align: right ; font-size: 16px ; "><span> 합계: ( 0 건) 입금액: 0 원 DC: 0 원</span></td></tr>'
        + '</table>';
    $("#footerManageCollectList").html(html).trigger("create");
    injectionSearchOptionManageCollectList("searchOptionManageCollectList"); //검색조건
    $("#divSearchResultManageCollectList").html("").trigger("create");
    $("#btnMorePageManageCollectList").html("").trigger("create");
}

//수금현황 검색 버튼 처리
function searchManageCollectList() {
    $("#btnMorePageManageCollectList").html("").trigger("create");
    //$("#divSearchResultManageCollectList").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    var collectClass = $('input:radio[name="radioSearchOptionManageCollectList"]:checked').val();
    var employeeCode = $("#selectEmployeeManageCollectList").attr("value");
    var collectTypeCode = $("#selectCollectTypeManageCollectList").attr("value");
    var keyword = $("#txtKeywordManageCollectList").attr("value");
    var startDate = $("#txtStartDateManageCollectList").attr("value");
    var endDate = $("#txtEndDateManageCollectList").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "manage_collect_list_search_ajx.jsp",
        type: "post",
        data: "keyword=" + keyword
            + "&collectClass=" + collectClass
            + "&employeeCode=" + employeeCode
            + "&startDate=" + startDate
            + "&endDate=" + endDate
            + "&collectTypeCode=" + collectTypeCode,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultManageCollectList").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var totalRowCount = $(xml).find("totalRowCount").text();
            var totalCollect = $(xml).find("totalCollectAmount").text();
            var totalDiscount = $(xml).find("totalDiscountAmount").text();
            var rowCount = 0;
            $(xml).find("CollectList").each(function () {
                rowCount++;
                var collectClassName = $(this).find("collectClass").text();
                var collectType = "1";
                if (collectClassName == "일반") {
                    collectType = "0";
                }
                var collectClassHtml = getSaleTypeHtml(collectClassName, false);
                var collectDate = $(this).find("collectDate").text();
                var collectDateMMdd = collectDate.substr(2, 2) + "-" + collectDate.substr(4, 2) + "-" + collectDate.substr(6, 2);
                var customerCode = $(this).find("customerCode").text();
                var customerName = $(this).find("customerName").text();
                var remark = $(this).find("remark").text();
                //				var collectTypeCode = $(this).find("collectTypeCode").text();
                //				var collectTypeHtml = getCollectTypeHtml(collectTypeCode, false);
                var collectTypeName = $(this).find("collectTypeName").text();
                collectTypeName = '<span style="font-size: 14px ; color: black ; " >[' + collectTypeName + ']</span>';
                var collectAmount = $(this).find("collectAmount").text();
                var collect = insertComma(collectAmount);
                var discountAmount = $(this).find("discountAmount").text();
                var discount = insertComma(discountAmount);
                var employeeName = $(this).find("employeeName").text();
                html += '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + customerCode + '" id2="' + collectType + '">'
                    + '		<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ;  border-top: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					<span style="font-size:14px ;">' + collectDateMMdd + '</span>'
                    + '				</td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					' + collectClassHtml
                    + '					<span style="font-size:16px ;">' + customerName + '</span>'
                    + '				</td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    //						+ '					<img src="images/lbl_employee.png" />'
                    + '					<span style="color:#3300FF ; font-size:14px ; ">' + employeeName + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '		<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ;  border-top: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 120px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					<span style="color: red ; font-size:14px ;">' + collect + '</span>'
                    //						+ '					<img src="images/lbl_discount.png" />'
                    + '				</td>'
                    + '				<td style="width: 80px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					<span style="color: red ; font-size:14px ;">' + discount + '</span>'
                    + '				</td>'
                    + '				<td style="width: 50px ; text-align: left ; border-left: 1px solid #999999 ; border-right: 0px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    //						+ '					' + collectTypeHtml
                    + '					' + collectTypeName
                    + '				</td>'
                    + '				<td style="text-align: left ; border-left: 0px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">'
                    //						+ '					<img src="images/lbl_remark.png" />'
                    + '					<span style="color: black ; font-size:14px ;">' + remark + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
            });
            $("#divSearchResultManageCollectList").html(html).trigger("create");
            $("#divSearchResultManageCollectList a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 거래처별 업무 메뉴 출력
                //거래처별 업무 메뉴를 선택한 페이지 정보를 수금 현황 페이지로 설정
                $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageManageCollectList");
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"));
            });
            var footerHtml = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
                + '<tr><td style="text-align: right ; font-size: 16px ; "><span> 합계: (' + insertComma(totalRowCount) + " 건) 입금액: " + insertComma(totalCollect) + " 원 DC: " + insertComma(totalDiscount) + " 원</span></td></tr>"
                + '</table>';
            $("#footerManageCollectList").html(footerHtml).trigger("create");
            $('#hdnNextPageNumberManageCollectList').attr("value", '2'); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageManageCollectList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageManageCollectList()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageManageCollectList").html("").trigger("create");
            }
        }
    });
}

//수금현황 검색 페이지 추가 처리하기
function clickMorePageManageCollectList() {
    $.ajax({
        url: gasmaxWebappPath + "manage_collect_list_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberManageCollectList").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultManageCollectList").append(html).trigger("create");
                $("#btnMorePageManageCollectList").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var rowCount = 0;
            $(xml).find("CollectList").each(function () {
                rowCount++;
                var collectClassName = $(this).find("collectClass").text();
                var collectType = "1";
                if (collectClassName == "일반") {
                    collectType = "0";
                }
                var collectClassHtml = getSaleTypeHtml(collectClassName, false);
                var collectDate = $(this).find("collectDate").text();
                var collectDateMMdd = collectDate.substr(2, 2) + "-" + collectDate.substr(4, 2) + "-" + collectDate.substr(6, 2);
                var customerCode = $(this).find("customerCode").text();
                var customerName = $(this).find("customerName").text();
                var remark = $(this).find("remark").text();
                var collectTypeCode = $(this).find("collectTypeCode").text();
                var collectTypeHtml = getCollectTypeHtml(collectTypeCode, false);
                var collectAmount = $(this).find("collectAmount").text();
                var collect = insertComma(collectAmount);
                var discountAmount = $(this).find("discountAmount").text();
                var discount = insertComma(discountAmount);
                var employeeName = $(this).find("employeeName").text();
                var html = '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + customerCode + '" id2="' + collectType + '">'
                    + '		<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ;  border-top: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					<span style="font-size:14px ;">' + collectDateMMdd + '</span>'
                    + '				</td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					' + collectClassHtml
                    + '					<span style="font-size:16px ;">' + customerName + '</span>'
                    + '				</td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    //				+ '					<img src="images/lbl_employee.png" />'
                    + '					<span style="color:#3300FF ; font-size:14px ; ">' + employeeName + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '		<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ;  border-top: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 120px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					<span style="color: red ; font-size:14px ;">' + collect + '</span>'
                    //				+ '					<img src="images/lbl_discount.png" />'
                    + '				</td>'
                    + '				<td style="width: 80px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					<span style="color: red ; font-size:14px ;">' + discount + '</span>'
                    + '				</td>'
                    + '				<td style="width: 50px ; text-align: left ; border-left: 1px solid #999999 ; border-right: 0px solid #999999 ; border-bottom: 0px solid #999999 ; ">'
                    + '					' + collectTypeHtml
                    + '				</td>'
                    + '				<td style="text-align: left ; border-left: 0px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">'
                    //				+ '					<img src="images/lbl_remark.png" />'
                    + '					<span style="color: black ; font-size:14px ;">' + remark + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
                $("#divSearchResultManageCollectList").append(html).trigger("create");
            });

            $("#divSearchResultManageCollectList a").unbind("click"); //모든 클릭 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
            $("#divSearchResultManageCollectList a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 거래처별 업무 메뉴 출력
                //거래처별 업무 메뉴를 선택한 페이지 정보를 수금 현황 페이지로 설정
                $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageManageCollectList");
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"));
            });
            var nextPageNumber = parseInt($("#hdnNextPageNumberManageCollectList").attr("value"), 10) + 1;
            $("#hdnNextPageNumberManageCollectList").attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageManageCollectList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageManageCollectList()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageManageCollectList").html("").trigger("create");
            }
        }
    });
}


function showInappOpenPortalPage() {
    var ref = cordova.InAppBrowser.open('http://apache.org', '_blank', 'location=yes');

}

function showGasmaxeye2020() {
    //val intent = packageManager.getLaunchIntentForPackage("com.joainfo.gasmaxeye2020")

}


//검침현황 페이지로 이동
function showPageManageReadMeterList(refresh) {
    var menuPermissionReadMeter = $("#hdnMenuPermissionReadMeter").attr("value");
    if (menuPermissionReadMeter == "0") {
    } else if (menuPermissionReadMeter == "1") {
    } else if (menuPermissionReadMeter == "2") {
        alert("권한이 없습니다.");
        return;
    }

    if (refresh == undefined) {
        refresh = true;
    }
    if (refresh == false) {
        $.mobile.changePage("#pageManageReadMeterList", { changeHash: false });
        return;
    }

    $.mobile.changePage("#pageManageReadMeterList", { changeHash: false });
    setCurrentPage("pageManageReadMeterList");
    var html = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
        + '	<tr><td style="text-align: right ; font-size: 16px ; "><span> 합계: ( 0 건) 당월금액: 0 원</span></td></tr>'
        + '</table>';
    $("#footerManageReadMeterList").html(html).trigger("create");
    injectionSearchOptionManageReadMeterList("searchOptionManageReadMeterList"); //검색조건
    $("#divSearchResultManageReadMeterList").html("").trigger("create");
    $("#btnMorePageManageReadMeterList").html("").trigger("create");
}

//검침현황 검색 버튼 처리
function searchManageReadMeterList() {
    $("#btnMorePageManageReadMeterList").html("").trigger("create");
    //$("#divSearchResultManageReadMeterList").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    var employeeCode = $("#selectEmployeeManageReadMeterList").attr("value");
    var collectTypeCode = $("#selectPayTypeManageReadMeterList").attr("value");
    var keyword = $("#txtKeywordManageReadMeterList").attr("value");
    var startDate = $("#txtStartDateManageReadMeterList").attr("value");
    var endDate = $("#txtEndDateManageReadMeterList").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "manage_read_meter_list_search_ajx.jsp",
        type: "post",
        data: "keyword=" + keyword
            + "&employeeCode=" + employeeCode
            + "&collectTypeCode=" + collectTypeCode
            + "&startDate=" + startDate
            + "&endDate=" + endDate
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultManageReadMeterList").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var totalRowCount = $(xml).find("totalRowCount").text();
            var totalNowAmount = $(xml).find("totalNowAmount").text();
            var rowCount = 0;
            $(xml).find("ReadMeterList").each(function () {
                rowCount++;
                var readMeterDate = $(this).find("readMeterDate").text();
                var readMeterDateMMdd = readMeterDate.substr(2, 2) + "-" + readMeterDate.substr(4, 2) + "-" + readMeterDate.substr(6, 2);
                var customerCode = $(this).find("customerCode").text();
                var customerName = $(this).find("customerName").text();
                var nowReadMeter = $(this).find("nowReadMeter").text(); //사용량
                var useQuantity = $(this).find("useQuantity").text(); //사용량
                var nowAmount = $(this).find("nowAmount").text(); //사용료
                var sumNowAmount = $(this).find("sumNowAmount").text(); //당월금액
                var otherAmount = $(this).find("otherAmount").text(); //기타금액
                var chargeAmount = $(this).find("chargeAmount").text(); //연체료
                //				var collectType = $(this).find("collectTypeCode").text();
                //				var collectTypeHtml = getCollectTypeHtml(collectType, false);
                var employeeName = $(this).find("employeeName").text();
                html += '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + customerCode + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 83px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">'
                    + '					<span style="font-size:14px ; ">' + readMeterDateMMdd + '</span>'
                    + '				</td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">'
                    + '					<span style="font-size:16px ;">' + customerName + ' </span>'
                    + '				</td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">'
                    //					+ '					<img src="images/lbl_employee.png" />'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + employeeName + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 40px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    //					+ '					<img src="images/lbl_this_read_meter.png" />'
                    //						+ '					<span style="color:black ; font-size:14px ;">당검</span>'
                    + '					<span style="color:#222222 ; font-size:14px ;"> ' + nowReadMeter + ' </span>'
                    + '				</td>'
                    + '				<td style="width: 40px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    //					+ '					<img src="images/lbl_this_use.png" />'
                    //						+ '					<span style="color:black ; font-size:14px ;">사용</span>'
                    + '					<span style="color:#222222 ; font-size:14px ;">' + insertComma(useQuantity) + ' </span>'
                    + '				</td>'
                    + '				<td style="width: 80px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + insertComma(nowAmount) + '</span>'
                    + '				</td>'
                    + '				<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    //					+ '					<img src="images/lbl_other.png" />'
                    //						+ '					<span style="color:black ; font-size:14px ;">기타</span>'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + insertComma(otherAmount) + '</span>'
                    + '				</td>'
                    + '				<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    //					+ '					<img src="images/lbl_delay.png" />'
                    //						+ '					<span style="color:black ; font-size:14px ;">연체</span>'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + insertComma(chargeAmount) + '</span>'
                    + '				</td>'
                    + '				<td style="text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    //						+ '					' + collectTypeHtml
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + insertComma(sumNowAmount) + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
            });
            $("#divSearchResultManageReadMeterList").html(html).trigger("create");
            $("#divSearchResultManageReadMeterList a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 거래처별 업무 메뉴 출력
                //거래처별 업무 메뉴를 선택한 페이지 정보를 검침 현황 페이지로 설정
                $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageManageReadMeterList");
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id"), "1"); //검침은 체적 장부로
            });
            var footerHtml = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
                + '<tr><td style="text-align: right ; font-size: 16px ; "><span> 합계: (' + insertComma(totalRowCount) + " 건) 당월금액: " + insertComma(totalNowAmount) + " 원</span></td></tr>"
                + '</table>';
            $("#footerManageReadMeterList").html(footerHtml).trigger("create");
            $("#hdnNextPageNumberManageReadMeterList").attr("value", "2"); //다음 페이지는 무조건 2가 되도록 세팅
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageManageReadMeterList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageManageReadMeterList()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageManageReadMeterList").html("").trigger("create");
            }
        }
    });
}

//검침현황 검색 페이지 추가 처리하기
function clickMorePageManageReadMeterList() {
    $.ajax({
        url: gasmaxWebappPath + "manage_read_meter_list_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberManageReadMeterList").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#divSearchResultManageReadMeterList").append(html).trigger("create");
                $("#btnMorePageManageReadMeterList").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var rowCount = 0;
            $(xml).find("ReadMeterList").each(function () {
                rowCount++;
                var readMeterDate = $(this).find("readMeterDate").text();
                var readMeterDateMMdd = readMeterDate.substr(2, 2) + "-" + readMeterDate.substr(4, 2) + "-" + readMeterDate.substr(6, 2);
                var customerCode = $(this).find("customerCode").text();
                var customerName = $(this).find("customerName").text();
                var nowReadMeter = $(this).find("nowReadMeter").text(); //사용량
                var useQuantity = $(this).find("useQuantity").text(); //사용량
                var nowAmount = $(this).find("nowAmount").text(); //사용료
                var sumNowAmount = $(this).find("sumNowAmount").text(); //당월금액
                var otherAmount = $(this).find("otherAmount").text(); //기타금액
                var chargeAmount = $(this).find("chargeAmount").text(); //연체료
                //				var collectType = $(this).find("collectTypeCode").text();
                //				var collectTypeHtml = getCollectTypeHtml(collectType, false);
                var employeeName = $(this).find("employeeName").text();
                var html = '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + customerCode + '">'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 83px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">'
                    + '					<span style="font-size:14px ; ">' + readMeterDateMMdd + '</span>'
                    + '				</td>'
                    + '				<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">'
                    + '					<span style="font-size:16px ;">' + customerName + ' </span>'
                    + '				</td>'
                    + '				<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">'
                    //					+ '					<img src="images/lbl_employee.png" />'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + employeeName + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '		<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr>'
                    + '				<td style="width: 40px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    //					+ '					<img src="images/lbl_this_read_meter.png" />'
                    //				+ '					<span style="color:black ; font-size:14px ;">당검</span>'
                    + '					<span style="color:#222222 ; font-size:14px ;"> ' + nowReadMeter + ' </span>'
                    + '				</td>'
                    + '				<td style="width: 40px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    //					+ '					<img src="images/lbl_this_use.png" />'
                    //				+ '					<span style="color:black ; font-size:14px ;">사용</span>'
                    + '					<span style="color:#222222 ; font-size:14px ;">' + insertComma(useQuantity) + ' </span>'
                    + '				</td>'
                    + '				<td style="width: 80px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + insertComma(nowAmount) + '</span>'
                    + '				</td>'
                    + '				<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    //					+ '					<img src="images/lbl_other.png" />'
                    //				+ '					<span style="color:black ; font-size:14px ;">기타</span>'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + insertComma(otherAmount) + '</span>'
                    + '				</td>'
                    + '				<td style="width: 50px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    //					+ '					<img src="images/lbl_delay.png" />'
                    //				+ '					<span style="color:black ; font-size:14px ;">연체</span>'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + insertComma(chargeAmount) + '</span>'
                    + '				</td>'
                    + '				<td style="text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">'
                    //				+ '					' + collectTypeHtml
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + insertComma(sumNowAmount) + '</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
                $("#divSearchResultManageReadMeterList").append(html).trigger("create");
            });

            $("#divSearchResultManageReadMeterList a").unbind("click"); //모든 클릭 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
            $("#divSearchResultManageReadMeterList a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 거래처별 업무 메뉴 출력
                //거래처별 업무 메뉴를 선택한 페이지 정보를 검침 현황 페이지로 설정
                $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageManageReadMeterList");
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id"), "1"); //검침은 체적장부로
            });
            var nextPageNumber = parseInt($("#hdnNextPageNumberManageReadMeterList").attr("value"), 10) + 1;
            $("#hdnNextPageNumberManageReadMeterList").attr("value", nextPageNumber); //다음 페이지 세팅 처리
            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnMorePageManageReadMeterList").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageManageReadMeterList()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnMorePageManageReadMeterList").html("").trigger("create");
            }
        }
    });
}

//공지사항 페이지로 이동
function showPageNotice() {
    setCurrentPage("pageNotice");
    alert("개발중");
}

//거래처별 업무 메뉴 - 상세보기 클릭 처리
function clickCustomerDetailMenu() {
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerDetail");
    choiceCustomer($("#hdnCurrentCustomerAreaCode").attr("value"), $("#hdnTapholdCustomerCode").attr("value"));
}

//거래처별 업무 메뉴 - 일반장부 클릭 처리
function clickCustomerBookWeightMenu() {
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerBookWeight");
    choiceCustomer($("#hdnCurrentCustomerAreaCode").attr("value"), $("#hdnTapholdCustomerCode").attr("value"));
}

//거래처별 업무 메뉴 - 체적장부 클릭 처리
function clickCustomerBookVolumeMenu() {
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerBookVolume");
    choiceCustomer($("#hdnCurrentCustomerAreaCode").attr("value"), $("#hdnTapholdCustomerCode").attr("value"));
}

//거래처별 업무 메뉴 - 거래장부 클릭 처리
function clickCustomerBookMenu() {
    if (!hasPermission("hdnMenuPermissionCustomerBook", ["0", "1"])) {
        alert("권한이 없습니다.");
        return;
    }

    var customerType = $("#hdnTapholdCustomerType").attr("value");
    if (customerType == "0") { //일반 업체일경우
        clickCustomerBookWeightMenu();
    } else { //체적 업체일 경우
        clickCustomerBookVolumeMenu();
    }
}

//거래처별 업무 메뉴 - 세금계산서 클릭 처리
function clickCustomerBookTaxInvoiceMenu() {
    if (!hasPermission("hdnMenuPermissionCustomerBook", ["0", "1"])) {
        alert("권한이 없습니다222222222.");
        return;
    }

    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerBookTaxInvoice");
    choiceCustomer($("#hdnCurrentCustomerAreaCode").attr("value"), $("#hdnTapholdCustomerCode").attr("value"));
}

//거래처별 업무 메뉴 - 재고내역 클릭 처리
function clickCustomerBookItemBalanceMenu() {
    // if (!hasPermission("hdnMenuPermissionCustomerBook", ["0", "1"])) {
    //     alert("권한이 없습니다.");
    //     return;
    // }
    //alert('재고내역 클릭 처리')

    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerBookItemBalance");
    choiceCustomer($("#hdnCurrentCustomerAreaCode").attr("value"), $("#hdnTapholdCustomerCode").attr("value"));
}

//거래처별 업무 메뉴 - 일반판매등록 클릭 처리
function clickCustomerSaleWeightMenu() {
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerSaleWeightInsert");
    choiceCustomer($("#hdnCurrentCustomerAreaCode").attr("value"), $("#hdnTapholdCustomerCode").attr("value"));
}

//거래처별 업무 메뉴 - 체적공급등록 클릭 처리
function clickCustomerSaleVolumeMenu() {
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerSaleVolumeInsert");
    choiceCustomer($("#hdnCurrentCustomerAreaCode").attr("value"), $("#hdnTapholdCustomerCode").attr("value"));
}

//거래처별 업무 메뉴 - 판매등록 클릭 처리
function clickCustomerSaleMenu() {
    if (!hasPermission("hdnMenuPermissionSale", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    var customerType = $("#hdnTapholdCustomerType").attr("value");
    if (customerType == "0") { //일반 업체일경우
        clickCustomerSaleWeightMenu();
    } else { //체적 업체일 경우
        clickCustomerSaleVolumeMenu();
    }
}

//거래처별 업무 메뉴 - 검침등록 클릭 처리
function clickCustomerReadMeterMenu() {
    if (!hasPermission("hdnMenuPermissionReadMeter", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerReadMeterInsert");
    choiceCustomer($("#hdnCurrentCustomerAreaCode").attr("value"), $("#hdnTapholdCustomerCode").attr("value"));
}

//거래처별 업무 메뉴 - 수금등록 클릭 처리
function clickCustomerCollectMenu() {
    if (!hasPermission("hdnMenuPermissionCollect", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerCollect");
    choiceCustomer($("#hdnCurrentCustomerAreaCode").attr("value"), $("#hdnTapholdCustomerCode").attr("value"));
}

//거래처별 업무 메뉴 - 안전점검이력 클릭 처리
function clickCustomerSaftyCheckListMenu() {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerSaftyCheckList");
    choiceCustomer($("#hdnCurrentCustomerAreaCode").attr("value"), $("#hdnTapholdCustomerCode").attr("value"));
}

//거래처별 업무 메뉴 - 안전점검등록 클릭 처리
function clickCustomerSaftyCheckMenu() {
    $("#hdnCurrentPageCustomerBiz").attr("value", "pageCustomerSaftyCheckInsert");
    choiceCustomer($("#hdnCurrentCustomerAreaCode").attr("value"), $("#hdnTapholdCustomerCode").attr("value"));
}

//거래처별 업무 메뉴 다이얼로그로 이동
function showDialogCustomerBizMenu(customerCode, customerType) {
    //	$("#hdnCustomerDialogPreviousPage").attr("value", $("#hdnCurrentPage").attr("value"));
    $("#hdnCurrentPage").attr("value", "dialogCustomerBizMenu");
    $("#hdnTapholdCustomerCode").attr("value", customerCode);
    $("#hdnTapholdCustomerType").attr("value", customerType);
    if ((customerCode == "") || (customerCode == undefined)) {
        alert("거래처 정보가 없습니다.");
    } else { //거래처 코드가 있을 때만 메뉴 오픈
        $.mobile.changePage("#dialogCustomerBizMenu", { changeHash: false, role: "dialog" });
        //		$("#selectCustomerBizMenu").selectmenu();
        //		$("#selectCustomerBizMenu").selectmenu("open");
    }
}


//거래처별 업무 메뉴 다이얼로그 닫기
//다이얼로그를 호출한 페이지로 이동하기
function closeDialogCustomerBizMenu() {
    $.mobile.changePage("#" + $("#hdnCallPageDiaglogCustomerBizMenu").attr("value"), { changeHash: false });
    $("#hdnCurrentPage").attr("value", $("#hdnCallPageDiaglogCustomerBizMenu").attr("value"));
}

//홈의 거래처 검색 텍스트 처리하기
function keydownHomeSearch() {
    if (event.keyCode == 13) {
        homeSearch();
    }
}

//홈의 거래처 검색 처리하기
function homeSearch() {
    $("#txtCustomerKeyword").attr("value", $("#txtHomeCustomerKeyword").attr("value"));


    showPageCustomerSearch();
    customerSearch();
}

//거래처 검색 텍스트 처리하기
function keydownCustomerSearch() {
    if (event.keyCode == 13) {
        customerSearch();
    }
}

//거래처 검색 처리하기
function customerSearch() {
    focusControl("btnSearchCustomer");

    if ($("#txtCustomerKeyword").attr("value") == "") {
        $("#searchCustomerResult").html(getResultMessage("검색어를 입력해주세요.", false)).trigger("create");
        $("#btnCustomerSearchMorePage").html("");
        focusControl("txtCustomerKeyword");
        return;
    }
    $("#btnCustomerSearchMorePage").html("").trigger("create");
    //$("#searchCustomerResult").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")

    $.ajax({
        url: gasmaxWebappPath + "search_customer_keyword_ajx.jsp",
        type: "post",
        data: "keyword=" + encodeURIComponent($("#txtCustomerKeyword").val()),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#searchCustomerResult").html(html).trigger("create");
                $("#btnCustomerSearchMorePage").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerSearch").each(function () {
                rowCount++;
                //				var sequenceNumber = $(this).find("sequenceNumber").text();
                var customerTypeCode = $(this).find("customerType").text();
                if (parseInt(customerTypeCode, 10) > 4) customerTypeCode = "1";
                var customerTypeIcon = "images/lbl_customer_type_" + customerTypeCode + ".png";
                var areaCode = $(this).find("areaCode").text();
                var customerCode = $(this).find("customerCode").text();
                var customerStatusCode = $(this).find("customerStatusCode").text(); // 0.정상, 1.대기, 2.중지, 3.폐업
                var customerStatusName = "[" + $(this).find("customerStatusName").text() + "]"; // 0.정상, 1.대기, 2.중지, 3.폐업
                var customerNameStyle = "#222222";
                if (customerStatusCode != "0") {
                    customerNameStyle = "#999999";
                } else {
                    customerStatusName = "";
                }
                var customerName = $(this).find("customerName").text();
                var phoneNumber = $(this).find("phoneNumber").text();
                var mobileNumber = $(this).find("mobileNumber").text();
                var address1 = $(this).find("address1").text();
                var address2 = $(this).find("address2").text();
                var area_customer_code = "'" + areaCode + "', '" + customerCode + "'";

                html += '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" onclick="choiceCustomer(' + area_customer_code + ')" id="' + customerCode + '" id2="' + customerTypeCode + '">'
                    + '		<table style="width: 100% ;" >'
                    + '			<tr>'
                    + '				<td>'
                    + '					<img src="' + customerTypeIcon + '" ></img>'
                    + '					<span style="color: ' + customerNameStyle + '; font-size:16px ;">' + customerName + '</span><span style="color: red ; font-size: 16px ;" >' + customerStatusName + '</span>'
                    + '					<br/>'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + phoneNumber + ' ' + mobileNumber + '</span>'
                    + '					<br/>'
                    + '					<span style="color:#222222 ; font-size:14px ;">' + address1 + ' ' + address2 + '&nbsp;</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
            });
            $("#searchCustomerResult").html(html).trigger("create");
            if (Capacitor.getPlatform() === 'ios') {
                $("#table01").css("margin-top", "50px");
            }
            $("#searchCustomerResult a").taphold(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 꾹 누르고 있을 때 거래처별 업무 메뉴 출력
                //거래처별 업무 메뉴를 선택한 페이지 정보를 거래처 검색 페이지로 설정
                $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageCustomerSearch");
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"));
            });
            $("#hdnNextPageNumberSearchCustomer").attr("value", "2"); //다음 페이지는 무조건 2가 되도록 세팅

            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnCustomerSearchMorePage").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerSearch()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnCustomerSearchMorePage").html("").trigger("create");
            }
        }
    });
}

//현재 태크의 특정 상위 태그명의 id 값 반환
//sourceTag jQuery의 객체
//tagName 찾으려는 상위 태그명
//propertyName 찾은 상위 태크에서 반환할 속성 값
function getParentSpecifiedTagId(sourceTag, tagName, propertyName) {
    var result = "";
    var count = 0;
    var countThreshold = 10;
    var currentTag = sourceTag;
    while (true) {
        if (currentTag[0].nodeName == tagName.toUpperCase()) {
            result = currentTag.attr(propertyName);
            break;
        }
        if (count > countThreshold) break;
        count++;
        currentTag = currentTag.parent();
    }
    return result;
}

//거래처 코드 QR코드로 검색
function searchCustomerQR(customerCode) {
    //	$("#txtCustomerKeyword").attr("value", customerCode);
    showPageCustomerSearch();
    $("#btnCustomerSearchMorePage").html("").trigger("create");
    $("#searchCustomerResult").html(getResultMessage("QR코드(" + customerCode + ") 검색 중입니다.", true)).trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "search_customer_qrcode_ajx.jsp",
        type: "post",
        data: "customerCode=" + customerCode,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#searchCustomerResult").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var searchAreaCode = $(xml).find("areaCode").text();
            var searchCustomerCode = $(xml).find("customerCode").text();
            if (searchCustomerCode != "") {
                $("#hdnCurrentCustomerCode").attr("value", searchCustomerCode);
                choiceCustomer(searchAreaCode, searchCustomerCode);
                $("#searchCustomerResult").html("").trigger("create");
                $("#btnCustomerSearchMorePage").html("").trigger("create");
            } else {
                var html = getResultMessage("QR코드(" + customerCode + ")로 검색된 거래처 코드가 없습니다.", false);
                $("#searchCustomerResult").html(html).trigger("create");
            }
        }
    });
}

//거래처 검색 페이지 추가 처리하기
function clickMorePageCustomerSearch() {
    $.ajax({
        url: gasmaxWebappPath + "search_customer_paging_ajx.jsp",
        type: "post",
        data: "pageNumber=" + $("#hdnNextPageNumberSearchCustomer").attr("value"),
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("더 이상 자료가 없습니다.", false);
                $("#searchCustomerResult").append(html).trigger("create");
                $("#btnCustomerSearchMorePage").html("").trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var rowCount = 0;
            $(xml).find("CustomerSearch").each(function () {
                rowCount++;
                //				var sequenceNumber = $(this).find("sequenceNumber").text();
                var customerTypeCode = $(this).find("customerType").text();
                if (parseInt(customerTypeCode, 10) > 4) customerTypeCode = "1";
                var customerTypeIcon = "images/lbl_customer_type_" + customerTypeCode + ".png";
                var areaCode = $(this).find("areaCode").text();
                var customerCode = $(this).find("customerCode").text();
                var customerStatusCode = $(this).find("customerStatusCode").text(); // 0.정상, 1.대기, 2.중지, 3.폐업
                var customerStatusName = "[" + $(this).find("customerStatusName").text() + "]"; // 0.정상, 1.대기, 2.중지, 3.폐업
                var customerNameStyle = "#222222";
                if (customerStatusCode != "0") {
                    customerNameStyle = "#999999";
                } else {
                    customerStatusName = "";
                }
                var customerName = $(this).find("customerName").text();
                var phoneNumber = $(this).find("phoneNumber").text();
                var mobileNumber = $(this).find("mobileNumber").text();
                var address1 = $(this).find("address1").text();
                var address2 = $(this).find("address2").text();
                var area_customer_code = "'" + areaCode + "', '" + customerCode + "'";

                var html = '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" onclick="choiceCustomer(' + area_customer_code + ')" ' + ' id="' + customerCode + '" id2="' + customerTypeCode + '" >'
                    + '		<table style="width: 100% ; ">'
                    + '			<tr>'
                    + '				<td>'
                    + '					<img src="' + customerTypeIcon + '" ></img>'
                    + '					<span style="color: ' + customerNameStyle + '; font-size:16px ;">' + customerName + '</span><span style="color: red ; font-size: 16px ;" >' + customerStatusName + '</span>'
                    + '					<br/>'
                    + '					<span style="color:#3300FF ; font-size:14px ;">' + phoneNumber + ' ' + mobileNumber + '</span>'
                    + '					<br/>'
                    + '					<span style="color:#222222 ; font-size:14px ;">' + address1 + ' ' + address2 + '&nbsp;</span>'
                    + '				</td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';

                $("#searchCustomerResult").append(html).trigger("create");
            });
            $("#searchCustomerResult a").unbind("taphold"); //모든 taphold 이벤트를 우선 제거한다.(중복 이벤트 발생을 방지)
            $("#searchCustomerResult a").taphold(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 꾹 누르고 있을 때 거래처별 업무 메뉴 출력
                //거래처별 업무 메뉴를 선택한 페이지 정보를 거래처 검색 페이지로 설정
                $("#hdnCallPageDiaglogCustomerBizMenu").attr("value", "pageCustomerSearch");
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 거래처 코드를 이용하여 거래처별 업무 메뉴 출력
                showDialogCustomerBizMenu(getParentSpecifiedTagId($(event.target), "a", "id"), getParentSpecifiedTagId($(event.target), "a", "id2"));
            });
            var nextPageNumber = parseInt($("#hdnNextPageNumberSearchCustomer").attr("value"), 10) + 1;
            $("#hdnNextPageNumberSearchCustomer").attr("value", nextPageNumber); //다음 페이지 세팅 처리

            if (rowCount == 20) { //20건이 출력되었다면 다음 페이지 표시 버튼 보이기
                $("#btnCustomerSearchMorePage").html('<table style="width: 100% ; "><tr><td><a href="#" data-role="button" data-mini="true" onclick="clickMorePageCustomerSearch()">검색결과 20건 더보기</a></td></tr></table>').trigger("create");
            } else {
                $("#btnCustomerSearchMorePage").html("").trigger("create");
            }
        }
    });
}

//회원가입 신청 화면 삽입하기
function injectionAppUserInsert(tagId) {

    let hpNo = localStorage.getItem("phoneNumber")

    if (hpNo && hpNo.startsWith("+82")) {
        hpNo = "0" + hpNo.slice(3); // "+82" 제거 후 앞에 "0" 추가
    }

    $.ajax({
        url: gasmaxWebappPath + "app_user_insert_rev3.jsp",
        type: "post",
        data: "macNumber=" + window.sessionStorage.uuid + "&phoneNumber=" + hpNo,
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//거래처 신규등록 화면 저장 클릭 처리
function clickSaveAppUserInsert() {
    $("#divSaveMessageAppUserInsert").html(getResultMessage("가입신청 중입니다.", true)).trigger("create");
    var macNumber = $("#hdnUuid").attr("value");
    if (macNumber == "") {
        $("#divSaveMessageAppUserInsert").html(getResultMessage("모바일 기기에서만 가입신청이 가능합니다.", false)).trigger("create");
        return;
    }
    var mobileNumber = $("#hdnMobileNumber").attr("value");
    var prefixMobileNumber = mobileNumber.substring(0, 1);
    if (prefixMobileNumber == "+") {
        if (mobileNumber.length > 3) {
            mobileNumber = "0" + mobileNumber.substring(3);
        }
    }
    var areaName = $("#txtAreaNameAppUserInsert").attr("value");
    if (areaName == "") {
        $("#divSaveMessageAppUserInsert").html(getResultMessage("업체명을 입력해주세요.", false)).trigger("create");
        return;
    }
    var employeeName = $("#txtEmployeeNameAppUserInsert").attr("value");
    if (employeeName == "") {
        $("#divSaveMessageAppUserInsert").html(getResultMessage("성명을 입력해주세요.", false)).trigger("create");
        return;
    }
    var userId = $("#txtUserIdAppUserInsert").attr("value");
    if (userId == "") {
        $("#divSaveMessageAppUserInsert").html(getResultMessage("사용자명을 입력해주세요.", false)).trigger("create");
        return;
    }
    if (userId == "") {
        $("#divSaveMessageAppUserInsert").html(getResultMessage("사용자명을 입력해주세요.", false)).trigger("create");
        return;
    }
    var password = $("#txtPasswordAppUserInsert").attr("value");
    if (password == "") {
        $("#divSaveMessageAppUserInsert").html(getResultMessage("비밀번호를 입력해주세요.", false)).trigger("create");
        return;
    }
    var passwordConfirm = $("#txtPasswordConfirmAppUserInsert").attr("value");
    if (passwordConfirm == "") {
        $("#divSaveMessageAppUserInsert").html(getResultMessage("비밀번호를 확인하기 위해 다시 입력해주세요.", false)).trigger("create");
        return;
    }
    if (password != passwordConfirm) {
        $("#divSaveMessageAppUserInsert").html(getResultMessage("다시 입력한 비밀번호를 확인해주세요.", false)).trigger("create");
        return;
    }
    //	var phoneAreaNumber = $("#selectPhoneAreaNumberAppUserInsert").attr("value");
    var phoneAreaNumber = "";

    let hpNo = localStorage.getItem("phoneNumber")

    if (hpNo && hpNo.startsWith("+82")) {
        hpNo = "0" + hpNo.slice(3); // "+82" 제거 후 앞에 "0" 추가
    }

    $.ajax({
        url: gasmaxWebappPath + "app_user_insert_save_s2_ajx.jsp",
        type: "post",
        data: "macNumber=" + sec(macNumber)
            + "&mobileNumber=" + hpNo
            + "&phoneNumber=" + hpNo
            + "&areaName=" + areaName
            + "&employeeName=" + employeeName
            + "&userId=" + userId
            + "&password=" + sec(password)
            + "&phoneAreaNumber=" + phoneAreaNumber
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var result = $(xml).find("code").text();
            var html = "";
            if (result == "Y") {
                html = getResultMessage("회원가입 신청이 완료되었습니다.", false);
            } else {
                html = getResultMessage($(xml).find("description").text(), false);
            }
            $("#divSaveMessageAppUserInsert").html(html).trigger("create");
        }
    });
}

//거래처 신규등록 화면 삽입하기
function injectionCustomerInsert(tagId) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "customer_insert.jsp",
        type: "post",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });

}

//거래처 수정 화면 삽입하기
function injectionCustomerUpdate(tagId) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "customer_update.jsp",
        type: "post",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//거래처 신규등록 화면 초기화 클릭 처리
function clickCustomerSearchInsertInit() {
    $("#selectCustomerSearchInsertCustomerType").attr("value", "0");
    $("#txtCustomerSearchInsertCustomerName").attr("value", "");
    $("#txtCustomerSearchInsertUserName").attr("value", "");
    $("#txtCustomerSearchInsertPhoneNumber").attr("value", "");
    $("#txtCustomerSearchInsertMobileNumber").attr("value", "");
    $("#txtCustomerSearchInsertAddress1").attr("value", "");
    $("#txtCustomerSearchInsertAddress2").attr("value", "");
    $("#txtCustomerSearchInsertRemark1").attr("value", "");
    $("#txtCustomerSearchInsertRemark2").attr("value", "");
    $("#selectCustomerSearchConsumeTypeCode").attr("value", "00");
}

//거래처 신규등록 화면 저장 클릭 처리
function clickCustomerSearchInsertSave() {
    if (!hasPermission("hdnMenuPermissionCustomerInsert", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    var customerType = $("#selectCustomerSearchInsertCustomerType").attr("value");
    var customerName = $("#txtCustomerSearchInsertCustomerName").attr("value");
    var userName = $("#txtCustomerSearchInsertUserName").attr("value");
    var phoneNumber = $("#txtCustomerSearchInsertPhoneNumber").attr("value");
    var mobileNumber = $("#txtCustomerSearchInsertMobileNumber").attr("value");
    var address1 = $("#txtCustomerSearchInsertAddress1").attr("value");
    var address2 = $("#txtCustomerSearchInsertAddress2").attr("value");
    var remark1 = $("#txtCustomerSearchInsertRemark1").attr("value");
    var remark2 = $("#txtCustomerSearchInsertRemark2").attr("value");
    var consumeTypeCode = $("#selectCustomerSearchInsertConsumeTypeCode").attr("value");
    if (customerName == "") {
        alert("필수 항목인 거래처명이 입력되지 않았습니다.");
        focusControl("txtCustomerSearchInsertCustomerName");
    } else {
        $("#divSaveMessageCustomerSearchInsert").html(getResultMessage("거래처 저장 중입니다.", true)).trigger("create");
        $.ajax({
            url: gasmaxWebappPath + "customer_insert_save_ajx.jsp",
            type: "post",
            data: "customerType=" + customerType
                + "&customerName=" + customerName
                + "&userName=" + userName
                + "&phoneNumber=" + phoneNumber
                + "&mobileNumber=" + mobileNumber
                + "&address1=" + address1
                + "&address2=" + address2
                + "&remark1=" + remark1
                + "&remark2=" + remark2
                + "&consumeTypeCode=" + consumeTypeCode,
            dataType: "xml",
            timeout: 120000,
            error: function (result) {
                if (result.status == 0) {
                    console.log("서버 응답 지연 (timeout 또는 연결 실패)");
                } else {
                    alert("error occured. Status:" + result.status
                        + " --Status Text:" + result.statusText
                        + " --Error Result:" + result);
                }
                $("#divSaveMessageCustomerSearchInsert").html("").trigger("create");
            },
            success: function (xml) {
                if ($(xml).find("session").text() == "X") {
                    alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                    showPageIntro(false);
                    return;
                }
                var areaCode = $(xml).find("areaCode").text();
                var customerCode = $(xml).find("customerCode").text();
                $("#divSaveMessageCustomerSearchInsert").html("").trigger("create");
                if ($("#hdnCidCustomerSearchYesNo").attr("value") == "Y") { //만일 CID 편집화면에서 검색한 경우에는 이전 페이지를 CID 편집화면으로 강제로 변경함.
                    $("#hdnPreviousPage").attr("value", "pageManageCidEdit");
                }
                choiceCustomer(areaCode, customerCode);
            }
        });
    }
}

//거래처 수정 화면 저장 클릭 처리
function clickCustomerUpdateSave() {
    if (!hasPermission("hdnMenuPermissionCustomerInsert", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    var areaCode = $("#hdnCustomerUpdateAreaCode").attr("value");
    var customerCode = $("#hdnCustomerUpdateCustomerCode").attr("value");
    var customerType = $("#selectCustomerUpdateCustomerType").attr("value");
    var customerName = $("#txtCustomerUpdateCustomerName").attr("value");
    var buildingName = $("#txtCustomerUpdateCustomerName").attr("value");
    var userName = $("#txtCustomerUpdateUserName").attr("value");
    var phoneNumber = $("#txtCustomerUpdatePhoneNumber").attr("value");
    var mobileNumber = $("#txtCustomerUpdateMobileNumber").attr("value");
    var address1 = $("#txtCustomerUpdateAddress1").attr("value");
    var address2 = $("#txtCustomerUpdateAddress2").attr("value");
    var remark1 = $("#txtCustomerUpdateRemark1").attr("value");
    var remark2 = $("#txtCustomerUpdateRemark2").attr("value");
    var consumeTypeCode = $("#selectCustomerUpdateConsumeTypeCode").attr("value");
    if (customerName == "") {
        alert("필수 항목인 거래처명이 입력되지 않았습니다.");
        focusControl("txtCustomerUpdateCustomerName");
    } else {
        $("#divCustomerUpdateSavingMessage").html(getResultMessage("거래처 저장 중입니다.", true)).trigger("create");
        $.ajax({
            url: gasmaxWebappPath + "customer_update_save_ajx.jsp",
            type: "post",
            data: "customerType=" + customerType
                + "&customerCode=" + customerCode
                + "&areaCode=" + areaCode
                + "&customerName=" + customerName
                + "&buildingName=" + buildingName
                + "&userName=" + userName
                + "&phoneNumber=" + phoneNumber
                + "&mobileNumber=" + mobileNumber
                + "&address1=" + address1
                + "&address2=" + address2
                + "&remark1=" + remark1
                + "&remark2=" + remark2
                + "&consumeTypeCode=" + consumeTypeCode,
            dataType: "xml",
            timeout: 120000,
            error: function (result) {
                if (result.status == 0) {
                    console.log("서버 응답 지연 (timeout 또는 연결 실패)");
                } else {
                    alert("error occured. Status:" + result.status
                        + " --Status Text:" + result.statusText
                        + " --Error Result:" + result);
                }
                $("#divCustomerUpdateSavingMessage").html("").trigger("create");
            },
            success: function (xml) {
                if ($(xml).find("session").text() == "X") {
                    alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                    showPageIntro(false);
                    return;
                }
                $("#divSaveMessageCustomerSearchInsert").html("").trigger("create");
                choiceCustomer(areaCode, customerCode);
            }
        });
    }
}


//거래처별 업무 하단 메뉴 삽입하기
function injectionFooterCustomerBiz(tagId, selectId) {
    var customerBookMenuStyle = "";
    if (selectId == 1) customerBookMenuStyle = 'class="ui-btn-active"';
    var customerSaleMenuStyle = "";
    if (selectId == 2) customerSaleMenuStyle = 'class="ui-btn-active"';
    var customerReadMeterMenuStyle = "";
    if (selectId == 3) customerReadMeterMenuStyle = 'class="ui-btn-active"';
    var customerCollectMenuStyle = "";
    if (selectId == 4) customerCollectMenuStyle = 'class="ui-btn-active"';
    var customerSaftyCheckMenuStyle = "";
    if (selectId == 5) customerSaftyCheckMenuStyle = 'class="ui-btn-active"';
    var html = '				<nav data-role="navbar">'
        + '					<ul>'
        + '						<li><a href="#" onclick="showPageCustomerBook()"' + customerBookMenuStyle + '>거래<br/>장부</a></li>'
        + '						<li><a href="#" onclick="showPageCustomerSale()"' + customerSaleMenuStyle + '>판매<br/>등록</a></li>'
        + '						<li><a href="#" onclick="showPageCustomerReadMeterInsert()"' + customerReadMeterMenuStyle + '>검침<br/>등록</a></li>'
        + '						<li><a href="#" onclick="showPageCustomerCollect()"' + customerCollectMenuStyle + '>수금<br/>등록</a></li>'
        + '						<li><a href="#" onclick="showPageCustomerSaftyCheck()"' + customerSaftyCheckMenuStyle + '>안전<br/>점검</a></li>'
        + '					</ul>'
        + '				</nav>';
    $("#" + tagId).html(html).trigger("create");
}

//거래처별 거래장부 업무 하단 메뉴 삽입하기
function injectionSubFooterCustomerBook(tagId, selectId) {
    var customerBookWeightSubMenuStyle = "";
    if (selectId == 1) customerBookWeightSubMenuStyle = 'class="ui-btn-active"';
    var customerBookVolumeReadMeterSubMenuStyle = "";
    if (selectId == 2) customerBookVolumeReadMeterSubMenuStyle = 'class="ui-btn-active"';
    var customerBookTaxInvoiceSubMenuStyle = "";
    if (selectId == 3) customerBookTaxInvoiceSubMenuStyle = 'class="ui-btn-active"';
    var customerBookItemBalanceSubMenuStyle = "";
    if (selectId == 4) customerBookItemBalanceSubMenuStyle = 'class="ui-btn-active"';
    var html = '		<nav data-role="navbar">'
        + '					<ul>'
        + '						<li><a href="#" onclick="showPageCustomerBookWeight()"' + customerBookWeightSubMenuStyle + '>일반</a></li>'
        + '						<li><a href="#" onclick="showPageCustomerBookVolume()"' + customerBookVolumeReadMeterSubMenuStyle + '>체적</a></li>'
        + '						<li><a href="#" onclick="showPageCustomerBookTaxInvoice()"' + customerBookTaxInvoiceSubMenuStyle + '>세금</a></li>'
        + '						<li><a href="#" onclick="showPageCustomerBookItemBalance()"' + customerBookItemBalanceSubMenuStyle + '>재고</a></li>'
        + '					</ul>'
        + '				</nav>';
    $("#" + tagId).html(html).trigger("create");
}

//거래처별 판매등록 업무 하단 메뉴 삽입하기
function injectionSubFooterCustomerSale(tagId, selectId) {
    var customerSaleWeightDetailSubMenuStyle = "";
    if (selectId == 1) customerSaleWeightDetailSubMenuStyle = 'class="ui-btn-active"';
    var customerSaleVolumeDetailSubMenuStyle = "";
    if (selectId == 2) customerSaleVolumeDetailSubMenuStyle = 'class="ui-btn-active"';
    var html = '		<nav data-role="navbar">'
        + '					<ul>'
        + '						<li><a href="#" onclick="showPageCustomerSaleWeightInsert()"' + customerSaleWeightDetailSubMenuStyle + '>일반판매</a></li>'
        + '						<li><a href="#" onclick="showPageCustomerSaleVolumeInsert()"' + customerSaleVolumeDetailSubMenuStyle + '>체적공급</a></li>'
        + '					</ul>'
        + '				</nav>';
    $("#" + tagId).html(html).trigger("create");
}

//거래처별 안전점검 업무 하단 메뉴 삽입하기
function injectionSubFooterCustomerSaftyCheck(tagId, selectId) {
    // [2017.11.01][Rev3] 메뉴 추가 및 메뉴명 변경
    // '저장탱크 점검' 메뉴 추가
    // '점검등록' -> '소비설비 점검'으로 메뉴명 변경
    var customerSaftyCheckListSubMenuStyle = "";
    if (selectId == 1) customerSaftyCheckListSubMenuStyle = 'class="ui-btn-active"';
    var customerSaftyCheckInsertSubMenuStyle = "";
    if (selectId == 2) customerSaftyCheckInsertSubMenuStyle = 'class="ui-btn-active"';
    var customerSaftyCheckTankInsertSubMenuStyle = "";
    if (selectId == 3) customerSaftyCheckTankInsertSubMenuStyle = 'class="ui-btn-active"';
    var html = '		<nav data-role="navbar">'
        + '					<ul>'
        + '						<li><a href="#" onclick="showPageCustomerSaftyCheckList()"' + customerSaftyCheckListSubMenuStyle + '>점검이력</a></li>'
        + '						<li><a href="#" onclick="showPageCustomerSaftyCheckInsert()"' + customerSaftyCheckInsertSubMenuStyle + '>소비설비 점검</a></li>'
        + '						<li><a href="#" onclick="showPageCustomerSaftyCheckTankInsert()"' + customerSaftyCheckTankInsertSubMenuStyle + '>저장탱크 점검</a></li>'
        + '					</ul>'
        + '				</nav>';
    $("#" + tagId).html(html).trigger("create");
}

//거래처 거래장부-일반장부 검색 조건 삽입하기 pageType 0:거래내역 1:판매내역
function injectionSearchOptionCustomerBookWeight(tagId, pageType) {
    if (pageType == undefined) {
        pageType = "0";
    }
    $.ajax({
        url: gasmaxWebappPath + "customer_book_weight_search_option.jsp",
        type: "post",
        data: "startDate=" + $("#hdnStartDateCustomerBookWeight").attr("value")
            + "&endDate=" + $("#hdnEndDateCustomerBookWeight").attr("value")
            + "&radioIndex=" + pageType
        ,
        dataType: "html",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
    $("#hdnRequireRefreshPageCustomerBookWeight").attr("value", "N"); //거래처 거래장부-일반장부가 이제 Refresh되었음을 표시
}

//거래처 거래장부-체적장부 검색 조건 삽입하기
function injectionSearchOptionCustomerBookVolume(tagId, pageType) {
    if (pageType == undefined) {
        pageType = "0";
    }
    $.ajax({
        url: gasmaxWebappPath + "customer_book_volume_search_option.jsp",
        type: "post",
        data: "startDate=" + $("#hdnStartDateCustomerBookVolume").attr("value")
            + "&endDate=" + $("#hdnEndDateCustomerBookVolume").attr("value")
            + "&radioIndex=" + pageType
        ,
        dataType: "html",
        async: false,
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
    $("#hdnRequireRefreshPageCustomerBookVolume").attr("value", "N"); //거래처 거래장부-체적장부가 이제 Refresh되었음을 표시
}

//거래처 거래장부-세금계산서 검색 조건 삽입하기
function injectionSearchOptionCustomerBookTaxInvoice(tagId) {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_tax_invoice_search_option.jsp",
        type: "post",
        data: "startDate=" + $("#hdnStartDateCustomerBookTaxInvoice").attr("value")
            + "&endDate=" + $("#hdnEndDateCustomerBookTaxInvoice").attr("value"),
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
    $("#hdnRequireRefreshPageCustomerBookTaxInvoice").attr("value", "N"); //거래처 거래장부-세금계산서가 이제 Refresh되었음을 표시
}

//거래처 거래장부-재고현황 검색 조건 삽입하기
function injectionSearchOptionCustomerBookItemBalance(tagId) {
    $.ajax({
        url: gasmaxWebappPath + "customer_book_item_balance_search_option.jsp",
        type: "post",
        data: "startDate=" + $("#hdnStartDateCustomerBookItemBalance").attr("value")
            + "&endDate=" + $("#hdnEndDateCustomerBookItemBalance").attr("value"),
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
    $("#hdnRequireRefreshPageCustomerBookItemBalance").attr("value", "N"); //거래처 거래장부-재고현황이 이제 Refresh되었음을 표시
}

//거래처 거래장부-재고현황(고압) 상세내역 검색 조건 삽입하기
function injectionSearchOptionCustomerBookItemBalanceHPGDetailList(tagId, itemName, salePrice, preBalance, inout, balance) {
    $("#footerSummaryCustomerBookItemBalanceDetailList").html("").trigger("create");
    var startDate = $("#txtStartDateCustomerBookItemBalance").attr("value");
    var endDate = $("#txtEndDateCustomerBookItemBalance").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_book_item_balance_hpg_detail_list_search_option.jsp",
        type: "post",
        data: "itemName=" + itemName
            + "&startDate=" + startDate
            + "&endDate=" + endDate
        ,
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
            var footerHtml = '<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; ">'
                + '<tr><td style="text-align: right ; font-size: 16px ; "><span> 단가: ' + insertComma(salePrice) + ", 전재고: " + insertComma(preBalance) + ", 出/入: " + inout + ", 재고: " + insertComma(balance) + "</span></td></tr>"
                + '</table>';
            $("#footerSummaryCustomerBookItemBalanceDetailList").html(footerHtml).trigger("create");
        }
    });
    $("#hdnRequireRefreshPageCustomerBookItemBalanceDetailList").attr("value", "N"); //거래처 거래장부-재고현황(고압) 상세내역이 이제 Refresh되었음을 표시
}


//거래처 판매등록-일반판매 검색 조건 삽입하기
function injectionCustomerSaleWeightInsert(tagId) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "customer_sale_weight_insert_batch.jsp?uuid=" + window.sessionStorage.uuid,
        type: "post",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });

    $("#hdnRequireRefreshPageCustomerSaleWeightInsert").attr("value", "N"); //거래처 판매등록-일반판매가 이제 Refresh되었음을 표시
}

// 거래처 판매등록-일반판매 품목 추가버튼 클릭시
function clickInsertItemCustomerSaleWeightInsertBatch() {
    showPageCustomerSaleWeightInsertItemDetail("1");
}

// 거래처 판매등록-일반판매 품목 클릭시
function clickItemCustomerSaleWeightInsertBatch(itemIndex) {
    showPageCustomerSaleWeightInsertItemDetail("0", itemIndex);
}

// 거래처 판매등록-일반판매 납품 수정 시 회수 동일하게 처리
function changeSaleQuantityCustomerSaleWeightInsertBatch(currentIndex) {
    // 남품 수량 수정시, 납품 수량을 회수 수량과 동일하게 처리
    var saleQuantity = $("#txtSaleQuantityCustomerSaleWeightInsertBatch" + currentIndex).attr("value");
    $("#txtWithdrawQuantityCustomerSaleWeightInsertBatch" + currentIndex).attr("value", saleQuantity);

    // VAT 및 입금액 등의 금액 계산
    calculateTotalAmountCustomerSaleWeightInsertBatch(currentIndex);
}

//거래처 판매등록-일반판매 금액 계산하기
function calculateTotalAmountCustomerSaleWeightInsertBatch(currentIndex) {
    var itemCount = parseInt($("#hdnItemCountCustomerSaleWeightInsertBatch").attr("value"), 10);
    var saleAmount = 0;
    var taxAmount = 0;
    var totalAmount = 0;
    var collectAmount = 0;
    var discountAmount = 0;
    var unpaidAmount = 0;
    for (var i = 1; i <= itemCount; i++) {
        var salePrice = parseFloat(deleteComma($("#txtSalePriceCustomerSaleWeightInsertBatch" + i).attr("value")), 10);
        var itemBalance = parseInt(deleteComma($("#txtItemBalanceCustomerSaleWeightInsertBatch" + i).attr("value")), 10);
        var saleQuantity = parseInt(deleteComma($("#txtSaleQuantityCustomerSaleWeightInsertBatch" + i).attr("value")), 10);
        var withdrawQuantity = parseInt(deleteComma($("#txtWithdrawQuantityCustomerSaleWeightInsertBatch" + i).attr("value")), 10);
        var quantity = saleQuantity - withdrawQuantity;
        var nowBalance = itemBalance + quantity;
        //		var nowBalance = quantity;
        $("#txtNowBalanceCustomerSaleWeightInsertBatch" + i).attr("value", insertComma(nowBalance));
        var itemSaleAmount = Math.round(salePrice * saleQuantity);
        var vatType = $("#hdnVatTypeCustomerSaleWeightInsertBatch" + i).attr("value");
        var itemTaxAmount = 0;
        var itemTotalAmount = 0;
        if (vatType == "0") {//VAT 별도일 때 처리
            itemTaxAmount = Math.round(itemSaleAmount / 10);
            itemTotalAmount = itemSaleAmount + itemTaxAmount;
        } else if (vatType == "1") { //VAT 포함일 때 처리
            itemTotalAmount = itemSaleAmount;
            itemSaleAmount = Math.round(itemTotalAmount / 1.1);
            itemTaxAmount = itemTotalAmount - itemSaleAmount;
        } else if (vatType == "2") { //비과세일 경우
            itemTotalAmount = itemSaleAmount;
        }
        saleAmount += itemSaleAmount;
        $("#hdnSaleAmountCustomerSaleWeightInsertBatch" + i).attr("value", itemSaleAmount);
        $("#hdnTaxAmountCustomerSaleWeightInsertBatch" + i).attr("value", itemTaxAmount);
        $("#hdnTotalAmountCustomerSaleWeightInsertBatch" + i).attr("value", itemTotalAmount);
        taxAmount += itemTaxAmount;
        totalAmount += itemTotalAmount;
        var itemCollectType = $("#hdnCollectTypeCustomerSaleWeightInsertBatch" + i).attr("value");
        var itemDiscountAmount = parseInt($("#hdnDiscountAmountCustomerSaleWeightInsertBatch" + i).attr("value"), 10);
        //		if (i == currentIndex){
        var currentItemCollectAmount = 0;
        var currentItemUnpaidAmount = 0;
        // 0.현금, 2.예금, 3. 카드, 4.어음, B.현금영수증: 입금액=합계금액, D/C = 0, 미입금액=0
        // A.외상: 입금액 = 0,  D/C = 0, 미입금액 =  합계금액
        // [2017.11.01][Rev3] 예금도 현금과 동일하게 처리
        if ((itemCollectType == "0") || (itemCollectType == "2") || (itemCollectType == "3") || (itemCollectType == "4") || (itemCollectType == "B")) {
            currentItemCollectAmount = itemTotalAmount - itemDiscountAmount;
        } else if (itemCollectType == "A") {
            currentItemUnpaidAmount = itemTotalAmount - itemDiscountAmount;
        }
        $("#hdnCollectAmountCustomerSaleWeightInsertBatch" + i).attr("value", currentItemCollectAmount);
        $("#hdnUnpaidAmountCustomerSaleWeightInsertBatch" + i).attr("value", currentItemUnpaidAmount);
        //		}
        var itemCollectAmount = parseInt($("#hdnCollectAmountCustomerSaleWeightInsertBatch" + i).attr("value"), 10);
        collectAmount += itemCollectAmount;
        var itemDiscountAmount = parseInt($("#hdnDiscountAmountCustomerSaleWeightInsertBatch" + i).attr("value"), 10);
        discountAmount += itemDiscountAmount;
        var itemUnpaidAmount = parseInt($("#hdnUnpaidAmountCustomerSaleWeightInsertBatch" + i).attr("value"), 10);
        unpaidAmount += itemUnpaidAmount;
    }
    $("#txtSaleAmountCustomerSaleWeightInsertBatch").attr("value", insertComma(saleAmount));			// 공급액
    $("#txtTaxAmountCustomerSaleWeightInsertBatch").attr("value", insertComma(taxAmount));				// 세액
    $("#txtTotalAmountCustomerSaleWeightInsertBatch").attr("value", insertComma(totalAmount));			// 합계
    $("#txtCollectAmountCustomerSaleWeightInsertBatch").attr("value", insertComma(collectAmount));		// 입금액
    $("#txtDiscountAmountCustomerSaleWeightInsertBatch").attr("value", insertComma(discountAmount));	// D/C
    $("#txtUnpaidAmountCustomerSaleWeightInsertBatch").attr("value", insertComma(unpaidAmount));		// 미입금
}

//거래처 판매등록-일반판매 VAT 일괄 변경
function changeDefaultVatTypeCustomerSaleWeightInsertBatch() {
    var itemCount = parseInt($("#hdnItemCountCustomerSaleWeightInsertBatch").attr("value"), 10);
    var vatType = $("#selectDefaultVatTypeCustomerSaleWeightInsertBatch").attr("value");
    for (var i = 1; i <= itemCount; i++) {
        $("#hdnVatTypeCustomerSaleWeightInsertBatch" + i).attr("value", vatType);
        var itemSaleAmount = parseInt($("#hdnSaleAmountCustomerSaleWeightInsertBatch" + i).attr("value"), 10);
        var itemTaxAmount = 0;
        var itemTotalAmount = 0;
        if (vatType == "0") {
            itemTaxAmount = Math.round(itemSaleAmount / 10);
            itemTotalAmount = itemSaleAmount + itemTaxAmount;
        } else if (vatType == "1") { //VAT 포함일 때 처리
            itemTotalAmount = itemSaleAmount;
            itemSaleAmount = Math.round(itemTotalAmount / 1.1);
            itemTaxAmount = itemTotalAmount - itemSaleAmount;
        } else if (vatType == "2") { //비과세일 경우
            itemTotalAmount = itemSaleAmount;
        }
        $("#hdnSaleAmountCustomerSaleWeightInsertBatch" + i).attr("value", itemSaleAmount);
        $("#hdnTaxAmountCustomerSaleWeightInsertBatch" + i).attr("value", itemTaxAmount);
        $("#hdnTotalAmountCustomerSaleWeightInsertBatch" + i).attr("value", itemTotalAmount);
    }
    changeDefaultCollectTypeCustomerSaleWeightInsertBatch();
    calculateTotalAmountCustomerSaleWeightInsertBatch(-1);
}

//거래처 판매등록-일반판매 입금방법 일괄 변경
function changeDefaultCollectTypeCustomerSaleWeightInsertBatch() {
    var itemCount = parseInt($("#hdnItemCountCustomerSaleWeightInsertBatch").attr("value"), 10);
    var collectType = $("#selectDefaultCollectTypeCustomerSaleWeightInsertBatch").attr("value");
    for (var i = 1; i <= itemCount; i++) {
        $("#hdnCollectTypeCustomerSaleWeightInsertBatch" + i).attr("value", collectType);
        var itemTotalAmount = parseInt($("#hdnTotalAmountCustomerSaleWeightInsertBatch" + i).attr("value"), 10);
        var itemCollectAmount = 0;
        var itemDiscountAmount = 0;
        var itemUnpaidAmount = 0;
        // 0.현금, 2.예금, 3. 카드, 4.어음, B.현금영수증: 입금액=합계금액, D/C = 0, 미입금액=0
        // A.외상: 입금액 = 0,  D/C = 0, 미입금액 =  합계금액
        // [2017.11.01][Rev3] 예금도 현금과 동일하게 처리
        if ((collectType == "0") || (collectType == "2") || (collectType == "3") || (collectType == "4") || (collectType == "B")) {
            itemCollectAmount = itemTotalAmount;
        } else if (collectType == "A") {
            itemUnpaidAmount = itemTotalAmount;
        }
        $("#hdnCollectAmountCustomerSaleWeightInsertBatch" + i).attr("value", itemCollectAmount);
        $("#hdnDiscountAmountCustomerSaleWeightInsertBatch" + i).attr("value", itemDiscountAmount);
        $("#hdnUnpaidAmountCustomerSaleWeightInsertBatch" + i).attr("value", itemUnpaidAmount);
    }
    calculateTotalAmountCustomerSaleWeightInsertBatch(-1);
}

//거래처 판매등록-일반판매 담당자 일괄 변경
function changeDefaultEmployeeCustomerSaleWeightInsertBatch() {
    var itemCount = parseInt($("#hdnItemCountCustomerSaleWeightInsertBatch").attr("value"), 10);
    var employeeCode = $("#selectDefaultEmployeeCustomerSaleWeightInsertBatch").attr("value");
    var employeeName = $("#selectDefaultEmployeeCustomerSaleWeightInsertBatch option:selected").text();
    for (var i = 1; i <= itemCount; i++) {
        $("#hdnEmployeeCodeCustomerSaleWeightInsertBatch" + i).attr("value", employeeCode);
        $("#hdnEmployeeNameCustomerSaleWeightInsertBatch" + i).attr("value", employeeName);
    }
    $("#hdnDefaultEmployeeNameCustomerSaleWeightInsertBatch").attr("value", employeeName);
}

//거래처 판매등록-일반판매 비고 일괄 변경
function changeRemarkCustomerSaleWeightInsertBatch() {
    var itemCount = parseInt($("#hdnItemCountCustomerSaleWeightInsertBatch").attr("value"), 10);
    var remark = $("#txtDefaultRemarkCustomerSaleWeightInsert").attr("value");
    for (var i = 1; i <= itemCount; i++) {
        $("#hdnRemarkTextCustomerSaleWeightInsertBatch" + i).attr("value", remark);
    }
}

//거래처 판매등록-일반판매 일괄등록 하기 저장 버튼 처리
function clickSaveInsertCustomerSaleWeightInsertBatch(continueBool) {
    if (!hasPermission("hdnMenuPermissionSale", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    // 사원선택 확인
    var employeeCode = $("#selectDefaultEmployeeCustomerSaleWeightInsertBatch").attr("value");
    if (employeeCode == "") {
        alert("사원을 선택해 주세요.");
        return;
    }

    if (saveInsertCustomerSaleWeightInsertBatch() == "") { // 저장 시 오류가 없다면,
        setAllCustomerBizRequireRefresh();
        if (continueBool == true) { //연속등록일 경우 거래처 검색으로 이동
            showPageCustomerSearch();
        } else { //저장일 경우 일반장부 거래내역으로 이동
            $("#hdnRequireRefreshPageCustomerBookWeight").attr("value", "Y");
            showPageCustomerBookWeight("0");
        }
    }
}

//거래처 판매등록-일반판매 일괄등록
function saveInsertCustomerSaleWeightInsertBatch() {
    var result = "";
    var count = parseInt($("#hdnItemCountCustomerSaleWeightInsertBatch").attr("value"), 10);

    for (var i = 1; i <= count; i++) {
        var saleType = $("#hdnSaleTypeCustomerSaleWeightInsertBatch" + i).attr("value");
        var saleDate = $("#txtSaleDateCustomerSaleWeightInsertBatch").attr("value");
        var itemCode = $("#hdnItemCodeCustomerSaleWeightInsertBatch" + i).attr("value");
        var itemName = $("#hdnItemNameCustomerSaleWeightInsertBatch" + i).attr("value");
        var itemSpec = $("#hdnItemSpecCustomerSaleWeightInsertBatch" + i).attr("value");
        var saleQuantity = deleteComma($("#txtSaleQuantityCustomerSaleWeightInsertBatch" + i).attr("value"));
        var withdrawQuantity = deleteComma($("#txtWithdrawQuantityCustomerSaleWeightInsertBatch" + i).attr("value"));
        var salePrice = deleteComma($("#txtSalePriceCustomerSaleWeightInsertBatch" + i).attr("value"));
        var priceType = $("#hdnPriceTypeCustomerSaleWeightInsertBatch" + i).attr("value");
        var vatType = $("#hdnVatTypeCustomerSaleWeightInsertBatch" + i).attr("value");
        var saleAmount = $("#hdnSaleAmountCustomerSaleWeightInsertBatch" + i).attr("value");
        var taxAmount = $("#hdnTaxAmountCustomerSaleWeightInsertBatch" + i).attr("value");
        var totalAmount = $("#hdnTotalAmountCustomerSaleWeightInsertBatch" + i).attr("value");
        var discountAmount = $("#hdnDiscountAmountCustomerSaleWeightInsertBatch" + i).attr("value");
        var collectAmount = $("#hdnCollectAmountCustomerSaleWeightInsertBatch" + i).attr("value");
        var unpaidAmount = $("#hdnUnpaidAmountCustomerSaleWeightInsertBatch" + i).attr("value");
        var employeeCode = $("#hdnEmployeeCodeCustomerSaleWeightInsertBatch" + i).attr("value");
        var employeeName = $("#hdnEmployeeNameCustomerSaleWeightInsertBatch" + i).attr("value");
        var remark = $("#hdnRemarkTextCustomerSaleWeightInsertBatch" + i).attr("value");
        var collectType = $("#hdnCollectTypeCustomerSaleWeightInsertBatch" + i).attr("value");
        if ((saleQuantity == "0") && (withdrawQuantity == "0")) { //판매수량과 회수수량이 모두 0이면 저장안함
            continue;
        }
        $.ajax({
            url: gasmaxWebappPath + "customer_sale_weight_insert_batch_save_ajx.jsp",
            data: "insertMode=1"
                + "&saleType=" + saleType
                + "&saleDate=" + saleDate
                + "&itemCode=" + itemCode
                + "&itemName=" + itemName
                + "&itemSpec=" + itemSpec
                + "&saleQuantity=" + saleQuantity
                + "&withdrawQuantity=" + withdrawQuantity
                + "&salePrice=" + salePrice
                + "&priceType=" + priceType
                + "&vatType=" + vatType
                + "&saleAmount=" + saleAmount
                + "&taxAmount=" + taxAmount
                + "&totalAmount=" + totalAmount
                + "&discountAmount=" + discountAmount
                + "&collectAmount=" + collectAmount
                + "&unpaidAmount=" + unpaidAmount
                + "&employeeCode=" + employeeCode
                + "&employeeName=" + employeeName
                + "&remark=" + remark
                + "&collectType=" + collectType
            ,
            type: "post",
            async: false,
            dataType: "html",
            timeout: 120000,
            error: function (result) {
                if (result.status == 0) {
                    console.log("서버 응답 지연 (timeout 또는 연결 실패)");
                } else {
                    alert("error occured. Status:" + result.status
                        + " --Status Text:" + result.statusText
                        + " --Error Result:" + result);
                }
            },
            success: function (xml) {
                if ($(xml).find("session").text() == "X") {
                    alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                    showPageIntro(false);
                    return;
                }
                var errorCode = $(xml).find("code").text();
                if (errorCode == "E") {
                    result = $(xml).find("message").text();
                }
            }
        });
    }
    if (result == "") {
        $("#hdnRequireRefreshPageCustomerSaleWeightInsert").attr("value", "Y"); //refresh 하도록 함.
    } else {
        $("#divResultMessageCustomerSaleWeightInsertBatch").html(getResultMessage("저장 중에 오류가 발생하였습니다.", false)).trigger("create");
    }
    return result;
}


//거래처 판매등록-일반판매 상세 화면에서 비고 검색 버튼 클릭
function clickRemarkCustomerSaleWeightInsert() {
    showPageCustomerSaleWeightInsertRemarkSearch();
}

//거래처 판매등록-일반판매 삽입하기
function injectionCustomerSaleWeightInsertItemDetail(tagId, insertMode, itemIndex) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    if (insertMode == undefined) {
        insertMode = "1"; // 1: 신규 0: 수정
    }
    if (itemIndex == undefined) {
        itemIndex = parseInt($("#hdnItemCountCustomerSaleWeightInsertBatch").attr("value"), 10) + 1;
    }
    var dataStr = "";
    var saleDate = $("#txtSaleDateCustomerSaleWeightInsertBatch").attr("value");
    if (insertMode == "0") { // 수정모드일 때
        var saleType = $("#hdnSaleTypeCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var itemCode = $("#hdnItemCodeCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var itemName = $("#hdnItemNameCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var itemSpec = $("#hdnItemSpecCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var priceType = $("#hdnPriceTypeCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var salePrice = $("#txtSalePriceCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var itemBalance = $("#txtItemBalanceCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var saleQuantity = $("#txtSaleQuantityCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var withdrawQuantity = $("#txtWithdrawQuantityCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var nowBalance = $("#txtNowBalanceCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var vatType = $("#hdnVatTypeCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var saleAmount = $("#hdnSaleAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var taxAmount = $("#hdnTaxAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var totalAmount = $("#hdnTotalAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var collectAmount = $("#hdnCollectAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var discountAmount = $("#hdnDiscountAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var unpaidAmount = $("#hdnUnpaidAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var employeeCode = $("#hdnEmployeeCodeCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var employeeName = $("#hdnEmployeeNameCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var remarkCode = $("#hdnRemarkCodeCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var remarkText = $("#hdnRemarkTextCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        var collectType = $("#hdnCollectTypeCustomerSaleWeightInsertBatch" + itemIndex).attr("value");
        dataStr = "insertMode=" + insertMode
            + "&saleDate=" + saleDate
            + "&itemIndex=" + itemIndex
            + "&saleType=" + saleType
            + "&itemCode=" + itemCode
            + "&itemName=" + itemName
            + "&itemSpec=" + itemSpec
            + "&priceType=" + priceType
            + "&salePrice=" + salePrice
            + "&itemBalance=" + itemBalance
            + "&saleQuantity=" + saleQuantity
            + "&withdrawQuantity=" + withdrawQuantity
            + "&nowBalance=" + nowBalance
            + "&vatType=" + vatType
            + "&saleAmount=" + saleAmount
            + "&taxAmount=" + taxAmount
            + "&totalAmount=" + totalAmount
            + "&collectAmount=" + collectAmount
            + "&discountAmount=" + discountAmount
            + "&unpaidAmount=" + unpaidAmount
            + "&employeeCode=" + employeeCode
            + "&employeeName=" + employeeName
            + "&remarkCode=" + remarkCode
            + "&remarkText=" + remarkText
            + "&collectType=" + collectType;
    } else { // 신규 모드일 때
        var vatType = $("#selectDefaultVatTypeCustomerSaleWeightInsertBatch").attr("value");
        var employeeCode = $("#selectDefaultEmployeeCustomerSaleWeightInsertBatch").attr("value");
        var employeeName = $("#hdnDefaultEmployeeNameCustomerSaleWeightInsertBatch").attr("value");
        var remarkCode = $("#selectDefaultRemarkSaleWeightInsertBatch").attr("value");
        var remarkText = $("#txtDefaultRemarkCustomerSaleWeightInsert").attr("value");
        var collectType = $("#selectDefaultCollectTypeCustomerSaleWeightInsertBatch").attr("value");
        dataStr = "insertMode=" + insertMode
            + "&saleDate=" + saleDate
            + "&itemIndex=" + itemIndex
            + "&vatType=" + vatType
            + "&employeeCode=" + employeeCode
            + "&employeeName=" + employeeName
            + "&remarkCode=" + remarkCode
            + "&remarkText=" + remarkText
            + "&collectType=" + collectType;
    }

    $.ajax({
        url: gasmaxWebappPath + "customer_sale_weight_insert_item_detail.jsp",
        data: dataStr,
        type: "post",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//거래처 판매등록-일반판매 품목 클릭하여 품목 검색하여 선택하는 화면으로 이동
function clickItemCustomerSaleWeightInsertItemDetail(insertMode) {
    if (insertMode == "0") { // 수정모드 일때는 품목 선택 변경 불가
        return;
    }
    showPageCustomerSaleWeightInsertItemSearch();
}

//거래처 판매등록-일반판매 판매 구분 변경 시 처리
function changeSaleTypeCustomerSaleWeightInsertItemDetail() {
    var saleType = $("#selectSaleTypeCustomerSaleWeightInsertItemDetail").attr("value");
    if (saleType == "0") { // 가스일 경우 전일, 회수, 잔량 표시
        $("#txtItemBalanceCustomerSaleWeightInsertItemDetail").css("display", "inline-block");
        $("#txtWithdrawQuantityCustomerSaleWeightInsertItemDetail").css("display", "inline-block");
        $("#txtNowBalanceCustomerSaleWeightInsertItemDetail").css("display", "inline-block");
        $("#tdWithdrawCustomerSaleWeightInsertItemDetail").css("background-color", "");

    } else if ((saleType == "1") || (saleType == "2")) { // 용기, 기구일 경우 전일, 회수, 잔량 표시안함
        $("#txtItemBalanceCustomerSaleWeightInsertItemDetail").css("display", "none");
        $("#txtWithdrawQuantityCustomerSaleWeightInsertItemDetail").css("display", "none");
        $("#txtNowBalanceCustomerSaleWeightInsertItemDetail").css("display", "none");
        $("#tdWithdrawCustomerSaleWeightInsertItemDetail").css("background-color", "#DDDDDD");
    }
    $("#hdnItemCodeCustomerSaleWeightInsertItemDetail").attr("value", "");
    $("#hdnItemNameCustomerSaleWeightInsertItemDetail").attr("value", "");
    $("#hdnItemSpecCustomerSaleWeightInsertItemDetail").attr("value", "");
    $("#txtItemCustomerSaleWeightInsertItemDetail").attr("value", "");
    $("#txtSalePriceCustomerSaleWeightInsertItemDetail").attr("value", "0");
    $("#txtSalePriceCustomerSaleWeightInsertItemDetail").trigger("change");
}

//거래처 판매등록-일반판매 납품 수량 입력시 회수수량 동일하게 처리
function changeSaleQuantityCustomerSaleWeightInsertItemDetail() {
    var saleQuantity = parseInt(deleteComma($("#txtSaleQuantityCustomerSaleWeightInsertItemDetail").attr("value")), 10);
    $("#txtWithdrawQuantityCustomerSaleWeightInsertItemDetail").attr("value", insertComma2(saleQuantity));
    calculateTotalAmountCustomerSaleWeightInsertItemDetail();
}

//거래처 판매등록-일반판매 판매 금액 계산 처리
function calculateTotalAmountCustomerSaleWeightInsertItemDetail() {
    var salePrice = parseFloat(deleteComma($("#txtSalePriceCustomerSaleWeightInsertItemDetail").attr("value")), 10);
    //	var itemBalance = parseInt($("#txtItemBalanceCustomerSaleWeightInsertItemDetail").attr("value"), 10);
    var saleQuantity = parseInt(deleteComma($("#txtSaleQuantityCustomerSaleWeightInsertItemDetail").attr("value")), 10);
    var withdrawQuantity = parseInt(deleteComma($("#txtWithdrawQuantityCustomerSaleWeightInsertItemDetail").attr("value")), 10);
    var quantity = saleQuantity - withdrawQuantity;
    //	var nowBalance = itemBalance + quantity;
    var nowBalance = quantity;
    $("#txtNowBalanceCustomerSaleWeightInsertItemDetail").attr("value", insertComma(nowBalance));
    var saleAmount = Math.round(salePrice * saleQuantity);
    var taxAmount = 0;
    var totalAmount = 0;
    var vatType = $("#selectVatTypeCustomerSaleWeightInsertItemDetail").attr("value");
    if (vatType == "0") { // VAT 별도일 때만 부가세 설정
        taxAmount = Math.round(saleAmount / 10);
        totalAmount = saleAmount + taxAmount;
    } else if (vatType == "1") { //VAT 포함일 때 처리
        totalAmount = saleAmount;
        saleAmount = Math.round(totalAmount / 1.1);
        taxAmount = totalAmount - saleAmount;
    } else if (vatType == "2") { //비과세일 경우
        totalAmount = saleAmount;
    }
    $("#txtSaleAmountCustomerSaleWeightInsertItemDetail").attr("value", insertComma(saleAmount));
    $("#txtTaxAmountCustomerSaleWeightInsertItemDetail").attr("value", insertComma(taxAmount));
    $("#txtTotalAmountCustomerSaleWeightInsertItemDetail").attr("value", insertComma(totalAmount));
    var collectType = $("#selectCollectTypeCustomerSaleWeightInsertItemDetail").attr("value");
    var collectAmount = 0;
    var discountAmount = parseInt(deleteComma($("#txtDiscountAmountCustomerSaleWeightInsertItemDetail").attr("value")), 10);
    var unpaidAmount = 0;
    // 0.현금, 2.예금, 3. 카드, 4.어음, B.현금영수증: 입금액=합계금액, D/C = 0, 미입금액=0
    // A.외상: 입금액 = 0,  D/C = 0, 미입금액 =  합계금액
    // [2017.11.01][Rev3] 예금도 현금과 동일하게 처리
    if ((collectType == "0") || (collectType == "2") || (collectType == "3") || (collectType == "4") || (collectType == "B")) {
        collectAmount = totalAmount - discountAmount;
    } else if (collectType == "A") {
        unpaidAmount = totalAmount - discountAmount;
    }
    $("#txtCollectAmountCustomerSaleWeightInsertItemDetail").attr("value", insertComma2(collectAmount));
    $("#txtUnpaidAmountCustomerSaleWeightInsertItemDetail").attr("value", insertComma(unpaidAmount));
}

//거래처 판매등록-일반판매 미입금액 계산 처리
function calculateUnpaidAmountCustomerSaleWeightInsertItemDetail() {
    var totalAmount = parseInt(deleteComma($("#txtTotalAmountCustomerSaleWeightInsertItemDetail").attr("value")), 10);
    var collectAmount = parseInt(deleteComma($("#txtCollectAmountCustomerSaleWeightInsertItemDetail").attr("value")), 10);
    var discountAmount = parseInt(deleteComma($("#txtDiscountAmountCustomerSaleWeightInsertItemDetail").attr("value")), 10);
    var unpaidAmount = 0;
    var collectType = $("#selectCollectTypeCustomerSaleWeightInsertItemDetail").attr("value");
    // 0.현금, 2.예금, 3. 카드, 4.어음, B.현금영수증: 입금액=합계금액, D/C = 0, 미입금액=0
    // A.외상: 입금액 = 0,  D/C = 0, 미입금액 =  합계금액
    // [2017.11.01][Rev3] 예금도 현금과 동일하게 처리
    if ((collectType == "0") || (collectType == "2") || (collectType == "3") || (collectType == "4") || (collectType == "B")) {
        collectAmount = totalAmount - discountAmount;
    } else if (collectType == "A") {
        unpaidAmount = totalAmount - collectAmount - discountAmount;
    }
    $("#txtCollectAmountCustomerSaleWeightInsertItemDetail").attr("value", insertComma2(collectAmount));
    $("#txtUnpaidAmountCustomerSaleWeightInsertItemDetail").attr("value", insertComma(unpaidAmount));
}

//거래처 판매등록-일반판매 판매 금액 계산 처리
function changeCollectTypeCustomerSaleWeightInsertItemDetail() {
    var totalAmount = parseInt(deleteComma($("#txtTotalAmountCustomerSaleWeightInsertItemDetail").attr("value")));
    var collectType = $("#selectCollectTypeCustomerSaleWeightInsertItemDetail").attr("value");
    var collectAmount = 0;
    var discountAmount = 0;
    var unpaidAmount = 0;
    // 0.현금, 2.예금, 3. 카드, 4.어음, B.현금영수증: 입금액=합계금액, D/C = 0, 미입금액=0
    // A.외상: 입금액 = 0,  D/C = 0, 미입금액 =  합계금액
    // [2017.11.01][Rev3] 예금도 현금과 동일하게 처리
    if ((collectType == "0") || (collectType == "2") || (collectType == "3") || (collectType == "4") || (collectType == "B")) {
        collectAmount = totalAmount;
    } else if (collectType == "A") {
        unpaidAmount = totalAmount;
    }
    $("#txtCollectAmountCustomerSaleWeightInsertItemDetail").attr("value", insertComma2(collectAmount));
    $("#txtDiscountAmountCustomerSaleWeightInsertItemDetail").attr("value", insertComma2(discountAmount));
    $("#txtUnpaidAmountCustomerSaleWeightInsertItemDetail").attr("value", insertComma(unpaidAmount));
}

// 거래처 판매등록 - 사원 변경 처리
function changeEmployeeCustomerSaleWeightInsertItemDetail() {
    $("#hdnEmployeeNameCustomerSaleWeightInsertItemDetail").attr("value", $("#selectEmployeeCustomerSaleWeightInsertItemDetail").attr("value"));
}

//거래처 판매등록-일반판매 품목 저장버튼 처리
function clickSaveCustomerSaleWeightInsertItemDetail() {
    if (!hasPermission("hdnMenuPermissionSale", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    var insertMode = $("#hdnInsertModeCustomerSaleWeightInsertItemDetail").attr("value"); // 0:수정 1:신규
    var itemIndex = $("#hdnItemIndexCustomerSaleWeightInsertItemDetail").attr("value");
    var saleType = $("#selectSaleTypeCustomerSaleWeightInsertItemDetail").attr("value");
    var displayStyle = "";
    var tdStyle = "";
    if ((saleType == "1") || (saleType == "2")) {
        displayStyle = ' display: none ; ';
        tdStyle = " background-color: #DDDDDD ; ";
    }
    var saleTypeName = "";
    if (saleType == "1") {
        saleTypeName = '[용기]';
    } else if (saleType == "2") {
        saleTypeName = '[기구]';
    }
    var itemCode = $("#hdnItemCodeCustomerSaleWeightInsertItemDetail").attr("value");
    if (itemCode == "null") {
        alert("품목을 먼저 선택하세요.");
        return;
    }
    var itemName = $("#hdnItemNameCustomerSaleWeightInsertItemDetail").attr("value");
    var itemSpec = $("#hdnItemSpecCustomerSaleWeightInsertItemDetail").attr("value");
    var salePrice = $("#txtSalePriceCustomerSaleWeightInsertItemDetail").attr("value");
    var priceType = "";
    var itemBalance = $("#txtItemBalanceCustomerSaleWeightInsertItemDetail").attr("value");
    var saleQuantity = $("#txtSaleQuantityCustomerSaleWeightInsertItemDetail").attr("value");
    var withdrawQuantity = $("#txtWithdrawQuantityCustomerSaleWeightInsertItemDetail").attr("value");
    var nowBalance = $("#txtNowBalanceCustomerSaleWeightInsertItemDetail").attr("value");
    var vatType = $("#selectVatTypeCustomerSaleWeightInsertItemDetail").attr("value");
    var saleAmount = deleteComma($("#txtSaleAmountCustomerSaleWeightInsertItemDetail").attr("value"));
    var taxAmount = deleteComma($("#txtTaxAmountCustomerSaleWeightInsertItemDetail").attr("value"));
    var totalAmount = deleteComma($("#txtTotalAmountCustomerSaleWeightInsertItemDetail").attr("value"));
    var collectType = $("#selectCollectTypeCustomerSaleWeightInsertItemDetail").attr("value");
    var collectAmount = deleteComma($("#txtCollectAmountCustomerSaleWeightInsertItemDetail").attr("value"));
    var discountAmount = deleteComma($("#txtDiscountAmountCustomerSaleWeightInsertItemDetail").attr("value"));
    var unpaidAmount = deleteComma($("#txtUnpaidAmountCustomerSaleWeightInsertItemDetail").attr("value"));
    var employeeCode = $("#selectEmployeeCustomerSaleWeightInsertItemDetail").attr("value");
    var employeeName = $("#hdnEmployeeNameCustomerSaleWeightInsertItemDetail").attr("value");
    var remarkCode = $("#selectRemarkSaleWeightInsertItemDetail").attr("value");
    var remarkText = $("#selectRemarkSaleWeightInsertItemDetail option:selected").text();
    if (remarkCode == "") {
        remarkText = $("#txtRemarkCustomerSaleWeightInsertItemDetail").attr("value");
    }

    if (insertMode == "0") { // 수정모드일때
        $("#hdnVatTypeCustomerSaleWeightInsertBatch" + itemIndex).attr("value", vatType);
        $("#hdnSaleAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value", saleAmount);
        $("#hdnTaxAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value", taxAmount);
        $("#hdnTotalAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value", totalAmount);
        $("#hdnCollectAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value", collectAmount);
        $("#hdnDiscountAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value", discountAmount);
        $("#hdnUnpaidAmountCustomerSaleWeightInsertBatch" + itemIndex).attr("value", unpaidAmount);
        $("#hdnEmployeeCodeCustomerSaleWeightInsertBatch" + itemIndex).attr("value", employeeCode);
        $("#hdnEmployeeNameCustomerSaleWeightInsertBatch" + itemIndex).attr("value", employeeName);
        $("#hdnRemarkCodeCustomerSaleWeightInsertBatch" + itemIndex).attr("value", remarkCode);
        $("#hdnRemarkTextCustomerSaleWeightInsertBatch" + itemIndex).attr("value", remarkText);
        $("#hdnCollectTypeCustomerSaleWeightInsertBatch" + itemIndex).attr("value", collectType);
        $("#txtSalePriceCustomerSaleWeightInsertBatch" + itemIndex).attr("value", salePrice);
        $("#txtItemBalanceCustomerSaleWeightInsertBatch" + itemIndex).attr("value", itemBalance);
        $("#txtSaleQuantityCustomerSaleWeightInsertBatch" + itemIndex).attr("value", saleQuantity);
        $("#txtWithdrawQuantityCustomerSaleWeightInsertBatch" + itemIndex).attr("value", withdrawQuantity);
        $("#txtNowBalanceCustomerSaleWeightInsertBatch" + itemIndex).attr("value", nowBalance);
    } else { // 신규모드 일때
        var footerName = "'footersCustomerSaleWeightInsert'";
        var eventHtml = 'onclick="focusNumber(this, ' + footerName + ')" onfocus="disableFixed(' + footerName + ')" onblur="blurNumber(this, ' + footerName + ')"';
        var html = '<tr id="trItemCustomerSaleWeightInsertBatch' + itemIndex + '" style="height: 40px ; ">'
            + '    <td style="text-align: left ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">'
            + '       <a href="#" onclick="clickItemCustomerSaleWeightInsertBatch(' + itemIndex + ')">' + saleTypeName + itemName + ' ' + itemSpec + '</a>'
            + '<input type="hidden" id="hdnSaleTypeCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + saleType + '" />'
            + '<input type="hidden" id="hdnItemCodeCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + itemCode + '" />'
            + '<input type="hidden" id="hdnItemNameCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + itemName + '" />'
            + '<input type="hidden" id="hdnItemSpecCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + itemSpec + '" />'
            + '<input type="hidden" id="hdnPriceTypeCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + priceType + '" />'
            + '<input type="hidden" id="hdnVatTypeCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + vatType + '" />'
            + '<input type="hidden" id="hdnSaleAmountCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + saleAmount + '" />'
            + '<input type="hidden" id="hdnTaxAmountCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + taxAmount + '" />'
            + '<input type="hidden" id="hdnTotalAmountCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + totalAmount + '" />'
            + '<input type="hidden" id="hdnCollectAmountCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + collectAmount + '" />'
            + '<input type="hidden" id="hdnDiscountAmountCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + discountAmount + '" />'
            + '<input type="hidden" id="hdnUnpaidAmountCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + unpaidAmount + '" />'
            + '<input type="hidden" id="hdnEmployeeCodeCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + employeeCode + '" />'
            + '<input type="hidden" id="hdnEmployeeNameCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + employeeName + '" />'
            + '<input type="hidden" id="hdnRemarkCodeCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + remarkCode + '" />'
            + '<input type="hidden" id="hdnRemarkTextCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + remarkText + '" />'
            + '<input type="hidden" id="hdnCollectTypeCustomerSaleWeightInsertBatch' + itemIndex + '" value="' + collectType + '" />'
            + '</td>'
            + '<td style="width: 60px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">'
            + ' <input type="number"  data-role="none" id="txtSalePriceCustomerSaleWeightInsertBatch' + itemIndex + '" data-mini="true" value="' + deleteComma(salePrice) + '" style="width: 80% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; "  onchange="calculateTotalAmountCustomerSaleWeightInsertBatch(' + itemIndex + ')" ' + eventHtml + ' />'
            + '</td>'
            + '<td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">'
            + ' <input type="text"  data-role="none" id="txtItemBalanceCustomerSaleWeightInsertBatch' + itemIndex + '" data-mini="true" value="' + itemBalance + '" readonly style="width: 80% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; ' + displayStyle + '" />'
            + '</td>'
            + '<td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">'
            + ' <input type="number"  data-role="none" id="txtSaleQuantityCustomerSaleWeightInsertBatch' + itemIndex + '" data-mini="true" value="' + deleteComma(saleQuantity) + '" style="width: 80% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; "  onchange="changeSaleQuantityCustomerSaleWeightInsertBatch(' + itemIndex + ')" ' + eventHtml + ' />'
            + '</td>'
            + '<td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ' + tdStyle + '">'
            + ' <input type="number"  data-role="none" id="txtWithdrawQuantityCustomerSaleWeightInsertBatch' + itemIndex + '" data-mini="true" value="' + deleteComma(withdrawQuantity) + '" style="width: 80% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; ' + displayStyle + '"  onchange="calculateTotalAmountCustomerSaleWeightInsertBatch(' + itemIndex + ')"  ' + eventHtml + ' />'
            + '</td>'
            + '<td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">'
            + ' <input type="text"  data-role="none" id="txtNowBalanceCustomerSaleWeightInsertBatch' + itemIndex + '" data-mini="true" value="' + nowBalance + '" readonly style="width: 80% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; ' + displayStyle + '" />'
            + '</td>'
            + '</tr>';
        $("#tbItemListCustomerSaleWeightInsertBatch").append(html).trigger("create");
        $("#hdnItemCountCustomerSaleWeightInsertBatch").attr("value", itemIndex);
    }
    calculateTotalAmountCustomerSaleWeightInsertBatch(itemIndex); // 다시 계산하기
    showPageCustomerSaleWeightInsert();
}

//거래처 판매등록-일반판매 품목 삭제버튼 처리
function clickDeleteCustomerSaleWeightInsertItemDetail() {
    if (!hasPermission("hdnMenuPermissionSale", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    var itemIndex = $("#hdnItemIndexCustomerSaleWeightInsertItemDetail").attr("value");
    $("#txtSaleQuantityCustomerSaleWeightInsertBatch" + itemIndex).attr("value", "0");
    $("#txtWithdrawQuantityCustomerSaleWeightInsertBatch" + itemIndex).attr("value", "0");
    $("#txtSaleQuantityCustomerSaleWeightInsertBatch" + itemIndex).trigger("change");
    $("#trItemCustomerSaleWeightInsertBatch" + itemIndex).css("display", "none");
    showPageCustomerSaleWeightInsert();
}

//거래처 판매등록-일반판매 품목 검색 화면 삽입하기
function injectionCustomerSaleWeightInsertItemSearch(tagId) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "customer_sale_weight_insert_item_search.jsp",
        data: "saleType=" + $("#selectSaleTypeCustomerSaleWeightInsertItemDetail").attr("value")
            + "&keyword=" + $("#txtItemCustomerSaleWeightInsertItemDetail").attr("value")
        ,
        type: "post",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//거래처 판매등록-일반판매 비고 검색 화면 삽입하기
function injectionCustomerSaleWeightInsertRemarkSearch(tagId) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    var customerCode = $("#hdnCustomerCodeManageCidEdit").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_sale_weight_insert_remark_search.jsp",
        data: "customerCode=" + customerCode,
        type: "post",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//거래처 판매등록-일반판매 비고 검색 화면 비고 클릭하기
function clickCustomerSaleWeightInsertRemarkSearch(remark) {
    $("#txtRemarkCustomerSaleWeightInsert").attr("value", remark);
    $("#txtRemarkCustomerSaleWeightInsert").trigger("change");
    //닫기
    showPageCustomerSaleWeightInsert();
}

//거래처 판매등록-일반판매 상세 화면에서 비고 검색 버튼 클릭
function clickRemarkCustomerSaleWeightInsertItemDetail() {
    showPageCustomerSaleWeightInsertItemDetailRemarkSearch();
}

//거래처 판매등록-일반판매 품목 검색 화면에서 품목 선택 클릭
function clickItemCustomerSaleWeightInsertItemSearch(itemCode) {
    //품목 설정
    $("#hdnItemCodeCustomerSaleWeightInsertItemDetail").attr("value", itemCode);
    var itemName = $("#hdnItemNameCustomerSaleWeightInsertItemSearch" + itemCode).attr("value");
    $("#hdnItemNameCustomerSaleWeightInsertItemDetail").attr("value", itemName);
    var itemSpec = $("#hdnItemSpecCustomerSaleWeightInsertItemSearch" + itemCode).attr("value");
    $("#hdnItemSpecCustomerSaleWeightInsertItemDetail").attr("value", itemSpec);
    $("#txtItemCustomerSaleWeightInsertItemDetail").attr("value", itemName + " " + itemSpec);
    var salePrice = $("#hdnSalePriceCustomerSaleWeightInsertItemSearch" + itemCode).attr("value");
    $("#txtSalePriceCustomerSaleWeightInsertItemDetail").attr("value", insertComma2(salePrice));
    $("#txtSalePriceCustomerSaleWeightInsertItemDetail").trigger("change"); // 단가 변경 이벤트 자동 처리
    //닫기
    showPageCustomerSaleWeightInsertItemDetail('', '', false);

}

//거래처 판매등록-일반판매 고압 품목 상세 비고 검색 화면 삽입하기
function injectionCustomerSaleWeightInsertItemDetailRemarkSearch(tagId) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    var customerCode = $("#hdnCustomerCodeManageCidEdit").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_sale_weight_insert_item_detail_remark_search.jsp",
        data: "customerCode=" + customerCode,
        type: "post",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//거래처 판매등록-일반판매 고압 품목 상세 비고 검색 화면 비고 클릭하기
function clickCustomerSaleWeightInsertItemDetailRemarkSearch(remark) {
    $("#txtRemarkCustomerSaleWeightInsertItemDetail").attr("value", remark);
    //닫기
    showPageCustomerSaleWeightInsertItemDetail('', '', false);
}

//거래처 판매등록-체적공급 검색 조건 삽입하기
function injectionCustomerSaleVolumeInsert(tagId) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "customer_sale_volume_insert.jsp?uuid=" + window.sessionStorage.uuid,
        type: "post",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
    $("#hdnRequireRefreshPageCustomerSaleVolumeInsert").attr("value", "N"); //거래처 판매등록-체적공급이 이제 Refresh되었음을 표시
}

//거래처 판매등록-체적공급 품목코드 선택 변경 시
function changeItemCodeCustomerSaleVolume() {
    $("#hdnItemCapacityCustomerSaleVolumeInsert").attr("value", $("#selectCustomerItemCustomerSaleVolumeInsert > option:selected").attr("value2"));
    $("#hdnItemNameCustomerSaleVolumeInsert").attr("value", $("#selectCustomerItemCustomerSaleVolumeInsert > option:selected").attr("value3"));
    $("#hdnItemPriceCustomerSaleVolumeInsert").attr("value", $("#selectCustomerItemCustomerSaleVolumeInsert > option:selected").attr("value4"));
    $("#hdnLastSaleQuantityCustomerSaleVolumeInsert").attr("value", $("#selectCustomerItemCustomerSaleVolumeInsert > option:selected").attr("value5"));
    $("#hdnItemBalanceCustomerSaleVolumeInsert").attr("value", $("#selectCustomerItemCustomerSaleVolumeInsert > option:selected").attr("value6"));
    var lastSaleQuantity = parseInt($("#hdnLastSaleQuantityCustomerSaleVolumeInsert").attr("value"), 10);
    var itemBalance = parseInt($("#hdnItemBalanceCustomerSaleVolumeInsert").attr("value"), 10);
    $("#txtPreBalanceCustomerSaleVolumeInsert").attr("value", insertComma(itemBalance));
    $("#txtSaleQuantityCustomerSaleVolumeInsert").attr("value", insertComma2(lastSaleQuantity));
    $("#txtWithdrawQuantityCustomerSaleVolumeInsert").attr("value", insertComma2(lastSaleQuantity));
    $("#txtNowBalanceCustomerSaleVolumeInsert").attr("value", "0");
    $("#txtRemainQuantityCustomerSaleVolumeInsert").attr("value", "0");
    $("#txtSaleQuantityCustomerSaleVolumeInsert").trigger("change");
}

//거래처 판매등록-체적공급 납품 수량과 회수 수량 동일하게 처리
function changeSaleQuantityCustomerSaleVolume() {
    var saleQuantity = parseInt(deleteComma($("#txtSaleQuantityCustomerSaleVolumeInsert").attr("value")), 10);
    $("#txtWithdrawQuantityCustomerSaleVolumeInsert").attr("value", insertComma2(saleQuantity));
    calculateSaleQuantityCustomerSaleVolume();
}

//거래처 판매등록-체적공급 공급수량 변경 공급량 처리
function calculateSaleQuantityCustomerSaleVolume() {
    var itemCapacity = parseInt($("#hdnItemCapacityCustomerSaleVolumeInsert").attr("value"), 10);						// 품목용량
    var preBalance = parseInt(deleteComma($("#txtPreBalanceCustomerSaleVolumeInsert").attr("value")), 10);				// 전재고
    var saleQuantity = parseInt(deleteComma($("#txtSaleQuantityCustomerSaleVolumeInsert").attr("value")), 10);			// 공급(남품수량)
    var withdrawQuantity = parseInt(deleteComma($("#txtWithdrawQuantityCustomerSaleVolumeInsert").attr("value")), 10);	// 회수(회수수량)
    var remainBalance = preBalance + saleQuantity - withdrawQuantity;													// 현재고 = 전재고 + 공급 - 회수
    $("#txtNowBalanceCustomerSaleVolumeInsert").attr("value", insertComma(remainBalance));								// 현재고
    var itemPrice = parseInt($("#hdnItemPriceCustomerSaleVolumeInsert").attr("value"), 10);								// 품목단가
    var saleVolume = itemCapacity * saleQuantity;																		// 공급량 = 공급(남품수량) * 용량
    //var saleAmount = saleVolume * itemPrice;																			// 기존)공급액 = 공급량(공급수량*용량) * 단가(품목단가)
    var saleAmount = saleQuantity * itemPrice;																			// 수정)공급액 = 공급수량 * 단가(품목단가)
    $("#txtSaleVolumeCustomerSaleVolumeInsert").attr("value", insertComma(saleVolume));									// 공급량
    $("#hdnSaleAmountCustomerSaleVolumeInsert").attr("value", saleAmount);												// 공급액
    $("#txtRemainQuantityCustomerSaleVolumeInsert").trigger("change");
}

//거래처 판매등록-체적공급 잔량 변경 공급후잔량 처리
function calculateRemainAfterSaleQuantityCustomerSaleVolume() {
    var remainQuantity = parseInt(deleteComma($("#txtRemainQuantityCustomerSaleVolumeInsert").attr("value")), 10);
    var saleVolume = parseInt(deleteComma($("#txtSaleVolumeCustomerSaleVolumeInsert").attr("value")), 10);
    var remainAfterSaleQuantity = remainQuantity + saleVolume;
    $("#txtRemainAfterSaleQuantityCustomerSaleVolumeInsert").attr("value", insertComma(remainAfterSaleQuantity));
}

//거래처 체적공급 사원 변경 처리
function changeEmployeeCustomerSaleVolumeInsert() {
    //$("#hdnEmployeeNameCustomerSaleVolumeInsert").attr("value", $("#selectEmployeeCustomerSaleVolumeInsert").attr("value"));
    $("#hdnEmployeeNameCustomerSaleVolumeInsert").attr("value", $("#selectEmployeeCustomerSaleVolumeInsert option:selected").text());
}

//거래처 체적공급 등록 저장 버튼 처리
function clickSaveInsertCustomerSaleVolumeInsert(continueYesNo) {
    if (!hasPermission("hdnMenuPermissionSale", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    // 사원선택 확인
    var employeeCode = $("#selectEmployeeCustomerSaleVolumeInsert").attr("value");
    if (employeeCode == "") {
        alert("사원을 선택해 주세요.");
        return;
    }

    $("#divResultMessageCustomerSaleVolumeInsert").html(getResultMessage("저장 중입니다.", true));
    saveCustomerSaleVolume(true);
    if (continueYesNo == true) { // 연속저장이라면, 저장 후 신규 등록할 수 있도록 거래처 검색 화면으로 이동
        showPageCustomerSearch();
    } else { // 연속저장이 아니라면, 저장 후 체적장부의 공급내역으로 이동
        $("#hdnRequireRefreshPageCustomerBookVolume").attr("value", "Y"); // refresh 하도록 설정
        showPageCustomerBookVolume("2");
    }
}

//거래처 공급등록 - 신규등록 저장버튼 처리.
function saveCustomerSaleVolume(insertYesNo) {
    var key = "";
    $("#divResultMessageCustomerSaleVolumeInsert").html(getResultMessage("저장 중입니다.", true));
    var insertMode = "0"; // 수정 저장
    if (insertYesNo == true) { // 신규등록 저장
        $("#divResultMessageCustomerSaleVolumeInsert").html(getResultMessage("저장 중입니다.", true)).trigger("create");
        insertMode = "1";
    }
    $.ajax({
        url: gasmaxWebappPath + "customer_sale_volume_insert_save_ajx.jsp",
        type: "post",
        data: "insertMode=" + insertMode
            + "&customerCode=" + $("#hdnCurrentCustomerCode").attr("value")
            + "&saleDate=" + $("#txtSaleDateCustomerSaleVolumeInsert").attr("value")						// 판매일자(공급일자)
            + "&buildingName=" + $("#hdnBuildingNameCustomerSaleVolumeInsert").attr("value")				// 건물명
            + "&itemCode=" + $("#selectCustomerItemCustomerSaleVolumeInsert").attr("value")					// 품목코드
            + "&itemName=" + $("#selectCustomerItemCustomerSaleVolumeInsert option:selected").text()		// 품목명
            + "&itemCapacity=" + $("#hdnItemCapacityCustomerSaleVolumeInsert").attr("value")				// 품목용량
            + "&salePrice=" + $("#hdnItemPriceCustomerSaleVolumeInsert").attr("value")						// 품목단가
            + "&saleQuantity=" + $("#txtSaleQuantityCustomerSaleVolumeInsert").attr("value")				// 공급(남품수량)
            + "&withdrawQuantity=" + $("#txtWithdrawQuantityCustomerSaleVolumeInsert").attr("value")		// 회수(회수수량)
            + "&saleAmount=" + $("#hdnSaleAmountCustomerSaleVolumeInsert").attr("value")					// 공급액
            + "&employeeCode=" + $("#selectEmployeeCustomerSaleVolumeInsert").attr("value")					// 사원코드
            + "&employeeName=" + $("#hdnEmployeeNameCustomerSaleVolumeInsert").attr("value")				// 사원명
            + "&cubicPrice=" + $("#hdnCubicPriceCustomerSaleVolumeInsert").attr("value")					// 개별단가(환경단가/할인단가)(루베단가)
            + "&readMeterQuantity=" + $("#txtReadMeterQuantityCustomerSaleVolumeInsert").attr("value")		// 배달검침
            + "&remainQuantity=" + $("#txtRemainQuantityCustomerSaleVolumeInsert").attr("value")			// 잔량
            + "&remark=" + $("#txtRemarkCustomerSaleVolumeInsert").attr("value")							// 비고
            + "&uuid=" + device.uuid						// 비고
        ,
        dataType: "xml",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultManageSaleList").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                html = getResultMessage("저장이 완료되었습니다.", false);
            }
            if (insertMode == true) {
                $("#divResultMessageCustomerSaleVolumeInsert").html(html).trigger("create");
            } else {

            }
        }
    });
    return key;
}

//거래처 검침등록 검색 조건 삽입하기
function injectionCustomerReadMeterInsert(tagId, readMeterDate) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    $("#divResultMessageCustomerReadMeterInsert").html("").trigger("create");
    $("#divResultMessageCustomerReadMeterEdit").html("").trigger("create");
    var dataStr = "";
    if (readMeterDate == undefined) {
    } else {
        dataStr = "readMeterDate=" + readMeterDate;
    }
    $.ajax({
        url: gasmaxWebappPath + "customer_read_meter_edit.jsp?uuid=" + window.sessionStorage.uuid,
        data: dataStr,
        type: "post",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
            calculateThisMonthAmountCustomerReadMeterEdit();
        }
    });

    $("#hdnRequireRefreshPageCustomerReadMeterInsert").attr("value", "N"); //거래처 검침등록이 이제 Refresh되었음을 표시
}

//거래처 검침등록 - 검침 일자 변경 시 사용기간 변경 처리
function changeReadMeterDateCustomerReadMeterEdit(inputName) {
    var startDate = $("#hdnStartDateCustomerReadMeterEdit").attr("value");
    var endDate = $("#" + inputName).attr("value");
    if (startDate > endDate) {
        alert("사용기간 시작일이 검침일자 이전이어야 합니다.");
        endDate = getToday("-");
        $("#" + inputName).attr("value", endDate);
    }
    $("#txtUsePeriodCustomerReadMeterEdit").attr("value", startDate + " ~ " + endDate);
}

//거래처 검침등록 - 연체적용일자 변경 시 다시 화면 갱신하기
function changeApplyDelayDateCustomerReadMeterEdit() {
    var applyDelayDate = $("#txtApplyDelayDateCustomerReadMeterInsert").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_read_meter_edit_reload_ajx.jsp",
        data: "readMeterDate=" + applyDelayDate,
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var delayFeePercent = parseInt($(xml).find("delayFeePercent").text(), 10);
            var delayFeeMethodType = $(xml).find("delayFeeMethodType").text();
            var delayAmount1 = parseInt($(xml).find("delayAmount1").text(), 10);
            var delayAmount2 = parseInt($(xml).find("delayAmount2").text(), 10);
            var delayAmount3 = parseInt($(xml).find("delayAmount3").text(), 10);
            var delayAmount4 = parseInt($(xml).find("delayAmount4").text(), 10);
            var delayAmount5 = parseInt($(xml).find("delayAmount5").text(), 10);
            var delayAmount = 0;
            if (delayFeeMethodType == "0") {
            } else if (delayFeeMethodType == "1") {
                delayAmount = Math.floor(delayAmount1 * delayFeePercent / 100);
            } else if (delayFeeMethodType == "2") {
                delayAmount = Math.floor(delayAmount2 * delayFeePercent / 100);
            } else if (delayFeeMethodType == "3") {
                delayAmount = Math.floor(delayAmount3 * delayFeePercent / 100);
            } else if (delayFeeMethodType == "4") {
                delayAmount = Math.floor(delayAmount4 * delayFeePercent / 100);
            } else if (delayFeeMethodType == "5") {
                delayAmount = Math.floor(delayAmount5 * delayFeePercent / 100);
            }
            $("#txtDelayAmountCustomerReadMeterEdit").attr("value", insertComma2(delayAmount));
            $("#hdnDelayAmountCustomerReadMeterEdit").attr("value", insertComma(delayAmount));
            $("#txtDelayAmountCustomerReadMeterEdit").trigger("change");
        }
    });
}

//거래처 검침등록 - 당검 변경시 사용량 변경처리
function calculateUseQuantityCustomerReadMeterEdit() {
    var thisMonthReadMeter = parseInt(deleteComma($("#txtThisMonthReadMeterCustomerReadMeterEdit").attr("value")), 10);
    var preReadMeter = parseInt(deleteComma($("#txtPreReadMeterCustomerReadMeterEdit").attr("value")), 10);
    var useQuantity = thisMonthReadMeter - preReadMeter;
    if (useQuantity < 0) {
        alert("당검은 전검보다 적을 수 없습니다.");
        $("#txtThisMonthReadMeterCustomerReadMeterEdit").attr("value", insertComma2(preReadMeter));
        useQuantity = 0;
    }
    $("#txtUseQuantityCustomerReadMeterEdit").attr("value", insertComma(useQuantity));
    $("#txtUseQuantityCustomerReadMeterEdit").trigger("change");		// 트리거로 사용량 변경 이벤트  호출
}

//거래처 검침등록 - 단가 및 사용량 변경시 사용금액 변경처리
function calculateUseAmountCustomerReadMeterEdit() {
    var useAmount = parseInt(deleteComma($("#txtUseQuantityCustomerReadMeterEdit").attr("value")), 10) * parseInt(deleteComma($("#txtPriceCustomerReadMeterEdit").attr("value")), 10);
    $("#txtUseAmountCustomerReadMeterEdit").attr("value", insertComma(useAmount));
    $("#txtUseAmountCustomerReadMeterEdit").trigger("change");		// 트리거로 사용료 변경 이벤트 호출
}

//거래처 검침등록 - 사용금액(기본료를 감안하여) 계산하여 가져오기
function getUseAmountCustomerReadMeterEdit() {
    var defaultAmountYesNo = $("#hdnDefaultAmountYesNoCustomerReadMeterEdit").attr("value");
    var defaultUse = parseInt($("#hdnDefaultUseCustomerReadMeterEdit").attr("value"), 10);
    var useQuantity = parseFloat(deleteComma($("#txtUseQuantityCustomerReadMeterEdit").attr("value")));		// 사용량
    var useAmount = parseFloat(deleteComma($("#txtUseAmountCustomerReadMeterEdit").attr("value")));			// 사용료
    if (defaultAmountYesNo == "Y") {
        if (defaultUse > useQuantity) { // 기본 사용량 보다 적게 사용했다면 기본료 적용
            useAmount = parseFloat(deleteComma($("#txtDefaultAmountCustomerReadMeterEdit").attr("value")));
        }
    }
    return useAmount;
}

//거래처 검침등록 - 원단위 처리방식에 따른 할인료 계산 roundType - true:반올림, false:절사
function getDiscountAmountCustomerReadMeterEdit(roundType) {
    var useAmount = getUseAmountCustomerReadMeterEdit();
    var discountPercent = parseFloat($("#hdnDiscountPercentCustomerReadMeterEdit").attr("value"));
    var discountAmount = 0;
    if (roundType == true) {
        discountAmount = Math.round(useAmount * (discountPercent / 1000)) * 10;
    } else {
        discountAmount = Math.floor(useAmount * (discountPercent / 1000)) * 10;
    }
    return discountAmount;
}

//거래처 검침등록 - 단가 및 사용량 변경시 사용금액 변경처리
function calculateDiscountAmountCustomerReadMeterEdit(roundType) {
    var discountAmount = getDiscountAmountCustomerReadMeterEdit(false);
    $("#txtDiscountAmountCustomerReadMeterEdit").attr("value", discountAmount);
    $("#txtDiscountAmountCustomerReadMeterEdit").trigger("change");		// 트리거로 할인액 변경 이벤트 호출
}

//거래처 검침등록 - 관리비, 연체료, 할인액 변경시 당월 금액, 청구금액 변경처리
function calculateThisMonthAmountCustomerReadMeterEdit() {
    var roundType = $("#hdnRoundTypeCustomerReadMeterEdit").attr("value");
    var useAmount = getUseAmountCustomerReadMeterEdit(); // 사용금액
    var managementAmount = parseFloat(deleteComma($("#txtManagementAmountCustomerReadMeterEdit").attr("value")), 10); // 관리금액
    var discountAmount = parseInt(deleteComma($("#txtDiscountAmountCustomerReadMeterEdit").attr("value")), 10); // 할인액
    var delayAmount = parseInt(deleteComma($("#txtDelayAmountCustomerReadMeterEdit").attr("value"), 10)); // 연체료
    var preUnpaidAmount = parseInt(deleteComma($("#txtPreUnpaidAmountCustomerReadMeterEdit").attr("value"), 10)); // 전미수
    var thisMonthAmount = useAmount + managementAmount - discountAmount + delayAmount;
    if (roundType == "1") { //당월합계 원단위 절사
        thisMonthAmount = Math.floor(thisMonthAmount / 10) * 10;
    } else if (roundType == "2") { //당월합계 원단위 반올림
        thisMonthAmount = Math.round(thisMonthAmount / 10) * 10;
    } else if (roundType == "3") { //항목별 절사
        thisMonthAmount = Math.floor(useAmount / 10) * 10 + Math.floor(managementAmount / 10) * 10 - Math.floor(discountAmount / 10) * 10 + Math.floor(delayAmount / 10) * 10;
    } else if (roundType == "4") { //항목별 반올림
        thisMonthAmount = Math.round(useAmount / 10) * 10 + Math.round(managementAmount / 10) * 10 - Math.round(discountAmount / 10) * 10 + Math.round(delayAmount / 10) * 10;
    }
    var totalAmount = preUnpaidAmount + thisMonthAmount;
    $("#txtThisMonthAmountCustomerReadMeterEdit").attr("value", insertComma(thisMonthAmount));
    $("#txtTotalAmountCustomerReadMeterEdit").attr("value", insertComma(totalAmount));
}

//거래처 체적검침 등록 저장 버튼 처리
function clickSaveInsertCustomerReadMeterEdit(continueYesNo) {
    if (!hasPermission("hdnMenuPermissionReadMeter", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    var readMeterYearMonth = $("#txtReadMeterYearMonthCustomerReadMeterEdit").attr("value");
    if (!isValidateFormat(readMeterYearMonth, 'yyyy-MM')) {
        alert("검침년월을 형식에 맟게 입력해 주세요.(yyyy-MM)");
        return;
    }

    $("#divResultMessageCustomerReadMeterInsert").html(getResultMessage("저장 중입니다.", true));
    //	var key = saveCustomerReadMeter(true);
    saveCustomerReadMeter(true);
    if (continueYesNo == true) { // 연속저장이라면, 저장 후 신규 등록할 수 있도록 거래처 검색 화면으로 이동
        showPageCustomerSearch();
    } else { // 연속저장이 아니라면, 저장 후 상세 화면으로 이동=>조회화면으로 이동
        //		showPageCustomerReadMeterEdit(key);
        showPageCustomerBookVolume(0);
    }
}

//거래처 체적검침 수정 저장 버튼 처리
function clickSaveUpdateCustomerReadMeterEdit() {
    if (!hasPermission("hdnMenuPermissionReadMeter", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    var readMeterYearMonth = $("#txtReadMeterYearMonthCustomerReadMeterEdit").attr("value");
    if (!isValidateFormat(readMeterYearMonth, 'yyyy-MM')) {
        alert("검침년월을 형식에 맟게 입력해 주세요.(yyyy-MM)");
        return;
    }

    $("#divResultMessageCustomerReadMeterEdit").html(getResultMessage("저장 중입니다.", true));
    //	var key = saveCustomerReadMeter(false);
    saveCustomerReadMeter(false);
    //	showPageCustomerReadMeterEdit(key);
    showPageCustomerBookVolume(0);
}

//거래처 검침등록 - 신규등록 저장버튼 처리. newInsert - true: 연속등록
function saveCustomerReadMeter(insertYesNo) {
    var key = "";
    $("#divResultMessageCustomerReadMeterEdit").html(getResultMessage("저장 중입니다.", true));
    $("#divResultMessageCustomerReadMeterInsert").html("").trigger("create");
    var readMeterDate = $("#txtReadMeterDateCustomerReadMeterEdit").attr("value");
    var applyDelayDate = $("#txtApplyDelayDateCustomerReadMeterEdit").attr("value");
    if (applyDelayDate == undefined) {
        applyDelayDate == "";
    }
    var insertMode = "0"; // 수정 저장
    if (insertYesNo == true) { // 신규등록 저장
        $("#divResultMessageCustomerReadMeterEdit").html("").trigger("create");
        $("#divResultMessageCustomerReadMeterInsert").html(getResultMessage("저장 중입니다.", true)).trigger("create");
        insertMode = "1";
        readMeterDate = $("#txtReadMeterDateCustomerReadMeterInsert").attr("value");
        applyDelayDate = $("#txtApplyDelayDateCustomerReadMeterInsert").attr("value");
        if (applyDelayDate == undefined) {
            applyDelayDate == "";
        }
    }
    $.ajax({
        url: gasmaxWebappPath + "customer_read_meter_edit_save_ajx.jsp",
        type: "post",
        data: "insertMode=" + insertMode
            + "&customerCode=" + $("#hdnCurrentCustomerCode").attr("value")
            + "&sequenceNumber=" + $("#txtReadMeterYearMonthCustomerReadMeterEdit").attr("value") + $("#txtSequenceNumberCustomerReadMeterEdit").attr("value")
            + "&readMeterDate=" + readMeterDate
            + "&customerName=" + $("#hdnCustomerNameCustomerReadMeterEdit").attr("value")
            + "&userName=" + $("#hdnUserNameCustomerReadMeterEdit").attr("value")
            + "&employeeCode=" + $("#hdnEmployeeCodeCustomerReadMeterEdit").attr("value")
            + "&employeeName=" + $("#hdnEmployeeNameCustomerReadMeterEdit").attr("value")
            + "&preMonthReadMeter=" + deleteComma($("#txtPreReadMeterCustomerReadMeterEdit").attr("value"))
            + "&thisMonthReadMeter=" + deleteComma($("#txtThisMonthReadMeterCustomerReadMeterEdit").attr("value"))
            + "&useQuantity=" + deleteComma($("#txtUseQuantityCustomerReadMeterEdit").attr("value"))
            + "&price=" + deleteComma($("#txtPriceCustomerReadMeterEdit").attr("value"))
            + "&useAmount=" + deleteComma($("#txtUseAmountCustomerReadMeterEdit").attr("value"))
            + "&managementAmount=" + deleteComma($("#txtManagementAmountCustomerReadMeterEdit").attr("value"))
            + "&discountAmount=" + deleteComma($("#txtDiscountAmountCustomerReadMeterEdit").attr("value"))
            + "&delayAmount=" + deleteComma($("#txtDelayAmountCustomerReadMeterEdit").attr("value"))
            + "&thisMonthAmount=" + deleteComma($("#txtThisMonthAmountCustomerReadMeterEdit").attr("value"))
            + "&preRemain=" + $("#hdnPreRemainCustomerReadMeterEdit").attr("value")
            + "&nowRemain=" + deleteComma($("#txtNowRemainCustomerReadMeterEdit").attr("value"))
            + "&remark=" + $("#txtRemarkCustomerReadMeterEdit").attr("value")
            + "&startDate=" + $("#hdnStartDateCustomerReadMeterEdit").attr("value")
            + "&preUnpaidAmount=" + deleteComma($("#txtPreUnpaidAmountCustomerReadMeterEdit").attr("value"))
            + "&defaultDelayAmount=" + deleteComma($("#hdnDelayAmountCustomerReadMeterEdit").attr("value"))
            + "&applyDelayDate=" + applyDelayDate
            + "&defaultAmount=" + deleteComma($("#txtDefaultAmountCustomerReadMeterEdit").attr("value"))
            + "&defaultAmountYesNo=" + $("#hdnDefaultAmountYesNoCustomerReadMeterEdit").attr("value")
        ,
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultManageSaleList").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            key = $(xml).find("key").text();
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                html = getResultMessage("저장이 완료되었습니다.", false);
                setAllCustomerBizRequireRefresh(); // 모든 거래처별 업무 Refresh 필요.
            }
            if (insertMode == true) {
                $("#divResultMessageCustomerReadMeterInsert").html(html).trigger("create");
            } else {
                $("#divResultMessageCustomerReadMeterEdit").html(html).trigger("create");
            }
        }
    });
    return key;
}

//거래처 수금등록 검색 조건 삽입하기
function injectionCustomerCollect(tagId) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");


    $.ajax({
        url: gasmaxWebappPath + "customer_collect_insert.jsp?uuid=" + window.sessionStorage.uuid,
        type: "get",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });

    $("#hdnRequireRefreshPageCustomerCollect").attr("value", "N"); //거래처 수금등록이 이제 Refresh되었음을 표시
}

// 거래처별 수금등록 처리후잔액 처리하기
function calculateRemainAmountCustomerCollectInsert() {
    var receivable = 0;
    var collectType = $('input:radio[name="rdoCollectTypeCustomerCollectInsert"]:checked').val();
    if (collectType == "J") {
        receivable = $("#hdnWeightReceivableCustomerCollectInsert").attr("value");
    } else if (collectType == "C") {
        receivable = $("#hdnVolumeReceivableCustomerCollectInsert").attr("value");
    }
    var collectAmount = $("#txtCollectAmountCustomerCollectInsert").attr("value");
    var discountAmount = $("#txtDiscountAmountCustomerCollectInsert").attr("value");
    var remainAmount = receivable - parseInt(deleteComma(collectAmount), 10) - parseInt(deleteComma(discountAmount), 10);
    $("#txtReceivableCustomerCollectInsert").attr("value", insertComma(receivable));
    $("#txtRemainAmountCustomerCollectInsert").attr("value", insertComma(remainAmount));
}

//거래처별 수금등록 사원변경 처리
function changeEmployeeCustomerCollectInsert() {
    $("#hdnEmployeeNameCustomerCollectInsert").attr("value", $("#selectEmployeeCustomerCollectInsert").attr("value"));
}

// 거래처별 수금등록 저장버튼 처리
function clickSaveAndEditCustomerCollectInsert() {
    if (!hasPermission("hdnMenuPermissionCollect", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    //	var customerTypeCode = $('input:radio[name="rdoCollectTypeCustomerCollectInsert"]:checked').val();
    //	var returnCode = saveCustomerCollectInsert();
    saveCustomerCollectInsert();
    //	if (returnCode == "S"){ // 저장에 성공했을 때만 페이지 이동
    //		if (customerTypeCode == "J") { //일반(중량)장부의 거래 내역 조회
    //			showPageCustomerBookWeight("0");
    ////		$("#rdoCustomerBookWeightCollect").attr("checked", true);
    //		} else { //체적장부의 수금내역 조회
    //			showPageCustomerBookVolume("1");
    ////		$("#rdoCustomerBookVolumeCollect").attr("checked", true);
    //		}
    //	}
}

// 거래처별 수금등록 계속 저장버튼 처리
function clickSaveAndInsertCustomerCollectInsert() {
    if (!hasPermission("hdnMenuPermissionCollect", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    var returnCode = saveCustomerCollectInsert();
    if (returnCode == "S") {
        showPageCustomerSearch();
    }
}

// 거래처별 수금등록 저장 처리
function saveCustomerCollectInsert() {
    $("#divMessageCustomerCollectInsert").html(getResultMessage("수금내역 저장 중입니다.", true)).trigger("create");
    var customerCode = $("#hdnCurrentCustomerCode").attr("value");
    var collectType = $('input:radio[name="rdoCollectTypeCustomerCollectInsert"]:checked').val();
    var collectDate = $("#txtCollectDateCustomerCollectInsert").attr("value");
    var buildingName = $("#hdnBuildingNameCustomerCollectInsert").attr("value");
    var userName = $("#hdnUserNameCustomerCollectInsert").attr("value");
    var collectAmount = deleteComma($("#txtCollectAmountCustomerCollectInsert").attr("value"));
    if ((collectAmount == 0) || (collectAmount == "")) {
        $("#divMessageCustomerCollectInsert").html(getResultMessage("수금액을 입력해 주세요.", false));
        return;
    }
    var discountAmount = deleteComma($("#txtDiscountAmountCustomerCollectInsert").attr("value"));
    var collectMethodType = $("select[id=selectCollectTypeCustomerCollectInsert]").val();
    if (collectMethodType == "") {
        $("#divMessageCustomerCollectInsert").html(getResultMessage("수금방법을 선택해 주세요.", false));
        return;
    }
    var employeeCode = $("select[id=selectEmployeeCustomerCollectInsert]").val();
    if (!employeeCode || employeeCode == "") {
        $("#divMessageCustomerCollectInsert").html(getResultMessage("사원을 선택해 주세요.", false));
        return;
    }
    var employeeName = $("#selectEmployeeCustomerCollectInsert option:selected").text();
    var remark = $("#txtRemarkCustomerCollectInsert").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_collect_insert_save_ajx.jsp",
        type: "post",
        data: "customerCode=" + customerCode
            + "&collectType=" + collectType
            + "&collectDate=" + collectDate
            + "&buildingName=" + buildingName
            + "&userName=" + userName
            + "&collectAmount=" + collectAmount
            + "&discountAmount=" + discountAmount
            + "&collectMethodType=" + collectMethodType
            + "&employeeCode=" + employeeCode
            + "&employeeName=" + employeeName
            + "&remark=" + remark,
        dataType: "xml",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
            $("#divMessageCustomerCollect").html("").trigger("create");
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var code = $(xml).find("code").text();
            var message = $(xml).find("message").text();
            setAllCustomerBizRequireRefresh();
            if (code == "S") {
                $("#divMessageCustomerCollectInsert").html(getResultMessage("저장이 완료되었습니다.", false));
                if (code == "S") { // 저장에 성공했을 때만 페이지 이동
                    var customerTypeCode = $('input:radio[name="rdoCollectTypeCustomerCollectInsert"]:checked').val();
                    if (customerTypeCode == "J") { //일반(중량)장부의 거래 내역 조회
                        showPageCustomerBookWeight("0");
                    } else { //체적장부의 수금내역 조회
                        showPageCustomerBookVolume("1");
                    }
                }
            } else {
                injectionCustomerSummary("customerSummaryCustomerCollect"); //거래처 요약정보
                injectionCustomerCollect("divCustomerCollect"); //거래처 등록 화면
                alert("저장중에 오류가 발생하였습니다.");
                $("#divMessageCustomerCollectInsert").html(getResultMessage("저장 중 오류가 발생하였습니다. " + message, false));
            }
            return code;
        }
    });
}

//거래처 안전점검-점검이력 검색 조건 삽입하기
function injectionSearchOptionCustomerSaftyCheckList(tagId) {
    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_list_search_option_rev3.jsp",
        type: "post",
        dataType: "html",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });

    showActivityIndicator("검색 중입니다. 잠시만 기다려주세요")
    //$("#divSearchResultCustomerSaftyCheckList").html(getResultMessage("검색 중입니다.", true)).trigger("create");
    var customerCode = $("#hdnCurrentCustomerCode").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_list_search_ajx_rev3.jsp",
        type: "post",
        data: "customerCode=" + customerCode,
        dataType: "xml",
        timeout: 120000,
        async: false,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultCustomerSaftyCheckList").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            hideActivityIndicator()
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var rowCount = 0;
            $(xml).find("CustomerSaftyCheckList").each(function () {
                rowCount++;
                var key = $(this).find("key").text();
                var sequenceNumber = $(this).find("sequenceNumber").text();
                var checkType = $(this).find("checkType").text();
                var checkName = $(this).find("checkName").text();
                var scheduledCheckDate = $(this).find("scheduledCheckDate").text();
                scheduledCheckDate = scheduledCheckDate.substr(2, 2) + "-" + scheduledCheckDate.substr(4, 2) + "-" + scheduledCheckDate.substr(6, 2);
                var employeeName = $(this).find("employeeName").text();
                var acceptableYn = true;
                var acceptable = "적합";
                var acceptable1 = $(this).find("acceptable1").text();
                if (acceptable1 == "2") acceptableYn = false;
                var acceptable2 = $(this).find("acceptable2").text();
                if (acceptable2 == "2") acceptableYn = false;
                var acceptable3 = $(this).find("acceptable3").text();
                if (acceptable3 == "2") acceptableYn = false;
                var acceptable4 = $(this).find("acceptable4").text();
                if (acceptable4 == "2") acceptableYn = false;
                var acceptable5 = $(this).find("acceptable5").text();
                if (acceptable5 == "2") acceptableYn = false;
                var acceptable6 = $(this).find("acceptable6").text();
                if (acceptable6 == "2") acceptableYn = false;
                var acceptable7 = $(this).find("acceptable7").text();
                if (acceptable7 == "2") acceptableYn = false;
                var acceptable8 = $(this).find("acceptable8").text();
                if (acceptable8 == "2") acceptableYn = false;
                var acceptable9 = $(this).find("acceptable9").text();
                if (acceptable9 == "2") acceptableYn = false;
                var acceptable10 = $(this).find("acceptable10").text();
                if (acceptable10 == "2") acceptableYn = false;
                var acceptable11 = $(this).find("acceptable11").text();
                if (acceptable11 == "2") acceptableYn = false;
                var acceptable12 = $(this).find("acceptable12").text();
                if (acceptable12 == "2") acceptableYn = false;

                if (acceptableYn == false) {
                    acceptable = "부적합";
                }

                var signatureYn = $(this).find("signatureYn").text();

                html += '<a href="#" class="ui-btn ui-li ui-btn-up-c ui-btn-text ui-link ui-link-inherit" id="' + key + '" type="' + checkType + '" sno="' + sequenceNumber + '">'
                    + '		<table style="border: 0px solid #999999 ; border-bottom: 1px solid #222222 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">'
                    + '			<tr style="height: 40px ; ">'
                    + '				<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + checkName + '</span></td>'
                    + '				<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">' + scheduledCheckDate + '</span></td>'
                    + '				<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; "> ' + employeeName + ' </span></td>'
                    + '				<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; white-space: nowrap ; text-overflow: ellipsis ; overflow: hidden ; " ><span style="color:#222222 ; font-size:14px ; ">' + acceptable + '</span></td>'
                    + '				<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; "> ' + signatureYn + ' </span></td>'
                    + '			</tr>'
                    + '		</table>'
                    + '</a>';
            });
            if (rowCount == 0) {
                html = getResultMessage("검색된 자료가 없습니다.", false);
            }
            $("#divSearchResultCustomerSaftyCheckList").html(html).trigger("create");
            $("#divSearchResultCustomerSaftyCheckList a").click(function (event) { //하위의 모든 Anchor 태그에 이벤트 등록. 클릭 했을 때 거래처별 업무 메뉴 출력
                //Anchor의 하위에 있는 노드에서 이벤트가 발생하므로 상위의 Anchor 태그를 찾아 Anchor의 id에 등록된 key를 이용하여 안전점검 상세 페이지 출력
                var checkType = getParentSpecifiedTagId($(event.target), "a", "type");
                var key = getParentSpecifiedTagId($(event.target), "a", "id");
                var sequenceNumber = getParentSpecifiedTagId($(event.target), "a", "sno");

                // 점검구분(checkType) - 소비설비("1"), 저장탱크("2")
                if (checkType == "1") {
                    showPageCustomerSaftyCheckEdit(key, sequenceNumber);
                } else if (checkType == "2") {
                    showPageCustomerSaftyCheckTankEdit(key, sequenceNumber);
                }
            });
        }
    });

    //$("#hdnRequireRefreshPageCustomerSaftyCheckList").attr("value", "N"); //거래처 안전점검-점검이력이 이제 Refresh되었음을 표시
}


//거래처별 안전점검 사원변경 처리
function changeEmployeeCustomerSaftyCheckInsert() {
    //$("#hdnEmployeeNameCustomerSaftyCheckInsert").attr("value", $("#selectEmployeeCodeCustomerSaftyCheckInsert").attr("value"));
    $("#hdnEmployeeNameCustomerSaftyCheckInsert").attr("value", $("#selectEmployeeCodeCustomerSaftyCheckInsert option:selected").text());
}

//거래처별 안전점검 사원변경 처리
function changeEmployeeCustomerSaftyCheckEdit() {
    //$("#hdnEmployeeNameCustomerSaftyCheckEdit").attr("value", $("#selectEmployeeCodeCustomerSaftyCheckEdit").attr("value"));
    $("#hdnEmployeeNameCustomerSaftyCheckEdit").attr("value", $("#selectEmployeeCodeCustomerSaftyCheckEdit option:selected").text());
}

//거래처 안전점검-점검수정 상세내역 삽입하기
function injectionCustomerSaftyCheckEdit(tagId, key) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    // 안전점검-점검수정 신규등록내역 초기화
    $("#divCustomerSaftyCheckInsert").html("").trigger("create");
    $("#divResultMessageCustomerSaftyCheckEdit").html("").trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_edit_rev3.jsp",
        data: "key=" + key,
        type: "post",
        dataType: "html",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

// 거래처 안전점검 등록시 보일러 및 온수기 유형이 설정되면 검사항목 마바사아를 자동으로 적합으로 설정
function changeBoilerAndHeaterTypeCustomerSaftyCheckInsert() {
    var boilerType = $("select[id=selectCombustorBoilerTypeCustomerSaftyCheckInsert]").val();
    var heaterType = $("select[id=selectCombustorHeaterTypeCustomerSaftyCheckInsert]").val();
    var acceptable5 = $("select[id=selectAcceptable5CustomerSaftyCheckInsert]").val();
    var acceptable6 = $("select[id=selectAcceptable6CustomerSaftyCheckInsert]").val();
    var acceptable7 = $("select[id=selectAcceptable7CustomerSaftyCheckInsert]").val();
    var acceptable8 = $("select[id=selectAcceptable8CustomerSaftyCheckInsert]").val();
    if ((boilerType != "0") || (heaterType != "0")) {
        if (acceptable5 == "0") {
            $("#selectAcceptable5CustomerSaftyCheckInsert").attr("value", "1");
        }
        if (acceptable6 == "0") {
            $("#selectAcceptable6CustomerSaftyCheckInsert").attr("value", "1");
        }
        if (acceptable7 == "0") {
            $("#selectAcceptable7CustomerSaftyCheckInsert").attr("value", "1");
        }
        if (acceptable8 == "0") {
            $("#selectAcceptable8CustomerSaftyCheckInsert").attr("value", "1");
        }
    }
}

//거래처 안전점검 등록시 보일러 및 온수기 유형이 설정되면 검사항목 마바사아를 자동으로 적합으로 설정
function changeBoilerAndHeaterTypeCustomerSaftyCheckEdit() {
    var boilerType = $("select[id=selectCombustorBoilerTypeCustomerSaftyCheckEdit]").val();
    var heaterType = $("select[id=selectCombustorHeaterTypeCustomerSaftyCheckEdit]").val();
    var acceptable5 = $("select[id=selectAcceptable5CustomerSaftyCheckEdit]").val();
    var acceptable6 = $("select[id=selectAcceptable6CustomerSaftyCheckEdit]").val();
    var acceptable7 = $("select[id=selectAcceptable7CustomerSaftyCheckEdit]").val();
    var acceptable8 = $("select[id=selectAcceptable8CustomerSaftyCheckEdit]").val();
    if ((boilerType != "0") || (heaterType != "0")) {
        if (acceptable5 == "0") {
            $("#selectAcceptable5CustomerSaftyCheckEdit").attr("value", "1");
        }
        if (acceptable6 == "0") {
            $("#selectAcceptable6CustomerSaftyCheckEdit").attr("value", "1");
        }
        if (acceptable7 == "0") {
            $("#selectAcceptable7CustomerSaftyCheckEdit").attr("value", "1");
        }
        if (acceptable8 == "0") {
            $("#selectAcceptable8CustomerSaftyCheckEdit").attr("value", "1");
        }
    }
}

//거래처 안전점검-소비설비 점검등록 상세내역 삽입하기
function injectionCustomerSaftyCheckInsert(tagId) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    // 안전점검-점검수정 상세내역 초기화
    $("#divCustomerSaftyCheckEdit").html("").trigger("create");
    $("#divResultMessageCustomerSaftyCheckInsert").html("").trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_insert_rev3.jsp?uuid=" + window.sessionStorage.uuid,
        type: "post",
        dataType: "html",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });

    // $("#hdnRequireRefreshPageCustomerSaftyCheckInsert").attr("value", "N"); //거래처 안전점검-점검등록이 이제 Refresh되었음을 표시
}

//거래처 안전점검 등록 저장 버튼 처리
function clickSaveCustomerSaftyCheckInsert(continueYesNo) {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    // 사원선택 확인
    var employeeCode = $("#selectEmployeeCodeCustomerSaftyCheckInsert").attr("value");
    if (!employeeCode || employeeCode == "") {
        alert("사원을 선택해 주세요.");
        return;
    }

    //	var key = saveCustomerSaftyCheck(true);
    saveCustomerSaftyCheckInsert();
    if (continueYesNo == true) { // 연속저장이라면, 저장 후 신규 등록할 수 있도록 거래처 검색 화면으로 이동
        showPageCustomerSearch();
    } else { // 연속저장이 아니라면, 저장 후 상세 화면으로 이동 =>점검 목록으로 이동하는것으로 변경 처리
        //showPageCustomerSaftyCheckEdit(key);
        showPageCustomerSaftyCheckList();
    }
}

// 거래처 안전점검 수정 저장 버튼 처리
function clickUpdateCustomerSaftyCheckEdit() {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    // 사원선택 확인
    var employeeCode = $("#selectEmployeeCodeCustomerSaftyCheckEdit").attr("value");
    if (!employeeCode || employeeCode == "") {
        alert("사원을 선택해 주세요.");
        return;
    }

    //$("#divResultMessageCustomerSaftyCheckEdit").html(getResultMessage("저장 중입니다.", true));
    var key = saveCustomerSaftyCheckEdit(false);
    //showPageCustomerSaftyCheckEdit(key);
    showPageCustomerSaftyCheckList();
}

// 거래처 안전점검 저장 처리
function saveCustomerSaftyCheckInsert() {
    var key = "";
    $("#divResultMessageCustomerSaftyCheckInsert").html(getResultMessage("저장 중입니다.", true)).trigger("create");
    var async = false;
    var insertMode = "1"; // 신규등록 저장
    var contractDate = $("#txtContractDateCustomerSaftyCheckInsert").attr("value");
    var scheduledCheckDate = $("#txtScheduledCheckDateCustomerSaftyCheckInsert").attr("value");

    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_insert_save_ajx_rev3.jsp",
        type: "post",
        data: "insertMode=" + insertMode
            + "&customerCode=" + $("#hdnCurrentCustomerCode").attr("value")
            + "&address1=" + $("#txtAddress1CustomerSaftyCheckInsert").attr("value")
            + "&address2=" + $("#txtAddress2CustomerSaftyCheckInsert").attr("value")
            + "&contractNumber=" + $("#txtContractNumberCustomerSaftyCheckInsert").attr("value")
            + "&sequenceNumber=" + $("#hdnSequenceNumberCustomerSaftyCheckInsert").attr("value")
            + "&contractDate=" + contractDate
            + "&scheduledCheckDate=" + scheduledCheckDate
            + "&employeeCode=" + $("#selectEmployeeCodeCustomerSaftyCheckInsert").attr("value")
            + "&employeeName=" + $("#hdnEmployeeNameCustomerSaftyCheckInsert").attr("value")
            + "&contractName=" + $("#txtContractNameCustomerSaftyCheckInsert").attr("value")
            + "&phoneNumber=" + $("#txtPhoneNumberCustomerSaftyCheckInsert").attr("value")
            + "&pipeLength1=" + $("#txtPipeLength1CustomerSaftyCheckInsert").attr("value")
            + "&pipeLength2=" + $("#txtPipeLength2CustomerSaftyCheckInsert").attr("value")
            + "&pipeLength3=" + $("#txtPipeLength3CustomerSaftyCheckInsert").attr("value")
            + "&pipeLength4=" + $("#txtPipeLength4CustomerSaftyCheckInsert").attr("value")
            + "&pipeLength5=" + $("#txtPipeLength5CustomerSaftyCheckInsert").attr("value")
            + "&valveQuantity1=" + $("#txtValveQuantity1CustomerSaftyCheckInsert").attr("value")
            + "&valveQuantity2=" + $("#txtValveQuantity2CustomerSaftyCheckInsert").attr("value")
            + "&valveQuantity3=" + $("#txtValveQuantity3CustomerSaftyCheckInsert").attr("value")
            + "&valveQuantity4=" + $("#txtValveQuantity4CustomerSaftyCheckInsert").attr("value")
            + "&valveQuantity5=" + $("#txtValveQuantity5CustomerSaftyCheckInsert").attr("value")
            + "&etcEquipmentName1=" + $("#txtEtcEquipmentName1CustomerSaftyCheckInsert").attr("value")
            + "&etcEquipmentQuantity1=" + $("#txtEtcEquipmentQuantity1CustomerSaftyCheckInsert").attr("value")
            + "&etcEquipmentName2=" + $("#txtEtcEquipmentName2CustomerSaftyCheckInsert").attr("value")
            + "&etcEquipmentQuantity2=" + $("#txtEtcEquipmentQuantity2CustomerSaftyCheckInsert").attr("value")
            + "&etcEquipmentName3=" + $("#txtEtcEquipmentName3CustomerSaftyCheckInsert").attr("value")
            + "&etcEquipmentQuantity3=" + $("#txtEtcEquipmentQuantity3CustomerSaftyCheckInsert").attr("value")
            + "&etcEquipmentName4=" + $("#txtEtcEquipmentName4CustomerSaftyCheckInsert").attr("value")
            + "&etcEquipmentQuantity4=" + $("#txtEtcEquipmentQuantity4CustomerSaftyCheckInsert").attr("value")
            + "&combustorRange1=" + $("#txtCombustorRange1CustomerSaftyCheckInsert").attr("value")
            + "&combustorRange2=" + $("#txtCombustorRange2CustomerSaftyCheckInsert").attr("value")
            + "&combustorRange3=" + $("#txtCombustorRange3CustomerSaftyCheckInsert").attr("value")
            + "&combustorRangeEtcName=" + $("#txtCombustorRangeEtcNameCustomerSaftyCheckInsert").attr("value")
            + "&combustorRangeEtcQuantity=" + $("#txtCombustorRangeEtcQuantityCustomerSaftyCheckInsert").attr("value")
            + "&combustorBoilerType=" + $("select[id=selectCombustorBoilerTypeCustomerSaftyCheckInsert]").val()
            + "&combustorBoilerPosition=" + $("select[id=selectCombustorBoilerPositionCustomerSaftyCheckInsert]").val()
            + "&combustorBoilerConsumption=" + $("#txtCombustorBoilerConsumptionCustomerSaftyCheckInsert").attr("value")
            + "&combustorBoilerInstaller=" + $("#txtCombustorBoilerInstallerCustomerSaftyCheckInsert").attr("value")
            + "&combustorHeaterType=" + $("select[id=selectCombustorHeaterTypeCustomerSaftyCheckInsert]").val()
            + "&combustorHeaterPosition=" + $("select[id=selectCombustorHeaterPositionCustomerSaftyCheckInsert]").val()
            + "&combustorHeaterConsumption=" + $("#txtCombustorHeaterConsumptionCustomerSaftyCheckInsert").attr("value")
            + "&combustorHeaterInstaller=" + $("#txtCombustorHeaterInstallerCustomerSaftyCheckInsert").attr("value")
            + "&combustorEtcName1=" + $("#txtCombustorEtcName1CustomerSaftyCheckInsert").attr("value")
            + "&combustorEtcQuantity1=" + $("#txtCombustorEtcQuantity1CustomerSaftyCheckInsert").attr("value")
            + "&combustorEtcName2=" + $("#txtCombustorEtcName2CustomerSaftyCheckInsert").attr("value")
            + "&combustorEtcQuantity2=" + $("#txtCombustorEtcQuantity2CustomerSaftyCheckInsert").attr("value")
            + "&combustorEtcName3=" + $("#txtCombustorEtcName3CustomerSaftyCheckInsert").attr("value")
            + "&combustorEtcQuantity3=" + $("#txtCombustorEtcQuantity3CustomerSaftyCheckInsert").attr("value")
            + "&combustorEtcName4=" + $("#txtCombustorEtcName4CustomerSaftyCheckInsert").attr("value")
            + "&combustorEtcQuantity4=" + $("#txtCombustorEtcQuantity4CustomerSaftyCheckInsert").attr("value")
            + "&acceptable1=" + $("select[id=selectAcceptable1CustomerSaftyCheckInsert]").val()
            + "&acceptable2=" + $("select[id=selectAcceptable2CustomerSaftyCheckInsert]").val()
            + "&acceptable3=" + $("select[id=selectAcceptable3CustomerSaftyCheckInsert]").val()
            + "&acceptable4=" + $("select[id=selectAcceptable4CustomerSaftyCheckInsert]").val()
            + "&acceptable5=" + $("select[id=selectAcceptable5CustomerSaftyCheckInsert]").val()
            + "&acceptable6=" + $("select[id=selectAcceptable6CustomerSaftyCheckInsert]").val()
            + "&acceptable7=" + $("select[id=selectAcceptable7CustomerSaftyCheckInsert]").val()
            + "&acceptable8=" + $("select[id=selectAcceptable8CustomerSaftyCheckInsert]").val()
            + "&acceptable9=" + $("select[id=selectAcceptable9CustomerSaftyCheckInsert]").val()
            + "&acceptable10=" + $("select[id=selectAcceptable10CustomerSaftyCheckInsert]").val()
            + "&acceptable11=" + $("select[id=selectAcceptable11CustomerSaftyCheckInsert]").val()
            + "&acceptable12=" + $("select[id=selectAcceptable12CustomerSaftyCheckInsert]").val()
            + "&notifyRemark1=" + $("#txtNotifyRemark1CustomerSaftyCheckInsert").attr("value")
            + "&notifyRemark2=" + $("#txtNotifyRemark2CustomerSaftyCheckInsert").attr("value")
            + "&recommendation1=" + $("#txtRecommendation1CustomerSaftyCheckInsert").attr("value")
            + "&recommendation2=" + $("#txtRecommendation2CustomerSaftyCheckInsert").attr("value")
            + "&signatureImage=" + encodeURIComponent($("#hdnSignatureImageCustomerSaftyCheckInsert").attr("value"))
        ,
        dataType: "xml",
        async: async,
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultManageSaleList").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            key = $(xml).find("key").text();
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                html = getResultMessage("저장이 완료되었습니다.", false);
                setAllCustomerBizRequireRefresh(); // 모든 거래처별 업무 Refresh 필요.
            }
            $("#divResultMessageCustomerSaftyCheckInsert").html(html).trigger("create");
        }
    });
    return key;
}

//거래처 안전점검 저장 처리
function saveCustomerSaftyCheckEdit(insertYesNo) {
    var key = "";
    $("#divResultMessageCustomerSaftyCheckEdit").html(getResultMessage("저장 중입니다.", true));
    var async = false;
    var insertMode = "0"; // 수정 저장
    var contractDate = $("#txtContractDateCustomerSaftyCheckEdit").attr("value");
    var scheduledCheckDate = $("#txtScheduledCheckDateCustomerSaftyCheckEdit").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_edit_save_ajx_rev3.jsp",
        type: "post",
        data: "insertMode=" + insertMode
            + "&customerCode=" + $("#hdnCurrentCustomerCode").attr("value")
            + "&address1=" + $("#txtAddress1CustomerSaftyCheckEdit").attr("value")
            + "&address2=" + $("#txtAddress2CustomerSaftyCheckEdit").attr("value")
            + "&contractNumber=" + $("#txtContractNumberCustomerSaftyCheckEdit").attr("value")
            + "&sequenceNumber=" + $("#hdnSequenceNumberCustomerSaftyCheckEdit").attr("value")
            + "&contractDate=" + contractDate
            + "&scheduledCheckDate=" + scheduledCheckDate
            + "&employeeCode=" + $("#selectEmployeeCodeCustomerSaftyCheckEdit").attr("value")
            + "&employeeName=" + $("#hdnEmployeeNameCustomerSaftyCheckEdit").attr("value")
            + "&contractName=" + $("#txtContractNameCustomerSaftyCheckEdit").attr("value")
            + "&phoneNumber=" + $("#txtPhoneNumberCustomerSaftyCheckEdit").attr("value")
            + "&pipeLength1=" + $("#txtPipeLength1CustomerSaftyCheckEdit").attr("value")
            + "&pipeLength2=" + $("#txtPipeLength2CustomerSaftyCheckEdit").attr("value")
            + "&pipeLength3=" + $("#txtPipeLength3CustomerSaftyCheckEdit").attr("value")
            + "&pipeLength4=" + $("#txtPipeLength4CustomerSaftyCheckEdit").attr("value")
            + "&pipeLength5=" + $("#txtPipeLength5CustomerSaftyCheckEdit").attr("value")
            + "&valveQuantity1=" + $("#txtValveQuantity1CustomerSaftyCheckEdit").attr("value")
            + "&valveQuantity2=" + $("#txtValveQuantity2CustomerSaftyCheckEdit").attr("value")
            + "&valveQuantity3=" + $("#txtValveQuantity3CustomerSaftyCheckEdit").attr("value")
            + "&valveQuantity4=" + $("#txtValveQuantity4CustomerSaftyCheckEdit").attr("value")
            + "&valveQuantity5=" + $("#txtValveQuantity5CustomerSaftyCheckEdit").attr("value")
            + "&etcEquipmentName1=" + $("#txtEtcEquipmentName1CustomerSaftyCheckEdit").attr("value")
            + "&etcEquipmentQuantity1=" + $("#txtEtcEquipmentQuantity1CustomerSaftyCheckEdit").attr("value")
            + "&etcEquipmentName2=" + $("#txtEtcEquipmentName2CustomerSaftyCheckEdit").attr("value")
            + "&etcEquipmentQuantity2=" + $("#txtEtcEquipmentQuantity2CustomerSaftyCheckEdit").attr("value")
            + "&etcEquipmentName3=" + $("#txtEtcEquipmentName3CustomerSaftyCheckEdit").attr("value")
            + "&etcEquipmentQuantity3=" + $("#txtEtcEquipmentQuantity3CustomerSaftyCheckEdit").attr("value")
            + "&etcEquipmentName4=" + $("#txtEtcEquipmentName4CustomerSaftyCheckEdit").attr("value")
            + "&etcEquipmentQuantity4=" + $("#txtEtcEquipmentQuantity4CustomerSaftyCheckEdit").attr("value")
            + "&combustorRange1=" + $("#txtCombustorRange1CustomerSaftyCheckEdit").attr("value")
            + "&combustorRange2=" + $("#txtCombustorRange2CustomerSaftyCheckEdit").attr("value")
            + "&combustorRange3=" + $("#txtCombustorRange3CustomerSaftyCheckEdit").attr("value")
            + "&combustorRangeEtcName=" + $("#txtCombustorRangeEtcNameCustomerSaftyCheckEdit").attr("value")
            + "&combustorRangeEtcQuantity=" + $("#txtCombustorRangeEtcQuantityCustomerSaftyCheckEdit").attr("value")
            + "&combustorBoilerType=" + $("select[id=selectCombustorBoilerTypeCustomerSaftyCheckEdit]").val()
            + "&combustorBoilerPosition=" + $("select[id=selectCombustorBoilerPositionCustomerSaftyCheckEdit]").val()
            + "&combustorBoilerConsumption=" + $("#txtCombustorBoilerConsumptionCustomerSaftyCheckEdit").attr("value")
            + "&combustorBoilerInstaller=" + $("#txtCombustorBoilerInstallerCustomerSaftyCheckEdit").attr("value")
            + "&combustorHeaterType=" + $("select[id=selectCombustorHeaterTypeCustomerSaftyCheckEdit]").val()
            + "&combustorHeaterPosition=" + $("select[id=selectCombustorHeaterPositionCustomerSaftyCheckEdit]").val()
            + "&combustorHeaterConsumption=" + $("#txtCombustorHeaterConsumptionCustomerSaftyCheckEdit").attr("value")
            + "&combustorHeaterInstaller=" + $("#txtCombustorHeaterInstallerCustomerSaftyCheckEdit").attr("value")
            + "&combustorEtcName1=" + $("#txtCombustorEtcName1CustomerSaftyCheckEdit").attr("value")
            + "&combustorEtcQuantity1=" + $("#txtCombustorEtcQuantity1CustomerSaftyCheckEdit").attr("value")
            + "&combustorEtcName2=" + $("#txtCombustorEtcName2CustomerSaftyCheckEdit").attr("value")
            + "&combustorEtcQuantity2=" + $("#txtCombustorEtcQuantity2CustomerSaftyCheckEdit").attr("value")
            + "&combustorEtcName3=" + $("#txtCombustorEtcName3CustomerSaftyCheckEdit").attr("value")
            + "&combustorEtcQuantity3=" + $("#txtCombustorEtcQuantity3CustomerSaftyCheckEdit").attr("value")
            + "&combustorEtcName4=" + $("#txtCombustorEtcName4CustomerSaftyCheckEdit").attr("value")
            + "&combustorEtcQuantity4=" + $("#txtCombustorEtcQuantity4CustomerSaftyCheckEdit").attr("value")
            + "&acceptable1=" + $("select[id=selectAcceptable1CustomerSaftyCheckEdit]").val()
            + "&acceptable2=" + $("select[id=selectAcceptable2CustomerSaftyCheckEdit]").val()
            + "&acceptable3=" + $("select[id=selectAcceptable3CustomerSaftyCheckEdit]").val()
            + "&acceptable4=" + $("select[id=selectAcceptable4CustomerSaftyCheckEdit]").val()
            + "&acceptable5=" + $("select[id=selectAcceptable5CustomerSaftyCheckEdit]").val()
            + "&acceptable6=" + $("select[id=selectAcceptable6CustomerSaftyCheckEdit]").val()
            + "&acceptable7=" + $("select[id=selectAcceptable7CustomerSaftyCheckEdit]").val()
            + "&acceptable8=" + $("select[id=selectAcceptable8CustomerSaftyCheckEdit]").val()
            + "&acceptable9=" + $("select[id=selectAcceptable9CustomerSaftyCheckEdit]").val()
            + "&acceptable10=" + $("select[id=selectAcceptable10CustomerSaftyCheckEdit]").val()
            + "&acceptable11=" + $("select[id=selectAcceptable11CustomerSaftyCheckEdit]").val()
            + "&acceptable12=" + $("select[id=selectAcceptable12CustomerSaftyCheckEdit]").val()
            + "&notifyRemark1=" + $("#txtNotifyRemark1CustomerSaftyCheckEdit").attr("value")
            + "&notifyRemark2=" + $("#txtNotifyRemark2CustomerSaftyCheckEdit").attr("value")
            + "&recommendation1=" + $("#txtRecommendation1CustomerSaftyCheckEdit").attr("value")
            + "&recommendation2=" + $("#txtRecommendation2CustomerSaftyCheckEdit").attr("value")
            + "&signatureImage=" + encodeURIComponent($("#hdnSignatureImageCustomerSaftyCheckEdit").attr("value"))
        ,
        dataType: "xml",
        async: async,
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divSearchResultManageSaleList").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            key = $(xml).find("key").text();
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                html = getResultMessage("저장이 완료되었습니다.", false);
                setAllCustomerBizRequireRefresh(); // 모든 거래처별 업무 Refresh 필요.
            }
            $("#divResultMessageCustomerSaftyCheckEdit").html(html).trigger("create");
        }
    });
    return key;
}

// 거래처별 소비설비 안전점검 삭제버튼 처리
function clickDeleteCustomerSaftyCheckEdit() {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    if (!confirm("삭제하시겠습니까?")) {
        return;
    }
    $("#divResultMessageCustomerSaftyCheckEdit").html(getResultMessage("삭제 중입니다.", true)).trigger("create");

    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_edit_delete_ajx_v2016_r2.jsp",
        data: "sequenceNumber=" + $("#hdnSequenceNumberCustomerSaftyCheckEdit").attr("value")
            + "&customerCode=" + $("#hdnCurrentCustomerCode").attr("value")
        ,
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageCustomerSaftyCheckEdit").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                setAllCustomerBizRequireRefresh(); // 모든 거래처별 업무 Refresh 필요.
                $("#divResultMessageCustomerSaftyCheckEdit").html("").trigger("create");
                showPageCustomerSaftyCheckList();
            }
            $("#divResultMessageCustomerSaftyCheckEdit").html(html).trigger("create");
        }
    });
}

//거래처 안전점검-점검등록 서명 삽입하기
//기능변경으로 사용안함 (2016/11/10)
function injectionCustomerSaftyCheckSign(tagId) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    // 안전점검-점검수정 상세내역 초기화
    $("#" + tagId).html("").trigger("create");
    $("#divResultMessageCustomerSaftyCheckSign").html("").trigger("create");
    var insertMode = $("#hdnInsertModeCustomerSaftyCheckEdit").attr("value");
    var signatureFilePath = $("#hdnSignatureFilePathCustomerSaftyCheckEdit").attr("value");
    var signatureFileName = $("#hdnSignatureFileNameCustomerSaftyCheckEdit").attr("value");
    var signImagePath = $("#hdnSignImagePath").attr("value");
    var signatureImage = $("#hdnSignatureImageCustomerSaftyCheckEdit").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_sign.jsp",
        data: "insertMode=" + insertMode //0:insert 1:update
            + "&signatureFilePath=" + signatureFilePath
            + "&signatureFileName=" + signatureFileName
            + "&signImagePath=" + signImagePath
            + "&signatureImage=" + signatureImage
        ,
        type: "post",
        dataType: "html",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
            onloadCustomerSaftyCheckSign(signatureFileName);
        }
    });
}

//거래처 안전점검-점검등록 서명 팝업표시하기
function popupSignatureCustomerSaftyCheckInsert(tagId) {
    $("#" + tagId).html("").trigger("create");
    var insertMode = $("#hdnInsertModeCustomerSaftyCheckInsert").attr("value");		// 0:신규, 1:수정
    var signatureImage = $("#hdnSignatureImageCustomerSaftyCheckInsert").attr("value");
    var signatureYN = "N";
    if (signatureImage && signatureImage.length > 0) {
        signatureYN = "Y";
    }

    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_sign_v2016_r2.jsp",
        data: "insertMode=" + insertMode //0:insert 1:update
            + "&signatureYN=" + signatureYN
        ,
        type: "post",
        dataType: "html",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");

            onloadSignatureCustomerSaftyCheckInsert(signatureImage);
            $("#popupSignatureCustomerSaftyCheckInsert").popup({
                corners: false,
                history: false,
                shadow: false,
                positionTo: "window",
                overlayTheme: "a"
            });

            $("#popupSignatureCustomerSaftyCheckInsert").popup("open");

        }
    });
}

//거래처 안전점검-점검수정 서명 팝업표시하기
function popupSignatureCustomerSaftyCheckEdit(tagId) {
    $("#" + tagId).html("").trigger("create");
    var insertMode = $("#hdnInsertModeCustomerSaftyCheckEdit").attr("value");		// 0:신규, 1:수정
    var signatureImage = $("#hdnSignatureImageCustomerSaftyCheckEdit").attr("value");
    var signatureYN = "N";
    if (signatureImage && signatureImage.length > 0) {
        signatureYN = "Y";
    }

    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_sign_v2016_r2.jsp",
        data: "insertMode=" + insertMode //0:insert 1:update
            + "&signatureYN=" + signatureYN
        ,
        type: "post",
        dataType: "html",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");

            onloadSignatureCustomerSaftyCheckEdit(signatureImage);
            $("#popupSignatureCustomerSaftyCheckEdit").popup({
                corners: false,
                history: false,
                shadow: false,
                positionTo: "window",
                overlayTheme: "a"
            });

            $("#popupSignatureCustomerSaftyCheckEdit").popup("open");
        }
    });
}

//거래처별 저장탱크 안전점검 사원변경 처리
function changeEmployeeCustomerSaftyCheckTankInsert() {
    //$("#hdnEmployeeNameCustomerSaftyCheckTankInsert").attr("value", $("#selectEmployeeCustomerSaftyCheckTankInsert").attr("value"));
    $("#hdnEmployeeNameCustomerSaftyCheckTankInsert").attr("value", $("#selectEmployeeCodeCustomerSaftyCheckTankInsert option:selected").text());
}

//거래처별 저장탱크안전점검 사원변경 처리
function changeEmployeeCustomerSaftyCheckTankEdit() {
    //$("#hdnEmployeeNameCustomerSaftyCheckTankEdit").attr("value", $("#selectEmployeeCustomerSaftyCheckTankEdit").attr("value"));
    $("#hdnEmployeeNameCustomerSaftyCheckTankEdit").attr("value", $("#selectEmployeeCodeCustomerSaftyCheckTankEdit option:selected").text());
}

//거래처 저장탱크안전점검-점검수정 상세내역 삽입하기
function injectionCustomerSaftyCheckTankEdit(tagId, key, sequenceNumber) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    // 안전점검-점검수정 신규등록내역 초기화
    $("#divCustomerSaftyCheckTankInsert").html("").trigger("create");
    $("#divResultMessageCustomerSaftyCheckTankEdit").html("").trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_tank_edit.jsp",
        data: "key=" + key
            + "&sequenceNumber=" + sequenceNumber
        ,
        type: "post",
        dataType: "html",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//거래처 안전점검-저장탱크 점검등록 상세내역 삽입하기
function injectionCustomerSaftyCheckTankInsert(tagId) {
    $("#" + tagId).html(getResultMessage("잠시만 기다려주세요.", true)).trigger("create");
    // 안전점검-점검수정 상세내역 초기화
    $("#divCustomerSaftyCheckTankEdit").html("").trigger("create");
    $("#divResultMessageCustomerSaftyCheckTankInsert").html("").trigger("create");
    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_tank_insert.jsp?uuid=" + window.sessionStorage.uuid,
        type: "post",
        dataType: "html",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//거래처 저장탱크 안전점검 등록 저장 버튼 처리
function clickSaveCustomerSaftyCheckTankInsert(continueYesNo) {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    // 사원선택 확인
    var employeeCode = $("#selectEmployeeCodeCustomerSaftyCheckTankInsert").attr("value");
    if (!employeeCode || employeeCode == "") {
        alert("사원을 선택해 주세요.");
        return;
    }

    saveCustomerSaftyCheckTankInsert();
    if (continueYesNo == true) { // 연속저장이라면, 저장 후 신규 등록할 수 있도록 거래처 검색 화면으로 이동
        showPageCustomerSearch();
    } else { // 연속저장이 아니라면, 저장 후 상세 화면으로 이동 =>점검 목록으로 이동하는것으로 변경 처리
        showPageCustomerSaftyCheckList();
    }
}

//거래처 저장탱크 안전점검 수정 저장 버튼 처리
function clickUpdateCustomerSaftyCheckTankEdit() {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    // 사원선택 확인
    var employeeCode = $("#selectEmployeeCodeCustomerSaftyCheckTankEdit").attr("value");
    if (!employeeCode || employeeCode == "") {
        alert("사원을 선택해 주세요.");
        return;
    }

    var key = saveCustomerSaftyCheckTankEdit(false);
    showPageCustomerSaftyCheckList();
}

//거래처 저장탱크 안전점검 저장 처리
function saveCustomerSaftyCheckTankInsert() {
    var key = "";
    $("#divResultMessageCustomerSaftyCheckTankInsert").html(getResultMessage("저장 중입니다.", true)).trigger("create");
    var async = false;
    var insertMode = "1"; // 신규등록 저장
    var scheduledCheckDate = $("#txtScheduledCheckDateCustomerSaftyCheckTankInsert").attr("value");

    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_tank_insert_save_ajx.jsp",
        type: "post",
        data: "insertMode=" + insertMode
            + "&customerCode=" + $("#hdnCurrentCustomerCode").attr("value")
            + "&sequenceNumber=" + $("#hdnSequenceNumberCustomerSaftyCheckTankInsert").attr("value")
            + "&scheduledCheckDate=" + scheduledCheckDate
            + "&employeeCode=" + $("#selectEmployeeCodeCustomerSaftyCheckTankInsert").attr("value")
            + "&employeeName=" + $("#hdnEmployeeNameCustomerSaftyCheckTankInsert").attr("value")
            + "&tankCapacity1=" + $("#txtTankCapacity1CustomerSaftyCheckTankInsert").attr("value")
            + "&tankCapacity2=" + $("#txtTankCapacity2CustomerSaftyCheckTankInsert").attr("value")
            + "&acceptable1=" + $("select[id=selectAcceptable1CustomerSaftyCheckTankInsert]").val()
            + "&acceptable1Comment=" + $("#txtAcceptable1CommentCustomerSaftyCheckTankInsert").val()
            + "&acceptable2=" + $("select[id=selectAcceptable2CustomerSaftyCheckTankInsert]").val()
            + "&acceptable2Comment=" + $("#txtAcceptable2CommentCustomerSaftyCheckTankInsert").val()
            + "&acceptable3=" + $("select[id=selectAcceptable3CustomerSaftyCheckTankInsert]").val()
            + "&acceptable3Comment=" + $("#txtAcceptable3CommentCustomerSaftyCheckTankInsert").val()
            + "&acceptable4=" + $("select[id=selectAcceptable4CustomerSaftyCheckTankInsert]").val()
            + "&acceptable4Comment=" + $("#txtAcceptable4CommentCustomerSaftyCheckTankInsert").val()
            + "&acceptable5=" + $("select[id=selectAcceptable5CustomerSaftyCheckTankInsert]").val()
            + "&acceptable5Comment=" + $("#txtAcceptable5CommentCustomerSaftyCheckTankInsert").val()
            + "&acceptable6=" + $("select[id=selectAcceptable6CustomerSaftyCheckTankInsert]").val()
            + "&acceptable6Comment=" + $("#txtAcceptable6CommentCustomerSaftyCheckTankInsert").val()
            + "&acceptable7=" + $("select[id=selectAcceptable7CustomerSaftyCheckTankInsert]").val()
            + "&acceptable7Comment=" + $("#txtAcceptable7CommentCustomerSaftyCheckTankInsert").val()
            + "&acceptable8=" + $("select[id=selectAcceptable8CustomerSaftyCheckTankInsert]").val()
            + "&acceptable8Comment=" + $("#txtAcceptable8CommentCustomerSaftyCheckTankInsert").val()
            + "&acceptable9=" + $("select[id=selectAcceptable9CustomerSaftyCheckTankInsert]").val()
            + "&acceptable9Comment=" + $("#txtAcceptable9CommentCustomerSaftyCheckTankInsert").val()
            + "&acceptable10Content=" + $("#txtAcceptable10ContentCustomerSaftyCheckTankInsert").val()
            + "&acceptable10=" + $("select[id=selectAcceptable10CustomerSaftyCheckTankInsert]").val()
            + "&acceptable10Comment=" + $("#txtAcceptable10CommentCustomerSaftyCheckTankInsert").val()
            + "&acceptable11Content=" + $("#txtAcceptable11ContentCustomerSaftyCheckTankInsert").val()
            + "&acceptable11=" + $("select[id=selectAcceptable11CustomerSaftyCheckTankInsert]").val()
            + "&acceptable11Comment=" + $("#txtAcceptable11CommentCustomerSaftyCheckTankInsert").val()
            + "&acceptable12Content=" + $("#txtAcceptable12ContentCustomerSaftyCheckTankInsert").val()
            + "&acceptable12=" + $("select[id=selectAcceptable12CustomerSaftyCheckTankInsert]").val()
            + "&acceptable12Comment=" + $("#txtAcceptable12CommentCustomerSaftyCheckTankInsert").val()
            + "&employeeComment1=" + $("#txtEmployeeComment1CustomerSaftyCheckTankInsert").attr("value")
            + "&employeeComment2=" + $("#txtEmployeeComment2CustomerSaftyCheckTankInsert").attr("value")
            + "&customerName=" + $("#txtCustomerNameCustomerSaftyCheckTankInsert").attr("value")
            + "&signatureImage=" + encodeURIComponent($("#hdnSignatureImageCustomerSaftyCheckTankInsert").attr("value"))
        ,
        dataType: "xml",
        async: async,
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageCustomerSaftyCheckTankInsert").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            key = $(xml).find("key").text();
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                html = getResultMessage("저장이 완료되었습니다.", false);
                setAllCustomerBizRequireRefresh(); // 모든 거래처별 업무 Refresh 필요.
            }
            $("#divResultMessageCustomerSaftyCheckTankInsert").html(html).trigger("create");
        }
    });
    return key;
}

//거래처 저장탱크 안전점검 저장 처리
function saveCustomerSaftyCheckTankEdit(insertYesNo) {
    var key = "";
    $("#divResultMessageCustomerSaftyCheckTankEdit").html(getResultMessage("저장 중입니다.", true));
    var async = false;
    var insertMode = "0"; // 수정 저장
    var scheduledCheckDate = $("#txtScheduledCheckDateCustomerSaftyCheckTankEdit").attr("value");

    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_tank_edit_save_ajx.jsp",
        type: "post",
        data: "insertMode=" + insertMode
            + "&customerCode=" + $("#hdnCurrentCustomerCode").attr("value")
            + "&sequenceNumber=" + $("#hdnSequenceNumberCustomerSaftyCheckTankEdit").attr("value")
            + "&scheduledCheckDate=" + scheduledCheckDate
            + "&employeeCode=" + $("#selectEmployeeCodeCustomerSaftyCheckTankEdit").attr("value")
            + "&employeeName=" + $("#hdnEmployeeNameCustomerSaftyCheckTankEdit").attr("value")
            + "&tankCapacity1=" + $("#txtTankCapacity1CustomerSaftyCheckTankEdit").attr("value")
            + "&tankCapacity2=" + $("#txtTankCapacity2CustomerSaftyCheckTankEdit").attr("value")
            + "&acceptable1=" + $("select[id=selectAcceptable1CustomerSaftyCheckTankEdit]").val()
            + "&acceptable1Comment=" + $("#txtAcceptable1CommentCustomerSaftyCheckTankEdit").val()
            + "&acceptable2=" + $("select[id=selectAcceptable2CustomerSaftyCheckTankEdit]").val()
            + "&acceptable2Comment=" + $("#txtAcceptable2CommentCustomerSaftyCheckTankEdit").val()
            + "&acceptable3=" + $("select[id=selectAcceptable3CustomerSaftyCheckTankEdit]").val()
            + "&acceptable3Comment=" + $("#txtAcceptable3CommentCustomerSaftyCheckTankEdit").val()
            + "&acceptable4=" + $("select[id=selectAcceptable4CustomerSaftyCheckTankEdit]").val()
            + "&acceptable4Comment=" + $("#txtAcceptable4CommentCustomerSaftyCheckTankEdit").val()
            + "&acceptable5=" + $("select[id=selectAcceptable5CustomerSaftyCheckTankEdit]").val()
            + "&acceptable5Comment=" + $("#txtAcceptable5CommentCustomerSaftyCheckTankEdit").val()
            + "&acceptable6=" + $("select[id=selectAcceptable6CustomerSaftyCheckTankEdit]").val()
            + "&acceptable6Comment=" + $("#txtAcceptable6CommentCustomerSaftyCheckTankEdit").val()
            + "&acceptable7=" + $("select[id=selectAcceptable7CustomerSaftyCheckTankEdit]").val()
            + "&acceptable7Comment=" + $("#txtAcceptable7CommentCustomerSaftyCheckTankEdit").val()
            + "&acceptable8=" + $("select[id=selectAcceptable8CustomerSaftyCheckTankEdit]").val()
            + "&acceptable8Comment=" + $("#txtAcceptable8CommentCustomerSaftyCheckTankEdit").val()
            + "&acceptable9=" + $("select[id=selectAcceptable9CustomerSaftyCheckTankEdit]").val()
            + "&acceptable9Comment=" + $("#txtAcceptable9CommentCustomerSaftyCheckTankEdit").val()
            + "&acceptable10Content=" + $("#txtAcceptable10ContentCustomerSaftyCheckTankEdit").val()
            + "&acceptable10=" + $("select[id=selectAcceptable10CustomerSaftyCheckTankEdit]").val()
            + "&acceptable10Comment=" + $("#txtAcceptable10CommentCustomerSaftyCheckTankEdit").val()
            + "&acceptable11Content=" + $("#txtAcceptable11ContentCustomerSaftyCheckTankEdit").val()
            + "&acceptable11=" + $("select[id=selectAcceptable11CustomerSaftyCheckTankEdit]").val()
            + "&acceptable11Comment=" + $("#txtAcceptable11CommentCustomerSaftyCheckTankEdit").val()
            + "&acceptable12Content=" + $("#txtAcceptable12ContentCustomerSaftyCheckTankEdit").val()
            + "&acceptable12=" + $("select[id=selectAcceptable12CustomerSaftyCheckTankEdit]").val()
            + "&acceptable12Comment=" + $("#txtAcceptable12CommentCustomerSaftyCheckTankEdit").val()
            + "&employeeComment1=" + $("#txtEmployeeComment1CustomerSaftyCheckTankEdit").attr("value")
            + "&employeeComment2=" + $("#txtEmployeeComment2CustomerSaftyCheckTankEdit").attr("value")
            + "&customerName=" + $("#txtCustomerNameCustomerSaftyCheckTankEdit").attr("value")
            + "&signatureImage=" + encodeURIComponent($("#hdnSignatureImageCustomerSaftyCheckTankEdit").attr("value"))
        ,
        dataType: "xml",
        async: async,
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageCustomerSaftyCheckTankEdit").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            key = $(xml).find("key").text();
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                html = getResultMessage("저장이 완료되었습니다.", false);
                setAllCustomerBizRequireRefresh(); // 모든 거래처별 업무 Refresh 필요.
            }
            $("#divResultMessageCustomerSaftyCheckTankEdit").html(html).trigger("create");
        }
    });
    return key;
}

function clickDeleteCustomerSaftyCheckTankEdit() {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    if (!confirm("삭제하시겠습니까?")) {
        return;
    }
    $("#divResultMessageCustomerSaftyCheckTankEdit").html(getResultMessage("삭제 중입니다.", true)).trigger("create");

    $.ajax({
        url: gasmaxWebappPath + "customer_safty_check_tank_edit_delete_ajx.jsp",
        data: "sequenceNumber=" + $("#hdnSequenceNumberCustomerSaftyCheckTankEdit").attr("value")
            + "&customerCode=" + $("#hdnCurrentCustomerCode").attr("value")
        ,
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageCustomerSaftyCheckTankEdit").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var html = "";
            var errorCode = $(xml).find("code").text();
            if (errorCode == "E") {
                var message = $(xml).find("message").text();
                html = getResultMessage(message, false);
            } else {
                setAllCustomerBizRequireRefresh(); // 모든 거래처별 업무 Refresh 필요.
                $("#divResultMessageCustomerSaftyCheckTankEdit").html("").trigger("create");
                showPageCustomerSaftyCheckList();
            }
            $("#divResultMessageCustomerSaftyCheckTankEdit").html(html).trigger("create");
        }
    });
}

//CID-주문정보 검색 조건 삽입하기
function injectionSearchOptionManageCidList(tagId) {
    showActivityIndicator("로딩중입니다...")
    $.ajax({
        url: gasmaxWebappPath + "manage_cid_list.jsp",
        type: "post",
        data: "now=" + getToday("-"),
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("페이지가 존재하지 않습니다.", false);
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (html) {
            hideActivityIndicator()
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//CID-주문정보 신규등록 버튼 처리
function clickInsertCidManageCidList() {
    // if (!hasPermission("hdnMenuPermissionCid", ["0"])) {
    // 	alert("권한이 없습니다.");
    // 	return;
    // }

    showPageManageCidEdit("", true);
    changeCustomerManageCidEdit();
}

//CID 조회 화면에서 저장 버튼 클릭.
function clickSaveManageCidList(key) {
    var delivery = $("#ckbDeliveryManageCidList" + key).is(":checked");
    var complete = $("#ckbCompleteManageCidList" + key).is(":checked");
    var menuPermissionCid = $("#hdnMenuPermissionCid").attr("value");
    if (menuPermissionCid == "0") {
    } else if (menuPermissionCid == "1") {
        if (delivery == true) { //완료 불가
            alert("권한이 없습니다.");
            $("#ckbDeliveryManageCidList" + key).attr("checked", false).checkboxradio("refresh");
            return;
        }

        if (complete == true) { //완료 불가
            alert("권한이 없습니다.");
            $("#ckbCompleteManageCidList" + key).attr("checked", false).checkboxradio("refresh");
            return;
        }
    } else if (menuPermissionCid == "2") {
        if (delivery == true) { //완료 불가
            alert("권한이 없습니다.");
            $("#ckbDeliveryManageCidList" + key).attr("checked", false).checkboxradio("refresh");
            return;
        }

        if (complete == true) { //완료 불가
            alert("권한이 없습니다.");
            $("#ckbCompleteManageCidList" + key).attr("checked", false).checkboxradio("refresh");
            return;
        }
    }

    var employeeCode = $("#selectEmployeeManageCidList" + key).attr("value");
    var employeeName = $("#selectEmployeeManageCidList" + key + " option:selected").text();
    var itemCode = $("#hdnItemCodeManageCidList" + key).attr("value");
    var gasType = $("#hdnGasType").attr("value");
    if (employeeCode == "NA") {
        employeeCode = "";
        employeeName = "";
    }
    if ((delivery == true) || (complete == true)) { //배달이나 완료일 경우 품목 및 사원이 먼저 선택되어야 함.
        if (employeeCode == "") {
            alert("사원을 먼저 선택해 주세요.");
            $("#ckbDeliveryManageCidList" + key).attr("checked", false).checkboxradio("refresh");
            ;
            $("#ckbCompleteManageCidList" + key).attr("checked", false).checkboxradio("refresh");
            return;
        }
    }
    if ((complete == true) && (gasType == "LPG")) {
        if (itemCode == "") { //LPG일 때 완료 시 품목이 없으면 상세화면으로 이동
            $("#ckbCompleteManageCidList" + key).attr("checked", false).checkboxradio("refresh");
            showPageManageCidEdit(key, true);
            return;
        }
    }
    if (complete == true) {
        delivery = true; //완료일 경우 배달도 자동 체크함.
        $("#ckbDeliveryManageCidList" + key).attr("checked", true).checkboxradio("refresh");
        $("#selectEmployeeManageCidList" + key).selectmenu('disable');
    } else {
        $("#selectEmployeeManageCidList" + key).selectmenu('enable');
        //		$("#selectEmployeeManageCidList" + key).attr("aria-disabled", false).trigger("create");
        //		$("#selectEmployeeManageCidList" + key).removeClass("mobile-selectmenu-disabled").trigger("create");
        //		$("#selectEmployeeManageCidList" + key).removeClass("ui-state-disabled").trigger("create");
    }

    var deliveryYesNo = "false";
    if (delivery == true) {
        deliveryYesNo = "true";
    }
    var completeYesNo = "false";
    if (complete == true) {
        completeYesNo = "true";
    }

    $.ajax({
        url: gasmaxWebappPath + "manage_cid_list_save_ajx.jsp",
        type: "post",
        data: "key=" + key
            + "&employeeCode=" + employeeCode
            + "&employeeName=" + employeeName
            + "&deliveryYesNo=" + deliveryYesNo
            + "&completeYesNo=" + completeYesNo
        ,
        dataType: "xml",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
            $("#divMessageCustomerCollect").html("").trigger("create");
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var code = $(xml).find("code").text();
            var message = $(xml).find("message").text();
            if (code == "S") {
            } else {
                alert(message);
            }
        }
    });
}


//CID 상세화면 내용 삽입하기
function injectionManageCidEdit(tagId, key) {
    $("#" + tagId).html(getResultMessage("잠시 기다려주세요.", true)).trigger("create");
    var insertMode = "0";
    if (key == "") {
        insertMode = "1";
    }
    $.ajax({
        url: gasmaxWebappPath + "manage_cid_edit.jsp",
        type: "post",
        data: "key=" + key
            + "&insertMode=" + insertMode
        ,
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("페이지가 존재하지 않습니다.", false);
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//CID 상세화면 이전 다음 처리를 위한 키 가져오기
function navigateManageCidEdit(key, direction) {
    $.ajax({
        url: gasmaxWebappPath + "manage_cid_edit_navigate_ajx.jsp",
        data: "key=" + key
            + "&direction=" + direction
        ,
        type: "post",
        dataType: "xml",
        timeout: 120000,
        error: function (result) {
            if (result.status == 200) {
                var html = getResultMessage("검색된 자료가 없습니다.", false);
                $("#divResultMessageManageCidEdit").html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var key = $(xml).find("key").text();
            if (key == "X") {
                $("#divResultMessageManageCidEdit").html(getResultMessage("더 이상 자료가 없습니다.", false)).trigger("create");
            } else {
                $("#divResultMessageManageCidEdit").html("").trigger("create");
                injectionManageCidEdit("searchOptionManageCidEdit", key);
            }
        }
    });
}

//CID 상세화면에서 상세화면 거래처 선택하기
function changeCustomerManageCidEdit() {
    showPageCustomerSearch();
}

//CID 상세화면에서 품목 검색 클릭
function clickItemManageCidEdit() {
    showPageManageCidEditItemSearch();
}

//CID 상세화면에서 납품 수량과 회수 수량 동일하게
function changeSaleQuantityManageCidEdit() {
    var saleQuantity = parseInt(deleteComma($("#txtSaleQuantityManageCidEdit").attr("value")), 10);
    $("#txtWithdrawQuantityManageCidEdit").attr("value", insertComma2(saleQuantity));
    calculateTotalAmountManageCidEdit();
}

//CID 상세화면에서 합계금액 계산처리
function calculateTotalAmountManageCidEdit() {
    var cidPrice = parseFloat(deleteComma($("#txtCidPriceManageCidEdit").attr("value")), 10);
    var itemBalance = parseInt(deleteComma($("#txtItemBalanceManageCidEdit").attr("value")), 10);
    var saleQuantity = parseInt(deleteComma($("#txtSaleQuantityManageCidEdit").attr("value")), 10);
    var withdrawQuantity = parseInt(deleteComma($("#txtWithdrawQuantityManageCidEdit").attr("value")), 10);
    var quantity = saleQuantity - withdrawQuantity;
    var nowBalance = itemBalance + quantity;
    $("#txtNowBalanceManageCidEdit").attr("value", insertComma(nowBalance));
    var cidAmount = Math.round(cidPrice * saleQuantity);
    var taxAmount = 0;
    var totalAmount = 0;
    var vatType = $("#selectVatTypeManageCidEdit").attr("value");
    if (vatType == "0") { // VAT 별도일 때만 부가세 설정
        taxAmount = Math.round(cidAmount / 10);
        totalAmount = cidAmount + taxAmount;
    } else if (vatType == "1") { //VAT 포함일 때 처리
        totalAmount = cidAmount;
        cidAmount = Math.round(totalAmount / 1.1);
        taxAmount = totalAmount - cidAmount;
    } else if (vatType == "2") { //비과세일 경우
        totalAmount = cidAmount;
    }
    $("#txtCidAmountManageCidEdit").attr("value", insertComma(cidAmount));
    $("#txtTaxAmountManageCidEdit").attr("value", insertComma(taxAmount));
    $("#txtTotalAmountManageCidEdit").attr("value", insertComma(totalAmount));

    var collectType = $("#selectCollectTypeManageCidEdit").attr("value");
    var collectAmount = 0;
    var discountAmount = parseInt(deleteComma($("#txtDiscountAmountManageCidEdit").attr("value")), 10);
    var unpaidAmount = 0;
    // 0.현금, 2.예금, 3. 카드, 4.어음, B.현금영수증: 입금액=합계금액, D/C = 0, 미입금액=0
    // A.외상: 입금액 = 0,  D/C = 0, 미입금액 =  합계금액
    // [2017.11.01][Rev3] 예금도 현금과 동일하게 처리
    if ((collectType == "0") || (collectType == "2") || (collectType == "3") || (collectType == "4") || (collectType == "B")) {
        collectAmount = totalAmount - discountAmount;
    } else if (collectType == "A") {
        unpaidAmount = totalAmount - discountAmount;
    }
    $("#txtCollectAmountManageCidEdit").attr("value", insertComma2(collectAmount));
    $("#txtUnpaidAmountManageCidEdit").attr("value", insertComma(unpaidAmount));
}

//CID 상세화면에서 수금방법 변경 시
function changeCollectTypeManageCidEdit() {
    var totalAmount = parseInt(deleteComma($("#txtTotalAmountManageCidEdit").attr("value")));
    var collectType = $("#selectCollectTypeManageCidEdit").attr("value");
    var collectAmount = 0;
    var discountAmount = parseInt(deleteComma($("#txtDiscountAmountManageCidEdit").attr("value")));
    var unpaidAmount = 0;
    // 0.현금, 2.예금, 3. 카드, 4.어음, B.현금영수증: 입금액=합계금액, D/C = 0, 미입금액=0
    // A.외상: 입금액 = 0,  D/C = 0, 미입금액 =  합계금액
    // [2017.11.01][Rev3] 예금도 현금과 동일하게 처리
    if ((collectType == "0") || (collectType == "2") || (collectType == "3") || (collectType == "4") || (collectType == "B")) {
        collectAmount = totalAmount - discountAmount;
    } else if (collectType == "A") {
        unpaidAmount = totalAmount - collectAmount - discountAmount;
    }
    $("#txtCollectAmountManageCidEdit").attr("value", insertComma2(collectAmount));
    $("#txtUnpaidAmountManageCidEdit").attr("value", insertComma(unpaidAmount));
}

//CID 미입금액 계산처리
function calculateUnpaidAmountManageCidEdit() {
    var totalAmount = parseInt(deleteComma($("#txtTotalAmountManageCidEdit").attr("value")), 10);
    var collectAmount = parseInt(deleteComma($("#txtCollectAmountManageCidEdit").attr("value")), 10);
    var discountAmount = parseInt(deleteComma($("#txtDiscountAmountManageCidEdit").attr("value")), 10);
    var collectType = $("#selectCollectTypeManageCidEdit").attr("value");
    var unpaidAmount = 0;
    // 0.현금, 2.예금, 3. 카드, 4.어음, B.현금영수증: 입금액=합계금액, D/C = 0, 미입금액=0
    // A.외상: 입금액 = 0,  D/C = 0, 미입금액 =  합계금액
    // [2017.11.01][Rev3] 예금도 현금과 동일하게 처리
    if ((collectType == "0") || (collectType == "2") || (collectType == "3") || (collectType == "4") || (collectType == "B")) {
        collectAmount = totalAmount - discountAmount;
    } else if (collectType == "A") {
        unpaidAmount = totalAmount - collectAmount - discountAmount;
    }
    $("#txtCollectAmountManageCidEdit").attr("value", insertComma2(collectAmount));
    $("#txtUnpaidAmountManageCidEdit").attr("value", insertComma(unpaidAmount));
}

//CID 상세 화면에서 비고 검색 버튼 클릭
function clickRemarkManageCidEdit() {
    showPageManageCidEditRemarkSearch();
}

//CID 상세 화면에서 저장 버튼 클릭. insertMode 0:수정 1:신규
function clickSaveManageCidEdit(insertMode, closeBool) {
    if (!hasPermission("hdnMenuPermissionCid", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    if (closeBool == undefined) {
        closeBool = true;
    }
    var delivery = $("#ckbDeliveryManageCidEdit").is(":checked");
    var complete = $("#ckbCompleteManageCidEdit").is(":checked");
    var itemCode = $("#hdnItemCodeManageCidEdit").attr("value");
    var employeeCode = $("#selectEmployeeManageCidEdit").attr("value");
    if (employeeCode == "NA") {
        employeeCode = "";
    }
    if ((delivery == true) || (complete == true)) { //배달이나 완료일 경우 품목 및 사원이 먼저 선택되어야 함.
        var messageItem = "";
        if (itemCode == "") {
            messageItem = "품목 ";
        }
        if (employeeCode == "") {
            messageItem += "사원 ";
        }
        if (messageItem != "") {
            $("#divMessageManageCidEdit").html(getResultMessage(messageItem + "항목을 선택해 주세요.", false)).trigger("create");
            $("#ckbDeliveryManageCidEdit").attr("checked", false).checkboxradio("refresh");
            $("#ckbCompleteManageCidEdit").attr("checked", false).checkboxradio("refresh");
            return;
        }
    }
    if (complete == true) {
        delivery = true; //완료일 경우 배달도 자동 체크함.
    }

    $("#divMessageManageCidEdit").html(getResultMessage("저장 중입니다.", true)).trigger("create");
    var keyValue = $("#hdnKeyManageCidEdit").attr("value");
    var address = $("#hdnAddressManageCidEdit").attr("value");
    var cidDate = $("#hdnCidDateManageCidEdit").attr("value");
    var sequenceNumber = $("#hdnSequenceNumberManageCidEdit").attr("value");
    var saleType = $("#selectSaleTypeManageCidEdit").attr("value");
    var customerCode = $("#hdnCustomerCodeManageCidEdit").attr("value");
    var customerName = $("#hdnCustomerNameManageCidEdit").attr("value");
    var phoneNumber = $("#hdnPhoneNumberManageCidEdit").attr("value");
    var itemName = $("#txtItemManageCidEdit").attr("value");
    var saleQuantity = deleteComma($("#txtSaleQuantityManageCidEdit").attr("value"));
    var withdrawQuantity = deleteComma($("#txtWithdrawQuantityManageCidEdit").attr("value"));
    var salePrice = deleteComma($("#txtCidPriceManageCidEdit").attr("value"));
    var priceType = $("#hdnPriceTypeManageCidEdit").attr("value");
    var vatType = $("#selectVatTypeManageCidEdit").attr("value");
    var saleAmount = deleteComma($("#txtCidAmountManageCidEdit").attr("value"));
    var taxAmount = deleteComma($("#txtTaxAmountManageCidEdit").attr("value"));
    var totalAmount = deleteComma($("#txtTotalAmountManageCidEdit").attr("value"));
    var discountAmount = deleteComma($("#txtDiscountAmountManageCidEdit").attr("value"));
    var collectAmount = deleteComma($("#txtCollectAmountManageCidEdit").attr("value"));
    var unpaidAmount = deleteComma($("#txtUnpaidAmountManageCidEdit").attr("value"));
    var employeeName = $("#selectEmployeeManageCidEdit option:selected").text();
    var remark = $("#txtRemarkManageCidEdit").attr("value");
    var deliveryYesNo = "false";
    if (delivery == true) {
        deliveryYesNo = "true";
    }
    var completeYesNo = "false";
    if (complete == true) {
        completeYesNo = "true";
    }
    var collectType = $("#selectCollectTypeManageCidEdit").attr("value");

    $.ajax({
        url: gasmaxWebappPath + "manage_cid_edit_save_ajx.jsp",
        type: "post",
        data: "insertMode=" + insertMode
            + "&key=" + keyValue
            + "&sequenceNumber=" + sequenceNumber
            + "&cidDate=" + cidDate
            + "&saleType=" + saleType
            + "&customerCode=" + customerCode
            + "&customerName=" + customerName
            + "&phoneNumber=" + phoneNumber
            + "&itemCode=" + itemCode
            + "&itemName=" + itemName
            + "&saleQuantity=" + saleQuantity
            + "&withdrawQuantity=" + withdrawQuantity
            + "&salePrice=" + salePrice
            + "&priceType=" + priceType
            + "&vatType=" + vatType
            + "&saleAmount=" + saleAmount
            + "&taxAmount=" + taxAmount
            + "&totalAmount=" + totalAmount
            + "&discountAmount=" + discountAmount
            + "&collectAmount=" + collectAmount
            + "&unpaidAmount=" + unpaidAmount
            + "&employeeCode=" + employeeCode
            + "&employeeName=" + employeeName
            + "&remark=" + remark
            + "&deliveryYesNo=" + deliveryYesNo
            + "&completeYesNo=" + completeYesNo
            + "&collectType=" + collectType
        ,
        dataType: "xml",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
            $("#divMessageCustomerCollect").html("").trigger("create");
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var code = $(xml).find("code").text();
            var message = $(xml).find("message").text();
            if (code == "S") {
                if (closeBool == true) {
                    showPageManageCidList(false);
                }
                // 사원 목록 select option 항목 html 생성
                var selectEmployeeHtml = "";
                var employeeCount = $("#selectEmployeeManageCidList option").size();
                for (var i = 1; i < employeeCount; i++) {
                    var value = $("#selectEmployeeManageCidList option:eq(" + i + ")").val();
                    var text = $("#selectEmployeeManageCidList option:eq(" + i + ")").text();
                    if (text == "미지정") {
                        text = "";
                    }
                    var selected = "";
                    if (value == employeeCode) {
                        selected = "selected";
                    }
                    selectEmployeeHtml += '<option value="' + value + '" ' + selected + '>' + text + '</option>';
                }
                if (insertMode == "0") { // 수정모드일 때 조회화면 해당 항목의 상태 갱신
                    var customerNameColor = "red";
                    if (delivery == true) {
                        customerNameColor = "blue";
                        $("#ckbDeliveryManageCidList" + keyValue).attr("checked", true).checkboxradio("refresh");
                    } else {
                        $("#ckbDeliveryManageCidList" + keyValue).attr("checked", false).checkboxradio("refresh");
                    }
                    if (complete == true) {
                        customerNameColor = "black";
                        $("#ckbCompleteManageCidList" + keyValue).attr("checked", true).checkboxradio("refresh");
                    } else {
                        $("#ckbCompleteManageCidList" + keyValue).attr("checked", false).checkboxradio("refresh");
                    }
                    $("#spnCustomerNameManageCidList" + keyValue).css("color", customerNameColor);
                    $("#spnPhoneNumberManageCidList" + keyValue).html(phoneNumber);
                    $("#spnCustomerNameManageCidList" + keyValue).html(customerName);
                    $("#spnAddressManageCidList" + keyValue).html(address);
                    $("#spnRemarkManageCidList" + keyValue).html(remark + "&nbsp;");
                    $("#selectEmployeeManageCidList" + keyValue).html(selectEmployeeHtml).selectmenu("refresh");
                } else { // 신규모드일 때 조회화면의 맨 위 항목에 추가
                    var defaultAreaAddress = $("#hdnAreaAddressManageCidList").attr("value");
                    var phoneAreaNumber = $("#hdnPhoneAreaNumberManageCidList").attr("value");
                    searchManageCidList(defaultAreaAddress, phoneAreaNumber);
                }
                $("#divMessageManageCidEdit").html(getResultMessage("저장이 완료되었습니다.", false));
            } else {
                $("#divMessageManageCidEdit").html(getResultMessage(message, false));
            }
        }
    });
}

//CID 상세 화면에서 배달 체크박스 클릭시 저장하기. insertMode 0:수정 1:신규
function clickDeliveryCheckBoxSaveManageCidEdit(insertMode) {
    if (insertMode == "1") {
        return;
    }
    var delivery = $("#ckbDeliveryManageCidEdit").is(":checked");
    var complete = $("#ckbCompleteManageCidEdit").is(":checked");
    if ((delivery == false) && (complete == true)) {
        return;
    }

    if (!hasPermission("hdnMenuPermissionCid", ["0"])) {
        alert("권한이 없습니다.");
        $("#ckbDeliveryManageCidEdit").attr("checked", false).checkboxradio("refresh");
        return;
    }

    clickSaveManageCidEdit(insertMode, true);
}

//CID 상세 화면에서 완료 체크박스 클릭시 저장하기. insertMode 0:수정 1:신규
function clickCompleteCheckBoxSaveManageCidEdit(insertMode) {
    var complete = $("#ckbCompleteManageCidEdit").is(":checked");
    var menuPermissionCid = $("#hdnMenuPermissionCid").attr("value");
    if (menuPermissionCid == "0") {
    } else if (menuPermissionCid == "1") {
        if (complete == true) { //완료 불가
            alert("권한이 없습니다.");
            $("#ckbCompleteManageCidEdit").attr("checked", false).checkboxradio("refresh");
            return;
        }
    } else if (menuPermissionCid == "2") {
        if (complete == true) { //완료 불가
            alert("권한이 없습니다.");
            $("#ckbCompleteManageCidEdit").attr("checked", false).checkboxradio("refresh");
            return;
        }
    }
    if (insertMode == "1") {
        return;
    }
    if (complete == true) {
        clickSaveManageCidEdit(insertMode, true);
        $("#txtCollectAmountManageCidEdit").attr("readonly", true);
    } else {
        clickSaveManageCidEdit(insertMode, false);
        $("#txtCollectAmountManageCidEdit").attr("readonly", false);
        $("#btnSaveManageCidEdit").css("display", "inline-block");
        $("#btnDeleteManageCidEdit").css("display", "inline-block");
    }
}


//CID 상세 화면에서 삭제 버튼 클릭.
function clickDeleteManageCidEdit() {
    if (!hasPermission("hdnMenuPermissionCid", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    $("#divMessageManageCidEdit").html(getResultMessage("삭제 중입니다.", true)).trigger("create");
    var cidDate = $("#hdnCidDateManageCidEdit").attr("value");
    var sequenceNumber = $("#hdnSequenceNumberManageCidEdit").attr("value");

    $.ajax({
        url: gasmaxWebappPath + "manage_cid_edit_delete_ajx.jsp",
        type: "post",
        data: "sequenceNumber=" + sequenceNumber
            + "&cidDate=" + cidDate
        ,
        dataType: "xml",
        timeout: 120000,
        async: false,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
            $("#divMessageCustomerCollect").html("").trigger("create");
        },
        success: function (xml) {
            if ($(xml).find("session").text() == "X") {
                alert("오랫동안 사용하지 않아서\n접속이 종료되었습니다.\n다시 로그인해 주세요.");
                showPageIntro(false);
                return;
            }
            var code = $(xml).find("code").text();
            var message = $(xml).find("message").text();
            if (code == "S") {
                showPageManageCidList(false);
                var defaultAreaAddress = $("#hdnAreaAddressManageCidList").attr("value");
                var phoneAreaNumber = $("#hdnPhoneAreaNumberManageCidList").attr("value");
                searchManageCidList(defaultAreaAddress, phoneAreaNumber);
            } else {
                $("#divMessageManageCidEdit").html(getResultMessage(message, false));
            }
        }
    });
}

//CID 품목 검색 화면 삽입하기
function injectionManageCidEditItemSearch(tagId) {
    var keyword = $("#txtItemManageCidEdit").attr("value");
    var customerCode = $("#hdnCustomerCodeManageCidEdit").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "manage_cid_edit_item_search.jsp",
        data: "keyword=" + keyword
            + "&customerCode=" + customerCode
        ,
        type: "post",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//CID 품목 검색 화면에서 품목 선택 클릭
function clickItemManageCidEditItemSearch(itemCode) {
    //품목 설정
    $("#hdnItemCodeManageCidEdit").attr("value", itemCode);
    var itemName = $("#hdnItemNameManageCidEditItemSearch" + itemCode).attr("value");
    $("#hdnItemNameManageCidEdit").attr("value", itemName);
    var itemSpec = $("#hdnItemSpecManageCidEditItemSearch" + itemCode).attr("value");
    $("#hdnItemSpecManageCidEdit").attr("value", itemSpec);
    $("#txtItemManageCidEdit").attr("value", itemName + " " + itemSpec);
    var salePrice = $("#hdnSalePriceManageCidEditItemSearch" + itemCode).attr("value");
    var lastUnpaidAmount = $("#hdnLastUnpaidAmountManageCidEditItemSearch" + itemCode).attr("value");
    $("#txtCidPriceManageCidEdit").attr("value", insertComma(salePrice));
    $("#txtCidPriceManageCidEdit").trigger("change"); // 단가 변경 이벤트 자동 처리
    if (lastUnpaidAmount == "0") {
        $("#selectCollectTypeManageCidEdit").val("0").attr("selected", "selected");
    } else {
        $("#selectCollectTypeManageCidEdit").val("4").attr("selected", "selected");
    }
    $("#selectCollectTypeManageCidEdit").selectmenu("refresh", true);
    //닫기
    showPageManageCidEdit('', false);
}

//CID 비고 검색 화면 삽입하기
function injectionManageCidEditRemarkSearch(tagId) {
    var customerCode = $("#hdnCustomerCodeManageCidEdit").attr("value");
    $.ajax({
        url: gasmaxWebappPath + "manage_cid_edit_remark_search.jsp",
        data: "customerCode=" + customerCode,
        type: "post",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            } else {
                alert("error occured. Status:" + result.status
                    + " --Status Text:" + result.statusText
                    + " --Error Result:" + result);
            }
        },
        success: function (html) {
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//CID 비고 검색 화면 비고 클릭하기
function clickRemarkManageCidEditRemarkSearch(remark) {
    $("#txtRemarkManageCidEdit").attr("value", remark);
    //닫기
    showPageManageCidEdit('', false);
}

//판매현황 검색 조건 삽입하기
function injectionSearchOptionManageSaleList(tagId) {
    showActivityIndicator("로딩중입니다..")
    $.ajax({
        url: gasmaxWebappPath + "manage_sale_list.jsp",
        type: "post",
        data: "now=" + getToday("-"),
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("페이지가 존재하지 않습니다.", false);
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (html) {
            hideActivityIndicator()
            $("#" + tagId).html(html).trigger("create");
        }
    });
}


//미수현황 검색 조건 삽입하기
function injectionSearchOptionManageUnpaidList(tagId) {
    showActivityIndicator("로딩중입니다..")
    $.ajax({
        url: gasmaxWebappPath + "manage_unpaid_list.jsp?uuid=" + window.sessionStorage.uuid,
        type: "get",
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("페이지가 존재하지 않습니다.", false);
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (html) {
            hideActivityIndicator()
            $("#" + tagId).html(html).trigger("create");
        }
    });
}

//수금현황 검색 조건 삽입하기
function injectionSearchOptionManageCollectList(tagId) {
    showActivityIndicator("로딩중입니다..")
    $.ajax({
        url: gasmaxWebappPath + "manage_collect_list.jsp",
        type: "post",
        data: "now=" + getToday("-"),
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("페이지가 존재하지 않습니다.", false);
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (html) {
            hideActivityIndicator()
            $("#" + tagId).html(html).trigger("create");
        }
    });
}


//검침현황 검색 조건 삽입하기
function injectionSearchOptionManageReadMeterList(tagId) {
    showActivityIndicator("로딩중입니다...")
    $.ajax({
        url: gasmaxWebappPath + "manage_read_meter_list.jsp",
        type: "post",
        data: "now=" + getToday("-"),
        dataType: "html",
        timeout: 120000,
        error: function (result) {
            hideActivityIndicator()
            if (result.status == 200) {
                var html = getResultMessage("페이지가 존재하지 않습니다.", false);
                $("#" + tagId).html(html).trigger("create");
            } else if (result.status == 0) {
                console.log("서버 응답 지연 (timeout 또는 연결 실패)");
            }
        },
        success: function (html) {
            hideActivityIndicator()
            $("#" + tagId).html(html).trigger("create");
        }
    });
}


//현재 페이지 세팅하기
function setCurrentPage(pageName) {
    var previousPage = $("#hdnCurrentPage").attr("value");
    $("#hdnPreviousPage").attr("value", previousPage);
    $("#hdnCurrentPage").attr("value", pageName);
}

// 서명 이미지 업로드
function uploadSign(fileName, saveDir) {
    var a;
    if (fileName == undefined) {
        fileName = "test_10.jpg";
    }
    if (saveDir == undefined) {
        saveDir = "";
    }
    var signImagePath = $("#hdnSignImagePath").attr("value");
    var server = signImagePath + "/uploader";
    var imageURI = "/sdcard/gasmax_sign/" + fileName;
    alert(imageURI);
    var options = new FileUploadOptions();
    options.fileKey = "fileKey";
    options.fileName = fileName;
    options.mimeType = "image/jpeg";
    var params = new Object();
    params.param1 = "gasmax_sign";
    options.chunkedMode = false;
    options.params = params;

    var fileTransfer = new FileTransfer();
    fileTransfer.upload(
        imageURI,
        server + "?saveDir=" + saveDir,
        function (response) {
            //			alert("전송완료!!\n" +
            //			"Code = " + response.responseCode + "\n" +
            //			"Response = " + response.response + "\n" +
            //			"Sent = " + response.bytesSent);
        },
        function (error) {
            alert("서명 전송에 실패하였습니다. \n다시 시도해주세요.\n(Error Code = " + error.code + ")");
        },
        options
    );
}

// 스크린 샷
function screenShot(fileName) {
    var quality = "10";
    if (fileName == undefined) {
        fileName = "test_" + quality + ".jpg";
    }
    var resultCode = "";
    window.plugins.screenshot.saveScreenshot(fileName, quality, function (result) {
        //alert(fileName);
        resultCode = result;
    });
    return resultCode;
}

//phonegap DatePicker 플러그인 실행하기
function pickDate(inputId) {
    $("#hdnDatePickerInputId").attr("value", inputId);
    var currentField = $("#" + inputId);
    window.plugins.datePicker.show({
        date: currentField.val(),
        mode: 'date',
        allowOldDates: true
    }, function (returnDate) {
        currentField.val(returnDate);
        currentField.focus().select();
        currentField.blur();
        $("#" + $("#hdnDatePickerInputId").attr("value")).trigger("change");
    });
}


//날짜 선택 다이얼로그 제목 업데이트
function updateTitleDialogDatePicker(year, month, date) {
    var dateValue = new Date(year, month - 1, date);
    var week = new Array("일", "월", "화", "수", "목", "금", "토");
    var day = week[dateValue.getDay()] + "요일";
    $("#spnTitleDialogDatePicker").html(year + "년 " + month + "월 " + date + "일 " + day);
}

//날짜 선택 다이얼로그 - 날짜 선택하기
function clickOkDatePicker() {
    var year = $("#txtYearDatePicker").attr("value");
    var month = $("#txtMonthDatePicker").attr("value");
    if (month.length == 1) month = "0" + month;
    var day = $("#txtDayDatePicker").attr("value");
    if (day.length == 1) day = "0" + day;
    var result = year + "-" + month + "-" + day;
    $("#" + $("#hdnDatePickerInputId").attr("value")).attr("value", result);
    //	if ($("#hdnDatePickerInputId").attr("value").indexOf("Start") != -1){
    //		$("#hdnStartDateCustomerBiz").attr("value", result);
    //	} else if ($("#hdnDatePickerInputId").attr("value").indexOf("End") != -1){
    //		$("#hdnEndDateCustomerBiz").attr("value", result);
    //	}
    var date = $("#hdnDatePickerInputId").attr("value");
    if (date == "txtStartDateCustomerBookWeight") {
        $("#hdnStartDateCustomerBookWeight").attr("value", result);
    } else if (date == "txtEndDateCustomerBookWeight") {
        $("#hdnEndDateCustomerBookWeight").attr("value", result);
    } else if (date == "txtStartDateCustomerBookVolume") {
        $("#hdnStartDateCustomerBookVolume").attr("value", result);
    } else if (date == "txtEndDateCustomerBookVolume") {
        $("#hdnEndDateCustomerBookVolume").attr("value", result);
    } else if (date == "txtStartDateCustomerBookTaxInvoice") {
        $("#hdnStartDateCustomerBookTaxInvoice").attr("value", result);
    } else if (date == "txtEndDateCustomerBookTaxInvoice") {
        $("#hdnEndDateCustomerBookTaxInvoice").attr("value", result);
    } else if (date == "txtStartDateCustomerBookItemBalance") {
        $("#hdnStartDateCustomerBookItemBalance").attr("value", result);
    } else if (date == "txtEndDateCustomerBookItemBalance") {
        $("#hdnEndDateCustomerBookItemBalance").attr("value", result);
    }

    closeDatePicker();
    $("#" + $("#hdnDatePickerInputId").attr("value")).trigger("change");
}

//날짜 선택 다이얼로그 - 닫기 처리
function closeDatePicker() {
    $.mobile.changePage("#" + $("#hdnCallPageDiaglogDatePicker").attr("value"), { changeHash: false, reverse: true });
}

//날짜 선택 다이얼로그 - 년도 증가
function clickIncreaseYear() {
    var year = $("#txtYearDatePicker").attr("value");
    var newYear = parseInt(year, 10) + 1;
    $("#txtYearDatePicker").attr("value", newYear);
    var month = $("#txtMonthDatePicker").attr("value");
    var day = $("#txtDayDatePicker").attr("value");
    var lastDay = (new Date(newYear, month, 0)).getDate();
    if (parseInt(day, 10) > lastDay) {
        $("#txtDayDatePicker").attr("value", lastDay);
    }
    updateTitleDialogDatePicker($("#txtYearDatePicker").attr("value"), $("#txtMonthDatePicker").attr("value"), $("#txtDayDatePicker").attr("value"));
}

//날짜 선택 다이얼로그 - 월 증가
function clickIncreaseMonth() {
    var year = $("#txtYearDatePicker").attr("value");
    var month = $("#txtMonthDatePicker").attr("value");
    var newMonth = parseInt(month, 10) + 1;
    var day = $("#txtDayDatePicker").attr("value");
    if (parseInt(month, 10) == 12) {
        clickIncreaseYear();
        newMonth = 1;
        $("#txtMonthDatePicker").attr("value", 1);
    } else {
        $("#txtMonthDatePicker").attr("value", newMonth);
    }
    var lastDay = (new Date(year, newMonth, 0)).getDate();
    if (parseInt(day, 10) > lastDay) {
        $("#txtDayDatePicker").attr("value", lastDay);
    }
    updateTitleDialogDatePicker($("#txtYearDatePicker").attr("value"), $("#txtMonthDatePicker").attr("value"), $("#txtDayDatePicker").attr("value"));
}

//날짜 선택 다이얼로그 - 일 증가
function clickIncreaseDay() {
    var year = $("#txtYearDatePicker").attr("value");
    var month = $("#txtMonthDatePicker").attr("value");
    var day = $("#txtDayDatePicker").attr("value");
    var lastDay = (new Date(year, month, 0)).getDate();
    if (parseInt(day, 10) == lastDay) {
        clickIncreaseMonth();
        $("#txtDayDatePicker").attr("value", 1);
    } else {
        $("#txtDayDatePicker").attr("value", parseInt(day, 10) + 1);
    }
    updateTitleDialogDatePicker($("#txtYearDatePicker").attr("value"), $("#txtMonthDatePicker").attr("value"), $("#txtDayDatePicker").attr("value"));
}

//날짜 선택 다이얼로그 - 년도 감소
function clickDecreaseYear() {
    var year = $("#txtYearDatePicker").attr("value");
    var newYear = parseInt(year, 10) - 1;
    $("#txtYearDatePicker").attr("value", newYear);
    var month = $("#txtMonthDatePicker").attr("value");
    var day = $("#txtDayDatePicker").attr("value");
    var lastDay = (new Date(newYear, month, 0)).getDate();
    if (parseInt(day, 10) > lastDay) {
        $("#txtDayDatePicker").attr("value", lastDay);
    }
    updateTitleDialogDatePicker($("#txtYearDatePicker").attr("value"), $("#txtMonthDatePicker").attr("value"), $("#txtDayDatePicker").attr("value"));
}

//날짜 선택 다이얼로그 - 월 감소
function clickDecreaseMonth() {
    var year = $("#txtYearDatePicker").attr("value");
    var month = $("#txtMonthDatePicker").attr("value");
    var newMonth = parseInt(month, 10) - 1;
    if (parseInt(month, 10) == 1) {
        clickDecreaseYear();
        newMonth = 12;
        $("#txtMonthDatePicker").attr("value", 12);
    } else {
        $("#txtMonthDatePicker").attr("value", newMonth);
    }
    var day = $("#txtDayDatePicker").attr("value");
    var lastDay = (new Date(year, newMonth, 0)).getDate();
    if (parseInt(day, 10) > lastDay) {
        $("#txtDayDatePicker").attr("value", lastDay);
    }
    updateTitleDialogDatePicker($("#txtYearDatePicker").attr("value"), $("#txtMonthDatePicker").attr("value"), $("#txtDayDatePicker").attr("value"));
}

//날짜 선택 다이얼로그 - 일 감소
function clickDecreaseDay() {
    var day = $("#txtDayDatePicker").attr("value");
    if (parseInt(day, 10) == 1) {
        clickDecreaseMonth();
        var year = $("#txtYearDatePicker").attr("value");
        var month = $("#txtMonthDatePicker").attr("value");
        var lastDay = (new Date(year, month, 0)).getDate();
        $("#txtDayDatePicker").attr("value", lastDay);
    } else {
        $("#txtDayDatePicker").attr("value", parseInt(day, 10) - 1);
    }
    updateTitleDialogDatePicker($("#txtYearDatePicker").attr("value"), $("#txtMonthDatePicker").attr("value"), $("#txtDayDatePicker").attr("value"));
}

//날짜 더하기(v_day가 음수이면 날짜 빼기)
function addDay(ymd, v_day, delimiter) {
    var newDate = new Date(parseInt(ymd.substring(0, 4), 10), eval(ymd.substring(4, 6) + "-1"), ymd.substring(6));
    newDate.setDate(newDate.getDate() + v_day);
    return getFormatDate(newDate, delimiter);
}

//그 달의 첫번째 날
function firstDay(ymd, delimiter) {
    var newDate = new Date(parseInt(ymd.substring(0, 4), 10), eval(ymd.substring(4, 6) + "-1"), "01");
    return getFormatDate(newDate, delimiter);
}

//특정 컨트롤에 포커스 주기
function focusControl(inputId) {
    $("#" + inputId).focus().select();
}

//오늘 날짜 가져오기
function getToday(delimiter) {
    return getFormatDate(new Date(), delimiter);
}

//날짜를 특정 형식으로 가져오기
function getFormatDate(specifiedDate, delimiter) {
    if (delimiter == undefined) delimiter = "";
    var year = specifiedDate.getYear();
    if (year < 1000)
        year += 1900;
    var month = specifiedDate.getMonth() + 1;
    if (month < 10)
        month = "0" + month;
    var date = specifiedDate.getDate();
    if (date < 10)
        date = "0" + date;
    return year + delimiter + month + delimiter + date;
}

//헤더나 풋터를 고정하지 않도록 함.
function disableFixed(id) {
    $("#" + id).addClass("ui-fixed-hidden");
}

//헤더나 풋터를 고정함.
function enableFixed(id) {
    $("#" + id).removeClass("ui-fixed-hidden");
}

//문자열 내부에 일부 문자 모두 교체하기
function replaceAll(sourceString, targetString, replaceString) {
    return sourceString.split(targetString).join(replaceString);
}

//사업자등록번호 마스킹
function maskSaupNo(value) {
    var reg = /([0-9]{3})-?([0-9]{2})-?([0-9]{5})/;   //정규식
    value += "";                          //숫자를 문자열로 변환
    if (reg.test(value)) {
        value = value.substr(0, 3) + "-" + value.substr(3, 2) + "-" + value.substr(5, 5);
    }
    return value;
}

//주민등록번호 마스킹
function maskJuminNo(value) {
    var reg = /\d{6}(\-|)[1-4]\d{6}$/;   //정규식
    value += "";                          //숫자를 문자열로 변환
    if (reg.test(value)) {
        value = value.substr(0, 6) + "-" + value.substr(6, 7);
    }
    return value;
}

//천단위마다 쉼표 넣기
function insertComma(value) {
    //	var reg = /(^[+-]?\d+)(\d{3})/;   //정규식
    var reg = /(\-?\d+)(\d{3})($|\.\d+)/;   //정규식
    value += "";                          //숫자를 문자열로 변환
    if (reg.test(value)) {
        return value.replace(reg, function (str, p1, p2, p3) {
            return insertComma(p1) + "," + p2 + "" + p3;
        }
        );
    } else {
        return value;
    }
    //	while (reg.test(value)) {
    //		value = value.replace(reg, "$1" + "," + "$2");
    //	}
    //	return value;
}


//천단위마다 쉼표 넣기-숫자패드 이용을 위해 쉼표 처리 안하도록 수정됨.
function insertComma2(value) {
    //	var reg = /(\-?\d+)(\d{3})($|\.\d+)/;   //정규식
    //	value += "";                          //숫자를 문자열로 변환
    //  if(reg.test(value)){
    //	    return value.replace(reg, function(str, p1,p2,p3){
    //	           return insertComma(p1) + "," + p2 + "" + p3;
    //	          }
    //	    );
    //	} else {
    return value;
    //	}
}

//숫자에서 콤마 지우기
function deleteComma(value) {
    result = value.replace(/,/g, "");
    result = parseFloat(result, 10);
    if (isNaN(result)) {
        return 0;
    } else {
        return result;
    }
}

//숫자 input 포커스 될 때
function focusNumber(input, fixedId) {
    var inputId = input.id;
    var numberInput = $("#" + inputId);
    //	numberInput.attr("pattern", "[0-9]*");
    var value = deleteComma(numberInput.attr("value"));
    value = parseFloat(value, 10);
    numberInput.attr("value", value);
    numberInput.select();
    if (fixedId == undefined) {
    } else {
        disableFixed(fixedId);
    }
}

//숫자 input 빠져나올 때
function blurNumber(input, fixedId) {
    var inputId = input.id;
    var numberInput = $("#" + inputId);
    var inputType = numberInput.attr("type");
    var value = deleteComma(numberInput.attr("value"));
    //	var value = numberInput.attr("value");
    if (value == "") {
        value = 0;
    }
    var intValue = parseFloat(value, 10);
    if (isNaN(intValue)) {
        value = 0;
    }
    if (inputType == "number") {
        value = insertComma2(intValue);
    } else {
        value = insertComma(intValue);
    }
    numberInput.attr("value", value);
    //	numberInput.attr("pattern", "");
    if (fixedId == undefined) {
    } else {
        enableFixed(fixedId);
    }
}

//회차 input 빠져나올 때
function blurSN(input, fixedId) {
    var inputId = input.id;
    var numberInput = $("#" + inputId);
    var value = deleteComma(numberInput.attr("value"));
    if (value == "") {
        value = 1;
    }
    var intValue = parseInt(value, 10);
    if (isNaN(intValue)) {
        value = 1;
    }
    var strValue = '' + value;
    if (strValue.length == 1) {
        strValue = "0" + strValue;
    }
    numberInput.attr("value", strValue);
    //	numberInput.attr("pattern", "");

    if (fixedId == undefined) {
    } else {
        enableFixed(fixedId);
    }
}

//판매유형 HTML 가져오기
function getSaleTypeHtml(saleTypeName, imgBool) {
    var saleTypeCode = "1";
    if (saleTypeName == "일반") saleTypeCode = "0";
    else if (saleTypeName == "둘다") saleTypeCode = "2";
    if (imgBool == true) {
        var saleTypeIcon = "images/lbl_customer_type_" + saleTypeCode + ".png";
        return '<img src="' + saleTypeIcon + '" />';
    } else {
        return '<span style="font-size: 14px ; color: black ; " >[' + saleTypeName + ']</span>';
    }
}

//지불유형 HTML 가져오기
function getPayTypeHtml(payType, imgBool) {
    if (imgBool == true) {
        var payTypeIcon = "images/lbl_pay_type_" + payType + ".png";
        return '<img src="' + payTypeIcon + '" />';
    } else {
        var payTypeName = "현금";
        if (payType == "2") {
            payTypeName = "예금";
        } else if (payType == "3") {
            payTypeName = "카드";
        } else if (payType == "4") {
            payTypeName = "어음";
        } else if (payType == "A") {
            payTypeName = "외상";
        } else if (payType == "B") {
            payTypeName = "영수증";
        }
        return '<span style="font-size: 14px ; color: black ; " >[' + payTypeName + ']</span>';
    }
}

//미수유형 HTML 가져오기
function getUnpaidTypeHtml(unpaidTypeName, imgBool) {
    var unpaidTypeCode = "1";
    if (unpaidTypeName == "일반") unpaidTypeCode = "0";
    else if (unpaidTypeName == "둘다") unpaidTypeCode = "2";
    if (imgBool == true) {
        var unpaidTypeIcon = "images/lbl_customer_type_" + unpaidTypeCode + ".png";
        return '<img src="' + unpaidTypeIcon + '" />';
    } else {
        return '<span style="font-size: 14px ; color: black ; " >[' + unpaidTypeName + ']</span>';
    }
}

//수금유형 HTML 가져오기
function getCollectTypeHtml(collectType, imgBool) {
    if (imgBool == true) {
        var collectTypeIcon = "images/lbl_collect_type_" + collectType + ".png";
        if (collectTypeCode == " ") collectTypeIcon = "images/lbl_collect_type_0.png"; //값이 없을 때는 "0 방문"으로
        return '<img src="' + collectTypeIcon + '" />';
    } else {
        var collectTypeName = "방문";
        if (collectType == "1") {
            collectTypeName = "지로";
        } else if (collectType == "2") {
            collectTypeName = "예금";
        } else if (collectType == "3") {
            collectTypeName = "카드";
        } else if (collectType == "4") {
            collectTypeName = "어음";
        } else if (collectType == "5") {
            collectTypeName = "EDI";
        } else if (collectType == "6") {
            collectTypeName = "CMS";
        }
        return '<span style="font-size: 14px ; color: black ; " >[' + collectTypeName + ']</span>';
    }
}

////////////////////////////////////////////////////////////////////////

//생성자 함수를 이용해서 Arraylist 만들기
ArrayList = function arrayList() {
    this.list = []; //데이터를 저장할 수 있는 배열을 멤버필드로 선언한다.
    this.add = function (item) {//인자로 전달되는 데이터를 저장하는 함수
        this.list.push(item);//인자로 전달된 데이터를 자기 자신의 필드에 저장
    };
    this.get = function (index) {//인자로 전달되는 해당 인덱스의 값을 리턴 하는 함수
        return this.list[index];
    };
    this.removeAll = function () {//인자로 전달되는 해당 인덱스의 값을 삭제하는 함수
        this.list = []; //빈 배열을 대입해서 삭제하는 효과를 준다
    };
    this.size = function () {//현재 저장된 크기를 리턴하는 메소드
        return this.list.length;
    };
    this.remove = function (index) {//새로운 배열을 정의
        var newList = [];
        for (var i = 0; i < this.list.length; i++) {//반복문을 돌면서 인자로 전달된 인덱스를 제외한 모든 요소를 새 배열에 담는다.
            if (i != index) { //삭제할 인덱스가 아니라면
                newList.push(this.list[i]);
            }
            ;
        }
        ;
        this.list = newList;//새로 만든 배열을 멤버 필드에 저장한다.
    };
};

var isDraw = false; //현재 상태가 그리는 상태인지를 확인하는 여부
var pointList = new ArrayList(); //Point 객체를 저장할 배열객체 생성

//로딩이 끝났을때 호출되는 함수
function onloadCustomerSaftyCheckSign(signatureFileName) {
    if (signatureFileName != "") {
        $("#imgSignImageCustomerSaftyCheckSign").css("display", "inline-block");
        $("#divCanvasCustomerSaftyCheckSign").css("display", "none");
        $("#btnEditCustomerSaftyCheckSign").button("enable");
        $("#btnResetCustomerSaftyCheckSign").button("disable");
        $("#btnSaveCustomerSaftyCheckSign").button("disable");
    } else {
        $("#imgSignImageCustomerSaftyCheckSign").css("display", "none");
        $("#divCanvasCustomerSaftyCheckSign").css("display", "inline-block");
        $("#btnEditCustomerSaftyCheckSign").button("disable");
        $("#btnResetCustomerSaftyCheckSign").button("enable");
        $("#btnSaveCustomerSaftyCheckSign").button("enable");
    }
    var canvasId = "canvasCustomerSaftyCheckSign";
    clearCanvas(canvasId);
    var thisLineWidth = 3; //디폴트 선의 굵기
    var thisColor = "black"; //디폴트 색깔
    //화면의 폭과 높이 얻어오기
    var width = window.innerWidth;
    var height = window.innerHeight;

    //canvas 요소의 속성 지정하기
    $("#" + canvasId)
        .attr("width", width)
        .attr("height", height - 100)
        .attr("style", "background-color:white;cursor:crosshair");
    //마우스 다운 혹은 터치 다운이 일어났을 때
    $("#" + canvasId).bind("vmousedown touchstart", function (event) {//virtualmousedown jQuery모바일에서 추가된 것
        if (isDraw == false) {
            if (event && !isNaN(event.pageX)) {
                clearPoints();

                isDraw = true;

                //현재 위치를 객체에 기록한다.
                var point = new Point(event.pageX, event.pageY, thisLineWidth, thisColor, true);
                //생성한 객체를 배열에 담는다
                //pointList.add(point);
                addPoint(point);
            }
        }
    });
    //마우스 무브 혹은 터치한 상태로 무브했을때
    $("#" + canvasId).bind("vmousemove touchmove", function (event) {
        if (isDraw) {
            if (event && !isNaN(event.pageX)) {
                //현재 이벤트가 일어나는 점의 좌표를 객체에 담은 후 배열에 담는다.
                var point = new Point(event.pageX, event.pageY, thisLineWidth, thisColor, false);
                //pointList.add(point);
                addPoint(point);

                drawScreen(canvasId);//화면 그리기
            }
        }
    });
    //마우스업 혹은 터치를 종료하였을 때
    $("#" + canvasId).bind("vmouseup touchend", function (event) {
        isDraw = false;
    });
}

//로딩이 끝났을때 호출되는 함수
function onloadSignatureCustomerSaftyCheckInsert(signatureImage) {
    if (signatureImage && signatureImage.length > 0) {
        $("#imgSignatureImageCustomerSaftyCheckInsert").attr("src", signatureImage);
    }
    var canvasId = "canvasCustomerSaftyCheckInsert";
    clearCanvas(canvasId);
    var thisLineWidth = 3; //디폴트 선의 굵기
    var thisColor = "black"; //디폴트 색깔
    //화면의 폭과 높이 얻어오기
    var width = window.innerWidth - 32;			// 좌우마진(32)
    var height = window.innerHeight - 120;		// 상하마진(120, 헤더 및 버튼 높이 포함)

    $("#divCanvasCustomerSaftyCheckInsert").css("width", width);
    $("#divCanvasCustomerSaftyCheckInsert").css("height", height);

    $("#imgSignatureImageCustomerSaftyCheckInsert").css("width", width);
    $("#imgSignatureImageCustomerSaftyCheckInsert").css("height", height);

    //canvas 요소의 속성 지정하기
    $("#" + canvasId)
        .attr("width", width)
        .attr("height", height)
        .attr("style", "background-color:white;cursor:crosshair");
    //마우스 다운 혹은 터치 다운이 일어났을 때
    $("#" + canvasId).bind("vmousedown touchstart", function (event) {//virtualmousedown jQuery모바일에서 추가된 것
        event.preventDefault();
        if (isDraw == false) {
            if (event && !isNaN(event.pageX)) {
                clearPoints();

                isDraw = true;

                //터치포인트의 상대값을 알기위한 오프셋 정보
                var offsetPoint = getOffsetSignatureCustomerSaftyCheck("popupSignatureCustomerSaftyCheckInsert");
                //현재 위치를 객체에 기록한다.
                var point = new Point(event.pageX - offsetPoint.x, event.pageY - offsetPoint.y, thisLineWidth, thisColor, true);
                //생성한 객체를 배열에 담는다
                //pointList.add(point);
                addPoint(point);
            }
        }
    });
    //마우스 무브 혹은 터치한 상태로 무브했을때
    $("#" + canvasId).bind("vmousemove touchmove", function (event) {
        event.preventDefault();
        if (isDraw) {
            if (event && !isNaN(event.pageX)) {
                //터치포인트의 상대값을 알기위한 오프셋 정보
                var offsetPoint = getOffsetSignatureCustomerSaftyCheck("popupSignatureCustomerSaftyCheckInsert");

                //현재 이벤트가 일어나는 점의 좌표를 객체에 담은 후 배열에 담는다.
                var point = new Point(event.pageX - offsetPoint.x, event.pageY - offsetPoint.y, thisLineWidth, thisColor, false);
                //pointList.add(point);
                addPoint(point);

                drawScreen(canvasId);//화면 그리기
            }
        }
    });
    //마우스업 혹은 터치를 종료하였을 때
    $("#" + canvasId).bind("vmouseup touchend", function (event) {
        event.preventDefault();
        isDraw = false;
    });
}

//로딩이 끝났을때 호출되는 함수
function onloadSignatureCustomerSaftyCheckEdit(signatureImage) {
    if (signatureImage && signatureImage.length > 0) {
        $("#imgSignatureImageCustomerSaftyCheckEdit").attr("src", signatureImage);
    }

    var canvasId = "canvasCustomerSaftyCheckEdit";
    clearCanvas(canvasId);
    var thisLineWidth = 3; //디폴트 선의 굵기
    var thisColor = "black"; //디폴트 색깔
    //화면의 폭과 높이 얻어오기
    var width = window.innerWidth - 32;			// 좌우마진(32)
    var height = window.innerHeight - 120;		// 상하마진(120, 헤더 및 버튼 높이 포함)

    $("#divCanvasCustomerSaftyCheckEdit").css("width", width);
    $("#divCanvasCustomerSaftyCheckEdit").css("height", height);

    $("#imgSignatureImageCustomerSaftyCheckEdit").css("width", width);
    $("#imgSignatureImageCustomerSaftyCheckEdit").css("height", height);

    //canvas 요소의 속성 지정하기
    $("#" + canvasId)
        .attr("width", width)
        .attr("height", height)
        .attr("style", "background-color:white;cursor:crosshair");
    //마우스 다운 혹은 터치 다운이 일어났을 때
    $("#" + canvasId).bind("vmousedown touchstart", function (event) {//virtualmousedown jQuery모바일에서 추가된 것
        event.preventDefault();
        if (isDraw == false) {
            if (event && !isNaN(event.pageX)) {
                clearPoints();

                isDraw = true;

                //터치포인트의 상대값을 알기위한 오프셋 정보
                var offsetPoint = getOffsetSignatureCustomerSaftyCheck("popupSignatureCustomerSaftyCheckEdit");
                //현재 위치를 객체에 기록한다.
                var point = new Point(event.pageX - offsetPoint.x, event.pageY - offsetPoint.y, thisLineWidth, thisColor, true);
                //생성한 객체를 배열에 담는다
                //pointList.add(point);
                addPoint(point);
            }
        }
    });
    //마우스 무브 혹은 터치한 상태로 무브했을때
    $("#" + canvasId).bind("vmousemove touchmove", function (event) {
        event.preventDefault();
        if (isDraw) {
            if (event && !isNaN(event.pageX)) {
                //터치포인트의 상대값을 알기위한 오프셋 정보
                var offsetPoint = getOffsetSignatureCustomerSaftyCheck("popupSignatureCustomerSaftyCheckEdit");

                //현재 이벤트가 일어나는 점의 좌표를 객체에 담은 후 배열에 담는다.
                var point = new Point(event.pageX - offsetPoint.x, event.pageY - offsetPoint.y, thisLineWidth, thisColor, false);
                //pointList.add(point);
                addPoint(point);

                drawScreen(canvasId);//화면 그리기
            }
        }
    });
    //마우스업 혹은 터치를 종료하였을 때
    $("#" + canvasId).bind("vmouseup touchend", function (event) {
        event.preventDefault();
        isDraw = false;
    });
}

// 서명 수정하기 버튼 클릭 처리
function clickEditCustomerSaftyCheckSign() {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    $("#imgSignImageCustomerSaftyCheckSign").css("display", "none");		// 기존서명 비표시
    $("#divCanvasCustomerSaftyCheckSign").css("display", "inline-block");	// 서명입력란 표시
    /*
    $("#btnEditCustomerSaftyCheckSign").button("disable");
    $("#btnResetCustomerSaftyCheckSign").button("enable");
    $("#btnSaveCustomerSaftyCheckSign").button("enable");
    */
    $("#divEditCustomerSaftyCheckSign").css("display", "none");				// 수정버튼 비표시
    $("#divSaveCustomerSaftyCheckSign").css("display", "inline-block");		// 초기화/저장버튼 표시
}

//서명 저장하기 버튼 클릭 처리
function clickSaveSignatureCustomerSaftyCheckInsert(canvasId) {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    // 입력한 내용이 없을 경우
    if (pointList.size() == 0) {
        alert("서명이 입력되지 않았습니다.");
        return;
    }

    var signatureImage = document.getElementById(canvasId).toDataURL();

    $("#hdnSignatureImageCustomerSaftyCheckInsert").attr("value", signatureImage);
    $("#spnSignCustomerSaftyCheckInsert").html("서명됨");
    $("#btnSignCustomerSaftyCheckInsert").attr("value", "서명보기").button("refresh");

    $("#popupSignatureCustomerSaftyCheckInsert").popup("close");
}

//서명 저장하기 버튼 클릭 처리
function clickSaveSignatureCustomerSaftyCheckEdit(canvasId) {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    // 입력한 내용이 없을 경우
    if (pointList.size() == 0) {
        alert("서명이 입력되지 않았습니다.");
        return;
    }

    var signatureImage = document.getElementById(canvasId).toDataURL();

    $("#hdnSignatureImageCustomerSaftyCheckEdit").attr("value", signatureImage);
    $("#spnSignCustomerSaftyCheckEdit").html("서명됨");
    $("#btnSignCustomerSaftyCheckEdit").attr("value", "서명보기").button("refresh");

    $("#popupSignatureCustomerSaftyCheckEdit").popup("close");
}

/////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////

//로딩이 끝났을때 호출되는 함수
function onloadSignatureCustomerSaftyCheckTankInsert(signatureImage) {
    if (signatureImage && signatureImage.length > 0) {
        $("#imgSignatureImageCustomerSaftyCheckTankInsert").attr("src", signatureImage);
    }
    var canvasId = "canvasCustomerSaftyCheckTankInsert";
    clearCanvas(canvasId);
    var thisLineWidth = 3; //디폴트 선의 굵기
    var thisColor = "black"; //디폴트 색깔
    //화면의 폭과 높이 얻어오기
    var width = window.innerWidth - 32;			// 좌우마진(32)
    var height = window.innerHeight - 120;		// 상하마진(120, 헤더 및 버튼 높이 포함)

    $("#divCanvasCustomerSaftyCheckTankInsert").css("width", width);
    $("#divCanvasCustomerSaftyCheckTankInsert").css("height", height);

    $("#imgSignatureImageCustomerSaftyCheckTankInsert").css("width", width);
    $("#imgSignatureImageCustomerSaftyCheckTankInsert").css("height", height);

    //canvas 요소의 속성 지정하기
    $("#" + canvasId)
        .attr("width", width)
        .attr("height", height)
        .attr("style", "background-color:white;cursor:crosshair");
    //마우스 다운 혹은 터치 다운이 일어났을 때
    $("#" + canvasId).bind("vmousedown touchstart", function (event) {//virtualmousedown jQuery모바일에서 추가된 것
        event.preventDefault();
        if (isDraw == false) {
            if (event && !isNaN(event.pageX)) {
                clearPoints();

                isDraw = true;

                //터치포인트의 상대값을 알기위한 오프셋 정보
                var offsetPoint = getOffsetSignatureCustomerSaftyCheck("popupSignatureCustomerSaftyCheckTankInsert");
                //현재 위치를 객체에 기록한다.
                var point = new Point(event.pageX - offsetPoint.x, event.pageY - offsetPoint.y, thisLineWidth, thisColor, true);
                //생성한 객체를 배열에 담는다
                //pointList.add(point);
                addPoint(point);
            }
        }
    });
    //마우스 무브 혹은 터치한 상태로 무브했을때
    $("#" + canvasId).bind("vmousemove touchmove", function (event) {
        event.preventDefault();
        if (isDraw) {
            if (event && !isNaN(event.pageX)) {
                //터치포인트의 상대값을 알기위한 오프셋 정보
                var offsetPoint = getOffsetSignatureCustomerSaftyCheck("popupSignatureCustomerSaftyCheckTankInsert");

                //현재 이벤트가 일어나는 점의 좌표를 객체에 담은 후 배열에 담는다.
                var point = new Point(event.pageX - offsetPoint.x, event.pageY - offsetPoint.y, thisLineWidth, thisColor, false);
                //pointList.add(point);
                addPoint(point);

                drawScreen(canvasId);//화면 그리기
            }
        }
    });
    //마우스업 혹은 터치를 종료하였을 때
    $("#" + canvasId).bind("vmouseup touchend", function (event) {
        event.preventDefault();
        isDraw = false;
    });
}

//로딩이 끝났을때 호출되는 함수
function onloadSignatureCustomerSaftyCheckTankEdit(signatureImage) {
    if (signatureImage && signatureImage.length > 0) {
        $("#imgSignatureImageCustomerSaftyCheckTankEdit").attr("src", signatureImage);
    }

    var canvasId = "canvasCustomerSaftyCheckTankEdit";
    clearCanvas(canvasId);
    var thisLineWidth = 3; //디폴트 선의 굵기
    var thisColor = "black"; //디폴트 색깔
    //화면의 폭과 높이 얻어오기
    var width = window.innerWidth - 32;			// 좌우마진(32)
    var height = window.innerHeight - 120;		// 상하마진(120, 헤더 및 버튼 높이 포함)

    $("#divCanvasCustomerSaftyCheckTankEdit").css("width", width);
    $("#divCanvasCustomerSaftyCheckTankEdit").css("height", height);

    $("#imgSignatureImageCustomerSaftyCheckTankEdit").css("width", width);
    $("#imgSignatureImageCustomerSaftyCheckTankEdit").css("height", height);

    //canvas 요소의 속성 지정하기
    $("#" + canvasId)
        .attr("width", width)
        .attr("height", height)
        .attr("style", "background-color:white;cursor:crosshair");
    //마우스 다운 혹은 터치 다운이 일어났을 때
    $("#" + canvasId).bind("vmousedown touchstart", function (event) {//virtualmousedown jQuery모바일에서 추가된 것
        event.preventDefault();
        if (isDraw == false) {
            if (event && !isNaN(event.pageX)) {
                clearPoints();

                isDraw = true;

                //터치포인트의 상대값을 알기위한 오프셋 정보
                var offsetPoint = getOffsetSignatureCustomerSaftyCheck("popupSignatureCustomerSaftyCheckTankEdit");
                //현재 위치를 객체에 기록한다.
                var point = new Point(event.pageX - offsetPoint.x, event.pageY - offsetPoint.y, thisLineWidth, thisColor, true);
                //생성한 객체를 배열에 담는다
                //pointList.add(point);
                addPoint(point);
            }
        }
    });
    //마우스 무브 혹은 터치한 상태로 무브했을때
    $("#" + canvasId).bind("vmousemove touchmove", function (event) {
        event.preventDefault();
        if (isDraw) {
            if (event && !isNaN(event.pageX)) {
                //터치포인트의 상대값을 알기위한 오프셋 정보
                var offsetPoint = getOffsetSignatureCustomerSaftyCheck("popupSignatureCustomerSaftyCheckTankEdit");

                //현재 이벤트가 일어나는 점의 좌표를 객체에 담은 후 배열에 담는다.
                var point = new Point(event.pageX - offsetPoint.x, event.pageY - offsetPoint.y, thisLineWidth, thisColor, false);
                //pointList.add(point);
                addPoint(point);

                drawScreen(canvasId);//화면 그리기
            }
        }
    });
    //마우스업 혹은 터치를 종료하였을 때
    $("#" + canvasId).bind("vmouseup touchend", function (event) {
        event.preventDefault();
        isDraw = false;
    });
}

// 서명 수정하기 버튼 클릭 처리
function clickEditCustomerSaftyCheckTankSign() {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    $("#imgSignImageCustomerSaftyCheckTankSign").css("display", "none");		// 기존서명 비표시
    $("#divCanvasCustomerSaftyCheckTankSign").css("display", "inline-block");	// 서명입력란 표시

    $("#divEditCustomerSaftyCheckTankSign").css("display", "none");				// 수정버튼 비표시
    $("#divSaveCustomerSaftyCheckTankSign").css("display", "inline-block");		// 초기화/저장버튼 표시
}

//서명 저장하기 버튼 클릭 처리
function clickSaveSignatureCustomerSaftyCheckTankInsert(canvasId) {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    // 입력한 내용이 없을 경우
    if (pointList.size() == 0) {
        alert("서명이 입력되지 않았습니다.");
        return;
    }

    var signatureImage = document.getElementById(canvasId).toDataURL();

    $("#hdnSignatureImageCustomerSaftyCheckTankInsert").attr("value", signatureImage);
    //$("#spnSignCustomerSaftyCheckTankInsert").html("서명됨");
    $("#btnSignCustomerSaftyCheckTankInsert").attr("value", "서명보기").button("refresh");

    $("#popupSignatureCustomerSaftyCheckTankInsert").popup("close");
}

//서명 저장하기 버튼 클릭 처리
function clickSaveSignatureCustomerSaftyCheckTankEdit(canvasId) {
    if (!hasPermission("hdnMenuPermissionSaftyCheck", ["0"])) {
        alert("권한이 없습니다.");
        return;
    }

    // 입력한 내용이 없을 경우
    if (pointList.size() == 0) {
        alert("서명이 입력되지 않았습니다.");
        return;
    }

    var signatureImage = document.getElementById(canvasId).toDataURL();

    $("#hdnSignatureImageCustomerSaftyCheckTankEdit").attr("value", signatureImage);
    //$("#spnSignCustomerSaftyCheckTankEdit").html("서명됨");
    $("#btnSignCustomerSaftyCheckTankEdit").attr("value", "서명보기").button("refresh");

    $("#popupSignatureCustomerSaftyCheckTankEdit").popup("close");
}

/////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////


//화면을 그리는 함수
function drawScreen(canvasId) {
    var canvas = document.getElementById(canvasId);//캔바스 객체의 참조값 얻어오기
    var context = canvas.getContext("2d");//그림을 그리기 위한 context 객체 얻어오기

    //console.log("Point Size:" + pointList.size());

    if (pointList.size() > 2) { //점이 두개 이상일 때 그린다.
        for (var i = 0; i < pointList.size(); i++) {//반복문 돌면서 이벤트가 일어난 곳의 좌표를 연결한다.
            var point = pointList.get(i);//배열에서 i번째 point 객체를 얻어온다.
            if (point.isStart) {//시작점이라면
                context.beginPath();
                context.moveTo(point.x, point.y);
            } else {//선을 그린다
                context.lineWidth = point.width; //선의굵기
                context.lineTo(point.x, point.y);//목표 좌표
                context.strokeStyle = point.color; //선의 색
                context.stroke(); //그린다
            }
        }
    }
}

//각각의 점을 기록할 Point 객체의 생성자 함수
function Point(x, y, width, color, isStart) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.color = color;
    this.isStart = isStart;
}

function addPoint(point) {

    //console.log("Point X:" + point.x + " / Y:" + point.y + " / bStart:" + point.isStart);
    /*
    var length = pointList.size();
    if (length > 0) {
        var prevPoint = pointList.get(length - 1);

        var deltaX = point.x - prevPoint.x;
        var deltaY = point.y - prevPoint.y;
        var distance = deltaX * deltaX + deltaY * deltaY;

        if (distance > 25) {
            pointList.add(point);
        }
    } else {
        pointList.add(point);
    }
    */
    pointList.add(point);
}

// 캔바스 초기화
function clearCanvas(canvasId) {
    var canvas = document.getElementById(canvasId);//캔바스 객체의 참조값 얻어오기
    var context = canvas.getContext("2d");//그림을 그리기 위한 context 객체 얻어오기
    context.clearRect(0, 0, canvas.width, canvas.height);
    clearPoints();
}

function clearPoints() {
    if (pointList) {
        pointList.removeAll();
    } else {
        pointList = new ArrayList();
    }
}

function hasPermission(tagId, requiredPermissions) {

    console.log("tagId===>", tagId);
    console.log("tagId===>", tagId);
    console.log("tagId===>", tagId);
    console.log("tagId===>", tagId);

    var userPermission = $("#" + tagId).attr("value");


    for (var requiredPermission in requiredPermissions) {
        if (userPermission == requiredPermission) {
            return true;
        }
    }
    return false;
}

function getOffsetSignatureCustomerSaftyCheck(tagId) {
    var top = $("#" + tagId + "-popup").css("top");
    var left = $("#" + tagId + "-popup").css("left");
    var headerHeight = $("#" + tagId + " > div").height();

    top = top.replace("px", "");
    left = left.replace("px", "");

    return new Point(new Number(left), new Number(top) + headerHeight);
}

//하단 메뉴의 선택상태 재설정하기
function resetActiveStateFooterCustomerBiz() {
    var currentPage = $("#hdnCurrentPage").attr("value");
    var selectId = -1;
    if (currentPage == "pageCustomerDetail") {
        selectId = 0;
    } else if ((currentPage == "pageCustomerBookWeight")
        || (currentPage == "pageCustomerBookVolume")
        || (currentPage == "pageCustomerBookTaxInvoice")
        || (currentPage == "pageCustomerBookItemBalance")) {
        selectId = 1;
    } else if ((currentPage == "pageCustomerSaleWeightInsert")
        || (currentPage == "pageCustomerSaleVolumeInsert")) {
        selectId = 2;
    } else if (currentPage == "pageCustomerReadMeterInsert") {
        selectId = 3;
    } else if (currentPage == "pageCustomerCollect") {
        selectId = 4;
    } else if ((currentPage == "pageCustomerSaftyCheckList")
        || (currentPage == "pageCustomerSaftyCheckEdit")
        || (currentPage == "pageCustomerSaftyCheckInsert")
        || (currentPage == "pageCustomerSaftyCheckTankEdit")
        || (currentPage == "pageCustomerSaftyCheckTankInsert")) {
        selectId = 5;
    }

    if (selectId > 0) {
        $("#" + currentPage + " > footer div[id^='footer'] .ui-btn").each(function (index) {
            if (index + 1 == selectId) {
                if (!$(this).hasClass('ui-btn-active')) $(this).addClass('ui-btn-active');
            } else {
                $(this).removeClass('ui-btn-active');
                $(this).blur();
            }
        });
    } else if (selectId == 0) {
        $("#" + currentPage + " > footer .ui-btn").each(function (index) {
            $(this).removeClass('ui-btn-active');
            $(this).blur();
        });
    }
}

function isValidateFormat(value, format) {
    if (!value || !value.length) return false;

    if (value.length != 7) return false;

    var valueSplit = value.split("-");
    if (valueSplit.length != 2) return false;
    if (valueSplit[1].length != 2) return false;

    if (Number(valueSplit[1]) <= 12 && Number(valueSplit[1]) >= 1) return true;

    return false;
}

/////////////////////////////////////////////////////////////////////////////////
///WORKING///////////////////////////////////////////////////////////////////////
function showDialogPipeLengthOptions(pageId, dataInputId) {
    $("#hdnPageScrollTop").attr("value", $(window).scrollTop());

    $("#hdnCallPageDialogPipeLengthOptions").attr("value", pageId);		// 다이얼로그의 부모 ID 저장
    $.mobile.changePage("#dialogPipeLengthOptions", { changeHash: false, role: "dialog", reverse: true });	// 다이얼로그로 화면전환
    $("#hdnDialogPipeLengthOptionsInputId").attr("value", dataInputId);		// 데이터를 표시할 ID 저장

    var currentPipeLengthOption = $("#" + dataInputId).attr("value");	// 현재값을 불러오기
    $('input:radio[name="rdoPipeLengthOptions"]').each(function () {
        if ($(this).val() == currentPipeLengthOption) {
            $(this).attr('checked', 'checked');
        } else {
            $(this).removeAttr('checked');
        }
    });
    $('input:radio[name="rdoPipeLengthOptions"]').checkboxradio("refresh");
}

//파이프길이 옵션 다이얼로그 - 파이프길이 옵션 선택하기
function clickOkPipeLengthOptions() {
    var newPipeLengthOption = $('input:radio[name="rdoPipeLengthOptions"]:checked').val();
    var dataInputId = $("#hdnDialogPipeLengthOptionsInputId").attr("value");

    if (newPipeLengthOption != "") {
        $("#" + dataInputId).attr("value", newPipeLengthOption);
    } else {
        var prevValue = $("#" + dataInputId).attr("value");
        $("#" + dataInputId).attr("value", '');
        $("#" + dataInputId).attr("value", prevValue);
    }

    closePipeLengthOptions();
    $("#" + dataInputId).trigger("change");
    $("#" + dataInputId).focus();
}

//파이프길이 옵션 다이얼로그 - 닫기 처리
function closePipeLengthOptions() {
    $.mobile.changePage("#" + $("#hdnCallPageDialogPipeLengthOptions").attr("value"), {
        changeHash: false,
        reverse: true
    });
    $.mobile.silentScroll(parseInt($("#hdnPageScrollTop").attr("value")));
}

function sec(text) {
    return btoa(text)
}

/////////////////////////////////////////////////////////////////////////////////
