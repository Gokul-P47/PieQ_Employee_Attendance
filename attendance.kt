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

class EmployeeService {
    val employeeList = mutableListOf<Employee>()

    init {
        addDummyEmployees()
    }

    fun addDummyEmployees() {
        employeeList.addAll(
            listOf(
                Employee(101, "Gokul", "P", "Developer", "Bob John"),
                Employee(102, "Mark", "Lee", "Developer", "Bob John"),
                Employee(103, "Mark", "Lee", "Tester", "Bob John"),
                Employee(104, "Ashok", "Kumar", "Designer", "Bob John"),
                Employee(105, "Bob", "John", "Manager", "CEO")
            )
        )
    }

    fun addEmployee(empId: Int, empFirstName: String, empLastName: String, empRole: String, reporting: String) {
        if (employeeExists(empId)) {
            println("Employee already exists with $empId")
            return
        }
        employeeList.add(Employee(empId, empFirstName, empLastName, empRole, reporting))
    }


    fun showEmployeeList(){
        println("----- Employees List -----")
        if (employeeList.isEmpty()) {
            println("No Employees added yet.")
            return
        }

        for (employee in employeeList) {
            println("ID           : ${employee.id}")
            println("Name         : ${employee.firstName} ${employee.lastName}")
            println("Role         : ${employee.role}")
            println("Reporting To : ${employee.reportingTo}\n")
        }
    }
    fun employeeExists(empId: Int): Boolean {
        return employeeList.any { it.id == empId }
    }

    fun getEmployee(empId: Int): Employee? {
        return employeeList.find { it.id == empId }
    }
}

class AttendanceService(private val employeeService: EmployeeService) {
    private val checkInList = mutableListOf<Attendance>()

    fun checkIn(empId: Int) {
        if (!employeeService.employeeExists(empId)) {
            println("Employee ID not found.")
            return
        }

        val inputDateTime = getDateTimeFromUserOrNow()
        if (inputDateTime != null) {
            if (validateCheckIn(empId, inputDateTime)) {
                checkInList.add(Attendance(empId, inputDateTime))
                val employee = employeeService.getEmployee(empId)
                println("Check-in Successful!")
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                println("ID: $empId | Name: ${employee?.firstName} ${employee?.lastName} | DateTime: ${inputDateTime.format(formatter)}")
            }
        }
    }

    fun validateCheckIn(empId: Int, inputDateTime: LocalDateTime): Boolean {
        val hasCheckedIn = checkInList.any {
            it.id == empId && it.checkInDateTime.toLocalDate() == inputDateTime.toLocalDate()
        }
        if (hasCheckedIn) {
            println("Already checked in on ${inputDateTime.toLocalDate()}")
            return false
        }
        return true
    }

    fun printCheckInList() {
        println("----- Check-In List -----")
        if (checkInList.isEmpty()) {
            println("No Check-ins.")
            return
        }

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        for (attendance in checkInList) {
            val employee = employeeService.getEmployee(attendance.id)
            println("ID           : ${employee?.id}")
            println("Name         : ${employee?.firstName} ${employee?.lastName}")
            println("Check-in Time: ${attendance.checkInDateTime.format(formatter)}\n")
        }
    }

    private fun getDateTimeFromUserOrNow(): LocalDateTime? {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        print("Enter date and time (dd-MM-yyyy HH:mm) or press Enter to use current: ")
        val input = readln().trim()

        return if (input.isEmpty()) {
            LocalDateTime.now()
        } else {
            try {
                val dateTime = LocalDateTime.parse(input, formatter)
                if (dateTime.isAfter(LocalDateTime.now())) {
                    println("Future date-time not allowed.")
                    null
                } else dateTime
            } catch (e: DateTimeException) {
                println("Invalid format!")
                null
            }
        }
    }
}
fun main() {
    val employeeService = EmployeeService()
    val attendanceService = AttendanceService(employeeService)

    while (true) {
        println("\n1.Add Employee\t2.Check In\t3.Show Employee List\t4.Show Check-In List\t5.Exit")
        print("Choose an option: ")
        when (readln().toIntOrNull()) {
            1->{
                println("Enter id: ")
                val empId=readln().toIntOrNull()
                if(empId == null) {
                    println("Invalid id")
                    continue
                }
                println("First Name: ")
                val empFirstName=readln()
                println("Last Name: ")
                val empLastName=readln()
                println("Role: ")
                val empRole=readln()
                println("Reporting To: ")
                val reportingTo=readln()
                employeeService.addEmployee(empId,empFirstName,empLastName,empRole,reportingTo)
            }
            2 -> {
                print("Enter Your Employee ID: ")
                val empId = readln().toIntOrNull()
                if (empId != null) {
                    attendanceService.checkIn(empId)
                } else {
                    println("Invalid ID input")
                }
            }
            3->{
                employeeService.showEmployeeList()
            }

            4 -> {
                attendanceService.printCheckInList()
            }

            5 -> {
                break
            }
            else -> println("Invalid option")
        }
    }
}
