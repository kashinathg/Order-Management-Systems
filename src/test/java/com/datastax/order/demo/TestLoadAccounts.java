package com.datastax.order.demo;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.datastax.order.demo.loaders.LoadAccount;
import com.datastax.order.demo.objects.Account;

public class TestLoadAccounts {

	@Test
	public void testAccount(){
		
		Map<String, Account> accountList = LoadAccount.processAccountFile("account_list_final.csv");
		
		Assert.assertEquals(5001, accountList.size());
	}
}

