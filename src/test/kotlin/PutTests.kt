import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PutTests {

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
        val (newKey, newValue) = "4" to "test4"

        put(dbHashMap, newKey, newValue)
        assertEquals(dbHashMap[newKey], newValue)
    }

    @Test
    fun deleteTest2() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val (newKey, newValue) = "2" to "test4"
        val out = "${Color.RED.ANSICode}Ключ |$newKey| уже существует${Color.RESET.ANSICode}"

        put(dbHashMap, newKey, newValue)
        assertEquals(out, stream.toString().trim())
    }


    @Test
    fun deleteTest3() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val (newKey, newValue) = null to "test4"
        val out = "${Color.RED.ANSICode}Проверьте корректность ввода данных${Color.RESET.ANSICode}"

        put(dbHashMap, newKey, newValue)
        assertEquals(out, stream.toString().trim())
    }

    @Test
    fun deleteTest4() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val (newKey, newValue) = "4" to null
        val out = "${Color.RED.ANSICode}Проверьте корректность ввода данных${Color.RESET.ANSICode}"

        put(dbHashMap, newKey, newValue)
        assertEquals(out, stream.toString().trim())
    }

    @Test
    fun deleteTest5() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val (newKey, newValue) = null to null
        val out = "${Color.RED.ANSICode}Проверьте корректность ввода данных${Color.RESET.ANSICode}"

        put(dbHashMap, newKey, newValue)
        assertEquals(out, stream.toString().trim())
    }
}
