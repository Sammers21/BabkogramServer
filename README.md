how to build?
-------------

    $ git clone "https://github.com/Sammers21/BatServer"
    $ cd BatServer
    $ mvn clean package
    $ mvn spring-boot:run
    
   
available methods
=================
1. /greetings
2. /register

POST request with body


    {
        username : {usernamevalue}
        password : {passwordvalue}
    }
    

    
response(tokenvalue is alredy "kek"(in current version(i m so lazy(see you at morning))))
 
    {
       token : {tokenvalue}      
    }

    
    

   

=================

server specification - https://github.com/frnkySila/batya-api/blob/master/docs/source/index.rst
