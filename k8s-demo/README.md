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