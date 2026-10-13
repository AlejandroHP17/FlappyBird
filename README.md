# Flappy Bird (MiniJuegos AAR)

Clon jugable embebible para [MiniJuegos](https://github.com/AlejandroHP17/MiniJuegos).

El prototipo Godot 4 queda en [`godot/`](godot/) como referencia de diseño/mecánica. El artefacto que consume el host es el módulo Compose `:flappybird` (mismo enfoque que Find Out).

## Artefacto Maven

| Coordenada | Valor |
|------------|--------|
| Group | `pelkidev.com.mx.minijuegos` |
| Artifact | `flappybird` |
| Versión | `1.0.0-SNAPSHOT` |
| Packages | `https://maven.pkg.github.com/AlejandroHP17/FlappyBirdCopy` |

Depende de: `pelkidev.com.mx.minijuegos:sdk:1.0.0`

## Módulos

| Módulo | Tipo | Rol |
|--------|------|-----|
| `:flappybird` | `android-library` | Motor, UI Canvas, `FlappyBirdGameModule` → AAR |
| `:app` | `android-application` | Shell (`applicationId` = `pelkidev.com.mx.flappybird`) |
| `godot/` | Godot 4.4 | Prototipo (no se empaqueta en el AAR) |

## Desarrollo local

```bash
./gradlew :app:installDebug
./gradlew :flappybird:publishToMavenLocal
```

## Integración MiniJuegos

1. Repo Maven del juego en `settings.gradle.kts` (o `mavenLocal()` en dev)
2. `implementation("pelkidev.com.mx.minijuegos:flappybird:1.0.0-SNAPSHOT")`
3. Registrar `FlappyBirdGameModule` en `GameCatalog`

## Controles

- Tap / click: saltar (y empezar partida)
- Atrás (flecha): `onExit` → vuelve al hub / cierra el shell

## CI

Push a `main` → build/test + publish. Secret: `GH_PACKAGES_TOKEN` (`read:packages` + `write:packages`).
