/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("dmplay.java.java-application-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-text")
    implementation(project(":utilities"))
}

application {
    // Define the main class for the application.
    mainClass.set("dmplay.java.app.App")
}
