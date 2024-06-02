
plugins {
    `java-library`
    `maven-publish`
    signing
    checkstyle
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    withJavadocJar()
    withSourcesJar()
}
val dynamodb by configurations.creating

dependencies {
    // DynamoDB testing
    dynamodb(fileTree("lib") { include(listOf("*.dylib", "*.so", "*.dll")) })
    dynamodb(libs.aws.dynamodblocal)
    implementation(project(":oop-mock-common"))
    implementation(project(":oop-mock-client"))
    implementation(libs.slf4j.api)
    implementation(libs.bundles.jackson)
    implementation(libs.guava)
    implementation(libs.commons.io)
    implementation(libs.aws.sdk.ddb)

    implementation(libs.dagger)
    annotationProcessor(libs.dagger.compiler)
    implementation(libs.immutable.annotations)
    annotationProcessor(libs.immutable )

    testAnnotationProcessor(libs.immutable)
    testImplementation(libs.bundles.testing)
    testImplementation(libs.bundles.logback)
    testAnnotationProcessor(libs.dagger.compiler)
    testImplementation(project(":oop-mock-tests"))
    testImplementation(libs.codehead.test)
    testImplementation(libs.database.test)
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

tasks.register("copyNativeDeps", Copy::class.java) {
    from(configurations.runtimeClasspath.get() + configurations.testRuntimeClasspath.get()) {
        include("*.dll", "*.dylib", "*.so")
    }.into("build/libs")
}

tasks.named<Test>("test") {
    dependsOn("copyNativeDeps")
    systemProperty("java.library.path", "build/libs")
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "oop-mock-dynamodb"
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
                name = "Oop-Mock DynamoDB"
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

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

tasks.publish {
    dependsOn("copyNativeDeps")
}

tasks.getByName("generateMetadataFileForMavenJavaPublication") {
    dependsOn("copyNativeDeps")
}