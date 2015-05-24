$(function () {
    //$.ajaxSetup({
    //    headers: {'X-XSRFToken': Request.getCookie('_xsrf')}
    //});
    var flag = 0;
    $(document).ajaxStart(function () {
        $.startLoading();
    }).ajaxStop(function () {

    }).ajaxError(function (e, jqxhr, settings, exception) {
        $.stopLoading();
        var ex = {};

        if (jqxhr.status == 0 && jqxhr.statusText == 'error') {
            ex['exception'] = "无法连接服务器";
        } else if (jqxhr.status == 0 && jqxhr.statusText == 'canceled') {
            ex['exception'] = "请求失败，被系统取消";
        } else if (jqxhr.status == 401) {
            if(flag === 0) {
                $("body").load('sys/login.html');
            }
            return;
        } else if (jqxhr.status == 400) {
            try {
                ex = JSON.parse(jqxhr.responseText);
            } catch (e) {
                ex['exception'] = "错误:400，未知系统异常！";
            }
        } else if (jqxhr.status == 403) {
            ex['exception'] = "您无权访问该内容！";
        } else if (jqxhr.status == 404) {
            ex['exception'] = "错误:404，未找到该页面！";
        }

        $.sticky('<span class="ui-icon ui-icon-alert" style="float:left; "></span>' + ex.exception);

    }).ajaxSuccess(function (event, xhr, settings) {
        $.stopLoading();
        if (settings.type === 'POST' || settings.type === 'PUT') {
            $.sticky("提交成功");
        } else if (settings.type === 'DELETE') {
            $.sticky("删除成功");
        }
    });

    var cur = Request.QueryAnchor();
    $.ajax({ url: 'rest/sec/menu',
        type: "GET",
        async: false,
        success: function (data) {
            App.Cache.button = data.user.buttons;
            App.Cache.User = data.user;
            var step = 0;
            var menu = "";
            var title = "";
            var current = 0;
            var shortcut = "";
            $.each(data.menu, function (i, item) {
                if (item.step > step) { // 下一级
                    menu += '<ul>';
                    step++;
                } else if (item.step === step) {// 同级下一节点
                    menu += '</li>';
                }

                while (step > item.step) { // 上一级
                    menu += '</li></ul></li>';
                    step -= 1;
                }

                if (item.url) { // 有链接
                    menu += '<li data-url="' + item.url + '">' + item.name;

                    if (cur === item.url) {
                        title = item.name;
                        current = item.parent;
                    }

                    if (item.shortcut) {
                        shortcut += '<li><a href="#' + item.url + '">' + item.name + '</a></li>';
                    }
                } else { // 无链接
                    menu += '<li class="parent-' + item.id + '">' + item.name + '<i></i><e></e>';
                }
            });

            while (0 < step--) {
                menu += "</li></ul>";
            }
            
            $('#header').html('<div><div id="navigate">' + menu
                + '</div><div id="userinfo">'
                + '<a href="#sys/message.html"><i class="white" style="background-position: -16px -113px;"></i><span id="messageNum"></span></a>'
                + '<i class="white" style="background-position:-144px -97px;"></i>'
                + '<span style="text-align:left;display: inline-block; max-width: 250px; height:13px;vertical-align: bottom; white-space: nowrap; word-break: keep-all; overflow: hidden; text-overflow: ellipsis; padding-left: 5px;">'
                + data['user'].userName + " - " + data['user'].realName + '</span>'
                + ' [<a href="rest/sec/logout?_=' + new Date().getTime() + '" style="color:#fff;">退出</a>]'
                + '<span class="contact" style="color:#fff;margin-left:7px"><i class="white"></i></span>'
                + '<a href="help/index.html" target="_blank" style="color:#fff;margin-left:7px">帮助</a></div></div>');

            $('.parent-' + current).addClass('active').find('e').html(title);

            $('#shortcut').html('<ul>' + shortcut + '</ul>').on('click', 'a', function () {
                var url = $(this).attr('href');
                if (url) {
                    $.history.load(url.replace('#', ''));
                    $('.active').removeClass('active');
                    $(this).parent().addClass('active');
                }
                return false;
            });

            $('#navigate').children().menu({
                position: { my: "left top", at: "left bottom"}
            }).on("mouseleave", function () {
                $(this).menu('collapseAll');
            }).on('click', '.ui-menu-item', function () {
                var url = $(this).attr('data-url');
                if (url) {
                    $.history.load(url);
                    $('.active').removeClass('active');
                    var act = $(this).parents('.ui-state-active');
                    if (act.length) {
                        act.find('e').html($(this).html());
                        act.addClass('active');
                    } else {
                        $(this).addClass('active');
                    }
                }
                return false;
            });
        }
    });

    $.history.init(function (url) {
        url = (url == "" ? "sys/main.html" : decodeURIComponent(url));
        $("#container").load(url, function () {
	        $.datepicker.regional['zh-CN'] = {
		        clearText: '清除',
		        clearStatus: '清除已选日期',
		        closeText: '关闭',
		        closeStatus: '不改变当前选择',
		        prevText: '<上月',
		        prevStatus: '显示上月',
		        prevBigText: '<<',
		        prevBigStatus: '显示上一年',
		        nextText: '下月>',
		        nextStatus: '显示下月',
		        nextBigText: '>>',
		        nextBigStatus: '显示下一年',
		        currentText: '今天',
		        currentStatus: '显示本月',
		        monthNames: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],
		        monthNamesShort: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],
		        monthStatus: '选择月份',
		        yearStatus: '选择年份',
		        weekHeader: '周',
		        weekStatus: '年内周次',
		        dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
		        dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
		        dayNamesMin: ['日','一','二','三','四','五','六'],
		        dayStatus: '设置 DD 为一周起始',
		        dateStatus: '选择 m月 d日, DD',
		        dateFormat: 'yy-mm-dd',
		        firstDay: 1,
		        initStatus: '请选择日期',
		        isRTL: false
	        };
	        $.datepicker.setDefaults($.datepicker.regional['zh-CN']);
        	
            $(this).find('.datepicker').datepicker({
                dateFormat: "yy-mm-dd",
                controlType: 'select',
                changeMonth: true,
                changeYear: true });

            $(this).find('.timepicker').timepicker({controlType: 'select'});
            $(this).find('.datetimepicker').datetimepicker({
                controlType: 'select',
                changeMonth: true,
                changeYear: true,
                dateFormat: "yy-mm-dd" });

            $(".mindatepicker").datepicker({
                dateFormat: "yy-mm-dd",
                defaultDate: "+1w",
                changeMonth: true,
                changeYear: true,
                numberOfMonths: 3,
                onClose: function (selectedDate) {
                    $(this).next('.maxdatepicker').datepicker("option", "minDate", selectedDate);
                },
                beforeShow: function () {
                    if (!$(this).next('.maxdatepicker').val())
                        $(this).datepicker("option", {"maxDate": null, 'minDate': null});
                }
            });

            $(".maxdatepicker").datepicker({
                dateFormat: "yy-mm-dd",
                defaultDate: "+1w",
                changeMonth: true,
                numberOfMonths: 3,
                onClose: function (selectedDate) {
                    $(this).prev('.mindatepicker').datepicker("option", "maxDate", selectedDate);
                },
                beforeShow: function () {
                    if (!$(this).prev('.mindatepicker').val())
                        $(this).datepicker("option", {"maxDate": null, 'minDate': null});
                }
            });
            $('#tabs').tabs();
            $('.buttonset').buttonset();
            $('button').button();
        });
        
        //每十分钟自动请求一次，临时解决超时问题
        setInterval(function(){
        	$.get("sys/main.html?_=" + new Date(), function(data,status){});
        }, 1000*60*10);//
        	
    });

});


//var socket = new SockJS('/rest/websocket/msg');
//var stompClient = Stomp.over(socket);
//stompClient.connect('', '', function (frame) {
//    stompClient.subscribe('/notify/'+ App.Cache.User.userName, function (message) {
//        $.sticky(JSON.parse(message.body).note);
//    });
//});