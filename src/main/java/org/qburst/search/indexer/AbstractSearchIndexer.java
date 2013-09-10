package org.qburst.search.indexer;

import java.io.File;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * @author Gilsha
 * 
 */
public abstract class AbstractSearchIndexer implements ISearchIndexer {
	protected HttpSolrServer solr;
	private String homeFolder = "";
	
	public AbstractSearchIndexer(HttpSolrServer solr, String homeFolder) {
		this.solr = solr;
		this.homeFolder = homeFolder;
	}
	public ArrayList<File> getFilesFromFolder() throws Exception{
		File folder = new File(homeFolder);
		File[] listOfFiles = folder.listFiles();
		ArrayList<File> files = new ArrayList<File>();
		for (File file : listOfFiles) {
			if (file.isFile() && file.getAbsolutePath().endsWith("pdf")) {
				files.add(file);
			}
		}
		return files;
	}
	
	public boolean isFileIndexed(String id)  throws Exception{
		SolrQuery query = new SolrQuery("id:"+id);
		QueryResponse rsp;
		rsp = solr.query( query );
		if(rsp.getResults().getNumFound()>0)
	    	return true;
		return false;
	}

	public void shutDownSolrServer() throws Exception{
		if (solr != null) {
			solr.shutdown();
			solr = null;
		}
	}

	public void rollbackSolrServer() throws Exception{
		if (solr != null) {
			solr.rollback();
		}
	}
	
}
