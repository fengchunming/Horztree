$(function () {

    if (!$('#footer').find('#locationPlugin').length) {
        $('<div id="locationPlugin"></div>')
            .appendTo('#footer')
            .load('wm/widget/location.html', function () {

                App.CardPluginRow = App.Row.extend({
                    dblclick: function () {
                        App.CardPluginGrid.model.set('cardNo', this.model.get('cardNo'));
                        $('#cardlookup').dialog('close');
                    }
                });

                App.CardPluginModel = App.Model.extend({
                    rowView: App.CardPluginRow
                });

                App.CardPluginList = App.Collection.extend({
                    urlRoot: 'rest/wm/locationList',
                    model: App.CardPluginModel,
                    autoLoad: false,
                    columns: [
                        {label: '卡号', key: 'cardNo'},
                        {label: '件数', key: 'quantity'},
                        {label: '总支数', key: 'totalQuantity'},
                        {label: '重量', key: 'weight'}
                    ]
                });

                App.cardPluginList = new App.CardPluginList();

                App.CardPluginGrid = App.Grid.extend({
                    collection: App.cardPluginList,
                    el: "#cardlookup",
                    toolbar: [],
                    querybar: '#cardlookup .queryBar'
                });
                App.cardPluginGrid = new App.CardPluginGrid();

                $('#locationPlugin').dialog({
                    autoOpen: false,
                    modal: false,
                    width: 400,
                    buttons: {
                        "新建卡号": function () {
                            $.ajax({
                                url: 'rest/wm/newCard/' + App.cardPluginList.param["goodsId"]
                                    + '/' + App.cardPluginList.param["warehouseId"],
                                success: function (data) {
                                    App.CardPluginGrid.model.set('cardNo', data.cardNo);
                                }
                            });
                            $(this).dialog("close");
                        },
                        "取消": function () {
                            $(this).dialog("close");
                        }
                    }
                }).dialog("widget").find(".ui-dialog-titlebar").hide();
            });
    }

    App.Plugin.location = function (ui, model) {
        App.cardPluginList.reset();
        App.cardPluginList.load();
        App.CardPluginGrid.model = model;

        ui.attr('readonly', true);

        $('#locationPlugin').dialog({
            position: {of: ui, my: 'left top', at: 'left bottom'}
        }).dialog('open');
    };

});