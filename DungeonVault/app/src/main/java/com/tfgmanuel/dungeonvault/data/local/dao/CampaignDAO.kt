package com.tfgmanuel.dungeonvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tfgmanuel.dungeonvault.data.model.Campaign

@Dao
interface CampaignDAO {

    @Insert
    suspend fun insert(campaign: Campaign)

    @Insert
    suspend fun insertAll(campaigns: List<Campaign>)

    @Query("SELECT * FROM Campaigns")
    suspend fun getAllCampaigns(): List<Campaign>

    @Query("SELECT * FROM Campaigns WHERE id = :id LIMIT 1")
    suspend fun getCampaignById(id: Int): Campaign?

    @Query("DELETE FROM Campaigns WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM campaigns")
    suspend fun deleteAll()
}