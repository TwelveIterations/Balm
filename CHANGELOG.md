- Added automatic migration attempt of Balm-specific data between NeoForge/Forge/Fabric
- Fixed server- or client-side optional mods still trying to send packets unknown to the remote, resulting in disconnects
- Fixed crash when trying to send a packet to a server while not connected on Fabric
- Fixed blank values sometimes being appended to config lists on Fabric
- API: Fixed reload listeners on NeoForge not having access to tags
- API: Fixed invalid config errors on Neo/Forge when a property's comment is empty
> 30b5a41 chore: Update URLs
> 607e776 refactor: Remove obsolete i18n keys
> d575363 feat: Backport isMessageSupported guards to prevent disconnects when sending packets unknown to the remote (client/server-optional mods)
> 2a3d189 fix: Use event.getServerResources().getRegistryLookup() for reload listeners so that tags are available in it
> dc5ca0e fix: Don't try to set empty comments into NightConfig spec
> 4ec8ecc fix: Fix crash when calling sendToServer while not ingame on Fabric
> 324adc9 fix: Fix mod icon
> db20c5b feat: Automatically try to migrate BalmData between NeoForge/Forge/Fabric #105
> 22f1335 build: Update Kuma
> d3cbada fix: Only emit values after trailing commas if it's not just blank #99
> af020e6 Merge remote-tracking branch 'origin/1.21.1' into 1.21.1
> 1936093 lang(pt_br): Update Brazilian Portuguese Translation (#234)
> 2746ee2 build: Remove Gradle capability stuff that breaks whenever you breathe wrong
> 565fd08 refactor(1.21.1): Some more backwards-compatible soft deprecations for future version renames to reduce docs discrepancies
> d715492 build: Properly expose Kuma as api on 1.21.1 common, forge and neoforge too
> f2b5b52 ci: Update workflows to run in new org
