package star.global.gitusers.presentation.search

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import star.global.gitusers.R

@BindingAdapter("imageSrc")
fun setImageSource(iv: ImageView, srcUrl: String) {
    if (srcUrl.isNotBlank())
        Glide.with(iv)
            .load(srcUrl)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(iv)
}

@BindingAdapter("dataSrc")
fun <T> setDataSource(rv: RecyclerView, data: List<T>?) {
    (rv.adapter as? ListAdapter<T, *>)?.submitList(data)
}