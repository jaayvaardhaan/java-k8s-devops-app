[![Java App CI-CD Pipeline](https://github.com/jaayvaardhaan/java-k8s-devops-app/actions/workflows/ci-cd.yaml/badge.svg)](https://github.com/jaayvaardhaan/java-k8s-devops-app/actions/workflows/ci-cd.yaml)
# Java DevOps Pipeline: Multi-Stage Containerization & Local Cluster Orchestration 🚀
A comprehensive, hands-on portfolio project tracking the transition of a raw, local Java application into a highly available, load-balanced microservices infrastructure using **Docker** and **Kubernetes**. 

This repository documents my end-to-end implementation of the "Learn in Public" methodology, highlighting structural optimizations, advanced container builds, and terminal-driven cluster monitoring.

---

## 🏗️ Architecture & Component Breakdown

```unset
[Laptop Browser] ──> [Port-Forward Bridge] ──> [K8s Service Gateway] ──> [3x Replicated Java Pods]
```

### 1. The Application Layer (`Main.java`)
* Implemented a lightweight, standalone native Java HTTP web server utilizing `com.sun.net.httpserver`.
* Exposes an active, lightweight network context listening natively on port `8080`.

### 2. Multi-Stage Container Engineering (`Dockerfile`)
* **Optimization Pattern:** Implemented a strict **Multi-Stage Build** to decouple the heavy build workspace from the final execution layer.
* **Stage 1 (Builder Workspace):** Compiles source code inside an `eclipse-temurin:17-jdk-alpine` Linux environment. 
* **The Java Anonymous Class Fix:** Resolved a runtime `NoClassDefFoundError` by updating the compilation stage to capture nested anonymous inner classes (`Main$1.class`) using a wildcard directive (`COPY --from=builder /app/*.class .`).
* **Stage 2 (Production Runtime):** Transfers only the compiled execution binaries into a bare-bones, secure `eclipse-temurin:17-jre-alpine` container.
* **Efficiency Metric:** Slashed image overhead down to a lean **~21 MB memory footprint** in active execution.

### 3. Declarative Cluster Orchestration (`deployment.yaml`)
* Deployed the architecture inside a local **Kind (Kubernetes in Docker)** cluster ecosystem to bypass host virtualization constraints.
* Declared a target infrastructure state running **3 identical app replicas**.
* Validated Kubernetes' native **Self-Healing capability** by explicitly evicting active node instances via `kubectl delete` and observing the control plane instantly spin up replacement pods to maintain zero downtime.
* Exposed the container fleet through a unified `NodePort` Service mapping traffic to the pods.

---

## 📊 Terminal Infrastructure Monitoring

Instead of relying on heavy desktop graphical interfaces (GUIs) that consume excessive local RAM, cluster health, service networking, and live request load-balancing are monitored using **K9s**—the industry-standard terminal dashboard interface.

---

## 🛠️ Step-by-Step Local Deployment Guide

### Prerequisites
* Docker Desktop configured with a WSL2 backend
* Local `kind` and `kubectl` terminal binaries
* A terminal user interface dashboard (`k9s`)

### Execution Sequence

1. **Spin up the isolated Kubernetes cluster engine inside Docker:**
   ```bash
   kind create cluster --name devops-cluster
   ```

2. **Sync the local Docker image into the Kind node control plane:**
   ```bash
   kind load docker-image my-first-java-app:latest --name devops-cluster
   ```

3. **Apply the infrastructure layout manifest:**
   ```bash
   kubectl apply -f deployment.yaml
   ```

4. **Expose the deployment via a cluster service:**
   ```bash
   kubectl expose deployment java-app-deployment --type=NodePort --port=8080 --name=java-app-service
   ```

5. **Establish the local network bridge (Keep this terminal running):**
   ```bash
   kubectl port-forward svc/java-app-service 8080:8080
   ```

6. **Monitor live load-balancing logs natively in a separate tab:**
   ```bash
   .\k9s
   ```
   *(Inside K9s, type `:pod` to see the replica fleet, select a pod, and hit `l` to inspect traffic logs).*
   
   

