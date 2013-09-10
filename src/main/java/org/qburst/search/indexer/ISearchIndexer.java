/**
 * 
 */
package org.qburst.search.indexer;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Gilsha
 * 
 */
public interface ISearchIndexer {
	public boolean isFileIndexed(String id) throws Exception;

	public void doIndexing(File filePath)  throws Exception;

	public ArrayList<File> getFilesFromFolder()  throws Exception;

	public void shutDownSolrServer()  throws Exception;
	
	public Map<String, String> getMetaDataFromFile(File file) throws Exception;
	
	public void rollbackSolrServer() throws Exception;
}
