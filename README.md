# Mobile MediSupply

Mobile MediSupply es una aplicación Android diseñada para gestionar suministros médicos de manera eficiente. Este proyecto está desarrollado en Kotlin y utiliza Jetpack Compose con principios de diseño Material 3.

## 🏗️ Arquitectura

- **Clean Architecture** con capas bien definidas
- **MVVM** pattern con ViewModels
- **Jetpack Compose** para UI moderna
- **Material Design 3** para consistencia visual
- **Hilt** para inyección de dependencias
- **Room** para persistencia local
- **Retrofit** para comunicación con APIs

## 📋 Requisitos

- Android Studio Giraffe | 2022.3.1 Patch 1 o superior.
- Gradle 8.0 o superior.
- SDK mínimo: 21.
- SDK objetivo: 33.

## 🚀 Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Grupo8-MediSupply/Mobile-MediSupply.git
   cd Mobile-MediSupply
   ```

2. Configura Git Flow:
   ```bash
   chmod +x scripts/setup-git-hooks.sh
   ./scripts/setup-git-hooks.sh
   ```

3. Inicializa Git Flow:
   ```bash
   git flow init
   ```

4. Abre el proyecto en Android Studio

5. Sincroniza el proyecto con Gradle

## 🔄 Git Flow

Este proyecto utiliza Git Flow para el manejo de ramas y releases. Ver [docs/GIT_FLOW.md](docs/GIT_FLOW.md) para más detalles.

### Ramas principales:
- `main`: Código de producción
- `develop`: Rama de desarrollo

### Ramas de trabajo:
- `feature/*`: Nuevas funcionalidades
- `release/*`: Preparación de releases
- `hotfix/*`: Correcciones críticas

### Comandos básicos:
```bash
# Nueva feature
git flow feature start nombre-feature

# Finalizar feature
git flow feature finish nombre-feature

# Nuevo release
git flow release start 1.0.0

# Finalizar release
git flow release finish 1.0.0
```

## 🛠️ Desarrollo

### Estructura del proyecto:
```
app/src/main/java/com/example/mobile_medisupply/
├── core/                    # Funcionalidades compartidas
├── features/               # Módulos por funcionalidad
├── navigation/             # Navegación
└── ui/                     # Temas y componentes UI
```

### Comandos útiles:
```bash
# Ejecutar tests
./gradlew test

# Lint check
./gradlew lintDebug

# Build debug
./gradlew assembleDebug

# Build release
./gradlew assembleRelease
```

## 📏 Convenciones de Código

- **Commits**: [Conventional Commits](https://www.conventionalcommits.org/)
- **Kotlin**: [Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- **Android**: [Style Guide](https://developer.android.com/kotlin/style-guide)

## 🧪 Testing

```bash
# Unit tests
./gradlew testDebugUnitTest

# Instrumented tests
./gradlew connectedAndroidTest

# Test coverage
./gradlew jacocoTestReport
```

## 📦 Build y Release

### Debug Build:
```bash
./gradlew assembleDebug
```

### Release Build:
```bash
./gradlew assembleRelease
```

### Crear Tag de Release:
```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea tu feature branch (`git flow feature start amazing-feature`)
3. Commit tus cambios (`git commit -m 'feat: add amazing feature'`)
4. Push a la branch (`git push origin feature/amazing-feature`)
5. Abre un Pull Request
