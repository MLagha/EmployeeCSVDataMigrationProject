# Employee CSV Data Migration Project


## Overview:

This project reads data from `.csv` files, parse them, populates objects and adds them to a collection.

To use the project, you can choose to send the data to the SQL table in a single thread or multiple threads. However, as we are using a HashMap to filter and handle the records, each thread is required to wait for the former thread to finish before the next thread can start.

The programme also gives the option of retrieving individual records from the database using the Employee ID number as a Key reference.

<img width="788" alt="Screenshot 2022-08-22 at 2 24 06 am" src="https://user-images.githubusercontent.com/106883160/185823258-a62d0181-9b0a-48be-8dc6-b9fe1c26d71c.png">

<img width="925" alt="Screenshot 2022-08-22 at 2 08 28 am" src="https://user-images.githubusercontent.com/106883160/185823286-fcdc3398-c052-4a4e-a40a-1291f16f0bd0.png">

## Instructions to Run the Programm

1. Before starting the programme, please create your database in advance.

2. You also need to insert the details of your database (url, username and password) to the database.properties file under the resources package.

3. If your file has no corrupted or duplicate records, there is no need to run your file through the HashMap filters as this could impact the efficiency of the programme.
