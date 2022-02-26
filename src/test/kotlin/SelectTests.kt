import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SelectTests {

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
    fun selectTest1() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val key = "1"
        val out = "Найдена запись |$key: ${dbHashMap[key]}|"

        select(dbHashMap, key)
        assertEquals(out, stream.toString().trim())

    }

    @Test
    fun selectTest2() {
        val dbHashMap = hashMapOf("TestKey" to "test1", "2" to "test2", "3" to "test3")
        val key = "TestKey"
        val out = "Найдена запись |$key: ${dbHashMap[key]}|"

        select(dbHashMap, key)
        assertEquals(out, stream.toString().trim())
    }

    @Test
    fun selectTest3() {
        val dbHashMap = hashMapOf("2" to "test2")
        val key = "1"
        val out = "${Color.RED.ANSICode}Запись по ключу |$key| не найдена${Color.RESET.ANSICode}"

        select(dbHashMap, key)
        assertEquals(out, stream.toString().trim())
    }

    @Test
    fun selectTest4() {
        val dbHashMap = HashMap<String, String>()
        val key = "1"
        val out = "${Color.RED.ANSICode}Запись по ключу |$key| не найдена${Color.RESET.ANSICode}"

        select(dbHashMap, key)
        assertEquals(out, stream.toString().trim())
    }

    @Test
    fun selectTest5() {
        val dbHashMap = HashMap<String, String>()
        val key = null
        val out = "${Color.RED.ANSICode}Запись по ключу |$key| не найдена${Color.RESET.ANSICode}"

        select(dbHashMap, key)
        assertEquals(out, stream.toString().trim())
    }

    @Test
    fun selectTest6() {
        val dbHashMap = HashMap<String, String>()
        val key = ""
        val out = "${Color.RED.ANSICode}Запись по ключу |$key| не найдена${Color.RESET.ANSICode}"

        select(dbHashMap, key)
        assertEquals(out, stream.toString().trim())
    }
}
