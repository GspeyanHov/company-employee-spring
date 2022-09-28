package am.solidoGroup.companyemployeespring.repository;

import am.solidoGroup.companyemployeespring.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
