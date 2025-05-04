package com.tfgmanuel.dungeonvault.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Campaigns")
data class Campaign(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val img_name: String,
    val invite_code: String,
    val creator_id: Int
)
