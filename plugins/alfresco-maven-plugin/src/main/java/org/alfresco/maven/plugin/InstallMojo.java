/**
 * Copyright (C) 2017 Alfresco Software Limited.
 * <p/>
 * This file is part of the Alfresco SDK.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.alfresco.maven.plugin;

import java.io.File;
import java.io.IOException;

import org.alfresco.repo.module.tool.ModuleManagementTool;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * <p>
 * Performs a AMP to WAR overlay invoking the Alfresco Repository ModuleManagementTool.
 * It therefore wraps and emulates the same WAR overlay performed by Alfresco MMT.
 * </p>
 * This goal will install the AMP file(s) found in ${ampLocation} onto the WAR (or exploded WAR) found in ${warLocation}
 * 
 * @version $Id:$
 * @requiresDependencyResolution
 * @since 1.0
 * @goal install
 * @description Installs one or more AMPs onto an Alfresco / Share WAR (or
 *              exploded WAR folder)
 */
public class InstallMojo extends AbstractMojo {

    private static final String WEBAPP_MANIFEST_PATH = "META-INF" + File.separator + "MANIFEST.MF";
    private static final String WEBAPP_DESCRIPTOR_PATH = "WEB-INF" + File.separator + "web.xml";

    /**
     * The location of the AMP file(s) to be installed. If this location is a
     * folder all AMPs contained in the folder are installed, if it's a file it
     * get directly installed on the ${warLocation}
     * 
     * @parameter property="maven.alfresco.ampLocation" default-value="${project.build.directory}/${project.build.finalName}.amp"
     */
    private File ampLocation;
    
    /**
     * The WAR file or exploded dir to install the AMPs in. If specified
     * Defaults to <code>"${app.amp.client.war.folder}</code>
     *
     * @parameter property="maven.alfresco.warLocation" default-value="${app.amp.client.war.folder}"
     */
    private File warLocation;
    
    /**
     * Whether Alfresco MMT should be executed in verbose mode
     * 
     * @parameter property="maven.alfresco.verbose" default-value="false"
     */
    private boolean verbose;
    
    /**
     * Whether Alfresco MMT should be force installation of AMPs
     * 
     * @parameter property="maven.alfresco.force" default-value="true"
     */
    private boolean force;
    
    /**
     * Whether Alfresco MMT should produce backups while installing. Defaults to false to speed up development, set to true for Production AMP installations
     * 
     * @parameter property="maven.alfresco.backup" default-value="false"
     */
    private boolean backup;
    
    /**
     * Whether or not to skip the check for a manifest file in the warLocation
     * 
     * @parameter property="maven.alfresco.skipWarManifestCheck" default-value="false"
     */
    private boolean skipWarManifestCheck;
    
    /**
     * Whether or not to skip the attempt to install the AMP in the warLocation altogether
     * 
     * @parameter property="maven.alfresco.skipAmpInstallation" default-value="false"
     */
    private boolean skipAmpInstallation;

    public InstallMojo() {
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skipAmpInstallation) {
        	getLog().info("AMP Installation is skipped via configuration");
            return;
        }
        // Checks appropriate input params are in place
        checkParams();
        ModuleManagementTool mmt = new ModuleManagementTool();
        mmt.setVerbose(verbose);
        /**
         * Invoke the ModuleManagementTool to install AMP modules on the WAR
         * file; if ampLocation is a folder all contained AMPs are installed otherwise 
         * a single AMP install is attempted with the ampLocation
         */
        if(ampLocation.isDirectory())
        {
            try {
            	getLog().info("Installing all AMPs from directory " + ampLocation.getAbsolutePath() + " into WAR/exploded webapp at " + warLocation.getAbsolutePath());
                
            	mmt.installModules(ampLocation.getAbsolutePath(),
                        warLocation.getAbsolutePath(), false, // preview
                        force, // force install
                        backup); // backup
                getLog().info("AMPs installed successfully");
                
            } catch (IOException e) {
                throw new MojoExecutionException("ampLocation " + ampLocation.getAbsolutePath() + " did not contain AMP files - AMP installation cannot proceed");
            } // backup
        } else if(ampLocation.isFile())
        {
        	getLog().info("Installing AMP " + ampLocation.getAbsolutePath() + " into WAR/exploded webapp at " + warLocation.getAbsolutePath());
            mmt.installModule(ampLocation.getAbsolutePath(),
                    warLocation.getAbsolutePath(), false, // preview
                    force, // force install
                    backup); // backup
            getLog().info("AMP installed successfully");
        } else
        {
            throw new MojoFailureException("ampLocation " + ampLocation.getAbsolutePath() + " was neither an AMP file or a folder containing AMP files - AMP installation cannot proceed");
        }
    }

    private void checkParams() throws MojoExecutionException {
        if (this.ampLocation == null || !this.ampLocation.exists()) {
            throw new MojoExecutionException("No AMP file(s) found in " + ampLocation.getAbsolutePath() + " - AMP installation cannot proceed");
        }
        if (this.warLocation == null || !this.warLocation.exists()) {
            throw new MojoExecutionException("No WAR file found in " + warLocation.getAbsolutePath() + " - AMP installation cannot proceed");
        }
        
        File descriptor = new File(warLocation.getPath() + File.separator +  WEBAPP_DESCRIPTOR_PATH);
        if(warLocation.isDirectory() && !descriptor.exists())
            throw new MojoExecutionException("No webapp found in " + descriptor.getAbsolutePath() + ". AMP installation cannot proceed. Are you binding amp:install to the right phase?");
        
        File manifest = new File(warLocation.getPath() + File.separator + WEBAPP_MANIFEST_PATH); 
        if(!skipWarManifestCheck && warLocation.isDirectory() && !manifest.exists())
            throw new MojoExecutionException("No MANIFEST.MF found in " + manifest.getAbsolutePath() + ". AMP installation cannot proceed. Are you binding amp:install to the right phase?");
    }

    public void setWarLocation(File warLocation) {
        this.warLocation = warLocation;
    }

    public void setAmpLocation(File ampLocation) {
        this.ampLocation = ampLocation;
    }

    public void setForce(boolean force) {
        this.force = force;
    }
}