/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.yegor256;

import com.jcabi.log.Logger;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

/**
 * Checks whether the host machine is connected to the Internet.
 *
 * @since 0.0.1
 */
@Mojo(
    name = "jping",
    defaultPhase = LifecyclePhase.INITIALIZE,
    threadSafe = true,
    requiresDependencyResolution = ResolutionScope.COMPILE
)
public final class JpingMojo extends AbstractMojo {

    /**
     * Maven project.
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    private transient MavenProject project;

    /**
     * File to create.
     * @checkstyle MemberNameCheck (7 lines)
     */
    @Parameter(defaultValue = "${project.build.directory}/jping.txt")
    private transient File fileName;

    /**
     * Property name to set.
     * @checkstyle MemberNameCheck (7 lines)
     */
    @Parameter(defaultValue = "we-are-online")
    private transient String propertyName;

    /**
     * Property value to set.
     * @checkstyle MemberNameCheck (7 lines)
     */
    @Parameter(defaultValue = "true")
    private transient String propertyValue;

    /**
     * Fail the build if there is no Internet connection?
     * @checkstyle MemberNameCheck (7 lines)
     */
    @Parameter(defaultValue = "false")
    private transient boolean failWhenOffline;

    /**
     * The URL to ping.
     * @checkstyle MemberNameCheck (7 lines)
     */
    @Parameter(defaultValue = "https://www.google.com")
    private transient String url;

    /**
     * Timeout to make a connection (in milliseconds).
     * @checkstyle MemberNameCheck (7 lines)
     */
    @Parameter(defaultValue = "4000")
    private transient int connectTimeout;

    /**
     * Read to make a connection (in milliseconds).
     * @checkstyle MemberNameCheck (7 lines)
     */
    @Parameter(defaultValue = "4000")
    private transient int readTimeout;

    @Override
    public void execute() throws MojoFailureException {
        StaticLoggerBinder.getSingleton().setMavenLog(this.getLog());
        final boolean online = this.ping();
        if (online) {
            this.project.getProperties().setProperty(
                this.propertyName, this.propertyValue
            );
            Logger.info(
                this,
                "Property ${%s} set to \"%s\"",
                this.propertyName, this.propertyValue
            );
            if (this.fileName.getParentFile().mkdirs()) {
                Logger.info(
                    this,
                    "The directory \"%s\" created",
                    this.fileName.getParentFile()
                );
            }
            try {
                Files.write(
                    this.fileName.toPath(),
                    this.propertyName.getBytes(Charset.defaultCharset())
                );
            } catch (final IOException ex) {
                throw new MojoFailureException(ex);
            }
            Logger.info(
                this,
                "The file \"%s\" created",
                this.fileName
            );
        } else {
            Logger.info(
                this,
                "Property ${%s} is not set",
                this.propertyName
            );
            if (this.fileName.delete()) {
                Logger.info(
                    this,
                    "The file \"%s\" deleted",
                    this.fileName
                );
            }
        }
    }

    /**
     * Ping it.
     * @return TRUE if we are online
     */
    private boolean ping() throws MojoFailureException {
        boolean online = true;
        try {
            final URLConnection conn = new URI(this.url).toURL().openConnection();
            conn.setConnectTimeout(this.connectTimeout);
            conn.setReadTimeout(this.readTimeout);
            conn.connect();
            conn.getInputStream().close();
        } catch (final IOException ignored) {
            online = false;
        } catch (final URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
        if (!online && this.failWhenOffline) {
            throw new MojoFailureException(
                "There is no Internet connection"
            );
        }
        return online;
    }

}
