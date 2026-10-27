package com.mpazpro3.app.model

data class SeguimientoAvance(
    val estudianteId: String,
    val contenidoId: String,
    val materialRevisado: Boolean = false,
    val respuestas: List<RespuestaEstudiante> = emptyList(),
    val intentosEvaluacionUsados: Int = 0,
    val estado: EstadoAvance = EstadoAvance.NO_INICIADO
)

data class RespuestaEstudiante(
    val actividadId: String,
    val alternativaElegida: Int,
    val esCorrecta: Boolean,
    val numeroIntento: Int
)

enum class EstadoAvance {
    NO_INICIADO,
    EN_PROGRESO,
    FINALIZADO
}