package com.datastax.order.demo.queryobjects;

import java.util.Date;

/**
 * Query Object for <pre>table users_by_product(
	product_id text PRIMARY KEY,
	product_name text,
	user_id text,
	first_name text,
	last_name text,
	dob text,
	street_address text,
	zip_code text,
	city_name text,
	state_name text,
	phone_number text,
	email text 	
	</pre>
)
 * @author patcho
 *
 */
public class UsersByProduct {

	private String productId;
	private String productName;
	private String userId;
	private String firstname;
	private String middlename;
	private String lastname;
	private Date dob;
	private String streetAddress;
	private String zipCode;
	private String cityName;
	private String stateName;
	private String gender;
	private String phoneNumber;
	private String email;
	private String countryCode;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	@Override
	public String toString() {
		return "UsersByProduct [productId=" + productId + ", productName="
				+ productName + ", userId=" + userId + ", firstname="
				+ firstname + ", middlename=" + middlename + ", lastname="
				+ lastname + ", dob=" + dob + ", streetAddress="
				+ streetAddress + ", zipCode=" + zipCode + ", cityName="
				+ cityName + ", stateName=" + stateName + ", gender=" + gender
				+ ", phoneNumber=" + phoneNumber + ", email=" + email
				+ ", countryCode=" + countryCode + "]";
	}
	
	
}