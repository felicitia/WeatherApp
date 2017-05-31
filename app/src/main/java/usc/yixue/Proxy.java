package usc.yixue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * stub methods for instrumentation, then Xposed will rewrite the actual method
 * 
 * @author felicitia
 *
 */
public class Proxy {

	public static InputStream getInputStream(URLConnection urlconn) throws IOException {
		System.out.println("new getInputStream :)");
		return null;
	}

	public static Object getContent(URLConnection urlconn) throws IOException {
		System.out.println("new getContent :)");
		return null;
	}
	
	public static InputStream openStream(URL url) throws IOException{
		System.out.println("new openStream :)");
		return null;
	}

	public static void sendDef(String value, Integer nodeId, Integer index,
			String pkgName) {
		System.out.println("Proxy: sendDef :)");
		System.out.println("args = "+ value+"\t"+nodeId+"\t"+index+"\t"+pkgName);
	}

	public static String getResult(String urlStr) {
		System.out.println("Proxy: getResult :)");
		return null;
	}

	public static void printTimeStamp(){
		Long time = System.currentTimeMillis(); 
		System.out.println(time.toString());
	}
}
