- Updated for Breaking Changes in NeoForge 26.1.2.20-beta

- API: Added `BalmClient.clientHooks()` with a `getFocusedElement()` helper
- API: Added `BalmResourceReloadListenerRegistrar#addDependency` and `BalmClientResourceReloadListenerRegistrar#addDependency`, not supported on Forge
- API: Added `BalmResourceReloadListenerRegistrar#vanillaKeys` and `BalmClientResourceReloadListenerRegistrar#vanillaKeys` for use in `addDependency`
- API: Added `lootTableId` parameter to `BalmLootModifier`
- API: Added `BalmCompostableRegistrar`
- API: Added overloads to set texture size for JEI support
