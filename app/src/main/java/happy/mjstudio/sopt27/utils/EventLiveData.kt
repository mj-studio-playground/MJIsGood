package happy.mjstudio.sopt27.utils

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.*

open class EventLiveData<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    @MainThread
    open fun emit(value: T) {
        pending.set(true)
        setValue(value)
    }
}

class SimpleEventLiveData : EventLiveData<Unit>() {
    fun emit() {
        super.emit(Unit)
    }
}

fun <T> Fragment.observeEvent(eventLiveData: EventLiveData<T>, observer: Observer<in T>) {
    eventLiveData.observe(viewLifecycleOwner, observer)
}