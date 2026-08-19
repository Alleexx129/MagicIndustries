# Magic Industries 1.5.3 — NeoForge 1.21.1 port notes

## Target toolchain

- Minecraft: 1.21.1
- NeoForge: 21.1.248
- Java: 21
- ModDevGradle: 2.0.144
- Parchment: 1.21.1 / 2024.11.17
- Gradle wrapper: 9.2.1

## Loader/build migration

- Replaced ForgeGradle with NeoForge ModDevGradle.
- Replaced `META-INF/mods.toml` with generated `META-INF/neoforge.mods.toml` metadata.
- Removed the uploaded project's broken Git-worktree pointer, IDE caches, Gradle caches, old build output, and old `run/` directory from the deliverable.
- Kept Java 21 and Minecraft 1.21.1.

## Code migrations

- `net.minecraftforge.*` imports and Forge registry APIs migrated to `net.neoforged.*` / NeoForge registries.
- `RegistryObject`-style registrations migrated to NeoForge deferred holders/suppliers.
- Player mana capability/provider migrated to a serializable NeoForge attachment type.
- Block entity item, fluid, and energy exposure migrated to NeoForge capability registration through `RegisterCapabilitiesEvent`.
- Forge `SimpleChannel` networking replaced by NeoForge `CustomPacketPayload`, `StreamCodec`, `PayloadRegistrar`, and `PacketDistributor` payloads.
- Fluid synchronization and the custom recipe serializer use NeoForge `FluidStack` stream codecs rather than removed NBT helper methods.
- Menus migrated to `IMenuTypeExtension` / `IContainerFactory` with `RegistryFriendlyByteBuf`.
- GUI HUD registration migrated to `RegisterGuiLayersEvent`.
- Biome modifier registry/data paths and IDs migrated from Forge to NeoForge.
- Mod-owned item tags moved out of the old `forge` namespace to `magicindustries`.

## Small safety/behavior fixes made during the port

- Menu sync packets no longer blindly cast a `Player` to `ServerPlayer`.
- Coal generator burn time is captured before consuming fuel so generation does not depend on an already-emptied stack.
- Clientbound fluid packets support empty tanks with `FluidStack.OPTIONAL_STREAM_CODEC`.

## JEI

The existing JEI integration source files remain fully commented out. The base mod therefore does not require JEI to compile or run. The optional metadata entry is retained for compatibility.

## Verification limitation

A full Gradle compile could not run inside the porting sandbox because the environment could not resolve `services.gradle.org`, and no Gradle distribution/dependency cache was available locally. The failure occurred while trying to download the Gradle wrapper, before Java compilation started.

Static checks performed include:

- removed Forge package/API scan
- metadata/build-file review against NeoForge's official 1.21.1 MDK
- NeoForge networking/attachment/capability API migration review
- JSON resource parsing
- TOML metadata-template expansion/parsing
- ZIP integrity check

Run `gradlew.bat build` (Windows) or `./gradlew build` (Linux/macOS) locally as the first real compile verification.
