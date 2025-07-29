import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Employee(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
    val reportingTo: String
)

data class Attendance(
    val id: Int,
    val checkInDateTime: LocalDateTime
)

val employeeList= mutableListOf<Employee>()
val checkInList= mutableListOf<Attendance>()

fun main(){
    addEmployees()
    displayMenuOptions()
}

fun checkIn(empId: Int){
    if(!employeeExists(empId)){
        println("Employee id not found")
        return
    }
    val inputDateTime: LocalDateTime? = getDateTimeFromUserOrNow()
    if(inputDateTime!=null) {
        if(validateCheckIn(empId,inputDateTime)){
            checkInList.add(
                Attendance(empId, inputDateTime)
            )
            val employee = employeeList.find { it.id == empId }
            println("Check-in Successful!")
            val formatter= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            println("Employee ID: $empId  Name: ${employee?.firstName} ${employee?.lastName} DateTime: ${inputDateTime.format(formatter)}")
        }
    }
}

fun validateCheckIn(empId: Int,inputDateTime: LocalDateTime):Boolean{
    var isValid: Boolean= true;
    val attendance= checkInList.find { it.id == empId }
    if((attendance!=null) && (hasCheckedInOnDate(empId,inputDateTime))){
        isValid=false
        println("Employee has already checked in on ${inputDateTime.toLocalDate()}")
    }
    return isValid
}



fun printCheckInList(){
    println("----- Check-In List -----")
    if(checkInList.isEmpty()){
        println("No Check-ins!!!")
        return
    }
    for(attendance in checkInList){
        val employee= employeeList.find{it.id==attendance.id}
        if(employee!=null){
            println("ID           : ${employee.id}")
            println("Name         : ${employee.firstName} ${employee.lastName}")
            val formatter= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            println("Check-in dateTime: ${(attendance.checkInDateTime).format(formatter)}")
            println()
        }
    }
}

fun employeeExists(empId: Int):Boolean{
    return employeeList.any { it.id == empId }
}



fun getDateTimeFromUserOrNow(): LocalDateTime? {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    print("Enter date and time (dd-MM-yyyy HH:mm) or press Enter to use current: ")

    val input = readln().trim()
    return if (input.isEmpty()) {
        LocalDateTime.now()
    }
    else {
        try {
            val dateTime= LocalDateTime.parse(input, formatter)
            if(dateTime.isAfter(LocalDateTime.now())){
                println("Invalid date-time")
                null
            }
            else
                dateTime
        } catch (e: DateTimeException) {
            println("Invalid format!")
            null
        }
    }
}

fun hasCheckedInOnDate(empId: Int, dateTime: LocalDateTime): Boolean {
    return checkInList.any { it.id == empId && it.checkInDateTime.toLocalDate() == dateTime.toLocalDate() }
}

fun addEmployees(){
    employeeList.addAll(
        listOf(
            Employee(101,"Gokul","P","Developer","Bob John"),
            Employee(102,"Mark","Lee","Developer","Bob John"),
            Employee(103,"Mark","Lee","Tester","Bob John"),
            Employee(104,"Ashok","Kumar","Designer","Bob John"),
            Employee(105,"Bob","John","Manager","CEO")
        )
    );
}

fun displayMenuOptions(){
    while(true){
        println("1.Check In\t\t2.Print CheckIn List\t\t3.Exit")
        println("Enter your option")
        val userOption=readln().toIntOrNull()
        if(userOption==null){
            println("Invalid input")
            println()
            continue
        }
        when(userOption){
            1->{
                println("Enter Your Employee ID: ")
                val empId=readln().toIntOrNull()
                if(empId!= null){
                    checkIn(empId)
                }
                else{
                    println("Invalid User Id")
                }
            }

            2->{
                printCheckInList()
            }
            3->{
                break
            }
            else->{
                println("Invalid Option")
            }
        }
        println()
    }
}
