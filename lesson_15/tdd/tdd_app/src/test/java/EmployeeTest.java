import static org.assertj.core.api.Assertions.assertThat;

import com.codedifferently.lesson15.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeTest {

  Employee employee;

  @BeforeEach
  void setUp() {
    employee = new Employee(1, "Alice", "Engineering", 75000.0);
  }

  @Test
  void testGetId() {
    assertThat(employee.getId()).isEqualTo(1);
  }

  @Test
  void testSetId() {
    employee.setId(2);
    assertThat(employee.getId()).isEqualTo(2);
  }

  @Test
  void testGetName() {
    assertThat(employee.getName()).isEqualTo("Alice");
  }

  @Test
  void testSetName() {
    employee.setName("Bob");
    assertThat(employee.getName()).isEqualTo("Bob");
  }

  @Test
  void testGetDepartment() {
    assertThat(employee.getDepartment()).isEqualTo("Engineering");
  }

  @Test
  void testSetDepartment() {
    employee.setDepartment("HR");
    assertThat(employee.getDepartment()).isEqualTo("HR");
  }

  @Test
  void testGetSalary() {
    assertThat(employee.getSalary()).isEqualTo(75000.0);
  }

  @Test
  void testSetSalary() {
    employee.setSalary(80000.0);
    assertThat(employee.getSalary()).isEqualTo(80000.0);
  }

  @Test
  void testGetDetails() {
    String expected = "ID: 1, Name: Alice, Department: Engineering, Salary: 75000.0";
    assertThat(employee.getDetails()).isEqualTo(expected);
  }
}
