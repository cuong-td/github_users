package star.global.gitusers.deps

import star.global.gitusers.presentation.search.SearchActivity


interface ActivityInjectionProvider {
    fun inject(activity: SearchActivity)
}