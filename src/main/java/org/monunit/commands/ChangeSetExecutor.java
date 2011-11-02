package org.monunit.commands;

import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class ChangeSetExecutor {
	private final Logger logger = Logger.getLogger(ChangeSetExecutor.class);

	private DB db;

	public ChangeSetExecutor(Mongo mongo, String dbName) {
		db = mongo.getDB(dbName);
	}

	public void execute(List<ChangeSet> changeSets, String changeid) {
		for (ChangeSet changeSet : changeSets) {
			if (changeid == null) {
				execute(changeSet);
				logger.info("ChangeSet executed: " + changeSet.getChangeId());
			} else if (changeSet.isRunAlways()
					|| changeSet.getChangeId().equals(changeid)) {
				execute(changeSet);
				logger.info("ChangeSet executed: " + changeSet.getChangeId());
			} else {
				logger.info("ChangeSet ignored: " + changeSet.getChangeId());
			}
		}
	}

	private void execute(ChangeSet changeSet) {
		for (Script command : changeSet.getCommands()) {
			runScript(command.getBody());
		}
	}

	private void runScript(String code) {
		db.eval(code);
	}
}
