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

import static com.ibm.cloud.project.util.plugin.MojoUtil.replaceFile;
import static com.ibm.cloud.project.util.plugin.MojoUtil.verifyFileIntegrity;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * <p>Verify local file using url for true reference file
 * 
 * <p>Accepts URLs with 'file' or 'http(s)' scheme. If <code>ref_url</code> is specified and <code>replace_on_fail</code> is false, then save a copy of the reference file and fail.
 * <p>Parameters:
 * <ul>
 * <li><b>file_path</b> A path on the local filesystem for the file to verify
 * <li><b>cksum_url</b> A URL for a file containing the MD5 digest for the file to verify
 * <li><b>ref_url</b> A URL for the reference copy of the file to verify (optional)
 * <li><b>replace_on_fail</b> Replace the local file with the reference copy if the cksum match fails. (optional, default=false)
 * </ul>
 */
@Mojo(name = "verify-file", requiresProject = false)
public class VerifyFileIntegrityMojo extends AbstractMojo {

  @Parameter(property = "file_path")
  private String path;

  @Parameter(property = "cksum_url")
  private String cksumURL;

  @Parameter(property = "ref_url", required = false)
  private String refURL;

  @Parameter(property = "replace_on_fail", required = false, defaultValue = "false")
  private boolean replaceOnFail;


  public void execute() throws MojoExecutionException {

    boolean replace = replaceOnFail && refURL != null;
    try {
      // Verify cksum of subject file
      if( ! verifyFileIntegrity(new File(path), new URL(cksumURL)) ) {
        
        getLog().warn(MessageFormat.format(Messages.get(Messages.CKSUM_MISMATCH), path));  
        
        if( ! replace ) {
          if( refURL != null ) {
            // Save a copy of the true reference 
            String[] tokens = path.split("\\.(?=[^\\.]+$)");
            String path2 = tokens[0] + "-reference." + tokens[1]; 
            replaceFile(new URL(refURL), new File(path2));
            getLog().info(String.format("Expected file content: %s", path2));              
          }
          throw new MojoFailureException(MessageFormat.format(Messages.get(Messages.CKSUM_MISMATCH), path));
        }        
        
        // Replace subject file with reference of record
        replaceFile(new URL(refURL), new File(path));
        getLog().warn(MessageFormat.format(Messages.get(Messages.REPLACED_PROTECTED_FILE), path));  

      }
    }
    catch (Exception e) {
      throw new MojoExecutionException("Verify File Integrity", e);
    }
    
  }
}