package com.xloger.exlink.app.util;


import java.io.*;
import java.net.HttpURLConnection;

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
}
