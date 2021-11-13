package star.global.gitusers.usecase.deps

import star.global.gitusers.usecase.DoSomething


interface UseCasesExposeProvider {
    // TODO: Declare UseCase interface
    val doSomething: DoSomething
}