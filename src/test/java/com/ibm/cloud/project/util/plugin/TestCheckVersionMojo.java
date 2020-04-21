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

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;

public class TestCheckVersionMojo extends AbstractMojoTestCase {

  @Test
  public void testExecute() {
    
    CheckVersionMojo mojo = new CheckVersionMojo();
    
    String versionRange = "[0.3,0.4)";
    
    try {
      setVariableValueToObject(mojo, "range", versionRange);
      setVariableValueToObject(mojo, "version", "0.3.7");
      mojo.execute();
    }
    catch (Exception e) {
      fail(e.getMessage());
    }

    try {
      setVariableValueToObject(mojo, "version", "0.4");
      mojo.execute();
      fail("Expected MojoFailure for version: 0.4 and spec: "+versionRange);
    }
    catch (MojoFailureException e) {
      assertTrue(
              "Failed on test for version not within upper bound", 
              e.getMessage().matches("Invalid version: .* for spec: .*"));
    }
    catch (Exception e) {
      fail(e.getMessage());
    }

    try {
      setVariableValueToObject(mojo, "version", "0.2");
      mojo.execute();
      fail("Expected MojoFailure for version: 0.2 and spec: "+versionRange);
    }
    catch (MojoFailureException e) {
      assertTrue(
              "Failed on test for version not within lower bound", 
              e.getMessage().matches("Invalid version: .* for spec: .*"));
    }
    catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Override
  protected void setUp() throws Exception {
    // bypass for no maven project context
    //super.setUp();
  }

}
