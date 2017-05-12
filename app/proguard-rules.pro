# general
-dontobfuscate

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

# for NetworkMonkey
-keep class io.jasonatwood.networkmonkey.** { *; }
