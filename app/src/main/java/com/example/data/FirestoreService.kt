package com.example.data

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object FirestoreService {
    private const val TAG = "FirestoreService"
    private const val REPORTS_COLLECTION = "secure_reports"
    private const val ATTACHMENTS_COLLECTION = "secure_attachments"

    private val _isFirebaseInitialized = MutableStateFlow(false)
    val isFirebaseInitialized: StateFlow<Boolean> = _isFirebaseInitialized

    private var firestoreInstance: FirebaseFirestore? = null

    fun initialize(context: Context) {
        try {
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                _isFirebaseInitialized.value = true
                firestoreInstance = FirebaseFirestore.getInstance()
                Log.d(TAG, "Firebase already initialized. Firestore instance retrieved.")
            } else {
                FirebaseApp.initializeApp(context)
                _isFirebaseInitialized.value = true
                firestoreInstance = FirebaseFirestore.getInstance()
                Log.d(TAG, "Firebase initialized successfully. Firestore configured.")
            }
        } catch (e: Exception) {
            _isFirebaseInitialized.value = false
            firestoreInstance = null
            Log.w(TAG, "Firebase initialization skipped: ${e.localizedMessage}. Running in Local-Only Offline Cryptographic mode.")
        }
    }

    private fun getFirestore(): FirebaseFirestore? {
        if (firestoreInstance == null) {
            try {
                firestoreInstance = FirebaseFirestore.getInstance()
                _isFirebaseInitialized.value = true
            } catch (e: Exception) {
                _isFirebaseInitialized.value = false
            }
        }
        return firestoreInstance
    }

    fun syncReport(report: Report, onComplete: (Boolean, String?) -> Unit = { _, _ -> }) {
        val db = getFirestore() ?: run {
            onComplete(false, "Firebase Firestore is not initialized or unavailable in this environment.")
            return
        }

        val encryptedReport = hashMapOf(
            "id" to report.id,
            // Sensitive fields are fully encrypted via AES-256 before transmission
            "title" to SovereignCryptography.encrypt(report.title),
            "description" to SovereignCryptography.encrypt(report.description),
            "location" to SovereignCryptography.encrypt(report.location),
            "witnessInfo" to report.witnessInfo?.let { SovereignCryptography.encrypt(it) },
            // Metadata is stored in a structured, unencrypted format for indexing and compliance routing
            "department" to report.department,
            "organization" to report.organization,
            "date" to report.date,
            "time" to report.time,
            "identityType" to report.identityType,
            "userRole" to report.userRole,
            "status" to report.status,
            "timestamp" to report.timestamp,
            "cryptographicFingerprint" to report.cryptographicFingerprint,
            "chainOfCustodyRecord" to report.chainOfCustodyRecord,
            "qrCodeData" to report.qrCodeData,
            "witnessCount" to report.witnessCount,
            "reputationScore" to report.reputationScore,
            "isEncrypted" to true,
            "encryptionAlgorithm" to "AES-256-CBC"
        )

        db.collection(REPORTS_COLLECTION)
            .document(report.id)
            .set(encryptedReport, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Report ${report.id} successfully synced to secure Firestore ledger.")
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to sync report ${report.id} to Firestore: ${e.localizedMessage}")
                onComplete(false, e.localizedMessage)
            }
    }

    fun updateReportStatus(reportId: String, status: String, onComplete: (Boolean) -> Unit = {}) {
        val db = getFirestore() ?: run {
            onComplete(false)
            return
        }

        db.collection(REPORTS_COLLECTION)
            .document(reportId)
            .update("status", status)
            .addOnSuccessListener {
                Log.d(TAG, "Status for report $reportId updated in Firestore: $status")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to update status in Firestore for report $reportId: ${e.localizedMessage}")
                onComplete(false)
            }
    }

    fun syncAttachment(attachment: Attachment, onComplete: (Boolean) -> Unit = {}) {
        val db = getFirestore() ?: run {
            onComplete(false)
            return
        }

        val encryptedAttachment = hashMapOf(
            "id" to attachment.id,
            "reportId" to attachment.reportId,
            "fileName" to SovereignCryptography.encrypt(attachment.fileName),
            "fileType" to attachment.fileType,
            "encryptedStoragePath" to attachment.encryptedStoragePath,
            "sha256Hash" to attachment.sha256Hash,
            "sha3Hash" to attachment.sha3Hash,
            "timestamp" to attachment.timestamp,
            "isVerified" to attachment.isVerified
        )

        val docId = if (attachment.id == 0) "temp_${System.currentTimeMillis()}" else attachment.id.toString()
        db.collection(ATTACHMENTS_COLLECTION)
            .document(docId)
            .set(encryptedAttachment, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Attachment $docId synced successfully to Firestore.")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to sync attachment to Firestore: ${e.localizedMessage}")
                onComplete(false)
            }
    }
}
