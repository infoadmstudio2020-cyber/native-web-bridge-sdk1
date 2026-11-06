# Consumer ProGuard rules for NativeRuntimeSDK
-keep public class com.roni.library.sdk.** { public *; }
-keep interface com.roni.library.sdk.** { *; }
-keep enum com.roni.library.sdk.** { *; }
-keep class com.roni.library.sdk.bootstrap.** { *; }
-keep class com.roni.library.sdk.activity.** { *; }
-keep class com.roni.library.sdk.NativeRuntimeResult { *; }
-keep class com.roni.library.sdk.RuntimeConfiguration { *; }
-keep class com.roni.library.sdk.RuntimeSession { *; }
