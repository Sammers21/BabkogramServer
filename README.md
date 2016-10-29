how to build?
-------------


    $ mvn clean package
    $ cd target
    $ java -jar gs-rest-service-0.1.0.jar
    
   
available methods
=================
1. /greetings
2. /register

POST request with body


    {
        username : {usernamevalue}
        password : {passwordvalue}
    }
    

    
response(tokenvalue в данной версии всегда "kek")
 
    {
       token : {tokenvalue}      
    }

    
    

   

=================

server specification - https://github.com/frnkySila/batya-api/blob/master/docs/source/index.rst
