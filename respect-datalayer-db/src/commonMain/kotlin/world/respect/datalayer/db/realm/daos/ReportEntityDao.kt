package world.respect.datalayer.db.realm.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.db.realm.entities.ReportEntity

@Dao
interface  ReportEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putReport(reportEntity: ReportEntity)

    @Query("""
        SELECT * 
        FROM ReportEntity
        WHERE reportIsTemplate = :template
    """)
    fun getAllReportsByTemplate(template: Boolean): Flow<List<ReportEntity>>

    @Query("""
        SELECT * 
         FROM ReportEntity
        WHERE reportId = :reportId
    """)
    suspend fun getReportAsync(reportId: String): ReportEntity?

    @Query("""
        SELECT * 
         FROM ReportEntity
        WHERE reportId = :reportId
    """)
    fun getReportAsFlow(reportId: String): Flow<ReportEntity?>

    @Update
    suspend fun updateReport(entity: ReportEntity)
}