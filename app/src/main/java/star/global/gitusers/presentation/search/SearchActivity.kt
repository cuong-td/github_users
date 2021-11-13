package star.global.gitusers.presentation.search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import star.global.gitusers.App
import star.global.gitusers.R
import star.global.gitusers.databinding.ActivitySearchBinding
import star.global.gitusers.extension.hideKeyboard
import star.global.gitusers.presentation.user.UserDetailActivity
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {
    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SearchViewModel> { vmFactory }

    private lateinit var viewBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        (application as? App)?.activityInjector()?.inject(this)
        setupViewBinding(viewBinding)
        setupObservables()
    }

    private fun setupViewBinding(viewBinding: ActivitySearchBinding) {
        with(viewBinding) {
            bindingModel = SearchBindingModel().apply {
                bind(
                    this@SearchActivity,
                    SearchState.InitState
                )
            }

            with(searchView) {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        search(query, true)
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        search(newText, newText.isEmpty())
                        return true
                    }

                })
            }

            with(rvResults) {
                adapter = SearchAdapter {
                    UserDetailActivity.open(context, username = it.username, avatar = it.avatarUrl)
                }
                addItemDecoration(
                    DividerItemDecoration(
                        this@SearchActivity,
                        DividerItemDecoration.VERTICAL
                    )
                )
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    private var loading = true

                    override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) { //check for scroll down
                            val visibleItemCount = rv.layoutManager?.childCount ?: 0
                            val totalItemCount = rv.layoutManager?.itemCount ?: 0
                            val pastVisibleItems = (rv.layoutManager as? LinearLayoutManager)
                                ?.findFirstVisibleItemPosition()
                                ?: 0

                            if (loading) {
                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                    loading = false
                                    viewModel.searchNext()
                                    loading = true
                                }
                            }
                        }
                    }
                })
            }
        }

    }

    private fun setupObservables() {
        viewModel.searchState.observe(this) {
            viewBinding.bindingModel?.bind(this, it)

            // Handle more
            when (it) {
                is SearchState.LoadingState -> {
                    if (it.firstLoading)
                        hideKeyboard()
                }
                else -> {
                }
            }
        }
    }

    private fun search(query: String, force: Boolean = false) {
        if (force) {
            viewModel.search(query)
            hideKeyboard()
        } else
            viewModel.processSearching(query)
    }

}