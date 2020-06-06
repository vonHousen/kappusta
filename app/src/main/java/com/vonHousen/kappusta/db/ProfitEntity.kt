package com.vonHousen.kappusta.db

import androidx.room.*
import com.vonHousen.kappusta.reporting.ProfitRecord
import java.time.LocalDate

@Entity(
    tableName = "PROFITS",
    foreignKeys = [ForeignKey(
        entity = ProfitTypeEntity::class,
        parentColumns = ["profit_type_id"],
        childColumns = ["profit_type_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProfitEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profit_id")
    var profitID: Int?,

    @ColumnInfo(name = "profit_type_id")
    var profitTypeID: Int?,

    @ColumnInfo(name = "worth")
    var worth: Double,

    @ColumnInfo(name = "date")
    var date: LocalDate

) {
    constructor(profitRecord: ProfitRecord) : this(
        profitID = null,
        profitTypeID = profitRecord.getProfitType()?.ID,
        worth = profitRecord.getHowMuch(),
        date = profitRecord.getDate()
    )
}