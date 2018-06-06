package ru.gosha.CouplesDetective.xl;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;

class readFromExcelXLS implements ExcelFileInterface {
    HSSFWorkbook myExcelBook = null;


    readFromExcelXLS(HSSFWorkbook myExcelBook) throws IOException {
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
        HSSFSheet myExcelSheet = myExcelBook.getSheet("1");
        HSSFRow row = myExcelSheet.getRow(Row - 1);
        if (Column < 0 || Row < 0)
            name = null;
        else {
            if (row == null)
                name = " ";
            else {
                if(row.getCell(Column - 1).getCellType() == HSSFCell.CELL_TYPE_STRING)
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
