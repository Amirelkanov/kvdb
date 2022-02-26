import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.*

internal class DeleteTests {

    private val standardOut = System.out
    private val standardIn = System.`in`
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @AfterTest
    fun tearDown() {
        System.setOut(standardOut)
        System.setIn(standardIn)
    }

    @Test
    fun deleteTest1() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val key = "1"

        delete(dbHashMap, key)
        assertTrue(!dbHashMap.containsKey(key))
    }

    @Test
    fun deleteTest2() {
        val dbHashMap = hashMapOf("1" to "test1", "second" to "test2", "3" to "test3")
        val key = "2"

        delete(dbHashMap, key)
        assertTrue(!dbHashMap.containsKey(key))
    }

    @Test
    fun deleteTest3() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val key = "5"
        val out = "${Color.RED.ANSICode}Запись по ключу |$key| не найдена${Color.RESET.ANSICode}"

        delete(dbHashMap, key)
        assertEquals(out, stream.toString().trim())
    }

    @Test
    fun deleteTest4() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val key = null
        val out = "${Color.RED.ANSICode}Запись по ключу |$key| не найдена${Color.RESET.ANSICode}"

        delete(dbHashMap, key)
        assertEquals(out, stream.toString().trim())
    }
}
