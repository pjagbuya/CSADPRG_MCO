class MilitaryTime(hour : Int, min : Int) {

    private var hr : Int
    private var minute : Int

    init {
        this.hr = hour
        this.minute = min

        if (hour !in 0..23)
        {
            println("ERROR: Hour input detected us $hour, which is wrong")
            this.hr = 9
        }



        if (min !in 0..59)
        {
            println("ERROR: Minute input detected us $min, which is wrong")
            this.minute = 0
        }

    }

    fun isThisBiggerTime(hourGiven : Int, minGiven : Int) : Boolean
    {
        var tempMilitaryTime : MilitaryTime
        var givenTotal = hourGiven * 60 + minGiven
        var ourTotal = this.hr * 60 + this.minute

        if (givenTotal > ourTotal)
        {
            return false

        }
        else{
            return true
        }

    }
    fun isThisBiggerTime(givenMTime : MilitaryTime) : Boolean
    {
        return isThisBiggerTime(givenMTime.getHour(), givenMTime.getMin())

    }

    fun subTimeForHour(hourGiven : Int, minGiven : Int) : Int
    {
        var finalAns : Int
        var convH2M : Int
        convH2M = hourGiven * 60 + minGiven
        finalAns = 0

//        println("You inputted $hourGiven and $minGiven")
//
//        println("Comparing to ${this.hr} and ${this.minute}")
        if (this.hr < hourGiven)
        {


            finalAns = this.hr*60 + this.minute + 24*60 - convH2M

            // Convert into h
            finalAns /= 60

        }

        else if (this.hr > hourGiven)
        {
            finalAns = this.hr*60 + this.minute - convH2M
            finalAns /= 60

        }
        else{
            println("You inputted the same time, this will be considered absent or no pay for this time period")
            return -1
        }

        return finalAns


    }
    fun subTimeForHour(givenTime : MilitaryTime) : Int
    {


        var givenHour = givenTime.getHour()
        var givenMinute = givenTime.getMin()
        return subTimeForHour(givenHour, givenMinute)


    }

    fun getHour() : Int
    {
        return this.hr

    }

    fun getMin() : Int
    {
        return this.minute

    }

    override fun toString(): String {
        return String.format("%02d%02d", hr, minute)
    }



}