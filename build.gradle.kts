plugins {
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}
nexusPublishing {
    repositories {
        sonatype()
    }
}

allprojects {
    group = "com.codeheadsystems"
    version = "1.0.4-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()  // was jcenter() which is dying
        google()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/releases/")
        maven("https://s3-us-west-2.amazonaws.com/dynamodb-local/release/")
    }


}
