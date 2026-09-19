# Game rules

## `aggressiveSharks`

- Type: boolean
- Category: `mobs`
- Default: `false`

When `false` (the default), sharks behave exactly as in previous versions:
they hunt their normal aquatic prey and retaliate when hurt, but never seek
out players.

When `true`, the large predatory species additionally hunt players as prey:

- Great white shark (`benssharks:greatwhiteshark`)
- Bull shark
- Tiger shark
- Mako shark
- Bonnethead shark
- Blacktip reef shark
- Lemon shark
- Cookiecutter shark
- Megalodon
- Blue shark
- Greenland shark
- Oceanic whitetip shark
- Nurse shark (tamed individuals never target their owner)
- Barracuda

Filter feeders and non-predatory species are intentionally unaffected and
stay passive even with the rule enabled: whale shark, basking shark, krill,
remora, pilot fish, axodile, greater axodile, land shark, and thalassoger.

Set it per world with:

```
/gamerule aggressiveSharks true
```

The rule is strictly opt-in; existing worlds and servers upgrading in place
keep the previous passive behavior because the default is `false`.
