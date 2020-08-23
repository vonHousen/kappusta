package com.vonHousen.kappusta.db.reportDB

import androidx.room.*
import java.time.LocalDate

@Entity(
    tableName = "PROFIT_TAGS"
)
data class ProfitTagEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profit_tag_id")
    var profitTagID: Int?,

    @ColumnInfo(name = "profit_tag")
    var profitTag: String,

    @ColumnInfo(name = "created_date")
    var createdDate: LocalDate

) {
    constructor(profitTag: String) : this(
        profitTagID = null,
        profitTag = profitTag,
        createdDate = LocalDate.now()
    )
}