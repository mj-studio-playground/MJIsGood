package happy.mjstudio.sopt27.utils

import java.util.*

object CalendarFormatter {
    fun parseToUiText(calendar: Calendar): String {
        return "${calendar[Calendar.YEAR]}년 ${calendar[Calendar.MONTH] + 1}월 ${calendar[Calendar.DAY_OF_MONTH]}일 ${calendar[Calendar.HOUR]}:${calendar[Calendar.MINUTE]}"
    }
}