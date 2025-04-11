## GamesLeague TestOperation system

The files in this directory are provided so you can verify your code is compatible with the coursework specifications (and therefore the grading script which will award marks for operation).

Further tests will be added in due course - keep an eye on the announcements forum to find out when updates are available.

To run the test scripts:

1. Make sure the class binaries for your `GamesLeague` package is all up to date (remember to repeat each time you make a change to files in your `GamesLeague` directory.)

```sh
javac -d bin -cp bin ./src/gamesleague/*.java
```

2. Build the TestOperation programs

```sh
javac -cp bin ./TestOperation/*.java 
```

**NOTE** There are a couple of typos/bugs in the exception files you will need to fix before the files will compile. In the following files please add a qualifier `public` to the classes (as can be seen in the other exceptions). Remember to recompile the `gamesleague` binaries once you have made this change!

```
# files to fix:
./src/gamesleague/IDInvalidException.java
./src/gamesleague/IllegalEmailException.java
```
   

3. Run the TestOperationApp to run through all tests. 

   Look in the files in the TestOperation folder and check the output of your code is in line with that expected.


```sh
java -cp bin:TestOperation TestOperationApp
```
