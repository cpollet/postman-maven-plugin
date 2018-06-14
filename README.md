[![Build Status](https://travis-ci.org/cpollet/postman-maven-plugin.svg?branch=master)](https://travis-ci.org/cpollet/postman-maven-plugin) [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=net.cpollet.maven.plugins%3Apostman-maven-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=net.cpollet.maven.plugins%3Apostman-maven-plugin)

# Postman Maven Plugin
A maven plugin to export JAX-RS annotated classes and methods to Postman collection.

# Usage
## CLI
In you ```~/.m2/settings.xml``` file, add the following:
```
<pluginGroups>
  <pluginGroup>net.cpollet.maven.plugins</pluginGroup>
</pluginGroups>
```

Then, you can use
```
$ mvn postman:generate -Dpostman.packagesToScan=a,b \
                       -Dpostman.baseUrl=http://localhost \
                       -Dpostman.basicAuth.username=username \
                       -Dpostman.basicAuth.password=password
```

* ```postman.packagesToScan``` optional. If not set, the plugin will scan all packages;
* ```postman.baseUrl``` mandatory. A valid URL that will be used as the base URL for all endpoints discovered;
* ```postman.basicAuth.*``` optional.

## pom.xml
The plugin is bound to the ```package``` phase by default. The CLI equivalent XML configuration is:
```
<plugin>
    <groupId>net.cpollet.maven.plugins</groupId>
    <artifactId>postman-maven-plugin</artifactId>
    <version>...</version>
    <configuration>
        <packagesToScan>
            <value>a</value>
            <value>b</value>
        </packagesToScan>
        <baseUrl>http://localhost</baseUrl>
        <basicAuth>
            <username>username</username>
            <password>password</password>
        </basicAuth>
    </configuration>
    <executions>
        <execution>
            <id>generate-postman</id>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

# Build
```
$ mvn clean install
```

Tu run the integration tests, use the ```run-its``` profile. Make sure you have the ```newman-assert``` docker image (run ```build.sh``` script from ```newman-assert``` folder)
```
$ mvn clean install -Prun-its 
```

# Deploy snapshot
```
$ export GPG_TTY=$(tty)
$ mvn clean deploy -Prelease
```

# Release & Deploy
```
$ mvn release:clean release:prepare
$ mvn release:perform
... check nexus staging ...
$ cd target/checkout
$ mvn nexus-staging:release
```
