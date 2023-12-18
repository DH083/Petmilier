package com.example.myapplication.Fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.myapplication.Auth.PetDataModel
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.delay

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uid = FBAuth.getUid()

        getimagedata(uid)
        getPetData(uid)

//        mentlist()
    }

    //파이어베이스에서 사진 가져오기
    private fun getimagedata(key: String) {
        val uid = FBAuth.getUid()
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(uid + "_petpic.jpg")

        // ImageView in your Activity
        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(pet_image)
            } else {

            }
        }
    }

    private fun getPetData(uid: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var data = dataSnapshot.getValue(PetDataModel::class.java)

                val myUid = FBAuth.getUid()
                val PetUid = data!!.uid
                if (myUid.equals(PetUid)) {

                    binding.viewPetname.setText(data!!.petname)
                    binding.dDate.setText(data!!.date)

                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FirebaseRef.petInfoRef.child(uid).addValueEventListener(postListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.boardTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_boardFragment)
        }
        binding.mapTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_mapFragment)
        }
        binding.calendarTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_memoFragment)
        }
        binding.accountTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_accountFragment)
        }
        return binding.root


    }

}