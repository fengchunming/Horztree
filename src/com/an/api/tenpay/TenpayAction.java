package com.an.api.tenpay;

import com.an.api.tenpay.util.QRCode;
import com.an.api.tenpay.util.Util;
import com.an.api.tenpay.util.WxPayHelper;
import com.an.api.tenpay.util.XMLHelper;
import com.an.core.exception.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.xml.sax.SAXException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

/**
 * 微信支付 Created by karas on 3/10/15.
 */
@Controller
@RequestMapping("/ten/pay")
public class TenpayAction {
	@RequestMapping(value = "/jspack", method = RequestMethod.GET)
	@ResponseBody
	public String query(WebRequest request) throws BadRequestException {
		WxPayHelper wxPayHelper = new WxPayHelper();
		// 先设置基本信息
		// 设置请求package信息
		wxPayHelper.setParameter("bank_type", "WX");
		wxPayHelper.setParameter("body", "test");
		wxPayHelper.setParameter("partner", "1900000109");
		wxPayHelper.setParameter("out_trade_no", Util.CreateNoncestr());
		wxPayHelper.setParameter("total_fee", "1");
		wxPayHelper.setParameter("fee_type", "1");
		wxPayHelper.setParameter("notify_url", "htttp://www.baidu.com");
		wxPayHelper.setParameter("spbill_create_ip", "127.0.0.1");
		wxPayHelper.setParameter("input_charset", "UTF-8");

		return wxPayHelper.CreateBizPackage();
	}

	@RequestMapping(value = "/qrcode", method = RequestMethod.GET)
	public void qrcode(HttpServletRequest req, HttpServletResponse res)
			throws BadRequestException, IOException,
			ParserConfigurationException, SAXException {

		ServletOutputStream out = res.getOutputStream();
		WxPayHelper wxPayHelper = new WxPayHelper();

		// TODO: ADD PARAMETER HRERE
		String xml = wxPayHelper.Unifiedorder("device_info", "out_trade_no", "body", "1", "product_id", WxPayHelper.TRADE_TP.NATIVE);
		Map<String, String> rst = XMLHelper.getMapFromXML(xml);
		QRCode qrcode = new QRCode();
		qrcode.generate(rst.get("code_url"), out);
		out.flush();
	}

	public static void main(String args[]) {
		try {
			WxPayHelper wxPayHelper = new WxPayHelper();
			String xml = wxPayHelper.Unifiedorder("device_info", "out_trade_no", "body", "1", "product_id", WxPayHelper.TRADE_TP.NATIVE);
			Map<String, String> rst = XMLHelper.getMapFromXML(xml);
			System.out.println(xml);
			String filePath = "/Users/karas/Desktop/CrunchifyQR.png";
			QRCode qrcode = new QRCode();
			qrcode.generate(rst.get("code_url"), filePath);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
