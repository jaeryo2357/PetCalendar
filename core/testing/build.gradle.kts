plugins {
    alias(libs.plugins.pet.android.library)
}

android {
    namespace = "com.minuk.pet.core.testing"
}

dependencies {
    testApi(libs.junit)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)
}