# TQS_113585

Individual portfolio for the Teste e Qualidade de Software class

## Useful info

Sample junit-maven integration repo in the folder above
"True unit tests typically should not rely on the order in which they are executed" - Isto obriga-nos a inicializar a classe a ser testada em cada teste QUE REQUER estados ESPECIFICOS da classe.
Para isso usar @BeforeEach na batch de testes. O @BeforeEach garante a execução do código aí escrito ANTES de qualquer outro test. Ou seja, podemos inicializar a classe a ser testada no @BeforeEach, dessa forma garantimos que cada teste é executado sobre uma instancia da classe nova.
Pela mesma lógica, podemos usar @AfterEach para garantir a execução de um pedaço de código (por exemplo libertar memória) é executado no final de cada teste.
Test for true, test fails on first false assertion.

TODO -> Ir buscar o plugin jacoco para automatizar os testes

Generate mvn coverage report:
1. mvn clean verify

Check coverage html with jacoco:
1. cd /home/henrique/Documents/UA/3ºAno/2ºSemestre/TQS/TQS_113585/Lab1/Lab1_1/target/site/jacoco
2. xdg-open index.html