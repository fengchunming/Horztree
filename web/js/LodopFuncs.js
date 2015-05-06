function getLodop(oOBJECT, oEMBED) {
	/***************************************************************************
	 * 本函数根据浏览器类型决定采用哪个对象作为控件实例： IE系列、IE内核系列的浏览器采用oOBJECT，
	 * 其它浏览器(Firefox系列、Chrome系列、Opera系列、Safari系列等)采用oEMBED,
	 * 对于64位浏览器指向64位的安装程序install_lodop64.exe。
	 **************************************************************************/
	var strHtmInstall = "打印控件未安装!点击这里<a href='resource/install_lodop32.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。";
	var strHtmUpdate = "打印控件需要升级!点击这里<a href='resource/install_lodop32.exe' target='_self'>执行升级</a>,升级后请重新进入。";
	var strHtm64_Install = "打印控件未安装!点击这里<a href='resource/install_lodop64.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。";
	var strHtm64_Update = "打印控件需要升级!点击这里<a href='resource/install_lodop64.exe' target='_self'>执行升级</a>,升级后请重新进入。";
	var strHtmFireFox = "注意：<br>1：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它。";
	var LODOP = oEMBED;
	try {
		if (navigator.appVersion.indexOf("MSIE") >= 0)
			LODOP = oOBJECT;
		if (navigator.platform != "Win32" && navigator.platform != "Win64"
				&& navigator.platform != "Windows") {
			return LODOP;
		}
		if ((LODOP == null) || (typeof (LODOP.VERSION) == "undefined")) {
			if (navigator.userAgent.indexOf('Firefox') >= 0)
				$.sticky(strHtmFireFox);
			if (navigator.userAgent.indexOf('Win64') >= 0) {
				$.sticky(strHtm64_Install);
			} else {
				$.sticky(strHtmInstall);
			}
			return LODOP;
		} else if (LODOP.VERSION < "6.1.4.5") {
			if (navigator.userAgent.indexOf('Win64') >= 0) {
				$.sticky(strHtm64_Update);
			} else {
				$.sticky(strHtmUpdate);
			}
			return LODOP;
		}
		// =====如下空白位置适合调用统一功能:=====

		// =======================================
		return LODOP;
	} catch (err) {
		if (navigator.userAgent.indexOf('Win64') >= 0) {
			$.sticky(strHtm64_Install);
		} else {
			$.sticky(strHtmInstall);
		}
		return LODOP;
	}
}

var LODOP = getLodop(document.getElementById('LODOP_OB'), document
		.getElementById('LODOP_EM'));
