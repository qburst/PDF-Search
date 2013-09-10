package org.qburst.search;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.junit.Test;

public class DeleteAllIndexesTest {
	public void test() throws SolrServerException, IOException {
		HttpSolrServer server = new HttpSolrServer("http://10.4.0.56:8983/solr");
		server.deleteByQuery("id:*"); // deleteByQuery is also available
		server.commit();
	}

}
