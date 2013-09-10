package org.qburst.search;

import java.io.File;
import java.util.Date;

import org.junit.Test;

public class FilesInFolder {
	@Test
	public void getFiles() {
		File folder = new File("/home/user/my-stuffs/my-boox");
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				System.out.println(file.getName());
				System.out.println(new Date(file.lastModified()));
				System.out.println(file.getAbsolutePath());
			}
		}
	}
}
