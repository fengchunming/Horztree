/**
 * Created by karas on 5/31/14.
 */

(function ($) {
    var config = {
        "goods": {
            id: 'mid',
            title: '选择商品',
            url: 'rest/mm/goods/list',
            columns: [
                {key: 'selected', type: 'checkbox', style: 'width:25px'},
                {label: '商品名', key: 'goodsName'}
            ]
        },
        "hotel": {
            id: 'hotelName',
            url: 'rest/mm/hotelList',
            columns: [
                {key: 'selected', type: 'checkbox', style: 'width:25px'},
                {local: '', key: 'hotelName'}
            ]
        }
    };

    var methods = {
        init: function (opts) {
            var $this = $(this);
            var setting = $this.data('multipleList');
            if (typeof(setting) === 'undefined') {
                setting = $.extend({}, opts, config[opts.name]);
                var container = $('#multiple' + setting.name);
                if (!container.length) {
                    container = $('<div style="display:none" id="multiple' + setting.name + '" class="multipleList"><table class="app"></table><div class="page"></div></div>');
                    container.dialog({
                        autoOpen: false,
                        modal: true,
                        title: setting.title,
                        minWidth: 800,
                        height: 500
                    });
                    container.Model = App.Model.extend({
                        idAttribute: setting.id
                    });
                    container.Collection = App.Collection.extend({
                        param: setting.param,
                        urlRoot: setting.url,
                        pageSize: 15,
                        model: container.Model,
                        autoLoad: false,
                        columns: setting.columns
                    });
                    container.Row = App.Row.extend({
                        change: function () {
                            var val = $this.val();
                            if (!this.model.get('selected')) {
                                $this.val(val.replace(this.model.id, '').replace(/^\|*|\|*$/g, "").replace('||', '|')).change();
                            } else {
                                $this.val((val ? val + '|' : '') + this.model.id).change();
                            }
                        }
                    });
                    container.Grid = App.Grid.extend({
                        editable: true,
                        withHead: false,
                        el: '#multiple' + setting.name,
                        collection: new container.Collection,
                        //rowView: container.Row,
                        toolbar: []
                    });
                    container.grid = new container.Grid();
                }
                setting.container = container;
            }

            $this.data('multipleList', setting);
        },
        destroy: function () {
            $(this).removeData('multipleList');
        },
        show: function (data) {
            var setting = $(this).data('multipleList');
            setting.container.grid.collection.load();
            setting.container.dialog('open');
        },
        callback: function (opts) {

        }
    };

    // Extend jQuery
    $.fn.multipleList = function (options) {
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
