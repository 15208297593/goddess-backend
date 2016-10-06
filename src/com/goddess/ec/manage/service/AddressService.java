package com.goddess.ec.manage.service;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.goddess.ec.manage.tools.ToolSqlXml;

public class AddressService extends BaseService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(AddressService.class);

    public static final AddressService service = new AddressService();

	public static Map<String, Object> addrTree = new LinkedHashMap<String, Object>();
	
	static {

		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(ToolSqlXml.class.getClassLoader().getResourceAsStream("province_data.xml"));
			Element root = doc.getRootElement();
			parseNode(root, addrTree);
		} catch (DocumentException e) {
			log.error("地址文件解析异常");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static void parseNode(Element node, Map<String, Object> parent) {

		parent.put(node.attributeValue("name"), node.attributeValue("name"));
		int nodeCount = node.nodeCount();
		if (nodeCount == 0) return;
		
		Map<String, Object> children = new LinkedHashMap<String, Object>();
		parent.put(node.attributeValue("name"), children);
		for (Iterator<Element> iterTemp = node.elementIterator(); iterTemp.hasNext();) {
			Element element = (Element) iterTemp.next();
			parseNode(element, children);
		}
	}
	
	public static void main(String[] args) {
		Map<String, Object> m = new LinkedHashMap<String, Object>();
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(ToolSqlXml.class.getClassLoader().getResourceAsStream("province_data.xml"));
			Element root = doc.getRootElement();
			parseNode(root, m);
		} catch (DocumentException e) {
			log.error("地址文件解析异常");
			e.printStackTrace();
		}
		System.out.println(m);
	}
	
}
