package com.example.crackup.model

data class UserModel(
    var id: String = "",
    var email: String = "",
    var username: String = "",
    var profilePic: String = "",
    var uploadVideo: MutableList<String> = mutableListOf(),
    var followerList: MutableList<String> = mutableListOf(),
    var followingList: MutableList<String> = mutableListOf()
)