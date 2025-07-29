import java.time.LocalDate
import java.time.LocalTime
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
    val checkInDate: LocalDate,
    val checkInTime: LocalTime
)

val employeeList= mutableListOf<Employee>()
val checkInList= mutableListOf<Attendance>()
fun main(){
    addEmployees()
    while(true){
        println("1.Check In\t\t2.Print CheckIn List\t\t3.Exit")
        println("Enter your option")
        val userOption=readln().toIntOrNull()
        if(userOption==null){
            println("Invalid input")
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

fun checkIn(empId: Int){
    if(validateCheckIn(empId)) {
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        checkInList.add(
            Attendance(empId, currentDate, currentTime)
        )
        val employee = employeeList.find { it.id == empId }
        println("Check-in Successful! Employee ID: $empId  Name: ${employee?.firstName} ${employee?.lastName}")
    }
}

fun validateCheckIn(empId: Int):Boolean{
    if(!employeeExists(empId)){
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
            val formatter= DateTimeFormatter.ofPattern("HH:mm:ss")
            println("Check-in time: ${(attendance.checkInTime).format(formatter)}")
            println()
        }
    }
}

fun employeeExists(empId: Int):Boolean{
    return employeeList.any { it.id == empId }
}
