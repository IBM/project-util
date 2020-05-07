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

import static org.junit.Assert.*;

import org.junit.Test;

public class TestMessages {

  @Test
  public void test() {

    String[] keys = {
        Messages.MISSING_POM, 
        Messages.MISSING_REPO,
        Messages.MISSING_POM_ELEMENT,
        Messages.POM_INSTALL_FAIL,
        Messages.BAD_PARENT_POM_REF,
        Messages.PARENT_POM_VERSION_MISMATCH
        };

    for( String key : keys ) {
      String m = Messages.get(key);
      assertFalse(
              "Externalized message not found: "+key, 
              key.contentEquals(m));
    }
    
  }

}
