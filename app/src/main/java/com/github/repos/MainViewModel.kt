package com.github.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.repos.domain.model.AllRepositories
import com.github.repos.domain.usecase.MainUseCase
import com.github.repos.domain.model.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val mainUseCase: MainUseCase) : ViewModel() {

    private val _allRepositories = MutableLiveData<ResponseState<List<AllRepositories>>>()

    val allRepositories: LiveData<ResponseState<List<AllRepositories>>>
        get() = _allRepositories


    fun getAllRepositories() = viewModelScope.launch(Dispatchers.IO) {
        mainUseCase().collect {
            _allRepositories.postValue(it)
        }
    }


}