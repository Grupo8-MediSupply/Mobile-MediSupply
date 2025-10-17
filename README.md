# Mobile MediSupply

Mobile MediSupply es una aplicación Android para la gestión eficiente de suministros médicos. Desarrollada con Kotlin, Jetpack Compose y siguiendo principios de Clean Architecture.

## 🏗️ Arquitectura

El proyecto sigue los principios de **Clean Architecture** y **MVVM** organizados en las siguientes capas:

### Capas Principales
- **Presentation Layer**: UI (Jetpack Compose) + ViewModels
- **Domain Layer**: Use Cases + Business Logic + Repository Interfaces  
- **Data Layer**: Repository Implementations + API + Database

### Estructura de Directorios

```
app/src/main/java/com/example/mobile_medisupply/
├── core/                           # Funcionalidades compartidas
│   ├── network/                    # Configuración de red y estados
│   ├── database/                   # Base de datos y converters
│   ├── utils/                      # Utilidades comunes
│   └── di/                         # Inyección de dependencias
├── features/                       # Organizados por funcionalidad
│   ├── auth/                       # Autenticación y autorización
├── navigation/                     # Navegación entre pantallas
└── ui/                             # Componentes UI reutilizables
    ├── theme/                      # Tema personalizado Material 3
    ├── components/                 # Componentes compartidos
    └── utils/                      # Utilidades de UI
```

## 🛠️ Stack Tecnológico

### Core
- **Kotlin 1.9.0** - Lenguaje principal
- **Jetpack Compose 1.5.0** - UI moderna y declarativa
- **Material 3 1.1.2** - Sistema de diseño

### Arquitectura y Patrones
- **Clean Architecture** - Separación de responsabilidades
- **MVVM** - Patrón de presentación
- **Repository Pattern** - Abstracción de datos
- **Use Cases** - Lógica de negocio encapsulada

### Inyección de Dependencias
- **Hilt 2.48** - Inyección de dependencias

### Red y APIs
- **Retrofit 2.9.0** - Cliente HTTP
- **OkHttp 4.11.0** - Interceptores y logging
- **Gson 2.10.1** - Serialización JSON

### Base de Datos Local
- **Room 2.6.0** - Persistencia local y cache
- **SQLite** - Motor de base de datos

### Programación Asíncrona
- **Coroutines 1.7.3** - Concurrencia
- **Flow** - Streams reactivos

### Navegación
- **Navigation Compose 2.7.5** - Navegación entre pantallas

### Testing
- **JUnit 4.13.2** - Unit testing
- **Mockito 5.6.0** - Mocking para tests
- **Espresso 3.5.1** - UI testing

## 🎨 Tema Personalizado

La aplicación utiliza un tema personalizado basado en Material 3 con:
- **Esquema de colores teal/cyan** para el sector médico
- **Tipografía optimizada** para legibilidad
- **Componentes consistentes** en toda la app
- **Soporte para modo oscuro**


## 🚀 Configuración del Proyecto

### Requisitos
- **Android Studio Narwal | 2023.2.1** o superior
- **Gradle 8.6** o superior
- **SDK mínimo:** 26 (Android 8.0)
- **SDK objetivo:** 34 (Android 14)
- **JDK 17** (recomendado)

### Instalación

1. **Clonar el repositorio:**
   ```bash
   git clone git@github.com:Grupo8-MediSupply/Mobile-MediSupply.git
   cd Mobile-MediSupply
   ```

2. **Abrir en Android Studio:**
   - File → Open → Seleccionar carpeta del proyecto
   - Esperar sincronización de Gradle

3. **Configurar API Base URL:**
   ```kotlin
   // En NetworkModule.kt
   private const val BASE_URL = "https://tu-api.medisupply.com/v1/"
   ```

4. **Ejecutar la aplicación:**
   - Conectar dispositivo o usar emulador
   - Run → Run 'app'

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

## 🧪 Testing

### Estrategia de Testing
- **Unit Tests** - Lógica de negocio y ViewModels
- **Integration Tests** - Repository y Use Cases  
- **UI Tests** - Pantallas y componentes

### Ejecutar Tests
```bash
# Tests unitarios
./gradlew testDebugUnitTest

# Tests instrumentados
./gradlew connectedDebugAndroidTest

# Todos los tests
./gradlew check
```

## 📖 Patrones y Convenciones

### Naming Conventions
- **Packages**: `snake_case`
- **Classes**: `PascalCase`
- **Functions/Variables**: `camelCase`
- **Constants**: `SCREAMING_SNAKE_CASE`

### Git Workflow
- **Feature branches**: `feature/nombre-funcionalidad`
- **Bugfix branches**: `bugfix/descripcion-bug`
- **Hotfix branches**: `hotfix/descripcion-critica`

### Commits
```
feat: agregar pantalla de login
fix: corregir error en cache de productos
docs: actualizar README con arquitectura
refactor: reorganizar estructura de archivos
```

## 🤝 Contribución

1. Fork del proyecto
2. Crear feature branch (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -m 'feat: agregar nueva funcionalidad'`)
4. Push al branch (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request


