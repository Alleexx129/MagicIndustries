# MagicIndustries Forge 1.21.11 Port Notes

Target:
- Minecraft 1.21.11
- Forge 61.2.0
- Java 21
- ForgeGradle 7
- Gradle wrapper 9.3.0

## Kept from the 1.21.1 port
- Original `net.duodevs.magicindustries` package layout and general code style.
- Sapphire Charm and Curios remain removed. Normal sapphire items, blocks, ores, recipes, and worldgen remain.
- Mana Extractor water-bucket handling uses the direct vanilla bucket path: a water bucket fills 1000 mB if the tank has room and becomes an empty bucket.
- Existing JEI integration source files are kept as comments, but JEI is not a required dependency.

## Main 1.21.11 changes
- Forge 61 typed event-bus registration and current mod bus group setup.
- `ResourceLocation` usages migrated to `Identifier` where active.
- Block/item properties receive registry IDs.
- Block-entity NBT persistence uses `ValueInput` / `ValueOutput`.
- HUD uses Forge's current GUI layer system.
- GUI rendering uses the current `GuiGraphics` / `RenderPipelines` paths.
- Item model definition wrappers added under `assets/magicindustries/items`.
- Copper worn-armor equipment definition and new equipment texture paths added.
- Recipes migrated to the post-1.21.2 ingredient format.
- Villager/wandering-trader APIs updated.
- Gem Saw crafting remainder updated to the current Forge hook.
- Coal Generator fuel lookup updated to `FuelValues`.
- Mana Extractor custom recipe codec updated to current `Ingredient.CODEC`.
- Mana Flask is now a normal Item with a Consumable component because `HoneyBottleItem` is gone; it keeps the old 15-tick drink time, mana reward, and empty-flask result.
- Copper sword/pickaxe/armor migrated from removed item subclasses to the 1.21.11 property/material APIs.
- Copper tool/armor repair is backed by `magicindustries:copper_repair`, containing the mod's copper ingot.
- `BlockEntityType.Builder` migrated to the current direct constructor.
- Facing properties use `EnumProperty<Direction>` because `DirectionProperty` is gone.
- Old `pack.mcmeta` removed to follow the current Forge 1.21.11 MDK resource layout.

## IntelliJ IDEA
1. Extract the ZIP.
2. Open the extracted project folder in IntelliJ IDEA as a Gradle project.
3. Use JDK 21 for Gradle and the project SDK.
4. Let the Gradle wrapper download/sync dependencies.
5. If Forge run configurations do not appear, run `./gradlew genIntellijRuns` (Windows: `gradlew genIntellijRuns`).
6. Run `./gradlew build` as the final compile verification.

## Sandbox verification limitation
This environment has Java 21, but it cannot resolve `services.gradle.org`, so the Gradle wrapper cannot download Gradle 9.3.0 here. A real Forge compile could therefore not be completed in this sandbox. The project was instead checked against the official Minecraft 1.21.11 mappings and Forge 1.21.11 sources, plus local JSON/TOML/resource/archive validation.
