package pay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


public class Main {

	
	public static void main(String[] args) throws Exception {
		Main main = new Main();
		
		main.init();
	}
	
	private void init() throws Exception {
		//System.out.println(Arrays.toString(cline("`mebid` varchar(32) NOT NULL DEFAULT '',")));

		
		String str = readInfoStream(Main.class.getClassLoader().getResourceAsStream("str"));
		
		String[] ls = str.split("\n");
		
		int n= 0;
		
		for (String string : ls) {
			if(string==null||"".equals(string.trim()))
				continue;
			String s = string.trim();
			
			String[] arr = cline(s);
			
			outJava(arr);
		}
		System.out.println(n);
			
		
	}
	
	
	private void ouExcele(String[] arr) {

		String out1 = arr[0];
		String out2 = arr[1];
		String out3 = arr[2];
		String out4 = arr[3];
		System.out.println(
				(out1==null?" ":out1)+"\t"+
				(out2==null?" ":out2)+"\t"+
				(out3==null?" ":out3)+"\t"+
				(out4==null?" ":out4));
	


	}
	
	
	private void outJava(String[] arr) {

		String out1 = arr[0];
		String out2 = arr[1];
		out1 = out1.replace("`", "");
		String out3 = arr[2];
		String out4 = arr[3];
		System.out.println(
				"String "+
				(out1==null?" ":out1)+";//"+
				(out2==null?" ":out2)+"\t"+
				(out4==null?" ":out4));


	}
	
	
	
	
	private String[] cline(String str) {
		
		String[] ss = new String[4];
		
		int i1 = str.indexOf("`");
		int i2 = str.indexOf("`", i1+1);
		
		ss[0] = str.substring(i1, i2+1);
		
		str = str.substring(i2, str.length());
		
		
		i1 = str.indexOf(" ");
		i2 = str.indexOf(" ", i1+1);
		ss[1] = str.substring(i1, i2+1).trim();
		str = str.substring(i2, str.length());
		
		
		if(str.indexOf("COMMENT")>=0){
		i1 = 0;
		i2 = str.indexOf("COMMENT");
		ss[2] = str.substring(i1, i2).trim();
		str = str.substring(i2, str.length());
		
		i1 = str.indexOf("COMMENT")+7;
		i2 = str.length();
		ss[3] = str.substring(i1, i2-1);
		
		}else {
			
			i1 = 0;
			i2 = str.length();
			ss[2] = str.substring(i1, i2).trim();
			
		}
		
		return ss;

	}
	
	private static final String DEFAULT_ENCODING = "Utf-8";//编码  
	private static final int PROTECTED_LENGTH = 51200;// 输入流保护 50KB  
	  
	public String readInfoStream(InputStream input) throws Exception {  
	    if (input == null) {  
	        throw new Exception("输入流为null");  
	    }  
	    //字节数组  
	    byte[] bcache = new byte[2048];  
	    int readSize = 0;//每次读取的字节长度  
	    int totalSize = 0;//总字节长度  
	    ByteArrayOutputStream infoStream = new ByteArrayOutputStream();  
	    try {  
	        //一次性读取2048字节  
	        while ((readSize = input.read(bcache)) > 0) {  
	            totalSize += readSize;  
	            if (totalSize > PROTECTED_LENGTH) {  
	                throw new Exception("输入流超出50K大小限制");  
	            }  
	            //将bcache中读取的input数据写入infoStream  
	            infoStream.write(bcache,0,readSize);  
	        }  
	    } catch (IOException e1) {  
	        throw new Exception("输入流读取异常");  
	    } finally {  
	        try {  
	            //输入流关闭  
	            input.close();  
	        } catch (IOException e) {  
	            throw new Exception("输入流关闭异常");  
	        }  
	    }  
	  
	    try {  
	        return infoStream.toString(DEFAULT_ENCODING);  
	    } catch (UnsupportedEncodingException e) {  
	        throw new Exception("输出异常");  
	    }  
	}  
}
