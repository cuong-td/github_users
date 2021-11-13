package star.global.gitusers.deps

import star.global.gitusers.presentation.search.UserSearchActivity


interface ActivityInjectionProvider {
    fun inject(activity: UserSearchActivity)
}