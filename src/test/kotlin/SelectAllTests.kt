import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SelectAllTests {

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
        val out = """
            Все значения базы данных:
            -------------------------
            1: test1
            2: test2
            3: test3
            -------------------------
        """.trimIndent()

        selectAll(dbHashMap)
        assertEquals(out, stream.toString().trimIndent())
    }

    @Test
    fun selectTest2() {
        val dbHashMap = HashMap<String, String>()
        val out = """${Color.RED.ANSICode}База данных пуста${Color.RESET.ANSICode}"""

        selectAll(dbHashMap)
        assertEquals(out, stream.toString().trimIndent())
    }
}
