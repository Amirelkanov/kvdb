import java.io.File

enum class Color(val ANSICode: String) {
    RED("\u001b[31m"),
    GREEN("\u001b[32m"),
    RESET("\u001B[0m")
}

fun positiveMessage(message: String) = Color.GREEN.ANSICode + message + Color.RESET.ANSICode
fun negativeMessage(message: String) = Color.RED.ANSICode + message + Color.RESET.ANSICode
fun help(): String {
    return """
        -------------------------------------------------------------
        Вы можете использовать следующие команды:
        * Help - вывод информации о всех возможных командах
        * Selectall - вывод всей БД
        * Select <ключ> - нахождение записи по ключу
        * Delete <ключ> - удаление записи по ключу
        * Put <ключ> <значение> - добавление новой записи в БД
        * Set <ключ> <значение> - изменение существующей записи в БД
        * Quit (для UI интерфейса) - выход из программы
        -------------------------------------------------------------
    """.trimIndent()
}

typealias Database = HashMap<String, String>

/**
 * ## Подключение к БД
 * @param filename имя файла БД, к которой нужно подключиться
 * @return данные с файла БД, преобразованные в hashmap
 */
fun connect(filename: String): Database {
    val db = hashMapOf<String, String>()
    File(filename).useLines { lines ->
        lines.forEach {
            val (key, value) = it.split(": ")
            db[key.trim()] = value.trim()
        }
    }
    return db
}

/**
 * ## Сохранение изменений в БД
 * @param filename имя файла БД, который нужно перезаписать
 * @param db преобразованные при помощи функции connect данные
 * */
fun commit(filename: String, db: Database): String {
    val strBuilder = StringBuilder()
    db.forEach { (key, value) -> strBuilder.append("$key: $value\n") }
    File(filename).writeText(strBuilder.toString())
    return positiveMessage("Данные сохранены!")
}

// Вывод всех значений БД
fun selectAll(db: Database): String {
    val message = StringBuilder()
    return if (db.isNotEmpty()) {
        message.append("Все значения базы данных:\n")
        message.append("-------------------------\n")
        db.forEach { (key, value) -> message.append("$key: $value\n") }
        message.append("-------------------------")

        message.toString()
    } else negativeMessage("База данных пуста")
}

// Вывод записи по ключу (ключ уникален)
fun select(db: Database, key: String?): String {
    return if (db.containsKey(key)) {
        "Найдена запись |$key: ${db[key]}|"
    } else negativeMessage("Запись по ключу |$key| не найдена")
}

// Удаление по ключу (ключ уникален)
fun delete(db: Database, key: String?): String {
    val message: String
    if (db.containsKey(key)) {
        message = positiveMessage("Запись |$key: ${db[key]}| успешно удалена!")
        db.remove(key)
    } else message = negativeMessage("Запись по ключу |$key| не найдена")
    return message
}

// Добавление элемента в БД (ключ уникален)
fun put(db: Database, newKey: String?, newValue: String?): String {
    val message: String
    if (newKey != null && newValue != null) {
        if (!db.containsKey(newKey)) { // Проверка на возможность дубликата по ключу
            db[newKey] = newValue
            message = positiveMessage("Запись |$newKey: $newValue| добавлена в БД!")
        } else message = negativeMessage("Ключ |$newKey| уже существует")
    } else message = negativeMessage("Проверьте корректность ввода данных")
    return message
}

// Изменение существующей записи по ключу
fun set(db: Database, key: String?, newValue: String?): String {
    val message: String
    if (key != null && newValue != null) {
        if (db.containsKey(key)) { // Проверка на существование ключа
            val oldValue = db[key]
            db[key] = newValue
            message = positiveMessage("Запись изменена: |$key: $oldValue| -> |$key: ${db[key]}|")
        } else message = negativeMessage("Ключа $key не существует")
    } else message = negativeMessage("Проверьте корректность ввода данных")
    return message
}


/** Выполнение команд БД
 * @param db БД, в которой нужно выполнить команду
 * @param args массив, состоящий из аргументов
 * */
fun doCommand(db: Database, args: Array<String>) {
    // Обработка необязательных аргументов
    val key = if (args.size >= 2) args[1] else null
    val value = if (args.size >= 3) args.drop(2).joinToString(" ") else null

    // Обработка команд
    println(when (args[0].lowercase()) {
        "help" -> help()
        "select" -> select(db, key)
        "selectall" -> selectAll(db)
        "delete" -> delete(db, key)
        "set" -> set(db, key, value)
        "put" -> put(db, key, value)
        else -> negativeMessage("Такой команды не существует.")
    })
}


// Обработка данных, введенных пользователем в UI
fun uiInput(db: Database) {
    println("Добро пожаловать в утилиту AmEl KVDB, реализующую работу с key-value БД!")
    println("Для получения более подробной информации о возможностях программы введите команду help\n")

    while (true) {
        print("Введите команду: ")
        val input = readLine()

        if (input != null) {
            if (input.lowercase() == "quit") break else doCommand(db, input.split(" ").toTypedArray())
        } else println(negativeMessage("При считывании данных произошла ошибка"))
    }
}

// Запуск программы
fun main(args: Array<String>) {
    // В папке data лежат еще две тестовые БД - очень большая (миллион строк) и пустая
    val dbName = "data/bigData.amel"

    if (File(dbName).exists()) {
        val db = connect(dbName)
        if (args.isNotEmpty()) doCommand(db, args) else uiInput(db)
        commit(dbName, db)
    } else println(negativeMessage("Не удалось подключится к БД [${dbName.split("/").last()}]"))
}
