package star.global.gitusers.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    protected fun <T> LiveData<T>.setData(data: T) {
        (this as? MutableLiveData)?.value = data
    }

    protected fun <T> LiveData<T>.postData(data: T) {
        (this as? MutableLiveData)?.postValue(data)
    }

}