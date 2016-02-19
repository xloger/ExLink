package com.xloger.exlink.app.util;


import java.io.*;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xloger on 2015/10/26.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class StreamUtil {
    private StreamUtil(){

    }

    public static void close(Object object){
            try {
                if (object == null) {
                    return;
                }
                if(object instanceof InputStream){
                    ((InputStream) object).close();
                }else if (object instanceof OutputStream){
                    ((OutputStream)object).close();
                }else if (object instanceof Reader){
                    ((Reader) object).close();
                }else if (object instanceof Writer){
                    ((Writer) object).close();
                }else if(object instanceof HttpURLConnection){
                    ((HttpURLConnection) object).disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static byte[] readStream(InputStream in){
        byte[] bytes = null;
        if (in != null) {
            ByteArrayOutputStream bout=new ByteArrayOutputStream();
            readStream(in,bout);
            bytes=bout.toByteArray();
            close(bout);
        }
        return bytes;
    }

    public static void readStream(InputStream in,OutputStream out) {
        if (in != null&&out!=null) {
            int len=0;
            byte[] bytes=new byte[1024];

            try {
                while ((len=in.read(bytes))!=-1){
                      out.write(bytes,0,len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static boolean isMatch(String s1,String s2){
        if (s1==null||s2==null){
            return false;
        }

        if (s1.equals(s2)){
            return true;
        }

        if (s1.startsWith(s2)||s2.startsWith(s1)){
            return true;
        }
        return false;
    }

    public static String parseUrl(String s){
        StringBuffer ret = new StringBuffer();
        Pattern pattern =Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");//匹配的模式
//        Pattern pattern =Pattern.compile("charset=(.+?)https?:\\/\\/");//匹配的模式

        //通配符中也要加入转移字符 (.+?)代表要查找的内容

        Matcher matcher=pattern.matcher(s);

        while(matcher.find())

        {
            ret.append(matcher.group());
            System.out.println(matcher.group()); //每次返回第一个即可 可用groupcount()方法来查看捕获的组数 个数

        }

        return ret.toString();
    }

    public static boolean isSecondLevelDomain(String mainUrl,String testUrl){
        boolean ret = false;
        if (testUrl.contains(mainUrl)){
            ret=true;
        }

        return ret;
    }

}
