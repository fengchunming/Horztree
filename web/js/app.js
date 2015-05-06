var App = {
    Plugin: {},
    Static: 'static/resource/',
    Cache: {},
    Constant: {status: {trash: 'd'}},
    Regex: {
        required: function (value) {
            return $.trim(value).length > 0;
        },
        dateISO: function (value) {
            return /^\d{4}[\/\-]\d{1,2}[\/\-]\d{1,2}$/.test(value);
        },
        number: function (value) {
            return /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(value);
        },
        digits: function (value) {
            return /^\d+$/.test(value);
        },
        minlength: function (value, param) {
            var length = $.isArray(value) ? value.length : $.trim(value).length;
            return length >= param;
        },
        maxlength: function (value, param) {
            var length = $.isArray(value) ? value.length : $.trim(value).length;
            return length <= param;
        },
        rangelength: function (value, element, param) {
            var length = $.isArray(value) ? value.length : this.getLength($.trim(value), element);
            return this.optional(element) || (length >= param[0] && length <= param[1]);
        },
        min: function (value, param) {
            return value >= param;
        },
        max: function (value, param) {
            return value <= param;
        },
        range: function (value, param) {
            return (value >= param[0] && value <= param[1]);
        },
        equalTo: function (value, param) {
            return value === param;
        }
    }
};

$(function () {
    var flatten = function (obj, into, prefix) {
        into = into || {};
        prefix = prefix || '';
        for (var key in obj) {
            var val = obj[key];
            if (obj.hasOwnProperty(key)) {
                if (val && typeof val === 'object' && !(
                    val instanceof Array ||
                    val instanceof Date ||
                    val instanceof RegExp ||
                    val instanceof Backbone.Model ||
                    val instanceof Backbone.Collection)
                ) {
                    flatten(val, into, prefix + key + '.');
                } else {
                    into[prefix + key] = val;
                }
            }
        }
        return into;
    };

    var nested = function () {

    };

    /*
     *#######################################################################################################
     *###################    Data Model
     *#######################################################################################################
     */
    App.Model = Backbone.Model.extend({
        isRemoved: false,
        selected: false,
        rules: undefined,
        focus: function () {
            if (this.form && this.form.display) {
                this._edit();
            }
        },
        _edit: function () {
            if (this.form && !this.locked()) {
                this.form.setModel(this);
            }
            this.edit();
        },
        edit: function () {
            ;
        },
        change: function (item) {

        },
        locked: function (col) {
            return false;
        },
        _success: function (model, response) {
            if (model.form && model.form.grid && !model.row) {
                model.form.grid.addOne(model, true);
            }
            if (_.isFunction(this.success)) this.success();
        },
        hidden: function () {

        },
        validate: function (data) {
            if (!data) data = this.toJSON();
            var ts = this;
            if (!_.isUndefined(this.rules)) {
                for (var e in this.rules) {
                    var rule = this.rules[e];
                    if (_.isObject(rule)) {
                        for (var i in rule) {
                            if (!App.Regex[i](data[e], rule[i])) {
                                ts.row.$el.addClass('error');
                                return false;
                            }
                        }
                    } else {
                        if (!App.Regex[rule](data[e])) {
                            ts.row.$el.addClass('error');
                            return false;
                        }

                    }
                }
            }
        },
        select: function (state) {
            if (_.isUndefined(state)) {
                this.selected = !this.selected;
            } else {
                this.selected = state;
            }
            this.change();
        }
    });


    /*
     *#######################################################################################################
     *###################    Data Collection
     *#######################################################################################################
     */
    App.Collection = Backbone.Collection.extend({
        model: App.Model,
        count: 0,
        pageSize: 20,
        currentPage: 0,
        autoLoad: true,
        type: 'tbody',
        columns: [],
        sortCol: undefined,
        sortDirect: '',
        initialize: function () {
            this.param = this.param || {};
        },
        url: function () {
            return this.urlRoot || this.model.prototype.urlRoot && this.model.prototype.urlRoot + '/list';
        },
        load: function () {
            if (_.isString(arguments[0])) {
                this.url = arguments[0];
                if (typeof(arguments[1]) === 'object') {
                    _.extend(this.param, arguments[1]);
                }
            } else if (typeof(arguments[0]) === 'object') {
                _.extend(this.param, arguments[0]);
            }

            if (this.type === 'tree' && !this.param['parent']) {
                this.param['parent'] = 0;
            }
            if (this.pageSize) {
                this.param['_ST'] = this.currentPage * this.pageSize;
                this.param['_LM'] = this.pageSize;
            }
            if (this.sortCol)
                this.param['_BY'] = this.sortCol + ' ' + this.sortDirect;
            this.fetch({reset: true, remove: false, data: this.param});
        },
        stepLoad: function () {
            _.extend(this.param, arguments[0]);
            this.fetch({reset: false, remove: false, data: this.param});
        },
        parse: function (response) {
            this.focus = null;
            if (_.isArray(response)) {
                return response;
            } else {
                this.count = response.count;
                return response.list;
            }
        },
        save: function () {
            var ts = this;
            $.ajax({
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(ts.toJSON()),
                type: 'POST',
                precessData: false,
                url: ts.url,
                success: function (data) {
                    ts.load();
                }
            });
        },
        validate: function () {
            for (var i in this.models) {
                var model = this.models[i];
                if (!model.isRemoved && !_.isUndefined(model.validate())) {
                    return false;
                }
            }
            return true;
        },
        sum: function (clm) {
            var total = 0;
            this.each(function (model) {
                if (_.isNumber(model.get(clm)) && model.get('status') != App.Constant.status.trash)
                    total -= model.get(clm);
            }, this);
            return -total;
        },
        selectIds: function (clm) {
            return $.map(this.models, function (model) {
                if (model.selected) {
                    return clm ? model.get(clm) : model.id;
                }
            });
        }
    });


    /*
     *#######################################################################################################
     *###################    Row View of DataGrid
     *#######################################################################################################
     */

    App.Row = Backbone.View.extend({
        tagName: "tr",
        attr: ['style', 'class'],
        attributes: function () { // set view html attribute
            if (this.model.collection.type === 'tree') {
                return {
                    "class": (this.model.get('leaf') ? '' : 'parent ') + 'level-' + this.model.get('step')
                };
            }
        },
        events: {
            'click': 'click',
            'click .fold': 'fold',
            'dblclick': 'dblclick',
            'change :input': 'change'
        },
        initialize: function () {
            this.model.on('change', this.render, this);
            this.model.on('destroy', this.remove, this);
            this.render();
        },
        render: function () {
            this.$el.removeClass('error');
            var html = '';
            var model = this.model;
            var data = flatten(model.attributes);
            var cl = model.collection;
            var ts = this;

            $.each(cl.columns, function (i, c) {
                if (_.isUndefined(c)) return true;

                var val = data[c.key];
                if (_.isUndefined(val) || _.isNull(val)) {
                    val = '';
                }

                html += '<td ';
                _.each(ts.attr, function (a) {
                    if (c[a]) { // set <td> attribute
                        html += a + '="' + (_.isFunction(c[a]) ? c[a](val) : c[a]) + '"';
                    }
                });
                html += ' data-key="' + c.key + '"';
                html += '>';

                if (cl.type === "tree" && i == 0) { // add expended/collapsed icon
                    html += '<i class="fold"></i>';
                }
                if (c.render) { // build <td> using customer function
                    html += _.isFunction(c.render) ? c.render(val, model) : c.render;

                } else {
                    // build <td> using default
                    switch (true) {
                        case (c.type === 'checkbox'): // checkbox
                            if (_.isUndefined(c.label) || c.label === '') {
                                html += '<input type="' + c.type + '" ' + (model.selected ? ' checked' : '') + (model.locked(c) ? ' disabled' : '') + ' value="' + val + '">';
                            } else {
                                html += '<input type="' + c.type + '" name="' + c.key + '"' + (val ? ' checked' : '') + (model.locked(c) ? ' disabled' : '') + ' value="' + val + '">';
                            }
                            break;
                        case (c.type === 'radio'): // checkbox
                            html += '<input type="' + c.type + '" name="' + c.key + '"' + (model.locked(c) ? ' disabled' : '') + ' value="' + val + '">';
                            break;

                        case (c.type === 'text' || c.type === 'number'): // input html element
                            if (!model.locked(c)) {
                                html += '<input name="' + c.key + '" type="' + c.type + '" value="' + val + '" class="line-input" />';
                            } else {
                                html += val;
                            }
                            break;
                        case (c.type === 'date'):
                            if (val && !isNaN(val)) val = new Date(val).format('isoDate');
                            if (!model.locked(c) && !c.disabled) {
                                html += '<input name="' + c.key + '" type="text" value="' + val + '" class="line-input datepicker" />';
                            } else {
                                html += val;
                            }
                            break;
                        case (c.type === 'datetime'):
                            if (val && !isNaN(val)) val = new Date(val).format('yyyy-mm-dd HH:MM');
                            if (!model.locked(c) && !c.disabled) {
                                html += '<input name="' + c.key + '" type="text" value="' + val + '" class="line-input datetimepicker" />';
                            } else {
                                html += val;
                            }
                            break;
                        case (c.type === 'textarea'): // input html element
                            if (!model.locked(c)) {
                                html += '<textarea name="' + c.key + '" class="line-input">' + val + '</textarea>';
                            } else {
                                html += val;
                            }
                            break;
                        case (c.type === 'select'): // select html element
                            tryLoad(c.local);
                            if (!model.locked(c)) {
                                html += '<select name="' + c.key + '" class="line-input" value="' + val + '">' +
                                mkOptions(c.local, val) + '</select>';
                            } else if (val != null) {
                                html += _LocalKV[c.local][val];
                            }
                            break;
                        case (c.type === 'img'): // 图片
                            html += '<img src="' + val + '" class="inline-img">';
                            break;
                        default: // default template
                            if (c.local) {
                                tryLoad(c.local);
                                if (val instanceof Array) {
                                    val = $.map(val, function (m) {
                                        return _.isUndefined(_LocalKV[c.local][m]) ? m : _LocalKV[c.local][m];
                                    }).join(', ');
                                } else {
                                    val = _.isUndefined(_LocalKV[c.local][val]) ? val : _LocalKV[c.local][val];
                                }
                                val = ("" + val).replace(/&nbsp;/gi,'');
                            }
                            html += '<span' + (val.length > 4 ? ' title="' + val + '"' : '') + '>' + val + '</span>';
                    }
                }
                html += '</td>';
            });
            this.$el.html(html);
            this.$('.datepicker').datepicker({
                dateFormat: "yy-mm-dd"
            });
            this.$('.datetimepicker').datepicker({
                dateFormat: "yy-mm-dd"
            });
            this.listen();
            return this;
        },
        click: function () {
            this.model.focus();
            this.$el.siblings().removeClass('ui-selected');
            this.$el.addClass('ui-selected');
        },
        dblclick: function () {
            this.model._edit();
        },
        listen: function () {

        },
        change: function (event) {
            var e = event.target;
            var data = {};
            if ($(e).attr('type') === 'checkbox') {
                if (!e.name) {
                    this.model.select();
                } else {
                    data[e.name] = !this.model.get(e.name);
                    this.model.set(data, {silent: true});
                    this.model.change(data);
                }
            } else if (e.name) {
                var o = e.name;
                var v = e.value;
                var keys = o.split('.');
                if (keys.length > 1) {
                    data = this.model.get(keys[0]) ? this.model.get(keys[0]) : {};
                    data[keys[1]] = v;
                    data[keys[0]] = data;
                } else {
                    data[o] = v;
                }
                this.model.set(data, {silent: true});
                this.model.change(data);
            }
        },
        fold: function () {
            var cl = this.model.collection;
            if (this.$el.hasClass('parent') && this.$el.hasClass('expend')) {
                var models = cl.where({'parent': this.model.id});
                while (models.length) {
                    var m = models[models.length - 1];
                    models.length--;
                    models = models.concat(cl.where({'parent': m.id}));
                    m.row.remove();
                    cl.remove(m);
                }
                this.$el.removeClass('expend');

            } else if (this.$el.hasClass('parent')) {
                cl.stepLoad({parent: this.model.id});
                this.$el.addClass('expend');
            }
        },
        remove: function () {
            this.$el.remove();
        },
        serialize: function () {
            this.model.set(this.$el.serializeObject());
        }
    });

    /*
     *#######################################################################################################
     *###################    Toolbar for DataGrid or FormView
     *#######################################################################################################
     */
    App.Toolbar = Backbone.View.extend({
        el: '.toolBar:first',
        dync: false,
        defaults: {
            'create': '新建',
            'copy': '复制',
            'save': '保存',
            'edit': '编辑',
            'hide': '关闭',
            'trash': '删除',
            'append': '增加',
            'remove': '删除',
            'remoteRemove': '删除',
            'check': '确认',
            'inport': '导入',
            'export': '导出',
            'print': '打印',
            'auto': '自动生成',
            'resolve': '完成',
            'sync': '同步',
            'gear': '执行',
            'search': '查询',
            'merge': '合并',
            'pause': '暂停',
            'suspend': '挂起',
            'activate': '激活',
            'complete': '完成',
            'stop': '中止',
            'undo': '还原',
            'link': '链接'
        },
        initialize: function (arg) {
            this.router = arg.router;
            this.render();
        },
        events: {
            'click button': 'trigger'
        },
        render: function (model) {
            var ths = this;
            var html = _.map(this.attributes, function (e) {
                if (_.isString(e)) {
                    e = {key: e}
                } else {
                    e.key = _.keys(e)[0];
                    e.label = e[e.key];
                    if (e.visible) ths.dync = true;
                    if (e.visible && model && !e.visible(model)) return;
                    if (e['id'] && !_.contains(App.Cache.button, e['id'])) return;
                }
                e.type = e.type || 'button';
                if (_.isUndefined(ths.defaults[e.key])) {
                    e.label = e.label || e.key;
                } else {
                    e.icon = e.icon || e.key;
                    e.label = e.label || ths.defaults[e.key];
                }
                return '<button type="' + e.type + '" class="' + e.icon + '" data-ope="' + e.key + '">' + (e.icon ? '<i></i>' : '') + '<span>' + e.label + '</span></button>';
            }).join('');
            this.$el.html(html).buttonset();
        },
        trigger: function (event) {
            this.router[$(event.currentTarget).attr('data-ope')]();
        }
    });


    /*
     *#######################################################################################################
     *###################    Form View  / Data Editor
     *#######################################################################################################
     */
    App.FormView = Backbone.View.extend({
        el: '.navPanel',
        toolbar: ['create', 'save', 'trash', 'hide'],
        display: false,
        rules: {},
        sync: false,
        title: '编辑',
        size: {width: 600},
        type: 'slide',
        initialize: function () {
            this.$el || (this.$el = $(this.el));
            this.$form = this.$('form:first');
            if (this.toolbar) {
                this.toolbar = new App.Toolbar({
                    el: this.$('.toolBar:first'),
                    attributes: this.toolbar,
                    router: this
                });
            }
            convertLocal();

            this.valid = this.$form.validate({
                rules: this.rules
            });

            var ts = this;

            if (this.sync) {
                this.$(':input[name]').on('change', function () {
                    var o = this.name;
                    var v = this.value;
                    if (this.type === 'checkbox' && !this.checked) {
                        v = null;
                    }

                    var keys = o.split('.');
                    if (keys.length > 1) {
                        var data = ts.model.get(keys[0]);
                        if (!data) {
                            data = {};
                        }
                        data[keys[1]] = v;
                        ts.model.set(keys[0], data);
                    } else {
                        ts.model.set(o, v);
                    }
                });
            }

            if (this.listen) this.listen();
            this.render();
        },
        reset: function () {
            this.$form.reset();
        },
        serialize: function () {
            return this.$form.serializeObject();
        },
        isValid: function () {
            return this.$form.valid();
        },
        setModel: function (model) {
            this.model = model;
            this.model.on('change', this.setData, this);
            if (this.model.attributes) this.setData();
            this.show();
        },
        setData: function () {
            this.$form.setData(this.model.attributes);
            if (this.toolbar && this.toolbar.dync) this.toolbar.render(this.model);
        },
        render: function () {
            var ts = this;
            ts.$el.addClass(ts.type + "-panel");
            if (ts.type === 'slide') {
                // 计算弹出的左边距
                var pWidth = ts.$el.parent().width();
                ts.size.left = ts.size.left || ( pWidth - ts.size.width);
                ts.$el.css({left: pWidth, width: this.size.width, display: 'block'})
                    .resizable({
                        handles: "w",
                        minWidth: 7,
                        maxWidth: pWidth,
                        stop: function (event, ui) {
                            if (ui.size.width < 300) {
                                ts.$el.addClass('min');
                                ts.display = false;
                            } else {
                                ts.$el.removeClass('min');
                                ts.display = true;
                                ts.size = {
                                    left: ui.position.left,
                                    width: ui.size.width
                                };
                            }
                        }
                    });

                $('<div class="handle"><span></span></div>').appendTo('.ui-resizable-w:first').click(function () {
                    if (ts.$el.hasClass('min')) {
                        ts.show();
                    } else {
                        ts.hide('min');
                    }
                });

            } else if (ts.type === 'down') {
                ts.$el.css(ts.size);
            } else if (ts.type === 'right') {
                ts.$el.css(ts.size);
            } else if (ts.type === 'popup') {
                ts.$el.dialog({
                    autoOpen: false,
                    appendTo: '#container',
                    modal: true,
                    title: ts.title,
                    width: ts.size.width,
                    maxWidth: 1000,
                    maxHeight: 800,
                    minWidth: 300,
                    minHeight: 200,
                    close: function () {
                        ts.hide();
                    }
                });
            }
            return this;
        },
        show: function () {
            if (this.display) return;
            this.display = true;
            if (this.type === 'slide') { // 侧移显示
                this.$el.stop().animate(this.size, 500);
                this.$el.removeClass('min');
                this.$('#tabs').tabs({active: 0});

            } else if (this.type === 'popup') { // 弹出显示
                this.$el.dialog("open");
                $('button:focus').blur();

            } else if (this.type === 'inline') { // 下拉显示
                var handler = this.model.row.$el;
                handler.siblings('.spanrow').remove();
                handler.after('<tr class="spanrow"><td class="inline" style="height:' + this.$el.height() + 'px" colspan="' + this.model.row.$el.children().length + '"></td></tr>');
                this.$el.show().position({
                    of: handler.next(), my: 'left top', at: 'left top'
                });
            } else if (this.type === 'hide') {
                this.$el.show();
            }
        },
        hide: function (arg) {
            if (!this.display && arg != 'force') return;
            this.display = false;
            if (this.type === 'slide') {
                if (arg === 'min') {
                    this.$el.stop().animate({left: this.size.left + this.size.width - 10}, 500);
                } else {
                    this.$el.stop().animate({left: this.size.left + this.size.width}, 500);
                }
                this.$el.addClass('min');
            } else if (this.type === 'popup') {
                this.$el.dialog("close");
            } else if (this.type === 'inline') {
                this.$el.hide();
                if (this.model) this.model.row.$el.siblings('.spanrow').remove();
            } else if (this.type === 'hide') {
                this.$el.hide();
            }
        },
        create: function (event) {
            this.grid.append();
        },
        append: function (event) {
            this.grid.append();
        },
        prepare: function (params) {
            return params;
        },
        save: function () {
            if (this.isValid() && _.isUndefined(this.model.validate())) {
                this.model.save(this.prepare(this.serialize()), {
                    success: this.model._success
                });
            }
        },
        trash: function () {
            if (this.model && confirm("确认删除该数据？")) {
                if(this.model.get('leaf') === false){
                    $.sticky("删除失败该节点子节点不为空！");
                    return;
                }
                this.model.destroy({wait: true});
                this.hide();
            }
        }
    });

    /*
     *#######################################################################################################
     *###################    DataGrid
     *#######################################################################################################
     */
    App.Grid = Backbone.View.extend({
        el: '#article',
        pel: '.page',
        qel: undefined,
        querybar: undefined,
        withHead: true,
        editable: false,
        tel: undefined,
        toolbar: ['create', 'trash'],
        row: App.Row,
        events: {
            'click th.sort': '_sort',
            'click th.check': '_check'
        },
        initialize: function () {
            this.$el = this.$el || $(this.el);
            this.listenTo(this.collection, 'reset', this.addAll);
            this.listenTo(this.collection, 'add', this.addOne);
            if (this.withHead) {
                this.$el.find('table.app:first').append('<thead></thead><tbody></tbody>');
                if (this.head) {// 绘制表头
                    _.isFunction(this.head) ? this.head() : this.$('thead').html(this.head);
                }
            } else {
                this.$el.find('table.app:first').append('<tbody></tbody>');
            }

            var cl = this.collection;

            if (this.toolbar) { //初始化工具栏
                var tbar = this.tel ? $(this.tel) : this.$('.toolBar:first');
                this.toolbar = new App.Toolbar({
                    el: tbar,
                    attributes: this.toolbar,
                    router: this
                });
            }

            //初始化查询Form
            if (this.qel) {
                this.querybar = this.$(this.qel);
                if (this.querybar.length === 0) this.querybar = $(this.qel);
                var grid = this;
                this.querybar.submit(function () {
                    grid.search();
                    return false;
                }).on('click', ':input[name]', function () {
                    if (grid.form) grid.form.hide('min');
                });
            }

            //如果定义了url根，并且 自动加载开启
            if (cl.autoLoad && (cl.url())) {
                this.search();
            }

            if (App.request) {
                //接收全局参数，记入查询条件后清空
                if (this.querybar) {
                    this.querybar.setData(App.request);
                } else {
                    _.extend(cl.param, App.request);
                }
                App.request = null;
                $(document).one('ajaxStop', function () {
                    if (cl.length == 1) {
                        cl.at(0).edit();
                    }
                });
            }

            var model = new this.collection.model;
            if (model.rules) { // 如果定义了表单验证
                this.$('table:first').wrap('<form>').parent().validate({
                    rules: model.rules
                });
            }

            if (this.form) {
                this.form.grid = this;
                //按页面布局，调整Grid的宽度或高度
                if (this.form.type === 'down') {
                    this.$el.css('bottom', this.form.size.height);
                } else if (this.form.type === 'right') {
                    this.$el.css('right', this.form.size.width);
                }
            }
        },
        render: function () {
            return this;
        },
        head: function () {
            var html = '<tr>';
            _.each(this.collection.columns, function (elem) {
                if (_.isUndefined(elem)) return true;
                html += '<th';
                html += elem.type == 'checkbox' ? ' class="check"' : '';

                if (elem.sort === true) { // 默认采用同名字段排序
                    html += ' class="sort" data-header="' + elem.key + '" data-sort="' + elem.key + '"';
                } else if (elem.sort) {
                    html += ' class="sort" data-header="' + elem.key + '" data-sort="' + elem.sort + '"';
                }
                html += elem.thcss ? ' style="' + elem.thcss + '"' : '';
                html += '>' + (elem.label ? elem.label : '') + '<span></span></th>';
            });
            html += '</tr>';
            this.$('thead').html(html);
        },
        page: function () {
            var cl = this.collection;
            if (cl.pageSize) { // 需要进行分页

                var ct = 0; // 总条数
                var message = "";
                if (_.isObject(cl.count)) {
                    ct = cl.count.ct;
                    message = "共" + ct + "条记录";
                    $.each(cl.count, function (i, item) {
                        if (i != 'ct') message += ", " + i + " : " + item;
                    });
                } else {
                    ct = cl.count;
                    message = "共" + ct + "条记录";
                }

                // 分页栏展示
                this.$(this.pel).pagination(ct, {
                    callback: function (page) {
                        if (page >= 0) cl.currentPage = page;
                        cl.load();
                    },
                    current_page: cl.currentPage,
                    items_per_page: cl.pageSize,
                    message: message
                });
            } else { // 不需要分页
                this.$(this.pel).hide();
            }
        },
        _check: function () {  // 列表全选，反选
            this.collection.each(function (model) {
                if (!model.locked()) {
                    model.select();
                    model.row.render();
                }
            });
        },
        _sort: function (event) { // 点击标题排序
            var $e = $(event.currentTarget), cl = this.collection;
            if (cl.sortCol && cl.sortCol == $e.attr('data-sort')) {
                cl.sortDirect = (cl.sortDirect == "asc") ? 'desc' : 'asc';
                $e.removeClass("asc desc").addClass(cl.sortDirect);
            } else {
                this.$('th[data-sort=' + cl.sortCol + ']').removeClass('asc desc');
                cl.sortCol = $e.attr('data-sort');
                cl.sortDirect = 'asc';
                $e.toggleClass("asc");
            }
            cl.load();
        },
        addOne: function (model, isNew) {
            if (this.form) model.form = this.form;
            if (model.get('status') === App.Constant.status.trash || (!this.editable && model.isNew()) || model.hidden())
                return;

            model.row = new this.row({model: model});

            if (this.collection.type === 'tree') {
                var e = this.collection.at(this.collection.indexOf(model) - 1);
                if (!e || e.get('parent') != model.get('parent'))
                    e = this.collection.get(model.get("parent"));

                if (e) {
                    e.row.$el.after(model.row.el);
                } else {
                    this.$('tbody').append(model.row.el);
                }
            } else {
                //if (isNew)
                //    this.$(this.collection.type).append(model.row.el);
                //else
                this.$(this.collection.type).append(model.row.el);
            }
        },
        addAll: function () {
            if (this.form) this.form.hide('force');
            this.$('tbody').empty();
            if (this.collection.type === 'tbody' && this.pel) {
                this.page();
            }
            this.collection.each(this.addOne, this);
            this.afterLoad();
        },
        create: function (models) { // param can use model or model array
            this.append(models);
        },
        append: function (models) {
            if (_.isUndefined(models)) {
                var model = new this.collection.model;
                this.collection.add(model);
                model._edit();
                return model;
            } else {
                this.collection.add(models);
                return models;
            }
        },
        afterLoad: function () {

        },
        remove: function () {
            var cl = this.collection;
            var ids = [];
            cl.each(function (model) {
                if (model.selected) {
                    if (model.isNew()) {
                        cl.remove(model);
                        cl.focus = null;
                    } else {
                        model.set(model.previousAttributes());
                        model.set({'status': App.Constant.status.trash});
                        model.isRemoved = true;
                        ids.push(model.id);
                    }

                    if (cl.type === 'tree') {
                        $.each(cl.where({'parent': model.id}), function (i, e) {
                            ids.push(model.id);
                            e.row.remove();
                            e.set({'status': App.Constant.status.trash});
                        });
                    }
                    model.row.remove();
                }
            }, this);

            if (ids.length === 0) {
                $.sticky('请先选择要删除的记录!');
                return;
            }

            if (this.form) this.form.hide();
            if (this.afterRemove) this.afterRemove();
            return ids;
        },
        remoteRemove: function () {
            if (confirm('确定要删除吗?')) {
                var ids = this.remove();
                var cl = this.collection;
                if (ids) {
                    $.ajax({
                        contentType: 'application/json',
                        dataType: 'json',
                        data: JSON.stringify(ids),
                        type: 'DELETE',
                        precessData: false,
                        url: cl.urlRoot,
                        success: function () {
                            cl.load();
                        }
                    });
                }
            }
        },
        empty: function () {
            if (this.collection.type === 'tree' || this.collection.type === 'tbody')
                this.$('tbody').empty();
            else
                this.$(this.collection.type).empty();
        },
        search: function () { // 查询列表
            this.collection.currentPage = 0;
            if (this.querybar) {
                _.extend(this.collection.param, this.querybar.serializeObject());
            }
            if (this.collection.type === 'tree') {
                this.collection.param['parent'] = 0;
            }
            this.collection.load();
        },
        serialize: function () {
            this.collection.each(function (model) {
                if (model.row) {
                    model.row.serialize();
                }
            }, this);
        },
        reset: function () { // 查询列表
            if (this.querybar) {
                this.querybar.reset();
            }
        }
    });

});
