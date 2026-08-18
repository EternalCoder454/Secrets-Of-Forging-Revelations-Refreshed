# Port status

Secrets of Forging: Revelations, by AceTheEldritchKing, updated by GamerK_2, ported from 1.20.1
Forge to Minecraft 26.1.2 NeoForge on Java 25. The mod is theirs. This is a fork carrying the port.

## Licensing, read this first

The terms are in `README.md` rather than a licence file, and they are MIT with restrictions.

* **Permitted outright:** forking, building it in your own IDE, modifying your fork for personal
  use, modifying artwork for texture packs, and using the code as an example.
* **Needs the author's explicit permission:** offering to update the repository past 1.19.2, which
  is what this port is. The README says to ask on Discord.
* **Not permitted:** reuploading it elsewhere under a new name or author, or forking it and calling
  it your own.

So this fork is fine to build and play. **Publishing it, or folding it into Tetra and shipping
that, is not, until Ace says yes.** That question is separate from the one already put to Mikael
about Tetra itself.

## State

Builds and loads clean beside Tetra Refreshed and Mutil Refreshed, with no errors of its own. What
has not happened is play. No polearm has been crafted, thrown, or looked at.

## What changed

**The loader.** The mod loading context is gone, so the event bus arrives as a constructor argument
and the effect handlers register against `NeoForge.EVENT_BUS`. `@EventBusSubscriber` no longer takes
a bus, since it infers one.

**Registration.** An item carries its registry id on its properties, and only `DeferredRegister.Items`
sets it, so the polearm no longer builds its own properties. `@ObjectHolder` is gone, so the static
instance is assigned at registration instead. It still registers into Tetra's namespace, which is
why its data lives under `data/tetra`.

**Mob effects.** They are referred to by holder rather than by instance. Attribute modifiers are
keyed by an identifier rather than a uuid string, and the operations were renamed, so the old
`ADDITION` is `ADD_VALUE` and `MULTIPLY_TOTAL` is `ADD_MULTIPLIED_TOTAL`. The tick method takes the
level and returns whether to continue. Four of the vanilla effects it applies were renamed:
`DAMAGE_BOOST`, `MOVEMENT_SPEED`, `MOVEMENT_SLOWDOWN` and `DIG_SLOWDOWN` are now `STRENGTH`,
`SPEED`, `SLOWNESS` and `MINING_FATIGUE`.

**Events.** `LivingDamageEvent` is split into a pre and a post. Both effects hang off post, because
they react to a hit having landed rather than changing the damage.

**Biomes.** Asking whether it is cold enough to snow takes the level's sea level now, since the
answer depends on height above it.

**Both mixins are gone.** They existed for one reason: Tetra's thrown item renderer chose a pose
with a chain of instanceof against Tetra's own item classes, so an item from another mod could only
get one by injecting into that method. Tetra Refreshed asks the item instead, so the polearm
declares its pose as an ordinary override and the accessor that reached into the thrown entity's
stack is unnecessary. Two fewer runtime failure modes.

**Data.** The recipe directory is singular now. A result names its item as `id` with `components`
rather than `item` with `nbt`, so the modules a crafted polearm arrives with live in `custom_data`.
Ingredients are bare strings, and Forge's common tags moved to the `c` namespace. An item's model
lives under `assets/tetra/items` and the custom loader became a model type inside a model block.

The attribute ids the data uses all resolve. The four `generic.` prefixed ones and the two Forge
reach names are in the legacy table Tetra already carries, which is worth knowing because an
attribute id that resolves to nothing is dropped without a word.

## Content that needs another mod

Roughly a third of this is compatibility content for **Art of Forging**, which is a separate addon
and is not installed here.

| Target | Schematic files |
|---|---|
| polearm | 51 |
| greatsword, which is Art of Forging's item | 22 |
| sword, bow, double, single, extending Tetra's own | 7 |

There are 58 references to the `art_of_forging` namespace. Tetra drops content it cannot resolve, so
this is inert rather than broken, but it means the greatsword half of this mod does nothing until
Art of Forging is ported too.

## Building

Tetra and mutil come from mavenLocal, so publish them first:

```bash
cd "../Mutil Refreshed" && ./gradlew.bat publishToMavenLocal
cd "../Tetra Refreshed" && ./gradlew.bat publishToMavenLocal
cd "../Secrets-Of-Forging-Revelations" && ./gradlew.bat build
```
