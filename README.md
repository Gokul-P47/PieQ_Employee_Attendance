#  Employee Check-In(Kotlin)

  This is a command-line project built using Kotlin to manage employee check-ins. It allows adding employees, recording attendance, and viewing check-in records in a clean and structured format.

## 1.EmployeeData class
  stores Employee details such as
  <pre>
    *Employee ID
    *Name
    *Role
    *reporting to 
 </pre>
## 2.Attendance class
  Stores Employee Attendance details
  <pre>
    *Id
    *Check in Date and Time
    *Check out Date and Time
    *Working hours
  </pre>
    
## 3.EmployeeService class
 Handles everything related to employee data.
  ### Employee List 
  Stores all the Employee objects. 

  ### createEmployee()
  Adds a new employee to the Employee List.

  ### employeeExists(empId)
  Checks if an employee ID already exists.
    
  ### getEmployee(empId)
  Retrieves a specific employee’s details.

  ### getEmployeeList()
  Returns a list of employee details as strings.

## 4.AttendanceService Class
  Manages employee check-ins and attendance validation.
  ### CheckInList 
  Storeas all the check-in activities

  ### checkIn(empId, dateTime)
  Registers a check-in for a valid employee at the given date and time. Prevents duplicate check-ins for the same day.

  ### validateCheckIn(empId, dateTime)
  Checks whether the employee has already checked in on that date.
 
  ### getCheckInList()
   Returns a list of check-in records as strings.
    
