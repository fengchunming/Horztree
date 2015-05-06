/*!
 * jQuery HTML5 Uploader 1.0b
 *
 * http://www.igloolab.com/jquery-html5-uploader
 */
(function ($) {
    $.fn.html5Uploader = function (options) {
        var crlf = '\r\n';
        var boundary = "Horztree";
        var dashes = "--";
        var settings = {
            "name": "uploadedFile",
            "url": "Upload.do",
            "onClientAbort": null,
            "onClientError": null,
            "onClientLoad": null,
            "onClientLoadEnd": null,
            "onClientProgress": null,
            "onServerAbort": null,
            "onServerError": null,
            "onServerLoad": null,
            "onServerLoadStart": null,
            "onSuccess": null,
            "onClientLoadStart": null,
            "onServerProgress": function(evt, file){
                if (evt.lengthComputable) {
                    $.sticky('上传'+ file.name,{ autoclose: false,progress: true, value: Math.round(evt.loaded * 100 / evt.total)});
                }
            },
            "onServerReadyStateChange": function (e, file, response) {
                $.sticky('上传'+ file.name,{progress: true, value: 100});
            },
            "onError": function (e, file, jqxhr) {
                var ex = {};
                if (jqxhr.status == 0 && jqxhr.statusText == 'error') {
                    ex['exception'] = "系统错误<br>无法连接服务器";
                } else if (jqxhr.status == 0 && jqxhr.statusText == 'canceled') {
                    ex['exception'] = "请求失败，被系统取消";
                } else if (jqxhr.status == 401) {
                    $("body").load('login.html');
                    return;
                } else {
                    try {
                        ex = JSON.parse(jqxhr.responseText);
                    } catch (e) {
                        ex['exception'] = "系统错误<br>http" + jqxhr.status + "，" + jqxhr.statusText;
                    }
                }
                $.sticky('<span class="ui-icon ui-icon-alert" style="float:left; "></span>' + ex.exception);
            }
        };
        if (options) {
            $.extend(settings, options);
        }
        return this.each(function (options) {
            var $this = $(this);
            if ($this.is("form")) {
                $this.submit(function () {
                    var files = $(this).find("input[type='file']")[0].files;
                    for (var i = 0; i < files.length; i++) {
                        fileHandler(files[i]);
                    }
                    return false;
                });

            } else if ($this.is("[type='file']")) {
                $this
                    .bind("change", function () {
                        var files = this.files;
                        for (var i = 0; i < files.length; i++) {
                            fileHandler(files[i]);
                        }
                    });
            } else {
                $this
                    .bind("dragenter dragover", function () {
                        $(this).addClass("hover");
                        return false;
                    })
                    .bind("dragleave", function () {
                        $(this).removeClass("hover");
                        return false;
                    })
                    .bind("drop", function (e) {
                        $(this).removeClass("hover");
                        var files = e.originalEvent.dataTransfer.files;
                        for (var i = 0; i < files.length; i++) {
                            fileHandler(files[i]);
                        }
                        return false;
                    });
            }
        });
        function fileHandler(file) {
            var fileReader = new FileReader();
            fileReader.onabort = function (e) {
                if (settings.onClientAbort) {
                    settings.onClientAbort(e, file);
                }
            };
            fileReader.onerror = function (e) {
                if (settings.onClientError) {
                    settings.onClientError(e, file);
                }
            };
            fileReader.onload = function (e) {
                if (settings.onClientLoad) {
                    settings.onClientLoad(e, file);
                }
            };
            fileReader.onloadend = function (e) {
                if (settings.onClientLoadEnd) {
                    settings.onClientLoadEnd(e, file);
                }
            };
            fileReader.onloadstart = function (e) {
                if (settings.onClientLoadStart) {
                    settings.onClientLoadStart(e, file);
                }
            };
            fileReader.onprogress = function (e) {
                if (settings.onClientProgress) {
                    settings.onClientProgress(e, file);
                }
            };
            fileReader.readAsDataURL(file);
            var xmlHttpRequest = new XMLHttpRequest();
            xmlHttpRequest.upload.onabort = function (e) {
                if (settings.onServerAbort) {
                    settings.onServerAbort(e, file);
                }
            };
            xmlHttpRequest.upload.onerror = function (e) {
                if (settings.onServerError) {
                    settings.onServerError(e, file);
                }
            };
            xmlHttpRequest.upload.onload = function (e) {
                if (settings.onServerLoad) {
                    settings.onServerLoad(e, file);
                }
            };
            xmlHttpRequest.upload.onloadstart = function (e) {
                if (settings.onServerLoadStart) {
                    settings.onServerLoadStart(e, file);
                }
            };
            xmlHttpRequest.upload.onprogress = function (e) {
                if (settings.onServerProgress) {
                    settings.onServerProgress(e, file);
                }
            };
            xmlHttpRequest.onreadystatechange = function (e) {
                if (settings.onServerReadyStateChange) {
                    settings.onServerReadyStateChange(e, file, xmlHttpRequest);
                }
                if (settings.onSuccess && xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
                    settings.onSuccess(e, file, xmlHttpRequest.responseText);
                } else if (settings.onError && xmlHttpRequest.readyState == 4) {
                    settings.onError(e, file, xmlHttpRequest);
                }
            };
            var url = _.isFunction(settings.url) ? settings.url() : settings.url;
            xmlHttpRequest.open("POST", url, true);
            if (file.getAsBinary) {
                var data = dashes + boundary + crlf
                    + "Content-Disposition: form-data;"
                    + "name=\"" + settings.name + "\";" + crlf
                    + "Content-Type: application/octet-stream" + crlf
                    + crlf + file.getAsBinary() + crlf + dashes + boundary
                    + dashes;

                xmlHttpRequest.setRequestHeader("X-File-Name", encodeURIComponent(file.name));

                xmlHttpRequest.setRequestHeader("Content-Type", "multipart/form-data;boundary=" + boundary);
                xmlHttpRequest.sendAsBinary(data);
            } else if (window.FormData) {
                xmlHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                xmlHttpRequest.setRequestHeader("X-File-Name", encodeURIComponent(file.name));
                xmlHttpRequest.setRequestHeader("Content-Type", "application/octet-stream");
                xmlHttpRequest.send(file);

//                var formData = new FormData();
//                formData.append(settings.name, file);
//
//                xmlHttpRequest.send(formData);
            }
        }
    };
})(jQuery);