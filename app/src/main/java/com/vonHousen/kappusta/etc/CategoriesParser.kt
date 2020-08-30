package com.vonHousen.kappusta.etc

import com.vonHousen.kappusta.reporting.ExpenseType
import com.vonHousen.kappusta.reporting.ProfitType


class CategoriesParser(private val localCategoryNames: Array<String>) {

    private val categoryNames = arrayOf(
        ExpenseType.DAILY.toString(),
        ExpenseType.SPECIAL.toString(),
        ExpenseType.OTHER.toString(),
        ProfitType.SALARY.toString(),
        ProfitType.BONUS.toString(),
        ProfitType.ONE_TIME.toString()
    )   // TODO bind it with strings.xml

    init {
        if (categoryNames.size != localCategoryNames.size)
            throw Exception("Incorrect form of given local category names.")
    }

    fun convertToLocalCategoryName(categoryTxt: String?) : String {
        if (categoryTxt == null)
            return "Unknown category"

        for ((idx, name) in categoryNames.withIndex()) {
            if (categoryTxt == name)
                return localCategoryNames[idx]
        }
        return categoryTxt
    }

}