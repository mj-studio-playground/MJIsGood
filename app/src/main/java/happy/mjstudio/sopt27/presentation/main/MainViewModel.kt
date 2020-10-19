package happy.mjstudio.sopt27.presentation.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import happy.mjstudio.sopt27.model.Sample
import happy.mjstudio.sopt27.utils.ItemTouchHelperAdapter
import happy.mjstudio.sopt27.utils.logE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(/*@Assisted private val savedStateHandle: SavedStateHandle*/) :
    ViewModel(), ItemTouchHelperAdapter {

    private val _datas : MutableLiveData<List<Sample>> = MutableLiveData((1..100).map {
        Sample("Title$it", "sub title - $it")
    })
    val datas : LiveData<List<Sample>> = _datas

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        logE("$fromPosition -> $toPosition")
        val copy = datas.value!!.toMutableList()
        copy[fromPosition] = copy[toPosition].also { copy[toPosition] = copy[fromPosition] }
//        if (fromPosition < toPosition) {
//            for (i in fromPosition until toPosition) {
//                Collections.swap(copy, i, i + 1)
//            }
//        } else {
//            for (i in fromPosition downTo toPosition + 1) {
//                Collections.swap(copy, i, i - 1)
//            }
//        }
        _datas.value = copy
        return true
    }

    override fun onItemDismiss(position: Int) {
        val copy = datas.value!!.toMutableList()
        copy.removeAt(position)
        _datas.value = copy
    }
}