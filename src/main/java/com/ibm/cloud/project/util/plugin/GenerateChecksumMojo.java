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

import static com.ibm.cloud.project.util.plugin.MojoUtil.getMD5Digest;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * <p>Generate an MD5 checksum for a specified file.</p>
 * <p>Example:
 * <br><code>s=`mvn -q com.ibm.cloud:project-util-plugin:gen-cksum -Dfile_path=a.xml`</code></p>
 */
@Mojo(name = "gen-cksum", requiresProject = false)
public class GenerateChecksumMojo extends AbstractMojo {

  @Parameter(property = "file_path")
  private String path;

  public void execute() throws MojoExecutionException {
    
    try {
      System.out.println(getMD5Digest(new File(path)));
    }
    catch (Exception e) {
      throw new MojoExecutionException("Generate Checksum", e);
    }
  }
}