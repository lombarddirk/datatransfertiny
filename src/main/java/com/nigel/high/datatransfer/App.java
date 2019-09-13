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
		String sourceSheetName = args[2];
		String pathToTarget = args[3];
		String target = args[4];
		String targetSheetName = args[5];
		String column = args[6];
		HashMap<String, ReaderPOJO> dataFromSpreadsheet = excelReaderOldFormat.readerExcelSheet(pathToSource, source, sourceSheetName);
		ExcelUpdaterOldFormat excelUpdaterOldFormat =  new ExcelUpdaterOldFormat();
		excelUpdaterOldFormat.updateExcelSheet(dataFromSpreadsheet, column, pathToTarget, target, targetSheetName);
    }
}
