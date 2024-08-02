package com.example.bancodigital.util

class Mask(
    private val mMask: String,
) {

    private val symbolMask: MutableSet<String> = HashSet()

    init {
        initSymbolMask()
    }

    private fun initSymbolMask() {
        for (element in mMask) {
            if (element != '#') {
                symbolMask.add(element.toString())
            }
        }
    }

    companion object {
        fun unmask(value: String): String {
            var s = value
            val replaceSymbols: MutableSet<String> = HashSet()
            replaceSymbols.add("-")
            replaceSymbols.add(".")
            replaceSymbols.add("/")
            replaceSymbols.add("_")
            replaceSymbols.add("(")
            replaceSymbols.add(")")
            replaceSymbols.add(" ")
            for (symbol in replaceSymbols) {
                s = s.replace("[" + symbol + "]".toRegex(), "")
            }
            return s
        }

        fun mask(format: String, text: String): String {
            val maskedText = StringBuilder()
            var i = 0
            for (m in format.toCharArray()) {
                if (m != '#') {
                    maskedText.append(m)
                    continue
                }
                try {
                    maskedText.append(text[i])
                } catch (e: Exception) {
                    break
                }
                i++
            }
            return maskedText.toString()
        }
    }
}