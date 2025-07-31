import java.time.DateTimeException
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class EmployeeData(
    val id: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val reportingTo: String
)

data class AttendanceData(
    val id: String,
    val checkInDateTime: LocalDateTime,
    var checkOutDateTime: LocalDateTime? = null,
    var workingHrs: Duration?
)


class EmployeeService {
    var serialNumber: Int=105
    val employeeList = mutableListOf<EmployeeData>() //To store employees

    init {
        addInitialEmployees()
    }

    fun addInitialEmployees() {
        employeeList.addAll(
            listOf(
                EmployeeData("PieQ101", "Gokul", "P", "Developer", "Bob John"),
                EmployeeData("PieQ102", "Mark", "Lee", "Developer", "Bob John"),
                EmployeeData("PieQ103", "Jack", "Lee", "Tester", "Bob John"),
                EmployeeData("PieQ104", "Ashok", "Kumar", "Designer", "Bob John"),
                EmployeeData("PieQ105", "Bob", "John", "Manager", "CEO")
            )
        )
    }

    //Add new Employee
    fun addEmployee(empFirstName: String, empLastName: String, empRole: String, reporting: String) :String{
        val empId:String= generateEmpId()
        employeeList.add(EmployeeData(empId, empFirstName, empLastName, empRole, reporting))
        return empId
    }

    fun generateEmpId():String{
        serialNumber++;
        return "PieQ$serialNumber"
    }

    fun getEmployeesList(): List<String> {
        val list = mutableListOf<String>()

        if (!employeeList.isEmpty()) {
            for (employee in employeeList) {
                list.add("empID        : ${employee.id}")
                list.add("Name         : ${employee.firstName} ${employee.lastName}")
                list.add("Role         : ${employee.role}")
                list.add("Reporting To : ${employee.reportingTo}")
                list.add("")
            }
        }
        return list
    }

    fun employeeExists(empId: String): Boolean {
        return employeeList.any { it.id == empId }
    }

    fun getEmployee(empId: String): EmployeeData? {   //Return Employee object
        return employeeList.find { it.id == empId }
    }
}

class AttendanceService(private val employeeService: EmployeeService) {
    private val checkInList = mutableListOf<AttendanceData>()

    fun checkIn(empId: String, checkInDateTime: LocalDateTime): Boolean {
        if (!employeeService.employeeExists(empId)){
            println("Employee ID not found")
            return false      //Employee not found with the given id
        }
        if (!validateCheckIn(empId, checkInDateTime)) {
            println("Employee has already checked in")
            return false     //Check whether the employee has already checked in today or not
        }

        checkInList.add(AttendanceData(empId, checkInDateTime,null,null))
        return true
    }

    fun validateCheckIn(empId: String, inputDateTime: LocalDateTime): Boolean {
        val hasCheckedIn = checkInList.any {         //check whether already checked in today or not
            it.id == empId && it.checkInDateTime.toLocalDate() == inputDateTime.toLocalDate()
        }
        return !hasCheckedIn    //If already checked in means invalid check-in
    }

    fun checkOut(empId: String,checkOutDateTime: LocalDateTime):String?{
        if (!employeeService.employeeExists(empId)){
            println("Employee ID not found")
            return null      //Employee not found with the given id
        }
        val attendance: AttendanceData?= validateCheckOut(empId,checkOutDateTime)
        if(attendance== null){
            println("No valid check-in yet")
            return null     //Invalid check-out
        }

        val formatter= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

        val totHrs= Duration.between(attendance.checkInDateTime,checkOutDateTime)
        attendance.checkOutDateTime=checkOutDateTime
        attendance.workingHrs=totHrs
        return "EmpId: $empId CheckInTime: ${attendance.checkInDateTime.format(formatter)} " +
                "CheckOutTime: ${checkOutDateTime.format(formatter)} workingHrs: ${totHrs.toHours()}h ${totHrs.toMinutes()}m"

    }

    fun validateCheckOut(empId: String, checkOutDateTime: LocalDateTime): AttendanceData? {
        val attendance:AttendanceData? =checkInList.find {
            it.id == empId &&
                    it.checkInDateTime.toLocalDate() == checkOutDateTime.toLocalDate() &&
                    it.checkOutDateTime == null
        }
        // Check whether check in time is greater than or equal to check out time in terms of day,hr,minutes ignoring seconds
        if(attendance==null ||attendance.checkInDateTime.truncatedTo(ChronoUnit.MINUTES) >= checkOutDateTime.truncatedTo(ChronoUnit.MINUTES)) return null
        return attendance
    }

    fun getCheckInList(): List<String> {
        val checkInStrings = mutableListOf<String>()

        if (!checkInList.isEmpty()) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

            for (attendance in checkInList) {
                val employee = employeeService.getEmployee(attendance.id)
                if (employee != null) {
                    checkInStrings.add("empID         : ${employee.id}")
                    checkInStrings.add("Name          : ${employee.firstName} ${employee.lastName}")
                    checkInStrings.add("Check-in Time : ${attendance.checkInDateTime.format(formatter)}")
                    val checkOutFormatted = attendance.checkOutDateTime?.format(formatter) ?: "N/A"
                    checkInStrings.add("Check-out Time: $checkOutFormatted")
                    checkInStrings.add("Total Hrs     : ${attendance.workingHrs}")
                    checkInStrings.add("")
                }
            }
        }
        return checkInStrings
    }
}


fun main() {
    val employeeService = EmployeeService()
    val attendanceService = AttendanceService(employeeService)

    while (true) {
        println("1.Add Employee")
        println("2.Check In")
        println("3.Get Employees List")
        println("4.Get Check-In List")
        println("5.Check Out")
        println("6.Exit")
        print("Choose an option: ")
        when (readln().toIntOrNull()) {
            1->{
                addEmployee(employeeService)
            }
            2 -> {
               checkIn(attendanceService,employeeService);
            }
            3 -> {
                println("----- Employee List -----")
                val employeeEntries = employeeService.getEmployeesList()
                employeeEntries.forEach { println(it) }
            }

            4 -> {
                println("----- Check-In List -----")
                val checkInEntries = attendanceService.getCheckInList()
                checkInEntries.forEach { println(it) }
            }


            5 -> {
                checkOut(attendanceService)
            }

            6->{
                break
            }
            else -> println("Invalid option")
        }
        println()
    }
}

fun getDateTimeFromUserOrNow(): LocalDateTime? {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    print("Enter date and time (dd-MM-yyyy HH:mm) : ")
    val input = readln().trim()
    return if (input.isEmpty()) {
        LocalDateTime.now()
    } else {
        try {
            val dateTime = LocalDateTime.parse(input, formatter)
            if (dateTime.isAfter(LocalDateTime.now())) {  //Future date time
                null
            } else dateTime
        } catch (e: DateTimeException) {  //Invalid format
            null
        }
    }
}

fun addEmployee(employeeService: EmployeeService){
    println("First Name: ")
    val empFirstName=readln()
    println("Last Name: ")
    val empLastName=readln()
    println("Role: ")
    val empRole=readln()
    println("Reporting To: ")
    val reportingTo=readln()
    val empId=employeeService.addEmployee(empFirstName,empLastName,empRole,reportingTo)
    println("Employee added successfully")
    println("EmpId: $empId Name: $empFirstName $empLastName")
}

fun checkIn(attendanceService:AttendanceService,employeeService:EmployeeService){
    print("Enter Employee ID: ")
    val empId = readln()

    val inputDateTime = getDateTimeFromUserOrNow()
    if (inputDateTime == null) {
        println("Invalid dateTime")
        return
    }

    val checkInStatus = attendanceService.checkIn(empId, inputDateTime)
    if (checkInStatus) {
        val employee: EmployeeData?= employeeService.getEmployee(empId)
        val formatter= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        println("Check-in Successful! Employee Id: $empId Name: ${employee?.firstName} ${employee?.lastName} DateTime: ${inputDateTime.format(formatter)}")
    } else {
        println("Check-in Failed.") //Either user already checked-in or invalid user id
    }
}

fun checkOut(attendanceService:AttendanceService){
    print("Enter Employee ID: ")
    val empId = readln()

    val inputDateTime = getDateTimeFromUserOrNow()
    if (inputDateTime == null) {
        println("Invalid dateTime")
        return
    }

    val checkOutResult = attendanceService.checkOut(empId, inputDateTime)
    if (checkOutResult != null) {
        println("Check out successful")
        println(checkOutResult)
    } else {
        println("Check-out Failed.")
    }
}
