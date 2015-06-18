package com.an.api.tenpay.util;

import com.thoughtworks.xstream.XStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: rizenguo Date: 2014/11/1 Time: 14:06
 */
public class XMLHelper {

	public static String getXMLFromMap(Map<String, String> map) {
		List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());
		String xml = "<xml>";
		for (Map.Entry<String, String> entry : infoIds) {
			String key = entry.getKey();
			String val = entry.getValue();
			if (val.matches("\\d *")) {
				xml += "<" + key + ">" + val + "</" + key + ">";
			} else {
				xml += "<" + key + "><![CDATA[" + val + "]]></" + key + ">";
			}
		}
		xml += "</xml>";
		return xml;
	}

	public static Object getObjectFromXML(String xml, Class<?> tClass) {
		// 将从API返回的XML数据映射到Java对象
		XStream xStreamForResponseData = new XStream();
		xStreamForResponseData.alias("xml", tClass);
		xStreamForResponseData.ignoreUnknownElements();// 暂时忽略掉一些新增的字段
		return xStreamForResponseData.fromXML(xml);
	}

	public static Map<String, String> getMapFromXML(String xmlString)
			throws ParserConfigurationException, IOException, SAXException {

		// 这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream is = Util.getStringStream(xmlString);
		Document document = builder.parse(is);

		// 获取到document里面的全部结点
		NodeList allNodes = document.getFirstChild().getChildNodes();
		Node node;
		Map<String, String> map = new HashMap<>();
		int i = 0;
		while (i < allNodes.getLength()) {
			node = allNodes.item(i);
			if (node instanceof Element) {
				map.put(node.getNodeName(), node.getTextContent());
			}
			i++;
		}
		return map;

	}

}
