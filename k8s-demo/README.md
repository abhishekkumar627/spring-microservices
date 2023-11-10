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

# Kubernetes Namespaces

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

#### To create deployment in my namespace
`kubectl apply -f .\deploy.yaml -n my-namespace`

`kubectl get all`

`kubectl get all -n my-namespace`

#### In a namespace
`kubectl api-resources --namespaced=true`

#### Not in a namespace
`kubectl api-resources --namespaced=false`

# Kubernetes Services

1. Internal service -- > 
    1. Cluster Ip service -- Ex. deploy.yaml/svc.yaml
    2. Headless service -- Whenever we are working with stateful sets(sticky identity)
2. External Service -- >
    1. LoadBalancer Service --  spec type as LoadBalancer only one port : 80 , take all requests from external world.
    2. Nodeport service -- spec type as NodePort, port range - (33000-32767)

