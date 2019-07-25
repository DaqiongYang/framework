package com.qzl.util;
import java.util.UUID;

/**
 * @version 1.0.0
 * @author <a href=" ">hanjp</a>
 */
public class GuidCreateHelper {
	
	public static String getGuid() {
		  UUID uuid = UUID.randomUUID();
		  String guid = uuid.toString().replace("-", "");
		  return guid;
	}
}
