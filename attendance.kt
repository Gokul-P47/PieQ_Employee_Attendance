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
    val employeeList = mutableListOf<Employee>() //To store employees

    init {
        addInitialEmployees()
    }

    fun addInitialEmployees() {
        employeeList.addAll(
            listOf(
                Employee(101, "Gokul", "P", "Developer", "Bob John"),
                Employee(102, "Mark", "Lee", "Developer", "Bob John"),
                Employee(103, "Jack", "Lee", "Tester", "Bob John"),
                Employee(104, "Ashok", "Kumar", "Designer", "Bob John"),
                Employee(105, "Bob", "John", "Manager", "CEO")
            )
        )
    }

    //Add new Employee
    fun addEmployee(empId: Int, empFirstName: String, empLastName: String, empRole: String, reporting: String) : Boolean{
        if (employeeExists(empId)) {
            return false    //Employee already exists
        }
        employeeList.add(Employee(empId, empFirstName, empLastName, empRole, reporting))
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

    fun getEmployee(empId: Int): Employee? {   //Return Employee object
        return employeeList.find { it.id == empId }
    }
}

class AttendanceService(private val employeeService: EmployeeService) {
    private val checkInList = mutableListOf<Attendance>()

    fun checkIn(empId: Int, inputDateTime: LocalDateTime): Boolean {
        if (!employeeService.employeeExists(empId)){
                return false      //Employee not found with the given id
        }
        if (!validateCheckIn(empId, inputDateTime)) {
            return false     //Check whether the employee has already checked in today or not
        }

        checkInList.add(Attendance(empId, inputDateTime))
        return true
    }


    fun validateCheckIn(empId: Int, inputDateTime: LocalDateTime): Boolean {
        val hasCheckedIn = checkInList.any {         //check whether already checked in today or not
            it.id == empId && it.checkInDateTime.toLocalDate() == inputDateTime.toLocalDate()
        }
        return !hasCheckedIn    //If already checked in means invalid check-in
    }

    fun getCheckInList(): List<String> {
        val checkInStrings = mutableListOf<String>()

        if (!checkInList.isEmpty()) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

            for (attendance in checkInList) {
                val employee = employeeService.getEmployee(attendance.id)
                if (employee != null) {
                    checkInStrings.add("empID           : ${employee.id}")
                    checkInStrings.add("Name         : ${employee.firstName} ${employee.lastName}")
                    checkInStrings.add("Check-in Time: ${attendance.checkInDateTime.format(formatter)}")
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
        println("\n1.Add Employee\t2.Check In\t3.Get Employees List\t4.Get Check-In List\t5.Exit")
        print("Choose an option: ")
        when (readln().toIntOrNull()) {
            1->{
                println("Enter empId: ")
                val empId=readln().toIntOrNull()
                if(empId == null) {
                    println("Invalid empId")
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
                if(employeeService.addEmployee(empId,empFirstName,empLastName,empRole,reportingTo)){
                    println("Employee added successfully")
                    println("EmpId: $empId Name: $empFirstName $empLastName")
                }
                else{
                    println("Employee already exists with EmpId : $empId")
                }
            }
            2 -> {
                print("Enter Employee ID: ")
                val empId = readln().toIntOrNull()
                if (empId == null) {
                    println("Invalid ID")
                    continue
                }

                val inputDateTime = getDateTimeFromUserOrNow()
                if (inputDateTime == null) { 
                    println("Invalid dateTime")
                    continue
                }

                val checkInStatus = attendanceService.checkIn(empId, inputDateTime)
                if (checkInStatus) {
                    val employee: Employee?= employeeService.getEmployee(empId)
                    val formatter= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                    println("Check-in Successful! Employee Id: $empId Name: ${employee?.firstName} ${employee?.lastName} DateTime: ${inputDateTime.format(formatter)}")
                } else {
                    println("Check-in Failed.") //Either user already checked-in or invalid user id
                }
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
                break
            }
            else -> println("Invalid option")
        }
    }
}

fun getDateTimeFromUserOrNow(): LocalDateTime? {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    print("Enter date and time (dd-MM-yyyy HH:mm) or press Enter to use current: ")
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

