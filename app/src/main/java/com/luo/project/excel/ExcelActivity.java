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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
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
    private String regex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excel_activity);

        regex = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";
    }

    @Override
    protected void onResume() {
        super.onResume();
        read();
        test();
    }

    private List<ExcelCell> excelList = new ArrayList<>();

    private void read() {
//        AssetManager assetManager = getAssets();

        Workbook book;

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(path + "/abc/excel.xls");
            book = Workbook.getWorkbook(file);

//            book = Workbook.getWorkbook(assetManager.open("excel.xls"));


            Sheet[] sheets = book.getSheets();
            if (sheets.length == 0) {
                return;
            }

            for (Sheet sheet : sheets) {
                ExcelCell excelCell = new ExcelCell();
                excelCell.tableName = sheet.getName();

                List<Info> list = new ArrayList<>();
//
//                //获得第一个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
//                sheet = book.getSheet(0);

                int row = sheet.getRows();

                for (int i = 1; i < row; i++) {
                    Cell cell = sheet.getCell(2, i); //（列，行）
                    String data = cell.getContents();
                    String[] content = data.split("装：");

                    Info inf = new Info();
                    if (content.length > 1) {
//                    Log.d("ExcelActivity", "content: " + content[0]);

                        String[] info = content[0].split("，|,| ", 3);  //分为3个数组

                        if (info.length == 3) {
                            String[] pStr = info[0].split("[\\d]+");
                            if (pStr.length > 1) {
                                inf.provider = pStr[1].substring(1);
                            } else {
                                inf.provider = info[0];
                            }

                            inf.card = info[1];


                            //找出车牌和个人信息
                            String str = info[2];
                            Matcher m = Pattern.compile(regex).matcher(str);

                            if (m.find()) {
                                String number = m.group();
                                inf.number = number;

                                String information = null;

                                int index = str.indexOf(number);
                                if (index == 0) {
                                    information = str.substring(number.length(), str.length());
                                } else if (index == str.length() - number.length()) {
                                    information = str.substring(0, str.length() - number.length());
                                } else {
                                    information = str.substring(0, index) + str.substring(index + number.length(), str.length());
                                }

                                information = information.replaceAll("，", " ");
                                information = information.replaceAll(",", " ");
                                information = information.replaceAll("；", " ");
                                information = information.replaceAll(" ", "");

                                inf.information = information.trim();
                            }

                        }
                    }

                    Log.i("ExcelActivity", "" + inf.toString());
                    list.add(inf);
                }

                excelCell.list = list;
                excelList.add(excelCell);
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
            for (int j = 0; j < excelList.size(); j++) {
                ExcelCell excelCell = excelList.get(j);
                List<Info> list = excelCell.list;

                WriteExcel excel = new WriteExcel();
                excel.create("excel_" + excelCell.tableName);

                for (int i = 0; i < list.size(); i++) {
                    Info info = list.get(i);
                    Log.d("ExcelActivity", "" + info.toString());
                    excel.addString(3, i, info.provider);//列，行，文本
                    excel.addString(4, i, info.card);
                    excel.addString(5, i, info.number);
                    excel.addString(6, i, info.information);

                }

                excel.close();
            }
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.w("ExcelActivity", "==============写入完毕 ===============");
    }
}