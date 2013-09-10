/**
 * 
 */
package org.qburst.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author Cyril
 *
 */
public class RegexMatch {
	@Test
	public void getTitle(){
		String test = "/home/user/my-stuffs/my-boox/spring-web =flow-2-web-development.9781847195425.46659.pdf";
	    test = test.replaceAll("([\\s\\S])+/", "");
	    System.out.println(test);
	}
}
