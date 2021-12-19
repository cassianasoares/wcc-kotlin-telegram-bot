package poi

import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.io.FileOutputStream

object Spreadsheet {

    //get the spreadsheet inside directory resources (read)
    private val input = FileInputStream("src/main/resources/personal_finance_spreadsheet.xlsx")
    private val my_xlsx_workbook = XSSFWorkbook(input)
    private val first_sheet = my_xlsx_workbook.getSheetAt(0) //get the first sheet from the spreadsheet

    //get the cells witch will be modified
    private val cellUserName = first_sheet.getRow(0).getCell(0)
    private val cellMoneyToSpend = first_sheet.getRow(4).getCell(3)
    private val cellMoneyToEntertainment = first_sheet.getRow(5).getCell(3)
    private val cellMoneyToTravel = first_sheet.getRow(6).getCell(3)
    private val cellIncomeTotal = first_sheet.getRow(3).getCell(1)
    private val cellExpenditureTotal = first_sheet.getRow(3).getCell(4)
    private val cellBillsTotal = first_sheet.getRow(4).getCell(13)

    private val log: Logger = LoggerFactory.getLogger("poi")


    fun updateSheet(userName: String, income: Double, expenditure: Double, bill: Double) {

        log.info("Start changes")

        //change the values from the cells
        cellUserName.setCellValue("Planilha para Cassiana ficar Milionario(a)!!")
        cellMoneyToSpend.setCellValue("$userName (mesada)")
        cellMoneyToEntertainment.setCellValue("$userName Lazer")
        cellMoneyToTravel.setCellValue("$userName Viagem")
        cellIncomeTotal.setCellValue(income)
        cellExpenditureTotal.setCellValue(expenditure)
        cellBillsTotal.setCellValue(bill)

        //variable that update the values on the sheet (without this the cells won't be modified before open and click)
        val evaluator: FormulaEvaluator = my_xlsx_workbook.creationHelper.createFormulaEvaluator()
        evaluator.evaluateAll()

        //close file with changes
        input.close()

        //create a new spreadsheet from the previous one (write)
        val outPut = FileOutputStream("src/main/resources/personal_finance.xlsx")
        my_xlsx_workbook.write(outPut)
        //close export
        outPut.close()

        log.info("Finish changes and update. Create a sheet copy.")

    }

}