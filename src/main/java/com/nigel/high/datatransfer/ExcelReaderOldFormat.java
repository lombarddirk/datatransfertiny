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
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

public class ExcelReaderOldFormat {

	private static final String FILE_NAME = "/home/a152119/share/SOURCE WITH SPACES.xls";
	private int term = 0;
	private int helped = 0;
	private int numberOfSubjectsForty = 0;
	private int numberOfSubjectsThirty = 0;
	private int numberOfSubjectsBelowThirty = 0;
	private DataFormatter fmt = new DataFormatter();


	public HashMap<String, ReaderPOJO> readerExcelSheet(String pathOfSourceFile, String sourceFileName,
			String sourceSheetName, String column) {

		determineTheTerm(column);
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
					.of("English  Home Language", "Afrikaans First Additional Language", "Creative Arts", "Economic Management Sciences",
							"Life Orientation", "Mathematics", "Natural Sciences", "Social Sciences", "Technology")
					.collect(Collectors.toList());

			dataFromSpreadsheet = new HashMap<String, ReaderPOJO>();

			while (iterator.hasNext()) {

				Row row = iterator.next();
				if (cellNumber.size() > 3) {
					if (row.getCell(2) != null && !row.getCell(2).getStringCellValue().trim().isEmpty()) {
						helped = 0;
						numberOfSubjectsForty = 0;
						numberOfSubjectsThirty = 0;
						numberOfSubjectsBelowThirty = 0;
						int listPosition = 0;
						//System.out.println(row.getRowNum());
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
										readerPOJO.setNoOfYearsInThePhase(fmt.formatCellValue(c));
									}
								}
							}

							if (row.getCell(x) != null && x >= 10 && x <= 32
									&& !fmt.formatCellValue(row.getCell(x)).trim().isEmpty()) 
							{
								Cell pop = row.getCell(x, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
								if (pop == null) {
									readerPOJO.getMarksList().put(readerPOJO.subjects.get(listPosition), "NF");
								} else {
									readerPOJO.getMarksList().put(readerPOJO.subjects.get(listPosition), fmt.formatCellValue(pop));
									if(term == 4)
									{
										term4SubjectRulesSupplied(readerPOJO, listPosition, pop);
									}
									else
									{
										termSubjectRulesAutomated(readerPOJO, listPosition, pop);
									}
								}
								listPosition++;
							}
							if (term == 4 && row.getCell(x) != null && x > 32
									&& !row.getCell(x).getStringCellValue().trim().isEmpty()) 
							{
								Cell pop = row.getCell(x, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
								if (pop == null) {
									readerPOJO.setOverallStatus( "NF");
								} else {
									readerPOJO.setOverallStatus(pop.getStringCellValue());
								}
							}
						}
						if(term == 4)
						{
							term4OverallRulesSupplied(readerPOJO);
						}
						else
						{
							termOverallRulesAutomated(readerPOJO);
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
						}
						if (cell != null && cell.getStringCellValue().replace("\n", " ").replace("\r", " ").trim().equalsIgnoreCase("Code")) {
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
								|| (cell.getAddress().formatAsString().equalsIgnoreCase("A1"))
								|| (cell.getAddress().formatAsString().equalsIgnoreCase("A2"))
								|| (cell.getAddress().formatAsString().equalsIgnoreCase("A3"))
								|| (cell.getAddress().formatAsString().equalsIgnoreCase("BG4"))
								|| (cell.getAddress().formatAsString().equalsIgnoreCase("A5"))
								|| (cell.getAddress().formatAsString().equalsIgnoreCase("AG6"))
								|| (cell.getColumnIndex() > row.getLastCellNum())) {
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

			workbook.close();
		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataFromSpreadsheet;
	}
	
	private void term4SubjectRulesAutomated(ReaderPOJO readerPOJO, Integer listPosition, Cell pop)
	{
		switch (readerPOJO.subjects.get(listPosition)) {
		case "FIRSTLANGUAGE":
			if (Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) < 50) {
				if (helped < 3 && Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) >= 48) {
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
			if (Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) < 40) {
				if (helped < 3 && Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) >= 38) {
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
					readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "C");

			} else {
				readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "pass");
			}
			break;
		default:
			if (Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) < 40) {
				if (helped < 3 && Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) >= 38) {
					helped++;
					readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "*");
					numberOfSubjectsForty++;
				} else if (Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) >= 30) {
					numberOfSubjectsThirty++;
					readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
				} else {
					if (helped < 3 && Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) >= 28) {
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
	
	private void termSubjectRulesAutomated(ReaderPOJO readerPOJO, Integer listPosition, Cell pop)
	{
		switch (readerPOJO.subjects.get(listPosition)) {
		case "FIRSTLANGUAGE":
			if (Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) < 50) {
				readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
				readerPOJO.setPassOrFail("fail");
			} else {
				readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "pass");
			}
			break;
		case "SECONDLANGUAGE":
			if (Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) < 40) {
				readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
				readerPOJO.setPassOrFail("fail");
			} else {
				readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "pass");
			}
			break;
		case "MATHEMATICS":
			if (Integer.valueOf(pop.getStringCellValue()) < 40) {
				readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
				readerPOJO.setPassOrFail("fail");
			} else {
				readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "pass");
			}
			break;
		default:
			if (Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) < 40) {
				if (Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) >= 30) {
					numberOfSubjectsThirty++;
					readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
				} else {
					numberOfSubjectsBelowThirty++;
					readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
				}
			} else {
				numberOfSubjectsForty++;
				readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "pass");
			}

		}
	}
	
	private void term4SubjectRulesSupplied(ReaderPOJO readerPOJO, Integer listPosition, Cell pop)
	{
		switch (readerPOJO.subjects.get(listPosition)) {
		case "FIRSTLANGUAGE":
			if (Integer.valueOf(fmt.formatCellValue(pop).trim().replace("*", "")) < 50) {
				if (fmt.formatCellValue(pop).trim().contains("*")) {
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
			if (Integer.valueOf(pop.getStringCellValue().trim().replace("*", "")) < 40) {
				if (pop.getStringCellValue().trim().contains("*")) {
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
			if (Integer.valueOf(fmt.formatCellValue(pop).trim().replace("C", "")) < 40) {
				if (fmt.formatCellValue(pop).trim().contains("C")) {
					readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "C");
				}
				else
				{
					readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
					readerPOJO.setPassOrFail("fail");
				}
			} else {
				readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "pass");
			}
			break;
		default:
			if (Integer.valueOf(fmt.formatCellValue(pop).trim().replace("*", "")) < 40) {
				if (fmt.formatCellValue(pop).trim().contains("*")) {
					readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "*");
					numberOfSubjectsForty++;
				} else if (Integer.valueOf(fmt.formatCellValue(pop).trim().replace("*", "")) >= 30) {
					numberOfSubjectsThirty++;
					if (fmt.formatCellValue(pop).trim().contains("*")) {
						readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "*");
					}
					else
					{
						readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
					}
				} else {
					if (Integer.valueOf(fmt.formatCellValue(pop).trim().replace("*", "")) >= 28) {
						if (fmt.formatCellValue(pop).trim().contains("*")) {
							readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "*");
						}
						else
						{
							readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
						}
						numberOfSubjectsThirty++;
					}
					else
					{
						numberOfSubjectsBelowThirty++;
						if (fmt.formatCellValue(pop).trim().contains("*")) {
							readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "*");
						}
						else
						{
							readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "fail");
						}
					}
				}
			} else {
				numberOfSubjectsForty++;
				readerPOJO.getSubjectSymbol().put(readerPOJO.subjects.get(listPosition), "pass");
			}

		}
	}
	
	private void term4OverallRulesAutomated(ReaderPOJO readerPOJO)
	{
		if(Float.valueOf(readerPOJO.getNoOfYearsInThePhase()) >= 4)
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
	
	private void termOverallRulesAutomated(ReaderPOJO readerPOJO)
	{

		if (!(numberOfSubjectsForty >= 3 && (numberOfSubjectsThirty >= 2 || numberOfSubjectsBelowThirty < 2))) 
		{
			readerPOJO.setPassOrFail("fail");
			readerPOJO.setOverallStatus("NA");
		}
		else
		{
			if(readerPOJO.getPassOrFail() != null && readerPOJO.getPassOrFail().equalsIgnoreCase("fail"))
			{
				readerPOJO.setPassOrFail("fail");
				readerPOJO.setOverallStatus("NA");
			} 
			else 
			{
				readerPOJO.setPassOrFail("pass");
				readerPOJO.setOverallStatus("NA");
			}
		}
	}
	
	private void term4OverallRulesSupplied(ReaderPOJO readerPOJO)
	{
		if(readerPOJO.getOverallStatus().equalsIgnoreCase("NYP"))
		{
		  readerPOJO.setPassOrFail("pass");
		}
		else if(readerPOJO.getOverallStatus().equalsIgnoreCase("NP"))
		{
		  readerPOJO.setPassOrFail("fail");
		}
		else
		{
		  readerPOJO.setPassOrFail("pass");
		}
	}
	
	private void determineTheTerm(String columnToUpdate)
	{
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

}
