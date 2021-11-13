package star.global.gitusers.deps

import star.global.gitusers.presentation.search.SearchActivity
import star.global.gitusers.presentation.user.UserDetailActivity


interface ActivityInjectionProvider {
    fun inject(activity: SearchActivity)
    fun inject(activity: UserDetailActivity)
}