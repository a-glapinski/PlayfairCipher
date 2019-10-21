import java.io.File
import java.lang.IllegalStateException

private const val KEYWORD = "MANCHESTER"

object Menu {
    tailrec fun init() {
        println(
            """
            |Choose option:
            | 1. Read from console
            | 2. Read from file
            | 3. Exit program
        """.trimMargin()
        )

        when (readLine()!!.toIntOrNull()) {
            1 -> consoleIo()
            2 -> fileIo()
            3 -> return
            else -> println("Invalid option.\n")
        }

        init()
    }

    private fun consoleIo() {
        val playfair = Playfair(KEYWORD)

        println("\nEnter plaintext:")
        val plainText = readLine()!!
        val encodedText = playfair.encode(plainText)

        println("\nEncoded text:\n$encodedText\n")

//        println("\nPress Enter to continue...")
//        readLine()!!
    }

    private fun fileIo() {
        val playfair = Playfair(KEYWORD)

        val inputFile = File("input.txt")
        val outputFile = File("output.txt")

        try {
            check(inputFile.exists()) { "Input file doesn't exist." }
            check(outputFile.exists()) { "Output file doesn't exist." }
        } catch (exception: IllegalStateException) {
            println("ERROR: Make sure files: 'input.txt' and 'output.txt' exist in the same directory as executable file.\n")
            return
        }

        val plainText = inputFile.readLines()
        val encodedText = playfair.encode(plainText.joinToString(""))

        outputFile.writeText(encodedText)
        println("\nOutput was saved to file.\n")
    }
}

// fun String.spaceEverySecondLetter() = this.chunked(2).joinToString(" ")