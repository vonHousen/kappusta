package com.vonHousen.kappusta.reporting

import com.vonHousen.kappusta.db.ProfitEntity
import java.time.LocalDate

enum class ProfitType(val ID: Int) {
    SALARY(1),
    BONUS(2),
    ONE_TIME(3),
    DUMMY(0);

    companion object {
        private val map = ProfitType.values().associateBy(ProfitType::ID)
        fun fromID(type: Int?) = if(type!=null) map[type] else DUMMY
    }

}   // TODO it should be bounded with names in string.xml

class ProfitRecord(
    private var worth: Money,
    private var profitType: ProfitType?,
    private var profitTagID: Int?,
    private var date: LocalDate?,
    private var comment: String?,
    private var createdDate: LocalDate?,
    private var editedDate: LocalDate?
) {

    constructor(profitEntity: ProfitEntity): this(
        worth = profitEntity.worth,
        profitType = ProfitType.fromID(profitEntity.profitTypeID),
        profitTagID = profitEntity.profitTagID,
        date = profitEntity.date,
        comment = profitEntity.comment,
        createdDate = profitEntity.createdDate,
        editedDate = profitEntity.editedDate
    )

    constructor(
        worth: Money,
        profitType: ProfitType,
        date: LocalDate?,
        comment: String? = null,
        profitTagID: Int? = null
    ): this(
        worth = worth,
        profitType = profitType,
        profitTagID = profitTagID,
        date = date,
        comment = comment,
        createdDate = null,
        editedDate = null
    )

    constructor(reportRecord: ReportRecord): this(
        worth = reportRecord.WORTH,
        profitType = ProfitType.valueOf(reportRecord.CATEGORY!!),
        profitTagID = null,
        date = reportRecord.DATE,
        comment = null,
        createdDate = null,
        editedDate = null   // TODO check if should be null
    )

    fun getHowMuch(): Money {
        return worth
    }

    fun getProfitType(): ProfitType? {
        return profitType
    }

    fun getProfitTagID(): Int? {
        return profitTagID
    }

    fun getDate(): LocalDate {
        return date ?: LocalDate.now()
    }

    fun getComment(): String? {
        return comment
    }
}