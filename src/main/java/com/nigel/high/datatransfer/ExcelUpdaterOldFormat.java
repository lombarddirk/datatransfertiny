package com.nigel.high.datatransfer;

import java.io.File;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

public class ExcelUpdaterOldFormat {

	private static final String FILE_NAME = "/home/a152119/share/Target.xls";

	public void updateExcelSheet(HashMap<String, ReaderPOJO> dataFromSpreadsheet, String columnToUpdate, String pathOfTargetFile, String targetFileName) {

		try {

			FileInputStream excelFile = new FileInputStream(new File(pathOfTargetFile + targetFileName));
			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
			HSSFSheet datatypeSheet = workbook.getSheet("Annexure D (Grade 9)");
			Iterator<Row> rowIterator = datatypeSheet.iterator();

			ReaderPOJO readerPOJO = null;
			boolean columnIndexFound = false;
			boolean passed = true;
			StringBuilder searchName = new StringBuilder();
			StringBuilder storeName = new StringBuilder();
			int namesOfLearnersIndex = 0;
			int subjectsFailedIndex = 0;
			int searchValueColumnIndex = 0;
			int commentsOnProgressIndex = 0;
			int subjectCount = 0;
			int countEmptyRows = 0;
			int term = 0;
			CellStyle styleRED = workbook.createCellStyle();
			styleRED.setFillForegroundColor(IndexedColors.RED.getIndex());
			styleRED.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			styleRED.setAlignment(HorizontalAlignment.CENTER);
			styleRED.setBorderBottom(BorderStyle.THIN);
			CellStyle styleGREEN = workbook.createCellStyle();
			styleGREEN.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
			styleGREEN.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			styleGREEN.setAlignment(HorizontalAlignment.LEFT);
			styleGREEN.setBorderBottom(BorderStyle.THIN);
			CellStyle styleREDLEFT = workbook.createCellStyle();
			styleREDLEFT.setFillForegroundColor(IndexedColors.RED.getIndex());
			styleREDLEFT.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			styleREDLEFT.setAlignment(HorizontalAlignment.LEFT);
			styleREDLEFT.setBorderBottom(BorderStyle.THIN);
			CellStyle styleNormal = workbook.createCellStyle();
			styleNormal.setAlignment(HorizontalAlignment.CENTER);
			styleNormal.setBorderBottom(BorderStyle.THIN);
			int numberOfSubjectsForty = 0;
			int numberOfSubjectsThirty = 0;
			int numberOfSubjectsBelowThirty = 0;
			while (rowIterator.hasNext()) {

				Row currentRow = rowIterator.next();

				if (columnIndexFound == true) {
					if (currentRow.getCell(namesOfLearnersIndex) != null
							&& !currentRow.getCell(namesOfLearnersIndex).getStringCellValue().isEmpty()
							&& !currentRow.getCell(namesOfLearnersIndex).getStringCellValue().replace(" ", "").trim()
									.equalsIgnoreCase("Progressed")) {
						searchName.setLength(0);
						searchName.append(currentRow.getCell(namesOfLearnersIndex).getStringCellValue());
						Cell d = currentRow.getCell((namesOfLearnersIndex + 1),
								Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
						if (d != null) {
							// List<String> items = Arrays.asList(d.getStringCellValue().split("\\s*
							// \\s*"));
							if (d.getStringCellValue().indexOf(" ") > -1) {
								searchName.append(" ").append(d.getStringCellValue().trim().substring(0,
										d.getStringCellValue().indexOf(" ")));
							} else {
								searchName.append(" ").append(d.getStringCellValue().trim());
							}
						}
						if (!searchName.toString().equalsIgnoreCase(storeName.toString())) {
							storeName.setLength(0);
							storeName.append(searchName.toString());
							passed = true;
							numberOfSubjectsForty = 0;
							numberOfSubjectsThirty = 0;
							numberOfSubjectsBelowThirty = 0;
							readerPOJO = dataFromSpreadsheet.get(searchName.toString().toUpperCase());
							subjectCount = 0;
							if(readerPOJO != null)
							{
								for(String subject : readerPOJO.subjects)
								{
									switch (subject)
									{
										case "FIRSTLANGUAGE":								
											if (Integer.valueOf(readerPOJO.getMarksList().get(readerPOJO.subjects.indexOf(subject))) < 50)
												passed = false;
												break;
										case "SECONDLANGUAGE":
											if (Integer.valueOf(readerPOJO.getMarksList().get(readerPOJO.subjects.indexOf(subject))) < 40)
												passed = false;
												break;
										case "MATHEMATICS":
											if (Integer.valueOf(readerPOJO.getMarksList().get(readerPOJO.subjects.indexOf(subject))) < 40)
												passed = false;
												break;
										default:
											if (Integer.valueOf(readerPOJO.getMarksList().get(readerPOJO.subjects.indexOf(subject))) < 40)
											{
												if (Integer.valueOf(readerPOJO.getMarksList().get(readerPOJO.subjects.indexOf(subject))) >= 30)
												{
													numberOfSubjectsThirty++;
												}
												else
												{
													numberOfSubjectsBelowThirty++;
												}
											}
											else
											{
												numberOfSubjectsForty++;
											}	
									}
								}
								if (!(numberOfSubjectsForty >= 3 && (numberOfSubjectsThirty >= 2 || numberOfSubjectsBelowThirty < 2)))
								{
									passed = false;
								}
							}
						}
					}
					if (subjectCount < 9 && currentRow.getCell(searchValueColumnIndex) != null
							&& (currentRow.getCell(subjectsFailedIndex) != null && !currentRow
									.getCell(subjectsFailedIndex).getStringCellValue().trim().isEmpty())) {
						Cell cell2Update = currentRow.getCell(searchValueColumnIndex);
						if (cell2Update != null && readerPOJO != null) {
							cell2Update.setCellValue(readerPOJO.getMarksList().get(subjectCount));
							cell2Update.setCellStyle(styleNormal);
							switch (readerPOJO.subjects.get(subjectCount))
							{
								case "FIRSTLANGUAGE":
									if (Integer.valueOf(readerPOJO.getMarksList().get(subjectCount)) < 50)
										cell2Update.setCellStyle(styleRED);
										break;
								case "SECONDLANGUAGE":
									if (Integer.valueOf(readerPOJO.getMarksList().get(subjectCount)) < 40)
										cell2Update.setCellStyle(styleRED);
										break;
								case "MATHEMATICS":
									if (Integer.valueOf(readerPOJO.getMarksList().get(subjectCount)) < 40)
										cell2Update.setCellStyle(styleRED);
										break;
								default:
									if (Integer.valueOf(readerPOJO.getMarksList().get(subjectCount)) < 40 && !(numberOfSubjectsForty >= 3 && (numberOfSubjectsThirty >= 2 || numberOfSubjectsBelowThirty < 2)))
									{
										cell2Update.setCellStyle(styleRED);
									}	
									break;
							}
				
							if (subjectCount == term - 1) {
								Cell next2Update = currentRow.getCell(commentsOnProgressIndex);
								if (passed == false) {
									next2Update.setCellValue("Term " + term + ": NA");
									next2Update.setCellStyle(styleREDLEFT);
								} else {
									next2Update.setCellValue("Term " + term + ": A");
									next2Update.setCellStyle(styleGREEN);
								}
							}
							subjectCount++;
							//System.out.println(searchName);
							//System.out.println(currentRow.getCell(subjectsFailedIndex));
						}
					}
				}

				if (columnIndexFound == false) {
					int i = 0;
					Iterator<Cell> iteratorCell = currentRow.cellIterator();
					while (iteratorCell.hasNext()) {

						Cell cell = iteratorCell.next();
						if (cell != null && cell.getStringCellValue().replace("\n", " ").replace("\r", " ").trim()
								.trim().equalsIgnoreCase("Names of Learners")) {
							namesOfLearnersIndex = cell.getColumnIndex();
							columnIndexFound = true;
						}
						if (cell != null && cell.getStringCellValue().replace("\n", " ").replace("\r", " ").trim()
								.trim().equalsIgnoreCase("Subjects Failed")) {
							subjectsFailedIndex = cell.getColumnIndex();
							columnIndexFound = true;
						}
						if (cell != null && cell.getStringCellValue().replace("\n", " ").replace("\r", " ").trim()
								.equalsIgnoreCase(columnToUpdate)) {
							searchValueColumnIndex = cell.getColumnIndex();
							if (columnToUpdate.indexOf(" 1 ") > -1) {
								term = 1;
							} else if (columnToUpdate.indexOf(" 2 ") > -1) {
								term = 2;
							} else if (columnToUpdate.indexOf(" 3 ") > -1) {
								term = 3;
							} else if (columnToUpdate.indexOf(" 4 ") > -1) {
								term = 4;
							} 
						}
						if (cell != null && cell.getStringCellValue().replace("\n", " ").replace("\r", " ").trim()
								.trim().equalsIgnoreCase("Comments on progress")) {
							commentsOnProgressIndex = cell.getColumnIndex();
							columnIndexFound = true;
						}
						if (cell.getStringCellValue() == null
								|| (cell.getStringCellValue().trim().isEmpty() && i > 10)) {
							break;
						}
						i++;
					}
				}

				if (currentRow.getCell(subjectsFailedIndex) == null
						|| (currentRow.getCell(subjectsFailedIndex).getStringCellValue().trim().isEmpty())) {
					countEmptyRows++;
					if (countEmptyRows > 3)
						break;
				} else {
					countEmptyRows = 0;
				}
			}

			excelFile.close();
			FileOutputStream outputStream = new FileOutputStream(new File(pathOfTargetFile + targetFileName));
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
