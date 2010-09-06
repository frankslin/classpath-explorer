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

import static java.io.File.separator;
import static java.io.File.separatorChar;

import java.io.File;

import junit.framework.TestCase;

public class ClassPathFactoryTest extends TestCase {

  private ClassPathFactory factory = new ClassPathFactory();
  private String bin;
  private String binTest;
  private String binName;
  private String binTestName;

  @Override
  protected void setUp() throws Exception {
    String[] paths = factory.parseClasspath(factory.getJVMClasspath());
    for (String path : paths) {
      File pathFile = new File(path);
      if (pathFile.exists() && pathFile.isDirectory()) {
        if (new File(pathFile, pathOfClass(ClassPathFactory.class)).exists()) {
          bin = path;
          binName = path.substring(path.lastIndexOf(separator));
        }
        else if (new File(pathFile, pathOfClass(this.getClass())).exists()) {
          binTest = path;
          binTestName = path.substring(path.lastIndexOf(separator));
        }
      }
    }
    assertNotNull(bin);
    assertNotNull(binName);
  }

  public void testReadJVMClasspath() throws Exception {
    String classpath = factory.getJVMClasspath();
    assertEquals(System.getProperty("java.class.path"), classpath);
    assertTrue(classpath.contains(binName));
    assertTrue(classpath.contains(binTestName));
  }

  public void testParseClasspath() throws Exception {
    String[] paths = factory.parseClasspath(factory.getJVMClasspath());
    boolean bin = false;
    boolean binTest = false;
    for (String path : paths) {
      bin = bin || path.endsWith(binName);
      binTest = binTest || path.endsWith(binTestName);
    }
    assertTrue(bin);
    assertTrue(binTest);
  }

  public void testCreateFromJVM() throws Exception {
    ClassPath path = factory.createFromJVM();
    assertNotNull(path);
  }

  public void testCreateFromPath() throws Exception {
    ClassPath path = factory.createFromPath(bin + File.pathSeparator
        + binTest);
    assertNotNull(path);
  }

  public void testIllegalPathIsIgnored() throws Exception {
    factory.createFromPath("X-ABC-X");
    // no exception
  }

  public void testCreateFromPaths() throws Exception {
    ClassPath path = factory.createFromPaths(bin, binTest);
    assertNotNull(path);
    assertFalse(path.isResource(clazz(Object.class)));
    assertTrue(path.isResource(clazz(ClassPathFactory.class)));
    assertTrue(path.isResource(clazz(getClass())));
  }

  private String clazz(Class<?> clazz) {
    return clazz.getName().replace(".", "/") + ".class";
  }

  private String pathOfClass(Class<?> aClass) {
    return aClass.getName().replace('.', separatorChar) + ".class";
  }

}
