package config

import java.io.FileInputStream
import java.util.Properties

class ConfigManager {

    companion object {
        fun getKey(key: String): String {
            val properties = Properties()
            try {

                //The "config.yml" file is where you will put the TOKEN_BOT and BOT_NAME
                val inputStream = FileInputStream("src/main/resources/config.yml")
                properties.load(inputStream)

            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return properties.getProperty(key)
        }
    }

}