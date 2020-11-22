package com.mywidget.chat.waiting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mywidget.MainApplication
import com.mywidget.R
import com.mywidget.chat.waiting.adapter.WatingRoomAdapter
import kotlinx.android.synthetic.main.activity_wating_room.*
import kotlinx.android.synthetic.main.layout_title.*
import javax.inject.Inject
import javax.inject.Named

class WaitingRoomActivity : AppCompatActivity() {

    /*private var database: DatabaseReference = FirebaseDatabase.getInstance()
        .reference.child("Room")*/
    @Inject @Named("Room") lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wating_room)

        (application as MainApplication).getApplicationCompoenet()
            .watingActivityComponent().create().inject(this)

        watingRoomRv.adapter = WatingRoomAdapter()

        val userAct = GoogleSignIn.getLastSignedInAccount(this)
        userAct?.let {
            login_name?.text = it.givenName
        }

        createRoom.setOnClickListener {
            val email = userAct?.email
            email?.let {
                val mEmail = it.substring(0, it.indexOf('@'))
                val result: HashMap<String, String> = hashMapOf()
                result["roomName"] = "콩이네방"

                val ref = database.child(mEmail).push()
                val key = ref.key
                ref.setValue(result)
            }

        }
    }
}