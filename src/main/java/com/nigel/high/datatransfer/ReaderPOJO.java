package com.nigel.high.datatransfer;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReaderPOJO {
	private String name;
	private String secondName;
	private String surname;
	private String firstLanguage;
	private String secondLanguage;
	private String creative_Arts;
	private String economic_Management_Sciences;
	private String life_Orientation;
	private String mathematics;
	private String natural_Sciences;
	private String social_Sciences;
	private String technology;
	private HashMap<String, String> marksList = new HashMap<String, String>();
	private HashMap<String,String> subjectSymbol = new HashMap<String,String>();
	private String passOrFail;
	private String overallStatus;
	private String noOfYearsInThePhase;
	
	public List<String> subjects =  Stream
			.of("FIRSTLANGUAGE", "SECONDLANGUAGE", "CREATIVE_ARTS", "ECONOMIC_MANAGEMENT_SCIENCES",
		"LIFE_ORIENTATION", "MATHEMATICS", "NATURAL_SCIENCES", "SOCIAL_SCIENCES", "TECHNOLOGY")
		.collect(Collectors.toList());

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstLanguage() {
		return firstLanguage;
	}

	public void setFirstLanguage(String firstLanguage) {
		this.firstLanguage = firstLanguage;
	}

	public String getSecondLanguage() {
		return secondLanguage;
	}

	public void setSecondLanguage(String secondLanguage) {
		this.secondLanguage = secondLanguage;
	}

	public String getCreative_Arts() {
		return creative_Arts;
	}

	public void setCreative_Arts(String creative_Arts) {
		this.creative_Arts = creative_Arts;
	}

	public String getEconomic_Management_Sciences() {
		return economic_Management_Sciences;
	}

	public void setEconomic_Management_Sciences(String economic_Management_Sciences) {
		this.economic_Management_Sciences = economic_Management_Sciences;
	}

	public String getLife_Orientation() {
		return life_Orientation;
	}

	public void setLife_Orientation(String life_Orientation) {
		this.life_Orientation = life_Orientation;
	}

	public String getMathematics() {
		return mathematics;
	}

	public void setMathematics(String mathematics) {
		this.mathematics = mathematics;
	}

	public String getNatural_Sciences() {
		return natural_Sciences;
	}

	public void setNatural_Sciences(String natural_Sciences) {
		this.natural_Sciences = natural_Sciences;
	}

	public String getSocial_Sciences() {
		return social_Sciences;
	}

	public void setSocial_Sciences(String social_Sciences) {
		this.social_Sciences = social_Sciences;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getPassOrFail() {
		return passOrFail;
	}

	public void setPassOrFail(String passOrFail) {
		this.passOrFail = passOrFail;
	}

	public String getOverallStatus() {
		return overallStatus;
	}

	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}

	public HashMap<String, String> getMarksList() {
		return marksList;
	}

	public void setMarksList(HashMap<String, String> marksList) {
		this.marksList = marksList;
	}

	public HashMap<String, String> getSubjectSymbol() {
		return subjectSymbol;
	}

	public void setSubjectSymbol(HashMap<String, String> subjectSymbol) {
		this.subjectSymbol = subjectSymbol;
	}

	public String getNoOfYearsInThePhase() {
		return noOfYearsInThePhase;
	}

	public void setNoOfYearsInThePhase(String noOfYearsInThePhase) {
		this.noOfYearsInThePhase = noOfYearsInThePhase;
	}

}
