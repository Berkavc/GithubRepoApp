package com.github.repos.presentation.repodetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.repos.domain.model.RepoDetails
import com.github.repos.domain.usecase.GetRepositoryDetailsUseCase
import com.github.repos.domain.model.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryDetailsViewModel @Inject constructor(
    private val repositoryDetailsUseCase: GetRepositoryDetailsUseCase
) : ViewModel() {

    private val _repositoryDetails = MutableLiveData<ResponseState<RepoDetails>>()
    val repositoryDetails: LiveData<ResponseState<RepoDetails>>
        get() = _repositoryDetails

    fun getRepositoryDetails(username: String, repo: String) =
        viewModelScope.launch(Dispatchers.IO) {
            repositoryDetailsUseCase(username = username, repo = repo).collect {
                _repositoryDetails.postValue(it)
            }
        }
}