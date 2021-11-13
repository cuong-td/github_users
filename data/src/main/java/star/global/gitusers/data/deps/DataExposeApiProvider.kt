package star.global.gitusers.data.deps

import star.global.gitusers.data.repository.UserRepository

interface DataExposeApiProvider {
    val userRepo: UserRepository
}