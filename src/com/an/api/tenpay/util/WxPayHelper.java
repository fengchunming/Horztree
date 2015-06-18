package com.an.api.tenpay.util;

import com.an.utils.Digest;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;

public class WxPayHelper {
	public enum TRADE_TP {
		NATIVE, JSAPI, APP
	}

	private HashMap<String, String> parameters = new HashMap<>();

	public void setParameter(String key, String value) {
		parameters.put(key, value);
	}

	public String getParameter(String key) {
		return parameters.get(key);
	}

	// 生成jsapi支付请求json
	/*
	 * "appId" : "wxf8b4f85f3a794e77", //公众号名称，由商户传入 "timeStamp" : "189026618",
	 * //时间戳这里随意使用了一个值 "nonceStr" : "adssdasssd13d", //随机串 "package" :
	 * "bank_type=WX&body=XXX&fee_type=1&input_charset=GBK&notify_url=http%3a%2f
	 * %2fwww.qq.com&out_trade_no=16642817866003386000&partner=1900000109&
	 * spbill_create_i
	 * p=127.0.0.1&total_fee=1&sign=BEEF37AD19575D92E191C1E4B1474CA9",
	 * //扩展字段，由商户传入 "signType" : "SHA1", //微信签名方式:sha1 "paySign" :
	 * "7717231c335a05165b1874658306fa431fe9a0de" //微信签名
	 */
	public String CreateBizPackage() {
		HashMap<String, String> nativeObj = new HashMap<>();
		nativeObj.put("appId", Config.getAppid());
		nativeObj.put("package", Signature.getSignParam(parameters));
		nativeObj.put("timestamp", Long.toString(new Date().getTime() / 1000));
		nativeObj.put("noncestr", Util.CreateNoncestr());
		nativeObj.put("paySign", Signature.getSign(nativeObj));
		nativeObj.put("signType", "SHA1");
		return JSONObject.fromObject(nativeObj).toString();

	}

	// 生成原生支付url
	/*
	 * weixin://wxpay/bizpayurl?sign=XXXXX&appid=XXXXX&mch_id=XXXXX&product_id=
	 * XXXXXX&time_stamp=XXXXXX&nonce_str=XXXXX
	 */
	public String CreateNativeUrl(String productid) {
		HashMap<String, String> nativeObj = new HashMap<>();
		nativeObj.put("appid", Config.getAppid());
		nativeObj.put("product_id", productid);// URLEncoder.encode(productid,
												// "utf-8"));
		nativeObj.put("time_stamp", Long.toString(new Date().getTime() / 1000));
		nativeObj.put("nonce_str", Digest.MD5(Util.CreateNoncestr(), Digest.Cipher.HEX));
		nativeObj.put("mch_id", Config.getMchid());
		String bizString = Signature.getSignParam(nativeObj);
		return "weixin://wxpay/bizpayurl?" + bizString;
	}

	// 生成原生支付请求xml
	/*
	 * <xml> <AppId><![CDATA[wwwwb4f85f3a797777]]></AppId>
	 * <Package><![CDATA[a=1&url=http%3A%2F%2Fwww.qq.com]]></Package>
	 * <TimeStamp> 1369745073</TimeStamp>
	 * <NonceStr><![CDATA[iuytxA0cH6PyTAVISB28]]></NonceStr>
	 * <RetCode>0</RetCode> <RetErrMsg><![CDATA[ok]]></ RetErrMsg>
	 * <AppSignature><![CDATA[53cca9d47b883bd4a5c85a9300df3da0cb48565c]]>
	 * </AppSignature> <SignMethod><![CDATA[sha1]]></ SignMethod > </xml>
	 */
	// public String CreateNativePackage(String retcode, String reterrmsg)
	// throws SDKRuntimeException {
	// HashMap<String, String> nativeObj = new HashMap<>();
	// if (!CheckCftParameters() && "0".equals(retcode)) {
	// throw new SDKRuntimeException("生成package参数缺失！");
	// }
	// nativeObj.put("AppId", Config.getAppid());
	// nativeObj.put("Package", GetCftPackage());
	// nativeObj.put("TimeStamp", Long.toString(new Date().getTime() / 1000));
	// nativeObj.put("RetCode", retcode);
	// nativeObj.put("RetErrMsg", reterrmsg);
	// nativeObj.put("NonceStr", Util.CreateNoncestr());
	// nativeObj.put("AppSignature", GetBizSign(nativeObj));
	// nativeObj.put("SignMethod", "SHA1");
	// return XMLHelper.getXMLFromMap(nativeObj);
	//
	// }

	public String Unifiedorder(String device_info, String out_trade_no,
			String body, String total_fee, String product_id, TRADE_TP type) {

		HashMap<String, String> nativeObj = new HashMap<>();
		nativeObj.put("appid", Config.getAppid());
		nativeObj.put("mch_id", Config.getMchid());
		nativeObj.put("device_info", device_info);// URLEncoder.encode(productid,
													// "utf-8"));
		nativeObj.put("nonce_str", Util.CreateNoncestr());//
		nativeObj.put("time_stamp", Long.toString(new Date().getTime() / 1000));//
		nativeObj.put("body", body);
		nativeObj.put("out_trade_no", out_trade_no);// Digest.MD5("1211",
													// Digest.Cipher.HEX)
		nativeObj.put("total_fee", total_fee);
		nativeObj.put("spbill_create_ip", Config.getIP());
		nativeObj.put("notify_url", Config.CALL_PAY);
		nativeObj.put("trade_type", type.name());
		nativeObj.put("product_id", product_id);

		nativeObj.put("sign", Signature.getSign(nativeObj));
		String bizString = XMLHelper.getXMLFromMap(nativeObj);
		try {
			HttpsURLConnection conn = HttpClientUtil.getHttpsURLConnection(Config.UNIFIE_ORDER_API);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			conn.setRequestMethod("POST");

			OutputStream out = conn.getOutputStream();
			out.write(bizString.getBytes());
			InputStream in = conn.getInputStream();
			String result = IOUtils.toString(in);
			in.close();
			return result;
		} catch (IOException e) {
			Util.log(e.getMessage());
		}
		System.out.println(bizString);
		return null;
	}
}
