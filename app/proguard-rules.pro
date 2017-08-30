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
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.CheckReturnValue
-keep class javax.annotation.CheckReturnValue { *; }
-dontwarn javax.annotation.ParametersAreNonnullByDefault