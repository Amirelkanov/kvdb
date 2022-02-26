import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SetTests {

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
    fun setTest1() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val (key, newValue) = "1" to "test4"

        set(dbHashMap, key, newValue)
        assertEquals(dbHashMap[key], newValue)
    }

    @Test
    fun setTest2() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val (key, newValue) = null to "test4"
        val out = "${Color.RED.ANSICode}Проверьте корректность ввода данных${Color.RESET.ANSICode}"

        set(dbHashMap, key, newValue)
        assertEquals(out, stream.toString().trim())
    }

    @Test
    fun setTest3() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val (key, newValue) = "1" to null
        val out = "${Color.RED.ANSICode}Проверьте корректность ввода данных${Color.RESET.ANSICode}"

        set(dbHashMap, key, newValue)
        assertEquals(out, stream.toString().trim())
    }

    @Test
    fun setTest4() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val (key, newValue) = null to null
        val out = "${Color.RED.ANSICode}Проверьте корректность ввода данных${Color.RESET.ANSICode}"

        set(dbHashMap, key, newValue)
        assertEquals(out, stream.toString().trim())
    }

    @Test
    fun setTest5() {
        val dbHashMap = hashMapOf("1" to "test1", "2" to "test2", "3" to "test3")
        val (key, newValue) = "4" to "test4"
        val out = "${Color.RED.ANSICode}Ключа $key не существует${Color.RESET.ANSICode}"

        set(dbHashMap, key, newValue)
        assertEquals(out, stream.toString().trim())
    }
}
