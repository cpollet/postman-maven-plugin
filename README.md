[![Build Status](https://travis-ci.org/cpollet/postman-maven-plugin.svg?branch=master)](https://travis-ci.org/cpollet/postman-maven-plugin)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=net.cpollet.maven.plugins%3Apostman-maven-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=net.cpollet.maven.plugins%3Apostman-maven-plugin)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.cpollet.maven.plugins/postman-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.cpollet.maven.plugins/postman-maven-plugin)

# Postman Maven Plugin
A maven plugin to export [JAX-RS](https://github.com/jax-rs) annotated classes and methods to [Postman collection](https://www.getpostman.com/collection).

# Usage
Requirements
* Java 8+
* Maven 3.5.0+

## CLI
In you ```~/.m2/settings.xml``` file, add the following:
```
<pluginGroups>
  <pluginGroup>net.cpollet.maven.plugins</pluginGroup>
</pluginGroups>
```

Then, to generates a file ```${project.artifactId}-${project.version}.json``` containing the postman collection in the ```target``` folder:
```
$ mvn postman:generate -Dpostman.packagesToScan=net.cpollet \
                       -Dpostman.baseUrl=http://localhost \
                       -Dpostman.basicAuth.username=username \
                       -Dpostman.basicAuth.password=password
```
You can execute this command from the ```jaxrs``` folder for instance.

* ```postman.baseUrl``` mandatory. A valid URL that will be used as the base URL for all discovered endpoints;
* ```postman.packagesToScan``` optional. If not set, the plugin will scan all packages;
* ```postman.basicAuth.*``` optional.

## pom.xml
The CLI equivalent XML configuration is:
```
<plugin>
    <groupId>net.cpollet.maven.plugins</groupId>
    <artifactId>postman-maven-plugin</artifactId>
    <version>...</version>
    <configuration>
        <packagesToScan> <!-- optional -->
            <value>net.cpollet</value>
            <!-- ... -->
        </packagesToScan>
        <environments>
            <environment>
                <name>default</name>
                <baseUrl>http://localhost</baseUrl>
                <basicAuth> <!-- optional -->
                    <username>username</username>
                    <password>password</password>
                </basicAuth>
            <environment>
            <!-- ... -->
        <environments>
    </configuration>
    <executions> <!-- optional -->
        <execution>
            <id>generate-postman</id>
            <phase>package</package>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
The ```<execution>``` section is optional, as the plugin binds it's ```generate``` goal to the ```package``` phase. 

# Build
```
$ mvn clean install
```

Tu run the integration tests, use the ```run-its``` profile. Make sure you have the ```newman-assert``` and ```rodolpheche/wiremock``` docker images:
```
$ cd newman-assert && build.sh && cd ..
$ docker pull rodolpheche/wiremock
```
Then:
```
$ mvn clean install -Prun-its 
```

# Release & Deploy
When ```gpg: signing failed: Inappropriate ioctl for device```, executing ```export GPG_TTY=$(tty)``` might help.

## Validating before tagging
To validate the checks sonatype executes before tagging, update the ```pom.xml``` to remove the ```-SNAPSHOT```. Then, execute
```
$ mvn clean deploy -Prelease
```

## Actual release
```
$ mvn clean release:clean release:prepare
$ mvn release:perform
```
Release from web UI
