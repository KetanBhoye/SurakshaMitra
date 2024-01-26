package com.example.surakshamitra.data;

data class UserRegistrationData(
    val username: String = "",
    val agencyName: String = "",
    val agencyType: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val emailAddress: String = "",
    val totalMembers: String = "",
    val password: String = "",
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "username" to username,
            "agencyName" to agencyName,
            "agencyType" to agencyType,
            "address" to address,
            "phoneNumber" to phoneNumber,
            "emailAddress" to emailAddress,
            "totalMembers" to totalMembers,
            "password" to password,
        )
    }
}
