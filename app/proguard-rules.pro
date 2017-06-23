-dontobfuscate
-dontnote **

-dontwarn java.lang.invoke.*

# for Retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# for OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# for RxJava
-dontwarn sun.misc.Unsafe

# for android.content.res classes
-dontwarn org.xmlpull.v1.**

# for Butterknife
-dontwarn rx.functions.Func1

# Buddybuild specific
-keep class com.buddybuild.ui.view.** { *; }

-keep class org.threeten.bp.Duration { *; }

# For RxLifecycle
-dontwarn javax.annotation.Nonnull
-dontwarn javax.annotation.CheckReturnValue
-dontwarn javax.annotation.ParametersAreNonnullByDefault

# New Android Gradle Plugin (3.0) has some temp bugs. TODO revisit this
# read more:
# https://stackoverflow.com/questions/44215368/android-gradle-plugin-3-0-0-alpha2-error-inflating-class-android-support-v7-wid
-keep class android.arch.** { *; }
-keep class android.support.v7.widget.** { *; }
-keep class android.support.v4.widget.** { *; }
-keep class android.support.v4.view.** { *; }
-keep class android.support.design.** { *; }