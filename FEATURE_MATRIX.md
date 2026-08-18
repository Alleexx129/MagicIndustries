# Feature matrix

| Original 1.20.1 feature | 1.12.2 port status | Notes |
|---|---|---|
| Player mana capability | Ported | Key remains `magicindustries:properties`; value clamps 0..100 |
| Mana persistence | Ported | Capability NBT |
| Mana client sync | Ported | SimpleNetworkWrapper S2C sync on join/respawn/dimension + flask use |
| Mana HUD | Ported | Original textures, width 81, height 8, x=center-91, y offset -48/-58 with armor |
| Mana Flask | Ported | +4 mana, 15 ticks, stack 16, Empty Flask remainder |
| Empty Flask | Ported | Stack 64 |
| Sapphire Charm / Curios | Ported via Baubles | `BaubleType.CHARM`, Speed II (20 ticks, amplifier 1), remove Speed on unequip |
| Copper tool tier | Ported | level 2, uses 200, speed 5, damage bonus 5, enchantability 10; repairs with any `ingotCopper` |
| Copper armor | Ported | multiplier 10, defense 1/3/4/2, enchantability 12, iron equip sound, 0 toughness/KB resistance; repairs with any `ingotCopper` |
| Gem Saw | Ported | 100 durability, no repair, loses 1 durability as crafting remainder |
| Netherite Gemstone Saw | Ported | 500 durability, same remainder behavior |
| Filters | Ported | Copper 12, Gold 30, Netherite 167 durability |
| Heat Generator inventory | Ported | 3 backing slots; GUI exposes slot 0 exactly as source |
| Heat Generator energy | Ported | 69,000 FE, receive 0, extract 256 |
| Heat Generator fuel math | Ported | Log scaling and the source's shrink-before-burn-time behavior preserved |
| Lava bucket generation | Ported | 20,000 burn override, 12,500 FE, returns bucket |
| Heat Generator automation | Ported | top/horiz fuel insertion; bottom bucket-only extraction |
| Heat Generator GUI | Ported | Original texture, slot coordinates, arrow and energy bar positions |
| Mana Extractor inventory | Ported | 3 backing slots; GUI exposes filter + fluid-container slots; hidden output slot retained |
| Filter slot rules | Ported | OreDictionary analogue of `forge:mana_filters` |
| Water container slot | Ported | water bucket / bucket |
| Water tank | Ported | 64,000 mB |
| Mana tank | Ported | 100,000 mB physical capacity; GUI fill denominator stays 64,000 as in source renderer setup |
| Extractor operation | Ported | 500 mB water, 250 mB mana, 100 ticks, adjacent Mana Flower, 1 filter damage |
| Extractor sided automation | Ported | Source direction mapping and exposed side rules |
| Extractor fluid capability | Ported | Combined water-then-mana handler on every side |
| Extractor GUI | Ported | Original background, tanks, tooltips, help text and progress geometry |
| `gem_infusing` recipe type | Ported | Forge 1.12 JSON recipe factory representation |
| Built-in `gem_infusing` recipes | N/A in source | 1.5.2 ships none; extension point remains usable |
| JEI category | Ported | UID `magicindustries:gem_infusing`, original coordinates/catalyst/click area |
| Liquid Mana source/flow | Ported | 1.12 BlockFluidClassic translation |
| Liquid Mana bucket place/pickup | Ported | Custom ItemBucket + FillBucketEvent for source pickup |
| Liquid Mana tint | Ported | `0xFF00E7FF` |
| Liquid Mana fog color | Ported | 0.8784314 / 0.21960784 / 0.8156863 |
| Liquid Mana fog range | Ported | start 1, end 6 |
| Liquid Mana overlay | Ported | Original `in_mana_water.png` |
| Mana Flower | Ported | facing, full shape, no collision, 2 falling-water particles |
| Sapphire ore generation | Ported/height-translated | vein 2, one attempt/chunk, uniform -16..256; out-of-1.12-height samples skipped |
| Tungsten ore generation | Ported/height-translated | vein 9, count 20, uniform -64..72 |
| Mithril ore generation | Ported/height-translated | vein 6, count 14, trapezoid -32..64 |
| Deepslate variants | Compatibility translation | y < 16 uses deepslate variants |
| Sapphire drops/Fortune | Ported | 1 + uniform 0..fortune |
| Tungsten drops/Fortune | Ported | 2..4 rolls, uniform Fortune bonus per roll |
| Mithril ore drops | Ported | Drops itself |
| Crafting recipes | Ported | All shipped crafting IDs represented |
| Smelting recipes | Ported | Furnace recipes |
| Blasting recipes | Compatibility translation | 1.12 has no blast furnace; represented as furnace smelting |
| Smithing upgrade | Compatibility translation | 1.12 has no smithing table/templates; netherite saw upgrade is shapeless |
| Vanilla copper dependencies | Compatibility translation | Mod copper ore/ingot/block provide the missing 1.12 copper progression |
| Vanilla netherite dependency | Compatibility translation | Compatibility netherite ingot added for original netherite recipes |
| Farmer Mana Flower trade | Ported | level 4, 32 emeralds, max one use |
| Wandering Trader Mana Flower trade | Compatibility translation | 1.12 has no Wandering Trader; dedicated nitwit career gives 16-emerald one-use offer |
| Advancements | Ported | 1.12-compatible JSON criteria |
| Models/textures | Ported | All 54 original texture files byte-identical |

## Cross-mod compatibility layer

Recipes use Forge OreDictionary ingredients for copper, tungsten, mithril, netherite and sapphire where the source referenced interchangeable material concepts. The port also registers its own materials under those aliases, registers all three filters as `manaFilters`, and accepts third-party `ingotCopper` for copper tool/armor repair. Forge Energy and Forge Fluid capabilities remain the machine interoperability boundary.
