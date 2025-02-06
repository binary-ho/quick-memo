plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.16.1"
}

group = "com.quickmemo"

fun parseVersionFromPlugin(): String {
    val pluginXml = file("src/main/resources/META-INF/plugin.xml")
    val pattern = "<version>([^<]+)</version>".toRegex()
    val match = pattern.find(pluginXml.readText())
    return match?.groupValues?.get(1) ?: throw GradleException("Version not found in plugin.xml")
}

version = parseVersionFromPlugin()

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

intellij {
    version.set("2022.3")
    type.set("IC")
    plugins.set(listOf("com.intellij.platform.images"))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.encoding = "UTF-8"
    }

    patchPluginXml {
        sinceBuild.set("223")
        untilBuild.set("251.*")
        version.set(project.version.toString())
    }

    runIde {
        systemProperty("idea.config.path", "${layout.buildDirectory}/idea-sandbox/config-test")
        systemProperty("idea.system.path", "${layout.buildDirectory}/idea-sandbox/system-test")
        systemProperty("idea.plugins.path", "${layout.buildDirectory}/idea-sandbox/plugins-test")
        systemProperty("idea.log.path", "${layout.buildDirectory}/idea-sandbox/log-test")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
