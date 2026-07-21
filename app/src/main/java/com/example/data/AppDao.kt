package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // --- Reports ---
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<Report>>

    @Query("SELECT * FROM reports WHERE id = :id LIMIT 1")
    fun getReportById(id: String): Flow<Report?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: Report)

    @Query("UPDATE reports SET status = :status WHERE id = :id")
    suspend fun updateReportStatus(id: String, status: String)

    @Query("DELETE FROM reports WHERE id = :id")
    suspend fun deleteReport(id: String)

    // --- Attachments ---
    @Query("SELECT * FROM attachments WHERE reportId = :reportId")
    fun getAttachmentsForReport(reportId: String): Flow<List<Attachment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachment(attachment: Attachment): Long

    // --- Audit Trail ---
    @Query("SELECT * FROM audit_trail ORDER BY timestamp DESC")
    fun getAllAuditLogs(): Flow<List<AuditTrailEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(entry: AuditTrailEntry)

    // --- Blockchain Ledger ---
    @Query("SELECT * FROM blockchain_ledger ORDER BY blockIndex DESC")
    fun getBlockchainLedger(): Flow<List<BlockchainBlock>>

    @Query("SELECT * FROM blockchain_ledger ORDER BY blockIndex DESC LIMIT 1")
    suspend fun getLatestBlock(): BlockchainBlock?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlock(block: BlockchainBlock)

    // --- Users ---
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}
