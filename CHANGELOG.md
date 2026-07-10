- Hotfixed crash on startup on Fabric

- API: Fixed `OpenScreenEvent` not supporting cancellation on Fabric
- API: Backported `BalmBlockRegistration#withItem()` and `BalmDiscriminatedBlockRegistration#withItems()` overloads with name function
- API: Backported `DiscriminatedBlocks#sortedValues()` etc.
- API: Backported `CustomMobEffect`, a simple wrapper to make the constructor public
- API: Backported `BalmModSupport#recipeViewers()` for unified recipe viewer support (only supports JEI at the moment)
- API: Backported `BalmRegistrars#poiTypes()` for registering custom POI types
- API: Added `BalmItemPropertyRegistrar` for registering custom item properties
- API: Added `Balm.biomeModifications()` alias for easier forward-compatibility when porting
