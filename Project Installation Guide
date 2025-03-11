Project installation guide:-

download zip and extract
open in suitable editor -> netbeans/eclipse

Main file to see is application.properties in that edit according to ur email and password, following is sample:-

spring.datasource.url=jdbc:mysql://${RDS_HOSTNAME:localhost}:${RDS_PORT:3306}/${RDS_DB_NAME:marketmatrix}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=${RDS_USERNAME:root}
spring.datasource.password=${RDS_PASSWORD:}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# for email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=ur-email
spring.mail.password=ur-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# if u want to host on aws
aws.access.key=ur-access-key
aws.secret.key=ur-secret key
aws.region=ur-region
aws.s3.bucket.banners=s3-bucket name (from aws u can create bucket )
aws.s3.bucket.products=s3-bucket-name

server.port=5000

Note: if you dont want to deploy on aws remove that part from application.properties file and set product image as below:-
<img th:src="@{/product/{imageName}(imageName=${product.productImage})}" alt="Product Image" class="product-img"> -> for product image showing

Note: Create mysql database and for login(user side) register ur account from site
for Admin login -> copy user data from mysql database and paste it as new row, and add role as ROLE_ADMIN and password same as user because its encrypted
It has two side User and Admin
Admin has only two functionality right now -> add category and add product , its in developing

To see live project deployed on aws: http://marketmatrix.eu-north-1.elasticbeanstalk.com/signin (reload 2-3 times if not working)

