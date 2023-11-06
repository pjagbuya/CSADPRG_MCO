import kotlin.math.abs


class DayINFO(dayTypeGiven : DayType) {

    private var dailySal : Double
    private var totalWorkTime_NIGHT : Int
    private var totalOverTime_NON_NIGHT : Int
    private var totalOverTime_NIGHT : Int
    private var inTime : MilitaryTime
    private var outTime : MilitaryTime
    private var typeOfDay : DayType
    private var finalSalary : Double = 0.0
    private val MIN_NIGHT_TIME_CUT : MilitaryTime = MilitaryTime(22, 0)
    private val MAX_NIGHT_TIME_CUT : MilitaryTime = MilitaryTime(6, 0)
    private val MAX_WORK_HOURS = 9
    init {

        dailySal = 500.0
        totalOverTime_NIGHT = 0
        totalWorkTime_NIGHT = 0
        totalOverTime_NON_NIGHT = 0
        inTime = MilitaryTime(9, 0)
        outTime =  MilitaryTime(9, 0)
        typeOfDay = dayTypeGiven
        setAndCalcFinalSal()
    }

    private fun setAndCalcFinalSal()
    {
        this.finalSalary = 0.0
        this.totalOverTime_NIGHT =0
        this.totalWorkTime_NIGHT =0
        this.totalOverTime_NON_NIGHT = 0
        var isWithinEight : Boolean =  false

        var maxNightCut : Int = MAX_NIGHT_TIME_CUT.subTimeForHour(MIN_NIGHT_TIME_CUT)

        var hrDiff : Int = outTime.subTimeForHour(this.inTime)
        var hrOT : Int = 0
        var tempHr : Int
        var hrOT_NightInWork: Int
        var hrOT_NightInOT: Int


        if (MIN_NIGHT_TIME_CUT.isThisBiggerTime(outTime) && outTime.isThisBiggerTime(this.inTime) )
        {
            var resDiff = MIN_NIGHT_TIME_CUT.subTimeForHour(outTime)
            hrOT =  if (resDiff - MAX_WORK_HOURS <= 4 ) (hrDiff - MAX_WORK_HOURS) else 4
            this.totalOverTime_NON_NIGHT = hrOT
        }
        else{
            this.totalOverTime_NON_NIGHT = 0
        }


        if(MIN_NIGHT_TIME_CUT.isThisBiggerTime(outTime) && outTime.isThisBiggerTime(this.inTime))
        {
            hrOT_NightInWork = 0
            hrOT_NightInOT = 0
        }else{
            var leftHr : Int

            tempHr = MIN_NIGHT_TIME_CUT.subTimeForHour(this.inTime) + this.totalOverTime_NON_NIGHT
            leftHr = MAX_WORK_HOURS - tempHr

            println("Hours till begin of night time $tempHr")
            println("Hours still within work time but in night time $leftHr")
            println("Supposed total hours of work is $hrDiff")
            if (leftHr < 0)
            {
                leftHr = hrDiff-tempHr
            }

            // Check if the hours are within the minimum night time cut off is intersected form inTime
            if (leftHr > 0 && tempHr < MAX_WORK_HOURS)
            {
                isWithinEight = true
                this.finalSalary += this.typeOfDay.calcPay(WorkTimeType.OT_NIGHT, leftHr, this.dailySal, isWithinEight)
                this.totalWorkTime_NIGHT = leftHr
            }
            else if (leftHr > 0 && tempHr >= MAX_WORK_HOURS)
            {
                this.totalOverTime_NON_NIGHT = tempHr - MAX_WORK_HOURS
            }

            // Cutoff time for the hours considered night shift is subtracted
            // Following code blocks execute the if statements if hours left actually had more hours
            maxNightCut -= (leftHr)

            leftHr = hrDiff - MAX_WORK_HOURS
            println("$hrDiff hours is how long he worked and overtime in non night is $hrOT hours left till night is over is $maxNightCut")


            // This block would really only activate if we had overtime, hence hrOT should always be a positive integer
            if (leftHr > 0 && leftHr <= maxNightCut)
            {
                if (this.totalWorkTime_NIGHT == 0){
                    leftHr -= this.totalOverTime_NON_NIGHT

                }
                println("ACTIVATED IF CONDITION: $hrDiff hours is how long he worked and overtime in non night is $hrOT hours left till night is over is $maxNightCut")
                println("leftHr $leftHr for hours after maximum work horus")
                isWithinEight = false
                this.finalSalary += this.typeOfDay.calcPay(WorkTimeType.OT_NIGHT, leftHr, this.dailySal, isWithinEight)
                this.totalOverTime_NIGHT = leftHr
                println("Condition result ${this.totalOverTime_NIGHT}")
                leftHr -= leftHr
            }
            else if (leftHr > 0 && leftHr > maxNightCut)
            {
                isWithinEight = false
                this.finalSalary += this.typeOfDay.calcPay(WorkTimeType.OT_NIGHT, maxNightCut, this.dailySal, isWithinEight)
                this.totalOverTime_NIGHT = maxNightCut
                leftHr -= maxNightCut
            }



            // If there is a still left over time
            if (leftHr > 0)
            {
                this.totalOverTime_NON_NIGHT += leftHr
            }


//            if (this.totalWorkTime_NIGHT + this.totalOverTime_NIGHT + MAX_WORK_HOURS + this.totalOverTime_NON_NIGHT != hrDiff)
//            {
//                this.totalOverTime_NON_NIGHT = this.totalWorkTime_NIGHT + this.totalOverTime_NIGHT + MAX_WORK_HOURS- hrDiff
//            }

        }

        // When overtime is still empty then put total overtime received
//        if (this.totalOverTime_NON_NIGHT == 0 && totalOverTime_NIGHT == 0)
//        {
//
//        }


        this.finalSalary += this.typeOfDay.calcPay(WorkTimeType.NON_OT, hrDiff, this.dailySal, isWithinEight)

        this.finalSalary += this.typeOfDay.calcPay(WorkTimeType.OT, this.totalOverTime_NON_NIGHT, this.dailySal, isWithinEight)


    }



    fun changeBaseDailySal(givenNew : Double){
        this.dailySal = givenNew

        setAndCalcFinalSal()
    }
    fun changeDayType(givenNewType : DayType)
    {
        this.typeOfDay = givenNewType
        setAndCalcFinalSal()
    }



    fun setInTime(givenHr : Int, givenMin : Int){
        this.inTime = MilitaryTime(givenHr, givenMin)
        if (this.outTime.subTimeForHour(this.inTime) >= MAX_WORK_HOURS)
            setAndCalcFinalSal()

    }
    fun setInTime(givenTime : MilitaryTime){
        setInTime(givenTime.getHour(), givenTime.getMin())

    }
    fun setOutTime(givenHr : Int, givenMin : Int){
        this.outTime = MilitaryTime(givenHr, givenMin)
        setAndCalcFinalSal()
    }

    fun setOutTime(givenTime: MilitaryTime){
        setOutTime(givenTime.getHour(), givenTime.getMin())
    }

    fun getFinalSal() : Double{
        return this.finalSalary
    }

    fun getIn() : MilitaryTime
    {
        return this.inTime
    }

    fun getOut() : MilitaryTime
    {
        return this.outTime
    }

    fun getTypeOfDay() : DayType
    {
        return this.typeOfDay
    }

    fun isHolidayOrNW() : Boolean
    {
        if (this.typeOfDay == DayType.REG_HOL ||
            this.typeOfDay == DayType.SPECIAL_NWD)
        {
            return true
        }
        return false

    }

    fun isRestDay() : Boolean
    {
        if(this.typeOfDay == DayType.REST_DAY ||
            this.typeOfDay == DayType.SPECIAL_NWD_REST_DAY ||
            this.typeOfDay == DayType.REG_HOL_REST_DAY)
            return true
        return false

    }

    override fun toString(): String {
        var finalStr : String = ""

        if (!isRestDay() && !isHolidayOrNW() &&
            inTime == outTime)
        {
            finalStr += "${Text_Color.RED}ABSEMT!${Text_Color.RESET}\n"


        }

        if (totalWorkTime_NIGHT < 0)
            totalWorkTime_NIGHT = 0
        if (totalOverTime_NIGHT < 0)
        {
            totalOverTime_NIGHT = 0
        }
        if (totalOverTime_NON_NIGHT < 0)
        {
            totalOverTime_NON_NIGHT = 0
        }

        finalStr += "Daily Rate: ${Text_Color.CYAN}$dailySal${Text_Color.RESET} \n" +
                "IN Time : ${Text_Color.GREEN}$inTime${Text_Color.RESET} \n" +
                "OUT Time: ${Text_Color.BLUE}$outTime${Text_Color.RESET} \n" +
                "Day Type: ${Text_Color.CYAN}${typeOfDay.getMyLabel()}${Text_Color.RESET} \n" +
                "Hours (${Text_Color.RED}Work Time in Night, ${Text_Color.ORANGE}Overtime in Night, ${Text_Color.GREEN}Overtime non-night${Text_Color.RESET}): " +
                "${Text_Color.RED}$totalWorkTime_NIGHT${Text_Color.RESET}, " +
                "${Text_Color.ORANGE}$totalOverTime_NIGHT${Text_Color.RESET}, " +
                "${Text_Color.GREEN}$totalOverTime_NON_NIGHT${Text_Color.RESET} \n" +
                "Salary : ${Text_Color.CYAN}" + String.format("%.02f", finalSalary) + "${Text_Color.RESET}\n"
        return finalStr
    }

}