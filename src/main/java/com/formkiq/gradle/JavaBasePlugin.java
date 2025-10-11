package com.formkiq.gradle;

import com.diffplug.gradle.spotless.SpotlessExtension;
import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep;
import com.github.spotbugs.snom.SpotBugsExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.quality.CheckstyleExtension;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.testing.Test;
import org.gradle.jvm.toolchain.JavaLanguageVersion;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link Plugin} for FormKiQ Gradle Conventions.
 */
public class JavaBasePlugin implements Plugin<Project> {

  /** Checkstyle Version. */
  private static final String CHECKSTYLE_TOOL_VERSION = "10.12.4";
  /** Java version. */
  private static final int JAVA_VERSION = 17;

  @Override
  public void apply(Project root) {
    root.getRepositories().mavenCentral();

    // configure root + all subprojects
    root.getAllprojects().forEach(p -> {
      // Apply plugins
      p.getPluginManager().apply("com.diffplug.spotless");
      p.getPluginManager().apply("java-library");
      p.getPluginManager().apply("checkstyle");
      p.getPluginManager().apply("com.github.spotbugs");
      p.getPluginManager().apply("com.github.ben-manes.versions");
      p.getPluginManager().apply("com.formkiq.gradle.graalvm-native-plugin");
      p.getPluginManager().apply("distribution");

      // Repositories
      p.getRepositories().mavenLocal();
      p.getRepositories().mavenCentral();
      p.getRepositories()
          .maven(repo -> repo.setUrl("https://central.sonatype.com/repository/maven-snapshots/"));

      // Java toolchain 17
      JavaPluginExtension java = p.getExtensions().getByType(JavaPluginExtension.class);
      java.getToolchain().getLanguageVersion().set(JavaLanguageVersion.of(JAVA_VERSION));

      // Spotless
      p.getExtensions().configure(SpotlessExtension.class, (SpotlessExtension s) -> {
        s.java(j -> {
          j.eclipse().sortMembersEnabled(true)
              .configFile(p.getRootProject().file("spotless.eclipseformat.xml"));
          j.removeUnusedImports();
          j.removeWildcardImports();
          j.licenseHeaderFile(p.getRootProject().file("LICENSE"));
        });
        s.groovyGradle(g -> {
          g.target("*.gradle", "**/*.gradle");
          g.greclipse();
          g.leadingTabsToSpaces(2);
          g.trimTrailingWhitespace();
          g.endWithNewline();
        });
        s.json(j -> {
          j.target("*.json", "**/*.json");
          j.prettier();
        });

        s.format("xml", f -> {
          f.target("*.xml", "**/*.xml");
          f.eclipseWtp(EclipseWtpFormatterStep.XML);
          f.trimTrailingWhitespace();
          f.endWithNewline();
        });
      });

      // SpotBugs
      p.getExtensions().configure(SpotBugsExtension.class, sb -> sb.getExcludeFilter()
          .set(p.file(p.getRootDir() + "/config/gradle/spotbugs-exclude.xml")));

      p.getTasks().withType(com.github.spotbugs.snom.SpotBugsTask.class).configureEach(t -> {
        if (t.getReports().findByName("html") == null) {
          t.getReports().create("html");
        }

        t.getReports().getByName("html").getRequired().set(true);
        if (t.getReports().findByName("xml") != null) {
          t.getReports().getByName("xml").getRequired().set(false);
        }
      });

      // Checkstyle
      p.getExtensions().configure(CheckstyleExtension.class, cs -> {
        cs.setToolVersion(CHECKSTYLE_TOOL_VERSION);
        cs.setConfigFile(p.file("config/checkstyle/checkstyle.xml"));
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("project_loc", p.getProjectDir());
        cs.setConfigProperties(props);
        cs.setMaxWarnings(0);
        cs.setMaxErrors(0);
      });

      // Compiler flags
      p.getTasks().withType(JavaCompile.class)
          .configureEach(jc -> jc.getOptions().getCompilerArgs().add("-Xlint:deprecation"));

      // Tests
      p.getTasks().withType(Test.class).configureEach(t -> {
        t.useJUnitPlatform();
        t.setFailFast(true);
      });

      p.afterEvaluate(prj -> {
        if (!prj.file("config/checkstyle/checkstyle.xml").exists()) {
          prj.getLogger().warn("Checkstyle config not found at config/checkstyle/checkstyle.xml");
        }
      });
    });

  }
}
