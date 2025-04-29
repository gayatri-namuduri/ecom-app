package com.gayatri.e_commerce_application.presentation.Screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.gayatri.e_commerce_application.R
import com.gayatri.e_commerce_application.domain.models.UserData
import com.gayatri.e_commerce_application.domain.models.UserDataParent
import com.gayatri.e_commerce_application.presentation.Utils.LogOutAlerDiaglog
import com.gayatri.e_commerce_application.presentation.navigation.SubNavigation
import com.gayatri.e_commerce_application.presentation.viewModels.ShoppingAppViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreenUi(navController: NavController, firebaseAuth: FirebaseAuth, viewModel: ShoppingAppViewModel = hiltViewModel()) {

    LaunchedEffect (key1 = true) {
        viewModel.getUserById(firebaseAuth.currentUser!!.uid)
    }

    val profileScreenState = viewModel.profileScreenState.collectAsStateWithLifecycle()
    val upDateScreenState = viewModel. upDateScreenState.collectAsStateWithLifecycle()
    val userProfileImageState = viewModel.userProfileImageState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf( false) }
    val isEditing = remember { mutableStateOf( false) }
 //   val imageUri = rememberSaveable {mutableStateOf<Uri?>( null) }
    val imageUri = remember { mutableStateOf("") }
    val firstName = remember { mutableStateOf(  profileScreenState.value.userData?.userData?. fastName?: "") }
    val lastName = remember { mutableStateOf( profileScreenState.value.userData?.userData?.lastName?: "") }
    val email = remember { mutableStateOf( profileScreenState.value.userData?.userData?.email?: "") }
    val phoneNumber = remember { mutableStateOf ( profileScreenState. value.userData?.userData?. phoneNumber?: "") }
    val address = remember { mutableStateOf( profileScreenState.value.userData?.userData?.address?: "") }

    LaunchedEffect(profileScreenState.value.userData) {
        profileScreenState.value.userData?.userData?.let { userData ->
            firstName.value = userData.fastName ?: ""
            lastName.value = userData.lastName ?: ""
            email.value = userData.email ?: ""
            phoneNumber.value = userData.phoneNumber ?: ""
            address.value = userData.address ?: ""
            imageUri.value = userData.profileImage ?: ""
        }
    }

    val pickMedia = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri:Uri? ->
        if(uri != null) {
            viewModel.upLoadUserProfileImage(uri)
            imageUri.value = uri.toString() // it is converted in to string
        }
    }

    if (upDateScreenState.value.userData != null) {
        Toast.makeText(context, upDateScreenState.value.userData, Toast.LENGTH_SHORT).show()
    }
    else if (upDateScreenState.value.errorMessage != null){
        Toast. makeText(context, upDateScreenState.value.errorMessage, Toast.LENGTH_SHORT) .show()
    }
    else if (upDateScreenState.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (userProfileImageState.value.userData!= null) {
        imageUri.value = userProfileImageState.value.userData.toString()
    }
    else if (userProfileImageState.value.errorMessage != null) {
        Toast.makeText(context, userProfileImageState.value.errorMessage, Toast.LENGTH_SHORT).show()
    }
    else if (userProfileImageState.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (profileScreenState.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    else if (profileScreenState.value.errorMessage != null) {
        Text(text = profileScreenState.value.errorMessage!!)
    }
    else if(profileScreenState.value.userData!= null) {
        Scaffold(

        ) { innerpadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerpadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Start)
                ){
                    SubcomposeAsyncImage(
                        model = if (isEditing.value) imageUri. value else imageUri. value ,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale. Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, color = colorResource(id = R.color.darkGrey), CircleShape)
                    ){
                        when (painter. state) {
                            is AsyncImagePainter.State.Loading -> CircularProgressIndicator()
                            is AsyncImagePainter.State.Error -> Icon(
                                Icons.Default.Person,
                                contentDescription = null
                            )

                            else -> SubcomposeAsyncImageContent()
                        }
                    }
                    if (isEditing. value) {
                        IconButton(
                            onClick = {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.BottomEnd)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Change Picture",
                                tint = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size (16.dp))
                Row {
                    OutlinedTextField(
                        value = firstName.value,
                        onValueChange = {firstName.value=it},
                        modifier = Modifier.weight(1f),
                        readOnly = if (isEditing.value) false else true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(id = R.color.darkGrey),
                            focusedBorderColor = colorResource(id = R.color.darkGrey)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = { Text(text = "First Name") }
                    )
                    Spacer(modifier = Modifier.size (16.dp))
                    OutlinedTextField(
                        value = lastName.value,
                        onValueChange = {lastName.value=it},
                        modifier = Modifier.weight(1f),
                        readOnly = if (isEditing.value) false else true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(id = R.color.darkGrey),
                            focusedBorderColor = colorResource(id = R.color.darkGrey)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = { Text(text = "Last Name") }
                    )
                }
                Spacer(modifier = Modifier.size (16.dp))
                OutlinedTextField(
                    value = email.value,
                    onValueChange = {email.value=it},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = if (isEditing.value) false else true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(id = R.color.darkGrey),
                        focusedBorderColor = colorResource(id = R.color.darkGrey)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Email") }
                )
                Spacer(modifier = Modifier.size (16.dp))
                OutlinedTextField(
                    value = phoneNumber.value,
                    onValueChange = {phoneNumber.value=it},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = if (isEditing.value) false else true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(id = R.color.darkGrey),
                        focusedBorderColor = colorResource(id = R.color.darkGrey)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Phone Number") }
                )
                Spacer(modifier = Modifier.size (16.dp))
                OutlinedTextField(
                    value = address.value,
                    onValueChange = {address.value=it},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = if (isEditing.value) false else true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(id = R.color.darkGrey),
                        focusedBorderColor = colorResource(id = R.color.darkGrey)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Address") }
                )
                Spacer(modifier = Modifier.size (16.dp))
                OutlinedButton(
                    onClick = { showDialog.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape (10. dp) ,
                    colors = ButtonDefaults.buttonColors(colorResource (id = R.color.darkGrey))
                ) {
                    Text("Log Out")
                }

                if (showDialog.value) {
                    LogOutAlerDiaglog(
                        onDismiss = {
                            showDialog.value = false
                        },
                        onConfirm = {
                            firebaseAuth.signOut()
                            navController.navigate(SubNavigation.LoginSingUpScreen)
                        }
                    )
                }

                Spacer(modifier = Modifier.size (16.dp))
                if (isEditing. value == false) {
                    OutlinedButton(
                        onClick = {
                            isEditing.value = !isEditing.value
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text("Edit Profile")
                    }
                }
                else{
                    OutlinedButton(
                        onClick = {
                            val updatedUserData = UserData(
                                fastName = firstName.value,
                                lastName = lastName.value,
                                email = email.value,
                                phoneNumber = phoneNumber.value,
                                address = address.value,
                                profileImage = imageUri.value
                            )
                            val userDataParent = UserDataParent (
                                nodeId = profileScreenState.value.userData!!.nodeId,
                                userData = updatedUserData
                            )
                            viewModel. upDateUserData (userDataParent)
                            isEditing.value = !isEditing.value
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape (10. dp)
                    ){
                        Text("Save Profile")
                    }
                }
            }



        }
    }

}

