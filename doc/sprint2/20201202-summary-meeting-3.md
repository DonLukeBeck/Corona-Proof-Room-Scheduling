# Summary meeting 3 (02-12-2020)
Secretary: Timen Zandbergen

## Meeting
We decided to extend sprint deadline by one day because we hadn't had enough
time to connect all the microservices together, which is supposed to be
finished by the end of the sprint.

Mathijs and Alex worked on the scheduling algorithm to make it compliant with
the requirements. They also refactored the code to make the entities compatible
with the other microservices and the database.

Merdan: worked on course management microservice. refactored the connection to
the database

Can: changed names of microservices and connected them to databases. started
implementing the rooms and restrictions services. now have to fix communication
between the services

Luca: implemented JWT token for the identity service.

The response for wrong password should be unauthorized, not forbidden.
All responsed should be JSON

Most of us encountered problems with spring annotations that required a lot of
time to fix.

## Feedback
* good merge requests, keep it up
* branch naming is inconsistent, savest way is to make them from tasks
* board of tasks is good
* docs: upload the html report from JaCoCo
* create folders in docs folder for each sprint
* amount of commits is not equal
* algorithm doesn't need to be perfect
* CI/CD issue is fixed, can disable manual triggering
* We can send video before deadline to get feedback

## For next week
For next week we need to finish the system such that it can work end to end.

