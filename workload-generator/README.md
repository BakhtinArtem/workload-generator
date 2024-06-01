# Overview

This program is a workload generator designed to simulate HTTP requests to a specified URL. 
The configuration of the workload is highly customizable, allowing users to specify various 
parameters such as the mode of operation, the number of threads, the type of threads (virtual or standard),
the HTTP request method, and the total number of requests to be made. 
The program uses an argument parser to set these configurations, ensuring flexibility and ease of use.

Configuration Parameters

- Mode (--m or --mode). 
  - Description: Specifies the mode of operation for the workload generator.
  - Default: oneShot
  - Options:
    - oneShot: Executes a single request. 
    - throughput: Parallel execution of `n` requests.
  - Example: --mode oneShot

- URL (--url):
  - Description: The target URL to which the HTTP requests will be sent. 
  - **Required: Yes**
  - Example: --url http://example.com

- Thread Number (--tn or --threadsNumber):
  - Description: Specifies the number of threads to be used for sending requests.
  - Default: 10
  - Example: --threadsNumber 20

- Virtual Threads (--vt or --virtualThread):
  - Description: Indicates whether to use virtual threads.
  - Default: false
  - Note: Cannot be used simultaneously with --thread.
  - Example: --virtualThread

- Standard Threads (--t or --thread):
  - Description: Indicates whether to use standard threads.
  - Default: false
  - Note: Cannot be used simultaneously with --virtualThread.
  - Example: --thread

- Request Method (--r or --requestMethod):
  - Description: Specifies the HTTP request method to be used. 
  - Default: GET 
  - Options: GET, POST, PUT, DELETE 
  - Example: --requestMethod POST

- Request Number (--requestNumber):
  - Description: The total number of requests to be sent.
  - Default: 2000
  - Example: --requestNumber 5000

# Error Handling

**Duplicated Arguments**: If an argument is provided more than once, the program throws an error.
**Missing Values**: If an expected value for an argument is missing, the program throws an error.
**Invalid Values**: If a provided value is not valid (e.g., non-numeric values for numeric arguments, unsupported request methods), the program throws an error.
**Mutually Exclusive Options**: The program ensures that only one type of thread (virtual or standard) is specified; specifying both will result in an error.

# Running the application

I have encountered some errors while trying to run latest version of gradle in `Inteleji`.
The more robust way to execute program is utilizing `gradle cli`. 
The example of command that build and executes the program:

`./gradlew build | ./gradlew run --args="--url https://httpbin.org/anything --vt --mode throughput"`
