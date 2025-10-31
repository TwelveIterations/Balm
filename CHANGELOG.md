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