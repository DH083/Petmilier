package com.example.myapplication.Account

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import com.example.myapplication.Auth.PetDataModel
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.Fragment.AccountFragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.utils.FirebaseRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_petprofile.*
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.concurrent.TimeUnit

class PetprofileActivity : AppCompatActivity() {

    private lateinit var binding: PetprofileActivity
    private lateinit var auth: FirebaseAuth
    private lateinit var key: String

    private val TAG = PetprofileActivity::class.java.simpleName

    var petname : String? = null
//    var typeV : String? = null
    var typeDt : String? = null
    var sex : String? = null
    var bir : String? = null
    var meet : String? = null
    var del : String? = null
    var date : String? = null

//    private var petname  = ""
//    private var typeV = ""
//    private var typeDt = ""
//    private var sex = ""
//    private var bir = ""
//    private var meet = ""
//    private var del = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth
        val uid = FBAuth.getUid()
        key = intent.getStringExtra("key").toString()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petprofile)

        val back = findViewById<Button>(R.id.backbtn)
        back.setOnClickListener {
            finishAffinity()
            finish()
        }

//        getimagedata(uid)
        getPetData(uid)



//        //갤러리 열기
//        val petpic = findViewById<ImageView>(R.id.btn_petpic)
//        petpic.setOnClickListener {
//            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            startActivityForResult(gallery, 100)
//        }

        //생일 입력
        val datebir = findViewById<Button>(R.id.btn_date_bir)
        datebir.setOnClickListener {

            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(
                    view: DatePicker?, year: Int, month: Int, dayOfMonth: Int
                ) {

                    //선택한 날짜 출력하기
                    findViewById<TextView>(R.id.text_bir).setText("${year}년 ${month + 1}월 ${dayOfMonth}일")
                }
            }, year, month, date)
            dlg.show()

        }

        var calendar_start = Calendar.getInstance()

        //만난 날 입력
        val datemeet = findViewById<Button>(R.id.btn_date_meet)
        datemeet.setOnClickListener {

            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(
                    view: DatePicker?, year: Int, month: Int, dayOfMonth: Int
                ) {

                    calendar_start.set(year, month, dayOfMonth - 1)
                    val endDate = Calendar.getInstance()
                    val finalDate = TimeUnit.MILLISECONDS.toDays(endDate.timeInMillis - calendar_start.timeInMillis)

                    //선택한 날짜 출력하기
                    findViewById<TextView>(R.id.text_meet).setText("${year}년 ${month + 1}월 ${dayOfMonth}일")
                    findViewById<TextView>(R.id.d_day).setText(finalDate.toString())
                }
            }, year, month, date)
            dlg.show()
        }

        val savebtn = findViewById<Button>(R.id.save_btn)

        savebtn.setOnClickListener {

            //유저 uid 불러오기
            val uid = FBAuth.getUid()

            petname = findViewById<EditText>(R.id.pro_petname).text.toString()
//            typeV = findViewById<Spinner>(R.id.sp_type).selectedItem.toString()
            typeDt = findViewById<EditText>(R.id.text_type).text.toString()
//            sex = when (findViewById<RadioGroup>(R.id.sex_chk).checkedRadioButtonId) {
//                R.id.sex_male -> {"남아"}
//                R.id.sex_female -> {"여아"}
//                R.id.sex_non -> {"구분없음"}
//                else -> {""}
//            }
            sex = findViewById<TextView>(R.id.text_sex).text.toString()
            bir = findViewById<TextView>(R.id.text_bir).text.toString()
            meet = findViewById<TextView>(R.id.text_meet).text.toString()
//            del = when (findViewById<RadioGroup>(R.id.del_chk).checkedRadioButtonId) {
//                R.id.yes -> {"했음"}
//                R.id.no -> {"안했음"}
//                else -> {""}
//            }
            del = findViewById<TextView>(R.id.text_del).text.toString()
            date = findViewById<TextView>(R.id.d_day).text.toString()

//            imageupload()

            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)

            //저장위치
            FirebaseRef.petInfoRef
                .child(uid)
                .setValue(PetDataModel(uid, petname, typeDt, sex, bir, meet, del, date))

        }
    }

//    //사진 불러오기
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode == 100) {
//            val petpic = findViewById<ImageButton>(R.id.btn_petpic)
//            petpic.setImageURI(data?.data)
//        }
//    }

    //파이어베이스에 저장된 사진 띄우기
    private fun getimagedata(key: String) {
        val uid = FBAuth.getUid()
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(uid + "_petpic.jpg")

        // ImageView in your Activity
        val petpinFb = findViewById<ImageButton>(R.id.btn_petpic)

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(petpinFb)
            } else {

            }
        }
    }

    private fun getPetData(uid: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val data = dataSnapshot.getValue(PetDataModel::class.java)

                    val uid = FBAuth.getUid()

                    val myUid = FBAuth.getUid()
                    val PetUid = data!!.uid

                    if (myUid.equals(PetUid)) {

                        pro_petname.setText(data!!.petname)
                        text_type.setText(data!!.typeDt)
                        text_sex.setText(data!!.sex)
                        text_bir.text = data!!.bir
                        text_meet.text = data!!.meet
                        text_del.setText(data!!.del)
                        //                        sp_type.selectedItem.toString(data!!)
//                sex_chk.checkedRadioButtonId = when {data!!.sex}

                    } else {

                    }

                } catch (e: Exception) {

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FirebaseRef.petInfoRef.child(uid).addValueEventListener(postListener)
    }




//    //파이어베이스에 이미지 저장하기
//    private fun imageupload() {
//        val uid = FBAuth.getUid()
//        val storage = Firebase.storage
//        val storageRef = storage.reference
//        val mountainsRef = storageRef.child(uid + "_petpic.jpg")
//        val imageView = findViewById<ImageButton>(R.id.btn_petpic)
//
//        imageView.isDrawingCacheEnabled = true
//        imageView.buildDrawingCache()
//        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val data = baos.toByteArray()
//
//        var uploadTask = mountainsRef.putBytes(data)
//        uploadTask.addOnFailureListener {
//            // Handle unsuccessful uploads
//        }.addOnSuccessListener { taskSnapshot ->
//            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//            // ...
//        }
//    }
}
