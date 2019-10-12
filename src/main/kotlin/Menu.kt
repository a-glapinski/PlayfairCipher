object Menu {
    fun init() {
        println(
            """Choose IO:
            | 1. Read from/print to console
            | 2. Read from/save to file
        """.trimMargin()
        )

        when (readLine()!!.toIntOrNull()) {
            1 -> consoleIo()
            2 -> fileIo()
            else -> println("Invalid option\n").also { init() }
        }
    }

    private fun consoleIo() {
        println("\nEnter keyword:")
        val keyword = readLine()!!
        val playfair = Playfair(keyword)
        println("\nPlayfair table:")
        playfair.printTable()

        println("\nEnter plaintext:")
        val plainText = readLine()!!

        val encodedText = playfair.encode(plainText)
        val decodedText = playfair.decode(encodedText)

        println("\nEncoded text:\n$encodedText")
        println("\nDecoded text:\n$decodedText")
    }

    private fun fileIo() {

    }
}