package com.vonHousen.kappusta.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.vonHousen.kappusta.MainActivity
import com.vonHousen.kappusta.R
import com.vonHousen.kappusta.etc.RC_SIGN_IN
import kotlinx.android.synthetic.main.fragment_authentication.*


class AuthenticateFragment : Fragment() {

    private lateinit var authViewModel: AuthenticateViewModel
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private val args: AuthenticateFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        authViewModel =
            ViewModelProviders.of(requireActivity()).get(AuthenticateViewModel::class.java)

        configureGoogleSignIn()
        button_google_sign_in.setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()

        val doSignOutOnStart: Boolean = args.doSignOutOnStart
        if (doSignOutOnStart) {
            signOut()
        }
        else {
            // Check for existing Google Sign In account, if the user is already signed in
            // the GoogleSignInAccount will be non-null.
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // User is signed in
                startWithUser(currentUser)
            }
        }
    }

    private fun configureGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val parent = activity as MainActivity
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(parent, gso)
        auth = FirebaseAuth.getInstance()
    }

    private fun signIn() {
        val parent = activity as MainActivity
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        parent.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun signOut() {
        authViewModel.withdrawUser()
        auth.signOut()
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)!!

            // Google Sign In was successful, authenticate with Firebase
            firebaseAuthWithGoogle(account.idToken!!)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            Snackbar.make(
                requireView(),
                "Firebase authentication failed.",
                Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val parent = activity as MainActivity
        auth.signInWithCredential(credential)
            .addOnCompleteListener(parent) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    startWithUser(user!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Snackbar.make(
                        requireView(),
                        "Firebase authentication was not successful.",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    private fun startWithUser(user: FirebaseUser) {
        val parent = activity as MainActivity
        authViewModel.setUser(user)
        parent.startWithUserID(authViewModel.getUserUidHashed())
    }

    fun logOutUser() {
        signOut()
    }

}