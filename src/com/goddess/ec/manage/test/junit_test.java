package com.goddess.ec.manage.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.goddess.ec.manage.tools.ToolSqlXml;

public class junit_test {
	@Test
	public void readJson() throws MalformedURLException {
//		URL url = ToolSqlXml.class.getResource("/scene_tree.json");
		URL url = new URL("file:/D:/scene_tree.json");
		System.out.println(url);
		File file = FileUtils.toFile(url);
		   
		try {
			String sceneTreeJson = FileUtils.readFileToString(file);
			System.out.println(sceneTreeJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
