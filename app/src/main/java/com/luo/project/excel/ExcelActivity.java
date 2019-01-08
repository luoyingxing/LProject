package com.luo.project.excel;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.luo.project.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * author:  luoyingxing
 * date: 2019/1/8.
 */
public class ExcelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excel_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        read();
        test();
    }

    private List<Info> list = new ArrayList<>();

    private void read() {
        AssetManager assetManager = getAssets();

        Workbook book;
        Sheet sheet;

        try {
            //hello.xls为要读取的excel文件名
            book = Workbook.getWorkbook(assetManager.open("excel_t.xls"));

            //获得第一个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
            sheet = book.getSheet(1);

            int row = sheet.getRows();

            for (int i = 1; i < row; i++) {
                Cell cell = sheet.getCell(2, i); //（列，行）
                String data = cell.getContents();
                String[] content = data.split("装：");

                if (content.length > 1) {
//                    Log.d("ExcelActivity", "content: " + content[0]);

                    String[] info = content[0].split("，| ,| ");

                    Info inf = new Info();


                    if (info.length == 4) {
//                        Log.d("ExcelActivity", "" + info[0]);
                        String[] pStr = info[0].split("[\\d]+");
                        if (pStr.length > 1) {
                            inf.provider = pStr[1].substring(1);
                        } else {
                            inf.provider = info[0];
                        }

//                        Log.w("ExcelActivity", "" + inf.provider);

                        inf.card = info[1];
                        inf.number = info[2];
                        inf.information = info[3];

//                        Log.e("ExcelActivity", info[0] + "     " +
//                                info[1] + "      " +
//                                info[2] + "      " +
//                                info[3]);
                    } else if (info.length == 5) {
//                        Log.d("ExcelActivity", "" + info[0]);
                        String[] pStr = info[0].split("[\\d]+");
                        if (pStr.length > 1) {
                            inf.provider = pStr[1].substring(1);
                        } else {
                            inf.provider = info[0];
                        }

//                        Log.w("ExcelActivity", " " + inf.provider);

                        inf.card = info[1];
                        inf.number = info[2];
                        inf.information = info[3] + "，" + info[4];

//                        Log.e("ExcelActivity", info[0] + "     " +
//                                info[1] + "      " +
//                                info[2] + "      " +
//                                info[3] + "，" + info[4]);
                    }

                    list.add(inf);
                }
            }
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.w("ExcelActivity", "============== 读取完毕 ===============");
    }

    private void write() {
//        AssetManager assetManager = getAssets();
//
//        try {
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/excel.xls";
//            Log.i("==============", "path:" + path);
//
//            File file = new File(path);
//
//            if (!file.exists()){
//                file.mkdir();
//            }
//
//
//            WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
//
//
//            WritableSheet sheet = writableWorkbook.getSheet(0);
//
//
//            int row = sheet.getRows();
//            Label label = new Label(0, row + 1, "地址测试信息");
//            sheet.addCell(label);
//            Label label1 = new Label(1, row + 1, "测试1");
//            sheet.addCell(label1);
//            Label label2 = new Label(2, row + 1, "测试2");
//            sheet.addCell(label2);
//            Label label3 = new Label(3, row + 1, "测试3");
//            sheet.addCell(label3);
//            // 从内存中写入文件中
//            writableWorkbook.write();
//            // 关闭资源，释放内存
//            writableWorkbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (WriteException e) {
//            e.printStackTrace();
//        }
    }


    private void test() {
        try {
            WriteExcel excel = new WriteExcel();
            excel.create("excel_test");

            for (int i = 0; i < list.size(); i++) {
                Info info = list.get(i);

                excel.addString(3, i, info.provider);//列，行，文本
                excel.addString(4, i, info.card);
                excel.addString(5, i, info.number);
                excel.addString(6, i, info.information);

            }
            excel.close();

        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.w("ExcelActivity", "==============写入完毕 ===============");
    }
}