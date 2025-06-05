# Info

## 服务
## 服务列表
| 服务名称                  | 使用的语言       | 依赖的镜像                                                                 |
|---------------------------|------------------|----------------------------------------------------------------------------|
| adservice                | Java             | `eclipse-temurin:21` (构建阶段), `eclipse-temurin:21.0.4_7-jre-alpine` (运行阶段) |
| cartservice              | C#               | `mcr.microsoft.com/dotnet/sdk:8.0.402-noble` (构建阶段), `mcr.microsoft.com/dotnet/runtime-deps:8.0.8-noble-chiseled` (运行阶段) |
| checkoutservice          | Go               | `golang:1.23.1-alpine` (构建阶段), `scratch` (运行阶段)                     |
| currencyservice          | Node.js          | `node:20.17.0-alpine` (构建阶段), `alpine:3.20.3` (运行阶段)               |
| emailservice             | Python           | `python:3.12.6-slim`                                                       |
| frontend                 | Go               | `golang:1.23.1-alpine` (构建阶段), `scratch` (运行阶段)                     |
| loadgenerator            | Python           | `python:3.12.6-slim`                                                       |
| paymentservice           | Node.js          | `node:20.17.0-alpine` (构建阶段), `alpine:3.20.3` (运行阶段)               |
| productcatalogservice    | Go               | `golang:1.23.1-alpine` (构建阶段), `scratch` (运行阶段)                     |
| recommendationservice    | Python           | `python:3.12.6-slim`                                                       |
| shippingservice          | Go               | `golang:1.23.1-alpine` (构建阶段), `scratch` (运行阶段)                     |
| shoppingassistantservice | Python           | `python:3.12.6-slim`                                                       |

### 说明：
- **Java**：使用 `eclipse-temurin` 镜像作为基础镜像。
- **C#**：使用 `.NET SDK` 和 `.NET Runtime` 镜像。
- **Go**：通常使用 `golang` 镜像进行构建，并将最终二进制文件复制到 `scratch` 镜像中以减少体积。
- **Node.js**：使用 `node` 镜像进行构建，并将依赖项复制到轻量级的 `alpine` 镜像中。
- **Python**：使用 `python` 镜像作为基础镜像。

## 部署

1. 下载skaffold二进制文件

2. 在使用了golang镜像的Dockerfile中，添加GOPROXY环境变量
    ```Dockerfile
    # restore dependencies
    ENV GOPROXY=https://goproxy.cn,direct
    COPY go.mod go.sum ./
    RUN go mod download
    ```

3. adservice的gradle-wrapper.properties文件设置distributionUrl
    ```
    distributionUrl=https://mirrors.aliyun.com/gradle/distributions/v8.10.2/gradle-8.10.2-bin.zip
    ```

4. adservice的build.gradle文件设置maven镜像源
    ```
    maven { url 'https://maven.aliyun.com/repository/public' }
    ```

5. paymentservice和currencyservice更换Alpine镜像源
    ```
    RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
    ```

6. shoppingassistantservice更换apt-get镜像源
    ```
    RUN sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list.d/debian.sources \
    && sed -i 's/security.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list.d/debian.sources
    ```

## Istio
1. 安装istio
    从`https://github.com/istio/istio`下载发行版
    ```bash
    istioctl install --set profile=demo
    ```
2. 创建命名空间`online-boutique`，启用Istio自动Sidecar注入
    ```bash
    kubectl create namespace online-boutique
    kubectl label namespace online-boutique istio-injection=enabled
    ```

3. 部署项目到命名空间`online-boutique`中，并进行istio配置
    ```bash
    skaffold run -n online-boutique
    kubectl apply -f ./istio-manifests -n online-boutique
    ```

4. 配置
    ```bash
    kubectl apply -f "D:\Downloads\istio-1.26.1\samples\addons\kiali.yaml"
    kubectl apply -f "D:\Downloads\istio-1.26.1\samples\addons\jaeger.yaml"
    kubectl apply -f "D:\Downloads\istio-1.26.1\samples\addons\prometheus.yaml"
    kubectl apply -f "D:\Downloads\istio-1.26.1\samples\addons\grafana.yaml"
    ```

## EmailService_Re
```bash
kubectl create secret generic <secret-name> \
  --from-literal=username=<value1> \
  --from-literal=password=<value2> -n online-boutique
```

## tips
1. emailservice_re内存资源限制过小，频繁出现OOM并重启
    ```yaml
    resources:
    requests:
        cpu: 200m
        memory: 128Mi
    limits:
        cpu: 400m
        memory: 256Mi
    ```

2. emailservice_re健康检测初始延迟设置过短，导致健康检查误判而重启。
    ```yaml
    readinessProbe:
          initialDelaySeconds: 30 # 设置初始延迟时间
          periodSeconds: 15 # 设置检查间隔时间
          grpc:
            port: 9090
    livenessProbe:
        initialDelaySeconds: 30 # 设置初始延迟时间
        periodSeconds: 15 # 设置检查间隔时间
        grpc:
        port: 9090
    ```

3. 邮件模板文件中的变量名需要匹配对应语言中的类属性名