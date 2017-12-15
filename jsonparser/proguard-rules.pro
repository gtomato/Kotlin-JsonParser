# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/tszhin.lai/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class java.lang.*
-keep class org.jetbrains.*
-keep class kotlin.** { *; }
-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }
-keepnames @kotlin.Metadata class tomatobean.jsonparsere.**
-keepattributes *Annotation*
-keep class tomatobean.jsonparser.JsonFormatter
-keep class tomatobean.jsonparser.* {public *;}
-keep class tomatobean.jsonparser.ParserUtils.*
-dontwarn org.jetbrains.annotations.**
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-keepclassmembers class * {
    @tomatobean.jsonparser.JsonFormat <methods>;
}
-keepclasseswithmembers class tomatobean.jsonparser.JsonFormat.**{*;}

-keepclassmembers class tomatobean.jsonparser.** { *; }
