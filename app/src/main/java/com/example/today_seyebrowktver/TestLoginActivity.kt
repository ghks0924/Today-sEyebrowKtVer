package com.example.today_seyebrowktver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.today_seyebrowktver.databinding.ActivityTestLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class TestLoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int= 1111

    private lateinit var account:GoogleSignInAccount
    private lateinit var auth:FirebaseAuth
    private lateinit var loginProvider :String
    private lateinit var binding: ActivityTestLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
    }

    fun onClick(view: View) {
        when(view){
            binding.btn ->
                googleLogin()

        }
    }

    private fun googleLogin() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

       val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

       if (requestCode == RC_SIGN_IN){
           val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
           handleSingInResult(task)
       }
    }

    private fun handleSingInResult(completeTask: Task<GoogleSignInAccount>) {
        try {
            account = completeTask.getResult(ApiException::class.java)!!
            Log.e("error","firebaseAuthWithGoogle" + this.account.id)
            val expired = account.isExpired
            firebaseAuthWithGoogle(account.idToken)
        } catch (e:ApiException){

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    Log.e("error","signInWithCredential:success")
                    loginProvider = "google"
                    val user = auth.currentUser
                    updateUI(user)
                } else{
                    Log.e("error","signInWithCredential:failure ${task.exception}")
                    updateUI(null)
                }
            }
    }

    private fun updateUI(firebaseUser: FirebaseUser?){
        if (firebaseUser!=null){
            Log.e("error",
                """
                    ${firebaseUser.uid}
                    ${firebaseUser.email}
                    ${firebaseUser.displayName}
                    ${firebaseUser.providerData[0]?.providerId}
                    ${firebaseUser.providerData[1]?.providerId}
                    ${firebaseUser.photoUrl}
                    ${firebaseUser.photoUrl}
               
                    $loginProvider
                    """.trimIndent()

            )

//            updateDB(firebaseUser)

        }
    }

}