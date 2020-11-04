package com.datastax.order.demo.loaders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.datastax.demo.utils.FileUtils;
import com.datastax.order.demo.objects.User;

public class LoadUser {

	//User example - 
	private static final String COMMA = ",";
	private static final Logger LOG = Logger.getLogger(LoadUser.class);
	private static DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

	
	public static Map<String, User> processUserFile(String filename){
		Map<String, User> users = new HashMap<String, User>();	
		
		List<String> readFileIntoList = FileUtils.readFileIntoList(filename);
		
		User user;
		
		for (String row : readFileIntoList){
			
			String[] items = row.split(COMMA);
			
			user = new User();
			user.setUserId(items[0].trim());
			user.setFirstname(items[1].trim());
			user.setMiddlename(items[2].trim());
			user.setLastname(items[3].trim());
			user.setDob(LoadUser.parseDate(user.getUserId(), items[4].trim()));
			user.setStreetAddress(items[5].trim());
			user.setZipCode(items[6].trim());
			user.setCityName(items[7].trim());
			user.setStateName(items[8].trim());
			user.setGender(items[9].trim());
			user.setPhoneNumber(items[10].trim());
			user.setEmail(items[11].trim());
			user.setCountryCode(items[12].trim());
			
			users.put(user.getUserId(), user);
		}	
		LOG.info("Loaded : " + filename + " with " + users.size() + " users.");
		
		return users;
	}
	
	private static Date parseDate(String userId, String dob) {
		try{
			return dateFormatter.parse(dob);
		}catch (Exception e){
			LOG.warn("Dob :" + dob + " for user :" + userId + " is not a valid date");
			return new Date();
		}
	}	
}