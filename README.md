# It checks whether the machine is online 

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/yegor256/jping-maven-plugin)](http://www.rultor.com/p/yegor256/jping-maven-plugin)

[![mvn](https://github.com/yegor256/jping-maven-plugin/actions/workflows/mvn.yml/badge.svg)](https://github.com/yegor256/jping-maven-plugin/actions/workflows/mvn.yml)
[![PDD status](http://www.0pdd.com/svg?name=yegor256/jping-maven-plugin)](http://www.0pdd.com/p?name=yegor256/jping-maven-plugin)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.yegor256/jping-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.yegor256/jping-maven-plugin)
[![Javadoc](https://javadoc.io/badge/com.yegor256/jping-maven-plugin.svg)](http://www.javadoc.io/doc/com.yegor256/jping-maven-plugin)
[![codecov](https://codecov.io/gh/yegor256/jping-maven-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/yegor256/jping-maven-plugin)

Add it to your `pom.xml`:

```xml
<project>
  <build>
    <plugins>
      <plugin>
        <groupId>com.yegor256</groupId>
        <artifactId>jping-maven-plugin</artifactId>
        <version>0.0.0</version>
        <executions>
          <execution>
            <goals>
              <goal>jping</goal>
            </goals>
            <configuration>
              <fileName>${project.build.directory}/we-are-online.txt</fileName>
              <propertyName>we-are-online</propertyName>
              <propertyValue>true</propertyValue>
              <failWhenOffline>false</failWhenOffline>
              <url>https://www.google.com</url>
              <connectTimeout>1000</connectTimeout>
              <readTimeout>1000</readTimeout>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
```

It runs, by default, at the `initialize` phase and checks whether
the machine is connected to the Internet or not (by making a test
HTTP connection to the `url` specified). If the connection is alive,
the value of the `we-are-online` property will be set to `true`.
Otherwise, the property will not be set.

Also, if the machine is connected to the Internet, a file will be
created, as specified in the `fileName` configuration parameter. If
the machine is offline, the file will be deleted (if it exists).

## How to contribute?

Fork the repository, make changes, submit a pull request.
We promise to review your changes same day and apply to
the `master` branch, if they look correct.

Please run Maven build before submitting a pull request:

```
$ mvn clean install -Pqulice
```
