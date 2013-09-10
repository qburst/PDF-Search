/**
 * 
 */
package org.qburst.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.qburst.search.model.Search;

/**
 * @author Cyril
 * 
 */
public class QueryTest {
	public void test() throws Exception {
		HttpSolrServer solr = new HttpSolrServer("http://10.4.0.56:8983/solr");
		SolrQuery query = new SolrQuery();
		query.setQuery("html");
		query.setFields("content", "author");
		query.setHighlight(true);
		query.addHighlightField("content");
		query.setHighlightSnippets(3);
		System.out.println("...........Query.................");
		System.out.println(query);
		QueryResponse response = solr.query(query);
		SolrDocumentList results = response.getResults();
		Map<String, Map<String, List<String>>> highlights = response
				.getHighlighting();
		ArrayList<Search> mySearch = new ArrayList<Search>();
		int idx = 0;
		for (String key : highlights.keySet()){
			List<String> data = highlights.get(key).get("content");
			Search s = new Search();
			s.setHighlights(data);
			s.setId(key);
			s.setAuthor(results.get(idx).containsKey("author") ? results.get(idx).get("author") + "" : "");
			mySearch.add(s);
			idx++;
		}
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(mySearch));
	}
}
