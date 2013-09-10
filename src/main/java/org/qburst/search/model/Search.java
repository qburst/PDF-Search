/**
 * 
 */
package org.qburst.search.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Cyril
 * 
 */
public class Search {
	private List<String> highlights = new LinkedList<String>();
	private String author = "";
	private String id = "";
	private String title = "";
	private String url = "";
	private String fileName = "";
	private HashMap<String, String> userDetails = new HashMap<String, String>();
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<String> getHighlights() {
		return highlights;
	}

	public void setHighlights(List<String> highlights) {
		this.highlights = highlights;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title.trim().equals("")){
			title = url.replaceAll("([\\s\\S])+/", "");
		}
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		this.fileName = url.replaceAll("([\\s\\S])+/", "");
	}

	public String getFileName() {
		return fileName;
	}

	public HashMap<String, String> getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(HashMap<String, String> userDetails) {
		this.userDetails = userDetails;
	}
}
