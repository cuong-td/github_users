package star.global.gitusers.data.remote.request


fun String.toQueryFormat(): String = "$this in:username"