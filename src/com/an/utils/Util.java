package com.an.utils;

import com.an.core.exception.BadRequestException;
import org.springframework.web.context.request.WebRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Util {

	private static int sequence = 10000;

	public static SimpleDateFormat StampFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static SimpleDateFormat DateFmt = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat DateTimeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat DateZipFmt = new SimpleDateFormat("yyMMdd");
	public static SimpleDateFormat TimeZipFmt = new SimpleDateFormat("HHmm");
	public static SimpleDateFormat StampZipFmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public static SimpleDateFormat TimeFmt = new SimpleDateFormat("HH:mm");
	static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|%|&|" + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

	static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

	/**
	 * 字符串空值转换为默认值
	 *
	 * @param ori
	 *            Object 原始字符串
	 * @param def
	 *            String 默认字符串
	 * @return String 转换结果
	 */
	public static String nvl(Object ori, String def) {
		if (ori == null) {
			return def;
		} else {
			return ori.toString();
		}
	}

	/**
	 * 对象不为空且不为空串
	 *
	 * @param ori
	 * @return
	 */
	public static boolean isNotEmpty(Object ori) {
		return ori != null && !ori.toString().isEmpty();
	}

	/**
	 * 字符串是否为数字
	 *
	 * @param ori
	 *            String 原始字符串
	 * @return boolean 判断结果
	 */
	public static boolean isNum(String ori) {
		try {
			Double temp = Double.parseDouble(ori);
			return temp > 0;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * 取当前时间
	 *
	 * @return 格式： yyyyMMddHHmmss + 序列号
	 */
	public static String RunTimeSequence() {
		return Util.CurrentTime("yyMMddhhmmss") + (sequence++);
	}

	/**
	 * 取当前时间
	 *
	 * @param format
	 *            格式示例： yyyyMMddHHmmss
	 * @return
	 */
	public static String CurrentTime(String format) {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat(format);
		return outFormat.format(now);
	}
	
	public static String formatDate(Date date, String format) {
		SimpleDateFormat outFormat = new SimpleDateFormat(format);
		return outFormat.format(date);
	}

	/**
	 * 取Unix时间
	 *
	 * @param date
	 * @return
	 */
	public static long getUTCOffset(Date date) {
		if (null == date) {
			return 0;
		}
		return date.getTime() / 1000;
	}

	public static long getTimeOffset(String format, Date d) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		DateFormat df = DateFormat.getDateInstance();
		String date = df.format(d);
		Date date1 = sdf.parse(date);
		return getUTCOffset(date1);
	}

	/**
	 * 生成随机数
	 *
	 * @param length
	 *            int 随机数长度
	 * @return int 随机数
	 */
	public static int getRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

	/**
	 * 过滤HTML代码
	 *
	 * @param input
	 *            String HTML字符串
	 * @return String 过滤后结果
	 */
	public static String filterHtml(String input) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		return input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<([^>]*)>", "");
	}

	/**
	 * 将类似userName转换成user_name形式
	 *
	 * @param s
	 *            CameCase字符串
	 * @return 转换后的字符串
	 */
	public static String CamelCaseToDb(String s) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isUpperCase(c) && i != 0) {
				builder.append("_");
			}
			builder.append(Character.toLowerCase(c));
		}
		return builder.toString();
	}

	/**
	 * 组织request参数
	 *
	 * @param request
	 *            查询条件
	 * @return String 过滤后结果
	 */
	public static Map<String, Object> GetRequestMap(WebRequest request) {
		Map<String, Object> mParam = new HashMap<>();
		for (Iterator<String> it = request.getParameterNames(); it.hasNext();) {
			String key = it.next();
			if (key.equals("_BY")) {
				if (sqlPattern.matcher(request.getParameter(key)).find())
					throw new BadRequestException("Contain Invalid Char in " + request.getParameter(key));
				mParam.put(key, CamelCaseToDb(request.getParameter(key)).replaceAll("\\.", "_"));
			} else if (request.getParameterValues(key).length > 1) {
				mParam.put(key, request.getParameterValues(key));
			} else {
				mParam.put(key, request.getParameter(key));
			}
		}
		return mParam;
	}

	public static String encodeAscii(String str) {
		StringBuilder sbu = new StringBuilder();
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (i != chars.length - 1) {
				sbu.append((int) chars[i]).append(" ");
			} else {
				sbu.append((int) chars[i]);
			}
		}
		return sbu.toString();
	}

	public static String decodeAscii(String code) {
		StringBuilder sbu = new StringBuilder();
		String[] chars = code.split(",");
		for (String s : chars) {
			sbu.append((char) Integer.parseInt(s));
		}
		return sbu.toString();
	}

	/**
	 * 字符串的压缩
	 *
	 * @param str
	 *            待压缩的字符串
	 * @return 返回压缩后的字符串
	 * @throws IOException
	 */
	public static String compress(String str) throws IOException {
		if (null == str || str.length() <= 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		return out.toString("ISO-8859-1");
	}

	/**
	 * 字符串的解压
	 *
	 * @param str
	 *            对字符串解压
	 * @return 返回解压缩后的字符串
	 * @throws IOException
	 */
	public static String decompress(String str) throws IOException {
		if (null == str || str.length() <= 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
		GZIPInputStream gzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		return out.toString("UTF-8");
	}
}