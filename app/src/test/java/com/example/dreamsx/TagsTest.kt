package com.example.dreamsx

import junit.framework.TestCase

class TagsTest :  TestCase() {


    fun testGetTopTags(){
        val tagsHandler: TagsHandler = TagsHandler()

        var tags: Array<String> = arrayOf( "#Отпуск", "#Отпуск", "#Отпуск", "Бали",
            "#Самолет",  "#Самолет",  "#Самолет")
        var expectedResult: String = "#отпуск #самолет "

        var topTagsResult: String = tagsHandler.getTopTags(tags)
        assertEquals(expectedResult, topTagsResult)
    }
}