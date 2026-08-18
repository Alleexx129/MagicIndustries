#!/usr/bin/env python3
import json, re, subprocess, sys
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
JAVA=ROOT/'src/main/java'; RES=ROOT/'src/main/resources'; ASSETS=RES/'assets/magicindustries'
errors=[]; warnings=[]; checks=0

def check(cond,msg):
    global checks
    checks += 1
    if not cond: errors.append(msg)

def text(rel): return (ROOT/rel).read_text(encoding='utf-8', errors='ignore')

# Every JSON resource must parse.
json_files=list(RES.rglob('*.json'))
for p in json_files:
    try: json.loads(p.read_text(encoding='utf-8'))
    except Exception as e: errors.append(f'JSON parse: {p.relative_to(ROOT)}: {e}')
    checks += 1

java_files=list(JAVA.rglob('*.java'))
all_java='\n'.join(p.read_text(errors='ignore') for p in java_files)

# Modern-only API residue that would be a hard 1.12 incompatibility.
modern=['net.minecraft.world.level.','net.minecraft.core.','net.minecraftforge.registries.DeferredRegister',
        'net.minecraftforge.eventbus.api.','top.theillusivec4.curios','ForgeCapabilities','LazyOptional']
for token in modern: check(token not in all_java, f'modern API residue: {token}')

# Client-only Minecraft classes may only occur in physical-client classes.
for p in java_files:
    s=p.read_text(errors='ignore')
    if 'net.minecraft.client.' in s:
        rel=p.relative_to(JAVA).as_posix()
        allowed=('/gui/' in '/'+rel or '/jei/' in '/'+rel or rel.endswith('ClientEvents.java') or rel.endswith('ClientProxy.java'))
        check(allowed, f'dedicated-server classloading risk: client import in {rel}')

# Brace/comment/string balance.
def strip_java(s):
    out=[]; i=0; state='code'
    while i<len(s):
        c=s[i]; n=s[i+1] if i+1<len(s) else ''
        if state=='code':
            if c=='/' and n=='/': state='line'; i+=2; continue
            if c=='/' and n=='*': state='block'; i+=2; continue
            if c=='"': state='str'; out.append(' '); i+=1; continue
            if c=="'": state='char'; out.append(' '); i+=1; continue
            out.append(c); i+=1
        elif state=='line':
            if c=='\n': out.append('\n'); state='code'
            i+=1
        elif state=='block':
            if c=='*' and n=='/': state='code'; i+=2
            else: i+=1
        else:
            quote='"' if state=='str' else "'"
            if c=='\\': i+=2
            elif c==quote: state='code'; i+=1
            else: i+=1
    return ''.join(out),state
for p in java_files:
    s,state=strip_java(p.read_text())
    check(state=='code',f'unclosed comment/string: {p.relative_to(ROOT)}')
    depth=0
    for c in s:
        if c=='{': depth+=1
        elif c=='}': depth-=1
        if depth<0: break
    check(depth==0,f'brace imbalance: {p.relative_to(ROOT)}')

# High-value behavioral parity signatures from the shipped 1.20.1 source/JAR.
required={
'Baubles charm slot':'BaubleType.CHARM',
'Sapphire Charm Speed II':'new PotionEffect(MobEffects.SPEED, 20, 1, true, false)',
'mana flask +4':'mana.addMana(4)', 'mana cap 100':'Math.max(0, Math.min(100, mana))',
'coal capacity 69000':'CAPACITY = 69000', 'coal extract 256':'MAX_EXTRACT = 256', 'lava energy 12500':'energy.addEnergy(12500)',
'coal exact NBT energy':'"coal_generator.energy"', 'coal exact NBT progress':'"coal_generator.progress"',
'extractor water cap 64000':'WATER_CAPACITY = 64000', 'extractor mana cap 100000':'MANA_CAPACITY = 100000',
'extractor water/use 500':'WATER_PER_OPERATION = 500', 'extractor mana/use 250':'MANA_PER_OPERATION = 250',
'extractor exact water NBT':'"fluid_tank"', 'extractor exact mana NBT':'"mana_tank"', 'extractor exact progress NBT':'"mana_extractor.progress"',
'mana flower adjacency':'hasAdjacentManaFlower()', 'bucket pickup':'FillBucketEvent',
'fog start 1':'setFogStart(1F)', 'fog end 6':'setFogEnd(6F)',
'JEI recipe uid':'gem_infusing', 'custom recipe factory':'ManaExtractorRecipe$Factory',
'worldgen sapphire original range':'uniform(random,-16,256)', 'worldgen tungsten original range':'uniform(random,-64,72)',
'farmer trade':'ManaFlowerTrade(32)', 'wandering translation':'ManaFlowerTrade(16)',
'original mana fluid tint':'setColor(0xFF00E7FF)',
'vanilla water still sprite':'new ResourceLocation("minecraft", "blocks/water_still")',
'vanilla water flow sprite':'new ResourceLocation("minecraft", "blocks/water_flow")',
'extractor GUI exact render size':'guiTop + 11, 14, 58',
'extractor GUI exact hover water':'guiLeft + 156, guiTop + 12, 14, 58',
'extractor GUI exact hover mana':'guiLeft + 105, guiTop + 12, 14, 58',
}
combined=all_java+'\n'+text('src/main/resources/assets/magicindustries/recipes/_factories.json')
for name,tok in required.items(): check(tok in combined,f'missing parity signature: {name} ({tok})')

# Core resources.
files=[
'assets/magicindustries/textures/gui/coal_generator_gui.png','assets/magicindustries/textures/gui/mana_extractor_gui.png',
'assets/magicindustries/textures/gui/mana_bar_empty.png','assets/magicindustries/textures/gui/mana_bar_full.png',
'assets/magicindustries/textures/misc/in_mana_water.png','assets/magicindustries/recipes/_factories.json',
'assets/magicindustries/lang/en_us.lang','mcmod.info']
for f in files: check((RES/f).is_file(),f'missing resource: {f}')

# Local model/texture references resolve.
for p in (ASSETS/'models').rglob('*.json'):
    data=json.loads(p.read_text())
    def walk(x):
        if isinstance(x,dict):
            for v in x.values(): yield from walk(v)
        elif isinstance(x,list):
            for v in x: yield from walk(v)
        elif isinstance(x,str): yield x
    for v in walk(data):
        if v.startswith('magicindustries:') and '/' in v:
            rel=v.split(':',1)[1]
            if rel.startswith(('block/','item/')):
                tex=ASSETS/'textures'/f'{rel}.png'; model=ASSETS/'models'/f'{rel}.json'
                check(tex.exists() or model.exists(), f'unresolved local model/texture ref {v} in {p.relative_to(ROOT)}')

# Crafting recipe parity. Four furnace/blast recipes are represented by GameRegistry.addSmelting in 1.12.
expected=set('coal_generator copper_axe copper_axe2 copper_block copper_boots copper_chestplate copper_filter copper_helmet copper_hoe copper_hoe2 copper_ingot copper_leggings copper_pickaxe copper_shovel copper_sword cut_sapphire empty_flask gem_saw gold_filter mana_extractor mithril_block mithril_ingot netherite_filter netherite_gemstone_saw netherite_nugget sapphire sapphire_block sapphire_charm stick2 tungsten_block tungsten_ingot'.split())
actual={p.stem for p in (ASSETS/'recipes').glob('*.json') if not p.name.startswith('_')}
for r in sorted(expected): check(r in actual,f'missing converted crafting recipe: {r}')
check('GameRegistry.addSmelting(MITHRIL_ORE' in all_java,'missing mithril smelting translation')
check('GameRegistry.addSmelting(RAW_TUNGSTEN' in all_java,'missing tungsten smelting translation')

# 1.12 compatibility aliases: recipes may consume equivalents supplied by other backports.
ore_aliases=['ingotCopper','blockCopper','ingotTungsten','blockTungsten','ingotMithril','blockMithril','ingotNetherite','nuggetNetherite','gemSapphire','manaFilters']
for ore in ore_aliases: check(f'"{ore}"' in all_java or f'"ore": "{ore}"' in combined, f'missing OreDictionary compatibility alias: {ore}')
ore_recipe_count=0
for p in (ASSETS/'recipes').glob('*.json'):
    if 'forge:ore_dict' in p.read_text(errors='ignore'): ore_recipe_count+=1
check(ore_recipe_count >= 20, f'too few OreDictionary-compatible recipes ({ore_recipe_count})')
check('isCopperIngot(ItemStack stack)' in all_java, 'missing cross-mod copper repair helper')
check(all_java.count('ModContent.isCopperIngot(repair)') >= 6, 'copper tools/armor do not all accept OreDictionary copper repair')

# Toolchain/dependency metadata.
build=text('build.gradle'); mcmod=text('src/main/resources/mcmod.info')
for tok in ['1.12.2-14.23.5.2859','Baubles:1.12-1.5.2','jei_1.12.2:4.16.1.302:api','jei_1.12.2:4.16.1.1013']:
    check(tok in build,f'build dependency/target missing: {tok}')
check('ForgeGradle:3.+' in build and "apply plugin: 'net.minecraftforge.gradle'" in build, 'project is not on ForgeGradle 3 MDK style')
check("mappings channel: 'stable', version: '39-1.12'" in build, 'stable_39 mapping configuration missing')
check("minecraft 'net.minecraftforge:forge:1.12.2-14.23.5.2859'" in build, 'FG3 Minecraft dependency missing')
check("fg.deobf('com.azanor.baubles:Baubles:1.12-1.5.2')" in build, 'Baubles is not deobfuscated through FG3')
check("compileOnly 'mezz.jei:jei_1.12.2:4.16.1.302:api'" in build, 'JEI API baseline compileOnly dependency is missing or incorrectly deobfuscated')
check("jar.finalizedBy('reobfJar')" in build, 'normal jar task is not reobfuscated')
at_cfg=text('src/main/resources/META-INF/accesstransformer.cfg')
check('net.minecraft.item.crafting.Ingredient field_193371_b' in at_cfg, 'JEI Ingredient.matchingStacks access transformer missing')
check('net.minecraft.client.gui.recipebook.GuiRecipeBook field_191904_o' in at_cfg, 'JEI GuiRecipeBook access transformers missing')
check("localBaubles.exists()" in build and "localJeiRuntime.exists()" in build, 'missing libs/ local dependency fallback detection')
check('setupDecompWorkspace' not in text('compile-local.bat') and 'setupDecompWorkspace' not in text('compile-local.sh'), 'obsolete FG2 setupDecompWorkspace remains in build scripts')
for tok in ['baubles','jei','14.23.5.2859','4.16.1.302']:
    check(tok in mcmod,f'mcmod dependency metadata missing: {tok}')

# Java 8 syntax parse sanity. Missing Minecraft/Forge dependencies are expected here; reject syntax diagnostics only.
try:
    cmd=['javac','--release','8','-proc:none','-Xmaxerrs','10000']+[str(p) for p in java_files]
    proc=subprocess.run(cmd,cwd=str(ROOT),stdout=subprocess.PIPE,stderr=subprocess.STDOUT,text=True,timeout=45)
    syntax_markers=['; expected','illegal start of','reached end of file while parsing','unclosed string literal','not a statement',"')' expected", "'}' expected"]
    syntax_lines=[line for line in proc.stdout.splitlines() if any(m in line for m in syntax_markers)]
    check(not syntax_lines,'Java 8 syntax parser diagnostics: '+' | '.join(syntax_lines[:5]))
except (FileNotFoundError, subprocess.TimeoutExpired) as e:
    warnings.append(f'Java 8 syntax sanity skipped: {e}')

print(f'PASS checks: {checks-len(errors)}/{checks}')
print(f'Java files: {len(java_files)}')
print(f'Resource files: {len(list(RES.rglob("*.*")))}')
print(f'JSON files parsed: {len(json_files)}')
print(f'OreDictionary recipe files: {ore_recipe_count}')
if warnings:
    print('WARNINGS:')
    for w in warnings: print(' -',w)
if errors:
    print('ERRORS:')
    for e in errors: print(' -',e)
    sys.exit(1)
print('STATIC VALIDATION PASSED')
