package com.ddd.attendance

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import kotlin.getOrThrow
import kotlin.onFailure
import kotlin.recoverCatching
import kotlin.runCatching

/**
 * Application 모듈의 Android Gradle Extension(ApplicationExtension)을 가져오는 확장 프로퍼티
 **/
internal val Project.applicationExtension: CommonExtension<*, *, *, *, *, *>
    get() = extensions.getByType<ApplicationExtension>()

/**
 * Library 모듈의 Android Gradle Extension(LibraryExtension)을 가져오는 확장 프로퍼티
 **/
internal val Project.libraryExtension: CommonExtension<*, *, *, *, *, *>
    get() = extensions.getByType<LibraryExtension>()

/**
 * 현재 Project가 라이브러리인지 앱인지 판단하여 적절한 Android Extension을 반환
 **/
internal val Project.androidExtension: CommonExtension<*, *, *, *, *, *>
    get() = runCatching { libraryExtension }
        .recoverCatching { applicationExtension }
        .onFailure { println("Could not find Library or Application extension from this project") }
        .getOrThrow()

/**
 * Version Catalog(libs.versions.toml)를 ExtensionContainer에서 가져오는 확장 프로퍼티
 **/
internal val ExtensionContainer.libs: VersionCatalog
    get() = getByType<VersionCatalogsExtension>().named("libs")

/**
 * Version Catalog에서 특정 라이브러리 의존성을 Provider 형태로 꺼내는 함수
 **/
internal fun Project.findLibrary(name: String): Provider<MinimalExternalModuleDependency> =
    extensions.libs.findLibrary(name).get()

/**
 * Version Catalog에서 특정 버전 문자열을 가져오는 함수
 **/
internal fun Project.findVersion(name: String): String =
    extensions.libs.findVersion(name).get().requiredVersion

/**
 * Version Catalog에서 의존성 번들을 Provider 형태로 가져오는 함수
 **/
internal fun Project.findBundle(name: String): Provider<ExternalModuleDependencyBundle> =
    extensions.libs.findBundle(name).get()
