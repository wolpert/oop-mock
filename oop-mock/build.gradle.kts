

plugins {
    `java-library`
    `maven-publish`
    signing
    checkstyle
}

java {
    toolchain {
        languageVersion =   JavaLanguageVersion.of(17)
    }
    withJavadocJar()
    withSourcesJar()
}
dependencies {
    implementation(project(":oop-mock-common"))
    implementation(libs.slf4j.api)
    implementation(libs.bundles.jackson)
    implementation(libs.guava)
    implementation(libs.commons.io)

    implementation(libs.dagger)
    annotationProcessor(libs.dagger.compiler)
    implementation(libs.immutable.annotations)
    annotationProcessor(libs.immutable )

    testAnnotationProcessor(libs.immutable)
    testImplementation(libs.bundles.testing)
    testImplementation(libs.bundles.logback)
    testAnnotationProcessor(libs.dagger.compiler)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "oop-mock"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name = "Oop-Mock"
                description = "Out of process Mocking"
                url = "https://github.com/wolpert/oop-mock"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "wolpert"
                        name = "Ned Wolpert"
                        email = "ned.wolpert@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/wolpert/oop-mock.git"
                    developerConnection = "scm:git:ssh://github.com/wolpert/oop-mock.git"
                    url = "https://github.com/wolpert/oop-mock/"
                }
            }

        }
    }
    repositories {
        maven {
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            name = "ossrh"
            credentials(PasswordCredentials::class)
        }
    }
}
signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}
