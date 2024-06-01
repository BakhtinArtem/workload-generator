# Running the application

I have encountered some errors while trying to run latest version of gradle in `Inteleji`.
The more robust solution is to use `gradle cli`. 
The example of command that build and executes program
```bash
./gradlew build | ./gradlew run --args="--url https://httpbin.org/anything --vt --mode throughput"
```