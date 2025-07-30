#  Employee Check-In(Kotlin)

  This is a command-line project built using Kotlin to manage employee check-ins. It allows adding employees, recording attendance, and viewing check-in records in a clean and structured format.

## 1.EmployeeData class
  To store Employee Details such as
  *
1. EmployeeService
Handles everything related to employee data.

createEmployee()
Adds a new employee to the system.

employeeExists(empId)
Checks if an employee ID already exists.

getEmployee(empId)
Retrieves a specific employee’s details.

getEmployeeList()
Returns a list of formatted employee details as strings.

2. AttendanceService
Manages employee check-ins and attendance validation.

checkIn(empId, dateTime)
Registers a check-in for a valid employee at the given date and time. Prevents duplicate check-ins for the same day.

validateCheckIn(empId, dateTime)
Checks whether the employee has already checked in on that date.

getCheckInList()
Returns a list of formatted check-in records as strings.

getFormattedCheckInDetails(empId, dateTime)
Returns a formatted string containing the employee's name and check-in timestamp for display.

   
