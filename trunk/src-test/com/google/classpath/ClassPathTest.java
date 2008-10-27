/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.classpath;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.TestCase;

public abstract class ClassPathTest extends TestCase {

	protected ClassPath path;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		path = createClassPath();
	}

	abstract protected ClassPath createClassPath() throws IOException;

	private String read(InputStream is) throws IOException {
		StringBuilder buf = new StringBuilder();
		int ch;
		while ((ch = is.read()) != -1) {
			buf.append((char) ch);
		}
		is.close();
		return buf.toString();
	}

	public void assertArrayEquals(String[] actual, String... expected) {
		String error = String.format("Expected: '%s' Actual: '%s'", Arrays
				.toString(expected), Arrays.toString(actual));
		assertEquals(error, asList(actual), asList(expected));
	}

	public void testCheckForResource() throws Exception {
		assertTrue(path.isResource("A/file1"));
		assertTrue(path.isResource("/A/file1"));
		assertFalse(path.isResource("/A/file1/"));
		assertTrue(path.isResource("/A/file2"));
		assertFalse(path.isResource("NON_EXISTANT"));
		assertFalse(path.isResource("A"));
		assertFalse(path.isResource("/A"));
		assertFalse(path.isResource("/A/"));
		assertFalse(path.isPackage("NON_EXISTANT"));
	}

	public void testCheckForPackage() throws Exception {
		assertTrue(path.isPackage("A"));
		assertFalse(path.isResource("A"));
		assertFalse(path.isPackage("NON_EXISTANT"));
	}

	public void testListPackages() throws Exception {
		assertArrayEquals(path.listPackages(""), "A", "META-INF");
		assertArrayEquals(path.listPackages("/"), "A", "META-INF");
		assertArrayEquals(path.listPackages("A"), "B", "C");
		assertArrayEquals(path.listPackages("/A"), "B", "C");
		assertArrayEquals(path.listPackages("A/"), "B", "C");
		assertArrayEquals(path.listPackages("/A/"), "B", "C");
	}

	public void testListResources() throws Exception {
		assertArrayEquals(path.listResources(""));
		assertArrayEquals(path.listResources("/"));
		assertArrayEquals(path.listResources("A"), "file1", "file2");
		assertArrayEquals(path.listResources("/A"), "file1", "file2");
		assertArrayEquals(path.listResources("/A/"), "file1", "file2");
	}

	public void testReadResource() throws Exception {
		assertEquals("FILE1", read(path.getResourceAsStream("A/file1")));
		assertEquals("FILE1", read(path.getResourceAsStream("/A/file1")));
		assertNull(path.getResourceAsStream("A/file1/"));
		assertNull(path.getResourceAsStream("/A/file1/"));
		assertNull(path.getResourceAsStream("NON_EXISTANT"));
	}

}
