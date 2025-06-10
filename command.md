```bash

# 应用部署
skaffold run -n online-boutique
skaffold delete -n online-boutique

# 安装istio，指定采样率
istioctl install --set profile=demo --set meshConfig.defaultConfig.tracing.sampling=10

# 获取istio配置
kubectl get configmap istio -n istio-system -o yaml

# 前端服务接口前传
kubectl port-forward deployment/frontend 8080:8080 -n online-boutique

# 配置ingressgateway
kubectl apply -f ./istio-manifests/frontend-gateway.yaml -n online-boutique
kubectl delete -f ./istio-manifests/frontend-gateway.yaml -n online-boutique

# 配置限流
kubectl apply -f ./istio-manifests/ingress-rate-limit.yaml
kubectl delete -f ./istio-manifests/ingress-rate-limit.yaml

kubectl get namespace online-boutique --show-labels
kubectl get pods -n online-boutique
kubectl get pods -n istio-system


# 配置遥测追踪
kubectl apply -f "./istio-manifests/mesh-default.yaml"
kubectl delete -f "./istio-manifests/mesh-default.yaml"

istioctl dashboard kiali
istioctl dashboard jaeger
istioctl dashboard prometheus
istioctl dashboard grafana

istio_requests_total
```