package star.global.gitusers.presentation.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import star.global.gitusers.App
import star.global.gitusers.R
import star.global.gitusers.databinding.ActivitySearchBinding
import star.global.gitusers.extension.hideKeyboard
import javax.inject.Inject

class UserSearchActivity : AppCompatActivity() {
    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<UserSearchViewModel> { vmFactory }

    private lateinit var viewBinding: ActivitySearchBinding

    private val UserSearchBindingModel?.searchData: String
        get() = this?.keyword?.get() ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        (application as? App)?.activityInjector()?.inject(this)
        setupViewBinding(viewBinding)
        setupObservables()
    }

    private fun setupViewBinding(viewBinding: ActivitySearchBinding) {
        with(viewBinding) {
            bindingModel = UserSearchBindingModel().apply {
                bind(
                    this@UserSearchActivity,
                    SearchState.InitState
                )
            }

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                var overrideAction: Boolean = false
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        search(true)
                        overrideAction = true
                    }
                }
                overrideAction
            }

            etSearch.doAfterTextChanged {
                search()
            }

            with(rvResults) {
                adapter = UserSearchAdapter {
                    // Open User Detail
                }
                addItemDecoration(
                    DividerItemDecoration(
                        this@UserSearchActivity,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }

    }

    private fun setupObservables() {
        viewModel.searchState.observe(this) {
            viewBinding.bindingModel?.bind(this, it)

            // Handle more
            when (it) {
                SearchState.LoadingState -> hideKeyboard()
                else -> {
                }
            }
        }
    }

    private fun search(force: Boolean = false) {
        if (force) {
            viewModel.search(viewBinding.bindingModel.searchData)
            hideKeyboard()
        } else
            viewModel.processSearching(viewBinding.bindingModel.searchData)
    }

}