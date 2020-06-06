package com.vonHousen.kappusta.db

import androidx.room.*
import com.vonHousen.kappusta.reporting.ProfitType

@Entity(
    tableName = "PROFIT_TYPES",
    indices = [Index(value = ["profit_type"], unique = true)]
)
data class ProfitTypeEntity (

    @PrimaryKey
    @ColumnInfo(name = "profit_type_id")
    var profitTypeID: Int,

    @ColumnInfo(name = "profit_type")
    var profitType: String

) {
    constructor(profitType: ProfitType) : this(
        profitTypeID = profitType.ID,
        profitType = profitType.toString()
    )
}