


import java.math.BigDecimal
import java.math.RoundingMode
import javax.print.DocFlavor.INPUT_STREAM


// 0- NON_OT, base payrate Pay, not exceeding 8 hours
// 1 - OT
// 2 - OT and Night shift
enum class DayType (val label : String, val fullLabel : String, val rates : List<Double>){
    NORMAL_DAY("N", "Normal Day", listOf(1.0, 1.25, 1.375)),
    REST_DAY("R", "Rest Day", listOf(1.3, 1.69, 1.859)),
    SPECIAL_NWD("SNWD", "Special Non-Working Day", listOf(1.3, 1.69, 1.859)),
    SPECIAL_NWD_REST_DAY("SNWDR", "Special Non-Working Day and Rest Day", listOf(1.5, 1.95, 2.145)),
    REG_HOL("H", "Regular Holiday", listOf(2.0, 2.6, 2.86)),
    REG_HOL_REST_DAY("HR", "Regular Holiday and Rest Day", listOf(2.6, 3.38, 3.718));


    /**
     *
     * This function calculates the pay based on the type of worktime type asked and the hours given
     * workTime - is the type of work time pay to be caclulated
     * hours - number of work hours that is specified on that type of work tim, this includes unpaid break time
     * payRate - the double data type of the salary you would be getting on
     */
    fun calcPay (workTime : WorkTimeType, hours: Int, payRate: Double, isWithinEight : Boolean) : Double{

        val chosenRate : Double
        var finalAns : Double




        when(workTime)
        {
            WorkTimeType.NON_OT -> {chosenRate = rates[0]
                                    return roundToTwo( payRate * chosenRate)
                                    }
            WorkTimeType.OT -> chosenRate = rates[1]


            WorkTimeType.OT_NIGHT ->{
                                        // Case wherein the employee does not exceed eight hours but worked within a night shift
                                        if (isWithinEight){
                                            return roundToTwo( hours * payRate / 8 * 1.1)
                                        }
                                        else
                                            chosenRate = rates[2]
                                    }

            else -> chosenRate = 0.0


        }
//        println("Chosen rate: $chosenRate and hours to calculate is $hours")

        if (hours <= 0)
            return  0.0
        return roundToTwo( hours * payRate / 8 * chosenRate)

    }


    /**
     * This function returns the data type based on the label it is given
     */
    companion object{
        fun fromLabel(label: String) : DayType?{
            return values().find{it.label==label}
        }
    }

    /**
     * Returns the label that identify this particular enum
     */
    fun getMyLabel() : String{
        return label
    }


}
enum class WorkTimeType{

    OT,
    OT_NIGHT,
    NON_OT

}


/*
* Regular work hours for an employee is 8 hours per day
* Work sched starts at 0900 and ends in 1800, all include an unpaid one hour of break time
* Work at 2200 to 0600 is considered a night shift, 10% of hourly rate will be added for every HOUR in night shift
* Hourly rate is computed via daily salary, divided by max WORK HOURs per day
* Employee receives additional pay on rest day.
*
*
* Rest Day - 130% of hourly rate
* Special non - working day - 130% daily salary rate
* Special non-working day and rest day - 150% of hourly rate
* Regular Holiday - 200%
* Regular Holiday and Rest Day = 260%
* */





class Payroll()
{
    var salary = 500.00
    var MAX_WORK_PER_DAY = 8
    var time_IN = "0900"
    var time_OUT = "0900"


}



fun roundToTwo(amt : Double) : Double
{
    val ans = BigDecimal(amt).setScale(2, RoundingMode.HALF_EVEN).toDouble();
    return ans
}

fun displayEditDayMenu()
{
    println(" [I] - Edit In Time and Out Time Info" +
            "\n [P] - Edit Daily Pay" +
            "\n [D] - Edit Day Type" +
            "\n [E] - Exit to day select menu and refresh weekly data")

}


fun displayChoicesStartMenu()
{


    println("Weekly Pay Manager")
    println(" ${Text_Color.CYAN}[S]${Text_Color.RESET} - Start" +
            "\n ${Text_Color.CYAN}[E]${Text_Color.RESET} - Exit")


}
fun inputDayType() : DayType
{
    var userDayType : DayType?
    var userInput : String
    println("Select what Type of day it is")
    DayType.values().forEach {
        dayType -> println("${Text_Color.CYAN}[${dayType.label}]${Text_Color.RESET} - ${dayType.fullLabel}")
    }
    do
    {
        userInput = readln().uppercase()
        userDayType = DayType.fromLabel(userInput)
        if(userDayType == null)
        {
            println("${Text_Color.ORANGE}ERROR! Please select from the choices highlighted above${Text_Color.RESET}")
        }

    }while(userInput == null || userDayType == null)

    var notNullType : DayType = userDayType

    return notNullType


}
fun inputMilitaryTime() : MilitaryTime
{
    var newTime : MilitaryTime
    var givenTime : String = ""

    while(':' !in givenTime)
    {
        print("\n INPUT: ")
        givenTime = readln()
        if (':' !in givenTime){
            println("${Text_Color.ORANGE}INVALID INPUT!${Text_Color.RESET}")
        }
    }

    var splitRes = givenTime.split(":", limit = 2)
    var hourString = splitRes[0]
    var minString = splitRes[1]


    return MilitaryTime(hourString.toInt(), minString.toInt())
}


fun selectedDayMenu(weekRep: WeekINFO, dayNum : Int)
{

    var userInput : String = ""
    var userInputSal : String = ""
    var givenINTime : MilitaryTime
    var givenOUTTime : MilitaryTime
    var givenDayType : DayType

    while(true)
    {
        weekRep.displayDay(dayNum)
        displayEditDayMenu()
        print("INPUT: ")
        userInput = readln().uppercase()
        when(userInput[0])
        {
            'I'->{

                do{
                    print("\n\n NEW IN TIME (Military time Ex: 23:59): ")
                    givenINTime = inputMilitaryTime()
                    print("\n\n NEW OUT TIME (Military time Ex: 23:59): ")
                    givenOUTTime = inputMilitaryTime()

                    weekRep.modifyIN_OUT(dayNum, givenINTime, givenOUTTime)

                    if(givenOUTTime.subTimeForHour(givenINTime) < 8){
                        println("${Text_Color.ORANGE}In Time and Out Time is not 8 hours${Text_Color.RESET}")
                    }

                }while(givenOUTTime.subTimeForHour(givenINTime) < 8)


            }
            'P' ->{
                do{
                    print("INPUT SALARY: ")
                    userInputSal = readln()
                    if(userInputSal.toDoubleOrNull() == null && userInputSal.toIntOrNull() == null )
                    {
                        println("${Text_Color.ORANGE}WARNING! Input is not a number${Text_Color.RESET}")
                    }
                }while(userInputSal == null)


                weekRep.changeBaseSal(dayNum, userInputSal.toDouble())
            }
            'D' ->{

                givenDayType = inputDayType()
                weekRep.changeDayType(dayNum, givenDayType)

            }
            'E' -> break
            else -> println("${Text_Color.ORANGE}ERROR! Wrong choice, select from above${Text_Color.RESET}")

        }
    }




}

fun payManagerMenu()
{
    val weekReport = WeekINFO()
    var userDayInput : String
    while(true)
    {
        println(weekReport)

        print("[E] - Exit and refresh weekly data\n")
        println("Which day would you like to edit? ")
        print("Please select between (0-6), 0 meaning the first day of work\n")
        print("INPUT: ")
        userDayInput = readln().uppercase()
        when(userDayInput[0] ){
            in '0'..'6' -> selectedDayMenu(weekReport,userDayInput[0]-'0')

            'E' -> break

            else -> println("${Text_Color.ORANGE}ERROR! please input only from 0 to 6 index.${Text_Color.RESET}")

        }


    }
}


fun main(args: Array<String>) {

//    var dayGiven = DayType.SPECIAL_NWD_REST_DAY
//    var inTime = MilitaryTime(21, 30)
//    var outTime = MilitaryTime(23, 10)


    var firstDay = DayINFO(DayType.NORMAL_DAY)



    firstDay.setOutTime(21,0)


    var firstWeek = WeekINFO()
    firstWeek.modifyALL_IN_OUT(18,0,7,0)
    firstWeek.changeDayType(6, DayType.NORMAL_DAY)
    println(firstWeek)

    var userInput : String


    while(true)
    {
        displayChoicesStartMenu()
        print("\nINPUT: ")
        userInput = readln().uppercase()


        when(userInput[0])
        {
            'S' -> payManagerMenu()
            'E' -> {
                println("${Text_Color.ORANGE}Terminating Program...${Text_Color.RESET}")
                break
            }
            else -> println("${Text_Color.ORANGE}Wrong Input!${Text_Color.RESET}")

        }

    }





}