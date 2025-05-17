# 开发指南

本文档介绍了如何使用 `skaffold` 命令行工具在本地构建和运行 Online Boutique 的源代码。

## 前提条件

- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- [kubectl](https://kubernetes.io/docs/tasks/tools/)（可以通过 `gcloud components install kubectl` 安装，适用于选项 1 - GKE）
- [skaffold **2.0.2+**](https://skaffold.dev/docs/install/)（建议使用最新版本），一个批量构建和部署 Docker 镜像的工具。
- 克隆仓库：
    ```sh
    git clone https://github.com/GoogleCloudPlatform/microservices-demo
    cd microservices-demo/
    ```
- 启用了 Google Container Registry 的 Google Cloud 项目（适用于选项 1 - GKE）
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)（可选，适用于选项 2 - 本地集群）
- [Kind](https://kind.sigs.k8s.io/)（可选，适用于选项 2 - 本地集群）

## 选项 1: Google Kubernetes Engine (GKE)

> 💡 如果您使用 Google Cloud 并希望在一个真实的集群中尝试，推荐使用此方法。**注意**: 如果您的集群启用了 Workload Identity，请[查看这些说明](https://cloud.google.com/kubernetes-engine/docs/how-to/workload-identity#enable)

1. 创建一个 Google Kubernetes Engine 集群，并确保 `kubectl` 指向该集群。

    ```sh
    gcloud services enable container.googleapis.com
    ```

    ```sh
    gcloud container clusters create-auto demo --region=us-central1
    ```

    ```
    kubectl get nodes
    ```

2. 在您的 GCP 项目上启用 Artifact Registry (AR)，并配置 `docker` CLI 以通过 AR 认证：

    ```sh
    gcloud services enable artifactregistry.googleapis.com
    ```

    ```sh
    gcloud artifacts repositories create microservices-demo \
      --repository-format=docker \
      --location=us \
    ```

    ```sh
    gcloud auth configure-docker -q 
    ```

3. 在本仓库根目录下运行 `skaffold run --default-repo=us-docker.pkg.dev/[PROJECT_ID]/microservices-demo`，其中 [PROJECT_ID] 是您的 GCP 项目 ID。

    此命令将：

    - 构建容器镜像
    - 将它们推送到 AR
    - 应用 `./kubernetes-manifests`，将应用程序部署到 Kubernetes 上。

    **故障排除：** 如果您在 Google Cloud Shell 上遇到 "No space left on device" 错误，可以改用 Google Cloud Build 构建镜像：[启用 Cloud Build API](https://console.cloud.google.com/flows/enableapi?apiid=cloudbuild.googleapis.com)，然后运行 `skaffold run -p gcb --default-repo=us-docker.pkg.dev/[PROJECT_ID]/microservices-demo`。

4. 找到您的应用程序 IP 地址，然后在浏览器中访问该地址以确认安装成功。

    ```sh
    kubectl get service frontend-external
    ```

5. 导航至 `http://EXTERNAL-IP` 以访问 Web 前端。

## 选项 2 - 本地集群 

1. 使用以下任一工具启动本地 Kubernetes 集群：

    - 启动 **Minikube**（已在 Ubuntu Linux 上测试）。请确保本地 Kubernetes 集群至少具有：
        - 4 核 CPU
        - 4.0 GiB 内存
        - 32 GB 磁盘空间

      ```shell
      minikube start --cpus=4 --memory 4096 --disk-size 32g
      ```

    - 启动 **Docker for Desktop**（已在 Mac/Windows 上测试）。进入 Preferences：
        - 选择“Enable Kubernetes”
        - 设置 CPUs 至少为 3，内存至少为 6.0 GiB
        - 在 "Disk" 标签下，设置至少 32 GB 磁盘空间

    - 启动 **Kind** 集群：

      ```shell
      kind create cluster
      ```

2. 运行 `kubectl get nodes` 以验证是否连接到了对应的控制平面。

3. 运行 `skaffold run`（第一次会比较慢，可能需要 ~20 分钟）。这将构建并部署应用程序。如果您需要在重构代码时自动重建镜像，请运行 `skaffold dev` 命令。

4. 运行 `kubectl get pods` 以验证 Pod 是否已就绪并正在运行。

5. 运行 `kubectl port-forward deployment/frontend 8080:8080` 以前转前端服务的端口。

6. 导航至 `localhost:8080` 以访问 Web 前端。

## 清理

如果您是通过 `skaffold run` 命令部署的应用程序，可以运行 `skaffold delete` 来清理已部署的资源。