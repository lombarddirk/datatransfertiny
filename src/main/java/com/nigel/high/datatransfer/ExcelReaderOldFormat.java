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

	private static final String FILE_NAME = "/home/a152119/share/SOURCE WITH SPACES.xls";

	public HashMap<String, ReaderPOJO> readerExcelSheet(String pathOfSourceFile, String sourceFileName,
			String sourceSheetName) {

		HashMap<String, ReaderPOJO> dataFromSpreadsheet = null;
		try {
			// System.out.println(pathOfSourceFile);
			// System.out.println(sourceFileName);
			FileInputStream excelFile = new FileInputStream(new File(pathOfSourceFile + sourceFileName));
			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
			HSSFSheet datatypeSheet = workbook.getSheet(sourceSheetName);
			Iterator<Row> iterator = datatypeSheet.iterator();
			// boolean startToRead = false;
			ArrayList<Integer> cellNumber = new ArrayList<Integer>();
			ReaderPOJO readerPOJO = new ReaderPOJO();
			/*
			 * List<String> colomnNamesList = Stream .of("Home Language",
			 * "First Additional Language", "Creative Arts (Gr 09)",
			 * ("Economic Management Sciences (Gr 09)"), "Life Orientation (Gr 09)",
			 * "Mathematics (Gr 09)", "Natural Sciences (Gr 09)", "Social Sciences (Gr 09)",
			 * "Technology (Gr 09)") .collect(Collectors.toList());
			 */

			List<String> colomnNamesList = Stream
					.of("Home Language", "First Additional Language", "Creative Arts", ("Economic Management Sciences"),
							"Life Orientation", "Mathematics", "Natural Sciences", "Social Sciences", "Technology")
					.collect(Collectors.toList());

			dataFromSpreadsheet = new HashMap<String, ReaderPOJO>();

			while (iterator.hasNext()) {

				Row row = iterator.next();
				if (cellNumber.size() > 2) {
					if (row.getCell(2) != null && !row.getCell(2).getStringCellValue().trim().isEmpty()) {
						int helped = 0;
						int numberOfSubjectsForty = 0;
						int numberOfSubjectsThirty = 0;
						int numberOfSubjectsBelowThirty = 0;
						int listPosition = 0;
						for (int x : cellNumber) 
						{
							if (x < 10) {
								if (x < 6) readerPOJO = new ReaderPOJO();
								Cell c = row.getCell(x, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
								if (c == null) {
									// The cell is empty
								} else {
									if (x < 6) {
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
									else
									{
										readerPOJO.setNoOfYearsInThePhase(String.valueOf(c.getNumericCellValue()));
									}
								}
							}

							if (row.getCell(x) != null && x >= 10
									&& !row.getCell(x).getStringCellValue().trim().isEmpty()) 
							{
								Cell pop = row.getCell(x, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
								if (pop == null) {
									readerPOJO.getMarksList().put(readerPOJO.subjects.get(listPosition), "NF");
								} else {
									readerPOJO.getMarksList().put(readerPOJO.subjects.get(listPosition), pop.getStringCellValue());
									switch (readerPOJO.subjects.get(listPosition)) {
									case "FIRSTLANGUAGE":
										if (Integer.valueOf(pop.getStringCellValue()) < 50) {
											if (helped < 3 && Integer.valueOf(pop.getStringCellValue()) >= 48) {
												helped++;
												readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "*");
											} else {
												readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
												readerPOJO.setPassOrFail("fail");
											}

										} else {
											readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "pass");
										}
										break;
									case "SECONDLANGUAGE":
										if (Integer.valueOf(pop.getStringCellValue()) < 40) {
											if (helped < 3 && Integer.valueOf(pop.getStringCellValue()) >= 38) {
												helped++;
												readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "*");
											} else {
												readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
												readerPOJO.setPassOrFail("fail");
											}

										} else {
											readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "pass");
										}
										break;
									case "MATHEMATICS":
										if (Integer.valueOf(pop.getStringCellValue()) < 40) {
											if (helped < 3 && Integer.valueOf(pop.getStringCellValue()) >= 38) {
												helped++;
												readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "C");
											} else {
												readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
												readerPOJO.setPassOrFail("fail");
											}

										} else {
											readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "pass");
										}
										break;
									default:
										if (Integer.valueOf(pop.getStringCellValue()) < 40) {
											if (helped < 3 && Integer.valueOf(pop.getStringCellValue()) >= 38) {
												helped++;
												readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "*");
												numberOfSubjectsForty++;
											} else if (Integer.valueOf(pop.getStringCellValue()) >= 30) {
												numberOfSubjectsThirty++;
												readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
											} else {
												if (helped < 3 && Integer.valueOf(pop.getStringCellValue()) >= 28) {
													helped++;
													readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "*");
													numberOfSubjectsThirty++;
												}
												else
												{
													numberOfSubjectsBelowThirty++;
													readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
												}
											}
										} else {
											numberOfSubjectsForty++;
											readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "pass");
										}

									}
								}
								listPosition++;
							}
						}
						if(Float.valueOf(readerPOJO.getNoOfYearsInThePhase()) >= 4.0)
						{
							readerPOJO.setPassOrFail("pass");
							readerPOJO.setOverallStatus("NYP");
						}
						else
						{
							if (!(numberOfSubjectsForty >= 3 && (numberOfSubjectsThirty >= 2 || numberOfSubjectsBelowThirty < 2))) 
							{
								readerPOJO.setPassOrFail("fail");
								readerPOJO.setOverallStatus("NP");
							}
							else
							{
								if(readerPOJO.getPassOrFail() != null && readerPOJO.getPassOrFail().equalsIgnoreCase("fail"))
								{
									readerPOJO.setPassOrFail("fail");
									readerPOJO.setOverallStatus("NP");
								} 
								else if(readerPOJO.getSubjectSymbol().values().stream().filter(e -> e.toString().equalsIgnoreCase("*") || e.toString().equalsIgnoreCase("C")).count() > 0) 
								{
									if(readerPOJO.getSubjectSymbol().get("FIRSTLANGUAGE").toString().equalsIgnoreCase("*") || 
											readerPOJO.getSubjectSymbol().get("SECONDLANGUAGE").toString().equalsIgnoreCase("*") ||
											readerPOJO.getSubjectSymbol().get("MATHEMATICS").toString().equalsIgnoreCase("C") )
									{
										readerPOJO.setPassOrFail("pass");
										readerPOJO.setOverallStatus("PG");
									}
									else if(readerPOJO.getSubjectSymbol().entrySet().stream()
											.filter(a -> !a.getKey().equalsIgnoreCase("FIRSTLANGUAGE") && !a.getKey().equalsIgnoreCase("SECONDLANGUAGE") && !a.getKey().equalsIgnoreCase("MATHEMATICS"))
											.map(map -> map.getValue()).filter(b -> b.equalsIgnoreCase("*") || b.equalsIgnoreCase("fail")).count() >= 4)									
									{
										readerPOJO.setPassOrFail("pass");
										readerPOJO.setOverallStatus("PG");
									}
									else 								
									{
										readerPOJO.setPassOrFail("pass");
										readerPOJO.setOverallStatus("P");
									}
								}
								else 
								{
									readerPOJO.setPassOrFail("pass");
									readerPOJO.setOverallStatus("P");
								}
							}
						}
					}
					if (readerPOJO.getSurname() != null && !readerPOJO.getSurname().isEmpty())
						dataFromSpreadsheet.put(
								readerPOJO.getSurname().toUpperCase() + " " + readerPOJO.getName().toUpperCase(),
								readerPOJO);

				} else {
					Iterator<Cell> iteratorCell = row.cellIterator();
					int i = 0;
					while (iteratorCell.hasNext()) {

						Cell cell = iteratorCell.next();
						if (cell != null && cell.getStringCellValue().trim()
								.equalsIgnoreCase("Surnames and Names of Learners in Alphabetical Order")) {

							cellNumber.add(cell.getColumnIndex());
						}
						if (cell != null && cell.getStringCellValue().trim()
								.equalsIgnoreCase("No. of Years in Phase")) {
							cellNumber.add(cell.getColumnIndex());
							break;
						}
						if (cell.getStringCellValue().indexOf("(") > 0) {
							if (cell != null && colomnNamesList.contains(
									cell.getStringCellValue().substring(0, cell.getStringCellValue().indexOf("("))
											.replace("\n", " ").replace("\r", " ").trim())) {
								cellNumber.add(cell.getColumnIndex());
							}
						} else {
							if (cell != null && colomnNamesList
									.contains(cell.getStringCellValue().replace("\n", " ").replace("\r", " ").trim())) {
								cellNumber.add(cell.getColumnIndex());
							}
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
			 * Arrays.asList(c.getStringCellValue().split("\\s*,\\s*");
			 * readerPOJO.setSurname(items.get(0).trim()); List<String> names =
			 * Arrays.asList(items.get(1).split("\\s* \\s*");
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
		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataFromSpreadsheet;
	}

}
