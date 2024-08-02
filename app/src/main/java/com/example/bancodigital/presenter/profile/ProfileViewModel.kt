package com.example.bancodigital.presenter.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.data.model.User
import com.example.bancodigital.domain.profile.GetProfileUseCase
import com.example.bancodigital.domain.profile.SaveImageProfileUseCase
import com.example.bancodigital.domain.profile.SaveProfileUseCase
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.StateView
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val saveProfileUsecase: SaveProfileUseCase,
    private val saveImageProfileUsecase: SaveImageProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    fun saveProfile(user: User) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            saveProfileUsecase.invoke(user)

            emit(StateView.Sucess(null))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun saveImageProfile(imageProfile: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val urlImage = saveImageProfileUsecase.invoke(imageProfile)

            emit(StateView.Sucess(urlImage))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun getProfile() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val user = getProfileUseCase.invoke(FirebaseHelper.getUserId())

            emit(StateView.Sucess(user))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}