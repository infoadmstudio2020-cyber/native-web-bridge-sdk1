# ProGuard / R8 rules for NativeWebBridgeSDK consumers
-keep class com.roni.library.bridge.** { *; }
-keep interface com.roni.library.bridge.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
