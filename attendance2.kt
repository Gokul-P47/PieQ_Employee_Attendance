import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class EmployeeData(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
    val reportingTo: String
)

data class AttendanceData(
    val id: Int,
    val checkInDateTime: LocalDateTime,
    var checkOutDateTime: LocalDateTime? = null,
    var totHrs:Long
)


class EmployeeService {
    val employeeList = mutableListOf<EmployeeData>() //To store employees

    init {
        addInitialEmployees()
    }

    fun addInitialEmployees() {
        employeeList.addAll(
            listOf(
                EmployeeData(101, "Gokul", "P", "Developer", "Bob John"),
                EmployeeData(102, "Mark", "Lee", "Developer", "Bob John"),
                EmployeeData(103, "Jack", "Lee", "Tester", "Bob John"),
                EmployeeData(104, "Ashok", "Kumar", "Designer", "Bob John"),
                EmployeeData(105, "Bob", "John", "Manager", "CEO")
            )
        )
    }

    //Add new Employee
    fun addEmployee(empId: Int, empFirstName: String, empLastName: String, empRole: String, reporting: String) : Boolean{
        if (employeeExists(empId)) {
            return false    //Employee already exists
        }
        employeeList.add(EmployeeData(empId, empFirstName, empLastName, empRole, reporting))
        return true
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

    fun employeeExists(empId: Int): Boolean {
        return employeeList.any { it.id == empId }
    }

    fun getEmployee(empId: Int): EmployeeData? {   //Return Employee object
        return employeeList.find { it.id == empId }
    }
}

class AttendanceService(private val employeeService: EmployeeService) {
    private val checkInList = mutableListOf<AttendanceData>()

    fun checkIn(empId: Int, checkInDateTime: LocalDateTime): Boolean {
        if (!employeeService.employeeExists(empId)){
            return false      //Employee not found with the given id
        }
        if (!validateCheckIn(empId, checkInDateTime)) {
            return false     //Check whether the employee has already checked in today or not
        }

        checkInList.add(AttendanceData(empId, checkInDateTime,null,0))
        return true
    }

    fun checkOut(empId: Int,checkOutDateTime: LocalDateTime):String?{
        if (!employeeService.employeeExists(empId)){
            return null      //Employee not found with the given id
        }
        val attendance: AttendanceData?= validateCheckOut(empId,checkOutDateTime)
        if(attendance== null){
            return null     //Invalid check-out
        }

        val formatter= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

        val totHrs= getHrsBetween(attendance.checkInDateTime,checkOutDateTime)
        attendance.checkOutDateTime=checkOutDateTime
        attendance.totHrs=totHrs
        return "EmpId: $empId CheckInTime: ${attendance.checkInDateTime.format(formatter)} " +
                "CheckOutTime: ${checkOutDateTime.format(formatter)} totHrs: $totHrs"

    }


    fun validateCheckIn(empId: Int, inputDateTime: LocalDateTime): Boolean {
        val hasCheckedIn = checkInList.any {         //check whether already checked in today or not
            it.id == empId && it.checkInDateTime.toLocalDate() == inputDateTime.toLocalDate()
        }
        return !hasCheckedIn    //If already checked in means invalid check-in
    }

    fun validateCheckOut(empId: Int, checkOutDateTime: LocalDateTime): AttendanceData? {
        val attendance:AttendanceData? =checkInList.find {
                    it.id == empId &&
                    it.checkInDateTime.toLocalDate() == checkOutDateTime.toLocalDate() &&
                    it.checkOutDateTime == null
        }
        if(attendance==null || attendance.checkInDateTime.isAfter(checkOutDateTime)) return null
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
                    checkInStrings.add("Total Hrs     : ${attendance.totHrs}")
                    checkInStrings.add("")
                }
            }
        }
        return checkInStrings
    }

    fun getHrsBetween(checkIn: LocalDateTime, checkOut: LocalDateTime): Long {
        val duration = java.time.Duration.between(checkIn, checkOut)
        val hours = duration.toHours()
        return hours
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
    println("Enter empId: ")
    val empId=readln().toIntOrNull()
    if(empId == null) {
        println("Invalid empId")
        return
    }
    println("First Name: ")
    val empFirstName=readln()
    println("Last Name: ")
    val empLastName=readln()
    println("Role: ")
    val empRole=readln()
    println("Reporting To: ")
    val reportingTo=readln()
    if(employeeService.addEmployee(empId,empFirstName,empLastName,empRole,reportingTo)){
        println("Employee added successfully")
        println("EmpId: $empId Name: $empFirstName $empLastName")
    }
    else{
        println("Employee already exists with EmpId : $empId")
    }
}

fun checkIn(attendanceService:AttendanceService,employeeService:EmployeeService){
    print("Enter Employee ID: ")
    val empId = readln().toIntOrNull()
    if (empId == null) {
        println("Invalid ID")
        return
    }

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
    val empId = readln().toIntOrNull()
    if (empId == null) {
        println("Invalid ID")
        return
    }

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
