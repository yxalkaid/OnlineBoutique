# Online Boutique 快速入门

本教程展示了如何将 **[Online Boutique](https://github.com/GoogleCloudPlatform/microservices-demo)** 部署到 Kubernetes 集群。

您可以在以下环境中运行 Online Boutique：
- 本地 **[minikube](https://minikube.sigs.k8s.io/docs/)** 集群，该集群已内置在 Cloud Shell 实例中
- 使用新的或现有的 [Google Cloud 项目](https://cloud.google.com/resource-manager/docs/creating-managing-projects#creating_a_project) 创建 **[Google Kubernetes Engine](https://cloud.google.com/kubernetes-engine)** 集群

让我们开始吧！

## Kubernetes 集群设置

根据下面的说明，使用 **minikube** 或 **GKE** 设置 Kubernetes 集群。

### Minikube 指南

Minikube 会在 Cloud Shell 上创建一个本地 Kubernetes 集群。

1. 点击编辑器窗口底部状态栏上的 <walkthrough-editor-spotlight spotlightId="minikube-status-bar">minikube</walkthrough-editor-spotlight>。

2. 命令面板会提示您选择要控制的 minikube 集群。选择 **minikube**，并在下一个提示中点击 **Start**（如果集群尚未启动）。

3. 如果出现提示，请授权 Cloud Shell 使用您的凭据进行 GCP API 调用。

*minikube 启动可能需要几分钟时间。*

一旦 minikube 启动完成，您就可以继续下一步了。

### GKE 指南

要创建 GKE 集群，您需要 **[创建一个 Google Cloud 项目](https://cloud.google.com/resource-manager/docs/creating-managing-projects#creating_a_project)** 或使用现有项目。

1. 通过 **View > Find Command** 打开命令面板。

2. 运行命令 **"Cloud Code: Create GKE cluster"**。

3. 选择您的 GCP 项目。

4. 在 GKE 向导中应用以下配置：  
> - 区域：us-central1-b
> - 集群名称：onlineboutique
> - 节点数量：4
> - 机器类型：e2-standard-2

5. 点击 **Create Cluster**。一旦集群成功创建，您可以继续下一步。

## 在 Kubernetes 上运行

现在您可以在 Kubernetes 集群上运行 Online Boutique！

1. 从状态栏启动 <walkthrough-editor-spotlight spotlightId="cloud-code-status-bar">Cloud Code 菜单</walkthrough-editor-spotlight> 并选择 <walkthrough-editor-spotlight spotlightId="cloud-code-run-on-k8s">Run on Kubernetes</walkthrough-editor-spotlight>。

2. 如果提示选择 Skaffold 配置文件，请选择 **[default]**。

3. 选择 **Yes** 以确认当前上下文。

4. 如果您使用的是 GKE 集群，您需要确认容器镜像仓库。

5. 如果出现提示，请授权 Cloud Shell 使用您的凭据进行 GCP API 调用。

Cloud Code 使用 <walkthrough-editor-open-file filePath="skaffold.yaml">skaffold.yaml</walkthrough-editor-open-file> 中定义的配置来构建和部署应用程序。*部署可能需要几分钟时间。*

6. 应用程序运行后，本地 URL 将显示在 <walkthrough-editor-spotlight spotlightId="output">Output</walkthrough-editor-spotlight> 终端中。

7. 要访问您的 Online Boutique 前端服务，请点击编辑器窗口右上角的 <walkthrough-spotlight-pointer spotlightId="devshell-web-preview-button" target="cloudshell">Web Preview 按钮</walkthrough-spotlight-pointer>。

8. 选择 **Change Port** 并输入 '4503' 作为端口，然后点击 **Change and Preview**。您的应用程序将在新窗口中打开。

## 停止应用程序

要停止运行应用程序：

1. 进入 <walkthrough-editor-spotlight spotlightId="activity-bar-debug">Debug 视图</walkthrough-editor-spotlight>

2. 点击 **Stop** 图标。

3. 选择 **Yes** 以清理已部署的资源。

您可以从 Debug 视图中启动、停止和调试应用程序。

### 清理

如果您已将应用程序部署到 Google Cloud 项目中的 GKE 集群，为了避免产生费用，您应删除该集群。

1. 导航到 Activity 栏中的 <walkthrough-editor-spotlight spotlightId="activity-bar-cloud-k8s">Cloud Code - Kubernetes 视图</walkthrough-editor-spotlight>。

2. 在 <walkthrough-editor-spotlight spotlightId="cloud-code-gke-explorer">Google Kubernetes Engine Explorer 标签页</walkthrough-editor-spotlight> 下，右键点击您的集群并选择 **Delete Cluster**。

## 结束

<walkthrough-conclusion-trophy></walkthrough-conclusion-trophy>

恭喜！您已成功使用 Cloud Shell 部署了 Online Boutique。

<walkthrough-inline-feedback></walkthrough-inline-feedback>

##### 接下来做什么？

尝试其他部署选项来部署 Online Boutique：
- **Istio/Cloud Service Mesh**：<walkthrough-editor-open-file filePath="./kustomize/components/service-mesh-istio/README.md">查看这些说明</walkthrough-editor-open-file>。

了解更多关于 [Cloud Shell](https://cloud.google.com/shell) IDE 环境和 [Cloud Code](https://cloud.google.com/code) 插件。