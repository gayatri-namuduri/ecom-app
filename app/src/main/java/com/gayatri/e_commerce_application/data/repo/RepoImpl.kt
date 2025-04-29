package com.gayatri.e_commerce_application.data.repo

import android.net.Uri
import com.gayatri.e_commerce_application.common.ADDTOFAV
import com.gayatri.e_commerce_application.common.ADD_TO_CART
import com.gayatri.e_commerce_application.common.PRODUCT_COLLECTION
import com.gayatri.e_commerce_application.common.ResultState
import com.gayatri.e_commerce_application.common.USER_COLLECTION
import com.gayatri.e_commerce_application.domain.models.BannerDataModels
import com.gayatri.e_commerce_application.domain.models.CartDataModels
import com.gayatri.e_commerce_application.domain.models.CategoryDataModels
import com.gayatri.e_commerce_application.domain.models.ProductDataModels
import com.gayatri.e_commerce_application.domain.models.UserData
import com.gayatri.e_commerce_application.domain.models.UserDataParent
import com.gayatri.e_commerce_application.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImpl @Inject constructor(
    var firebaseAuth: FirebaseAuth, var firebaseFirestore: FirebaseFirestore
) : Repo
{
    override fun registerUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>> = callbackFlow {
        trySend (ResultState.Loading)
        if (userData.password.isNullOrBlank()) {
            val uid = firebaseAuth.currentUser?.uid
            if (uid != null) {
                firebaseFirestore.collection(USER_COLLECTION)
                    .document(uid)
                    .set(userData.copy(password = null.toString())) // optional: omit password
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            trySend(ResultState.Success("Google user added to Firestore"))
                        } else {
                            trySend(ResultState.Error(task.exception?.localizedMessage ?: "Firestore write error"))
                        }
                    }
            } else {
                trySend(ResultState.Error("User not authenticated"))
            }
            awaitClose { close() }
            return@callbackFlow
        }
        firebaseAuth.createUserWithEmailAndPassword(userData.email, userData.password!!) .addOnCompleteListener {
            if(it.isSuccessful){
                firebaseFirestore.collection(USER_COLLECTION). document(it.result.user?.uid.toString()).set(userData).addOnCompleteListener{
                    if(it.isSuccessful) {
                        trySend(ResultState.Success("User Registered Successfully and add to Firestore"))
                    }else {
                        if (it.exception != null) {
                            trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                        }
                    }
                }
                trySend(ResultState.Success("User Registered Successfully"))
            }else {
                if (it.exception != null) {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            }
        }
        awaitClose{
            close()
        }
    }

    override fun loginUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseAuth.signInWithEmailAndPassword(userData.email, userData.password) .addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(ResultState.Success("User Login Successfully"))
            } else {
                if (it.exception != null) {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            }
        }
        awaitClose {
            close()
        }
    }

    override fun getuserById(uid: String): Flow<ResultState<UserDataParent>> = callbackFlow{
        firebaseFirestore. collection (USER_COLLECTION). document(uid).get().addOnCompleteListener{
            if(it.isSuccessful){
                val result = it.result
                val data = result.toObject(UserData::class.java)
                if (data != null) {
                    val userDataParent = UserDataParent(result.id, data)
                    trySend(ResultState.Success(userDataParent))
                } else {
                    trySend(ResultState.Error("User data not found in Firestore."))
                }
            }else {
                if (it.exception != null) {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            }
        }
        awaitClose {
            close()
        }
    }

    override fun upDateUserData(userDataParent: UserDataParent): Flow<ResultState<String>> = callbackFlow {
        trySend (ResultState. Loading)
        firebaseFirestore. collection (USER_COLLECTION). document(userDataParent.nodeId).update(userDataParent.userData.toMap()).addOnCompleteListener{
            if(it.isSuccessful) {
                trySend(ResultState.Success("User Data Updated Successfully"))
            }else{
                if(it.exception != null){
                    trySend (ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            }
        }
        awaitClose {
            close()
        }
    }

    override fun userProfileImage(uri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        FirebaseStorage.getInstance().reference.child("userProfile/${System.currentTimeMillis()}+${firebaseAuth.currentUser?.uid}")
            .putFile(uri ?: Uri.EMPTY).addOnCompleteListener {
                it.result.storage.downloadUrl.addOnSuccessListener { imageUri ->
                    trySend(ResultState.Success(imageUri.toString()))
                }
                if (it.exception != null) {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            }
        awaitClose {
            close()
        }

    }

    override fun getCategoriesInLimited(): Flow<ResultState<List<CategoryDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection("categories"). limit(7).get().addOnSuccessListener { querySnapshot ->
            val categories = querySnapshot.documents.mapNotNull { document ->
                document.toObject(CategoryDataModels::class.java)
            }
            trySend(ResultState.Success(categories))
        }.addOnFailureListener{ exception ->
            trySend(ResultState.Error(exception.toString()))
        }
        awaitClose {
            close()
        }
    }

    override fun getProductsInLimited(): Flow<ResultState<List<ProductDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection ("Products"). limit(10).get().addOnSuccessListener {
            val products = it.documents.mapNotNull { document->
                document. toObject(ProductDataModels:: class.java)?.apply {
                    productId = document.id
                }
            }
            trySend (ResultState. Success (products))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }
        awaitClose {
            close()
        }
    }

    override fun getAllProducts(): Flow<ResultState<List<ProductDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection ("Products").get().addOnSuccessListener {
        val products = it.documents.mapNotNull { document->
            document.toObject(ProductDataModels:: class.java)?.apply {
                productId = document.id
            }
        }
        trySend (ResultState. Success (products))
        }. addOnFailureListener{
            trySend (ResultState. Error (it. toString()))
        }
        awaitClose {
            close()
        }
    }

    override fun getProductById(productId: String): Flow<ResultState<ProductDataModels>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(PRODUCT_COLLECTION). document(productId).get().addOnSuccessListener {
            val product = it. toObject(ProductDataModels::class.java)
            trySend (ResultState. Success(product!!))
    }.addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }
        awaitClose {
            close()
        }
    }

    override fun addToCart(cartDataModels: CartDataModels): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection (ADD_TO_CART) .document(firebaseAuth.currentUser!!.uid).collection ("User_Cart")
            .add (cartDataModels). addOnSuccessListener {
                trySend (ResultState. Success ("Product Added To Cart"))
    }. addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun addToFav(productDataModels: ProductDataModels): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection (ADDTOFAV) .document(firebaseAuth.currentUser!!.uid).collection ("User_Fav")
            .add (productDataModels). addOnSuccessListener {
                trySend (ResultState. Success ("Product Added To Fav"))
            }. addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun getallFav(): Flow<ResultState<List<ProductDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection (ADDTOFAV) .document(firebaseAuth.currentUser!!.uid).collection ("User_Fav")
            .get(). addOnSuccessListener {
                val fav = it.documents.mapNotNull { document->
                    document. toObject(ProductDataModels:: class.java)
                }
                trySend (ResultState. Success (fav))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun getCart(): Flow<ResultState<List<CartDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(ADD_TO_CART).document(firebaseAuth.currentUser!!.uid).collection ( "User_Cart")
            .get().addOnSuccessListener {
                val cart = it.documents.mapNotNull { document ->
                    document. toObject(CartDataModels:: class.java)?.apply {
                        cartId = document.id
                    }
                }
                trySend (ResultState. Success (cart))
            }
        awaitClose {
            close()
        }
    }

    override fun getAllCategories(): Flow<ResultState<List<CategoryDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection( "categories").get().addOnSuccessListener {
            val categories = it.documents.mapNotNull { document ->
                document. toObject(CategoryDataModels::class.java)
            }
        trySend (ResultState. Success(categories))
    }. addOnFailureListener{
        trySend (ResultState. Error(it.toString()))
    }
        awaitClose {
            close()
        }
    }

    override fun getCheckout(productId: String): Flow<ResultState<ProductDataModels>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection("Products"). document (productId).get().addOnSuccessListener {
            val product = it.toObject(ProductDataModels::class.java)
            trySend(ResultState.Success(product!!))
        }.addOnFailureListener{
            trySend (ResultState. Error(it. toString()))
    }
        awaitClose {
            close()
        }
    }

    override fun getBanner(): Flow<ResultState<List<BannerDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection ( "banner").get().addOnSuccessListener {
            val banner = it.documents.mapNotNull { document ->
                document.toObject(BannerDataModels::class.java)
            }
            trySend (ResultState. Success (banner))
        }. addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }
        awaitClose {
            close()
        }
    }

    override fun getSpecificCategoryItems(categoryName: String): Flow<ResultState<List<ProductDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection( "Products").whereEqualTo(  "category", categoryName).get().addOnSuccessListener{
            val product = it.documents.mapNotNull { document ->
                document.toObject(ProductDataModels:: class.java)?.apply {
                    productId = document.id
                }
        }
        trySend (ResultState. Success(product))
    }.addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }
        awaitClose {
            close()
        }
    }

    override fun getAllSuggestedProducts(): Flow<ResultState<List<ProductDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(ADDTOFAV).document(firebaseAuth.currentUser!!.uid).collection ("User_Fav")
            .get().addOnSuccessListener {
                val fav = it.documents.mapNotNull { document ->
                    document.toObject(ProductDataModels::class.java)
                }
                trySend(ResultState.Success(fav))
            }.addOnFailureListener{
            trySend (ResultState. Error (it. toString()))
        }
        awaitClose {
            close()
        }
    }
}