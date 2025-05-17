<!-- <p align="center">
<img src="/src/frontend/static/icons/Hipster_HeroLogoMaroon.svg" width="300" alt="Online Boutique" />
</p> -->
![持续集成](https://github.com/GoogleCloudPlatform/microservices-demo/workflows/Continuous%20Integration%20-%20Main/Release/badge.svg)

**在线精品店** 是一个面向云的微服务演示应用程序。该应用程序是一个基于 Web 的电子商务应用，用户可以浏览商品、将其添加到购物车并购买。

Google 使用此应用程序来展示开发人员如何使用 Google Cloud 产品现代化企业应用程序，包括：[Google Kubernetes Engine (GKE)](https://cloud.google.com/kubernetes-engine)、[Cloud Service Mesh (CSM)](https://cloud.google.com/service-mesh)、[gRPC](https://grpc.io/)、[Cloud Operations](https://cloud.google.com/products/operations)、[Spanner](https://cloud.google.com/spanner)、[Memorystore](https://cloud.google.com/memorystore)、[AlloyDB](https://cloud.google.com/alloydb) 和 [Gemini](https://ai.google.dev/)。此应用程序可以在任何 Kubernetes 集群上运行。

如果您正在使用此演示，请 **★Star** 此存储库以表达您的兴趣！

**注意谷歌员工**：请填写 [go/microservices-demo](http://go/microservices-demo) 中的表格。

## 架构

**在线精品店** 由用不同语言编写的 11 个微服务组成，它们通过 gRPC 进行通信。

[![微服务架构](/docs/img/architecture-diagram.png)](/docs/img/architecture-diagram.png)

在 [`./protos` 目录](/protos)中找到 **Protocol Buffers 描述**。

| 服务                                              | 语言      | 描述                                                                                                                       |
| ---------------------------------------------------- | ------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| [frontend](/src/frontend)                           | Go            | 暴露一个 HTTP 服务器来提供网站。不需要注册/登录，并为所有用户自动生成会话 ID。 |
| [cartservice](/src/cartservice)                     | C#            | 在 Redis 中存储用户的购物车内容并检索它。                                                           |
| [productcatalogservice](/src/productcatalogservice) | Go            | 提供从 JSON 文件获取商品列表的功能，并能够搜索商品和获取单个商品。                        |
| [currencyservice](/src/currencyservice)             | Node.js       | 将一种货币金额转换为另一种货币。使用从欧洲中央银行获取的真实值。它是 QPS 最高的服务。 |
| [paymentservice](/src/paymentservice)               | Node.js       | 使用给定的信用卡信息（模拟）收取给定金额，并返回交易 ID。                                     |
| [shippingservice](/src/shippingservice)             | Go            | 基于购物车提供运费估算。将商品邮寄到给定地址（模拟）。                                 |
| [emailservice](/src/emailservice)                   | Python        | 向用户发送订单确认电子邮件（模拟）。                                                                                   |
| [checkoutservice](/src/checkoutservice)             | Go            | 检索用户购物车、准备订单并协调支付、发货和电子邮件通知。                            |
| [recommendationservice](/src/recommendationservice) | Python        | 根据购物车中的内容推荐其他商品。                                                                      |
| [adservice](/src/adservice)                         | Java          | 基于给定的上下文单词提供文字广告。                                                                                   |
| [loadgenerator](/src/loadgenerator)                 | Python/Locust | 持续发送模仿真实用户购物流程的请求到前端。                                              |

## 截图

| 主页                                                                                                         | 结账屏幕                                                                                                    |
| ----------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------ |
| [![商店主页截图](/docs/img/online-boutique-frontend-1.png)](/docs/img/online-boutique-frontend-1.png) | [![结账屏幕截图](/docs/img/online-boutique-frontend-2.png)](/docs/img/online-boutique-frontend-2.png) |

## 快速入门 (GKE)

1. 确保您具备以下要求：
   - [Google Cloud 项目](https://cloud.google.com/resource-manager/docs/creating-managing-projects#creating_a_project)。
   - 包含 `gcloud`、`git` 和 `kubectl` 的 Shell 环境。

2. 克隆最新主版本。

   ```sh
   git clone --depth 1 --branch v0 https://github.com/GoogleCloudPlatform/microservices-demo.git
   cd microservices-demo/
   ```

   `--depth 1` 参数跳过下载 Git 历史记录。

3. 设置 Google Cloud 项目和区域，并确保已启用 Google Kubernetes Engine API。

   ```sh
   export PROJECT_ID=<PROJECT_ID>
   export REGION=us-central1
   gcloud services enable container.googleapis.com \
     --project=${PROJECT_ID}
   ```

   将 `<PROJECT_ID>` 替换为您的 Google Cloud 项目的 ID。

4. 创建 GKE 集群并获取其凭据。

   ```sh
   gcloud container clusters create-auto online-boutique \
     --project=${PROJECT_ID} --region=${REGION}
   ```

   创建集群可能需要几分钟时间。

5. 将 Online Boutique 部署到集群。

   ```sh
   kubectl apply -f ./release/kubernetes-manifests.yaml
   ```

6. 等待 Pod 运行。

   ```sh
   kubectl get pods
   ```

   几分钟后，您应该看到 Pod 处于 `Running` 状态：

   ```
   NAME                                     READY   STATUS    RESTARTS   AGE
   adservice-76bdd69666-ckc5j               1/1     Running   0          2m58s
   cartservice-66d497c6b7-dp5jr             1/1     Running   0          2m59s
   checkoutservice-666c784bd6-4jd22         1/1     Running   0          3m1s
   currencyservice-5d5d496984-4jmd7         1/1     Running   0          2m59s
   emailservice-667457d9d6-75jcq            1/1     Running   0          3m2s
   frontend-6b8d69b9fb-wjqdg                1/1     Running   0          3m1s
   loadgenerator-665b5cd444-gwqdq           1/1     Running   0          3m
   paymentservice-68596d6dd6-bf6bv          1/1     Running   0          3m
   productcatalogservice-557d474574-888kr   1/1     Running   0          3m
   recommendationservice-69c56b74d4-7z8r5   1/1     Running   0          3m1s
   redis-cart-5f59546cdd-5jnqf              1/1     Running   0          2m58s
   shippingservice-6ccc89f8fd-v686r         1/1     Running   0          2m58s
   ```

7. 在浏览器中使用前端的外部 IP 访问 Web 前端。

   ```sh
   kubectl get service frontend-external | awk '{print $4}'
   ```

   在浏览器中访问 `http://EXTERNAL_IP` 以访问您的 Online Boutique 实例。

8. 恭喜！您已部署了默认的 Online Boutique。要部署 Online Boutique 的其他变体（例如，带有 Google Cloud Operations 跟踪、Istio 等），请参阅 [使用 Kustomize 部署 Online Boutique 变体](#deploy-online-boutique-variations-with-kustomize)。

9. 完成后，删除 GKE 集群。

   ```sh
   gcloud container clusters delete online-boutique \
     --project=${PROJECT_ID} --region=${REGION}
   ```

   删除集群可能需要几分钟时间。

## 其他部署选项

- **Terraform**: [参阅这些说明](/terraform) 了解如何使用 [Terraform](https://www.terraform.io/intro) 部署 Online Boutique。
- **Istio / Cloud Service Mesh**: [参阅这些说明](/kustomize/components/service-mesh-istio/README.md) 了解如何部署 Online Boutique 以及带有 Istio 支持的服务网格。
- **非 GKE 集群 (Minikube, Kind 等)**: 请参阅 [开发指南](/docs/development-guide.md) 了解如何在非 GKE 集群上部署 Online Boutique。
- **使用 Gemini 的 AI 助手**: [参阅这些说明](/kustomize/components/shopping-assistant/README.md) 了解如何部署一个由 Gemini 驱动的 AI 助手，该助手可以根据图像建议购买的商品。
- **更多**: [`/kustomize` 目录](/kustomize) 包含使用其他变体自定义 Online Boutique 部署的说明。

## 文档

- [开发](/docs/development-guide.md) 了解如何在本地运行和开发此应用程序。

## 使用 Online Boutique 的演示

- [Platform Engineering in action: Deploy the Online Boutique sample apps with Score and Humanitec](https://medium.com/p/d99101001e69)
- [The new Kubernetes Gateway API with Istio and Anthos Service Mesh (ASM)](https://medium.com/p/9d64c7009cd)
- [Use Azure Redis Cache with the Online Boutique sample on AKS](https://medium.com/p/981bd98b53f8)
- [Sail Sharp, 8 tips to optimize and secure your .NET containers for Kubernetes](https://medium.com/p/c68ba253844a)
- [Deploy multi-region application with Anthos and Google cloud Spanner](https://medium.com/google-cloud/a2ea3493ed0)
- [Use Google Cloud Memorystore (Redis) with the Online Boutique sample on GKE](https://medium.com/p/82f7879a900d)
- [Use Helm to simplify the deployment of Online Boutique, with a Service Mesh, GitOps, and more!](https://medium.com/p/246119e46d53)
- [How to reduce microservices complexity with Apigee and Anthos Service Mesh](https://cloud.google.com/blog/products/application-modernization/api-management-and-service-mesh-go-together)
- [gRPC health probes with Kubernetes 1.24+](https://medium.com/p/b5bd26253a4c)
- [Use Google Cloud Spanner with the Online Boutique sample](https://medium.com/p/f7248e077339)
- [Seamlessly encrypt traffic from any apps in your Mesh to Memorystore (redis)](https://medium.com/google-cloud/64b71969318d)
- [Strengthen your app's security with Cloud Service Mesh and Anthos Config Management](https://cloud.google.com/service-mesh/docs/strengthen-app-security)
- [From edge to mesh: Exposing service mesh applications through GKE Ingress](https://cloud.google.com/architecture/exposing-service-mesh-apps-through-gke-ingress)
- [Take the first step toward SRE with Cloud Operations Sandbox](https://cloud.google.com/blog/products/operations/on-the-road-to-sre-with-cloud-operations-sandbox)
- [Deploying the Online Boutique sample application on Cloud Service Mesh](https://cloud.google.com/service-mesh/docs/onlineboutique-install-kpt)
- [Anthos Service Mesh Workshop: Lab Guide](https://codelabs.developers.google.com/codelabs/anthos-service-mesh-workshop)
- [KubeCon EU 2019 - Reinventing Networking: A Deep Dive into Istio's Multicluster Gateways - Steve Dake, Independent](https://youtu.be/-t2BfT59zJA?t=982)
- Google Cloud Next'18 SF
  - [Day 1 Keynote](https://youtu.be/vJ9OaAqfxo4?t=2416) showing GKE On-Prem
  - [Day 3 Keynote](https://youtu.be/JQPOPV_VH5w?t=815) showing Stackdriver
    APM (Tracing, Code Search, Profiler, Google Cloud Build)
  - [Introduction to Service Management with Istio](https://www.youtube.com/watch?v=wCJrdKdD6UM&feature=youtu.be&t=586)
- [Google Cloud Next'18 London – Keynote](https://youtu.be/nIq2pkNcfEI?t=3071)
  showing Stackdriver Incident Response Management
