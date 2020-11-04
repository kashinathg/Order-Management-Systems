package com.datastax.demo;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.mortbay.log.Log;

import com.datastax.demo.utils.FileUtils;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;

public abstract class SchemaSetup {

	static final Logger LOG = Logger.getLogger(SchemaSetup.class);
	static String CREATE_KEYSPACE;
	static String DROP_KEYSPACE;

	private Cluster cluster;
	private Session session;

	SchemaSetup() {
		String contactPointsStr = System.getProperty("contactPoints");
		if (contactPointsStr == null) {
			contactPointsStr = "127.0.0.1";
		}

		cluster = Cluster.builder().addContactPoints(contactPointsStr.split(",")).build();
		session = cluster.connect();
	}
	
	void internalSetup() {
		this.runAllowFail(DROP_KEYSPACE);
			
		//Sleep to allow for changes to be propagted.
		sleep(1000);

		Log.info("Running : " + CREATE_KEYSPACE);
		this.run(CREATE_KEYSPACE);
		
		this.runfile();		
	}
	
	void runfile() {
		String readFileIntoString = FileUtils.readFileIntoString("cql/all_tables.cql");
		
		String[] commands = readFileIntoString.split(";");
		
		for (String command : commands){
			
			String cql = command.trim();
			
			if (cql.isEmpty()){
				continue;
			}
			
			if (cql.toLowerCase().startsWith("drop")){
				this.runAllowFail(cql);
			}else{
				this.run(cql);
			}			
		}
	}

	void runAllowFail(String cql) {
		try {
			run(cql);
		} catch (InvalidQueryException e) {
			LOG.warn("Ignoring exception - " + e.getMessage());
		}
	}

	void run(String cql){
		session.execute(cql);
	}

	void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (Exception e) {
		}
	}

	
	void shutdown() {
		session.shutdown(2, TimeUnit.SECONDS);
		cluster.shutdown(2, TimeUnit.SECONDS);
	}
}