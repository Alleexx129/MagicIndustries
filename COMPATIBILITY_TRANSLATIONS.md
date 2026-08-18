# 1.20.1 -> 1.12.2 compatibility translations

A literal byte-for-byte gameplay implementation is impossible where vanilla 1.12.2 has no corresponding system. These are the deliberate translations; other gameplay code is kept at the original values/logic.

## Curios -> Baubles
The original `curios:charm` slot is `BaubleType.CHARM` in Baubles 1.5.2. The Sapphire Charm still applies Speed II every tick and removes Speed when unequipped.

## World height and deepslate
1.20.1 ore placement can select negative Y and uses deepslate target rules. 1.12.2 only has Y=0..255 and no deepslate terrain. The port samples the original distributions; samples outside 0..255 are skipped, and valid samples below Y=16 use the shipped deepslate ore variants.

## Copper
1.20.1 recipes reference vanilla copper, which does not exist in 1.12.2. The port maps those references to Magic Industries copper and adds a compatibility copper ore source so the progression is obtainable without another mod.

## Netherite
1.12.2 has no vanilla netherite. A compatibility `magicindustries:netherite_ingot` is included solely to keep the original netherite filter/saw/nugget progression craftable.

## Blasting
1.12.2 has no blast furnace recipe type. Original smelting/blasting outputs are registered through the furnace system.

## Smithing
1.12.2 has no smithing table/template recipe system. The netherite gemstone-saw upgrade is represented by a shapeless upgrade recipe using the same logical inputs.

## Wandering Trader
1.12.2 has no Wandering Trader. The original generic 16-emerald, one-use Mana Flower offer is attached to a dedicated nitwit career. The Farmer level-4 32-emerald trade is preserved directly.

## Merchant XP / dynamic price multiplier
The 1.20 MerchantOffer constructor exposes trader XP and price multiplier values that the 1.12 MerchantRecipe format does not represent equivalently. Item cost, output and one-use limit are preserved; the missing pricing metadata cannot be encoded natively.

## Liquid Mana flow
Forge 1.12 fluid flow is quanta-based. The source's `slopeFindDistance(2)` and `levelDecreasePerBlock(2)` are represented with a 4-quanta `BlockFluidClassic` setup, while preserving source-only fluid/tank rules and visuals.

## Cross-mod interoperability

The 1.12.2 port registers its common materials in Forge OreDictionary and accepts OreDictionary-compatible inputs in material recipes. In particular, copper, tungsten, mithril, netherite-compat materials, sapphire and gemstone saw/filter groups expose stable aliases so other 1.12.2 backports can interoperate without hard dependencies. Copper equipment repair also accepts any `ingotCopper` OreDictionary entry.

Machines continue to expose Forge Energy and fluid/item capabilities rather than mod-specific transport APIs, so compatible 1.12.2 pipes/cables/tanks should be able to automate them subject to the original sided rules.
