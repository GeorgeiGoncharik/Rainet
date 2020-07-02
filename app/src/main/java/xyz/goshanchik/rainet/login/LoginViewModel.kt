package xyz.goshanchik.rainet.login

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val _isPasswordValid = MutableLiveData<Boolean>(true)
    val isPasswordValid: LiveData<Boolean>
        get() = _isPasswordValid

    private val _navigateToTrackerFragment = MutableLiveData<Boolean>(false)
    val navigateToTrackerFragment: LiveData<Boolean>
        get() = _navigateToTrackerFragment

    fun navigateToTrackerFragment(){
        if(_isPasswordValid.value!!)
            _navigateToTrackerFragment.value = true
    }

    fun onNavigateToTrackerFragment(){
        _navigateToTrackerFragment.value = false
    }


    fun isPasswordValid(text: Editable?){
        _isPasswordValid.value = text != null && text.length >= 8
    }

}