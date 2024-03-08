## MYSQL 
docker-compose -f docker-compose-mysql.yml up -d

## REDIS 
docker-compose -f docker-compose-redis.yml up -d

## ELK
```
git clone https://github.com/deviantony/docker-elk.git
```

SEt UP

```
docker-compose up setup
```

RUN

```
docker-compose up
```

FILEBEAT 
> https://devbksheen.tistory.com/entry/ELK-Filebeat%EB%A1%9C-%EC%8B%A4%EC%8B%9C%EA%B0%84-%EB%A1%9C%EA%B7%B8-%EC%88%98%EC%A7%91%ED%95%98%EA%B8%B0with-Spring-Boot


## Application Test With K8s
[ Docker Build ] 
```
./gradlew bootJar -Dorg.gradle.java.home=JDK21_PATH
```
```
docker build -f Dockerfile -t spring-boot3-sample:latest .
```
```
docker push spring-boot3-sample:latest
```

[ minikube 시작 및 yaml 설치 ]
```
minikube start
```
```
kubectl apply -f deployment_dev_service.yaml
```

[ kubectl yaml 삭제 명령어 ]
```
kubectl delete -f deployment_dev_service.yaml
```
[ pod 상태 확인 ]
```
kubectl get pods -A
```

[ pod 재시작 ]
```
kubectl get deployments
```

```
kubectl rollout restart deployment [deployment_name]
```

[kubectl 모니터링 명령어]
```
kubectl describe pod
```

```
kubectl logs [pod name] --all-containers
```

[pod 상태가 Running 으로 변경된 후 포트포워딩을 통해 서비스 접속 ]
```
kubectl port-forward service/nodeport-svc 9090:80
```
