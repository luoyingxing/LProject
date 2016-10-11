package com.luo.project.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;


import com.luo.project.MainApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

/**
 * FileUtils
 * <p/>
 * Created by luoyingxing on 16/10/9.
 */
public class FileUtils {

    private static Logger mlog = new Logger(FileUtils.class.getSimpleName(), Log.ERROR);
    private static Context mContext = MainApplication.getAppContext();

    /**
     * 写入文本文件
     * 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @param fileName 文件名
     * @param content  文件内容
     */
    public static void write(String fileName, String content) {
        try {
            FileOutputStream fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            mlog.e(e.getMessage());
        }
    }

    /**
     * 读取文本文件 (/data/data/PACKAGE_NAME/files)
     *
     * @param fileName 文件名
     * @return 文件内容
     */
    public static String read(String fileName) {
        try {
            FileInputStream in = mContext.openFileInput(fileName);
            return readInStream(in);
        } catch (Exception e) {
            mlog.e(e.getMessage());
        }
        return "";
    }

    /**
     * 读取输入流
     */
    private static String readInStream(FileInputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            mlog.e(e.getMessage());
        }
        return "";
    }


    /**
     * 将字节数组写入文件
     */
    public static boolean writeFile(byte[] buffer, String folder, String fileName) {
        boolean writeSucc = false;

        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        String folderPath = "";
        if (sdCardExist) {
            folderPath = Environment.getExternalStorageDirectory() + File.separator + folder + File.separator;
        } else {
            writeSucc = false;
        }

        File fileDir = new File(folderPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File file = new File(folderPath + fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(buffer);
            writeSucc = true;
        } catch (Exception e) {
            mlog.e(e.getMessage());
        } finally {
            if (out != null) {
                try {

                    out.close();
                } catch (IOException e) {
                    mlog.e(e.getMessage());
                }
            }
        }

        return writeSucc;
    }


    /**
     * 根据文件的绝对路径获取文件名但不包含扩展名
     *
     * @param filePath 文件的绝对路径
     * @return 文件名但不包含扩展名
     */
    public static String getFileNameNoFormat(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int point = filePath.lastIndexOf('.');
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1, point);
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileFormat(String fileName) {
        if (TextUtils.isEmpty(fileName)) return "";

        int point = fileName.lastIndexOf('.');

        if (point == fileName.length() - 1) return "";
        return fileName.substring(point + 1);
    }


    /**
     * 获取文件大小
     *
     * @param fileSize 文件字节数
     * @return 文件大小
     */
    public static String getFileSize(long fileSize) {
        if (fileSize <= 0) return "0";
        java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
        float temp = (float) fileSize / 1024;
        if (temp >= 1024) {
            return df.format(temp / 1024) + "M";
        } else {
            return df.format(temp) + "K";
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileSize 文件字节数
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileSize) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取目录文件大小
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); //递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 获取目录文件个数
     */
    public long getFileList(File dir) {
        long count = 0;
        File[] files = dir.listFiles();
        count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                count = count + getFileList(file);//递归
                count--;
            }
        }
        return count;
    }

    /**
     * 将输入流流转成字节数组
     */
    public static byte[] toBytes(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        try {
            while ((ch = in.read()) != -1) {
                out.write(ch);
            }
            byte buffer[] = out.toByteArray();
            out.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查文件是否存在
     */
    public static boolean checkFileExists(String name) {
        boolean status;
        if (!name.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + name);
            status = newPath.exists();
        } else {
            status = false;
        }
        return status;

    }

    /**
     * 计算SD卡的剩余空间
     *
     * @return 返回-1，说明没有安装sd卡
     */
    public static long getFreeDiskSpace() {
        String status = Environment.getExternalStorageState();
        long freeSpace = 0;
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                freeSpace = availableBlocks * blockSize / 1024;
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        } else {
            return -1;
        }
        return (freeSpace);
    }

    /**
     * 新建目录
     */
    public static boolean createDirectory(String directoryName) {
        boolean status;
        if (!directoryName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + directoryName);
            status = newPath.mkdir();
            status = true;
        } else
            status = false;
        return status;
    }

    /**
     * 检查Sdcard是否已挂载.
     */
    public static boolean checkSdcardExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 检查是否安装SD卡
     */
    public static boolean checkSaveLocationExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 删除目录(包括：目录里的所有文件)
     *
     * @param folderName 文件名
     * @return 是否删除成功
     */
    public static boolean deleteDirectory(String folderName) {
        SecurityManager checker = new SecurityManager();
        if (!TextUtils.isEmpty(folderName)) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + folderName);
            checker.checkDelete(newPath.toString());
            if (newPath.isDirectory()) {
                String[] listfile = newPath.list();
                try {
                    for (String file : listfile) {
                        File deletedFile = new File(newPath.toString() + "/"
                                + file);
                        deletedFile.delete();
                    }
                    newPath.delete();
                    mlog.i("deleteDirectory" + folderName);
                    return true;
                } catch (Exception e) {
                    mlog.e(e.getMessage());
                }
            }
        }
        return false;
    }


    public static void savePref(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit().putString(key, value).commit();
    }

    public static String getPref(String key) {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(key, "");
    }

    public static void saveBooleanPref(String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit().putBoolean(key, value).commit();
    }

    public static boolean getBooleanPref(String key) {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getBoolean(key, true);
    }

    public static void saveIntPref(String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit().putInt(key, value).commit();
    }

    public static int getIntPref(String key) {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getInt(key, 0);
    }

    public static void clearPref() {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit().clear().commit();
    }

    public static void removePref(String key) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit().remove(key).commit();

    }


    /**
     * Read file to String
     *
     * @param fileName
     * @param charset
     * @return
     * @throws IOException
     */
    public static String readFile(String fileName, String charset) throws IOException {
        File file = new File(fileName);
        return readFile(file, charset);
    }

    /**
     * Read file to String with default charset 'UTF-8'
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readFile(String fileName) throws IOException {
        return readFile(fileName, "UTF-8");
    }


    /**
     * Read file to String with default charset 'UTF-8'
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readFile(File fileName) throws IOException {
        return readFile(fileName, "UTF-8");
    }


    /**
     * Read file to String
     *
     * @param fileName
     * @param charset
     * @return
     * @throws IOException
     */
    public static String readFile(File fileName, String charset) throws IOException {
        if (fileName == null || !fileName.isFile()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(fileName), charset);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!sb.toString().equals("")) {
                    sb.append("\r\n");
                }
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }

    public static String getCurrentDataPath(Context context) throws IOException {

        return getCurrentDataPath(context, "");
    }

    public static String getCurrentDataPath(Context context, String folderName) throws IOException {
        String currentDataPath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            currentDataPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName;
            makeDirs(currentDataPath);
        } else {
            currentDataPath = context.getFilesDir().getAbsolutePath();
        }
        return currentDataPath;
    }


    /**
     * Create New File
     *
     * @param path     Folder Name
     * @param fileName File Name
     * @throws IOException
     */
    public static boolean createFile(String path, String fileName) throws IOException {
        File file = new File(path + "/" + fileName);
        return !file.exists() && file.createNewFile();
    }


    /**
     * Append file using FileOutputStream
     *
     * @param fileName
     * @param content
     */
    public static void WriteStreamAppendByFileOutputStream(String fileName, String content) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, true)));
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Append file using FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void writeStreamAppendByFileWriter(String fileName, String content) throws IOException {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Append file using RandomAccessFile
     *
     * @param fileName
     * @param content
     */
    public static void WriteStreamAppendByRandomAccessFile(String fileName, String content) throws IOException {
        try {
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            long fileLength = randomFile.length();
            // Write point to the end of file.
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy file
     *
     * @param oldPath String
     * @param newPath String
     * @return boolean
     */
    public static void copyFileFromPath(String oldPath, String newPath) throws IOException {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("copy failed");
            e.printStackTrace();

        }

    }

    /**
     * Copy File from InputStream
     *
     * @param is      InputStream
     * @param newPath String
     * @return boolean
     */
    public static void copyFileFromInputStream(InputStream is, String newPath) throws IOException {
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            if (is != null) {

                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = is.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                is.close();
            }
        } catch (Exception e) {
            System.out.println("Copy Failed");
            e.printStackTrace();

        } finally {
            try {
                is.close();
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * If {@code append} is true and the file already exists, it will be appended to; otherwise
     * it will be truncated. The file will be created if it does not exist.
     *
     * @param filePath
     * @param content
     * @param append   Indicates whether or not to append to an existing file.
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) throws IOException {
        if (content == null || content.isEmpty()) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw e;
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }


    /**
     * If the file already exists, it will be truncated. The file will be created if it does not exist.
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) throws IOException {
        return writeFile(filePath, content, false);
    }


    /**
     * Write file with InputStream
     *
     * @param filePath
     * @param stream
     * @return
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param stream   the input stream
     * @param append   if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * Write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * Copy file
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }


    /**
     * Get only the file name without extension
     *
     * @param filePath
     * @return
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (isFileExist(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(".");
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * Get  the file name with extension
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (isFileExist(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * Get folder name from the filepath
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (isFileExist(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * Get extension of the file
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }

        int extenPosi = filePath.lastIndexOf(".");
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * Create Folder
     *
     * @param dirPath
     * @return
     */
    public static boolean makeDirs(String dirPath) {
        File folder = new File(dirPath);
        return folder.mkdirs() || folder.isDirectory();
    }


    /**
     * Indicates if the file represents a file exists.
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if the file represents a directory exists.
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty()) {
            return false;
        }
        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * Delete the File no matter it's a file or folder.If the file path is a folder,this method
     * will delete all the file in the folder.
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * Get size of the file
     *
     * @param path
     * @return Return the length of the file in bytes. Return -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        if (path == null || path.isEmpty()) {
            return -1;
        }
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }


    /**
     * Get file which from assets
     *
     * @param context
     * @param fileName The name of the asset to open.
     * @return
     */
    public static String getFileFromAssets(Context context, String fileName) {
        if (context == null || fileName == null || fileName.isEmpty()) {
            return null;
        }

        StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get file which from Raw
     *
     * @param context
     * @param resId
     * @return
     */
    public static String getFileFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
