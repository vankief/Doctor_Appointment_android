package com.fatherofapps.androidbase.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun splitData(text: String): Array<String>? {
    // Biểu thức chính quy để phân tách dựa trên dấu "|" và ký tự "||"
    val regex = "\\|\\|?".toRegex() // Phân tách khi gặp || hoặc khi gặp dấu |

    // Phân tách đoạn văn bản thành các chuỗi nhỏ
    val parts = text.split(regex)
        .map { it.trim() } // Loại bỏ các khoảng trắng không mong muốn từ mỗi phần tử
        .toTypedArray()

    return parts
}
fun convertDate(dateString: String): String {
    // Định nghĩa định dạng ngày tháng ban đầu và định dạng muốn chuyển sang
    val originalFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    try {
        // Parse chuỗi ngày tháng ban đầu thành đối tượng Date
        val date = originalFormat.parse(dateString)

        // Chuyển đổi đối tượng Date sang chuỗi theo định dạng mới
        return targetFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // Trả về chuỗi rỗng nếu có lỗi xảy ra
    return ""
}