package star.global.gitusers.extension

import android.os.SystemClock
import android.view.View

fun View.setOnDebounceClick(debounceTime: Long = 500, action: View.() -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        private var startClickTime: Long = 0
        override fun onClick(v: View) {
            val now = SystemClock.elapsedRealtime()
            if (now - startClickTime < debounceTime)
                return
            action(v)
            startClickTime = now
        }
    })
}