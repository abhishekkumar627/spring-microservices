Go to the folder where these deploy.yaml and svc.yaml exits
# Kubernetes Services and deployments 
Open powershell window

`ls`

To all the information regarding which things are running

`kubectl get all`

To run these yaml files

Service registry gets created
`kubectl apply -f .\deploy.yaml`

service gets created
`kubectl apply -f .\svc.yaml`

`kubectl get all`

Services and deployments gets created using these.

`kubectl delete -f .\deploy.yaml`

`kubectl delete -f .\svc.yaml`  

##### Kubernetes Namespaces

`kubectl create namespace my-namespace`

To get the namespaces
By default its comes with 4 namespaces.

`kubectl get namespace`

1. default --  All the resources you create, deployment,pod,replica set if not defining your names system
2. kube-node-lease -- To create lease object from kubernetes cluster .
3. kube-public -- For public access all info related to kubernetes cluster comes from here.
4. kube-system -- Used for resources like API Server, controll manager, scheduler, kubernetes-proxy

1. Kubernetes-dashboard -- comes with it as well


To restrict team wise resources use namespaces.

##### To create deployment in my namespace
`kubectl apply -f .\deploy.yaml -n my-namespace`

`kubectl get all`

`kubectl get all -n my-namespace`

#### In a namespace
`kubectl api-resources --namespaced=true`

#### Not in a namespace
`kubectl api-resources --namespaced=false`

##### Kubernetes Services

1. Internal service -- > 
    1. Cluster Ip service -- Ex. deploy.yaml/svc.yaml
    2. Headless service -- Whenever we are working with stateful sets(sticky identity)
2. External Service -- >
    1. LoadBalancer Service --  spec type as LoadBalancer only one port : 80 , take all requests from external world.
    2. Nodeport service -- spec type as NodePort, port range - (33000-32767)

##### Ingres service

Check the addons available in minikube
`minikube addons list`

To enable ingress
`minikube addons enable ingress`

Difference between deployment and stateful set

Chrome -- > Java application  -- >  Mysql

Java application will only do the processing of data 
Mysql stores that data thus 

pods of java appn are only deployments
pods of mysql are stateful sets pods that maintain state of the application.

##### volumes
1. Whenever any issue is their with the pod, it destroys itself and recreate it, with help of volumes we safeguard our application data.
2. Storage should not depend on the pod lifecycle.
3. Storage should be available to all the nodes.
4. Storage should survive cluster crashes.
5. persistent volume = yaml configuration 
6. Persistent volume needs to be claimed thus pers-vol-claim configuration needs to be provided for this.
7. For dynamic peristent volume - Storage class is being used.

##### Kubernetes Health Checks
1. Liveness - Many applicaiton running for long time, eventually transition to broken states and cannot recover
except by being restarted. Kubernetes provides Liveness probe to detect and remedy such situations.

2. Readiness - Is your application ready to serve requests or not, once all dependent app are started and app is ready then its ready to serve the traffic.

These can be defineds using, http, tcp, commands as well as gRPc requests.

3. startup probe is used in legacy appn.


##### For Service-registry : 

Going to create stateful set, it will be storing/having state of each of the applcation which connects to it.

Mysql : 

Going to store state of everything thus it is also stateful set.

##### Deployments will be of 
1. cloudGateway
2. configserver
3. orderservice
4. paymentservice
5. productservice
6. serviceregistry

