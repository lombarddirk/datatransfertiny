package com.nigel.high.datatransfer;

import java.util.ArrayList;

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
	private ArrayList<String> marksList = new ArrayList<String>();

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

	public ArrayList<String> getMarksList() {
		return marksList;
	}

	public void setMarksList(ArrayList<String> marksList) {
		this.marksList = marksList;
	}

}
