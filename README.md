# Flappy Bird (MiniJuegos AAR)

Clon jugable embebible para [MiniJuegos](https://github.com/AlejandroHP17/MiniJuegos).

Repo GitHub: [AlejandroHP17/FlappyBird](https://github.com/AlejandroHP17/FlappyBird)  
(carpeta local del workspace AllInOne: `FlappyBirdCopy/`)

El prototipo Godot 4 queda en [`godot/`](godot/) como referencia de diseño/mecánica. El artefacto que consume el host es el módulo Compose `:flappybird` (mismo enfoque que [Find Out](https://github.com/AlejandroHP17/findout): **no** se embebe el runtime Godot).

Más detalle de estructura: [docs/ESTRUCTURA.md](docs/ESTRUCTURA.md).  
Playbook del ecosistema: [MINIJUEGOS_AAR_PLAYBOOK](https://github.com/AlejandroHP17/MiniJuegos/blob/main/docs/TUTORIAL.md) (host) / workspace `AllInOne/docs/`.

## Artefacto Maven

| Coordenada | Valor |
|------------|--------|
| Group | `pelkidev.com.mx.minijuegos` |
| Artifact | `flappybird` |
| Versión | `1.0.0-SNAPSHOT` |
| Packages | `https://maven.pkg.github.com/AlejandroHP17/FlappyBird` |

Depende de: `pelkidev.com.mx.minijuegos:sdk:1.0.0`

Entry: `FlappyBirdGameModule` (`id = "flappybird"`).

## Módulos

| Módulo | Tipo | Rol |
|--------|------|-----|
| `:flappybird` | `android-library` | Motor (`FlappyEngine`), UI Canvas, sonidos, `FlappyBirdGameModule` → AAR |
| `:app` | `android-application` | Shell (`applicationId` = `pelkidev.com.mx.flappybird`) |
| `godot/` | Godot 4.4 | Prototipo (no se empaqueta en el AAR) |

## Desarrollo local

```bash
./gradlew :app:installDebug
./gradlew :flappybird:publishToMavenLocal
```

La Activity llama a `FlappyBirdGameModule.Entry(onExit = ::finish)`.

## Integración MiniJuegos (ya cableada en el host)

1. Repo Maven: `maven.pkg.github.com/AlejandroHP17/FlappyBird`
2. `implementation("pelkidev.com.mx.minijuegos:flappybird:1.0.0-SNAPSHOT")`
3. `FlappyBirdGameModule` en `GameCatalog`

## Controles

- Tap / click: saltar (y empezar partida)
- Atrás (flecha): `onExit` → vuelve al hub / cierra el shell
- Game over → “Jugar de nuevo” reinicia; high score en SharedPreferences

## CI

Push a `main` o *Run workflow* → build/test + publish.

- Secret: `GH_PACKAGES_TOKEN` (`read:packages` + `write:packages`)
- Android SDK: `android-actions/setup-android@v3` con `packages: platform-tools` (evitar el paquete obsoleto `tools`)
