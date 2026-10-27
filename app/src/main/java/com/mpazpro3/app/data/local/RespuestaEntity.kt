package com.mpazpro3.app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "respuestas",
    foreignKeys = [ForeignKey(
        entity = SeguimientoEntity::class,
        parentColumns = ["id"],
        childColumns = ["seguimientoId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class RespuestaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val seguimientoId: String,
    val actividadId: String,
    val alternativaElegida: Int,
    val esCorrecta: Boolean,
    val numeroIntento: Int
)