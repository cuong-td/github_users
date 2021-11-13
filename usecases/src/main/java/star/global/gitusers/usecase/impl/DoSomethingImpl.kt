package star.global.gitusers.usecase.impl

import star.global.gitusers.usecase.DoSomething
import javax.inject.Inject

class DoSomethingImpl
@Inject
constructor(
    // May need repo here
) : DoSomething {
    override fun invoke() {}
}