package com.mpazpro3.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SeguimientoDao {
    // Consultas nuevas para leer al iniciar la app
    @Query("SELECT * FROM seguimientos")
    suspend fun getTodosLosSeguimientos(): List<SeguimientoEntity>

    @Query("SELECT * FROM respuestas")
    suspend fun getTodasLasRespuestas(): List<RespuestaEntity>

    @Query("SELECT * FROM seguimientos WHERE estudianteId = :estudianteId")
    fun getSeguimientosDeEstudiante(estudianteId: String): Flow<List<SeguimientoEntity>>

    @Query("SELECT * FROM seguimientos WHERE estudianteId = :estudianteId AND contenidoId = :contenidoId")
    suspend fun getSeguimiento(estudianteId: String, contenidoId: String): SeguimientoEntity?

    @Upsert
    suspend fun upsertSeguimiento(seg: SeguimientoEntity)

    @Query("SELECT * FROM respuestas WHERE seguimientoId = :seguimientoId")
    fun getRespuestas(seguimientoId: String): Flow<List<RespuestaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRespuesta(respuesta: RespuestaEntity)
}