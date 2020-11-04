package com.datastax.order.demo;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.datastax.order.demo.loaders.LoadUser;
import com.datastax.order.demo.objects.User;

public class TestLoadUsers {

	@Test
	public void testUsers(){
		
		Map<String, User> userList = LoadUser.processUserFile("user_list_final.csv");
		
		Assert.assertEquals(5000, userList.size());
	}
}

