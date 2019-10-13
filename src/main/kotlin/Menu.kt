import java.io.File

object Menu {
    tailrec fun init() {
        println(
            """
            |Choose option:
            | 1. Read from/print to console
            | 2. Read from/save to file
            | 3. Exit program
        """.trimMargin()
        )

        when (readLine()!!.toIntOrNull()) {
            1 -> consoleIo()
            2 -> fileIo()
            3 -> return
            else -> println("Invalid option\n")
        }

        init()
    }

    private fun consoleIo() {
        println("\nEnter keyword:")
        val keyword = readLine()!!
        val playfair = Playfair(keyword)
        println("\nPlayfair table:")
        print(playfair.tableToString())

        println("\nEnter plaintext:")
        val plainText = readLine()!!

        val encodedText = playfair.encode(plainText)
        val decodedText = playfair.decode(encodedText)

        println("\nEncoded text:\n${encodedText.spaceEverySecondLetter()}")
        println("\nDecoded text:\n${decodedText.spaceEverySecondLetter()}")

        println("\nPress Enter to continue...")
        readLine()!!
    }

    private fun fileIo() {
        val inputFile = File("input.txt")
        val outputFile = File("output.txt")
        check(inputFile.exists()) { "Input file doesn't exist." }
        check(outputFile.exists()) { "Output file doesn't exist." }

        val (keyword, plainText) = inputFile.readLines()
        val playfair = Playfair(keyword)

        val encodedText = playfair.encode(plainText)
        val decodedText = playfair.decode(encodedText)

        val output = buildString {
            appendln("Playfair table:")
            appendln(playfair.tableToString())
            appendln("Encoded text: ${encodedText.spaceEverySecondLetter()}")
            appendln("Decoded text: ${decodedText.spaceEverySecondLetter()}")
        }

        outputFile.writeText(output)
    }
}

fun String.spaceEverySecondLetter() = this.chunked(2).joinToString(" ")