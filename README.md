# Balm

Minecraft Library Mod. Abstraction Layer for Multiplatform Mods.

Balm is a library mod for mod developers that simplifies the process of creating multi-loader mods by providing common
interfaces and events and removing the need for most mod-loader specific code.

It is not a magic solution for running Forge and Fabric mods together. As a user, you only need to install this mod if
you use a mod that requires it.

## Key Points

- No custom Gradle tooling, official mod loader plugins only
- Uses Mojang Mappings, supports [Jared's MultiLoader-Template](https://github.com/jaredlll08/MultiLoader-Template)
- All-inclusive from networking to configs, no third party dependencies
- Battle-tested across Blay's 20+ mods ranging from content additions to quality of life utilities
- Supports NeoForge, Fabric and Forge without duplicate code
- Snapshots builds for pre-releases and release candidates
- Unified support for Third Party Mods like Curios/Trinkets or Jade/TheOneProbe
- Available on CurseForge and Modrinth (incl. Third Party Access) 

## How to make a mod with Balm

You can get started [using this template repository](https://github.com/TwelveIterationMods/balm-mod).
Documentation is [limited](https://balm.twelveiterations.com/docs/getting-started), but the template gives an overview on how to get started, and you can browse [Blay's other mods'
code](https://github.com/TwelveIterationMods) to learn how specific things are done. Join Blay and other developers using Balm on the [Balm Developers Discord](https://discord.gg/36qHFMNgAh) server.

#### Downloads

[![Versions](http://cf.way2muchnoise.eu/versions/531761_latest.svg)](https://www.curseforge.com/minecraft/mc-mods/balm)
[![Downloads](http://cf.way2muchnoise.eu/full_531761_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/balm)

## Adding Balm to a development environment

### Using CurseMaven

Add the following to your `build.gradle`:

```groovy
repositories {
    maven { url "https://www.cursemaven.com" }
}

dependencies {
    // Replace ${balm_file_id} with the id of the file you want to depend on.
    // You can find it in the URL of the file on CurseForge (e.g. 3914527).
    // NeoForge: implementation "curse.maven:balm-531761:${balm_file_id}"
    // Fabric (1.21.5+): modImplementation "curse.maven:balm-531761:${balm_file_id}"
    // Fabric (older versions): modImplementation "curse.maven:balm-fabric-500525:${balm_file_id}"
    // Forge: implementation "curse.maven:balm-531761:${balm_file_id}"
}
```

### Using Twelve Iterations Maven (includes snapshot versions)

Add the following to your `build.gradle`:

```groovy
repositories {
    maven {
        url "https://maven.twelveiterations.com/repository/maven-public/"

        content {
            includeGroup "net.blay09.mods"
        }
    }
}

dependencies {
    // Replace ${balm_version} with the version you want to depend on. 
    // You can find the latest version for a given Minecraft version at https://maven.twelveiterations.com/service/rest/repository/browse/maven-public/net/blay09/mods/balm-common/
    // Common (mojmap): implementation "net.blay09.mods:balm-common:${balm_version}"
    // NeoForge: implementation "net.blay09.mods:balm-neoforge:${balm_version}"
    // Fabric: modImplementation "net.blay09.mods:balm-fabric:${balm_version}"
    // Forge: implementation "net.blay09.mods:balm-forge:${balm_version}"
}
```

## License

This project is licensed under the Apache License 2.0, with the following exceptions:

- Forks published outside of GitHub must clearly rename the project as to not create confusion with the original version
  of Balm.
- The icon(s) for Balm are Copyright (c) 2025 BlayTheNinth, All Rights Reserved.
- Binary builds including these icon(s) are Copyright (c) 2025 BlayTheNinth, All Rights Reserved.

Additionally, it is strongly recommended that forks published outside of GitHub change the mod ID and package names to
avoid conflicts with the original version of Balm.

## Contributing

If you're interested in contributing to the mod, you can check
out [issues labelled as "help wanted"](https://github.com/TwelveIterationMods/Balm/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22).

When it comes to new features, it's best to confer with me first to ensure we share the same vision. You can join us
on [Discord](https://discord.gg/36qHFMNgAh) if you'd like to talk. Generally, Balm is designed to be a wrapper around existing mod and mod loader 
APIs and does not aim to provide its own systems beyond the core functionality (such as configs). Large emphasis is put 
on whether a feature is maintainable in the long-term and provides value to the average mod developer.

Contributions must be done through pull requests. I will not be able to accept translations, code or other assets
through any other channels.

