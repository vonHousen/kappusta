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
    private var worth: Double,
    private var profitType: ProfitType?,
    private var date: LocalDate?
) {

    constructor(profitEntity: ProfitEntity): this(
        worth = profitEntity.worth,
        profitType = ProfitType.fromID(profitEntity.profitTypeID),
        date = profitEntity.date
    )

    fun getHowMuch(): Double {
        return worth
    }

    fun getProfitType(): ProfitType? {
        return profitType
    }

    fun getDate(): LocalDate {
        return date ?: LocalDate.now()
    }
}