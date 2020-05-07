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

import static com.ibm.cloud.project.util.plugin.MojoUtil.getParentPomCoordinate;
import static com.ibm.cloud.project.util.plugin.MojoUtil.getPomCoordinate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.transfer.artifact.ArtifactCoordinate;
import org.junit.Test;


public class TestMojoUtil {

  static String parentPomPath = "src/test/resources/testcase-parent-pom.xml";
  static String pomPath = "src/test/resources/testcase-pom.xml";

  @Test
  public void testGetPomCoordinate() {
    
    ArtifactCoordinate c;
    try {
      c = getPomCoordinate(parentPomPath);
      assertEquals("unexpected pom coordinate", "dev.appsody:spring-boot2-stack:pom:0.3.7", c.toString());
    }
    catch (MojoExecutionException e) {
      fail(e.toString());
    }

  }

  @Test
  public void testGetParentPomCoordinate() {
    
    ArtifactCoordinate c;
    try {
      c = getParentPomCoordinate(pomPath);
      assertEquals("unexpected pom coordinate", "dev.appsody:spring-boot2-stack:pom:[0.3, 0.4)", c.toString());
    }
    catch (MojoExecutionException e) {
      fail(e.toString());
    }
  
  }

}
