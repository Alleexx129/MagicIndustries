# Validation status

## Completed in this environment
- Audited original GitHub source against the supplied `magicindustries-1.5.2.jar` behavior/resources.
- Checked exact machine constants, inventory rules, tank/energy capacities, trades, item stats, Charm effect, worldgen placement data and recipe IDs.
- Verified the port contains all 54 original texture paths and that all 54 files are byte-identical by SHA-256.
- Parsed all 112 JSON files successfully.
- Ran `tools/static_validate.py`: **377/377 checks passed**.
- Static validator checks for modern 1.20 API residue, brace/string/comment balance, important behavior signatures, required assets, local model/texture references, recipe coverage and dependency target strings, dedicated-server client-import isolation, exact machine GUI geometry, fluid tint/sprites, original NBT keys, OreDictionary recipes and cross-mod copper repair.

## Not completed here
A genuine Forge 1.12.2 compile/client/server boot was not possible in this execution environment because only JDK 21 was installed, no Gradle executable/JDK 8 was available, and process-level outbound dependency downloads were unavailable. The official Forge 14.23.5.2859 MDK itself is available upstream, but this runtime cannot use its wrapper to resolve ForgeGradle/Minecraft libraries.

That means this source is deliberately **not described as “guaranteed no bugs.”** Static validation materially reduces mistakes, but only a Java 8 ForgeGradle build and actual Minecraft client/server run can prove binary/API/runtime behavior.

## Recommended acceptance test after build
See `FIRST_RUNTIME_TEST.md` for the expanded checklist. Minimal pass:
1. Launch Forge 1.12.2 with only Magic Industries, Baubles and JEI.
2. Create a new world and verify all items/blocks appear in the Magic Industries creative tab.
3. Equip Sapphire Charm in Baubles Charm slot and verify Speed II/removal.
4. Drink Mana Flask and verify +4 HUD mana + Empty Flask.
5. Place/bucket Liquid Mana and verify tint, fog, overlay and flow.
6. Test Heat Generator with coal stacks and lava buckets; inspect FE extraction from every side.
7. Test Mana Extractor with each filter, water buckets, adjacent flower, hoppers/pipes on every side and fluid pipes.
8. Verify JEI category/catalyst/click area.
9. Generate fresh chunks and inspect ore distribution/variants.
10. Test all crafting/smelting recipes, advancements and villager trades.
