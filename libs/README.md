# Optional local dependency fallback

If the historical Maven repositories fail, put these exact production JARs here:

- `Baubles-1.12-1.5.2.jar`
- `jei_1.12.2-4.16.1.1013.jar`

The ForgeGradle 3 build detects these names and uses normal local `implementation files(...)` dependencies instead of Maven deobfuscation. Keep these JARs out of the source ZIP when redistributing the port unless their licenses/redistribution terms allow it.

The project compiles against the JEI 4.16.1.302 API from Maven, but uses 4.16.1.1013 as the full runClient runtime.
