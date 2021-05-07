`Essa é uma RESTFul API que disponibiliza funcionalidades básicas de uma calculadora

Funcionalidades
• REST API que expõe as operações de soma, subtracção, multiplicação e divisão.
• Suporte para dois operandos apenas (a e b, por simplicidade).
• Suporte para arbitraryprecision signed decimal numbers.

Requisitos Não Funcionais:
• Projecto Gradle ou Maven com pelo menos dois módulos — rest e calculator.
• Utilização de Spring Boot 2.2.6 como foundation de ambos os módulos.
• Utilização de RabbitMQ e Spring AMQP para comunicação intermódulo.
• Configuração via application.properties (default do Spring Boot).
• Nenhuma configuração XML (com excepção, eventualmente, da de logging).
• Versionamento do trabalho em Git.

Composição do projecto:

 Projecto Maven multimodular composta por dois modulos:

 •Rest
 •Calculator

Onde  modulo rest é responsalvel por receber as requesicoes HTTP com instruções de cálculo e envia
o mesmo para o módulo calculator através do RabbitMQ e Spring AMQP, e devolve o resultado do cálculo ao cliente HTTP.

Pre-requisitos

Para a operalização do projecto, será necessário uma instância do rabbitmq disponível na porta padrão (5672);


Inicializando os modulos:

1: Entrar no diretório  rest-caculator\calculator\target atraves do terminal 
2: Execuar o comando "java -jar calculator --spring.profiles.active=calculator_exchange,server"
3: Entrar no diretório  rest-caculator\rest\target atraves do terminal 
4: Execuar o comando "java -jar rest --spring.profiles.active=calculator_exchange,client"

Executando as operações:

O modulo rest fica dispnível na porta 8080 e suprta requesições através do método GET:
•Adição:          http://localhost/add/a/b
•Subtração:       http://localhost/sub/a/b
•Divisão:         http://localhost/divide/a/b
•Multiplicação:   http://localhost/multyply/a/b

(onde "a" é  primeiro operador e "b" o segundo)


