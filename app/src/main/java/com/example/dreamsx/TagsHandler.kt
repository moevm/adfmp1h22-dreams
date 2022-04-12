package com.example.dreamsx

import java.util.*

//Обработка тегов
class TagsHandler {
    fun getTopTags(tags: Array<String>) : String {
        val countTags = mutableMapOf<String, Int>()

        var tags2 : Array<String> = arrayOf()

        for (tag in tags){
            if (tag.contains(" ")) {
                var splitedTags : List<String> = tag.split(" ")
                tags2 += splitedTags
            } else{
                tags2 += tag
            }

        }

        for (tag in tags2){
            var tag2 = tag.lowercase(Locale.getDefault())
            if (!countTags.containsKey(tag2))
                countTags[tag2] = 1
            else
                countTags[tag2] = countTags.getValue(tag2) + 1
        }

        val values: MutableCollection<Int> = countTags.values
        val maxValue = values.maxOrNull()

        val topTags: MutableList<String> = mutableListOf()
        var key: String
        for (teg in countTags){
            key = teg.key
            if (countTags[key] == maxValue)
                topTags.add(key)
        }

        var stringTopTegs: String = ""

        var newTeg: String = ""
        for (teg in topTags){
            newTeg = "$teg "
            stringTopTegs += newTeg
        }
        return stringTopTegs
    }

    private fun remove(arr: IntArray, index: Int): IntArray {
        return if (index < 0 || index >= arr.size) {
            arr
        } else (arr.indices)
            .filter { i: Int -> i != index }
            .map { i: Int -> arr[i] }
            .toIntArray()
    }
}