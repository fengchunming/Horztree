//var PST = new Array('未匹配' , '部分匹配' , '已匹配' , '作废记录');

var _LocalKV = {
    "conf": {
        "domainName": 'MIS'
    },
    "status": {
        "t": "启用",
        "f": "停用"
    },
    "priority": {
        '50': '普通',
        '70': '紧急',
        '90': '最紧急'
    },
    "http_method": {
        "GET": "GET",
        "POST": "POST",
        "PUT": "PUT",
        "DELETE": "DELETE"
    },
    "tradeStatus": {
        "0": "待支付",
        "1": "待发货",
        "3": "已发货",
        "4": "已完结",
        "5": "已取消"
    },
    "billStatus": {
        "0": "新建",
        "1": "已确认",
        //"2": "挂起",
        "d": "已取消"
    },
    "tradeType": {
        'XS': '网站',
        'WX': '微信',
        'OF': '线下'
    },
    "printStatus": {
        "0": "",
        "1": "运",
        "2": "配",
        "3": "配 运"
    },
    "issueStatus": {
        "none": "",
        "waiting": "待配货",
        "working": "配货中",
        "stop": "已停止",
        "resolved": "已完成",
        "cancel": "已取消"
    },
    "receiptStatus": {
        "none": "",
        "waiting": "待收货",
        "working": "收货中",
        "stop": "已停止",
        "resolved": "已完成",
        "cancel": "已取消"
    },
    'shipment': {
        self: "门口头",
        zto: "中通快递",
        yto: "圆通快递",
        sf: "顺风快递",
        ems: "EMS"
    },
    "billType": {
        "BH": "门店配货单",
        "CS": "初始化",
        "PD": "盘点单",
        "SH": "收货单",
        "SC": "试吃单",
        "FH": "发货单",
        "XS": "销售单",
        "XT": "销退单",
        "SJ": "上架单",
        "PO": "采购单",
        "WD": "库内物动单",
        "DR": "调拨入库单",
        "DC": "调拨出库单",
        "KT": "退货单",
        "FQ": "废弃单",
        "FJ": "拣选单"
    },
    "orgType": {
        "warehouse": "仓库",
        "store": "门店",
        "supplier": "供应商",
        "ecstore": "网店",
        "virtual": "管理组织"
    },
    "storageType": {
        "001": "小货架",
        "002": "托盘货架",
        "003": "平面场地"
    },
    "abcType": {
        "1": "A",
        "2": "B",
        "3": "C"
    },
    "usageType": {
        "001": "收货暂存",
        "002": "发货暂存",
        "003": "备货区",
        "004": "拣选区"
    },
    "locationUseStatus": {
        "empty": "空闲",
        "full": "已满",
        "use": "使用中"
    },
    "purchasePayment": {
        "before": "先付",
        "receipt": "到付",
        "after": "账期"
    },
    "terminalType": {
        "RM60": "RM60",
        "RM5800": "RM5800"
    },
    "apply": {
        "enabled": "允许",
        "disabled": "不允许"
    },
    "taxType": {
        "0": "免税",
        "1": "内税",
        "2": "外税"
    },
    "goodsType": {
        "1": "普通商品",
        "2": "虚拟商品"
    },
    "currency": {
        "CNY": "人民币",
        "EUR": "欧元",
        "JPY": "日元",
        "USD": "美元"
    },
    "invoiceType": {
        "0": "普通发票",
        "1": "增值税发票"
    },
    "gender": {
        "0": "女",
        "1": "男"
    },
    "yn": {
        "true": "是",
        "false": "否"
    },
    "cashierType": {
        "3": "营业员",
        "0": "超级用户",
        "1": "管理用户",
        "2": "设置用户"
    },
    "productType": {
        "product": "商品",
        "gift": "礼品"
    },
    "receiptType": {
        "0": "计划入库",
        "1": "调拨入库",
        "2": "临时补仓",
        "3": "退货入库"
    },
    "refunedType": {
        "0": "退至网站余额",
        "1": "退至会员卡",
        "3": "按支付方式退回"
    },
    "refunedState": {
        "0": "申请中",
        "1": "已退款",
        "3": "审核未通过",
        "4": "退款失败",
        "5": "退款取消"
    },
    "supportType": {
        "0": "导购咨询",
        "1": "订单售后",
        "2": "服务投诉",
        "3": "产品质量",
        "4": "系统操作"
    },
    "orient": {
        "0": "方向不定",
        "1": "纵向打印，固定纸张",
        "2": "横向打印，固定纸张",
        "3": "纵向打印，宽度固定"
    },
    "pager": {
        "X": "手工指定",
        "A4": "A4",
        "A5": "A5"
    }
};

function tryLoad(local) {
    if (_LocalKV[local])
        return;
    _LocalKV[local] = {};
    reloadLocal(local);
}

function clearLocal(key) {
    delete _LocalKV[key];
}

function reloadLocal(local) {
    var url = Request.QueryDomain() + 'rest/' + local;
    if (url.indexOf('?') > 0) {
        url += '&_=' + new Date().getTime();
    } else {
        url += '?_=' + new Date().getTime();
    }
    $.ajax({
        url: url,
        dataType: 'json',
        async: false,
        success: function (data) {
            if (!_.isArray(data)) data = data.mapList;
            $.each(data, function (i, item) {
                if (item.step) item.v = new Array(item.step).join('&nbsp;&nbsp;') + item.v;
                _LocalKV[local][item.k] = item.v;
            });
        }
    });
}

function convertLocal() {
    $('td[data-local],li[data-local],span[data-local]').each(function () {
        var e = $(this);
        var d = e.attr('data-local');
        tryLoad(d);
        e.removeAttr('data-local');
        if (_LocalKV[d][e.html()])
            e.html(_LocalKV[d][e.html()]);
        else {
            e.html($.map(e.html().split(','), function (item, i) {
                return _LocalKV[d][item];
            }).join());
        }
    });

    $('select[data-local]').each(function () {
        var e = $(this);
        e.append(mkOptions(e.attr('data-local'), e.attr('data-value')));
        e.removeAttr('data-local');
    });
}
$.fn.makeSelect = function (url, v) {
    $(this).html(mkOptions(url, v));
}

$.fn.clearSelect = function () {
    $(this).empty();
}
function mkOptions(d, v) {
    tryLoad(d);
    return $.map(_LocalKV[d], function (i, key) {
        return '<option value="' + key + '" ' + (v == key ? 'selected' : '') + '>'
            + _LocalKV[d][key] + '</option>';
    }).join('');
}

function kToV(d, k) {
    tryLoad(d);
    return _LocalKV[d][k];
}
