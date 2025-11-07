### User Changelog

- Fixed control key modifier not working on OSX

### Developer Changelog

- Added a `QuickMove` utility for declarative `quickMoveStack` implementations
- Added progress bar rendering utilities
- Added more flexible `modifyBiome` method not limited to adding one specific placed feature
- Added `UnpackedLootTableHolder` for accessing the loot table on a block entity during loot modifiers
- Added `NumberProvider` utility that works without a loot context for custom use cases
- Added `CustomChestMaterials` for easily adding chest material overrides on custom chest blocks
- Added `CustomMobEffect` with public constructors for convenience
- Added `SingleItemContainer` class for convenience
- Added `BlockEntityUtils`, deprecated `BalmBlockEntity`
- Added `isShiftDown`, `isControlDown` and `isAltDown` to `Balm.safeClientAccess()`, for use e.g. in tooltips 
- Added new Registrar classes for simpler and more stable registration of things into registries
  - This is backwards-compatible, but the old methods have been deprecated and will be removed in 1.21.11
  - This change will be backported to Minecraft 1.21.1, but probably not 1.20.1 unless there is demand.