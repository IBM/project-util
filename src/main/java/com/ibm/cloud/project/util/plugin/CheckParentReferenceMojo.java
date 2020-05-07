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
import static com.ibm.cloud.project.util.plugin.MojoUtil.verifyFolderExists;
import static com.ibm.cloud.project.util.plugin.MojoUtil.verifyVersion;

import java.io.File;
import java.text.MessageFormat;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.apache.maven.shared.transfer.artifact.ArtifactCoordinate;
import org.apache.maven.shared.transfer.artifact.install.ArtifactInstaller;
import org.apache.maven.shared.transfer.artifact.resolve.ArtifactResolver;
import org.apache.maven.shared.transfer.artifact.resolve.ArtifactResolverException;
import org.apache.maven.shared.transfer.artifact.resolve.ArtifactResult;
import org.apache.maven.shared.transfer.project.install.ProjectInstaller;
import org.apache.maven.shared.transfer.project.install.ProjectInstallerRequest;

/**
 * This is a Maven Mojo that can be used to verify consistency between a POM and its required parent POM.  
 * 
 *  <br>1. Ensure the parent POM file is installed in the local Maven repository
 *  <br>2. Verify a parent POM reference exists and that its identifiers match the required parent POM
 *  <p>Example:<code>
 *  <br>% mvn com.ibm.cloud:project-util-plugin:check-parent-pom \
 *  <br>  -Dparent_path=testcase-parent-pom.xml \
 *  <br>  -Dchild_path=testcase-pom.xml \
 *  <br>  -Dmaven.repo.local=/mvn/repository
 *  </code></p>
 *  <p>Note: This does not require a Maven project context, and moreover cannot be run from a working 
 *  directory containing a "pom.xml" file. </p> 
 */
@Mojo(name = "check-parent-pom", requiresProject = false)
public class CheckParentReferenceMojo extends AbstractMojo {

  @Parameter(property = "parent_path", required = true )
  private String parentPath;

  @Parameter(property = "child_path", required = true )
  private String childPath;

  @Parameter( defaultValue = "${session}", required = true, readonly = true )
  private MavenSession session;


  @Component
  private ArtifactResolver artifactResolver;
  
  @Component
  private ArtifactInstaller artifactInstaller;

  @Component
  private ProjectBuilder projectBuilder;

  @Component
  private ProjectInstaller projectInstaller;

  
  public void execute() throws MojoExecutionException, MojoFailureException {

    String mavenRepoPath = session.getLocalRepository().getBasedir();
            
    // Verify Maven repository exists
    if( ! verifyFolderExists(mavenRepoPath) )
      throw new MojoExecutionException(MessageFormat.format(Messages.get(Messages.MISSING_REPO), mavenRepoPath));
    
    // Verify POM coordinate exists in parent pom.xml
    ArtifactCoordinate parentCoordinate = getPomCoordinate(parentPath);
    getLog().info(MessageFormat.format(Messages.get(Messages.FOUND_PARENT_POM_COORDINATE), parentCoordinate));

    // Check for installed parent POM
    boolean installRequired = false;
    ProjectBuildingRequest buildingRequest = session.getProjectBuildingRequest();
    try {      
      ArtifactResult artifactResult = artifactResolver.resolveArtifact(buildingRequest, parentCoordinate);
      getLog().info(MessageFormat.format(Messages.get(Messages.FOUND_PARENT_POM), artifactResult.getArtifact().getFile().toString()));
    }
    catch (ArtifactResolverException e) {
      installRequired = true;
    }
    
    // Install parent POM
    if( installRequired ) {
      try {
        getLog().info(MessageFormat.format(Messages.get(Messages.INSTALLING_PARENT_POM), parentPath));
        ProjectBuildingResult projectBuildingResult = projectBuilder.build( new File(parentPath), buildingRequest );
        ProjectInstallerRequest projectInstallerRequest = new ProjectInstallerRequest();
        projectInstallerRequest.setProject( projectBuildingResult.getProject() );
        projectInstaller.install( buildingRequest, projectInstallerRequest );
      }
      catch (Exception e) {
        throw new MojoExecutionException(MessageFormat.format(Messages.get(Messages.POM_INSTALL_FAIL), parentPath, mavenRepoPath), e);
      }
    }
    
    // Verify parent reference exists in pom.xml
    ArtifactCoordinate parentReferenceCoordinate = getParentPomCoordinate(childPath);
    getLog().info(MessageFormat.format(Messages.get(Messages.VERIFIED_PARENT_POM_REF), parentReferenceCoordinate));

    // Verify referenced parent matches expected parent 
    String g_expected = parentCoordinate.getGroupId();
    String a_expected = parentCoordinate.getArtifactId();
    String v_expected = parentCoordinate.getVersion();
    
    if( ! (g_expected.equals(parentReferenceCoordinate.getGroupId()) && a_expected.equals(parentReferenceCoordinate.getArtifactId())) ) 
      throw new MojoFailureException(MessageFormat.format(
              Messages.get(Messages.BAD_PARENT_POM_REF), 
              g_expected, a_expected, v_expected));

    // Verify parent version range in pom.xml matches parent version 
    if( ! verifyVersion(v_expected, parentReferenceCoordinate.getVersion()) )
      throw new MojoFailureException(MessageFormat.format(
              Messages.get(Messages.PARENT_POM_VERSION_MISMATCH), 
              g_expected, a_expected, v_expected, parentReferenceCoordinate.getVersion()));
    
    return;
  }
}