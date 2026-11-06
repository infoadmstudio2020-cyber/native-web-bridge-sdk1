# Consumer ProGuard rules for NativeRuntimeContracts
# Preserves all public contracts, interfaces, DTOs, enums, and listeners across all sub-packages

-keep public class com.roni.library.contracts.** {
    public *;
}

-keep public interface com.roni.library.contracts.** {
    public *;
}

# Preserve Serializable DTOs and serialVersionUID
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preserve Builder patterns
-keepclassmembers class com.roni.library.contracts.**$Builder {
    public *;
}

