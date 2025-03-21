Feature: Bookstore interactions

  Scenario: View list of fantasy and fiction books
    When I navigate to "https://cover-bookstore.onrender.com/genre/fantasy&fiction"
    Then I should see a list of books in the genre "Fantasy & Fiction"

  Scenario: View book details
    When I navigate to "https://cover-bookstore.onrender.com/genre/fantasy&fiction"
    And I click on the book titled "Harry Potter and the Sorcerer's Stone"
    Then I should be redirected to the book detail page for "Harry Potter and the Sorcerer's Stone" with the URL "https://cover-bookstore.onrender.com/book/6305e5633a24045018b70d7e"

  Scenario: Searching for a book
    When I navigate to "https://cover-bookstore.onrender.com/genre/fantasy&fiction"
    And I search for "Rick"
    Then 2 books should have been found
      And Book 1 should have the title "The Tower of Nero"
      And Book 2 should have the title "The Lightning Thief"
      And both books should have been written by "Rick Riordan"

  Scenario: Add books to the cart
    When I navigate to "https://cover-bookstore.onrender.com/book/6305e5633a24045018b70d7e"
    And I click on "Add to Cart" for the book titled "Harry Potter and the Sorcerer's Stone"
    And I navigate to "https://cover-bookstore.onrender.com/book/6304d8de4cf5a2ee3139e7e2"
    And I click on "Add to Cart" for the book titled "A Game of Thrones"
    Then the cart should show 2 items

  Scenario: Checkout with invalid credit card
    When I navigate to "https://cover-bookstore.onrender.com/cart"
    And I click on "Checkout"
    And I fill in the credit card information with an invalid card number
    And I click on "Submit"
    Then I should see an error message "Your card number is incomplete."

