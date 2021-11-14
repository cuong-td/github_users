package star.global.gitusers.mapper

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import star.global.gitusers.data.mapper.toLocalError
import star.global.gitusers.data.mapper.toModel
import star.global.gitusers.data.mapper.toUser
import star.global.gitusers.domain.Error
import star.global.gitusers.genSearchData
import star.global.gitusers.genSearchItem
import star.global.gitusers.genUserDetail
import java.io.IOException
import java.net.UnknownHostException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class MapperTest {
    @Test
    fun `search dto to data`() {
        val dto = genSearchData()
        val data = dto.toModel()
        assertEquals(10000, data.total)
        assertEquals(15, data.users.count())
    }

    @Test
    fun `brief user dto to model`() {
        val dto = genSearchItem("cuong")
        val data = dto.toUser()
        assertTrue(data.username.startsWith("cuong"))
        assertEquals(
            "https://images.unsplash.com/photo-1453728013993-6d66e9c9123a?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8dmlld3xlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=60",
            data.avatarUrl
        )
    }

    @Test
    fun `user dto to model default value`() {
        val dto = genUserDetail("cuong")
        val data = dto.toUser()
        assertTrue(data.username.startsWith("cuong"))
        assertEquals("N/A", data.name)
        assertEquals("", data.avatarUrl)
        assertEquals("N/A", data.company)
        assertEquals("N/A", data.location)
        assertEquals("N/A", data.email)
        assertEquals("N/A", data.bio)
        assertEquals(0, data.followers)
        assertEquals(0, data.following)
        assertEquals(0, data.repos)
        assertEquals(0, data.gists)
    }

    @Test
    fun `throwable to local error`() {
        var err = OutOfMemoryError().toLocalError()
        assertEquals(Error.UNKNOWN, err.code)

        err = UnknownHostException().toLocalError()
        assertEquals(Error.NETWORK, err.code)

        err = IOException().toLocalError()
        assertEquals(Error.NETWORK, err.code)
    }
}