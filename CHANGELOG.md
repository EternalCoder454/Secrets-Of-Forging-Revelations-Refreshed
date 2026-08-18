# Changelog

Secrets of Forging: Revelations is by [AceTheEldritchKing](https://github.com/AceTheEldritchKing),
updated by GamerK_2. Only the 26.1.2 NeoForge port is recorded here. Upstream's own history is in
its repository, and the mod itself is Ace's work.

## 1.3.5, for 26.1.2

The first build for 26.1.2, and the first that ships inside Tetra rather than beside it.

Requires **Tetra Refreshed 6.13.0** and **Mutil Refreshed 7.0.0-pre.0** or later.

**This is a test build.** It loads, and its data all parses, but no polearm has been crafted,
thrown, or looked at. Read the known gaps at the bottom before reporting.

### Bundled rather than installed

Ace gave permission on 2026-08-18 for this to be included in Tetra Refreshed, asking that it stay a
separate project included in the mod rather than folded into Tetra's source, the way Create bundles
Flywheel. So Tetra embeds this jar with jarJar and NeoForge loads it from inside, as its own mod
with its own id.

**Remove any separate `secrets_of_forging_revelations` jar from your mods folder.** Tetra carries
it now, and two copies both register `tetra:modular_polearm`, which fails to load.

### Found at last

* **The polearm is in Tetra's own creative tab.** It used to go into vanilla's Combat tab, where
  nothing named it and nothing listed it as craftable. That, rather than any bug, is why most
  players never found it.
* **It has a holosphere entry**, fourth in the list, between the double headed tool and the bow.
* **Freezing, infernal and eternal blizzard show a bar in the holosphere** as well as the workbench.
  They were workbench only before, because they were added in java rather than as data.

### The port

* **The loader.** The mod loading context is gone, so the event bus arrives as a constructor
  argument and the effect handlers register against the game bus.
* **Registration.** An item carries its registry id on its properties, and only
  `DeferredRegister.Items` sets it, so the polearm no longer builds its own. `@ObjectHolder` is gone.
* **Mob effects** are referred to by holder rather than instance. Attribute modifiers are keyed by
  an identifier rather than a uuid, and the operations were renamed. Four vanilla effects it applies
  changed name: `DAMAGE_BOOST`, `MOVEMENT_SPEED`, `MOVEMENT_SLOWDOWN` and `DIG_SLOWDOWN` are now
  `STRENGTH`, `SPEED`, `SLOWNESS` and `MINING_FATIGUE`.
* **`LivingDamageEvent` is split** into a pre and a post. Both effects hang off post, because they
  react to a hit having landed rather than changing the damage.
* **Biomes.** Asking whether it is cold enough to snow takes the level's sea level now, since the
  answer depends on height above it.
* **Both mixins are gone.** They existed only because Tetra's thrown item renderer picked a pose
  with a chain of instanceof against Tetra's own classes, so an outside item could not get one.
  Tetra asks the item now, so the polearm declares its pose as an ordinary override. Two fewer
  runtime failure modes.
* **Data.** The recipe directory is singular. A result names its item as `id` with `components`
  rather than `item` with `nbt`, so a crafted polearm's modules live in `custom_data`. Ingredients
  are bare strings, and Forge's common tags moved to the `c` namespace.

### Known gaps

* **Roughly a third of this mod needs Art of Forging**, which is not ported. 22 greatsword
  schematics, 8 modules, 11 improvements and a synergy target slots no item here has, and 58
  references point at a namespace that does not exist. All of it is inert rather than broken.
* **Its `glyphs.png` is a stale copy of Tetra's**, adding nothing and missing one glyph, so load
  order decides whether that glyph draws. It should be deleted from this repo.
* **No play testing.** Nothing has been crafted or used.

### Reporting

Work through [PLAYTESTING.md](PLAYTESTING.md) first. Say what you did, what happened, and attach
`logs/latest.log`. If it failed to load, `logs/debug.log` has the real stack, and a crash report
usually shows only the launcher wrapper.
