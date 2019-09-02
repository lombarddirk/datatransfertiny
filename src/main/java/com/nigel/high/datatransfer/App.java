package com.nigel.high.datatransfer;

import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		ExcelReaderOldFormat excelReaderOldFormat = new ExcelReaderOldFormat();
		HashMap<String, ReaderPOJO> dataFromSpreadsheet = excelReaderOldFormat.readerExcelSheet(args[0], args[1]);
		ExcelUpdaterOldFormat excelUpdaterOldFormat =  new ExcelUpdaterOldFormat();
		excelUpdaterOldFormat.updateExcelSheet(dataFromSpreadsheet, args[4], args[2], args[3]);
    }
}
