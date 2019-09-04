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
		String pathToSource = args[0];
		String source = args[1];
		String pathToTarget = args[2];
		String target = args[3];
		String column = args[4];
		HashMap<String, ReaderPOJO> dataFromSpreadsheet = excelReaderOldFormat.readerExcelSheet(pathToSource, source);
		ExcelUpdaterOldFormat excelUpdaterOldFormat =  new ExcelUpdaterOldFormat();
		excelUpdaterOldFormat.updateExcelSheet(dataFromSpreadsheet, column, pathToTarget, target);
    }
}
