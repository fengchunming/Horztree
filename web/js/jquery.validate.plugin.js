$(function () {
    $.validator.setDefaults({
        showErrors: function (map, list) {
            this.currentElements.removeAttr("title").removeClass("error");
            $.each(list, function (index, error) {
                $(error.element).attr("title", error.message).addClass("error");
            });
            this.currentElements.tooltip();
        }
    });

    $.extend($.validator.messages, {
        required: "必须填写",
        remote: "请修正此栏位",
        email: "请输入有效的电子邮件",
        url: "请输入有效的网址",
        date: "请输入有效的日期",
        dateISO: "请输入有效的日期 (YYYY-MM-DD)",
        number: "请输入正确的数字",
        digits: "只可输入数字",
        creditcard: "请输入有效的信用卡号码",
        equalTo: "你的输入不相同",
        extension: "请输入有效的后缀",
        maxlength: $.validator.format("最多 {0} 个字"),
        minlength: $.validator.format("最少 {0} 个字"),
        rangelength: $.validator.format("请输入长度为 {0} 至 {1} 之間的字串"),
        range: $.validator.format("请输入 {0} 至 {1} 之间的数值"),
        max: $.validator.format("请输入不大于 {0} 的数值"),
        min: $.validator.format("请输入不小于 {0} 的数值")
    });

    $.validator.addMethod("alphanumeric", function (value, element) {
        return this.optional(element) || /^\w+$/i.test(value);
    }, "Letters, numbers, and underscores only please");

    $.validator.addMethod("phone", function (value, element) {
        return this.optional(element) || /^[\d-\*]+$/i.test(value);
    }, "电话号码格式错误!!");

    $.validator.addMethod("fax", function (value, element) {
        return this.optional(element) || /^((\+?[0-9]{2,4}\-[0-9]{3,4}\-)|([0-9]{3,4}\-))?([0-9]{7,8})(\-[0-9]+)?$/i.test(value);
    }, "传真格式错误!!");

    $.validator.addMethod("idcard", function (value, element) {
        return this.optional(element) || /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/i.test(value);
    }, "身份证号码格式错误!!");

    $.validator.addMethod("mobile", function (value, element) {
        return this.optional(element) || /^1[3|4|5|7|8]\d{9}$/i.test(value);
    }, "手机号码格式错误!!");

    $.validator.addMethod("email", function (value, element) {
        return this.optional(element) || /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/i.test(value);
    }, "邮箱格式错误!!");

    $.validator.addMethod("largeThan", function (value, element, param) {
        var target = $(param);
        if (this.settings.onfocusout) {
            target.unbind(".validate-largeThan").bind("blur.validate-largeThan", function () {
                $(element).valid();
            });
        }
        return value > target.val();
    }, "应大于前一值!!");
});