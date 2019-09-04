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
		HashMap<String, ReaderPOJO> dataFromSpreadsheet = excelReaderOldFormat.readerExcelSheet(args[0].trim(), args[1].trim());
		ExcelUpdaterOldFormat excelUpdaterOldFormat =  new ExcelUpdaterOldFormat();
		excelUpdaterOldFormat.updateExcelSheet(dataFromSpreadsheet, args[4].trim(), args[2].trim(), args[3].trim());
    }
}
