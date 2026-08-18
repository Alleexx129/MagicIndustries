# First runtime verification checklist

This is the minimum pass before calling the port runtime-proven.

## Startup / registries
- Client reaches title screen with Magic Industries, Baubles and JEI loaded.
- Dedicated server reaches `Done` with no client-classloading crash.
- Existing/new world opens with no missing-registry errors.

## Items and recipes
- All Magic Industries items/blocks appear with correct models/textures.
- JEI shows crafting recipes and the Mana Extractor category/catalyst.
- Gem Saw and Netherite Gemstone Saw lose exactly one durability as crafting remainders.
- Copper tools/armor have the intended stats and repair with OreDictionary copper.

## Mana
- New player starts at 0 mana; cap is 100.
- Mana Flask takes 15 ticks, adds 4 mana and leaves an Empty Flask outside creative.
- Mana survives non-death clone/dimension behavior according to the source logic and syncs to the HUD.
- Sapphire Charm only fits the Baubles charm slot, gives Speed II while equipped and removes Speed on unequip.

## Mana Water
- Bucket places/picks up Mana Water.
- Fluid is cyan-tinted and uses the custom underwater overlay/fog (RGB 0.8784314/0.21960784/0.8156863; fog 1..6).
- External Forge fluid pipes see two extractor tanks and can interact with their original ordering/rules.

## Mana Extractor
- Slot 0 only accepts a mana filter; slot 1 accepts water/empty buckets.
- Horizontal adjacent Mana Flower + >=500 mB water advances progress.
- At 100 ticks: -500 mB water, +250 mB mana, filter +1 damage.
- Water tank is 64,000 mB; mana tank is 100,000 mB.
- The GUI keeps the original 64,000 mB visual scaling for both tank renderers.
- Hopper/pipe insertion/extraction follows machine-facing-relative sided rules.
- NBT survives save/reload using the original keys.

## Coal / Heat Generator
- Capacity 69,000 FE; external extraction max 256 FE/t.
- Lava bucket operation produces 12,500 FE and leaves a bucket.
- Normal fuel follows the original post-shrink burn-time calculation.
- Top/horizontal automation inserts fuel only; bottom extracts an empty bucket only.
- NBT survives save/reload using the original keys.

## Worldgen / progression
- Sapphire, tungsten, mithril and compatibility copper generate in the Overworld.
- Bottom-Y deepslate variants appear according to the documented 1.12 translation.
- Farmer level-4 Mana Flower trade appears.
- The 1.12 wandering-trader translation appears on the documented nitwit fallback.

## Cross-mod pass
After the clean test passes, add target 1.12.2 mods one group at a time. Verify OreDictionary copper/tungsten/mithril/netherite substitutions and Forge Energy/fluid automation against the actual mods you intend to use.
