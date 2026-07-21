package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val department: String,
    val organization: String,
    val location: String,
    val date: String,
    val time: String,
    val witnessInfo: String?,
    val identityType: String, // "ANONYMOUS", "PROTECTED", "VERIFIED"
    val userRole: String,      // "Citizen", "Verified Citizen", "NGO", "Journalist", "Auditor"
    val status: String,        // "SUBMITTED", "UNDER_INVESTIGATION", "RESOLVED", "DISMISSED"
    val timestamp: Long,
    val cryptographicFingerprint: String,
    val chainOfCustodyRecord: String,
    val qrCodeData: String,
    val witnessCount: Int = 1,
    val reputationScore: Int = 100,
    val reporterUserId: String? = null,
    val reporterName: String? = null,
    val reporterEmail: String? = null,
    val reporterPhone: String? = null,
    val reporterVerificationId: String? = null,
    val isAnonymous: Boolean = false
)

@Entity(tableName = "attachments")
data class Attachment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reportId: String,
    val fileName: String,
    val fileType: String,          // "IMAGE", "VIDEO", "AUDIO", "DOCUMENT"
    val encryptedStoragePath: String,
    val sha256Hash: String,
    val sha3Hash: String,          // Mimicked SHA-3 hash for multiple verification layers
    val timestamp: Long,
    val isVerified: Boolean = true
)

@Entity(tableName = "audit_trail")
data class AuditTrailEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val action: String,            // "LOGIN", "REPORT_SUBMIT", "EVIDENCE_UPLOAD", "STATUS_CHANGE", "EVIDENCE_ACCESS", "BLOCKCHAIN_SYNC"
    val userId: String,
    val role: String,
    val deviceId: String,
    val ipMetadata: String,
    val details: String
)

@Entity(tableName = "blockchain_ledger")
data class BlockchainBlock(
    @PrimaryKey(autoGenerate = true) val blockIndex: Int = 0,
    val reportId: String,
    val evidenceHash: String,      // SHA-256 fingerprint of the report + attachments
    val timestamp: Long,
    val versionNumber: Int,
    val chainOfCustodyReference: String,
    val previousHash: String,
    val blockHash: String
)
