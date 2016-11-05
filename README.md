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

4./{auth_token}/logout both of GET and POST 

5./{auth_token}/logoutall POST both of GET and POST 

6./{auth_token}/messages/send/{dialog_id} POST

7./{auth_token}/contacts GET

8./{auth_token}/contacts/offset/{offset} GET

9./{auth_token}/messages/{dialog_id} GET

10./{auth_token}/messages/{dialog_id}/limit/{limit} GET

11./{auth_token}/messages/{dialog_id}/limit/{limit}/skip/{offset} GET

12./{auth_token}/messages/{dialog_id}/after/{timestamp} GET

13./after/{timestamp}/limit/{limit} GET

    
    

   

=================

server specification - http://batya-api.readthedocs.io/en/latest/
