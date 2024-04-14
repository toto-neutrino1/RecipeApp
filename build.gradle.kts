// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val navVersion = "2.7.7"
    val kotlinVersion = "1.9.10"
    val kspVersion = "1.0.13"

    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("androidx.navigation.safeargs.kotlin") version navVersion apply false
    id("com.google.devtools.ksp") version "$kotlinVersion-$kspVersion" apply false
}