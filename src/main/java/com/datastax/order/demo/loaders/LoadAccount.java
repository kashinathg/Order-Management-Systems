package com.datastax.order.demo.loaders;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.datastax.demo.utils.FileUtils;
import com.datastax.order.demo.objects.Account;

public class LoadAccount {
	
	//A1, U1, 108.6
	private static final String COMMA = ",";
	private static final Logger LOG = Logger.getLogger(LoadAccount.class);	
	
	public static Map<String, Account> processAccountFile(String filename){
		long blankDate = Date.UTC(110, 1, 1, 0, 0, 0);
		Date accountDate = new Date(blankDate);
		
		Map<String, Account> accounts = new HashMap<String, Account>();
	
		List<String> readFileIntoList = FileUtils.readFileIntoList(filename);		
		Account account;
		
		for (String row : readFileIntoList){
		
			String[] items = row.split(COMMA);
			
			account = new Account();
			account.setAccountId(items[0].trim());
			account.setUserId(items[1].trim());
			account.setBalance(Double.parseDouble(items[2]));
			account.setLastUpdated(accountDate);
	
			accounts.put(account.getUserId(), account);
		}
		LOG.info("Loaded : " + filename + " with " + accounts.size() + " accounts.");
		
		return accounts;
	}
}