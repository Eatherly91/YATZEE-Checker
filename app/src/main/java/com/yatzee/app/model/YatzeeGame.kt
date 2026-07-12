package com.yatzee.app.model

enum class Section { UPPER, LOWER, SUMMARY }

enum class Category(
    val displayName: String,
    val section: Section
) {
    ONES("Ones", Section.UPPER),
    TWOS("Twos", Section.UPPER),
    THREES("Threes", Section.UPPER),
    FOURS("Fours", Section.UPPER),
    FIVES("Fives", Section.UPPER),
    SIXES("Sixes", Section.UPPER),
    THREE_OF_A_KIND("Three of a Kind", Section.LOWER),
    FOUR_OF_A_KIND("Four of a Kind", Section.LOWER),
    FULL_HOUSE("Full House", Section.LOWER),
    SMALL_STRAIGHT("Small Straight", Section.LOWER),
    LARGE_STRAIGHT("Large Straight", Section.LOWER),
    YATZEE("YATZEE", Section.LOWER),
    CHANCE("Chance", Section.LOWER);

    companion object {
        val upperCategories = values().filter { it.section == Section.UPPER }
        val lowerCategories = values().filter { it.section == Section.LOWER }
    }
}

enum class SummaryRow(val displayName: String) {
    UPPER_SUM("Upper Sum"),
    BONUS("Bonus (+35)"),
    LOWER_SUM("Lower Sum"),
    TOTAL("Grand Total")
}

class PlayerData(val name: String) {
    val scores: MutableMap<Category, Int?> = Category.values().associateWith { null }.toMutableMap()

    val upperSum: Int
        get() = Category.upperCategories.sumOf { scores[it] ?: 0 }

    val bonus: Int
        get() = if (upperSum >= 63) 35 else 0

    val lowerSum: Int
        get() = Category.lowerCategories.sumOf { scores[it] ?: 0 }

    val total: Int
        get() = upperSum + bonus + lowerSum
}
