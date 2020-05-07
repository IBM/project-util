/* Copyright 2020 IBM Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */    
package com.ibm.cloud.project.util.plugin;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Messages {

  static ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages");
  
  protected static final String MISSING_POM = "missing_pom";
  protected static final String MISSING_REPO = "missing_repo";
  protected static final String MISSING_POM_ELEMENT = "missing_pom_element";
  protected static final String POM_INSTALL_FAIL = "pom_install_fail";
  protected static final String BAD_PARENT_POM_REF = "bad_parent_pom_ref";
  protected static final String PARENT_POM_VERSION_MISMATCH = "parent_pom_version_mismatch";
  protected static final String FOUND_PARENT_POM_COORDINATE = "found_parent_pom_coordinate";
  protected static final String FOUND_PARENT_POM = "found_parent_pom";
  protected static final String INSTALLING_PARENT_POM = "installing_parent_pom"; 
  protected static final String VERIFIED_PARENT_POM_REF = "verified_parent_pom_ref"; 
  
  protected static String get(String key) {
    return resourceBundle.getString(key);
  }
}
