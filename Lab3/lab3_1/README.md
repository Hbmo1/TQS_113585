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

