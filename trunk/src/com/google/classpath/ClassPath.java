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

import java.io.InputStream;

public interface ClassPath {

  boolean isResource(String resource);

  boolean isPackage(String packageName);

  String[] listPackages(String string);

  InputStream getResourceAsStream(String resource);

  String[] listResources(String packageName);

  String[] findResources(String rootPackageName, ResourceFilter resourceFilter);

}
