// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

buildscript {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven("https://jitpack.io") // 添加这行以便于引入JitPack上的库
        maven("https://maven.google.com") // 添加这行以便于引入Google的maven仓库
    }
}