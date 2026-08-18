# Source parity audit

This port was reconstructed against the MagicIndustries 1.5.2 source/JAR behavior and then translated onto Forge 1.12.2 APIs.

## Audited systems

- Registries, items, blocks, models, textures, language/resources.
- Player mana capability, NBT persistence, clone semantics, login/respawn/dimension sync, client HUD packet.
- Mana Flask consumption (+4, max 100), empty-flask remainder, stack sizes and use timing.
- Sapphire Charm as a Baubles CHARM with Speed II while equipped and effect removal on unequip.
- Mana Water fluid properties, bucket interaction, tint, fog/overlay behavior.
- Mana Extractor inventory, sided automation, water/mana tanks, filter durability, flower adjacency, exact 500 water -> 250 mana / 100-tick process, GUI sync/layout, recipe/JEI extension infrastructure.
- Coal/Heat Generator inventory, exact fuel/lava behavior, energy cap/extraction, sided automation, GUI sync.
- Copper equipment/material values, repair behavior, saw crafting remainders and durability.
- Ore/block drops and world generation translated from modern height placement into 1.12.2 world bounds.
- Villager trades translated for the lack of wandering traders.
- Crafting/smelting/advancement data moved to the Forge/Minecraft 1.12.2 resource format.
- Dedicated-server classloading separation for client-only GUI/render/HUD code.
- OreDictionary + Forge Energy/fluid/item capability interoperability.

## Verification status

`tools/static_validate.py` checks resource integrity, JSON syntax, expected parity signatures, model/texture references, client/server class separation, build metadata, OreDictionary integration, and a Java 8 syntax parse. This is a source/static audit, not a substitute for a real ForgeGradle compile and Minecraft runtime test.
