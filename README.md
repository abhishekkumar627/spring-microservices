# spring-microservices
spring-microservices

1. Start zipkin/sleuth in docker container first.
2. Start redis in docker container.
3. Start ServiceRegistryApplication first
4. Start configServerapplication
5. Start ORDER,PAYMENT,PRODUCT service
6. Start CloudGatewayApplication

# Docker Commands

Example 

Redis image
------------
1. To pull image of redis 

`docker pull redis`

2. To check all docker images

`docker images`

3. How to run this image

`docker run --name redis-latest imageNAME:LATEST: imageId`

`example :  run --name redis-latest redis:latest`

4. To check list of containers available

`docker ps`

5. For stopping container

`docker stop container-id`

6. To see the containers available

`docker ps -a`

7. To start container again

`docker start container-id/container-name`

8. To remove the container

`docket stop container-id`

`docker ps -a`

`docker rm container-id`

Docker Image will still be there 

9. To expose the host port with new container.

example :  `docker run --name redis-latest -p6379:6379 -d redis:latest`

`-p6379:6379 to access redis via host port`

`-p6379 host port`

`: 6379 container post`

`-d to run redis in detached mode.`

`docker ps`

10. To work with different versions of redis with different images

`docker pull redis' -- takes latest`

`docker pull redis:6.2.7'-- particular version will be fetched.`

11. To run this version specific redis 6.2.7

`docker run --name redis-old -p6378:6379 -d redis:6.2.7`

`docker ps` -- To check running containers

12. To get to know about more commands

`docker --help`

`docker run --help`

