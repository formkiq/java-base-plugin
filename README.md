# FormKiQ Gradle convention plugin

Reusable **Gradle convention plugin** for FormKiQ Java projects.


**Plugin ID:** `com.formkiq.gradle.java-base`

Build with JDK 25 and the included Gradle 9.8.2 wrapper (`./gradlew`). Projects applying
this plugin require JDK 25 and Gradle 9.1.0 or newer.

Features:
- Configurable Java toolchain (defaults to 25)
- Spotless (Java formatting + local whitespace checks for Gradle files)
- SpotBugs (HTML reports, shared exclude filter)
- Checkstyle (10.12.4, project-relative config)
- Gradle Versions plugin
- OWASP Dependency Check conventions
- GraalVM Native plugin (FormKiQ)
- Repositories: `mavenLocal`, `mavenCentral`, Sonatype snapshots (optional)

Gradle files use local checks for two-space tab expansion, trailing whitespace, and a final newline.
They do not use Groovy-Eclipse or contact Eclipse update sites. Full Groovy indentation and wrapping
are no longer enforced. Java and XML formatting are unchanged.

## Quick start

### Set the JVM version

Set `jvmVersion` in your project's `gradle.properties` to choose the Java toolchain
used for compilation and tests in the root project and all subprojects:

```properties
jvmVersion=21
```

You can also override it for a single build:

```shell
./gradlew build -PjvmVersion=21
```

The default is Java 25. This setting selects the project toolchain; running Gradle
and loading this plugin still require JDK 25.

### Apply Gradle plugin

#### Groovy
Using the [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block):
```groovy
plugins {
    id 'com.formkiq.gradle.java-base' version '1.0.3'
}
```

Using [legacy plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application):
```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.formkiq.gradle:java-base:1.0.3"
  }
}

apply plugin: "com.formkiq.gradle.java-base"
```

#### Kotlin
Using the [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block):
```kotlin
plugins {
    id("com.formkiq.gradle.java-base") version "1.0.3"
}
```

Using [legacy plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application):
```kotlin
buildscript {
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
  }
  dependencies {
    classpath("com.formkiq.gradle:java-base:1.0.3")
  }
}

apply(plugin = "com.formkiq.gradle.java-base")
```
