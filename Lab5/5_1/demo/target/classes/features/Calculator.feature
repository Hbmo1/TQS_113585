Feature: Simple Arithmetic

Background: A Calculator
  Given a calculator I just turned on

Scenario: Adding two numbers together
  When I add 2 and 5
  Then the result should be 7

Scenario: Subtracting one number from another
  When I subtract 10 by 2
  Then the result should be 8

Scenario Outline: Adding numbers <a> and <b> together
  When I add <a> and <b>
  Then the result should be <c>

Examples:
| a | b | c  |
| 9 | 5 | 14 |
| 7 | 4 | 11 |

Scenario Outline: Multiply two numbers
  When I multiply <a> by <b>
  Then the result should be <d>

Examples:
| a | b | d  |
| 9 | 5 | 45 |
| 7 | 4 | 28 |

Scenario Outline: Using invalid operator
  When I try to use the operator "<operator>"
  Then an exception should be thrown

Examples:
| operator |
| %        |
| :        |
