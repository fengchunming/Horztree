/**
 * Created by karas on 8/5/14.
 */
(function ($) {
    // Extend jQuery
    /**
    $.fn.loadActivityHis = function (id) {
        var $this = $(this);
        $.get('rest/act/processInstance/historicActivity/' + id, function (data) {
            var html = '<ul class="list">';
            $.each(data, function (i, e) {
                var flg = true;
                if (e.activityType === 'startEvent') {
                    html += '<li><h3>流程启动';
                } else if (e.activityType === 'endEvent') {
                    html += '<li><h3>流程结束';
                } else if (e.activityName) {
                    html += '<li><h3>' + e.activityName;
                } else {
                    flg = false;
                }

                if (flg) {
                    if (e.assignee)
                        html += '<span>承办:' + e.assignee + '</span>';
                    html += '<span>开始:' + new Date(e.startTime).format('yyyy-mm-dd') + '</span>'
                    if (e.endTime)
                        html += '<span>结束:' + new Date(e.endTime).format('yyyy-mm-dd') + '</span></h3></li>';
                }
            });
            html += '</ul>';
            $this.html(html);
        });
        $.get('rest/act/processInstance/historicDetail/' + id, function (data) {
            console.log(data);
        });

        return this;
    };
    **/


    $.fn.formMaker = function(data){
        if(data.formKey) {
            $(this).load(data.formKey, function(){
                $('.datepicker').datepicker({ dateFormat : "yy-mm-dd" });
                $('.datetimepicker').datetimepicker({ dateFormat : "yy-mm-dd" });
                convertLocal();
            });
        }else {
            var html = '<ol>';
            $.each(data.formProperties, function (i, item) {
                html += '<li><label>' + item.name + ':</label>';
                if (item.type == "boolean") {
                    html += '<input type="radio" name="' + item.id + '" value="_true" checked>是  <input type="radio" name="' + item.id + '" value="_false">否</li>';
                } else if(item.type === "enum") {
                    _.map(item.enumValues, function(e){
                       html += '<input type="radio" name="' + item.id + '" value="'+ e.id +'" checked>' + e.name + '  ';
                    });
                } else if(item.type === 'long') {
                    html += '<input type="number" name="' + item.id + '"></li>';
                }else if(item.type === 'date'){
                    html += '<input type="text" name="' + item.id + '" class="datepicker"></li>';
                } else{
                    html += '<input type="text" name="' + item.id + '"></li>';
                }
            });
            $(this).html(html + '</ol>').find('.datepicker').datepicker({ dateFormat : "yy-mm-dd" });;
        }
        return this;
    };

    $.fn.selectUser = function (callback) {
        var $lookup = $('<div><input type="text"/></div>');
        $lookup.find('input').autocomplete({
            appendTo: $lookup,
            minLength: 2,
            source: function (request, response) {
                var term = request.term;
                if (!App.Cache.userList) App.Cache.userList = {};
                if (term in App.Cache.userList) {
                    response($.map(App.Cache.userList[ term ], function(item) {
                        return {
                            label : item.staffCode + " - " + item.realName,
                            value : item.staffCode + " - " + item.realName,
                            object : item
                        };
                    }));
                    return;
                }


                $.getJSON("rest/sys/userList", request, function (data, status, xhr) {
                    App.Cache.userList[ term ] = data.list;
                    response($.map(App.Cache.userList[ term ], function(item) {
                        return {
                            label : item.staffCode + " - " + item.realName,
                            value : item.staffCode + " - " + item.realName,
                            object : item
                        };
                    }));
                });
            },
            change: function (event, ui) {
                if ($(".ui-autocomplete li:textEquals('" + $(this).val() + "')").size() == 0) {
                    $(this).val('').attr('data-id', '');
                }
            },
            select: function( event, ui ) {
                $(this).val( ui.item.value ).attr('data-id', ui.item.object.staffCode);
                return false;
            }
        });

        $lookup.dialog({
            autoOpen: false,
            title: '请选择用户',
            height: 300,
            width: 200,
            modal: true,
            buttons: {
                '确定': function () {
                    callback($lookup.find('input').attr('data-id'));
                    $(this).dialog("close");
                }
            }
        });

        return this.each(function () {
            var $this = $(this);
            $this.click(function () {
                $lookup.dialog('open');
            });
        });
    };

    $.fn.activityHistoric = function(method, id) {
        if (method === 'load' ) {
            if(id) {
                App.activityList.load('rest/act/processInstance/historicActivity/' + id);
                App.detailList.load('rest/act/processInstance/historicDetail/' + id);
            }else{
                App.activityList.reset();
                App.detailList.reset();
            }
        } else if (typeof(method) === 'object' || !method) {
            this.$activityView = $('<div><p>执行过程</p><table class="app"></table></div>');
            this.$detailView = $('<div><p>表单参数</p><table class="app"></table></div>');
            $(this).append(this.$activityView).append(this.$detailView);
            App.ActivityList = App.Collection.extend({
                autoLoad: false,
                columns: [
                    {key:'activityName', label:'名称'},
                    {key:'owner', label:'所有者'},
                    {key:'assignee', label:'承办人', local:'sys/staffKV.do'},
                    {key:'startTime', label:'开始时间', type: 'datetime', disabled: true},
                    {key:'endTime', label:'结束时间', type: 'datetime', disabled: true}]
            });
            App.activityList = new App.ActivityList();
            App.ActivityGrid = App.Grid.extend({
                collection: App.activityList,
                el: this.$activityView,
                $el: this.$activityView,
                toolbar: undefined,
                filter : function(model){
                    return model.get('activityName');
                }
            });

            App.DetailList = App.Collection.extend({
                autoLoad: false,
                columns: [{key:'variableName', label:'名称'},
                    {key:'value', label:'值'}]
            });
            App.detailList = new App.DetailList();
            App.DetailGrid = App.Grid.extend({
                collection: App.detailList,
                el: this.$detailView,
                $el: this.$detailView,
                toolbar: undefined
            });
            new App.ActivityGrid;
            new App.DetailGrid;
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.pluginName');
            return this;
        }
    }

})(jQuery);