package bots

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class WCCBotTest {

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun verifyIfAcceptText(){
        val bot = WCCBot()
        val text = "23d,7"

        val valorBoo = bot.isNumber(text)

        assertEquals(false, valorBoo)
    }

    @Test
    fun verifyIfAcceptComma(){
        val bot = WCCBot()
        val text = "23,7"

        val valorBoo = bot.isNumber(text)

        assertEquals(false, valorBoo)
    }

    @Test
    fun verifyIfAcceptDouble(){
        val bot = WCCBot()
        val text = "23.7"

        val valorBoo = bot.isNumber(text)

        assertEquals(true, valorBoo)
    }

    @Test
    fun verifyIfAcceptEmpty(){
        val bot = WCCBot()
        val text = ""

        val valorBoo = bot.isNumber(text)

        assertEquals(false, valorBoo)
    }

    @Test
    fun verifyConvertListValue(){
        val bot = WCCBot()
        val text = "23,7-54,3-2.8"

        val valorList = bot.convertTextToNumber(text)

        assertEquals("54.3", valorList[1])
    }

    @Test
    fun verifyDataListValue(){
        val bot = WCCBot()
        val text = "23,7-54,3-2.8"

        bot.isDataListFull(text)
        val list = bot.dataList

        assertEquals(23.7, list[0])
    }

    @Test
    fun verifyDataListValueIndexOne(){
        val bot = WCCBot()
        val text = "23,7--2.8"

        bot.isDataListFull(text)
        val list = bot.dataList

        assertEquals(2.8, list[1])
    }

    @Test
    fun verifyDataListSize(){
        val bot = WCCBot()
        val text = "5,4-5.8-2,8"

        val list = bot.isDataListFull(text)

        assertEquals(true, list)
    }

    @Test
    fun verifyMessageTypeCREATE(){
        val bot = WCCBot()
        val text = "-25,9-2.8"

        val valorList = bot.isDataListFull(text)

        assertEquals( MessageStatus.CREATE, valorList)
    }

}