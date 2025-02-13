# TQS_113585

Individual portfolio for the Teste e Qualidade de Software class

## Useful info

Sample junit-maven integration repo in the folder above
"True unit tests typically should not rely on the order in which they are executed" - Isto obriga-nos a inicializar a classe a ser testada em cada teste QUE REQUER estados ESPECIFICOS da classe.
Para isso usar @BeforeEach na batch de testes. O @BeforeEach garante a execução do código aí escrito ANTES de qualquer outro test. Ou seja, podemos inicializar a classe a ser testada no @BeforeEach, dessa forma garantimos que cada teste é executado sobre uma instancia da classe nova.
Pela mesma lógica, podemos usar @AfterEach para garantir a execução de um pedaço de código (por exemplo libertar memória) é executado no final de cada teste.

TODO -> Ir buscar o plugin jacoco para automatizar os testes
