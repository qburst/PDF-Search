/**
 * 
 */
package org.qburst.search.indexer;

import java.io.File;
import java.util.ArrayList;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @author Cyril
 * 
 */

@Service("pdfIndexerBean")
@Scope(value = "singleton")
@Repository
@PropertySource(value = "classpath:application.properties")
public class IndexEngine {
	@Autowired
    private Environment env;
	
	public void start() throws Exception {
		HttpSolrServer solr = new HttpSolrServer(env.getProperty("solr.books"));
		ISearchIndexer si = new SearchPDFIndexer(solr, env.getProperty("solr.bookfolder"));
		File fileToDelete = null;
		try {
			ArrayList<File> files = si.getFilesFromFolder();
			for (File file : files) {
				System.out.println(file.getAbsolutePath());
				si.doIndexing(file);
				fileToDelete = file;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			si.rollbackSolrServer();
			fileToDelete.delete();// Incase the indexing fails I delete the file!
		} finally {
			si.shutDownSolrServer();
		}
	}


}
