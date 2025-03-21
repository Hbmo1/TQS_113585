# Questions

## A) Couple of examples that use AssertJ expressive methods chaining.

assertThat(allEmployees).hasSize(3).extracting(Employee::getName).containsOnly(alex.getName(), ron.getName(), bob.getName());
assertThat(results)
        .hasSize(2)
        .extracting(Employee::getEmail)
        .containsExactlyInAnyOrder(
                "bob@ua.pt",
                "ron@ua.pt"
        );

## B) Take note of transitive annotations included in @DataJpaTest.

org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration -> Caching support
org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration ->  Configures Spring Data JPA repositories automatically
org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration -> Enables Flyway for database migrations
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration -> Configures the DataSource (db connection)
org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration -> Sets up transaction management for the DataSource
org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration -> Configures JdbcTemplate to simplify raw SQL queries
org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration -> Enables Liquibase for database migrations (if Liquibase is on the classpath)
org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration -> Configures Hibernate as the JPA provider
org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration -> Enables transaction management for JPA operations
org.springframework.boot.test.autoconfigure.orm.jpa.TestDatabaseAutoConfiguration -> Sets up an embedded database for testing unless a real database is provided
org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManagerAutoConfiguration -> Provides a TestEntityManager for easier JPA testing

## C) Identify an example in which you mock the behavior of the repository (and avoid involving a database).

The test file "B_EmployeeService_UnitTest.java" has a mocked repository, since it focuses on simple unit tests, that do not require a connection to the db. An example usage of the mocked repo appears in the setUp() function:
@BeforeEach
    public void setUp() {

        //these expectations provide an alternative to the use of the repository
        Employee john = new Employee("john", "john@deti.com");
        john.setId(111L);

        Employee bob = new Employee("bob", "bob@deti.com");
        Employee alex = new Employee("alex", "alex@deti.com");

        List<Employee> allEmployees = Arrays.asList(john, bob, alex);

        Mockito.when(employeeRepository.findByName(john.getName())).thenReturn(john);
        Mockito.when(employeeRepository.findByName(alex.getName())).thenReturn(alex);
        Mockito.when(employeeRepository.findByName("wrong_name")).thenReturn(null);
        Mockito.when(employeeRepository.findById(john.getId())).thenReturn(Optional.of(john));
        Mockito.when(employeeRepository.findAll()).thenReturn(allEmployees);
        Mockito.when(employeeRepository.findById(-99L)).thenReturn(Optional.empty());
    }


## D) What is the difference between standard @Mock and @MockBean?

@Mock comes from Mockito, used to create plain mocks and is not managed by Spring. It only works inside the test classes where it is declred and can be used along with unit tests
@MockBean comes from Sprint Boot, used to create a Spring-managed mock and replaces the corresponding Bean in the Spring context. Used for integration tests with annotations such as @SpringBootTest, @WebMvcTest, or @DataJpaTest.

## E) What is the role of the file “application-integrationtest.properties”? In which conditions will it be used?

It contains the database configuration needed for the integration tests, only used during those tests and overriding the normal application.properties file.  

## F) The sample project demonstrates three test strategies to assess an API (C, D and E) developed with SpringBoot. Which are the main/key differences?

C is sa sliced approach, only covering Controller tests, mocking the service layer and using MockMvc to simulate HTTP requests and responses.
D is a full scope approach, lodaing the full Spring Boot application, including the service and db logic that was mocked in C. However, D still tests the API endpoints with a mocked Mvc and not the real API.
E is a full scope approach, almost identical to D, except it sends HTTP requests to a real API, instead of a mocked version.