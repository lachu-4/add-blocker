package com.zeroads.data.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

/**
 * Room Database for ZeroAds.
 * Stores blocked domains, traffic logs, and stats history.
 */
@Entity(tableName = "blocked_domains")
data class BlockedDomain(
    @PrimaryKey val domain: String,
    @ColumnInfo(name = "type") val type: String, // e.g., "Ad Server", "Tracker", "Malware"
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "traffic_logs")
data class TrafficLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "domain") val domain: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "is_blocked") val isBlocked: Boolean,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface ZeroAdsDao {
    @Query("SELECT * FROM blocked_domains")
    fun getAllBlockedDomains(): List<BlockedDomain>

    @Query("SELECT * FROM blocked_domains WHERE domain = :domain LIMIT 1")
    fun getBlockedDomain(domain: String): BlockedDomain?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBlockedDomain(domain: BlockedDomain)

    @Insert
    fun insertTrafficLog(log: TrafficLog)

    @Query("SELECT COUNT(*) FROM traffic_logs WHERE is_blocked = 1")
    fun getBlockedCount(): Int

    @Query("SELECT * FROM traffic_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecentLogs(): List<TrafficLog>
}

@Database(entities = [BlockedDomain::class, TrafficLog::class], version = 1)
abstract class ZeroAdsDatabase : RoomDatabase() {
    abstract fun zeroAdsDao(): ZeroAdsDao
}
