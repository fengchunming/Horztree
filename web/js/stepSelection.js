(function ($) {
    var defaults = {
        maxLevel: 0,
        mustLeaf: false,
        urlRoot: 'rest/sys/regionList',
        callback: function () {
            return false;
        }
    };
    var methods = {
        init: function (options) {
            return this.each(function () {
                var $this = $(this);
                var container = $('<span></span>');
                $this.hide().after(container);
                var settings = $this.data('stepSelection');
                if (typeof(settings) === 'undefined') {
                    settings = $.extend({}, defaults, options);
                } else {
                    settings = $.extend({}, settings, options);
                }
                settings.container = container;
                $this.data('stepSelection', settings);
            });
        },
        destroy: function () {
            return this.each(function () {
                var $this = $(this);
                $this.removeData('stepSelection');
            });
        },
        load: function (val) {
            return this.each(function () {
                var $this = $(this);
                var settings = $this.data('stepSelection');
                settings.id = val;
                settings.container.empty();
                if ($this.val()) { //如何存在初始地区数据
                    var array = $this.val().split(',');// 取地区ID
                    array[0] = 0;
                    for (var i = 1; i < array.length && array[i-1] >= 0; i++){
                        methods.loadData(array[i - 1], parseInt(array[i]), settings); //加载各级地区select
                    }
                }else{
                    methods.loadData(0, null, settings);
                    methods.callback(settings);
                }
            });
        },
        loadData: function (pid, id, opts) {  //ids：文件id 字符串
            $.ajax({
                url: opts.urlRoot,
                dataType: 'json',
                data: {"parent": parseInt(pid)},
                async: false,
                success: function (data) {
                    if (data.list.length) {
                        if (opts.maxLevel > 0 && data.list[0].level > opts.maxLevel) return;
                        var e = $('<select style="width:auto;margin-right:2px"'
                        + (opts.mustLeaf?' required':'') +'><option value="">--请选择--</option></select>');

                        $.each(data.list, function (i, item) {
                            if(!opts.id || opts.id != item.id)
                            e.append($('<option' + (id && id == item.id ? ' selected="selected"' : "") + '></option>')
                                .attr('value', item.id)
                                .attr('data-leaf', item.leaf)
                                .text(item.name));
                        });
                        e.appendTo(opts.container).change(function () {
                            $(this).nextAll().remove();
                            if ($(this).val()) {
                                methods.loadData($(this).val(), null, opts);
                            }
                            methods.callback(opts);
                        });
                    }
                }
            });
        },
        callback: function(opts){
            var id = 0;
            var text = opts.container.children().map(function(){
                if($(this).find("option:selected").val())
                    return $(this).find("option:selected").text();
            }).get().join(" ");

            var ids = opts.container.children().map(function(){
                if($(this).find("option:selected").val()) {
                    id = $(this).find("option:selected").val();
                    return id;
                }
            }).get();
            opts.callback(id, ids, text);
        }
    };


    // Extend jQuery
    $.fn.stepSelection = function (options) {
        var method = arguments[0];
        if (methods[method]) {
            method = methods[method];
            arguments = Array.prototype.slice.call(arguments, 1);
        } else if (typeof(method) === 'object' || !method) {
            method = methods.init;
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.pluginName');
            return this;
        }

        return method.apply(this, arguments);
    };


})(jQuery);