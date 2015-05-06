/*
 * Read Request parameter by name, use it like this
 * Request.QueryString(name);
 */
Request = {
    getParam: function (item) {
        var svalue = location.search.match(new RegExp("[\?\&]" + item + "=([^\&]*)(\&?)", "i"));
        return svalue ? svalue[1] : svalue;
    },
    getRemote: function () {
        return location.hostname;
    },
    QueryURL: function () {
        return location.pathname;
    },
    QueryAnchor: function () {
        var svalue = location.href.match(new RegExp("#([^\?\&]*)([\?\&]?)", "i"));
        return decodeURIComponent(svalue ? svalue[1] : svalue);
    },
    QueryDomain: function () {
        var pathName = location.pathname;
        var index = pathName.indexOf("rest/");
        return pathName.substr(0, index);
    },
    getCookie: function (name) {
        var r = document.cookie.match("\\b" + name + "=([^;]*)\\b");
        return r ? r[1] : undefined;
    }
};


/*
 * check all input:checkbox or uncheck them in a table sheet
 * if the sheet has more than one group,
 * you can add name attribute to the checkbox to filter which group to check
 *
 * e: CheckAll('#sheet')
 *
 */
CheckAll = function (area) {
    var obj = event.srcElement ? event.srcElement : event.target;
    if (obj.getAttribute('name') != null) {
        $(area).find(':checkbox[name="' + obj.getAttribute('name') + '"]').attr("checked", obj.checked);
    } else {
        $(area).find(':checkbox').attr("checked", obj.checked);
    }
};

$(function () {
    $.expr[':'].textEquals = function (a, i, m) {
        return $(a).text().match("^" + m[3] + "$");
    };

    $.download = function (url, data, method) {
        // 获取url和data

        // data 是 string 或者 array/object
        // 把参数组装成 form的  input
        var inputs = '';
        if (data) {
            if (typeof data == 'string') {
                $.each(data.split('&'), function () {
                    var pair = this.split('=');
                    inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
                });
            } else {
                $.each(data, function (i, e) {
                    inputs += '<input type="hidden" name="' + i + '" value="' + e + '" />';
                });
            }
        }
        if (url)
        // request发送请求
            $('<form action="' + url + '" target="_blank" method="' + (method || 'post') + '">' + inputs + '</form>')
                .appendTo('body').submit().remove();

    };

    function mergeObject(ma, br, de, i) {
        if (_.isObject(br[de[i]]) && _.isObject(ma[de[i]])) {
            ma[de[i]] = mergeObject(ma[de[i]], br[de[i]], de, i + 1);
        } else {
            ma[de[i]] = br[de[i]];
        }
        return ma;
    }

    /*
     * serializeObject use for convert Form to Json
     * e: $('form').serializeObject()
     */
    $.fn.serializeObject = function () {
        var o = {};
        $.merge($(':input:enabled[name]', this), $(':input:enabled[name][form="' + $(this).attr('name') + '"]')).each(function () {
            var name = this.name;
            var val = $.trim(this.value || '');
            if (this.type === 'radio' && !this.checked) {
                return;
            } else if (this.type === 'number') {
                val = parseFloat(val);
            } else if (this.type === 'checkbox') {
                if (val === 'on') {
                    val = this.checked;
                }
            }
            var key_sp = name.split('.');

            //TODO: 算法有待优化
            if (key_sp.length > 1) {
                for (var i = key_sp.length - 1; i >= 0; i--) {
                    var tmp = {};
                    tmp[key_sp[i]] = val;
                    val = tmp;
                }
                o = mergeObject(o, val, key_sp, 0);
            } else {
                if (o[name]) {
                    if (!o[name].push) {
                        o[name] = [o[name]];
                    }
                    o[name].push(val);
                } else {
                    o[name] = val;
                }
            }
        });
        return o;
    };

    /*
     * reset a form,
     */
    $.fn.reset = function () {
        $.merge($(':input', this), $(':input[form="' + $(this).attr('name') + '"]')).each(function () {
            var type = this.type;
            var tag = this.tagName.toLowerCase();
            if (tag === 'input') {
                if (type == 'checkbox' || type == 'radio') {
                    this.checked = $(this).attr('data-default') ? $(this).attr('data-default') : false;
                } else {
                    this.value = $(this).attr('data-default') ? $(this).attr('data-default') : "";
                }
            } else if (tag === 'textarea') {
                this.value = $(this).attr('data-default') ? $(this).attr('data-default') : '';
            } else if (tag === 'select' && this.name) {
                this.selectedIndex = 0;
            }
        });
    };


    /*
     * init form element value,
     * add data-default attribute to the :input element,
     * add form="form-name" attribute to contain the :input out of the form
     */
    $.fn.setData = function (data) {
        if (data) $.merge(
            $(':input', this),
            $(':input[form="' + $(this).attr('name') + '"]'))
            .each(function () {
                var type = this.type;
                var tag = this.tagName.toLowerCase();
                var name = this.name.split('.');
                var value = data;
                for (var i = 0; i < name.length; i++) {
                    if (value != null && value.hasOwnProperty(name[i])) {
                        value = value[name[i]];
                    } else {
                        value = '';
                        break;
                    }
                }

                if (value == null) {
                    value = '';
                }

                if (tag === 'input') {
                    if (type === 'checkbox' || type === 'radio') {
                        this.checked = (!_.isNull(value) && (this.value == value + '' || _.indexOf(value, this.value) >= 0 || (this.value == 'on' && value)));
                    } else {
                        this.value = value;
                    }
                } else if (tag === 'textarea') {
                    this.value = value;
                } else if (tag === 'select' && type === 'select-one' && this.name) {
                    var flag = false;
                    for (var i = 0; i < this.options.length; i++) {
                        this.options[i].selected = false;
                        if (this.options[i].value === value + '') {
                            this.options[i].selected = true;
                            flag = true;
                        }
                    }
                    if (!flag) this.selectedIndex = 0;
                } else if (tag === 'select' && type === 'select-multiple') {
                    for (var i = 0; i < this.options.length; i++) {
                        this.options[i].selected = false;
                        for (var j = 0; j < value.length; j++)
                            if (this.options[i].value == value[j]) {
                                this.options[i].selected = true;
                            }
                    }
                }
            });
    };
});
