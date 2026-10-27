package com.mpazpro3.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seguimientos")
data class SeguimientoEntity(
    @PrimaryKey val id: String, // formato: "estudianteId-contenidoId"
    val estudianteId: String,
    val contenidoId: String,
    val materialRevisado: Boolean,
    val intentosEvaluacionUsados: Int,
    val estado: String
)