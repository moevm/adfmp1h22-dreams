package com.example.dreamsx

//Обработка тегов
class TagsHandler {
    fun getTopTags(tags: Array<String>) : String {
        val countTags = mutableMapOf<String, Int>()
        for (teg in tags){
            if (!countTags.containsKey(teg))
                countTags[teg] = 1
            else
                countTags[teg] = countTags.getValue(teg) + 1
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
            if(teg.startsWith("#")){
                newTeg = "$teg "
            } else{
                newTeg = "#$teg "
            }
            stringTopTegs += newTeg
        }
        return stringTopTegs
    }
}