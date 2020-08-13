# BlockRegenFlag
WorldGuard flag expansion, regenerates broken blocks after some time.

**Description**

Simple WorldGuard flag that regenerates broken blocks after configured time.

**Usage**

Add flag 'block-regen' with params to a region.
Syntax: `<BLOCK_TYPE>:<regeneration-delay-in-seconds>, ...`

WorldGuard command example: `/region flag <region-name> block-regen DIRT:10, STONE:5`

The delay is not required, ex.: `/region [...] DIRT, STONE:5`

**Features**

- Includes an anti-obstruct mechanism (block won't regenerate when there's a player on the location -- configurable)

[config.yml]
```yaml
obstruct-prevention:
  enabled: true
  radius: 1
```

- Deny the block from dropping item and exp drops.

[config.yml]
```yaml
deny-drops: true
```
