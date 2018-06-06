package ru.gosha.CouplesDetective.xl;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;


public class OpenFile implements ExcelFileInterface {

    HSSFWorkbook myExcelBookXLS = null;
    XSSFWorkbook myExcelBookXLSX = null;
    private boolean xls = false;
    private boolean xlsx = false;
    private String type = null;
    private String result = null;
    FileInputStream fileInputStream = null;
    ExcelFileInterface readXLS = null;
    ExcelFileInterface readXLSX = null;


    public OpenFile(String fileName) throws IOException {
        fileInputStream = new FileInputStream(fileName);

        for (int i = 0; i < fileName.length(); i++){
            if (fileName.charAt(i) == '.'){
                type = fileName.substring(i + 1, fileName.length());
            }
        }

        if (type.equals("xlsx")){
            myExcelBookXLSX = new XSSFWorkbook(fileInputStream);
            xlsx = true;
            xls = false;
        }else {
            if (type.equals("xls")){
                myExcelBookXLS = new HSSFWorkbook(fileInputStream);
                xls = true;
                xlsx = false;
            }
        }

    }

    @Override
    public String getCellData(int Column, int Row) throws IOException {
        if (xls){
            readXLS = new readFromExcelXLS(myExcelBookXLS);
            result = readXLS.getCellData(Column,Row);
        } else {
            if (xlsx){
                readXLSX = new readFromExcelXLSX(myExcelBookXLSX);
                result = readXLSX.getCellData(Column,Row);
            }
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        fileInputStream.close();

    }
}
