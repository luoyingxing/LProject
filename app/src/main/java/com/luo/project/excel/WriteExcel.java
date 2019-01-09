package com.luo.project.excel;

import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * author:  luoyingxing
 * date: 2019/1/8.
 */
public class WriteExcel {
    /**
     * Sheet表, Excel表中的底部的表名
     */
    private WritableSheet mWritableSheet;
    /**
     * Excel工作簿
     */
    private WritableWorkbook mWritableWorkbook;

    public static void main(String[] args) throws WriteException, IOException {
        WriteExcel excel = new WriteExcel();
        excel.create("excel");
        for (int i = 0; i < 10; i++) {
            excel.addString(0, i, "text" + i);
            excel.addString(1, i, "text" + i + "-1");
            excel.addString(2, i, "text" + i + "-2");
        }
        excel.close();
    }

    /**
     * 创建Sheet表
     *
     * @param fileName 文件名
     * @return Sheet表
     */
    public WritableSheet create(String fileName) {
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            // 输出Excel的路径
            String filePath = path + "/abc/" + fileName + ".xls";
            // 新建一个文件
            OutputStream os = new FileOutputStream(filePath);
            // 创建Excel工作簿
            mWritableWorkbook = Workbook.createWorkbook(os);
            // 创建Sheet表
            mWritableSheet = mWritableWorkbook.createSheet("第一张表", 0);
            return mWritableSheet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭工作簿
     *
     * @throws IOException
     * @throws WriteException
     */
    public void close() throws IOException, WriteException {
        // 写入数据
        mWritableWorkbook.write();
        // 关闭文件
        mWritableWorkbook.close();
    }

    /**
     * 添加字符串
     *
     * @param col  列号
     * @param row  行号
     * @param text 文本
     * @throws WriteException
     */
    public void addString(int col, int row, String text) throws WriteException {
        if (null == mWritableSheet) return;
        Label label = new Label(col, row, text);
        mWritableSheet.addCell(label);
    }

    /**
     * 添加数字
     *
     * @param col 列号
     * @param row 行号
     * @param num 数字
     * @throws WriteException
     */
    public void addInt(int col, int row, int num) throws WriteException {
//        if (null == mWritableSheet) return;
//        Number number = new Number(col, row, num);
//        mWritableSheet.addCell(number);
    }

}
