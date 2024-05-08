package com.fatherofapps.androidbase.utils

enum class EStatus {
    APPROVED,
    REJECTED,
    CANCELLED,
    COMPLETED,
    AWAITING_PAYMENT
}
fun convertStatusToVietnamese(status: EStatus): String {
    return when (status) {
        EStatus.APPROVED -> "Đã duyệt             "
        EStatus.REJECTED -> "Đã từ chối"
        EStatus.CANCELLED -> "Đã hủy              "
        EStatus.COMPLETED -> "Đã hoàn thành"
        EStatus.AWAITING_PAYMENT -> "Chờ thanh toán"
    }
}