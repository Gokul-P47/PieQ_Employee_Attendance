# PieQ_Employee_Attendance


1.Create a data class and store employee details with :
        *id
        *first & last name
        *Role

2.Store list of employee object  

3.Create check-in list for validation

4.create validate function with:
      parameters(id,values of checkinlist)
      conditions : 
                    if given id matches the available employee's id
                           {
                          if check whether the current id not in checkinlist
                               {    
                                          checkin occurs and print the employee details 
                                          and add employee in checkin list with id and date & time
                                   }else
                                   {
                                          employee already checked in
                                   }

                  Else invalid check-in               
