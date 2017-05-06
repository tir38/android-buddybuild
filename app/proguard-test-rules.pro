-dontobfuscate

-dontwarn

-dontwarn org.hamcrest.**
-dontwarn android.test.**

-dontwarn android.support.test.**
-keep class android.support.test.** { *; }

-keep class junit.runner.** { *; }
-keep class junit.framework.** { *; }
-keep class org.jmock.core.** { *; }
-keep class org.easymock.** { *; }

-dontwarn com.fasterxml.jackson.databind.**
-dontwarn com.fasterxml.jackson.core.**
-dontwarn com.fasterxml.jackson.annotation.**
-dontwarn org.ietf.jgss.**
-dontwarn javax.xml.**
-dontwarn javax.swing.**
-dontwarn javax.lang.**
-dontwarn java.nio.**
-dontwarn java.lang.**
-dontwarn org.w3c.dom.traversal.**
-dontwarn org.eclipse.jetty.**
-dontwarn java.beans.**
-dontwarn org.slf4j.**
-dontwarn org.apache.http.**
-dontwarn sun.misc.Unsafe
