 A Giter8 template for CRUD application with MySQL in Lagom framework.

 ## Lagom with MySql CRUD application

 ### Clone Project and Run
 
 `sbt new knoldus/lagom-scala-mysql-es.g8`
 
 `cd lagom-scala-mysql-es`
 
 To start the Application first we need to make sure we are exporting SQL username and password correctly.
 
 `export USERNAME=<SQL_USERNAME>`

 `export PASSWORD=<SQL_PASSWORD>`
 
 ### Start MySQL Server:
 `mysql -u root -p`
 
 Remember to create a Database `userdb` in MySQL .
 If you want to update the database name then, change value in application.conf `{db.default.url}`
 
 ### Run Application
 `sbt clean compile runAll` 

 ### Sample Service Requests:
 * ##### ADD USER  
 **POST** `http://localhost:9000/user/add`
 
 ````
 {
   	"orgId":1,
   	"email":"charmy@gmail.com",
   	"name":"Charmy"
 }
 ````
 
* ##### GET USER  
  **GET** `http://localhost:9000/user/get?orgId=1`
  
  
* ##### UPDATE USER  
  **PUT** `http://localhost:9000/user/update`
  
  ````
  {
  	"orgId":1,
  	"name":"Charmy Garg"
  }
  ````
* ##### DELETE USER  
  **DELETE** `http://localhost:9000/user/delete?orgId=1`
  
Hope this template comes in handy and you find it useful. Please reach out to me at - charmy.garg@knoldus.in in case of any queries.

Template license
----------------
Written in 2019 by Charmy Garg

To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this template to the public domain worldwide. This template is distributed without any warranty. See <http://creativecommons.org/publicdomain/zero/1.0/>.

