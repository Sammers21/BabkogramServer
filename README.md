how to build?
-------------

    $ git clone "https://github.com/Sammers21/BatServer"
    $ cd BatServer
    $ mvn clean package
    $ mvn spring-boot:run
    
   
available methods
=================
1./greetings
    
2./register 

3./login

POST request with body for both of login and register


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

4./{auth_token}/logout POST

5./{auth_token}/logoutall POST

6./{auth_token}/messages/send/{dialog_id} POST

    
    

   

=================

server specification - http://batya-api.readthedocs.io/en/latest/
