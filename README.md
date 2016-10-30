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
3. /login
POST request with body fro both of login and register


    {
        username : {usernamevalue}
        password : {passwordvalue}
    }
    

    
response
 
    {
       token : {tokenvalue}      
    }
    or
    {
       error : "error message"      
    }

    
    

   

=================

server specification - https://github.com/frnkySila/batya-api/blob/master/docs/source/index.rst
