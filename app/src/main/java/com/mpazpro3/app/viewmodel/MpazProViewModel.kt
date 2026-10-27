package com.mpazpro3.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mpazpro3.app.data.MockData
import com.mpazpro3.app.data.local.AppDatabase
import com.mpazpro3.app.data.local.RespuestaEntity
import com.mpazpro3.app.data.local.SeguimientoEntity
import com.mpazpro3.app.model.Actividad
import com.mpazpro3.app.model.Asignacion
import com.mpazpro3.app.model.Asignatura
import com.mpazpro3.app.model.Contenido
import com.mpazpro3.app.model.EstadoAvance
import com.mpazpro3.app.model.EstadoAprobacion
import com.mpazpro3.app.model.Estudiante
import com.mpazpro3.app.model.InformeDocente
import com.mpazpro3.app.model.Perfil
import com.mpazpro3.app.model.RecursoDidactico
import com.mpazpro3.app.model.RespuestaEstudiante
import com.mpazpro3.app.model.SeguimientoAvance
import com.mpazpro3.app.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MpazProViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).seguimientoDao()

    private val _seguimientos = MutableStateFlow(MockData.seguimientos)
    val seguimientos: StateFlow<List<SeguimientoAvance>> = _seguimientos.asStateFlow()

    init {
        viewModelScope.launch {
            val segsDb = dao.getTodosLosSeguimientos()
            val respDb = dao.getTodasLasRespuestas()

            if (segsDb.isNotEmpty()) {
                val convertidos = segsDb.map { entidad ->
                    val respuestasEntidad = respDb.filter { it.seguimientoId == entidad.id }
                    val respuestasConvertidas = respuestasEntidad.map { r ->
                        RespuestaEstudiante(r.actividadId, r.alternativaElegida, r.esCorrecta, r.numeroIntento)
                    }
                    SeguimientoAvance(
                        estudianteId = entidad.estudianteId,
                        contenidoId = entidad.contenidoId,
                        materialRevisado = entidad.materialRevisado,
                        respuestas = respuestasConvertidas,
                        intentosEvaluacionUsados = entidad.intentosEvaluacionUsados,
                        estado = EstadoAvance.valueOf(entidad.estado)
                    )
                }
                _seguimientos.value = convertidos
            }
        }
    }

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo: StateFlow<Usuario?> = _usuarioActivo.asStateFlow()

    private val _errorLogin = MutableStateFlow<String?>(null)
    val errorLogin: StateFlow<String?> = _errorLogin.asStateFlow()

    fun login(usuario: String, clave: String): Boolean {
        _errorLogin.value = "Login sin implementar."
        return false
    }

    fun entrarModoDesarrollo(perfil: Perfil) {
        _usuarioActivo.value = MockData.usuarios.first { it.perfil == perfil }
        _errorLogin.value = null
    }

    fun logout() {
        _usuarioActivo.value = null
        _errorLogin.value = null
        _contenidoSeleccionadoId.value = null
        _estudianteSeleccionadoId.value = null
    }

    private val _contenidos = MutableStateFlow(MockData.contenidos)
    val contenidos: StateFlow<List<Contenido>> = _contenidos.asStateFlow()

    private val _recursos = MutableStateFlow(MockData.recursos)
    private val _actividades = MutableStateFlow(MockData.actividades)

    private val _asignaciones = MutableStateFlow(MockData.asignaciones)
    val asignaciones: StateFlow<List<Asignacion>> = _asignaciones.asStateFlow()

    private val _estudiantes = MutableStateFlow(MockData.estudiantes)
    val estudiantes: StateFlow<List<Estudiante>> = _estudiantes.asStateFlow()

    private val _informes = MutableStateFlow<List<InformeDocente>>(emptyList())
    val informes: StateFlow<List<InformeDocente>> = _informes.asStateFlow()

    private val _contenidoSeleccionadoId = MutableStateFlow<String?>(null)
    val contenidoSeleccionadoId: StateFlow<String?> = _contenidoSeleccionadoId.asStateFlow()

    private val _estudianteSeleccionadoId = MutableStateFlow<String?>(null)
    val estudianteSeleccionadoId: StateFlow<String?> = _estudianteSeleccionadoId.asStateFlow()

    fun seleccionarContenido(id: String?) { _contenidoSeleccionadoId.value = id }
    fun seleccionarEstudiante(id: String?) { _estudianteSeleccionadoId.value = id }
    fun contenidoPorId(id: String?): Contenido? = _contenidos.value.find { it.id == id }
    fun estudiantePorId(id: String?): Estudiante? = _estudiantes.value.find { it.id == id }
    fun recursosDe(contenidoId: String?): List<RecursoDidactico> = _recursos.value.filter { it.contenidoId == contenidoId }
    fun actividadPorId(id: String): Actividad? = _actividades.value.find { it.id == id }

    fun contenidosFiltrados(asignatura: Asignatura?, estado: EstadoAprobacion?): List<Contenido> = _contenidos.value
    fun aprobarContenido(contenidoId: String, observacion: String) {}
    fun rechazarContenido(contenidoId: String, observacion: String) {}
    fun contenidosDisponiblesParaAsignar(): List<Contenido> = _contenidos.value
    fun asignarAlCurso(curso: String, contenidoIds: List<String>, maxIntentos: Int) {}
    fun asignacionesDelCurso(curso: String): List<Asignacion> = _asignaciones.value
    fun progresoGeneral(estudianteId: String): Float = 0f
    fun descargarResultados(curso: String) {}
    fun informeDe(estudianteId: String?): InformeDocente? = _informes.value.find { it.estudianteId == estudianteId }
    fun generarInforme(estudianteId: String) {}
    fun validarInforme(informeId: String) {}
    fun contenidosDelEstudiante(usuario: Usuario?): List<Contenido> = _contenidos.value
    fun desafiosDe(contenidoId: String?): List<Actividad> = _actividades.value
    fun evaluacionFinalDe(contenidoId: String?): List<Actividad> = _actividades.value
    fun intentosRestantes(estudianteId: String, contenidoId: String): Int? = null
    fun respuestasDe(estudianteId: String, contenidoId: String): List<RespuestaEstudiante> = emptyList()

    fun progreso(estudianteId: String, contenidoId: String): Float {
        val seg = _seguimientos.value.find {
            it.estudianteId == estudianteId && it.contenidoId == contenidoId
        } ?: return 0f

        val desafios = desafiosDe(contenidoId)
        val evaluacion = evaluacionFinalDe(contenidoId)

        val totalPasos = 1 + desafios.size + evaluacion.size
        if (totalPasos == 0) return 0f

        val pasosMaterial = if (seg.materialRevisado) 1 else 0
        val idsDesafios = desafios.map { it.id }.toSet()
        val idsEval = evaluacion.map { it.id }.toSet()

        val respondidosDesafio = seg.respuestas.count { it.actividadId in idsDesafios }
        val respondidosEval = seg.respuestas.count { it.actividadId in idsEval }

        val progresoBruto = (pasosMaterial + respondidosDesafio + respondidosEval).toFloat() / totalPasos
        return progresoBruto.coerceIn(0f, 1f)
    }

    fun confirmarMaterialRevisado(estudianteId: String, contenidoId: String) {
        val actual = _seguimientos.value.find { it.estudianteId == estudianteId && it.contenidoId == contenidoId }

        val nuevoEstado = if (actual != null) {
            actual.copy(materialRevisado = true, estado = EstadoAvance.EN_PROGRESO)
        } else {
            SeguimientoAvance(estudianteId, contenidoId, true, emptyList(), 0, EstadoAvance.EN_PROGRESO)
        }

        _seguimientos.value = _seguimientos.value.filterNot {
            it.estudianteId == estudianteId && it.contenidoId == contenidoId
        } + nuevoEstado

        viewModelScope.launch {
            val entity = SeguimientoEntity(
                id = "$estudianteId-$contenidoId",
                estudianteId = estudianteId,
                contenidoId = contenidoId,
                materialRevisado = true,
                intentosEvaluacionUsados = nuevoEstado.intentosEvaluacionUsados,
                estado = EstadoAvance.EN_PROGRESO.name
            )
            dao.upsertSeguimiento(entity)
        }
    }

    fun responderDesafio(estudianteId: String, actividad: Actividad, alternativa: Int): Boolean? {
        val esCorrecta = actividad.alternativas.getOrNull(alternativa)?.esCorrecta == true
        val respuesta = RespuestaEstudiante(actividad.id, alternativa, esCorrecta, 1)

        val actual = _seguimientos.value.find { it.estudianteId == estudianteId && it.contenidoId == actividad.contenidoId }
        val nuevoEstado = actual?.copy(
            respuestas = actual.respuestas + respuesta,
            estado = EstadoAvance.EN_PROGRESO
        ) ?: SeguimientoAvance(estudianteId, actividad.contenidoId, false, listOf(respuesta), 0, EstadoAvance.EN_PROGRESO)

        _seguimientos.value = _seguimientos.value.filterNot {
            it.estudianteId == estudianteId && it.contenidoId == actividad.contenidoId
        } + nuevoEstado

        viewModelScope.launch {
            val segId = "$estudianteId-${actividad.contenidoId}"
            dao.upsertSeguimiento(SeguimientoEntity(
                segId, estudianteId, actividad.contenidoId, nuevoEstado.materialRevisado, nuevoEstado.intentosEvaluacionUsados, EstadoAvance.EN_PROGRESO.name
            ))
            dao.insertRespuesta(RespuestaEntity(
                seguimientoId = segId, actividadId = actividad.id, alternativaElegida = alternativa, esCorrecta = esCorrecta, numeroIntento = 1
            ))
        }
        return esCorrecta
    }

    fun enviarEvaluacion(estudianteId: String, contenidoId: String, respuestas: Map<String, Int>) {
        val evaluacion = evaluacionFinalDe(contenidoId)
        val nuevasRespuestas = evaluacion.mapNotNull { actividad ->
            val alternativaElegida = respuestas[actividad.id] ?: return@mapNotNull null
            val esCorrecta = actividad.alternativas.getOrNull(alternativaElegida)?.esCorrecta == true
            RespuestaEstudiante(actividad.id, alternativaElegida, esCorrecta, (intentosRestantes(estudianteId, contenidoId) ?: 1))
        }

        val actual = _seguimientos.value.find { it.estudianteId == estudianteId && it.contenidoId == contenidoId }
        val nuevoEstado = actual?.copy(
            respuestas = actual.respuestas + nuevasRespuestas,
            intentosEvaluacionUsados = actual.intentosEvaluacionUsados + 1,
            estado = EstadoAvance.FINALIZADO
        ) ?: SeguimientoAvance(estudianteId, contenidoId, false, nuevasRespuestas, 1, EstadoAvance.FINALIZADO)

        _seguimientos.value = _seguimientos.value.filterNot {
            it.estudianteId == estudianteId && it.contenidoId == contenidoId
        } + nuevoEstado

        viewModelScope.launch {
            val segId = "$estudianteId-$contenidoId"
            dao.upsertSeguimiento(SeguimientoEntity(
                segId, estudianteId, contenidoId, nuevoEstado.materialRevisado, nuevoEstado.intentosEvaluacionUsados, EstadoAvance.FINALIZADO.name
            ))
            nuevasRespuestas.forEach { resp ->
                dao.insertRespuesta(RespuestaEntity(
                    seguimientoId = segId, actividadId = resp.actividadId, alternativaElegida = resp.alternativaElegida, esCorrecta = resp.esCorrecta, numeroIntento = resp.numeroIntento
                ))
            }
        }
    }
}
