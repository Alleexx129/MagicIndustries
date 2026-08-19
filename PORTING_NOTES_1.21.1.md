# Magic Industries — Forge 1.21.1 port notes

## Target

- Minecraft: 1.21.1
- Forge: 52.1.16
- Java: 21
- ForgeGradle: 7.x
- Gradle wrapper target: 9.6.0

## Intentionally removed

- Sapphire Charm item and its item model/texture/recipe/advancement references
- Curios dependency and Curios data files

The normal Sapphire item, Sapphire ores, Sapphire block, Cut Sapphire, and their worldgen/recipes are still present.

## Main compatibility changes

- Updated ForgeGradle/build settings for the 1.21.1 Forge toolchain.
- Updated tool and armor APIs for Minecraft 1.21.1.
- Updated block interaction/menu opening APIs.
- Updated block entity NBT save/load signatures and item-handler serialization.
- Updated custom recipe serializer to MapCodec/StreamCodec and RecipeInput-era APIs.
- Updated Forge networking packages/context APIs used by the existing SimpleChannel packet code.
- Updated HUD registration to Forge's layered GUI API.
- Updated rendering code for the 1.21 vertex/buffer API.
- Updated ResourceLocation construction and datapack directory names for 1.21.
- Updated recipe result ItemStack JSON and affected advancement predicates/icons.
- Updated BootstrapContext naming and FlowerBlock suspicious-stew effects constructor.

## JEI

The existing JEI integration source is still kept in the project in its old commented form. The optional JEI dependency metadata/build entries target JEI 19.44.0.403 for Forge 1.21.1.

## IntelliJ IDEA

1. Extract the source ZIP.
2. Open the extracted project folder (or `build.gradle`) in IntelliJ IDEA as a Gradle project.
3. Set the Project SDK / Gradle JVM to Java 21.
4. Let Gradle import and download Forge/Minecraft dependencies.
5. If IntelliJ run configurations are not generated automatically, run `./gradlew genIntellijRuns` (`gradlew.bat genIntellijRuns` on Windows).
6. Run `./gradlew build` (`gradlew.bat build` on Windows) as the first full compile check.

## Verification limitation in the porting environment

The porting sandbox has Java 21, but its shell cannot resolve external Gradle hosts (`services.gradle.org`). Because the Gradle distribution and Forge/Minecraft dependencies cannot be downloaded there, a complete Gradle compile could not be executed in that environment.

Static/API migration checks were performed instead, along with JSON/TOML parsing, removed-content scans, and ZIP integrity checks. Your local IntelliJ/Gradle import is therefore the first real dependency-resolved compile and may expose a small remaining API edge case that static checking cannot prove away.
