package com.example.myapplication.FireBase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object {
        private val database = Firebase.database

        val memoRef = database.getReference("memo").child(Firebase.auth.currentUser!!.uid)

        val boardRef = database.getReference("board")

        val catRef = database.getReference("cat")

        val rabbitRef = database.getReference("rabbit")

        val commentRef = database.getReference("comment")

        val birldRef = database.getReference("birld")

        val QueRef = database.getReference("Question")

        val ReptileRef = database.getReference("Reptile")

        val RecRef = database.getReference("Recommend")

        val fishRef = database.getReference("fish")

        val freeRef = database.getReference("free")
    }
}