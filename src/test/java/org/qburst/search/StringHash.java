package org.qburst.search;

import org.junit.Test;

public class StringHash {
	@Test
	public void doStringTest(){
		String test = "http://docs.oracle.com/javase/6/docs/api/java/lang/String.html";
		System.out.println(test.hashCode());
	}
}
