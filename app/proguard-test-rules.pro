-dontobfuscate
-dontnote **

-dontwarn org.hamcrest.**
-dontwarn android.test.**

-dontwarn android.support.test.**
-keep class android.support.test.** { *; }

-keep class junit.runner.** { *; }
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

-dontwarn java.time.LocalDate
-dontwarn java.time.LocalTime
-dontwarn java.time.LocalDateTime
-dontwarn java.time.ZonedDateTime
-dontwarn java.time.temporal.Temporal
-dontwarn java.time.temporal.TemporalUnit
-dontwarn java.time.OffsetTime
-dontwarn java.time.OffsetDateTime
-dontwarn java.time.Instant
-dontwarn java.time.format.DateTimeFormatter
-dontwarn java.io.File
-dontwarn org.apache.tools.ant.**
-dontwarn org.w3c.dom.bootstrap.**
-dontwarn org.apache.tools.ant.Project
-dontwarn org.assertj.core.internal.cglib.transform.AbstractProcessTask
-dontwarn org.assertj.core.internal.cglib.transform.AbstractTransformTask

-keep class org.threeten.bp.Duration { *; }
-dontwarn org.threeten.bp.Duration
-keep class org.threeten.bp.ZonedDateTime { *; }
-dontwarn org.threeten.bp.ZonedDateTime


# for RestMock
-dontwarn org.bouncycastle.x509.X509V3CertificateGenerator
-dontwarn org.bouncycastle.asn1.x509.X509Extensions
-dontwarn org.bouncycastle.asn1.x509.BasicConstraints
-dontwarn org.bouncycastle.asn1.ASN1Encodable
-dontwarn org.bouncycastle.asn1.x509.GeneralName
-dontwarn org.bouncycastle.asn1.DERSequence
-dontwarn org.bouncycastle.jce.provider.BouncyCastleProvider
