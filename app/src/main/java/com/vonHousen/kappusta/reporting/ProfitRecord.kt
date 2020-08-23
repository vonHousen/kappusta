package com.vonHousen.kappusta.reporting

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
    private var profitTag: String?,
    private var profitTagID: Int?,
    private var date: LocalDate?,
    private var comment: String?,
    private var createdDate: LocalDate?,
    private var editedDate: LocalDate?
) {


    constructor(
        worth: Money,
        profitType: ProfitType,
        date: LocalDate?,
        comment: String? = null,
        profitTagID: Int? = null,
        profitTag: String? = null
    ): this(
        worth = worth,
        profitType = profitType,
        profitTag = profitTag,
        profitTagID = profitTagID,
        date = date,
        comment = comment,
        createdDate = null,
        editedDate = null
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

    fun getProfitTag(): String? {
        return profitTag
    }
}