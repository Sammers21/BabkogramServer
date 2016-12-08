how to build?
-------------

    $ git clone "https://github.com/Sammers21/BatServer"
    $ cd BatServer
    $ mvn clean install
    $ mvn spring-boot:run
    
   
available methods
=================
    


2.1/login

2.2/register 

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

2.3/{auth_token}/logout && /{auth_token}/logoutall

2.4/{auth_token}/contacts/[offset/{offset}]

2.5/{auth_token}/messages/{dialog_id}/limit/{limit}/skip/{offset}

2.6/{auth_token}/messages/{dialog_id}/after/{timestamp}

2.7/{auth_token}/messages/send/{dialog_id}


7./{auth_token}/contacts GET


9./{auth_token}/messages/{dialog_id} GET

10./{auth_token}/messages/{dialog_id}/limit/{limit} GET

11./{auth_token}/messages/{dialog_id}/limit/{limit}/skip/{offset}

12./{auth_token}/messages/{dialog_id}/after/{timestamp} GET

13./after/{timestamp}/limit/{limit} GET

    
    

   

=================

server specification - http://batya-api.readthedocs.io/en/latest/
