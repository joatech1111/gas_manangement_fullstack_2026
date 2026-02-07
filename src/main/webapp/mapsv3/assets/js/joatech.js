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

$(document).ready(function(){	
	// 사업장 위치 구하기
	initClientLatLng();

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
    
    addTrafficLayer();
    
    // 콤보박스 초기화
    ComponentsBootstrapSelect.init();
    
    // 사원/지역분류 권한 설정
    grantBootstrapSelect();

    // 거래처 표시
    drawPOIs(clientLatLng);

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
    
    // 검색입력창에서 엔터 이벤트 처리
	$('#search_keyword').on('keypress', function(e) {
		if (e.which == 13) {
			e.preventDefault();
	    	
	    	$("#keyword").val($("#search_keyword").val());
	    	drawPOIs(clientLatLng);
		}
	});
	
	// 엑셀 출력
	$('#excel_download_btn').on('click', function() {

        //엑셀다운로드 진행
        if(confirm('엑셀 다운로드 하시겠습니까?')) {
            $("#fCreate").attr('action','./excel_download.jsp');

            //현재좌표 계산
            $("#current_latitude").val(clientLatLng.lat());
            $("#current_longitude").val(clientLatLng.lng());

            //var level = getLevelGroup();
            //var indicator_level_str = level.join("|");
            //$("#indicator_level").val(indicator_level_str);

            $("#fCreate").attr('target','hiddenFrame');
            $("#fCreate").submit();
        }
    });
 
});

function addTrafficLayer() {
	var trafficLayer = new naver.maps.TrafficLayer({
	    //interval: 2000 // 2초마다 새로고침
	});

	$('#traffic_select_btn').on("click", function(e) {
	    e.preventDefault();

	    if (trafficLayer.getMap()) {
	        trafficLayer.setMap(null);
	        $(this).removeClass("selected");
	    } else {
	        trafficLayer.setMap(map);
	        $(this).addClass("selected");
	    }
	});
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
		if (grant.charAt(1) && Number(grant.charAt(1)) == 0) $("#area_list").attr('disabled', true);
	} 
}

// 잔량레벨별 선택 초기화
function initLevelSelect() {
	$('.level_select_btn').each(function(index) {
        $(this).removeClass("bold");
    });
    
    $("#indicator_level").val("");
}

function setLevelSelect(option) {
	if (option) {
		$.each(option, function(i, level) {
			$('.level_select_btn.lv' + level).addClass('bold');
		});	
	}
    
    $("#indicator_level").val(option.join("|"));
}

// 발신기 상태별 선택 초기화
function initStateSelect() {
    $('.state_select_btn').each(function(index) {
        $(this).removeClass("bold");
    });
    
    $("#indicator_state").val("");
}

// 거래처 필터링 처리
function filterCustomer(option) {
	if (option) {		
		$('.account_info_cell').addClass('hidden');
		
		// 잔량레벨별 필터링
		if (option.level) {
			if (option.level.length == 0) {
				$('.account_info_cell').removeClass('hidden');
			} else {
				$.each(option.level, function(i, level) {
					$('.account_info_cell').has('.lv' + level).removeClass('hidden');
				});	
			}
		}

		// 발신기 상태별 필터링
		if (option.state) {
			$('.account_info_cell').has('.state' + option.state).removeClass('hidden');
		}

		// 거래처 갯수 표시
		$('#account_counts').html($('.account_info_cell').not('.hidden').length);

		// 거래처 목록과 지도의 마커 동기화
		$($('.account_info_cell.clickable').get().reverse()).each(function(i) {
			var idx = parseInt($(this).attr('index'));
			var bHidden = $(this).hasClass('hidden');

			markerArray[idx].setVisible(!bHidden);
			infoWindowsArray[idx].setMap(bHidden ? null : map);
		});
	}
}

function getBatteryLevel(percent) {
	var per = Number(percent);
	
	if (per == 0) {
		return "bat_none";
	} else if (per <= 15) {
		return "bat1";
	} else if (per <= 50) {
		return "bat2";
	} else if (per <= 80) {
		return "bat3";
	} else {
		return "bat4";
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

    $.ajax({
        url     :"./tank_level_list_ajax.jsp",
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
            $('#area_list').selectpicker('refresh');
                        
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
	$('#sw_list').find('option:not(:first)').remove();
    $('#area_list').find('option:not(:first)').remove();
    
    $('#sw_list').selectpicker('refresh');
    $('#area_list').selectpicker('refresh');
    
    // 잔량레벨별  거래처수 설정
    var levelResult = [0, 0, 0, 0, 0];
    $('.level_select_btn').each(function (index) {
        $(this).html(levelArray[index] + '<br/>' + levelResult[index]);
    });
    
    // 발신기 상태별 거래처수 설정
    var nBatteryStateArray = [0, 0, 0, 0];
    $('.state_select_btn').each(function (index) {
        $(this).html(stateArray[index] + '<br/>' + nBatteryStateArray[index]);
    });

    // 거래처 목록에 거래처 추가
    $("#account-list").html("");
    $('#account_counts').html("0");
    
    initLevelSelect();
    initStateSelect();
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

var ComponentsBootstrapSelect = function() {
    var handleBootstrapSelect = function() {
        $('.bs-select').selectpicker({
            iconBase: 'fa',
            tickIcon: 'fa-check'
        });
        
        $('#sw_list').change(function(){
        	var sawonCode = $('#sw_list option:selected').val();
        	$("#sawon_code").val(sawonCode);
        	
            drawPOIs(map.getCenter());
        });
        
        $('#area_list').change(function(){
        	var areaTypeCode = $('#area_list option:selected').val();
        	$("#area_type_code").val(areaTypeCode);
        	
        	drawPOIs(map.getCenter());
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
