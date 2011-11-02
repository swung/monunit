package org.monunit.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.monunit.commands.ChangeSet;
import org.monunit.commands.ChangeSetList;
import org.monunit.commands.Script;

public class ChangeSetReader {
	public List<ChangeSet> getChangeSets(File file) {
		List<ChangeSet> changeSets = new ArrayList<ChangeSet>();

		try {
			Digester digester = new Digester();

			digester.setValidating(false);

			digester.addObjectCreate("mongoChangeLog", ChangeSetList.class);
			digester.addObjectCreate("mongoChangeLog/changeSet",
					ChangeSet.class);
			digester.addSetProperties("mongoChangeLog/changeSet");
			digester.addSetNext("mongoChangeLog/changeSet", "add");

			digester.addObjectCreate("mongoChangeLog/changeSet/script",
					Script.class);
			digester.addBeanPropertySetter("mongoChangeLog/changeSet/script",
					"body");
			digester.addSetNext("mongoChangeLog/changeSet/script", "add");

			ChangeSetList changeFileSet = (ChangeSetList) digester
					.parse(new FileInputStream(file));
			for (ChangeSet changeSet : changeFileSet.getList()) {
				changeSet.setFile(file.getCanonicalPath());
			}
			changeSets.addAll(changeFileSet.getList());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.xml.sax.SAXException e) {
			e.printStackTrace();
		}

		return changeSets;
	}
}
