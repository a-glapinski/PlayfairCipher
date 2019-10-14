class Playfair(keyword: String) {
    companion object {
        const val REGEX_PATTERN = "[A-Za-z _.,!\"'$&;:?@-]*"

        val alphabet = ('A'..'Z') - 'J'

        private fun String.playfairFormat() = this.filter { it.isLetter() }
            .toUpperCase()
            .replace('J', 'I')
    }

    init {
        require(keyword.matches(Regex(REGEX_PATTERN))) {
            "Keyword must consist of letters A-Z and/or special characters."
        }
    }

    private val formattedKeyword = keyword.playfairFormat()
    private val table: List<List<Char>> = populateTable()

    fun encode(plainText: String): String {
        require(plainText.matches(Regex(REGEX_PATTERN))) {
            "Plaintext must consist of letters A-Z and/or special characters."
        }

        return formatAndSplitToPairs(plainText).fold("") { acc: String, (first: Char, second: Char) ->
            val (row1, col1) = indexOfChar(first)
            val (row2, col2) = indexOfChar(second)
            acc + when {
                row1 == row2 ->
                    "${table[row1][(col1 + 1) % 5]}" + "${table[row2][(col2 + 1) % 5]}"

                col1 == col2 ->
                    "${table[(row1 + 1) % 5][col1]}" + "${table[(row2 + 1) % 5][col2]}"

                else -> "${table[row1][col2]}" + "${table[row2][col1]}"
            }
        }
    }

    fun decode(encodedText: String): String {
        return encodedText.chunked(2).map {
            it.first() to it.last()
        }.fold("") { acc: String, (first: Char, second: Char) ->
            val (row1, col1) = indexOfChar(first)
            val (row2, col2) = indexOfChar(second)
            acc + when {
                row1 == row2 ->
                    "${table[row1][if (col1 > 0) col1 - 1 else 4]}" + "${table[row2][if (col2 > 0) col2 - 1 else 4]}"

                col1 == col2 ->
                    "${table[if (row1 > 0) row1 - 1 else 4][col1]}" + "${table[if (row2 > 0) row2 - 1 else 4][col2]}"

                else -> "${table[row1][col2]}" + "${table[row2][col1]}"
            }
        }
    }

    fun tableToString() = buildString {
        for (i in 0..4) {
            for (j in 0..4) append(table[i][j] + "\t")
            appendln()
        }
    }

    private fun populateTable(): List<List<Char>> {
        val distinctKeywordLetters = formattedKeyword.toCharArray().distinct()
        val remainingLetters = alphabet - distinctKeywordLetters
        val remainingAfterLast = remainingLetters - alphabet.filter { it < formattedKeyword.lastOrNull() ?: 'A' }
        val remainingBeforeLast = remainingLetters - alphabet.filter { it > formattedKeyword.lastOrNull() ?: 'Z' }
        val letters = distinctKeywordLetters + remainingAfterLast + remainingBeforeLast

        return letters.chunked(5)
    }

    private fun formatAndSplitToPairs(plainText: String): List<Pair<Char, Char>> {
        return plainText.playfairFormat().fold("") { acc: String, c: Char ->
            if (acc.lastOrNull() == c) acc + "X$c"
            else acc + "$c"
        }.let {
            when {
                it.hasEvenLength() -> it
                it.last() != 'X' -> it + "X"
                else -> it + "Z"
            }
        }.chunked(2).map { it.first() to it.last() }
    }

    private data class Index(val row: Int, val column: Int)

    private fun indexOfChar(c: Char): Index {
        for (i in 0..4)
            for (j in 0..4)
                if (table[i][j] == c) return Index(i, j)
        throw Exception("$c must be in the table.")
    }
}

private fun String.hasEvenLength() = this.length % 2 == 0