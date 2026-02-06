import com.android.build.api.variant.BuildConfigField
import java.io.FileInputStream
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
}

val secretsPropertiesFile = rootProject.file("secrets.properties")
val secretsProperties = Properties().apply {
    if (secretsPropertiesFile.exists()) {
        FileInputStream(secretsPropertiesFile).use { load(it) }
    }
}


android {
    namespace = "com.prometheus_service.midas"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.prometheus_service.midas"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    signingConfigs {
        create("release") { // Use create for named configurations
            keyAlias = secretsProperties["keyAlias"] as String?
            keyPassword = secretsProperties["keyPassword"] as String?
            storeFile = secretsProperties["storeFile"]?.let { rootProject.file(it as String) }
            storePassword = secretsProperties["storePassword"] as String?
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes {
        create("production") {
            buildConfigField("String", "BuildEnv", "\"P\"")
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            matchingFallbacks += listOf("release")
        }
        create("preproduction") {
            buildConfigField("String", "BuildEnv", "\"PP\"")
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            extensions.extraProperties["enableCrashlytics"] = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("debug")
        }
        create("uat") {
            buildConfigField("String", "BuildEnv", "\"U\"")
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            extensions.extraProperties["enableCrashlytics"] = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("debug")
            applicationIdSuffix = ".staging"
        }
    }

    flavorDimensions.add("operator")
    productFlavors {
        create("vn88") {
            applicationId = "com.prometheus_service.midas.vn88"
            dimension = "operator"
            buildConfigField(
                "String",
                "GoogleClientIdUAT",
                secretsProperties["VN88_WEB_CLIENT_ID_UAT"] as String? ?: "\"\""
            )
            buildConfigField(
                "String",
                "GoogleClientIdPROD",
                secretsProperties["VN88_WEB_CLIENT_ID_PROD"] as String? ?: "\"\""
            )
        }
    }

    androidComponents {
        beforeVariants(selector().all()) { variantBuilder ->
            if (variantBuilder.buildType == "release" || variantBuilder.buildType == "debug") {
                variantBuilder.enable = false
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.hilt.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Glide
    implementation(libs.compose)

    // Timber
    implementation(libs.timber)

    // Circle Progress View
    implementation(libs.android.spinkit)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Datastore
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)

    // Jetbrains
    implementation(libs.jetbrains.kotlinx.serialization.json)

    //google login
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}