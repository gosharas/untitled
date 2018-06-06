package ru.gosha.CouplesDetective.xl;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;


import java.io.IOException;

public class readFromExcelXLSX implements ExcelFileInterface{
    XSSFWorkbook myExcelBook = null;


    readFromExcelXLSX(XSSFWorkbook myExcelBook) throws IOException {
        this.myExcelBook = myExcelBook;
    }

    /**
     * Функция возвращает значение ячейки.
     * @param Column Порядковый номер столбца. Отсчёт начинается с 1.
     * @param Row Порядковый номер строки. Отсчёт начинается с 1.
     * @return значение ячейки типа String.
     */

    @Override
    public String getCellData(int Column, int Row){
        String name = null;
        XSSFSheet myExcelSheet = myExcelBook.getSheet("1");
        XSSFRow row = myExcelSheet.getRow(Row - 1);
        if (Column < 0 || Row < 0)
            name = null;
        else {
            if (row == null)
                name = " ";
            else {
                if(row.getCell(Column - 1).getCellType() == XSSFCell.CELL_TYPE_STRING)
                    name = row.getCell(Column - 1).getStringCellValue();
            }
        }

        return name;
    }

    /**
     * ЧОТО-ДЕЛОЕТ
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        myExcelBook.close();

    }
}
