package com.vonHousen.kappusta.db.authDB

import androidx.room.*
import java.time.LocalDate

@Entity(
    tableName = "REPORT_DB_IDS",
    indices = [
        Index(value = ["uid_hashed"], unique = true)
    ]
)
data class ReportDBIdentifierEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "uid_hashed")
    var uidHashed: String,

    @ColumnInfo(name = "report_db_name")
    var reportDBName: String?,

    @ColumnInfo(name = "created_date")
    var createdDate: LocalDate,

    @ColumnInfo(name = "edited_date")
    var editedDate: LocalDate

) {
    constructor(uidHashed: String) : this(
        id = null,
        uidHashed = uidHashed,
        reportDBName = null,
        createdDate = LocalDate.now(),
        editedDate = LocalDate.now()
    )
    constructor(uidHashed: String, reportDBName: String) : this(
        id = null,
        uidHashed = uidHashed,
        reportDBName = reportDBName,
        createdDate = LocalDate.now(),
        editedDate = LocalDate.now()
    )
}
