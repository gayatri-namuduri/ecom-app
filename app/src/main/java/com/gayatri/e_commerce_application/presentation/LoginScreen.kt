package com.gayatri.e_commerce_application.presentation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@Composable
fun LoginScreenUi(navController: NavHostController, viewModel: ShoppingAppViewModel = hiltViewModel()) {

    val state = viewModel.loginScreenState.collectAsStateWithLifecycle()
    val showDialog = remember {
        mutableStateOf(false)
    }

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
    }else if(state.value.errorMessage!=null){
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = state.value.errorMessage!!)
        }
    }
    else if (state.value.userData!=null){
        SuccessAlertDialog (onClick =
        {
            navController.navigate(SubNavigation.MainHomeScreen)
        }
        )
    }
    else {

        var email by remember { mutableStateOf( "") }
        var password by remember { mutableStateOf ( "") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login",
                fontSize = 24.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
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

            Spacer(modifier = Modifier.padding(8.dp))

            CustomTexField(
                value = password,
                onValueChange = { password = it },
                lable = "Password",
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Spacer(modifier = Modifier.padding(16.dp))
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        val userData = UserData(
                            fastName = "",
                            lastName = "",
                            email = email,
                            password = password,
                            phoneNumber = ""
                        )
                        viewModel. loginUser(userData)
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Fill all details", Toast.LENGTH_SHORT).show()
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.darkGrey)),
                border = BorderStroke(1.dp, colorResource(id = R.color.darkGrey))


            ) {
                Text(text = "Login", color = Color.White)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material.Text("Don't have an account?")
                TextButton(onClick = {
                    navController.navigate(Routes.SingUpScreen)
                    // navigate to signup screen
                }) {
                    androidx.compose.material.Text(
                        "SignUp",
                        color = colorResource(id = R.color.darkGrey)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                HorizontalDivider(modifier = Modifier.weight(1f))
                androidx.compose.material.Text(
                    text = "Or",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f))

            }

            OutlinedButton(
                onClick = {signInLauncher.launch(signInIntent)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google), contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))
                androidx.compose.material.Text("Login with Google")
            }


        }
    }


}