# Consumer ProGuard rules for NativeStorageEngine
-keep public class com.roni.library.storage.** { public *; }
-keep interface com.roni.library.storage.** { *; }
-keep class com.roni.library.storage.database.Document { *; }
-keep class com.roni.library.storage.NativeStorageEngineResult { *; }

