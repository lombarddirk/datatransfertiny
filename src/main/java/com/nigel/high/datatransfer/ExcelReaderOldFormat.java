package com.nigel.high.datatransfer;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelReaderOldFormat {

	private static final String FILE_NAME = "/home/a152119/share/SOURCE.xls";

	public HashMap<String, ReaderPOJO> readerExcelSheet(String pathOfSourceFile, String sourceFileName, String sourceSheetName) {

		HashMap<String, ReaderPOJO> dataFromSpreadsheet = null;
		try {
			//System.out.println(pathOfSourceFile);
			//System.out.println(sourceFileName);
			FileInputStream excelFile = new FileInputStream(new File(pathOfSourceFile + sourceFileName));
			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
			HSSFSheet datatypeSheet = workbook.getSheet(sourceSheetName);
			Iterator<Row> iterator = datatypeSheet.iterator();
			// boolean startToRead = false;
			ArrayList<Integer> cellNumber = new ArrayList<Integer>();
			ReaderPOJO readerPOJO = new ReaderPOJO();
			List<String> colomnNamesList = Stream
					.of("Home Language", "First Additional Language", "Creative Arts (Gr 09)",
							("Economic Management Sciences (Gr 09)"), "Life Orientation (Gr 09)", "Mathematics (Gr 09)",
							"Natural Sciences (Gr 09)", "Social Sciences (Gr 09)", "Technology (Gr 09)")
					.collect(Collectors.toList());
			dataFromSpreadsheet = new HashMap<String, ReaderPOJO>();
			while (iterator.hasNext()) {

				Row row = iterator.next();
				if (cellNumber.size() > 1) {
					if (row.getCell(2) != null && !row.getCell(2).getStringCellValue().trim().isEmpty()) {
						for (int x : cellNumber) {

							if (x < 10) {
								readerPOJO = new ReaderPOJO();
								Cell c = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
								if (c == null) {
									// The cell is empty
								} else {
									List<String> items = Arrays.asList(c.getStringCellValue().split("\\s*,\\s*"));
									readerPOJO.setSurname(items.get(0).trim());
									List<String> names = Arrays.asList(items.get(1).split("\\s* \\s*"));
									readerPOJO.setName(names.get(0).trim());
									if (names.size() > 1) {
										readerPOJO.setSecondName(names.get(1).trim());
									} else {
										readerPOJO.setSecondName("");
									}
								}
							}

							if (row.getCell(x) != null && x >= 10
									&& !row.getCell(x).getStringCellValue().trim().isEmpty()) {
								Cell pop = row.getCell(x, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
								if (pop == null) {
									readerPOJO.getMarksList().add("NF");
								} else {
									readerPOJO.getMarksList().add(pop.getStringCellValue());
								}
							}
						}
					}
					if (readerPOJO.getSurname() != null && !readerPOJO.getSurname().isEmpty())
						dataFromSpreadsheet.put(readerPOJO.getSurname().toUpperCase() + " " + readerPOJO.getName().toUpperCase(), readerPOJO);

				} else {
					Iterator<Cell> iteratorCell = row.cellIterator();
					int i = 0;
					while (iteratorCell.hasNext()) {

						Cell cell = iteratorCell.next();
						if (cell != null && cell.getStringCellValue().trim()
								.equalsIgnoreCase("Surnames and Names of Learners in Alphabetical Order")) {

							cellNumber.add(cell.getColumnIndex());
							break;
						}
						if (cell != null && colomnNamesList
								.contains(cell.getStringCellValue().replace("\n", " ").replace("\r", " ").trim())) {
							cellNumber.add(cell.getColumnIndex());
						}
						if (cell.getStringCellValue() == null
								|| (cell.getStringCellValue().trim().isEmpty() && row.getLastCellNum() > 0 && i > 28)) {
							break;
						}
						i++;
					}
				}
				if ((row.getCell(2) == null && !dataFromSpreadsheet.isEmpty())
						|| (row.getCell(2).getStringCellValue().trim().isEmpty() && !dataFromSpreadsheet.isEmpty())) {
					break;
				}
			}

			/*
			 * dataFromSpreadsheet = new HashMap<String, ReaderPOJO>(); int i = 0; while
			 * (iterator.hasNext()) {
			 * 
			 * Row currentRow = iterator.next(); if (currentRow.getCell(2) != null &&
			 * currentRow.getCell(2).getStringCellValue().trim()
			 * .equalsIgnoreCase("Surnames and Names of Learners in Alphabetical Order")) {
			 * startToRead = true; continue; }
			 * 
			 * if (currentRow.getCell(2) != null && startToRead &&
			 * !currentRow.getCell(2).getStringCellValue().trim().isEmpty()) { //
			 * Iterator<Cell> cellIterator = currentRow.iterator(); i = i + 1;
			 * System.out.println(i); readerPOJO = new ReaderPOJO(); Cell c =
			 * currentRow.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); if (c ==
			 * null) { // The cell is empty } else { List<String> items =
			 * Arrays.asList(c.getStringCellValue().split("\\s*,\\s*"));
			 * readerPOJO.setSurname(items.get(0).trim()); List<String> names =
			 * Arrays.asList(items.get(1).split("\\s* \\s*"));
			 * readerPOJO.setName(names.get(0).trim()); if (names.size() > 1) {
			 * readerPOJO.setSecondName(names.get(1).trim()); } else {
			 * readerPOJO.setSecondName(""); } }
			 * 
			 * for(int x = 10; x<26; x=x+2) { c = currentRow.getCell(x,
			 * Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); if (c == null) {
			 * readerPOJO.getMarksList().add("NF"); } else {
			 * readerPOJO.getMarksList().add(c.getStringCellValue()); } }
			 * 
			 * c = currentRow.getCell(10, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); if (c
			 * == null) { // The cell is empty } else {
			 * readerPOJO.setFirstLanguage(c.getStringCellValue()); }
			 * 
			 * c = currentRow.getCell(12, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); if (c
			 * == null) { // The cell is empty } else {
			 * readerPOJO.setSecondLanguage(c.getStringCellValue()); }
			 * 
			 * c = currentRow.getCell(14, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); if (c
			 * == null) { // The cell is empty } else {
			 * readerPOJO.setCreative_Arts(c.getStringCellValue()); }
			 * 
			 * c = currentRow.getCell(16, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); if (c
			 * == null) { // The cell is empty } else {
			 * readerPOJO.setEconomic_Management_Sciences(c.getStringCellValue()); }
			 * 
			 * c = currentRow.getCell(18, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); if (c
			 * == null) { // The cell is empty } else {
			 * readerPOJO.setLife_Orientation(c.getStringCellValue()); }
			 * 
			 * c = currentRow.getCell(20, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); if (c
			 * == null) { // The cell is empty } else {
			 * readerPOJO.setMathematics(c.getStringCellValue()); }
			 * 
			 * c = currentRow.getCell(22, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); if (c
			 * == null) { // The cell is empty } else {
			 * readerPOJO.setNatural_Sciences(c.getStringCellValue()); }
			 * 
			 * c = currentRow.getCell(24, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); if (c
			 * == null) { // The cell is empty } else {
			 * readerPOJO.setSocial_Sciences(c.getStringCellValue()); }
			 * 
			 * c = currentRow.getCell(26, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); if (c
			 * == null) { // The cell is empty } else {
			 * readerPOJO.setTechnology(c.getStringCellValue()); }
			 * dataFromSpreadsheet.put(readerPOJO.getSurname() + " " + readerPOJO.getName(),
			 * readerPOJO); } if(currentRow.getCell(2) == null ||
			 * (currentRow.getCell(2).getStringCellValue().trim().isEmpty() &&
			 * !dataFromSpreadsheet.isEmpty())) { break; } }
			 */
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataFromSpreadsheet;
	}

}
