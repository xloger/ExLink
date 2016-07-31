package com.xloger.exlink.app.util;


import android.net.Uri;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.entity.Rule;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.List;
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

    public  static boolean isContain(String url){
        if (url==null){
            return false;
        }
        boolean ret = false;
        //获取自定义Url
        byte useDifferentUrl='0';
        String differentUrl = null;
        byte[] bytes = FileUtil.load(Constant.APP_URL, Constant.DIFFERENT_URL_FILE_NAME);
        if (bytes != null&&bytes.length>0) {
            useDifferentUrl=bytes[0];
            differentUrl=new String(bytes,1,bytes.length-1);
            if (useDifferentUrl=='1'){
                MyLog.log("使用自定义Url:"+differentUrl);
            }
        }

        String testUrl;
        if (useDifferentUrl=='1'&&differentUrl!=null){
            testUrl=differentUrl;
        }else {
            testUrl="http://www.example.org/ex-link-test";
        }

        if (url.equals(testUrl)){
            ret=true;
        }else {
            ret=isContain(url,testUrl);
        }

        return ret;
    }

    public static boolean isContain(String longUrl,String shortUrl){

        longUrl= URLDecoder.decode(longUrl);
        shortUrl=URLDecoder.decode(shortUrl);
        Uri shortUri=Uri.parse(shortUrl);
        //假如不是个链接，那直接看是否包含吧
        if (shortUri.getHost()==null){
            if (longUrl.contains(shortUrl)){
                return true;
            }else {
                return false;
            }
        }

        if (shortUri.getPath() == null) {
            if (longUrl.contains(shortUri.getHost())){
                return true;
            }
        }else {
            if (longUrl.contains(shortUri.getHost())&&longUrl.contains(shortUri.getPath())){
                return true;
            }
        }

        return false;
    }

    public static String clearUrl(String url){
        String ret = null;

        if (url.startsWith("sinaweibo")){
            Uri sinaUri=Uri.parse(url);
            String tempUrl = sinaUri.getQueryParameter("url");
            if (tempUrl == null) {
                tempUrl=sinaUri.getQueryParameter("showurl");
            }

            if (tempUrl != null) {
                ret=tempUrl;
            }

            if (ret==null||!ret.contains("http")){
                //对秒拍链接特殊处理
                if (sinaUri.getQueryParameter("url_type").equals("39")&&sinaUri.getQueryParameter("object_type").equals("video")){
                    String containerid=sinaUri.getQueryParameter("containerid");
                    ret="http://m.weibo.cn/p/index/?containerid="+containerid;
                }
            }
        }


        return ret;
    }

    public static boolean isContainUrl(String string){
        if (string==null||string.equals("")){
            return false;
        }
        if (string.contains("http")||string.contains("sinaweibo://")){
            return true;
        }
        return false;
    }

    public static boolean isContain(List<Rule> ruleList,String ruleKey){
        for (int i = 0; i < ruleList.size(); i++) {
            if (ruleList.get(i).getExtrasKey().equals(ruleKey)){
                return true;
            }
        }
        return false;
    }

}
