
Truststore cacerts pass thang06112000

Set up env

1. Change config sql username password in application.properties

2. Create database name: banking_system

3. Create schema name: banking_system

4. Run project to create table (ddl auto: update)

5. Import Postman (file: MoneyTransferTest.postman_collection.json)

    5.1. Run all create instance of each object

    5.2. Setup enviroment parameter with id of each object created
    
    5.3. Run postman to test each case
         
         5.3.1 Create transaction 
               ROLE_ADMIN: can create any transaction
               ROLE_USER: create transaction which from_account_id must equal to credential account id
         5.3.2 Find account
               ROLE_ADMIN: can call all api find account
               ROLE_USER: unauthorized all api
         5.3.2 Find transaction
               ROLE_ADMIN: can call all api find transaction
               ROLE_USER: only find transaction belong to account have account id = credential account id
                     3 api can call
                     http://localhost:8088/account/{{Account 1 Id (User 1)}}/transactions?page=0&size=10
                     http://localhost:8088/account/{{Account 1 Id (User 1)}}/transactions/scroll
                     http://localhost:8088/account/{{Account 1 Id (User 1)}}/getTransactionTransfer

6. Run test

   6.1 Service package (UT, mock + stub to cover source service package)

   6.2 Business package (IT, cover source controller-service-repository package)