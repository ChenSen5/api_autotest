package com.sen.api.functions;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Function implements Function{

	@Override
	public String execute(String[] args) {
		try {
			String filePath = args[0];
			if (filePath.startsWith("http")) {
				return DigestUtils.md5Hex(new URL(filePath).openStream());
			} else {
				return DigestUtils.md5Hex(new FileInputStream(new File(
						filePath)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getReferenceKey() {
		// TODO Auto-generated method stub
		return "md5";
	}

}
