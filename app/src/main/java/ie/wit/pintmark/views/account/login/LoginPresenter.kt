package ie.wit.pintmark.views.account.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.wit.pintmark.views.markerlist.MarkerListView
import com.google.firebase.auth.FirebaseAuth
import ie.wit.pintmark.main.MainApp
import ie.wit.pintmark.models.MarkerFireStore


class LoginPresenter (val view: LoginView)  {
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>
    var app: MainApp = view.application as MainApp
    var fireStore: MarkerFireStore? = null

    init{
        registerLoginCallback()
        if (app.markers is MarkerFireStore) {
            fireStore = app.markers as MarkerFireStore
        }
    }
    // object used to interact with firebase
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doSignIn(username: String, secret: String) {
        // first show the login progress spinner icon
        view.showProgress()
        // authenticate using firebase
        auth.signInWithEmailAndPassword(username, secret).addOnCompleteListener(view!!) { task ->
            // SUCCESS
            if (task.isSuccessful) {
                // fetch the markers
                fireStore!!.fetchMarkers {
                    // after fetching the markers, go to the list view
                    view.hideProgress()
                    val launcherIntent = Intent(view, MarkerListView::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
            }
            // ERROR
            else {
                // display the error to the user
                view.showSnackBar("Login failed: ${task.exception?.message}")
                view.hideProgress()
            }
        }
    }

    fun doRegister(username: String, secret: String) {
        // first show the login progress spinner icon
        view.showProgress()
        // create a new user in firebase
        auth.createUserWithEmailAndPassword(username, secret).addOnCompleteListener(view!!) { task ->
            // SUCCESS
            if (task.isSuccessful) {
                fireStore!!.fetchMarkers {
                    // after fetching the markers, go to the list view
                    view.hideProgress()
                    val launcherIntent = Intent(view, MarkerListView::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
            }
            // ERROR
            else {
                // display the error to the user
                view.showSnackBar("Login was unsuccessful: ${task.exception?.message}")
                view.hideProgress()
            }
        }
    }

    suspend fun signOut(){
        FirebaseAuth.getInstance().signOut()
        // clear the local markers
        app.markers.clear()
        // go back to the login view
        val intent = Intent(view, LoginView::class.java)
        loginIntentLauncher.launch(intent)
    }


    private fun registerLoginCallback(){
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}