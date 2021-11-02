package com.hardextech.store.model


// THIS DATA CLASS CONTAINS DETAILS/VALUES ABOUT THE USER
class User(
    // start of the user details to be stored
    val id:String ="",
    val firstName:String= "",
    val lastName:String= "",
    val  email:String="",
    val image:String="",
    val gender:String="",
    val mobileNumber:Long=0,
    val contactAddress: String ="",
    val profileCompletion: Int = 0
// end of the user details stored in the Firestore
)