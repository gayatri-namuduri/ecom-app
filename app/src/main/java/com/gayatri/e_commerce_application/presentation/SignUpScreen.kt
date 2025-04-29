package com.gayatri.e_commerce_application.presentation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout. fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.gayatri.e_commerce_application.R
import com.gayatri.e_commerce_application.domain.models.UserData
import com.gayatri.e_commerce_application.presentation.Utils.CustomTexField
import com.gayatri.e_commerce_application.presentation.Utils.SuccessAlertDialog
import com.gayatri.e_commerce_application.presentation.navigation.Routes
import com.gayatri.e_commerce_application.presentation.navigation.SubNavigation
import com.gayatri.e_commerce_application.presentation.viewModels.ShoppingAppViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


@Composable // annotation
fun SignUpScreen(navController: NavController, viewModel: ShoppingAppViewModel = hiltViewModel()) {

    val state = viewModel.singUpScreenState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val activity = context as Activity
    val firebaseAuth = FirebaseAuth.getInstance()

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { signInTask ->
                    if (signInTask.isSuccessful) {
                        // Sign-in success, navigate to the main screen
                        val firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser != null) {
                            val userData = UserData(
                                fastName = firebaseUser.displayName?.split(" ")?.firstOrNull() ?: "",
                                lastName = firebaseUser.displayName?.split(" ")?.getOrNull(1) ?: "",
                                email = firebaseUser.email ?: "",
                                password = "", // no password for Google
                                phoneNumber = firebaseUser.phoneNumber ?: ""
                            )

                            // Add user to Firestore if not already present
                            val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                            firestore.collection("users") // or whatever USER_COLLECTION is
                                .document(firebaseUser.uid)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (!document.exists()) {
                                        firestore.collection("users")
                                            .document(firebaseUser.uid)
                                            .set(userData)
                                    }
                                }
                        }
                        navController.navigate(SubNavigation.MainHomeScreen)
                    } else {
                        // Sign-in failed, show error message
                        Toast.makeText(context, "Google sign-in failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google sign-in error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
    val signInIntent = googleSignInClient.signInIntent


    if (state.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if(state.value.errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize())
        Text(text = state.value.errorMessage!!)
    }
    else if(state.value.userData != null){
        SuccessAlertDialog (onClick = {
            navController.navigate(SubNavigation.MainHomeScreen)
        })
    }
    else{

        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "SignUp",
                fontSize = 24.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
            )
            CustomTexField(
                value = firstName,
                onValueChange = { firstName = it },
                lable = "First Name",
                leadingIcon = Icons.Default.Person,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            )
            CustomTexField(
                value = lastName,
                onValueChange = { lastName = it },
                lable = "Last Name",
                leadingIcon = Icons.Default.Person,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            )
            CustomTexField(
                value = email,
                onValueChange = { email = it },
                lable = "Email",
                leadingIcon = Icons.Default.Email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            )
            CustomTexField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                lable = "Phone Number",
                leadingIcon = Icons.Default.Phone,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            )

            CustomTexField(
                value = password,
                onValueChange = { password = it },
                lable = "Password",
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                visualTransformation = PasswordVisualTransformation(),
            )
            CustomTexField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                lable = "Confirm Password",
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                visualTransformation = PasswordVisualTransformation(),
            )
            Button(
                onClick = {
                    if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                        if (password == confirmPassword) {
                            val userData = UserData(
                                fastName = firstName,
                                lastName = lastName,
                                email = email,
                                password = password,
                                phoneNumber = phoneNumber
                            )
                            viewModel. createUser (userData)
                            Toast.makeText(context, "SignUp Successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Password and Confirm password should match",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.darkGrey)
                )

            ) {

                Text(text = "SignUp", color = colorResource(id = R.color.white))

            }

            Row(verticalAlignment = Alignment. CenterVertically) {
                Text( "Already have an account?")
                TextButton (onClick = {
                    navController.navigate(Routes.LoginScreen)
                    // navigate to login screen
                }) {
                    Text("Login", color = colorResource (id = R.color.darkGrey))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(text = "Or", modifier = Modifier.padding(horizontal = 8.dp))
                HorizontalDivider(modifier = Modifier.weight(1f))

            }

            OutlinedButton(
                onClick = {signInLauncher.launch(signInIntent)},
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google), contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))
                Text("SignUp with Google")
            }
        }
    }


}