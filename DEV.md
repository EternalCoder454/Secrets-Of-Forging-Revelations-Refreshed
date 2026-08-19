# Developer guide

Building this addon, and how it reaches into Tetra without Tetra knowing about it. For the port and
the licensing position see [PORT-STATUS.md](PORT-STATUS.md).

This is bundled inside Tetra Refreshed rather than installed alongside it, at Ace's request. It
stays its own mod with its own id, and Tetra embeds the jar with jarJar. Nothing about this project
lives in Tetra's source tree.

## Building

Gradle 9.7.0, ModDevGradle 2.0.144, Java 25, NeoForge 26.1.2.95.

Both mutil and Tetra come from mavenLocal, so the order matters:

```bash
cd "../Mutil Refreshed"               && ./gradlew.bat publishToMavenLocal
cd "../Tetra Refreshed"               && ./gradlew.bat publishToMavenLocal
cd "../Secrets-Of-Forging-Revelations" && ./gradlew.bat build publishToMavenLocal
cd "../Tetra Refreshed"               && ./gradlew.bat build
```

That last step is what embeds this jar into Tetra's. Skipping it leaves Tetra shipping the previous
build of this mod, which looks exactly like a change that did not take.

**Do not also put this jar in the mods folder.** Tetra carries it, and two copies both register
`tetra:modular_polearm`, which fails to load.

## Where the content lives

It registers into Tetra's namespace rather than its own, which is why almost all of its data sits
under `data/tetra` and `assets/tetra`. That is how the original was written and it is load bearing:
Tetra's stores only read their own namespace.

| Path | What |
|---|---|
| `data/tetra/modules/polearm` | 16 modules, the heads, handles and bindings |
| `data/tetra/schematics/polearm` | 46 schematics, what can be crafted onto what |
| `data/tetra/improvements/polearm` | 10 improvements, honing and settling |
| `data/tetra/{modules,schematics}/{sword,bow,single,double}` | additions to Tetra's own items |
| `data/tetra/materials/socket` | the thermal cell as a socket material |
| `data/tetra/recipe/polearm` | the five spear recipes, the only way in without creative |
| `data/tetra/*/greatsword` | Art of Forging content, inert until that mod exists |
| `assets/tetra/stat_bars` | the three effect bars |
| `assets/tetra/holosphere_entries` | the polearm's holosphere entry |

## Reaching into Tetra from this side

Tetra's source is untouched. Everything this adds to Tetra's screens is done from here, and that is
the pattern to follow for anything new.

**The creative tab.** Tetra's holder for its own tab is private, so match on the id instead:

```java
ResourceKey.create(Registries.CREATIVE_MODE_TAB,
        Identifier.fromNamespaceAndPath(TetraMod.MOD_ID, "default"))
```

The polearm used to land in vanilla's Combat tab, where nothing named it and nothing listed it as
craftable, which is why players did not find it.

**The holosphere.** Ship `assets/tetra/holosphere_entries/<name>.json` with an `item`, an `icon` and
a `position`. Tetra's own entries take 0 to 7, and the polearm sits at 3, which was the one gap.

**Stat bars.** Ship `assets/tetra/stat_bars/<name>.json` rather than calling `WorkbenchStatsGui.addBar`
from java. The data form also reaches the holosphere, which the java form never did, and it needs no
client side code at all. `contexts` takes `tetra:workbench`, `tetra:holosphere`, or both.

**Both of Tetra's stores filter to the `tetra` namespace**, which is the whole reason this works from
an addon. A file under `assets/secrets_of_forging_revelations/stat_bars` would be ignored.

## Effects

Three, all keyed under this mod's namespace, which is correct now that this is a separate project:

| Id | Reads | Does |
|---|---|---|
| `secrets_of_forging_revelations:freezing` | level, efficiency | chills on hit, deepening rather than refreshing, up to efficiency stacks |
| `secrets_of_forging_revelations:infernal` | level | sets the target alight for one second per level |
| `secrets_of_forging_revelations:blizzard` | level, efficiency | buffs where it is cold enough to snow, debuffs where it is warm enough to rain |

Each is an `ItemEffect.get(...)` constant and a game bus subscriber. A stat bar naming one takes the
full namespaced id in its `effect` field.

Lang keys come in three shapes and all three are needed. `tetra.stats.<id>` names it, `.tooltip`
feeds the stat bar, and `.tooltip_short` feeds the holosphere material list.

## Traps already hit

**Its `glyphs.png` is a stale copy of Tetra's.** It adds nothing and is missing the glyph at x=112
y=224, so whichever mod wins load order decides whether that glyph draws. It should be deleted from
this repo. The real additions are in `sofr_glyph.png` and `aof_glyph.png`.

**Data files that collide with Tetra's own merge at runtime**, because `"replace": false` makes
`MergingDataStore` merge rather than replace. Eleven files rely on that. It stops applying the
moment anything flattens the two into one tree.

**58 references to `art_of_forging`** sit in the greatsword tree and in some polearm modules. They
are effect names that nothing registers, so they contribute nothing and are dropped in silence, the
same way `tetra:draw_damage` is on Tetra's side.

Work through [PLAYTESTING.md](PLAYTESTING.md) before calling a build good.

## Repository rules

1. Minecraft 26.1.2, NeoForge only. Java 25.
2. The mod is AceTheEldritchKing's, updated by GamerK_2. Credit EternalHell for the 26.1.2 port
   only, never as author.
3. `upstream` stays pointed at Ace's repository. Take future changes by rebasing onto it.
4. Its terms live in the README rather than a licence file. Read them before publishing anything.
5. Writing rules: run `python tools/check-writing-rules.py --rules`. Nothing else states them.
