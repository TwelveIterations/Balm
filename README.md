# Balm

Minecraft Mod. Abstraction Layer (but not really)™ for Blay's multiplatform mods.

This is a library mod that allows Blay's mods to be built for both Forge and Fabric without needing separate codebases.

- [Modpack Permissions](https://mods.twelveiterations.com/permissions)

#### Forge

[![Versions](http://cf.way2muchnoise.eu/versions/531761_latest.svg)](https://www.curseforge.com/minecraft/mc-mods/balm)
[![Downloads](http://cf.way2muchnoise.eu/full_531761_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/balm)

#### Fabric

[![Versions](http://cf.way2muchnoise.eu/versions/500525_latest.svg)](https://www.curseforge.com/minecraft/mc-mods/balm-fabric)
[![Downloads](http://cf.way2muchnoise.eu/full_500525_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/balm-fabric)

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
    // Forge: implementation fg.deobf("curse.maven:balm-531761:${balm_file_id}")
    // Fabric: modImplementation "curse.maven:balm-fabric-500525:${balm_file_id}"
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
    // You may also have to change the Minecraft version in the artifact name.
    // You can find the latest version for a given Minecraft version at https://maven.twelveiterations.com/service/rest/repository/browse/maven-public/net/blay09/mods/balm-common/
    // Common (mojmap): implementation "net.blay09.mods:balm-common:${balm_version}"
    // Forge: implementation fg.deobf("net.blay09.mods:balm-forge:${balm_version}")
    // Fabric: modImplementation "net.blay09.mods:balm-fabric:${balm_version}"
}
```

## Contributing

If you're interested in contributing to the mod, you can check out [issues labelled as "help wanted"](https://github.com/TwelveIterations/Balm/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22). 

When it comes to new features, it's best to confer with me first to ensure we share the same vision. You can join us on [Discord](https://discord.gg/VAfZ2Nau6j) if you'd like to talk.

Contributions must be done through pull requests. I will not be able to accept translations, code or other assets through any other channels.

