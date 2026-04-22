- Added automatic migration attempt of Balm-specific data between NeoForge/Forge/Fabric
- Fixed server- or client-side optional mods still trying to send packets unknown to the remote, resulting in disconnects
- Fixed crash when trying to send a packet to a server while not connected on Fabric
- Fixed blank values sometimes being appended to config lists on Fabric
- API: Fixed reload listeners on NeoForge not having access to tags
- API: Fixed invalid config errors on Neo/Forge when a property's comment is empty

