how to build?
-------------

    $ git clone "https://github.com/Sammers21/BatServer"
    $ cd BatServer
    $ mvn clean install
    $ mvn spring-boot:run
    

INFO
===========
Google Play: https://play.google.com/store/apps/details?id=com.danielkashin.batyamessagingapp

Server API specification - http://batya-api.readthedocs.io/en/latest/
   
available methods
=================
    


1/login

2/register 

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

3/{auth_token}/logout && /{auth_token}/logoutall

4/{auth_token}/contacts/[offset/{offset}]

5/{auth_token}/messages/{dialog_id}/limit/{limit}/skip/{offset}

6/{auth_token}/messages/{dialog_id}/after/{timestamp}

7/{auth_token}/messages/send/{dialog_id}

8 GET /:auth_token/name/:dialog_id

9 POST /:auth_token/name[/:dialog_id]

10 POST /:auth_token/conferences/create

11 POST /:auth_token/conferences/:conference_id/invite/:user_id

12 POST /:auth_token/conferences/:conference_id/kick/:user_id

13 POST /:auth_token/conferences/:conference_id/leave

14 GET /:auth_token/conferences/:conference_id/user_list
    
15 GET /:auth_token/search_users/:search_request
   
16  GET /:auth_token/avatar/:dialog_id

17 POST /:auth_token/avatar[/:conference_id]

18  POST /:auth_token

=================

server specification - http://batya-api.readthedocs.io/en/latest/
