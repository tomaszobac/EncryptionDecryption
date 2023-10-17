package encryptdecrypt

import java.io.File

fun main(args: Array<String>) {
    var index: Int
    val mode: String
    val text: String
    val key: Int
    val algorithm: String
    var input: File? = null
    var output: File? = null

    if ((args.indexOf("-alg").also { index = it }) != -1) algorithm = args[index + 1] else algorithm = "shift"

    if ((args.indexOf("-mode").also { index = it }) != -1) mode = args[index + 1] else mode = "enc"

    if ((args.indexOf("-key").also { index = it }) != -1) key = args[index + 1].toInt() else key = 0

    if ((args.indexOf("-in").also { index = it }) != -1) {
        input = File(args[index + 1])
        text = input.readText()
    } else if ((args.indexOf("-data").also { index = it }) != -1)
        text = args[index + 1]
    else text = ""

    if ((args.indexOf("-out").also { index = it }) != -1) output = File(args[index + 1])

    if (input != null && !(input.exists())) {
        println("Error: input file doesn't exist")
        return
    }

    var outText = ""

    when (algorithm) {
        "unicode" -> text.forEach {
            var char = it
            if (mode == "enc") char += key
            if (mode == "dec") char -= key
            outText += char
        }
        "shift" -> text.forEach {
            if (it.code in 97..122 || it.code in 65..90) {
                var char = it
                for (remainder in 1..key) {
                    if (mode == "enc") {
                        char++
                        if (char.code == 123) char = 'a'
                        if (char.code == 91) char = 'A'
                    }
                    if (mode == "dec") {
                        char--
                        if (char.code == 96) char = 'z'
                        if (char.code == 64) char = 'Z'
                    }
                }
                outText += char
            }
            else outText += it
        }
    }
    if (output != null) output.writeText(outText) else println(outText)
}