package org.qburst.search;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.junit.Test;

public class IndexFilesTest {
	@Test
	public void test() throws SolrServerException, IOException {
		HttpSolrServer server = new HttpSolrServer("http://localhost:8983/solr");
		ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
		String fileName = "/home/user/Documents/study_materials/svg_essentials.pdf";
		String solrId = "svg_essentials.pdf";
		up.addFile(new File(fileName), "application/pdf");
		up.setParam("literal.id", solrId);
		up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
//		up.setParam("literal.author", "Cyril Cherian");
		server.request(up);
		UpdateResponse ur = server.commit();
		ur.getStatus();
	}
}
