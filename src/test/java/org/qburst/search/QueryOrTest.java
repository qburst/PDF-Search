package org.qburst.search;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class QueryOrTest {
	public void test() throws SolrServerException {
		HttpSolrServer solr = new HttpSolrServer("http://10.4.0.56:8983/solr");
		SolrQuery query = new SolrQuery();
		query.setQuery("HTML OR shadow");
		query.setFields("content");
		query.setHighlight(true);
		query.addHighlightField("content");
		query.setHighlightSnippets(1000);
		System.out.println("...........Query.................");
		System.out.println(query);
		QueryResponse response = solr.query(query);
		SolrDocumentList results = response.getResults();
		Map<String, Map<String, List<String>>> highlights = response
				.getHighlighting();
		System.out.println("...........Response.................");
		System.out.println(highlights);
	}
}
