# Pearl Stasis Fix

A temporary Fabric fix for a vanilla Minecraft bug (versions 26.1.2 / 26.2) that causes
Ender Pearls in stasis chambers to duplicate or respawn whenever the server restarts.
Coded by Claude with Sonnet 5, so all credit to Claude :)
Claude even generated this file that you are currently reading now.

## The Bug

Since version 26.1.2, a bug acknowledged by Paper developers
([Paper#13964](https://github.com/PaperMC/Paper/issues/13964)) causes the server
to retain a reference in the player's data to every Ender Pearl currently in "stasis"
(suspended in mid-air—a delayed teleportation technique). This reference is never
cleared when the actual pearl is removed from the world; consequently, the pearl
reappears upon reconnection or server restart, even after manual deletion.

The mechanism involved is vanilla (`ServerPlayer.enderPearls`, a
`Set<ThrownEnderpearl>`), so the bug affects Paper, Fabric, and vanilla alike.

## The Fix

A single mixin (`ServerPlayerPearlMixin`) hooks into `ServerPlayer#tick()` and,
on every tick, removes any pearl from the `Set` for which `Entity#isRemoved()`
returns true. Once removed, it is no longer saved in the player's data and
cannot be recreated during the next restart.

This mod is **server-side only**; there is no need to install it on the client.

## Temporary Fix

This bug is known and will likely be officially fixed by Mojang at some point.
Please uninstall this mod once that happens.

## Building

Prerequisite: JDK 25.

```
./gradlew build
```

The compiled `.jar` file will be located in `build/libs/`.

## Installation

1. Fabric Loader 0.19.3 or higher.
2. Place the `.jar` file in the server's `mods/` folder.
3. No configuration required. ## License

MIT - see [LICENSE](LICENSE).
