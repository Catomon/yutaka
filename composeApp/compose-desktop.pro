-dontwarn **

# Koin core
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# Koin scope functions
-keepclassmembers class org.koin.core.component.KoinComponent {
    *;
}

# Your app classes
-keep class com.github.catomon.tsukaremi.di.** { *; }

-keep class io.github.catomon.yutaka.ui.viewmodel.MainViewModel { *; }

# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$* Companion;
}

# Keep names for named companion object from obfuscation
# Names of a class and of a field are important in lookup of named companion in runtime
-keepnames @kotlinx.serialization.internal.NamedCompanion class *
-if @kotlinx.serialization.internal.NamedCompanion class *
-keepclassmembernames class * {
    static <1> *;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Don't print notes about potential mistakes or omissions in the configuration for kotlinx-serialization classes
# See also https://github.com/Kotlin/kotlinx.serialization/issues/1900
-dontnote kotlinx.serialization.**

# Serialization core uses `java.lang.ClassValue` for caching inside these specified classes.
# If there is no `java.lang.ClassValue` (for example, in Android), then R8/ProGuard will print a warning.
# However, since in this case they will not be used, we can disable these warnings
-dontwarn kotlinx.serialization.internal.ClassValueReferences

# disable optimisation for descriptor field because in some versions of ProGuard, optimization generates incorrect bytecode that causes a verification error
# see https://github.com/Kotlin/kotlinx.serialization/issues/2719
-keepclassmembers public class **$$serializer {
    private ** descriptor;
}

-keep class org.apache.commons.logging.impl.LogFactoryImpl { *; }

-keep class org.slf4j.** { *; }
-keep class org.slf4j.impl.** { *; }

# Keep specific logging implementation classes
-keep class org.apache.commons.logging.impl.Jdk14Logger { *; }
-keep class org.apache.commons.logging.impl.Jdk13LumberjackLogger { *; }
-keep class org.apache.commons.logging.impl.SimpleLog { *; }

# Keep all enum classes and their static methods
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep the EnumMap class
-keep class java.util.EnumMap { *; }

# Keep all classes in the framebody package
-keep class org.jaudiotagger.tag.id3.framebody.** { *; }

-keep class org.jaudiotagger.tag.datatype.** { *; }

# Keep coroutine classes and related metadata
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.coroutines.flow.** { *; }
-keepclassmembers class * {
    @kotlin.coroutines.jvm.internal.DebugMetadata *;
}
-keep class *$Continuation { *; }
-keep class kotlinx.coroutines.debug.internal.AgentPremain { *; }

#-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

-keep class okio.** { *; }
#-keep class coil3.** { *; }

-keep class * extends coil3.util.DecoderServiceLoaderTarget { *; }
-keep class * extends coil3.util.FetcherServiceLoaderTarget { *; }

#-dontobfuscate

# Keep all classes in com.sun.jna package and subpackages
-keep class com.sun.jna.** { *; }

# Keep all class members in classes that extend JNA classes
-keepclassmembers class * extends com.sun.jna.* { public *; }

# -------------------------- Sketch Privider ---------------------------- #
-keep class * implements com.github.panpf.sketch.util.DecoderProvider { *; }
-keep class * implements com.github.panpf.sketch.util.FetcherProvider { *; }

# Preserve Koin components
-keep class org.koin.** { *; }
-keepclassmembers class * {
    @org.koin.core.annotation.Module *;
    @org.koin.core.annotation.Single *;
    @org.koin.core.annotation.Factory *;
}

# Preserve Room components
-keep class * extends androidx.room.RoomDatabase { *; }
-keep class * extends androidx.room.Entity { *; }
-keep class * extends androidx.room.Dao { *; }
-keep class com.github.catomon.tsukaremi.data.local.AppDatabase_Impl { *; }
-keep class com.github.catomon.tsukaremi.data.local.AppDatabase { *; }

# Preserve ViewModels and dependencies
-keep class com.github.catomon.tsukaremi.ui.viewmodel.** { *; }
-keep class com.github.catomon.tsukaremi.domain.repository.** { *; }

# General Android rules
-keep public class * extends android.app.Application
-keep public class * extends androidx.lifecycle.ViewModel
-keepclassmembers class * {
    public <init>(...);
}