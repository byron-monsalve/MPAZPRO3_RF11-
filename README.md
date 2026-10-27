# MPAZ PRO 3° — Sección 007, San Bernardo
## RF-11: Conservar el progreso y confirmar la finalización

Proyecto Android del caso de Vinculación con el Medio **"MPAZ PRO 3°: Apoyo didáctico y seguimiento del aprendizaje"** de la Escuela Escritora Marcela Paz.

Esta entrega corresponde al **Requisito Funcional 11 (RF-11)**

---

## Descripción del RF-11

| Campo | Detalle |
|---|---|
| **Módulo** | Experiencia del estudiante |
| **Actor principal** | Estudiante |
| **Prioridad** | Alta |

**Texto del caso:**
> "El sistema debe conservar el progreso del estudiante y confirmar la finalización de cada actividad."

**Historia de usuario:**
> Como estudiante quiero que la app recuerde lo que ya hice aunque la cierre, y me confirme cuando termino algo.

---

## Dependencias del requisito

```
RF-07 Consultar material asignado
  └─► RF-08 Desafíos de dificultad progresiva
        └─► RF-09 Evaluación final (intentos permitidos)
              └─► RF-11 ◄── ESTE REQUISITO (Persistencia + Confirmación)
```

## Stack tecnológico

| Componente | Tecnología |
|---|---|
| Lenguaje | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| Patrón | MVVM — ViewModel + StateFlow |
| Navegación | Navigation Compose |
| **Persistencia (RF-11)** | **Room + KSP** |
| Preferencias | DataStore (configurado, disponible) |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 35 |

---

## Estructura del proyecto

```
app/src/main/java/com/mpazpro3/app/
│
├── model/
│   ├── SeguimientoAvance.kt     ← Entidad de dominio (NO_INICIADO / EN_PROGRESO / FINALIZADO)
│   ├── Actividad.kt
│   ├── Contenido.kt
│   └── ...
│
├── data/
│   ├── MockData.kt              ← Datos ficticios (seguimientos, estudiantes, contenidos)
│   └── local/
│       ├── AppDatabase.kt       ← Base de datos Room (mpazpro_db)
│       ├── SeguimientoDao.kt    ← DAO: upsert, queries de seguimientos y respuestas
│       ├── SeguimientoEntity.kt ← Tabla "seguimientos"
│       ├── RespuestaEntity.kt   ← Tabla "respuestas"
│       └── PersistenciaPendiente.kt ← Guía de expansión futura
│
├── viewmodel/
│   └── MpazProViewModel.kt      ← Lógica de RF-11: confirmarMaterialRevisado(), progreso()
│
└── ui/screen/estudiante/
    ├── InicioEstudianteScreen.kt ← Barra de avance por asignatura (LinearProgressIndicator)
    ├── MaterialScreen.kt         ← Botón "¡Listo! Ir a los desafíos"
    ├── DesafiosScreen.kt         ← AlertDialog "¡Terminaste los desafíos!"
    ├── EvaluacionFinalScreen.kt  ← AlertDialog "¡Evaluación completada!"
    └── RevisionRespuestasScreen.kt
```

---

## Cómo abrir y ejecutar

1. Descomprimir el ZIP en una carpeta limpia.
2. Android Studio → **File › Open…** → seleccionar la carpeta que contiene `settings.gradle.kts`.
3. Esperar el **Gradle Sync** (requiere internet la primera vez; KSP generará el código de Room).
4. Ejecutar ▶ **Run 'app'**.
5. En el login, usar **"Modo desarrollo"** → perfil **Estudiante**.

### Usuarios de prueba

| Perfil | Usuario | Clave |
|---|---|---|
| Jefatura UTP | utp@mpazpro3.cl | utp123 |
| Docente 3° básico A | docente.3basicoA@mpazpro3.cl | docente123 |
| **Estudiante (RF-11)** | **est001** | **1234** |
