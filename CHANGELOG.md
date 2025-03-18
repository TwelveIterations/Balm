- Removed outdated keymapping mixins as they never worked well and resulted in Balm being loaded too early with Essential
  - Shouldn't have any effect on Balm mods since all that depended on this already migrated to Kuma

---

- Added `BalmRegistries.register(...)` to allow registering objects into any registry
- Added `BalmParticles` to allow registering particles
- Added `BalmRenderers.registerParticleProvider(...)` to allow registering particle renderer providers
- Added `Balm.platformProxy().withForge(...).withFabric(...).build()` as a simple alternative to SPI for platform-specific implementations
- Added `Balm.modProxy().with(modId, className).withMultiplexer(...).withFallback(...)` for abstracting away mod compat under common interfaces
- Added `BalmPermissions` with support for Neo/Forge PermissionAPI and fabric-permissions-api
- Added `balm.command.balm.dev`, `balm.command.balm.export.config`, `balm.command.balm.export.icons` permission nodes
- Added crash when active config is unexpectedly set to null to avoid confusing errors later on
- Fixed `OnLoadHandler` not working on block entities in Fabric
- Fixed in-memory config potentially being reset to default on Forge instead of using data from config load event
- Fixed PoiTypes not registering properly on Fabric
- Fixed Kuma version pinning to avoid shipping snapshots
- Changed `balm export` commands to require op
- Changed mod id on Fabric to "balm" ("balm-fabric" is still provided for backwards compatibility)
- Deprecated most getters in BalmRegistries since Vanilla equivalents can be used nowadays