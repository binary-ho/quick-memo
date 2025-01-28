plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.16.1"
}

group = "com.quickmemo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

intellij {
    version.set("2023.3")
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
        sinceBuild.set("233")
        untilBuild.set("242.*")
    }

    runIde {
        systemProperty("idea.config.path", "${buildDir}/idea-sandbox/config-test")
        systemProperty("idea.system.path", "${buildDir}/idea-sandbox/system-test")
        systemProperty("idea.plugins.path", "${buildDir}/idea-sandbox/plugins-test")
        systemProperty("idea.log.path", "${buildDir}/idea-sandbox/log-test")
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
