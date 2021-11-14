package star.global.gitusers

import star.global.gitusers.data.remote.response.BriefUserDto
import star.global.gitusers.data.remote.response.UserDto
import star.global.gitusers.data.remote.response.UserSearchDto
import kotlin.random.Random

val rand = Random(10000)

fun genSearchItem(keywords: String): BriefUserDto = BriefUserDto(
    id = rand.nextInt(),
    login = genLoginId(keywords),
    avatar_url = "https://images.unsplash.com/photo-1453728013993-6d66e9c9123a?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8dmlld3xlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=60"
)

fun genSearchData(keywords: String = "data"): UserSearchDto = UserSearchDto(
    totalCount = 10000,
    incompleteResults = false,
    items = List(15) { genSearchItem(keywords) }
)

fun genUserDetail(
    keywords: String,
    avatar_url: String? = null,
    bio: String? = null,
    company: String? = null,
    email: String? = null,
    followers: Int? = null,
    following: Int? = null,
    location: String? = null,
    name: String? = null,
    public_gists: Int? = null,
    public_repos: Int? = null,
): UserDto = UserDto(
    id = rand.nextInt(),
    login = genLoginId(keywords),
    avatar_url = avatar_url,
    bio = bio,
    company = company,
    email = email,
    followers = followers,
    following = following,
    location = location,
    name = name,
    public_repos = public_repos,
    public_gists = public_gists,
)

fun genLoginId(keywords: String): String = keywords +
        List(rand.nextInt(3, 5)) {
            rand.nextInt('a'.toInt(), 'z'.toInt()).toChar()
        }.joinToString(separator = "")