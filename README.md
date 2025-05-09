# Domus Back-End Developer Challenge by Javier Gianneo

## Description 

This is implementation of the given challenge given by the next especification:
https://bitbucket.org/iugosds/challenge-domus/src/master/

In this challenge, the REST API contains information about a collection of movies released after the year 2010, directed by acclaimed directors. Given a threshold value, your task is to use the API to retrieve a list of directors who have directed more movies than the specified threshold. Specifically, the API should return the names of directors whose movie count is strictly greater than the given threshold, sorted alphabetically.
To access the movie collection, perform an HTTP GET request to the following endpoint:

##  Task

Fork the provided repository and implement a REST API endpoint using the provided template:

```
/api/directors?threshold=X
```

This endpoint must return a JSON object containing the names of directors whose number of movies directed is strictly greater than the given threshold.

The names should be returned in alphabetical order.

## Solution

The solution was implemented using WebFlux
I implementend an algorithm that fetch by movies by batches and joins them in a single response for processing.


### Advantages
The objective was to provide a solution without depending on the number of pages or the total amount of movies.

### Disvantages
If the number of movies is too large, it will fetch them all. A possible solution is store the fetched movies in a cache or set number of total number of movies we want to proccess.

## To improve
*	Unit test are missing at service layer
*	Could be implemented custom exceptions
