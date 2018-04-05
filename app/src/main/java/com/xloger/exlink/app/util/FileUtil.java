package com.xloger.exlink.app.util;

import android.annotation.SuppressLint;
import android.content.Context;

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

    private FileUtil(Context context) {
        this.context = context;
    }

    public static void createInstance(Context context) {
        if (fileUtil == null) {
            fileUtil = new FileUtil(context);
        }
    }

    public static FileUtil getInstance() {
        if (fileUtil == null) {
            throw new IllegalStateException("FileUtil create happen an Error ");
        }
        return fileUtil;
    }

    public void save(String fileName, byte[] content) {
        File folder = context.getFilesDir();
        File file = new File(folder, fileName);

        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            fout.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(fout);
        }

    }

    public byte[] load(String fileName) {
        byte[] ret = null;
        File folder = context.getFilesDir();
        ret = load(folder.toString(), fileName);
        return ret;
    }

    public static byte[] load(String url, String fileName) {
        byte[] ret = null;

        File file = new File(url, fileName);

        if (file.exists() && file.canRead()) {
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(file);
                ret = StreamUtil.readStream(fin);
            } catch (FileNotFoundException e) {
                MyLog.e(e.getMessage());
            }finally {
                StreamUtil.close(fin);
            }
        }

        return ret;
    }

    /**
     * @deprecated 新版已经由存取序列化对象改为存取json，因此不再推荐使用该方法，使用{@link JSONFile#saveJson(List)}
     */
    @Deprecated
    public void saveObject(String fileName, Object object) {
        File folder = context.getFilesDir();
        saveObject(folder.toString(), fileName, object);
    }

    /**
     * @deprecated 新版已经由存取序列化对象改为存取json，因此不再推荐使用该方法，使用{@link JSONFile#saveJson(List)}
     */
    @Deprecated
    public static void saveObject(String url, String fileName, Object object) {
        File dir = new File(url);
        if (!dir.exists()) {
            dir.mkdirs();
            dir.setExecutable(true, false);
        }
        File file = new File(url, fileName);
        file.setReadable(true, false);
        file.setWritable(true, false);
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(object);
            oos.close();
        } catch (FileNotFoundException e) {
            MyLog.log(e);
            e.printStackTrace();
        } catch (IOException e) {
            MyLog.log(e);
            e.printStackTrace();
        } finally {
            StreamUtil.close(fout);
        }
    }

    /**
     * @deprecated 新版已经由存取序列化对象改为存取json，因此不再推荐使用该方法，使用{@link JSONFile#getJson()}
     */
    @Deprecated
    public Object loadObject(String fileName) {
        Object ret = null;
        File folder = context.getFilesDir();
        ret = loadObject(folder.toString(), fileName);
        return ret;
    }

    /**
     * @deprecated 新版已经由存取序列化对象改为存取json，因此不再推荐使用该方法，使用{@link JSONFile#getJson()}
     */
    @Deprecated
    public static Object loadObject(String url, String fileName) {
        Object ret = null;

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(url + fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            ret = ois.readObject();
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

    public void setReadable(String fileName) {
        File folder = context.getFilesDir();
        File file = new File(folder, fileName);
        @SuppressLint("SetWorldReadable") boolean b = file.setReadable(true, false);
        MyLog.log("修改权限：" + b);
    }

}
