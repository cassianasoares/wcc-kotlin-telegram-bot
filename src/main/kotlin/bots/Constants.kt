package bots

object Constants {

    val welcomeMessage = """
       *Olá, tudo bem\?*
       Bem\-vindo ao Future Millionaire
       
       \/start \- começar
       \/create \- criar planilha da riqueza
    """.trimIndent()

    val startCreate = """
        Digite os valores de suas RENDAS, DESPESAS e DIVIDAS totais. 
        
        Escreva como no exemplo 
        (se o valor for zero, escreva: 0,0):
        
        2870,0 - 254,6 - 845,7
    """.trimIndent()

    const val errorData = "Valores incorretos tente de novo /create"
    const val unknownCommand = "Acho que eu não entendi :anguished: Vamos recomeçar /start"


    const val URL_YES_GIF = "http://24.media.tumblr.com/tumblr_lp6b71goTj1r0gsgto1_500.gif"
    const val URL_WHAT_GIF = "https://i.gifer.com/9qJ6.gif"

}