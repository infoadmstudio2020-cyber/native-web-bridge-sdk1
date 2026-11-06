# Changelog

All notable changes to NativeRuntimeSDK are documented here.
Format based on [Keep a Changelog](https://keepachangelog.com).

## [1.0.1] — 2026-11-02

### Added
- Complete Master Turnkey Facade: `NativeRuntime`.
- Coordinated 10-Step Bootstrapper (`NativeRuntimeBootstrap`) covering Layers L0 through L4.
- Immutable configuration builder (`RuntimeConfiguration`) and fluent runner (`RuntimeBuilder`).
- Runtime session tracking state machine (`RuntimeSession`) and lifecycle observer (`LifecycleObserver`).
- Standard host Activity (`NativeRuntimeActivity`) with automatic lifecycle binding.
- Standardized error-handling envelope (`NativeRuntimeResult`).
- Comprehensive companion demonstration app with Master Dashboard, 10-Step Visualizer, RPC Command Center, Test Center, and Live Console.
- Strict ProGuard/R8 consumer preservation rules in `consumer-rules.pro`.
- Robust GitHub Actions CI and Release workflows.
