package com.fatherofapps.androidbase.data.request

data class DoctorFilter(
    val searchTerm: String,
    val name: String,
    val specialist: String,
    val rating: Int,
) {
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        if (searchTerm.isNotEmpty()) {
            map["searchTerm"] = searchTerm
        }
        if (name.isNotEmpty()) {
            map["name"] = name
        }
        if (specialist.isNotEmpty()) {
            map["specialist"] = specialist
        }
        if (rating > 0) {
            map["rating"] = rating
        }
        return map
    }
}