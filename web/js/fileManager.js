/**
 * Created by karas on 7/9/14.
 */
(function ($) {

    var buildElem = function (data) {
        //HTML5 a标签  DownLoad属性
        return '<div><a href="rest/sys/downloadFile?code=' + data.code + '" download="' + data.fileName + '">'
            + data.fileName + '</a><i data-id="' + data.id + '" class="removeHandler"></i></div>';
    };

    var methods = {
        init: function (options) {
            return this.each(function () {
                var $this = $(this);
                var settings = $this.data('fileManager');
                if (typeof(settings) == 'undefined') {
                    var defaults = {
                        idsInput: undefined,
                        idArray: []
                    };

                    settings = $.extend({}, defaults, options);

                    $this.data('fileManager', settings);
                } else {
                    settings = $.extend({}, settings, options);
                }


                //添加文件上传
                $('#container').append('<form><input id="uploadHandler" type="file" style="display:none;"></form>');
                //上传文件
                $('#uploadHandler').html5Uploader({
                    url: "rest/sys/uploadFile",
                    onSuccess: function (e, file, response) {
                        var data = JSON.parse(response);
                        settings.idArray.push(data.id);
                        settings.idsInput.val(settings.idArray.join(','));
                        $this.append(buildElem(data));
                        $.stopLoading();
                    }
                });

                $this.on('click', '.removeHandler', function () {
                    var ts = $(this);
                    if (confirm('你确认要删除该附件？'))
                        $.ajax({
                            url: "rest/sys/updateStatus/" + ts.attr('data-id'),
                            type: "put",
                            async: false,
                            success: function () {
                                settings.idArray = _.without(settings.idArray, ts.attr('data-id'));
                                settings.idsInput.val(settings.idArray.join(',')); 
                                ts.parent().remove();
                                $.sticky("删除成功"); 
                            },
                            error: function () {
                                $.sticky("删除失败");
                            }
                        });
                });

            });
        },
        destroy: function () {
            return $(this).each(function () {
                var $this = $(this);
                $this.removeData('fileManager');
            });
        },
        load: function (ids) {  //ids：文件id 字符串
            var $this = $(this);
            var settings = $this.data('fileManager');
            $this.empty();
            if (!ids) return;
            settings.idArray = ids.split(",");
            $.ajax({
                url: 'rest/sys/accessoryKeys',
                data: {"attachs": ids},
                type: 'get',
                success: function (data) {
                    var fileList = '';
                    for (var i in data) {
                        fileList += buildElem(data[i]);
                    }
                    $this.html(fileList);
                }
            });
        },
        upload: function () {
            $("#uploadHandler").click();
        }
    };

    // Extend jQuery
    $.fn.fileManager = function (opts) {
        var method = arguments[0];

        if (methods[method]) {
            method = methods[method];
            arguments = Array.prototype.slice.call(arguments, 1);
        } else if (typeof(method) == 'object' || !method) {
            method = methods.init;
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.pluginName');
            return this;
        }

        return method.apply(this, arguments);
    };

})(jQuery);