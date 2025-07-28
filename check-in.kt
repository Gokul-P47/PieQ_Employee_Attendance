import java.time.LocalDate
import java.time.LocalTime

data class Employee(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
    val reportingTo: String
)

data class Attendance(
    val id: Int,
    val checkInDate: LocalDate,
    val checkInTime: LocalTime
)

val employeeList= mutableListOf<Employee>()
val checkInList=mutableListOf<Attendance>()
fun main(){
    addEmployees()
    while(true){
        println("1.Check In\t\t2.Print CheckIn List\t\t3.Exit")
        println("Enter your option")
        val userOption=readln().toInt()
        when(userOption){
           1->{
               println("Enter Your Employee ID: ")
               val empId=readln().toInt()
               checkIn(empId)
           }

           2->{
               printCheckInList()
           }
           3->{
               break
           }
        }
    }
}

fun checkIn(empId: Int){
    if(validateCheckIn(empId)){
        val currentDate= LocalDate.now()
        val currentTime= LocalTime.now()
        checkInList.add(
            Attendance(empId,currentDate,currentTime)
        )
        println("Check-in Successful! Employee ID: $empId ")
    }
}

fun validateCheckIn(empId: Int):Boolean{
    if(!employeeList.any { it.id == empId }){
        println("Invalid check-in. Employee ID not found!")
        return false;
    }
    if(checkInList.any { it.id == empId }){
        println("Employee has already checked in")
        return false
    }
    return true
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
            println("Check-in date: ${attendance.checkInDate}")
            println("Check-in time: ${attendance.checkInTime}")
            println()
        }
    }
}
