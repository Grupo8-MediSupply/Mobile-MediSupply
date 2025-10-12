# Git Flow - Mobile MediSupply

## Estructura de Ramas

### Ramas Principales

- **`main`**: Código de producción estable
- **`develop`**: Rama de integración para el próximo release

### Ramas de Soporte

- **`feature/*`**: Nuevas funcionalidades
- **`release/*`**: Preparación de releases
- **`hotfix/*`**: Correcciones críticas de producción

## Configuración Inicial

```bash
# Inicializar Git Flow
git flow init

# Configuraciones recomendadas:
# Branch name for production releases: main
# Branch name for "next release" development: develop
# Feature branches: feature/
# Bugfix branches: bugfix/
# Release branches: release/
# Hotfix branches: hotfix/
# Support branches: support/
# Version tag prefix: v
```

## Flujo de Trabajo

### 1. Trabajar en Features

```bash
# Crear nueva feature
git flow feature start nombre-feature

# Desarrollar la funcionalidad...
git add .
git commit -m "feat: descripción del cambio"

# Finalizar feature
git flow feature finish nombre-feature
```

### 2. Preparar Release

```bash
# Iniciar release
git flow release start 1.0.0

# Realizar ajustes finales...
git add .
git commit -m "chore: prepare release 1.0.0"

# Finalizar release
git flow release finish 1.0.0
```

### 3. Hotfixes

```bash
# Crear hotfix
git flow hotfix start 1.0.1

# Corregir el error...
git add .
git commit -m "fix: descripción del hotfix"

# Finalizar hotfix
git flow hotfix finish 1.0.1
```

## Convenciones de Commits

Utilizamos [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

### Tipos de Commit

- **feat**: Nueva funcionalidad
- **fix**: Corrección de bug
- **docs**: Cambios en documentación
- **style**: Cambios de formato (no afectan funcionalidad)
- **refactor**: Refactorización de código
- **perf**: Mejoras de rendimiento
- **test**: Agregar o modificar tests
- **chore**: Tareas de mantenimiento
- **ci**: Cambios en CI/CD
- **build**: Cambios en build system

### Ejemplos

```bash
feat(auth): add login functionality
fix(inventory): resolve stock calculation error
docs(readme): update installation instructions
refactor(supplies): simplify mapper functions
test(auth): add unit tests for login
chore(deps): update dependencies
```

## Reglas de Branch

### Feature Branches

- Nomenclatura: `feature/TASK-ID-descripcion-corta`
- Ejemplos: 
  - `feature/MMS-001-user-authentication`
  - `feature/MMS-002-inventory-dashboard`

### Release Branches

- Nomenclatura: `release/X.Y.Z`
- Ejemplos: `release/1.0.0`, `release/2.1.0`

### Hotfix Branches

- Nomenclatura: `hotfix/X.Y.Z`
- Ejemplos: `hotfix/1.0.1`, `hotfix/2.1.1`

## Pull Request Guidelines

### Template de PR

```markdown
## Descripción
Breve descripción de los cambios

## Tipo de cambio
- [ ] Bug fix
- [ ] Nueva feature
- [ ] Breaking change
- [ ] Documentación

## Checklist
- [ ] El código sigue las convenciones del proyecto
- [ ] Se han agregado tests
- [ ] Los tests pasan
- [ ] La documentación ha sido actualizada
- [ ] El código ha sido revisado

## Screenshots (si aplica)
```

### Revisión de Código

- Mínimo 1 reviewer requerido
- Todos los tests deben pasar
- No merge directo a `main`
- Squash commits al mergear features

## Versionado Semántico

Seguimos [Semantic Versioning](https://semver.org/):

- **MAJOR**: Cambios incompatibles en la API
- **MINOR**: Nueva funcionalidad compatible
- **PATCH**: Correcciones de bugs compatibles

Formato: `MAJOR.MINOR.PATCH` (ej: 1.2.3)

## Tags y Releases

```bash
# Crear tag
git tag -a v1.0.0 -m "Release version 1.0.0"

# Subir tag
git push origin v1.0.0

# Listar tags
git tag -l
```
