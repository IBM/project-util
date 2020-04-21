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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.osgi.framework.Version;
import org.osgi.framework.VersionRange;

/**
 * This is a Maven Mojo that can be used to verify that a version ID satisfies a semantic version
 * range requirement. Both of those values are required as plugin parameters, as shown in 
 * the example:
 * <p><code>
 * % mvn -q com.ibm.cloud:project-util-plugin:check-version -Drange_spec="[0.3,0.4)" -Dversion="0.3.7"
 * </code></p>
 */
@Mojo(name = "check-version", requiresProject = false)
public class CheckVersionMojo extends AbstractMojo {


  @Parameter(property = "range_spec", required = true )
  private String range;

  @Parameter(property = "version", required = true )
  private String version;

  public void execute() throws MojoExecutionException, MojoFailureException {
    
    getLog().info("Verifying version "+version+" for range spec "+range);
    
    if( ! VersionRange.valueOf(range).includes(Version.valueOf(version)) )
      throw new MojoFailureException("Invalid version: '" + version + "' for spec: '"+range+"'");

    return;
  }
}