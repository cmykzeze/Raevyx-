# Dragon Riding (Fabric, Minecraft Java 26.3)

Build: install JDK 25, then `gradle wrapper` (or copy the wrapper from the official Fabric template) and run `./gradlew build`.
Jar: build/libs/dragonriding-0.1.0.jar  ->  drop in your mods folder with Fabric API.

In game:
  /summon dragonriding:rideable_dragon
  Right-click with raw fish to tame (random chance), then right-click with a saddle. Right-click to mount.
  Riding controls (Saint's Dragons Raevyx layout):
    W/A/S/D + mouse = move/steer   Jump = take off/rise   Left Alt = descend   Sprint = boost
    Left click = bite / horn gore  X = toggle melee        R = roar             H = summon storm (animation+sound)
    G (hold) = beam (fire)         Ctrl+4 = flex
  Sneak + right-click: sit / follow toggle.

Raevyx model/animations by LilRiceAroni (Saints Dragon) - see CREDITS.md.
Needs GeckoLib 5.5.7+ (26.3) installed alongside Fabric API.
