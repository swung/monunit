package org.monunit;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.monunit.commands.ChangeSet;
import org.monunit.commands.ChangeSetExecutor;
import org.monunit.commands.Script;
import org.monunit.reader.ChangeSetReader;

import com.mongodb.Mongo;

public class Monunit {
	private final static Logger logger = Logger.getLogger(Monunit.class);

	private Mongo mongo = null;
	private String dbName;
	private File file = null;
	private String changeid = null;

	private boolean isVerbose = false;

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setChangeId(String id) {
		this.changeid = id;
	}

	public void setVerbose(boolean isVerbose) {
		this.isVerbose = isVerbose;
		if (isVerbose) {
			logger.setLevel(Level.TRACE);
		}
	}

	public void reset() {
		this.file = null;
		this.changeid = null;
	}

	public void process() {
		List<ChangeSet> changeSets = getChangeSets();
		new ChangeSetExecutor(mongo, dbName).execute(changeSets, changeid);
	}

	private List<ChangeSet> getChangeSets() {
		ChangeSetReader changeSetsReader = new ChangeSetReader();
		List<ChangeSet> changeSets = changeSetsReader.getChangeSets(file);
		logChangeSets(changeSets);
		return changeSets;
	}

	private void logChangeSets(List<ChangeSet> changeSets) {
		if (isVerbose) {
			for (ChangeSet changeSet : changeSets) {
				logger.trace("Changeset");
				logger.trace("id: " + changeSet.getChangeId());
				for (Script command : changeSet.getCommands()) {
					logger.trace("script");
					logger.trace(command.getBody());
				}
			}
		}
	}

}
