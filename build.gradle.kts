
allprojects {
    group = "com.codeheadsystems"
    version = "1.0.3-SNAPSHOT"

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
