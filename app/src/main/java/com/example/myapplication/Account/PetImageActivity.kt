package com.example.myapplication.Account

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.Fragment.AccountFragment
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_pet_image.*
import java.io.ByteArrayOutputStream

class PetImageActivity : AppCompatActivity() {

    private lateinit var binding: PetImageActivity
    private lateinit var auth: FirebaseAuth
    private lateinit var key: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_image)

        val uid = FBAuth.getUid()

        val back = findViewById<Button>(R.id.backbutton2)
        back.setOnClickListener {
            finish()
        }

        //갤러리 열기
        val petpic = findViewById<Button>(R.id.change)
        petpic.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }

        next.setOnClickListener {
            val intent = Intent(this, PetprofileActivity::class.java)
            startActivity(intent)

            imageupload()
        }
        getimagedata(uid)
    }

    //사진 불러오기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            val petpic = findViewById<ImageView>(R.id.pic)
            petpic.setImageURI(data?.data)
        }
    }

    //파이어베이스에 저장된 사진 띄우기
    private fun getimagedata(key: String) {
        val uid = FBAuth.getUid()
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(uid + "_petpic.jpg")

        // ImageView in your Activity
        val petpicFb = findViewById<ImageView>(R.id.pic)

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(petpicFb)
            } else {

            }
        }
    }

    //파이어베이스에 이미지 저장하기
    private fun imageupload() {
        val uid = FBAuth.getUid()
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(uid + "_petpic.jpg")
        val imageView = findViewById<ImageView>(R.id.pic)

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }


}