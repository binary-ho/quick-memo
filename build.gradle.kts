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
    version.set("2023.3")  // 최신 버전으로 업데이트
    type.set("GO")  // GoLand IDE 지원
    plugins.set(listOf("org.jetbrains.plugins.go"))  // Go 플러그인 의존성 추가
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("243.*")  // 호환성 범위 확장
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
