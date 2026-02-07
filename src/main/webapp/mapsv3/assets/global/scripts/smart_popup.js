(function($) {
    "use strict";

    /*
	 *  * 제한사항
	 * 가로 최대 1300, 세로 5000 - 더 크게할 경우 css 수정
	 *
	 * 사용법
	 * 1. html 내용 보여주기
	 *      $.smartPop.open({title: '스마트팝', width: 500, height: 500, html: '<h1>smartPop</h1> 여기에 보여줄 내용' });
	 * 2. url 페이지 띄우기
	 *      $.smartPop.open({title: '스마트팝', width: 500, height: 500, url: 'smartPop.html 여기에 보여줄 페이지' });
	 *      세로 크기는 불러오는 페이지 크기에 맞게 자동으로 저절됨
	 * 3. 높이값 확인 로그
	 *      $.smartPop.open({title: '스마트팝', width: 500, height: 500, log: true, url: 'smartPop.html 여기에 보여줄 페이지' });
	 *      log: true 설정
	 *
	 * 기본 옵션

	 */
    var ie     = $.browser.msie && ($.browser.version < 9);
    var innerH  = window.innerHeight;

    $.smartPop = {
        isInstall : false,
        opts : {},
        defaults : {
            position    : 'center',
            left        : 310,
            top         : 50,
            bodyClose   : true,
            padding     : 10,
            padding_top : 10,
            padding_bottom : 10,
            padding_left : 10,
            padding_right : 10,
            background  : '#000',
            border      : 5,
            borderColor : '#FFFFFF',
            closeMargin : 3,
            closeImg    : {width:13, height:13, src:'/assets/img/common/btn_close1.png'},
            opacity     : .75,
            width       : 720,
            height      : 500,
            html        : '',
            url         : '',
            log         : false,
            callback	:	{
            	close	:	null
            }
        },
        open : function(options) {
            this.opts = $.extend({}, $.smartPop.defaults, options);
            this.install(this.opts);
            this.resize();

            $('html.smart').css({ marginRight: '15px', display: 'block', overflow: 'hidden', overflowY: 'hidden' });
            $('#smartPop').show();
            if(this.opts.log) $('#smartPop_log').show();
        },

        resize : function() {
            this.log(this.opts.width + ' x ' + this.opts.height);
            this.log('background : ' + this.opts.background);
            this.log('padding : ' + this.opts.padding);
            this.log('border : ' + this.opts.border);
            this.log('borderColor : ' + this.opts.borderColor);
            this.log('closeMargin : ' + this.opts.closeMargin);
            this.log('opacity : ' + this.opts.opacity);
            this.log('');

            // 기본 설정
            $('#smartPop_container').css({ border: 'solid ' + this.opts.border + 'px ' + this.opts.borderColor });
            $('#smartPop_close').css({ top: this.opts.closeMargin + 'px', right: this.opts.closeMargin + 'px' });
            $('#smartPop_content').css({ padding: this.opts.padding + 'px' });
            $('#smartPop_container').width(this.opts.width);
            $('#smartPop_close_wrap').width(this.opts.width);
            this.resizeHeight(this.opts.height);
        },
        resizeHeight : function(h) {
            this.log('resizeHeight : ' + h);
            if(ie) {
                $('body').attr({ scroll: 'no' }); // ie7에서 overflow 적용안됨
                innerH = document.documentElement.clientHeight;
            }

            // 위치설정
            if(this.opts.position == 'center') {
                var t;
                t = (h < innerH) ? (innerH - h) / 20 : 75;
                $('#smartPop_container').css({ marginLeft: 'auto', marginTop: t + 'px' });
            } else {
                $('#smartPop_container').css({ marginLeft: this.opts.left + 'px', marginTop: this.opts.top + 'px' });
            }

            // 높이설정
            $('#smartPop_container').height(h);
            if(this.opts.url == '') {
                $('#smartPop_content').html(this.opts.html).height(h).show();
                $('#smartPop_frame').hide();
            } else {

                $('#smartPop_content').hide();//- this.opts.padding * 2
                $('#smartPop_frame').css({ padding: this.opts.padding + 'px', width: (this.opts.width ) + 'px', height: (h - this.opts.padding * 2) + 'px' }).show();
            }
            $('#smartPop_loading').hide();
            this.log('');
        },
        install : function(option) {
            if(this.isInstall == false) {
                var body                    = $('body');
                var smartPop_overlay        = $('<div />').attr('id', 'smartPop_overlay').css({ opacity: this.opts.opacity, background: this.opts.background });
                var smartPop                = $('<div />').attr('id', 'smartPop');
                var smartPop_container		= $('<div />').attr('id', 'smartPop_container');
                var smartPop_loading		= $('<div />').attr('id', 'smartPop_loading');
                var smartPop_content		= $('<div />').attr('id', 'smartPop_content');
                var scrolling = (option.scrolling)?option.scrolling:'no';
                var smartPop_frame			= $('<iframe />').attr({ id: 'smartPop_frame', frameBorder: 0, scrolling: scrolling });
				//var smartPop_btn	 		= $('<div />').attr('id', 'smartPop_btn');

                if ( option.enableCloseBtn !== false ) {
                	var smartPop_close_wrap		= $('<div />').attr('id', 'smartPop_close_wrap');
                    var smartPop_close			= $('<div />').attr('id', 'smartPop_close');//.append('<button class="close action top-right" data-dialog-close=""><i class="pg-close"></i></button>');

                    smartPop_close_wrap.append(smartPop_close).appendTo(smartPop_container);
                }

                smartPop_container.append(smartPop_loading).append(smartPop_content).append(smartPop_frame).appendTo(smartPop);
                smartPop.append($('<div />').attr('id', 'smartPop_log'));
                body.append(smartPop_overlay).append(smartPop);
                this.isInstall = true;
            } else {
                $('#smartPop').show();
                $('#smartPop_overlay').show();
            }

            // 닫기 버튼 설정
            if(this.opts.closeImg != undefined) {
                $('#smartPop_close').css({ width:this.opts.closeImg.width + 'px', height:this.opts.closeImg.height + 'px', backgroundImage:'url(' + this.opts.closeImg.src + ')'});
            }
            if(this.opts.url != '') {
                $('#smartPop_frame').attr({ src : this.opts.url }).load(function () {
                    var h = $(this).contents().height();
                    $.smartPop.resizeHeight(h);
                    
                    if($.smartPop.opts.data) $.smartPop.setData($.smartPop.opts.data);
                });
            }

            if(this.opts.bodyClose) {
                $('body').bind('click', function(event) {
                    if (!event) event = window.event;
                    var target = (event.target) ? event.target : event.srcElement;
                    if(target.id == 'smartPop') {
                        $.smartPop.close();
                        event.stopPropagation();
                    }
                });
            }

            $('#smartPop_close').click(function() {
                $.smartPop.close();
            });
        },
        close : function() {
            if(ie) {
                $('body').attr({ scroll: 'yes' });
            }
            $('html.smart').css({ marginRight: 0, display: '', overflowY: 'scroll'});
            //$('html.smart').css({ marginRight: 0, display: ''});

            $('#smartPop_frame').attr('src', '').unbind();
            $('#smartPop').hide();
            $('#smartPop_overlay').hide();

            if ( $.type( this.opts.callback.close ) == 'function' ) {
            	this.opts.callback.close();
            }

        },
        log : function(msg) {
            var log = $('#smartPop_log').html();
            $('#smartPop_log').html(msg + '<br />' + log);
        },
        setData : function(data) {
        	var jqPopup = $('#smartPop_frame').contents(); 
        	
        	// 거래처명
        	jqPopup.find('#customer_name').text(data.customerName);
        	jqPopup.find('.account_name').text(data.customerName);
        	/*
        	// 상태 표시
        	var levelState = parseInt(data.levelState) + 1;
        	jqPopup.find('.cell_mark > span').text(levelArray[levelState]);
        	jqPopup.find('.cell_mark').addClass('lv'+levelState);
        	
        	jqPopup.find('.cell_mark').addClass('lv'+levelState);
        	
        	// 주소
        	if (data.address) jqPopup.find('.regdate > span').text(data.address);
        	
        	// 최종수신일
        	jqPopup.find('.regdate > .address').text("최근 수신 일자 : " + parseFullDateTime(data.lastReceiveDate, data.lastReceiveTime));
        	
        	// 탱크잔량
        	jqPopup.find('.gague_level').text(data.lastLevel + "%");
        	jqPopup.find('.gague_box').addClass('lv'+levelState);
        	jqPopup.find('.indicator_t').text("잔량: " + data.lastLevel + "%");
        	
        	if (data.batteryPercent && data.batteryPercent != 0) jqPopup.find('.battery_t').text("배터리량: " + data.batteryPercent + "%");
        	*/
        	jqPopup.find('.indicator_t').text("잔량: " + data.lastLevel + "%");
        	jqPopup.find('.battery_t').text("배터리량: " + (data.batteryPercent > 0 ? data.batteryPercent : "-") + "%");
        	
        	// 주간수신이력
        	var today = getToday();
        	var levelDays = [addDate(today, -9),addDate(today, -8),addDate(today, -7),addDate(today, -6),addDate(today, -5),addDate(today, -4),addDate(today, -3),addDate(today, -2),addDate(today, -1),addDate(today, 0)];
        	//jqPopup.find('#check_list_body > table > tbody:last').append("<tr><td>" + levelDays[9] + "</td><td><span class=\"bold\">" + data.level0 + "%</span></td></tr>");
        	//jqPopup.find('#check_list_body > table > tbody:last').append("<tr><td>" + levelDays[8] + "</td><td><span class=\"bold\">" + data.level1 + "%</span></td></tr>");
        	//jqPopup.find('#check_list_body > table > tbody:last').append("<tr><td>" + levelDays[7] + "</td><td><span class=\"bold\">" + data.level2 + "%</span></td></tr>");
        	//jqPopup.find('#check_list_body > table > tbody:last').append("<tr><td>" + levelDays[6] + "</td><td><span class=\"bold\">" + data.level3 + "%</span></td></tr>");
        	//jqPopup.find('#check_list_body > table > tbody:last').append("<tr><td>" + levelDays[5] + "</td><td><span class=\"bold\">" + data.level4 + "%</span></td></tr>");
        	//jqPopup.find('#check_list_body > table > tbody:last').append("<tr><td>" + levelDays[4] + "</td><td><span class=\"bold\">" + data.level5 + "%</span></td></tr>");
        	//jqPopup.find('#check_list_body > table > tbody:last').append("<tr><td>" + levelDays[3] + "</td><td><span class=\"bold\">" + data.level6 + "%</span></td></tr>");
        	//jqPopup.find('#check_list_body > table > tbody:last').append("<tr><td>" + levelDays[2] + "</td><td><span class=\"bold\">" + data.level7 + "%</span></td></tr>");
        	//jqPopup.find('#check_list_body > table > tbody:last').append("<tr><td>" + levelDays[1] + "</td><td><span class=\"bold\">" + data.level8 + "%</span></td></tr>");
        	//jqPopup.find('#check_list_body > table > tbody:last').append("<tr><td>" + levelDays[0] + "</td><td><span class=\"bold\">" + data.level9 + "%</span></td></tr>");
        	
        	// 거래처 정보
        	jqPopup.find('#location_into_table > table > tbody:last').append("<tr><td><span class=\"bold\">설치주소</span></td><td style=\"text-align: right;\">" + (data.zipCode ? data.zipCode + ") " : "") + (data.address ? data.address : "-") + "</td></tr>");
        	jqPopup.find('#location_into_table > table > tbody:last').append("<tr><td><span class=\"bold\">전화번호</span></td><td style=\"text-align: right;\">" + (data.customerTel ? data.customerTel : "-") + "</td></tr>");
        	jqPopup.find('#location_into_table > table > tbody:last').append("<tr><td><span class=\"bold\">핸드폰 번호</span></td><td style=\"text-align: right;\">" + (data.customerHp ? data.customerHp : "-") + "</td></tr>");
        	jqPopup.find('#location_into_table > table > tbody:last').append("<tr><td><span class=\"bold\">담당 사원</span></td><td style=\"text-align: right;\">" + (data.sawonName ? data.sawonName : "-") + "</td></tr>");
        	jqPopup.find('#location_into_table > table > tbody:last').append("<tr><td><span class=\"bold\">지역</span></td><td style=\"text-align: right;\">" + (data.areaTypeName ? data.areaTypeName : "-") + "</td></tr>");
        	jqPopup.find('#location_into_table > table > tbody:last').append("<tr><td><span class=\"bold\">비고</span></td><td style=\"text-align: right;\">" + (data.remark ? data.remark : "-") + "</td></tr>");
        	jqPopup.find('#location_into_table > table > tbody:last').append("<tr><td><span class=\"bold\">발신기명</span></td><td style=\"text-align: right;\">" + (data.transmitterSerialNumber ? data.transmitterSerialNumber : "-") + "</td></tr>");
        	
        	jqPopup.find('#location_into_table > table > tbody:last').append("<tr><td><span class=\"bold\">수신잔량</span></td><td><table class=\"receiveHistory\"><tr>" +
        			"<td>" + levelDays[0].substring(5,10) + "</td><td>" + levelDays[1].substring(5,10) + "</td><td>" + levelDays[2].substring(5,10) + "</td><td>" + levelDays[3].substring(5,10) + "</td><td>" + levelDays[4].substring(5,10) + "</td>" +
        			"<td>" + levelDays[5].substring(5,10) + "</td><td>" + levelDays[6].substring(5,10) + "</td><td>" + levelDays[7].substring(5,10) + "</td><td>" + levelDays[8].substring(5,10) + "</td><td>" + levelDays[9].substring(5,10) + "</td></tr>" +        			
        			"<tr><td>" + (data.level9 > 0 ? data.level9 : "") + "</td><td>" + (data.level8 > 0 ? data.level8 : "") + "</td><td>" + (data.level7 > 0 ? data.level7 : "") + "</td><td>" + (data.level6 > 0 ? data.level6 : "") + "</td><td>" + (data.level5 > 0 ? data.level5 : "") + "</td>" +
        			"<td>" + (data.level4 > 0 ? data.level4 : "") + "</td><td>" + (data.level3 > 0 ? data.level3 : "") + "</td><td>" + (data.level2 > 0 ? data.level2 : "") + "</td><td>" + (data.level1 > 0 ? data.level1 : "") + "</td><td>" + (data.level0 > 0 ? data.level0 : "") + "</td>" +
        					"</tr></table></td></tr>");
        }
    };

//    $.smartPop.defaults = {
//        position    : 'center',
//        left        : 310,
//        top         : 50,
//        bodyClose   : true,
//        padding     : 10,
//        background  : '#000',
//        border      : 5,
//        borderColor : '#2d9f61',
//        closeMargin : 3,
//        closeImg    : {width:13, height:13, src:'/assets/img/common/btn_close1.png'},
//        opacity     : .7,
//        width       : 720,
//        height      : 500,
//        html        : '',
//        url         : '',
//        log         : true,
//        callback	:	{
//        	close	:	null
//        }
//    };

})(window.jQuery);
