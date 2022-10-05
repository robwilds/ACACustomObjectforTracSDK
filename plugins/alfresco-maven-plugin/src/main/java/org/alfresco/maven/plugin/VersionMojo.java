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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Removes -SNAPSHOT suffix from the version number (if present), optionally replacing it with a timestamp.
 * The result is provided in the Maven property ${noSnapshotVersion}.
 * This feature is mostly needed to avoid Alfresco failing when installing AMP modules with non-numeric
 * versions.
 *
 * @version $Id:$
 * @goal set-version
 * @phase initialize
 * @requiresProject
 * @since 1.0
 * @threadSafe
 * @description Parses Maven version and removes literals as not allowed in AMP versions, making a noSnapshotVersion property available for POM filtering
 */
public class VersionMojo extends AbstractMojo {
	
  	private static final DateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("yyMMddHHmm");

    /**
     * The snapshotSuffix used to identify and strip the -SNAPSHOT version suffix
     * See issue https://issues.alfresco.com/jira/browse/ENH-1232
     *
     * @parameter property="maven.alfresco.snapshotSuffix" default-value="-SNAPSHOT"
     * @required
     */
    protected String snapshotSuffix;

    /**
     * Enable this option in order to replace -SNAPSHOT with the currentTimestamp
     * of the artifact creation
     * See issue https://issues.alfresco.com/jira/browse/ENH-1232
     *
     * @parameter property="maven.alfresco.snapshotToTimestamp" default-value="false"
     * @required
     */
    protected boolean snapshotToTimestamp;

    /**
     * Allows to append a custom (numeric) value to the current artifact's version,
     * i.e. appending the SCM build number can be accomplished defining
     * &lt;customVersionSuffix&gt;${buildnumber}&lt;/customVersionSuffix&gt; in the plugin
     * configuration.
     *
     * @parameter property="maven.alfresco.customVersionSuffix"
     */
    protected String customVersionSuffix;
    
    /**
     * The Maven project property the stripped version is pushed into
     *
     * @parameter property="maven.alfresco.propertyName" default-value="noSnapshotVersion"
     * @required
     */
    private String propertyName;

    /**
     * [Read Only] The Maven project.
     *
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * [Read Only] Current version of the project
     *
     * @parameter default-value="${project.version}"
     * @required
     * @readonly
     */
    protected String version;

    /**
     * Normalizes the project's version following 2 patterns
     * - Remove the -SNAPSHOT suffix, if present
     * - (Optionally) append the timestamp to the version, if -SNAPSHOT is present
     * - (Optionally) append the build number to the version
     *
     * @return the current project's version normalized
     */
    protected String getNormalizedVersion() {
        int separatorIndex = version.indexOf(snapshotSuffix);
        String normalizedVersion = version;
        if (separatorIndex > -1) {
            normalizedVersion = version.substring(0, separatorIndex);
            getLog().info("Removed -SNAPSHOT suffix from version - " + normalizedVersion);
        }
        if (this.customVersionSuffix != null && this.customVersionSuffix.length() > 0) {
            normalizedVersion += "." + this.customVersionSuffix;
            getLog().info("Added custom suffix to version - " + normalizedVersion);
        } else if (this.snapshotToTimestamp) {
            normalizedVersion += "." + TIMESTAMP_FORMATTER.format(new Date());
            getLog().info("Added timestamp to version - " + normalizedVersion);
        }
        return normalizedVersion;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
    	project.getProperties().put(propertyName, getNormalizedVersion());
    }
}
