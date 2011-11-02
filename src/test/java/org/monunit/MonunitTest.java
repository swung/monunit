package org.monunit;

import java.io.File;
import java.net.UnknownHostException;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MonunitTest {

	/**
	 * @param args
	 * @throws MongoException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException,
			MongoException {
		Monunit monunit = new Monunit();
		monunit.setMongo(new Mongo("127.0.0.1", 27017));
		monunit.setFile(new File("/home/bijia/workspace/eclipse-jee/monunit/src/test/resources/changeset1.xml"));
		monunit.setChangeId("ChangeSet-2");
		monunit.setDbName("test");
		monunit.setVerbose(false);
		
		monunit.process();
	}

}
