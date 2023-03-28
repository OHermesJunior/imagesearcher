# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

-keep class * extends android.webkit.WebChromeClient { *; }
-dontwarn im.delight.android.webview.**

# shink app but do not obfuscate names to avoid unreadable crash stack
-keepnames class ** { *; }
-keepnames interface ** { *; }
-keepnames enum ** { *; }
