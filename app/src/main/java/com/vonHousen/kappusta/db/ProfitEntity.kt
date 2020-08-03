package com.vonHousen.kappusta.db

import androidx.room.*
import com.vonHousen.kappusta.reporting.Money
import com.vonHousen.kappusta.reporting.ProfitRecord
import java.time.LocalDate

@Entity(
    tableName = "PROFITS",
    foreignKeys = [
        ForeignKey(
            entity = ProfitTypeEntity::class,
            parentColumns = ["profit_type_id"],
            childColumns = ["profit_type_id"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = ProfitTagEntity::class,
            parentColumns = ["profit_tag_id"],
            childColumns = ["profit_tag_id"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class ProfitEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profit_id")
    var profitID: Int?,

    @ColumnInfo(name = "profit_type_id", index = true)
    var profitTypeID: Int,

    @ColumnInfo(name = "profit_tag_id", index = true)
    var profitTagID: Int?,

    @ColumnInfo(name = "worth")
    var worth: Money,

    @ColumnInfo(name = "date")
    var date: LocalDate,

    @ColumnInfo(name = "comment")
    var comment: String?,

    @ColumnInfo(name = "created_date")
    var createdDate: LocalDate,

    @ColumnInfo(name = "edited_date")
    var editedDate: LocalDate

) {
    constructor(profitRecord: ProfitRecord) : this(
        profitID = null,
        profitTypeID = profitRecord.getProfitType()!!.ID,           // TODO check that out
        profitTagID = profitRecord.getProfitTagID(),
        worth = profitRecord.getHowMuch(),
        date = profitRecord.getDate(),
        comment = profitRecord.getComment(),
        createdDate = LocalDate.now(),
        editedDate = LocalDate.now()
    )
}