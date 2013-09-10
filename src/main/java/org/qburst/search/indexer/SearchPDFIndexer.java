/**
 * 
 */
package org.qburst.search.indexer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;

/**
 * @author Gilsha
 * 
 */
public class SearchPDFIndexer extends AbstractSearchIndexer implements
		ISearchIndexer {
	
	public SearchPDFIndexer(HttpSolrServer solr, String homeFolder) {
		super(solr, homeFolder);
	}
	
	@Override
	public Map<String, String> getMetaDataFromFile(File file)
			throws Exception {
		Map<String, String> metaData = new HashMap<String, String>();
		metaData.put("url", file.getAbsolutePath());
		return metaData;
	}
	
	@Override
	public synchronized void doIndexing(File file) throws Exception {
		
		String ids = (file.getAbsolutePath().hashCode() + "").replace("-", "_");
		if (!isFileIndexed(ids)) {
			Map<String, String> metaData = getMetaDataFromFile(file);
			ContentStreamUpdateRequest up = new ContentStreamUpdateRequest(
					"/update/extract");
			up.addFile(file, "application/pdf");
			up.setParam("literal.id", ids);
			up.setParam("literal.url", (String) metaData.get("url"));
			up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
			solr.request(up);
			UpdateResponse ur = solr.commit();
			ur.getStatus();
		}

	}
}
