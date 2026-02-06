//variables
var myHost = '.';

//draw my location marker
var map;
var mapZoom = 8;
var markerArray = [];      // 거래처 마커 정보
var infoWindowsArray = [];      // 거래처 인포윈도우 정보
var oCustomerArray;


var pinIcons = ['',myHost + '/assets/icons/pin/img-location-triangle-01@2x.png',myHost + '/assets/icons/pin/img-location-triangle-02@2x.png',myHost + '/assets/icons/pin/img-location-triangle-03@2x.png',myHost + '/assets/icons/pin/img-location-triangle-04@2x.png',myHost + '/assets/icons/pin/img-location-triangle-05@2x.png'];
var pinDots = ['',myHost + '/assets/icons/pin/color-pin-01@2x.png',myHost + '/assets/icons/pin/color-pin-02@2x.png',myHost + '/assets/icons/pin/color-pin-03@2x.png',myHost + '/assets/icons/pin/color-pin-04@2x.png',myHost + '/assets/icons/pin/color-pin-05@2x.png'];
var pinEtcs = ['',myHost + '/assets/icons/pin/company_marker.png'];
var size = [{'x':6,'y':6},{'x':12,'y':18},{'x':24,'y':30},{'x':30,'y':40}];
var levelArray = ["전체", "긴급", "경보", "예정", "정상"];	// 잔량레벨
var stateArray = ["미표시", "미수신", "동일레벨", "배터리"];	// 발신기상태

//var markerZindex = [40000, 30000, 20000, 10000];
var markerZindex = 0;

var clientLatLng;
var bounds;

var radiusArray = [];
var maxDistance = 5000;
var maxRadius = 100000;		// 최대반경 (100km)

var bulkTrackingLines = [];
var bulkLastLocationMarkers = [];
var bulkRouteTimeMarkers = [];
var bulkChargeLocationMarkers = [];
var bulkMarkerZindex = 0;

var bulkBounds = null;

var prevSearchDate = null;

$(document).ready(function(){	
	// 컴포넌트 초기화
	initCompoments();

    // 지도 초기화
    map = new naver.maps.Map('nmap-container', {
        zoom: mapZoom,
        center: clientLatLng,
        logoControl: false,
        mapDataControl: false,
        zoomControl: true,
        zoomControlOptions: {position: 3},
    	mapTypeControl: true,
    	mapTypeControlOptions: {position: 3}
    });

    // 벌크로리 목록
    $('#account-list').slimScroll({
        width: '290px',
        height: '100%',
    });

    // 거래처 레벨별 제어
    $('.level_select_btn').on('click', function() {
    	
    	// [발신기 상태별 제어]버튼 모두선택 헤제
    	initStateSelect();

        if($(this).attr("id") != 'level_select_all') {
            $('#level_select_all').removeClass("bold");
        } else {
            $('.level_select_btn').each(function(index) {
                $(this).removeClass("bold");
            });
        }

        if($(this).hasClass("bold")) {
            $(this).removeClass("bold");
        } else {
            $(this).addClass("bold");
        }

        var level = getLevelGroup();

        var indicator_level_str = level.join("|");
        $("#indicator_level").val(indicator_level_str);
        
        // 모두해제시 전체선택
        if(indicator_level_str == '') {
            $('#level_select_all').addClass("bold");
        }
        
        filterCustomer({level:level});
    });
    
    // 발신기 상태별 제어
    $('.state_select_btn').on('click', function() {

    	// 미선택인 버튼을 누른 경우
    	if(!$(this).hasClass("bold")) {    		
	    	// [거래처 레벨별 제어]버튼 모두선택 헤제
    		initLevelSelect();
	        
	        $('.state_select_btn').each(function(index) {
                $(this).removeClass("bold");
            });
	        
	        $(this).addClass("bold");	// 선택상태로 표시
	        
	        var state = getState();
	        $("#indicator_state").val(state);

	        filterCustomer({state:state});
    	}
    });
    
    // 거래처 목록리 선택 이벤트 처리
    $('.account_info_cell.clickable').live('click',function() {
        markerSelected($(this),$(this).attr('kind'),$(this).attr('index'));

        //change selected cell color
        $('.account_info_cell').css('background','#ffffff');
        $(this).css('background','#ebebeb');
    });

    // 검색 버튼 이벤트 처리
    $('#search_btn').on('click', function(e) {
    	e.preventDefault();
    	
    	$("#keyword").val($("#search_keyword").val());
    	drawPOIs(clientLatLng);
    });
});

function initCompoments() {
    // 벌크로리,사원 콤보박스 초기화
	$('#sw_list').selectpicker('refresh');
    $('#bulk_list').selectpicker('refresh');
    
    // 변경시 이벤트 처리
    ComponentsBootstrapSelect.init();
    
    // 일자선택 컴포넌트 초기
    $('#datepicker_search').datepicker({
    	format: "yyyy-mm-dd",
    	language: "kr",
    	autoclose: true
    });
    $('#datepicker_search').datepicker('setDate', 'today');
    $('#datepicker_search').on('changeDate', function (ev) {
        if (ev.date) {
        	clickSearch();
        } else {
        	if (prevSearchDate) {
        		$('#datepicker_search').datepicker('setDate', prevSearchDate);
        	}
        }
    }).on('show', function (ev) {
    	prevSearchDate = $("#datepicker_search").data('datepicker').getFormattedDate('yyyy-mm-dd');        
    });
    //$("#datepicker_search").data('datepicker').getFormattedDate('yyyy-mm-dd');
    
    // 체크박스    
    $("#chb_loc_bulk").change(function() {
    	showLastLocation($("#chb_loc_bulk").is(":checked"));
    });
    
    $("#chb_loc_charge").change(function() {
    	showChargeLocation($("#chb_loc_charge").is(":checked"));
    });
    
    $("#chb_route").change(function() {
    	showRoute($("#chb_route").is(":checked"));
    });
    
    $("#chb_route_time").change(function() {
    	showRouteTime($("#chb_route_time").is(":checked"));
    });
    
    // Button
    $('#btn_init').click(function() {
    	clickInit(true);
    });
    
    $('#btn_search').click(function() {
    	clickSearch();
    });
    
}

function clickInit(bComponent) {
	if (bComponent) {
		$("#chb_loc_bulk").attr("checked", true);
		$("#chb_loc_charge").attr("checked", true);
		$("#chb_route").attr("checked", true);
		$("#chb_route_time").attr("checked", true);
		
		$('#sw_list').selectpicker('val', '');
		$('#bulk_list').selectpicker('val', '');
	    
	    $('#datepicker_search').datepicker('setDate', 'today');
	}
	
	// 정보 지우기
	clearRoute();
	clearRouteTime();
	clearLastLocation();
	clearChargeLocation();
	
	// 차량목록(마지막위치) 지우기
	$("#bulk-list").html("");
}

function clickSearch() {
	clickInit(false);
	
	var bulkCode = $("#bulk_list").val();
	var sawonCode = $("#sw_list").val();
	var searchDate = $("#datepicker_search").data('datepicker').getFormattedDate('yyyymmdd');
	
	$.ajax({
        url: "./bulk_info_ajax.jsp",
        type: "post",
        data: {
        	qid : $("#qid").val(),
			bulkCode : bulkCode,
			sawonCode : sawonCode,
			searchDate : searchDate
        },
        success:function(json) {
        	
        	// 충전위치
        	if (json.bulkChargeStates) {
        		updateChargeLocation(json.bulkChargeStates, true);
        	}
        	
        	// 궤적정보 (궤적시간, 이동경로)
        	if (json.bulkGpsLines) {
        		updateRoute(json.bulkGpsLines, true);
        	}

        	// 현재위치
        	if (json.bulkLastGpss) {
        		updateLastLocation(json.bulkLastGpss, true);
        	}
        	
        	// 지도 이동
        	if (bulkBounds) {
        		map.fitBounds(bulkBounds);
        		map.panToBounds(bulkBounds);
        	}
        }
    });
}

function showChargeLocation(bShow) {
	/*
	if (bShow) {
		for (var i in bulkChargeLocationMarkers) {
			bulkChargeLocationMarkers[i].setMap(map);
    	}
	} else {
		for (var i in bulkChargeLocationMarkers) {
			bulkChargeLocationMarkers[i].setMap(null);
    	}
	}
	*/
	for (var i in bulkChargeLocationMarkers) {
		bulkChargeLocationMarkers[i].setVisible(bShow);
	}
}

function clearChargeLocation() {
	showChargeLocation(false);
	for (var i in bulkChargeLocationMarkers) {
		bulkChargeLocationMarkers[i].setMap(null);
	}
	
	bulkChargeLocationMarkers = [];
}

function updateChargeLocation(bulkChargeStates, useBounds) {
	var visibleChargeLocation = $("#chb_loc_charge").is(":checked");
	var visibleChargeTime = $("#chb_route_time").is(":checked");
		
	for (var i in bulkChargeStates) {
		var oBulkChargeState = bulkChargeStates[i];
		
		var nCurLat = Number(oBulkChargeState.gpsX);	// 위도
		var nCurLng = Number(oBulkChargeState.gpsY);	// 경도
		
		var curLatLng = new naver.maps.LatLng(nCurLat, nCurLng);
		
		bulkRouteTimeMarkers[i] = addBulkChargeTimeMarker(curLatLng, oBulkChargeState, visibleChargeTime);
		bulkChargeLocationMarkers[i] = addBulkChargeLocationMarker(curLatLng, oBulkChargeState, visibleChargeLocation);
		
		if (useBounds) {
			setBounds(curLatLng);
		}
	}
}

function showRoute(bShow) {
	/*
	if (bShow) {
		for (var i in bulkTrackingLines) {
			bulkTrackingLines[i].setMap(map);
    	}
	} else {
		for (var i in bulkTrackingLines) {
			bulkTrackingLines[i].setMap(null);
    	}
	}
	*/
	for (var i in bulkTrackingLines) {
		bulkTrackingLines[i].setVisible(bShow);
	}
}

function clearRoute() {
	//showRoute(false);
	for (var i in bulkTrackingLines) {
		bulkTrackingLines[i].setMap(null);
	}
	
	bulkTrackingLines = [];
}

function updateRoute(bulkGpsLines, useBounds) {
	var prevLatLng = null;
	var routePathArray = [];
	var strokeWeight = 4;
	
	var visibleRoute = $("#chb_route").is(":checked");
	//var visibleRouteTime = $("#chb_route_time").is(":checked");
	
	for (var i in bulkGpsLines) {
		var oBulkGpsLine = bulkGpsLines[i];
		
		var nCurLat = Number(oBulkGpsLine.gpsY);
		var nCurLng = Number(oBulkGpsLine.gpsX);
		
		var curLatLng = new naver.maps.LatLng([nCurLat, nCurLng]);
		
		//bulkRouteTimeMarkers[i] = addBulkRouteTimeMarker(curLatLng, oBulkGpsLine, visibleRouteTime);
		
		if (prevLatLng) {
			routePathArray.push([prevLatLng, curLatLng]);
		}
		
		prevLatLng = curLatLng;
		
		if (useBounds) {
			setBounds(curLatLng);
		}
	}
	
	// 궤적 표시
    for (var i in routePathArray) {
    	bulkTrackingLines[i] = new naver.maps.Polyline({
	        path: routePathArray[i],
	        map: map,
	        endIcon: naver.maps.PointingIcon.BLOCK_ARROW,
	        strokeColor: '#2A8AFE',
	        strokeWeight: strokeWeight,
	        visible: visibleRoute
	    });
    }
}

function showLastLocation(bShow) {
	/*
	if (bShow) {
		for (var i in bulkLastLocationMarkers) {
			bulkLastLocationMarkers[i].setVisible(checked);
    	}
	} else {
		for (var i in bulkLastLocationMarkers) {
			bulkLastLocationMarkers[i].setMap(null);
    	}
	}
	*/
	for (var i in bulkLastLocationMarkers) {
		bulkLastLocationMarkers[i].setVisible(bShow);
	}
}

function clearLastLocation() {
	for (var i in bulkLastLocationMarkers) {
		bulkLastLocationMarkers[i].setMap(null);
	}
	
	bulkLastLocationMarkers = [];
}

function updateLastLocation(bulkLastGpss, useBounds) {
	var visibleLastLocation = $("#chb_loc_bulk").is(":checked");
	
	var bulkCount = 0;
    var str = "<ul class='bulk_lists_cell'>";
	
	for (var i in bulkLastGpss) {
		bulkCount++;
		var oBulkLastGps = bulkLastGpss[i];
		
		var nCurLat = Number(oBulkLastGps.gpsX);	// 위도
		var nCurLng = Number(oBulkLastGps.gpsY);	// 경도
		
		var carType = oBulkLastGps.carType;
		var carName = oBulkLastGps.carName;
		
		var curLatLng = new naver.maps.LatLng(nCurLat, nCurLng);
		
		bulkLastLocationMarkers[i] = addBulkLocationMarker(curLatLng, oBulkLastGps, visibleLastLocation);

    	str +=  '<li class="bulk_info_cell clearfix'+((nCurLat > 0 && nCurLng > 0) ? " clickable" : "")+'" index="'+bulkCount+'">';
    	str +=  '   <div class="bulk_title">';
    	str +=  '		<div class="bulk_mark bulk_type_'+carType+'"></div>';
    	str +=  '       <div class="bulk_name">' + carName + '</div>';
    	str +=  '	</div>';
    	str +=  '</li>';
    	
    	if (useBounds) {
    		setBounds(curLatLng);
		}
	}

    if(bulkCount == 0) {
        str += "\
            <li><center><h5 style=' line-height:100px;'>결과가 없습니다.</h5></center></li>\
        ";
    }
    str += "<ul>";
    
    $("#bulk-list").html(str);
}

function showRouteTime(bShow) {
	/*
	if (bShow) {
		for (var i in bulkRouteTimeMarkers) {
			bulkRouteTimeMarkers[i].setMap(map);
    	}
	} else {
		for (var i in bulkRouteTimeMarkers) {
			bulkRouteTimeMarkers[i].setMap(null);
    	}
	}
	*/
	for (var i in bulkRouteTimeMarkers) {
		bulkRouteTimeMarkers[i].setVisible(bShow);
	}
}

function clearRouteTime() {
	//showRouteTime(false);
	for (var i in bulkRouteTimeMarkers) {
		bulkRouteTimeMarkers[i].setMap(null);
	}
	
	bulkRouteTimeMarkers = [];
}

function setBounds(latlng) {
	// 지도 표시 영역 설정
    if (bulkBounds) {
    	bulkBounds.extend(latlng);
    } else {
    	bulkBounds = new naver.maps.PointBounds(latlng, latlng);
    }
}

function addBulkLocationMarker(latlng, data, visible) {	
	var width = 30;
	var height = 30;
	
	var carName = data.carName;
	
	var marker = new naver.maps.Marker({
        position: {lat: parseFloat(latlng.lat()), lng: parseFloat(latlng.lng())},
        map: map,
		icon: {
			content: [
			          '<div class="bulk_info_cell bulk_map">',
			          	'<div class="bulk_mark bulk_type_' + data.carType + '"></div>',
			          	'<div class="bulk_name">' + carName + '</div>',
			          '</div>'
			          ].join(''),
			size: new naver.maps.Size(width, height),
			anchor: new naver.maps.Point(width/2, height/2)
              
		},
        zIndex: bulkMarkerZindex++,
        visible: visible,
        data: data
    });

    return marker;
}

function addBulkChargeLocationMarker(latlng, data, visible) {
	/*
	var width = 144;
	var height = 78;

	var chargeDateTime = getFormattedShortDateTime(data.chargeDate, data.chargeTime);
	
	var marker = new naver.maps.Marker({
        position: {lat: parseFloat(latlng.lat()), lng: parseFloat(latlng.lng())},
        map: map,
		icon: {
			content: [
			          '<div class="bulk_charge_info_cell">',
			          	'<div class="chargeSeqNo">일련번호 : ' + data.chargeSeqNo + '</div>',
			          	'<div class="chargeDate">충전일시 : ' + chargeDateTime + '</div>',
			          	'<div class="chargeVolume">충전량(Kg) : ' + data.chargeVolumeKg + '</div>',
			          	'<div class="customerName">거래처명 : ' + data.customerName + '</div>',
			          '</div>'
			          ].join(''),
			size: new naver.maps.Size(width, height),
			anchor: new naver.maps.Point(0, 0)
              
		},
        //zIndex: bulkMarkerZindex++,
        visible: visible,
        data: data
    });

    return marker;
    */
	var width = 20;
	var height = 20;
	
	var chargeDateTime = getFormattedShortDateTime(data.chargeDate, data.chargeTime);
	
	var marker = new naver.maps.Marker({
        position: {lat: parseFloat(latlng.lat()), lng: parseFloat(latlng.lng())},
        map: map,
		icon: {
			content: [
			          '<div class="bulk_charge_info_cell">',
			          	'<div class="chargeSeqNo">'+ data.chargeSeqNo + '</div>',
			          '</div>'
			          ].join(''),
			size: new naver.maps.Size(width, height),
			anchor: new naver.maps.Point(width/2, height/2)
              
		},
        zIndex: bulkMarkerZindex++,
        visible: visible,
        data: data
    });
	
	var infowindow = new naver.maps.InfoWindow({
		content: [
		          '<div class="bulk_charge_info">',
		          	'<div class="chargeSeqNo">일련번호 : ' + data.chargeSeqNo + '</div>',
		          	'<div class="chargeDate">충전일시 : ' + chargeDateTime + '</div>',
		          	'<div class="chargeVolume">충전량(Kg) : ' + data.chargeVolumeKg + '</div>',
		          	'<div class="customerName">거래처명 : ' + data.customerName + '</div>',
		          '</div>'
		          ].join('')
	});

	naver.maps.Event.addListener(marker, "click", function(e) {
	    if (infowindow.getMap()) {
	        infowindow.close();
	    } else {
	        infowindow.open(map, marker);
	    }
	});

	//infowindow.open(map, marker);

    return marker;
}

function addBulkChargeTimeMarker(latlng, data, visible) {
	var width = 178;
	var height = 48;
	
	//var chargeTime = getFormattedDateTime(data.chargeDate, data.chargeTime);
	var chargeTime = getFormattedTime(data.chargeTime);
	var chargeVolumeKg = addComma(roundValue(0, data.chargeVolumeKg));
	var customerName = data.customerName;
	
	var marker = new naver.maps.Marker({
        position: {lat: parseFloat(latlng.lat()), lng: parseFloat(latlng.lng())},
        map: map,
		icon: {
			content: [
			          '<div class="route_time_cell">',
			          	'<div class="route_time">' + chargeTime + ' <span class="chargeVolume">' + chargeVolumeKg + ' kg</span></div>',
			          	'<div class="customer_name">' + customerName + '</div>',
			          '</div>'
			          ].join(''),
			size: new naver.maps.Size(width, height),
			anchor: new naver.maps.Point(0, 0)
              
		},
        zIndex: bulkMarkerZindex++,
        visible: visible,
        data: data
    });

    return marker;
}

function getFormattedDateTime(date, time) {
	var year = date.substr(0, 4);
	var month = date.substr(4, 2);
	var day = date.substr(6,2);
	
	var hour = time.substr(0, 2);
	var minute = time.substr(2, 2);
	var second = time.substr(4,2);
	
	return [year, month, day].join('-') + " " + [hour, minute, second].join(':'); 
}

function getFormattedShortDateTime(date, time) {
	var year = date.substr(0, 4);
	var month = date.substr(4, 2);
	var day = date.substr(6,2);
	
	var hour = time.substr(0, 2);
	var minute = time.substr(2, 2);
	var second = time.substr(4,2);
	
	return [month, day].join('-') + " " + [hour, minute].join(':'); 
}

function getFormattedTime(time) {	
	var hour = time.substr(0, 2);
	var minute = time.substr(2, 2);
	var second = time.substr(4,2);
	
	return [hour, minute, second].join(':'); 
}

function addBulkRouteTimeMarker(latlng, data, visible) {
	var width = 124;
	var height = 24;
	
	var routeTime = getFormattedDateTime(data.receiveDate, data.receiveTime);
	
	var marker = new naver.maps.Marker({
        position: {lat: parseFloat(latlng.lat()), lng: parseFloat(latlng.lng())},
        map: map,
		icon: {
			content: [
			          '<div class="route_time_cell">',
			          	'<div class="route_time">' + routeTime + '</div>',
			          '</div>'
			          ].join(''),
			size: new naver.maps.Size(width, height),
			anchor: new naver.maps.Point(0, 0)
              
		},
        zIndex: bulkMarkerZindex++,
        visible: visible,
        data: data
    });

    return marker;
}

// 사업장 위치정보 구하기
function initClientLatLng() {
	if (clientJSON.myCompanys && clientJSON.myCompanys.length > 0) {
		var clientCompany = clientJSON.myCompanys[0];
		var clientLatitude =  Number(clientCompany.latitude);
		var clientLongitude =  Number(clientCompany.longitude);
		
		if (clientLatitude > 0 && clientLongitude > 0) {
			clientLatLng = new naver.maps.LatLng(clientLatitude, clientLongitude);
			bounds = new naver.maps.PointBounds(clientLatLng.toPoint(), clientLatLng.toPoint());
		}
	}
}

// 사원/지역분류 권한 설정
function grantBootstrapSelect() {
	var grant = $("#grant").val();
	
	//$("#sw_list").attr('disabled', true);
	//$("#area_list").attr('disabled', true);
	
	if (grant && grant.length > 0) {
		if (Number(grant.charAt(0)) == 0) $("#sw_list").attr('disabled', true);
		//if (grant.charAt(1) && Number(grant.charAt(1)) == 0) $("#area_list").attr('disabled', true);
	} 
}

/**
 * 단말기 있는 거래처 출력
 */
function drawPOIs(latlng) {

    // 마커 초기화
    initMarkers();
    
    initCustomList();
    
    var sawonCode =  $("#sawon_code").val();
	var areaTypeCode =  $("#area_type_code").val();
	
	return;
	
    $.ajax({
        url     :"./bulk_list_ajax.jsp",
        type    :"post",
        data    :{
        	qid : $("#qid").val(),
			sawonCode : sawonCode,
			areaTypeCode : areaTypeCode,
			keyword : $("#keyword").val()
        },
        success:function(json) {
        	
        	// 사원 초기화 및 설정
        	var sawonCodes = json.employeeCodes;
        	$.each(sawonCodes, function(i, item) {
        		$('#sw_list').append($('<option>', {
        			value: item.employeeCode,
        			text: item.employeeName
        		})); 
        	});
        	if (sawonCode) $('#sw_list').val(sawonCode);
        	
        	// 지역 초기화 및 설정
        	var areaTypeCodes = json.areaTypeCodes;
        	$.each(areaTypeCodes, function(i, item) {
        		$('#area_list').append($('<option>', {
        			value: item.areaTypeCode,
        			text: item.areaTypeName
        		}));
        	});
        	if (areaTypeCode) $('#area_list').val(areaTypeCode);
        	
        	var nBatteryStateArray = [0, 0, 0, 0];
        	
        	// 잔량레벨별로 거래처 분리
        	for (var i in json.tankLevelLists) {
        		var oTankLevel = json.tankLevelLists[i];
        		var nLevelState = Number(oTankLevel.levelState);
        		
        		oCustomerArray[nLevelState].push(oTankLevel);
        		
        		// 발신기 상태별 거래처수 구하기
        		var nLatitude = Number(oTankLevel.latitude); 
            	var nLongitude = Number(oTankLevel.longitude);
            	if (nLatitude == 0 || nLongitude == 0) nBatteryStateArray[0]++;
            	
            	if (Boolean(oTankLevel.receiveDateOver)) nBatteryStateArray[1]++;
            	if (Boolean(oTankLevel.uniformLevel)) nBatteryStateArray[2]++;
            	if (Boolean(oTankLevel.lowBattery)) nBatteryStateArray[3]++;
        	}
            
            // 잔량레벨별  거래처수 설정
            var levelResult = [oCustomerArray[0].length + oCustomerArray[1].length + oCustomerArray[2].length + oCustomerArray[3].length,
                               oCustomerArray[0].length, oCustomerArray[1].length, oCustomerArray[2].length, oCustomerArray[3].length];
            $('.level_select_btn').each(function (index) {
                $(this).html(levelArray[index] + '<br/>' + levelResult[index]);
            });
            
            // 발신기 상태별 거래처수 설정
            $('.state_select_btn').each(function (index) {
                $(this).html(stateArray[index] + '<br/>' + nBatteryStateArray[index]);
            });

            // 지도에 거래처 표시
            for (var i = oCustomerArray.length - 1; i > -1 ; i--) {
            	var oTankLevels = oCustomerArray[i];
            	
            	var markerIndex = 1;
            	$.each(oCustomerArray, function(idx, oCustom) {
            		if (idx < i) {
            			markerIndex += oCustom.length;
            		}
            	}); 
            		
            	for(var j in oTankLevels) {
            		var oTankLevel = oTankLevels[j];
            		
            		var nLatitude = Number(oTankLevel.latitude); 
                	var nLongitude = Number(oTankLevel.longitude);
                	
                	if (nLatitude > 0 && nLongitude > 0) {

                    	// 마커 생성
                        markerArray[markerIndex] = addMarker(
                        		'terminal',
                        		pinDots[i+1], 
                        		new naver.maps.LatLng(nLatitude, nLongitude), 
                        		8, 8,
                        		markerIndex, 
                        		oTankLevel);
                        markerArray[markerIndex].setVisible(true);	// 마커 표시
                        
                        // 인포윈도우 생성 (마커가 표시돨때 표시, 마커가 비표시될때 비표시)
                        infoWindowsArray[markerIndex] = addInfoWindow(markerIndex, oTankLevel, 'terminal');

                        // 지도 표시 영역 설정
                        if (bounds) {
                        	bounds.extend(markerArray[markerIndex].getPosition());
                        } else {
                    		bounds = new naver.maps.PointBounds(markerArray[markerIndex].getPosition(), markerArray[markerIndex].getPosition());
                        }
                        
                     	// 반경을 그리기 위한 거리 계산 (네이버에서는 관련 API가 제공되지 않음)
                        if (clientLatLng) {
    	                    var distance = computeDistanceBetween(clientLatLng, markerArray[markerIndex].position);
    	                    if(maxDistance < distance) {
    	                        maxDistance = distance;
    	                    }
                        }
                    }
                	
                	markerIndex++;
            	}
            }

            // 거래처 목록에 거래처 추가
            var html_receive = addOrderList(oCustomerArray);
            $("#account-list").html(html_receive);
            
            $('#sw_list').selectpicker('refresh');
            $('#bulk_list').selectpicker('refresh');
                        
            setLevelSelect(["1", "2", "3"]);
            filterCustomer({level:["1", "2", "3"]});

            // 지도 이동
            if (bounds) {
            	map.fitBounds(bounds);
            	map.panToBounds(bounds);
            }
            
            // 표시할 거래처가 없을 경우 기본 위치 표시
            if(markerArray != undefined && markerArray.length <= 0) {
                if (!clientLatLng) {
                	map.setZoom(mapZoom);
                }
            }

            if (clientLatLng) {
                // 사업장 위치 표시
            	addMarker('etc', pinEtcs[1], clientLatLng, 22, 35).setVisible(true);
            	
            	// 반경 표시
	            setAllRadius(clientLatLng, maxDistance);
            }
        }
    });
}

/**
 * 하나의 마커 출력
 */
function addMarker(kind, icon, latlng, width, height, index, data) {

	var pinIcon = null;
    switch(kind) {
    	case 'terminal' :
		    pinIcon = {
		        /*url: icon,
		        scaledSize: new naver.maps.Size(width, height),
		        anchor: new naver.maps.Point(width/2, height/2)*/ 
    			content: '<i class="fa fa-circle lv' + (parseInt(data.levelState)+1) + '" aria-hidden="true" style="font-size:'+width+'px;"></i>',
		        size: new naver.maps.Size(width, height),
		        anchor: new naver.maps.Point(4, 11)		// 폰트크기에 따라 hotspot 포인트 조정 필요
		    };
		    break;
    	case 'etc' :
		    pinIcon = {
		        url: icon,
		        scaledSize: new naver.maps.Size(width, height)
		    };
		    break;
    }
    
    /*
    var zIndex = 0;
    if (data) {
    	zIndex = markerZindex[parseInt(data.levelState)];
    }
    */
    
    var marker = new naver.maps.Marker({
        position: {lat: parseFloat(latlng.lat()), lng: parseFloat(latlng.lng())},
        map: map,
        icon: pinIcon,
        visible: false,
        zIndex: markerZindex++,
        data: data
    });

    // 이벤트 등록
    switch(kind) {
        case 'terminal' :
            marker.addListener('click', function () {
                //marker.setZIndex(markerZindex++);
                popupDetail(index, data.customerCode);
            });
            break;

        case 'noTerminal' :
            marker.addListener('click', function () {
                //marker.setZIndex(markerZindex++);
                popupDetail(data['acc_idx'], data['terminal_idx'], data['user_idx']);
            });
            marker.addListener('mouseover', function () {
                //marker.setZIndex(markerZindex++);
            });
            break;

        case 'car' :
            marker.addListener('click', function () {
                //marker.setZIndex(markerZindex++);
                popupDetail(data['acc_idx'], data['terminal_idx'], data['user_idx'], 'car');
            });
            marker.addListener('mouseover', function () {
                //marker.setZIndex(markerZindex++);
            });
            break;
    }

    return marker;
}


/**
 *
 * @param data
 * @returns {InfoBox}
 */
function addInfoWindow(idx, data, kind) {

	var loc = [];
    switch(kind) {
        case 'car'          :
            loc = [-61,-50];
            break;

        case 'noTerminal'   :
        case 'terminal'     :
        default             :
            loc = [-61,-32];
    }

    var myInfoWindow = new MyInfoWindow({
        map: map,
        position: new naver.maps.LatLng(data.latitude, data.longitude),
        data: data,
        pixelOffset: loc,
        kind: kind,
        visible: true,
        index: idx,
    });

    return myInfoWindow;
}

/**
 * 좌측 리스트 추가하기
 * @param json
 * @param kind
 * @returns {string}
 */
function addOrderList(customerDatas) {

	var accountCount = 0;
	
    var str = "<ul class='account_lists_cell'>";
    for(var j in customerDatas) { 
    	for(var k in customerDatas[j]) {
	    	accountCount++;
	    	
	    	var item = customerDatas[j][k];
	    	
	    	var nLatitude = Number(item.latitude); 
	    	var nLongitude = Number(item.longitude);
	        	
	    	var batteryState = [];	// 발신기 상태
	    	if(nLatitude == 0 || nLongitude == 0) batteryState.push('state1');
	    	if(Boolean(item.receiveDateOver)) batteryState.push('state2');
	    	if(Boolean(item.uniformLevel)) batteryState.push('state3');
	    	if(Boolean(item.lowBattery)) batteryState.push('state4');
	    	
	    	var lastReceiveDateTime = parseDateTime(item.lastReceiveDate, item.lastReceiveTime);
	    	var remark = item.remark;
	
	    	/*
	    	str +=  '<li class="account_info_cell clearfix'+((nLatitude > 0 && nLongitude > 0) ? " clickable" : "")+'" index="'+accountCount+'">';
	    	str +=  '   <div class="cell_title">';
	    	str +=  '		<div class="cell_mark lv'+(parseInt(j)+1)+' '+batteryState.join(' ')+'"><span>' + item.lastLevel + '</span></div>';
	    	str +=  '       <div class="account_name" key='+item.customerCode+'><a href="javascript:popupDetail(\'' + k + '\', \''+item.customerCode+'\');"><i class="fa fa-sticky-note-o" aria-hidden="true"></i></a>'+item.customerName+'</div>';
	    	str +=  '       <div class="indicator ' + getBatteryLevel(item.batteryPercent) + '"><div class="top" ></div><div class="bottom">'+(item.batteryPercent > 0 ? item.batteryPercent + "%" : "")+'</div></div>';
	    	str +=  '	</div>';
	    	str +=  '</li>';
	    	 */
	    	
	    	str +=  '<li class="account_info_cell clearfix'+((nLatitude > 0 && nLongitude > 0) ? " clickable" : "")+'" index="'+accountCount+'">';
	    	str +=  '   <div class="cell_title">';
	    	str +=  '		<div class="cell_mark lv'+(parseInt(j)+1)+'"><span>' + item.lastLevel + '</span></div>';
	    	str +=  '       <div class="account_name ' + batteryState.join(' ') + '" key='+item.customerCode+'><div class="top"><a href="javascript:popupDetail(\'' + accountCount + '\', \''+item.customerCode+'\');"><i class="fa fa-sticky-note-o" aria-hidden="true"></i></a>'+item.customerName+'</div><div class="bottom"><span class="receiveDateTime">' + lastReceiveDateTime + '</span>' + (remark ? ' (' + remark + ')' : '') + '</div></div>';
	    	str +=  '       <div class="indicator ' + getBatteryLevel(item.batteryPercent) + '"><div class="top"></div><div class="bottom">'+(item.batteryPercent > 0 ? item.batteryPercent + "%" : "")+'</div></div>';
	    	str +=  '	</div>';
	    	str +=  '</li>';
    	}
    }
    if(accountCount == 0) {
        str += "\
            <li><center><h5 style=' line-height:100px;'>결과가 없습니다.</h5></center></li>\
        ";
    }
    str += "<ul>";
    
    $('#account_counts').html(accountCount);

    return str;
}

// 검침상세보기
function popupDetail(idx, acc_idx) {

    $.smartPop.open({
        width	        : 650,
        height	        : 470,
        border          : 0,
        padding         : 0,
        padding_top     : 0,
        padding_bottom  : 0,
        padding_left    : 0,
        padding_right   : 0,
        closeMargin     : 18,
        closeImg        : {width:18, height:18, src:'./assets/img/common/btn-popup-close.png'},
        url : "popupDetailView.html",
        callback	    : {
            close : function(){

            }
        },
        data : getCustomData(idx)
    });
}

function getCustomData(idx) {
	var position = 0;
	var index = idx - 1;
	for(var i in oCustomerArray) {
		position += oCustomerArray[i].length;
		
		if (idx <= position) {
			return oCustomerArray[i][index];
		} else {
			index -= oCustomerArray[i].length;
		}
	}
	
	return null;
}

/**
 * 반경 표시
 * @param latlng
 * @param radius
 * @returns {*}
 */
function setRadius(latlng, radius) {
    var circle = new naver.maps.Circle({
        map: map,
        center: latlng,
        radius : radius
    });

    radiusArray.push(circle);

    return circle;
}

/**
 *
 * @param obj
 * @param kind
 * @param i
 */
function markerSelected(obj, kind, i) {
	if (markerArray.length > 0) {
	    map.setZoom(mapZoom);
	    map.panTo(markerArray[i].getPosition());

	    //init bounce marker
	    /*
	    for (k in markerArray ) {
	        markerArray[k].setAnimation(null);
	    }
	    */
	
	    //bounce marker
	    //markerArray[i].setZIndex(markerZindex++);
	    markerArray[i].setAnimation(naver.maps.Animation.DROP);
	
	    /*
	    //infoWindowsArray[i].close();
	    infoWindowsArray[i].setZIndex(markerZindex++);
	    infoWindowsArray[i].open(map, markerArray[i]);
	    */
	}
}

/**
 * 거리반경 표시
 * @param center
 * @param radius
 */
function setAllRadius(center, radius) {
	var radiusRange = radius > maxRadius ? maxRadius : radius; 
	
    closeAllRadius();
    for(var r=0 ; r < radiusRange + 10000 ; r=r+10000) {
        setRadius(center, r);
    }
}

/**
 * 거리반경 클리어
 */
function closeAllRadius() {
    for (var i = 0; i < radiusArray.length; i++) {
        radiusArray[i].setMap(null);
    }
}

/**
 * 마커 및 인포윈도우 클리어
 */
function initMarkers() {
    for (var i in markerArray) {
        markerArray[i].setMap(null);
    }

    for (var i in infoWindowsArray) {
        infoWindowsArray[i].setMap(null);
    }
    
    oCustomerArray = [[], [], [], []];
}

function initCustomList() {
	//$('#sw_list').find('option:not(:first)').remove();
    //$('#area_list').find('option:not(:first)').remove();
    
    $('#sw_list').selectpicker('refresh');
    $('#bulk_list').selectpicker('refresh');
    
    $('.datepicker').datepicker({
        format: 'yyyy/mm/dd',
        setDate: new Date(),
        language: 'kr'
    });
}

/**
 * 현재 선택된 레벨정보 가져오기(복수)
 */
function getLevelGroup() {
    var level = [];
    $('.level_select_btn').each(function(index){
        if($(this).hasClass("bold")){
        	var value = $(this).attr('label');
        	if (value != 0) {
        		level.push(value);
        	}
        }
    });
    return level;
}

/**
 * 현재 선택된 상태정보 가져오기(단일)
 */
function getState() {
    var state = -1;
    $('.state_select_btn').each(function(index){
        if($(this).hasClass("bold")){
            state = $(this).attr('label');
            return;
        }
    });
    return state;
}

function computeDistanceBetween(latlng1, latlng2) {
    var pointLat1 = latToXY(latlng1);
    var pointLat2 = latToXY(latlng2);

    var pointLng1 = lngToXY(latlng1);
    var pointLng2 = lngToXY(latlng2);

    var distanceLat = Math.sqrt(Math.pow(pointLat1.x - pointLat2.x, 2) + Math.pow(pointLat1.y - pointLat2.y, 2));
    var distanceLng = Math.sqrt(Math.pow(pointLng1.x - pointLng2.x, 2) + Math.pow(pointLng1.y - pointLng2.y, 2));

    var distance = Math.sqrt(Math.pow(distanceLat, 2) + Math.pow(distanceLng, 2));

    return distance;
}

function latToXY(latlng) {
    var earthEquatorRadius = 6370000;
    var earthPolarRadius = earthEquatorRadius * (1 - 0.017);

    var x = earthEquatorRadius * Math.cos(degreeToRadian(latlng.lat()));
    var y = earthPolarRadius * Math.sin(degreeToRadian(latlng.lat()));

    return new naver.maps.Point(x, y);
}

function lngToXY(latlng) {
    var earthEquatorRadius = 6370000;
    var earthRadiusLat = earthEquatorRadius * Math.cos(degreeToRadian(latlng.lat()));

    var x = earthRadiusLat * Math.cos(degreeToRadian(latlng.lng()));
    var y = earthRadiusLat * Math.sin(degreeToRadian(latlng.lng()));

    return new naver.maps.Point(x, y);
}

function degreeToRadian(degree) {
    return degree * Math.PI / 180.0;
}

function parseDateTime(date, time) {
	return date.substring(4,6) + "-" + date.substring(6,8) + " " + time.substring(0,2) + ":" + time.substring(2,4); 
}

function parseFullDateTime(date, time) {
	return date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8) + " " + time.substring(0,2) + ":" + time.substring(2,4) + ":" + time.substring(4,6); 
}

function parseFullDate(date) {
	return date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8); 
}

function getToday() {
	return new Date();
}

function addDate(date, day) {
	var newDate = new Date(date);
	newDate.setDate(newDate.getDate() + day);
	return newDate.getFullYear() + "-" + zeroFill(newDate.getMonth() + 1) + "-" + zeroFill(newDate.getDate()); 
}

function zeroFill(value) {
	var result = "" + value;
	if (result.length == 1) result = "0" + result;  
	return result;
}

function addComma(num) {
	var regexp = /\B(?=(\d{3})+(?!\d))/g;
	return num.toString().replace(regexp, ',');
}

function roundValue(roundType, value) {
	if (roundType == 0) {
		// 소수1자리 반올림
		return Math.round(value);
	} else if (roundType == 1) {
		// 원단위 반올림
		return (Math.round(value / 10) * 10);
	} else if (roundType == 2) {
		// 원단위 절삭
		return Math.floor(value);
	}
	
	return value;
}

var ComponentsBootstrapSelect = function() {
    var handleBootstrapSelect = function() {
        $('.bs-select').selectpicker({
            iconBase: 'fa',
            tickIcon: 'fa-check'
        });
        
        $('#sw_list').change(function(){
        	var sawonCode = $('#sw_list option:selected').val();
        	$("#sawon_code").val(sawonCode);
        	
            //drawPOIs(map.getCenter());
        });
        
        $('#bulk_list').change(function(){
        	var bulkCode = $('#bulk_list option:selected').val();
        	$("#bulk_code").val(bulkCode);
        	
        	//drawPOIs(map.getCenter());
        });
    };

    return {
        //main function to initiate the module
        init: function () {
            handleBootstrapSelect();
        }
    };
}();

var MyInfoWindow = function(options) {
    var data = options.data;
    //var kind = options.kind;
    var idx = options.index; 
    var stateClass = Boolean(data.receiveDateOver) ? ' state2' : '';

    var html_text = '<div class="levelInfo" style="position:absolute;">';
	html_text +=	'   <span class="mark lv' + (parseInt(data.levelState)+1) + '">' + data.lastLevel + '</span>';
    html_text += 	'    <a href="javascript:popupDetail(\''+ idx + '\', \'' + data.customerCode+'\');"><span class="cname' + stateClass + '">'+data.customerName+'</span></a>';
    html_text += 	'</div>';

    this._element = $(html_text);
    this._pixelOffset = options.pixelOffset;

    this.setPosition(options.position);
    this.setMap(options.map || null);
};

MyInfoWindow.prototype = new naver.maps.OverlayView();
MyInfoWindow.prototype.constructor = MyInfoWindow;

MyInfoWindow.prototype.setPosition = function(position) {
    this._position = position;
    this.draw();
};

MyInfoWindow.prototype.getPosition = function() {
    return this._position;
};

MyInfoWindow.prototype.onAdd = function() {
    var overlayLayer = this.getPanes().overlayLayer;
    //var overlayLayer = this.getPanes().overlayMouseTarget;

    this._element.appendTo(overlayLayer);
};

MyInfoWindow.prototype.draw = function() {
    if (!this.getMap()) {
        return;
    }

    var projection = this.getProjection(),
        position = this.getPosition(),
        pixelPosition = projection.fromCoordToOffset(position),
        pixelOffset = this._pixelOffset;

    this._element.css('left', pixelPosition.x + pixelOffset[0]);
    this._element.css('top', pixelPosition.y + pixelOffset[1]);
};

MyInfoWindow.prototype.onRemove = function() {
    var overlayLayer = this.getPanes().overlayLayer;

    this._element.remove();
    this._element.off();
};
