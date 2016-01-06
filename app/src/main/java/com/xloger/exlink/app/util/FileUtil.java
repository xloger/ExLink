package com.xloger.exlink.app.util;

import android.content.Context;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.entity.App;

import java.io.*;
import java.util.List;

/**
 * Created by xloger on 1月1日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class FileUtil {
    private Context context;
    private static FileUtil fileUtil;

    private FileUtil(Context context){
        this.context=context;
    }

    public static void createInstance(Context context){
        if (fileUtil == null) {
            fileUtil=new FileUtil(context);
        }
    }

    public static FileUtil getInstance(){
        if (fileUtil == null) {
            throw new IllegalStateException("FileUtil create happen an Error ");
        }
        return fileUtil;
    }

    public void save(String fileName,byte[] content){
        File folder=context.getFilesDir();
        File file=new File(folder,fileName);

        FileOutputStream fout=null;
        try {
            fout=new FileOutputStream(file);
            fout.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            StreamUtil.close(fout);
        }

    }

    public byte[] load(String fileName){
        byte[] ret = null;

        File folder=context.getFilesDir();
        File file=new File(folder,fileName);

        if (file.exists()&&file.canRead()){
            FileInputStream fin=null;
            try {
                fin=new FileInputStream(file);
                ret=StreamUtil.readStream(fin);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                StreamUtil.close(fin);
            }
        }

        return ret;
    }

    public void saveObject(String fileName,Object object){
        File folder=context.getFilesDir();
        File file=new File(folder,fileName);

        FileOutputStream fout=null;
        try {
            fout=new FileOutputStream(file);
            ObjectOutputStream oos=new ObjectOutputStream(fout);
            oos.writeObject(object);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(fout);
        }
    }

    public Object loadObject(String fileName){
        Object ret = null;

        File folder=context.getFilesDir();

        ret=loadObject(folder.toString(),fileName);

//        File file=new File(folder,fileName);

//        if (file.exists()&&file.canRead()){
//            FileInputStream fin=null;
//            try {
//                fin=new FileInputStream(file);
//                ObjectInputStream ois=new ObjectInputStream(fin);
//                ret=ois.readObject();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (StreamCorruptedException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } finally {
//                StreamUtil.close(fin);
//            }
//        }

        return ret;
    }

    public static Object loadObject(String url,String fileName){
        Object ret = null;

        FileInputStream fin=null;
        try {
            fin=new FileInputStream(url+fileName);
            ObjectInputStream ois=new ObjectInputStream(fin);
            ret=ois.readObject();
        } catch (FileNotFoundException e) {
            MyLog.log(e);
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            MyLog.log(e);
            e.printStackTrace();
        } catch (IOException e) {
            MyLog.log(e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            MyLog.log(e);
            e.printStackTrace();
        } finally {
            StreamUtil.close(fin);
        }


        return ret;
    }

    public void setReadable(String fileName){
        File folder=context.getFilesDir();
        File file=new File(folder,fileName);
        boolean b = file.setReadable(true,false);
        MyLog.log("修改权限："+b);
    }

    public static List<App> appList;

    public static List<App> getAppList(){
        List<App> apps = null;
        if (appList == null) {
            apps= (List<App>) loadObject(Constant.APP_URL, Constant.APP_FILE_NAME);
            appList=apps;
        }else {
            apps=appList;
        }
        return apps;
    }
}
