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

13. Debugging in docker
    `docker images`
    `docker images -a`

14. All information of image

`docker inpsect imageId`

`docker inpsect imageId`

15. All information of container

`docker ps`

`docker inspect container-id`

16. To see logs of the container

`docker ps`

`docker logs container-id`

17. To go inside the container and run commands

`docker exec -it container-id /bin/sh`

to go inside redis just enter
`redis-cli`
`KEYS *`
`exit` -- to exit the redis and again exit to exit container.

----------------------------------------------------

1. `docker images`
2. `docker ps`
3. To clear everything we need to make sure every container is stopped.
4. docker stop container-1 container-2
5. `docker ps` - To check running containers
6. `docker ps -a` - To check each container in any condition.
7. `docker rm container-id` to remove
8. `docker images` All docker images
9. `docker rmi image-id OR imagename:tagname` to delete docker image
10. To delete all in once
    `docker system prune -a`


To build custom docker image
Create Dockerfile and then build project and then run command

`docker build -t abhishekkumar627/serviceregistry:0.0.1 .`


To run custom docker image
`docker run -d -p8761:8761 --name serviceregistry 28ad2205a045`

To build custom docker image
Create Dockerfile and then build project and then run command to make this version latest as well

`docker build -t abhishekkumar627/configserver:0.0.1 -t abhishekkumar627/configserver:latest .`

To run custom docker image

`docker run -d -p9296:9296 --name configserver a3acd545f428`

To check logs of container for any issue

`docker logs container-id`

If any issue in the container then stop it using
Like in the case of config server not able to connect to localhost and its in docker env.

`docker stop container-id`

and remove it using 

`docker rm container-id`

To resolve docker local env issue we need to pass runtime env variable value to pass host as host.docker.internal so that it can communicate to service-registery in same docker platform.

`docker run -d -p9296:9296 -e EUREKA_SERVER_ADDRESS=http://host.docker.internal:8761/eureka --name configserver a3acd545f428`

==============================

To run Cloudgateway as image
---------------------------------------------------------------
1. First define the variable to replace localhost in application.yaml
2. build the project using `mvn clean install`
3. Build docker image with command
   `docker build -t abhishekkumar627/cloudgateway:latest .`
4. Run the image using command
   `docker run -d -p9090:9090 -e CLOUD_GATEWAY_URL=host.docker.internal -e EUREKA_SERVER_ADDRESS=http://host.docker.internal:8761/eureka --name cloudgateway image-id`---------------------------------------------------------------

To push your image to docker hub repository
---------------------------------------------------------------
1. `docker login -u username -p password  ` to login.
2. To push `docker push abhishekkumar627/cloudgateway:latest`
3. To push `docker push abhishekkumar627/serviceregistry:0.0.1`
4. To push `docker push abhishekkumar627/configserver:latest`

To stop containers 
---------------------------------------------------------------
`docker stop container-id-1 container-id-2 container-id-3`

To clean everything
`docker system prune -a`
`clear`
 
Create images again.
1. `docker build -t abhishekkumar627/cloudgateway:latest .`
2. `docker build -t abhishekkumar627/configserver:0.0.1 -t abhishekkumar627/configserver:latest .`
3. `docker build -t abhishekkumar627/serviceregistry:0.0.1 .`

Create docker-compose.yml file

Run using

`docker-compose -f docker-compose.yml up -d`

Delete containers using command

`docker-compose -f docker-compose.yml down`


To make image using jib plugin

`mvn clean install jib:build`

Make sure to have settings.xml configured which have your usename password and docker hub url to authenticate to push to it.

### KUBERNETES
To check minikube status

`minikube status`

To start the minikube

`minikube start`

To get the namespaces

`kubectl get namespaces`

To get the pod information

`kubectl get pods`

To get the services information

`kubectl get services`

To get the deployment

`kubectl get deployment`

To delete everything in the cluster

`kubectl delete all --all`

To get all the details of pods,services,deployment,namespaces

`kubectl get all`

#### Create Deployment

To get the help regarding any command
`kubectl create --h`


`kubectl create deployment nginx --image=nginx`

To get the detailed info about deployment

`kubectl get all -o wide`

To get info about what executed while deployment

`kubectl describe deployment nginx`


To get information regarding pod how its created

`kubectl get pod`

`kubectl describe pod nginx-77b4fdf86c-2cp5n | pod name`



To get logs of the pod/container running

`kubectl logs nginx-77b4fdf86c-2cp5n`


To execute command within pod/container

`kubectl exec -it nginx-77b4fdf86c-2cp5n --/bin/bash`


To edit the deployment changes

`kubectl edit <resource>`

`kubectl edit deployment <name>`

`kubectl edit deployment` --> Takes the default one

Change the default replicas value to 3 and save the file, 
it will automatically takes the changes


To delete all the services , pods related to deployment just delete the 
deployment
`kubectl delete deployment nginx`
