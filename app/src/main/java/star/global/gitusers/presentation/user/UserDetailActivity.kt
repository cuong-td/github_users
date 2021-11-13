package star.global.gitusers.presentation.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import star.global.gitusers.App
import star.global.gitusers.R
import star.global.gitusers.databinding.ActivityUserDetailBinding
import javax.inject.Inject

class UserDetailActivity : AppCompatActivity() {
    companion object {
        private const val ARG_USERNAME = "ARG_USERNAME"
        private const val ARG_AVATAR = "ARG_AVATAR"
        fun open(context: Context, username: String, avatar: String) {
            val activity = Intent(context, UserDetailActivity::class.java).apply {
                putExtra(ARG_USERNAME, username)
                putExtra(ARG_AVATAR, avatar)
            }
            context.startActivity(activity)
        }
    }

    private val username: String by lazy { intent.getStringExtra(ARG_USERNAME) ?: "" }

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<UserDetailViewModel> { vmFactory }

    private lateinit var viewBinding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)
        (application as? App)?.activityInjector()?.inject(this)
        setupViewBinding(viewBinding)
        setupObservables()
    }

    private fun setupViewBinding(viewBinding: ActivityUserDetailBinding) {
        with(viewBinding) {
            bindingModel = UserDetailBindingModel().apply {
                username.set(this@UserDetailActivity.username)
                avatar.set(intent.getStringExtra(ARG_AVATAR) ?: "")
            }
        }
    }

    private fun setupObservables() {
        viewModel.loadUserDetail(username)
        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        viewModel.user.observe(this) {
            viewBinding.bindingModel?.bind(it)
        }
    }
}