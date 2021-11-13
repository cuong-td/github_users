package star.global.gitusers.usecase.deps

import star.global.gitusers.usecase.FindUsers
import star.global.gitusers.usecase.GetUserDetail


interface UseCasesExposeProvider {
    val findUsers: FindUsers
    val getUser: GetUserDetail
}