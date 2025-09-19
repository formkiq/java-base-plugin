# FormKiQ Gradle convention plugin

Reusable **Gradle convention plugin** for FormKiQ Java projects.


**Plugin ID:** `com.formkiq.gradle.java-base`

Features:
- Java Toolchain (17)
- Spotless (Java + Gradle files)
- SpotBugs (HTML reports, shared exclude filter)
- Checkstyle (10.12.4, project-relative config)
- Gradle Versions plugin
- GraalVM Native plugin (FormKiQ)
- Repositories: `mavenLocal`, `mavenCentral`, Sonatype snapshots (optional)

## Quick start

### Apply Gradle plugin

#### Groovy
Using the [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block):
```groovy
plugins {
    id 'com.formkiq.gradle.java-base' version '1.0.0'
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
    classpath "com.formkiq.gradle:java-base:1.0.0"
  }
}

apply plugin: "com.formkiq.gradle.java-base"
```

#### Kotlin
Using the [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block):
```kotlin
plugins {
    id("com.formkiq.gradle.java-base") version "1.0.0"
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
    classpath("com.formkiq.gradle:java-base:1.0.0")
  }
}

apply(plugin = "com.formkiq.gradle.java-base")
```