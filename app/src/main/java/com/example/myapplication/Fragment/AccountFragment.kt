package com.example.myapplication.Fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.myapplication.Account.PetImageActivity
import com.example.myapplication.Login.IntroActivity
import com.example.myapplication.Account.PetprofileActivity
import com.example.myapplication.Account.SendmailActivity
import com.example.myapplication.Auth.PetDataModel
import com.example.myapplication.Auth.UserDataModel
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.MemoInsideActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAccountBinding
import com.example.myapplication.memoModel
import com.example.myapplication.utils.FirebaseAuthUtils
import com.example.myapplication.utils.FirebaseRef
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_petprofile.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.ByteArrayOutputStream

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var key: String

    private val TAG = "AccountFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uid = FBAuth.getUid()
        val intent = Intent(context, PetDataModel::class.java)
        key = intent.getStringExtra("key").toString()

        getMyData()
        getpetimagedata(uid)
        getPetData(uid)
    }

    //파이어베이스에서 사진 가져오기
    private fun getpetimagedata(key: String) {
        val uid = FBAuth.getUid()
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(uid + "_petpic.jpg")

        // ImageView in your Activity
        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(btn_pet)
            } else {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)

        binding.btnPet.setOnClickListener {
            val intent = Intent(context, PetImageActivity::class.java)
            startActivity(intent)
        }

        binding.btnSuggest.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", "mail@mail.com", null))
            startActivity(Intent.createChooser(emailIntent, "전송"))
        }

        binding.btnLogout.setOnClickListener {

            Firebase.auth.signOut()

            val intent = Intent(context, IntroActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.homeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_accountFragment_to_homeFragment)
        }
        binding.boardTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_accountFragment_to_boardFragment)
        }
        binding.calendarTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_accountFragment_to_memoFragment)
        }
        binding.mapTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_accountFragment_to_mapFragment)
        }

        return binding.root

    }

    private fun getMyData() {

        val uid = FBAuth.getUid()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                my_name.text = data!!.nickname
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "load:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }

    private fun getPetData(uid : String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(PetDataModel::class.java)

                val myUid = FBAuth.getUid()
                val PetUid = data!!.uid

                if(myUid.equals(PetUid)) {

                    binding.petName.setText(data!!.petname)

                } else {

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FirebaseRef.petInfoRef.child(uid).addValueEventListener(postListener)
    }


    //이메일
    protected fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)

        emailIntent.data = Uri.fromParts("mailto", "patmilliar_@naver.com", null)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[건의사항]")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "[아이디]" +"\n"+ "\n"+
                "[건의내용]")

        try {
            startActivity(Intent.createChooser(emailIntent, "전송"))

        } catch (ex: ActivityNotFoundException) {

        }
    }
}