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
- Unified support for Third Party Mods like Curios/Trinkets or Jade/TheOneProbe

## How to make a mod with Balm

You can get started [using this template repository](https://github.com/TwelveIterationMods/balm-mod).
There is no documentation, but the template gives an overview of most features, and you can browse [Blay's other mods'
code](https://github.com/TwelveIterationMods) to learn how specific things are done.

## Why is Balm not Open Source?

Recently, other developers have started inquiring about or building upon Balm, and while I'm happy to facilitate others
using it, I think it's important to understand the implications of depending on Balm.

First and foremost, Balm is a library for my own set of mods, and I need it to be stable as such. I think it's great if
other developers want to use it, but you should be aware of the risks in using it over an open source project.

### Forks of Balm cannot be publicly released

The choice for Balm to remain All Rights Reserved is largely a pragmatic decision: If a fork of Balm were to
be published without necessary precautions (different package name, different mod id, sufficiently different name), it
would cause problems both for me on a technical level, and users in terms of confusion. Realistically, a serious fork
would make the necessary naming changes anyway, but the world of Minecraft Modding is unforgiving, and I've seen too
much happen in a decade of modding. 

In return this means that by using Balm, you risk the possibility of Balm being
discontinued without any recourse (which I wouldn't do, but anyone could say that!).

### Binaries of Balm itself cannot be freely redistributed

Being designated ARR also helps me fight against malicious re-uploads from bad actors, and improves moderation efforts
and file attribution on mod hosting platforms. Mod packs can't accidentally ship a custom or unattributed jar of
Balm, which in my experience happens more often than you'd think.

In return however this means that Balm cannot be
freely redistributed on third party platforms, and while it is both available on CurseForge and Modrinth (including
third party downloads / API access), you risk the possibility of Balm being removed from either platform, which would make
installation of your dependent mod more difficult for users (I'm not going to remove Balm from either platform, but
anyone could say that!).

### There is no team or developer community behind Balm

It also should be noted that while Balm does have 100m+ downloads, the vast majority of those are from my own mods. At
the moment, there's probably only ~5 other developers who also use Balm to develop their mods. There is no team or
community behind Balm, it's a one-person operation.

### Alternatives to Balm

If this is a deal-breaker for you (it would be for me), I recommend
using [Architectury](https://github.com/architectury/architectury)
instead, which is licensed under LGPL at the time of this writing and has a greater developer community around it.

Alternatively, roll your own solution
using [Jared's MultiLoader-Template](https://github.com/jaredlll08/MultiLoader-Template)
instead, which is what I did with Balm because I didn't want to depend on a third party that I realistically would not
be able to maintain myself (and the fact that I didn't want to deal with custom gradle plugins).

If you like to live on the edge, feel free to give Balm a try! I'm glad if it can be useful for others, and if it wasn't
directly tied to my livelihood and better licensing option for my needs were available, I'd be happy to consider them.

### Some soothing words for those who like to live dangerously

- I'm not retiring anytime soon, Minecraft Modding makes up a significant part of my income
- If I do retire, I'll license Balm under an open source license before I go
- If I die before I retire, I won't haunt you if you continue to maintain Balm
- I'm open to pull requests as long as the submissions are maintainable in the long term
    - If your submission does more than just wrapping and exposing existing APIs, it probably shouldn't be part of Balm
- You can use as much or as little of Balm as you like, it's really just a glorified ServiceLoader with a set of event
  mixins
- I usually update to the first pre-release and the first release candidate ASAP so that mod porting can begin
    - However, only full releases are published to mod hosting platforms unless you can convince me otherwise
- I avoid breaking changes mid-version unless absolutely necessary (after all, I have 20 mods running on Balm myself)
- I intend to support Fabric, NeoForge and Forge
    - However, I'm encouraging users to migrate away from Forge, and support for it may become more and more difficult
      as they continue to
      make [design](https://github.com/MinecraftForge/MinecraftForge/commit/587b684035e567e9f553ffab02a081a8b52ddb01) [choices](https://github.com/MinecraftForge/MinecraftForge/blob/8e0845d032e18b0999ec0221ba73cb4d7325ada7/src/main/java/net/minecraftforge/common/capabilities/RegisterCapabilitiesEvent.java#L21)
      that go against the nature of multi-loader contexts
- Third Party Downloads on CurseForge are enabled and Balm
  can [freely be used in mod packs](https://mods.twelveiterations.com/permissions/)

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

## Contributing

If you're interested in contributing to the mod, you can check
out [issues labelled as "help wanted"](https://github.com/TwelveIterationMods/Balm/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22).

When it comes to new features, it's best to confer with me first to ensure we share the same vision. You can join us
on [Discord](https://discord.gg/VAfZ2Nau6j) if you'd like to talk.

Contributions must be done through pull requests. I will not be able to accept translations, code or other assets
through any other channels.

