### User Changelog

- Fixed crashes on Forge
- Bumped network version (meaning client and server version must match) due to an unexpected protocol-breaking change in custom stats

### Developer Changelog

- Added support for `server`-type configurations
  - On Forge, uses type `ModConfig.Type.SERVER` and will be located in the `world/serverconfig/` folder. All properties are synced.
  - On NeoForge, uses type `ModConfig.Type.SERVER` and will be located in the `config` folder. May be overridden by placing a config file in `world/serverconfig/`. All properties are synced.
  - On Fabric, will be located in the `world/serverconfig/` folder. All properties are synced.
  - `@Synced` and `.synced()` are unaffected, they will continue to work on other types too as before