package bots

import bots.Constants.URL_WHAT_GIF
import bots.Constants.URL_YES_GIF
import bots.Constants.errorData
import bots.Constants.startCreate
import bots.Constants.unknownCommand
import bots.Constants.welcomeMessage
import com.vdurmont.emoji.EmojiParser
import config.ConfigManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import poi.Spreadsheet.updateSheet
import java.io.File
import java.util.regex.Pattern

enum class MessageStatus{
    START, CREATE, UNKNOWN, FINISH
}

class WCCBot : TelegramLongPollingBot() {

    private val sendDocument = SendDocument()
    private val sendMessage = SendMessage()
    val dataList = arrayListOf<Double>()

    private val log: Logger = LoggerFactory.getLogger("bot")

    private var messageType = MessageStatus.START

    //get new sheet created
    private val file = File("src/main/resources/personal_finance.xlsx")


    override fun getBotUsername(): String {
        //return bot username
        // If bot username is @HelloKotlinBot, it must return
        return ConfigManager.getKey("TOKEN_NAME")
    }

    override fun getBotToken(): String {
        // Return bot token from BotFather
        return ConfigManager.getKey("TOKEN_BOT")
    }

    override fun onUpdateReceived(update: Update?) {
        // We check if the update has a message and the message has text

        val nameSender = update?.message?.from?.firstName
        val chatId = update?.message?.chatId.toString() // to know for whom send the message
        val messageCommand = update?.message?.text?.lowercase()?.replace(" ", "")

        changeMessageStatus(messageCommand!!)

        log.info(messageCommand)
        log.info(messageType.toString())

        try {

            when (messageType) {
                MessageStatus.START -> {
                    sendDocument.apply {
                        this.chatId = chatId
                        this.caption = welcomeMessage
                        this.document = InputFile().setMedia(URL_YES_GIF)
                        this.parseMode = "MarkdownV2"
                    }
                    execute(sendDocument)
                }
                MessageStatus.CREATE -> {
                    sendMessage.apply {
                        this.chatId = chatId
                        this.text = startCreate
                    }

                    execute(sendMessage)
                    messageType = MessageStatus.FINISH
                }
                MessageStatus.FINISH-> {
                    if(isDataListFull(messageCommand)){
                        updateSheet(nameSender!!, dataList[0], dataList[1], dataList[2])

                        sendDocument.apply {
                            this.chatId = chatId
                            this.caption = EmojiParser.parseToUnicode("Sua planilha, $nameSender :blush:")
                            this.document = InputFile().setMedia(file)
                            this.parseMode = "MarkdownV2"
                        }
                        execute(sendDocument)

                        //delete file after send
                        file.delete()
                        log.info("delete temp file")

                        messageType = MessageStatus.START
                    }else{
                        sendMessage.apply {
                            this.chatId = chatId
                            this.text = errorData
                        }

                        execute(sendMessage)
                    }
                }
                else -> {
                    sendDocument.apply {
                        this.chatId = chatId
                        this.caption =
                            EmojiParser.parseToUnicode(unknownCommand)
                        this.document = InputFile().setMedia(URL_WHAT_GIF)
                        this.parseMode = "MarkdownV2"
                    }
                    execute(sendDocument)
                }
            }

        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }


    private fun changeMessageStatus(message: String?){
        messageType = when(message!!) {
            "/start" -> MessageStatus.START
            "/create" -> MessageStatus.CREATE
            else -> {
                if(messageType != MessageStatus.FINISH){
                    MessageStatus.UNKNOWN
                }else{
                    MessageStatus.FINISH
                }
            }
        }
    }


    fun isNumber(text: String): Boolean{
        val decimalPattern = "([0-9]*)\\.([0-9]*)"
        return Pattern.matches(decimalPattern, text)
    }

    fun convertTextToNumber(text: String?): List<String>{
       //create a list with each element before and after "-"
       return text!!.replace(",",".").split("-")
    }

    fun isDataListFull(text: String?): Boolean{

        dataList.clear()

        convertTextToNumber(text).forEach { value ->
            if(isNumber(value) && dataList.size < 3) {
                log.info(text.toString())
                dataList.add(value.toDouble())
            }
        }

        return dataList.size == 3
    }

}