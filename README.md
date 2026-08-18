# Magic Industries 1.5.2 — Minecraft 1.12.2 Forge full source port

This project is a clean 1.12.2 rewrite of **MagicIndustries 1.5.2**, using the original 1.20.1 source and the supplied 1.5.2 JAR as the behavior/assets reference.

Target stack:
- Minecraft 1.12.2
- Forge 14.23.5.2859 (official recommended 1.12.2 build)
- Java 8
- Baubles 1.5.2 (Curios `charm` -> Baubles `CHARM`)
- JEI 4.16.1.302+

## Ported gameplay

- Player mana capability, 0..100 clamping, NBT persistence and client sync
- Mana HUD with the original textures, position, armor offset and fill math
- Mana Flask: 15 tick drink, +4 mana, stack size 16, Empty Flask remainder
- Sapphire Charm: Baubles charm slot, Speed II while worn, removes Speed on unequip
- Copper armor and full tool set with original tier/material stats and copper repair
- Diamond/Netherite Gemstone Saws with exact crafting-remainder durability behavior
- Copper/Gold/Netherite filters and exact durability
- Heat/Coal Generator: 69,000 FE capacity, 256 FE extraction, exact fuel/lava behavior, sided automation, GUI and NBT sync
- Mana Extractor: exact 3-slot backing inventory, 64,000 mB water tank, 100,000 mB mana tank, 500 water -> 250 mana, 100 tick filter operation, filter damage, Mana Flower adjacency, sided item automation, combined fluid capability, GUI and NBT sync
- Liquid Mana block/fluid, placement and pickup with the custom bucket, tint/fog/overlay behavior and translated 1.12 flow decay
- Mana Flower facing, full visual selection box, no collision and falling-water particles
- Sapphire/Tungsten/Mithril world generation translated from the exact shipped placed-feature distributions
- Deepslate ore variants translated to the bottom 16 blocks of the 1.12 world
- Original loot/drop + Fortune distributions
- Original crafting recipes, furnace translations of smelting/blasting, advancements and trades
- OreDictionary interoperability for copper/tungsten/mithril/netherite/sapphire materials, filters and cross-mod copper repair
- Original `gem_infusing` machine-recipe extension point and JEI category, even though 1.5.2 ships no built-in `gem_infusing` data recipe
- Original textures copied byte-for-byte (54/54 texture files matched by SHA-256 during the port audit)

See `FEATURE_MATRIX.md` for the full audit, `COMPATIBILITY_TRANSLATIONS.md` for mechanics that cannot literally exist in Minecraft 1.12.2, and `FIRST_RUNTIME_TEST.md` for the acceptance checklist.

## Important verification note

`tools/static_validate.py` passes **377/377** project checks and all 112 JSON files parse. This environment did not have a Java 8 + ForgeGradle 3 build toolchain or dependency network available, so this ZIP is **source-level/static validated, not claimed as runtime-proven**. See `VALIDATION.md` and `VALIDATION_REPORT.txt`.

## Build

Read `BUILDING.md`. `compile-local.bat` / `compile-local.sh` verify that JDK 8 is active and then run the official-MDK Gradle wrapper. A normal successful build outputs the mod JAR under `build/libs/`.
