class WeekINFO() {
    private val payroll: List<DayINFO>


    init{
        // Lamvda expression used in collection initialization ex: List(5) { index -> index * 2 }
        // This repeats a respective expression you provide to fill up the collection
        val tempWorkD = MutableList(7) { DayINFO(DayType.NORMAL_DAY) }

        payroll = tempWorkD

    }

    fun isDaysValidType() : Boolean{
        if(payroll[5].isRestDay() && payroll[6].isRestDay())
        {
            println("${Text_Color.ORANGE}WARNING! You did not label properly the last two days of the week (day index 5 and 6)${Text_Color.RESET}}")
            return true
        }
        return false
    }

    fun displayDay(dayNum: Int){
        println(payroll[dayNum])

    }


    fun changeToRestDaysLastTwo(){
        payroll[5].changeDayType(DayType.REST_DAY)
        payroll[6].changeDayType(DayType.REST_DAY)
    }

    fun changeDayType(dayNum: Int, newType : DayType){
        payroll[dayNum].changeDayType(newType)
    }

    fun changeBaseSal(dayNum: Int, givenNew : Double)
    {
        payroll[dayNum].changeBaseDailySal(givenNew)
    }



    fun modifyIN_OUT(dayNum : Int, givenIN : MilitaryTime, givenOut : MilitaryTime)
    {
        payroll[dayNum].setInTime(givenIN)
        payroll[dayNum].setOutTime(givenOut)
    }

    fun modifyIN_OUT(dayNum : Int, givenIN_Hr : Int, givenIN_Min : Int, givenOUT_Hr : Int, givenOUT_Min : Int)
    {
        payroll[dayNum].setInTime(givenIN_Hr, givenIN_Min)
        payroll[dayNum].setOutTime(givenOUT_Hr, givenOUT_Min)
    }


    fun modifyALL_IN_OUT(givenIN_Hr : Int, givenIN_Min : Int, givenOUT_Hr : Int, givenOUT_Min : Int)
    {
        for ((ind, day) in payroll.withIndex()){
            modifyIN_OUT(ind, givenIN_Hr , givenIN_Min, givenOUT_Hr, givenOUT_Min)
        }
    }

    fun modifyALL_IN_OUT(dayNum : Int, givenIN : MilitaryTime, givenOut : MilitaryTime)
    {
        for ((ind, day) in payroll.withIndex()){
            modifyIN_OUT(ind, givenIN, givenOut)
        }
    }

    fun totalWeeklySalary() : Double
    {
        var sumUp : Double = 0.0
        for (day in payroll){
            sumUp += day.getFinalSal()
        }
        return sumUp

    }


    override fun toString(): String {

        var longString : String = ""

        for ((ind, day) in payroll.withIndex()){
            longString += "\n\nDay Index: $ind\n"
            longString += "$day \n"
        }

        longString += " Total Salary of the week: ${Text_Color.GREEN}${totalWeeklySalary()}${Text_Color.RESET}"
        return longString

    }




}