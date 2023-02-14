### CI/CD with Jenkins
#### EPAM Cloud & DevOps Fundamentals Autumn 2022
1. `Jenkinsfiles` - basically whole pipeline "logic" described inside.
2. `script.groovy` - included in `Jenkinsfile` just to have it more readable. Could be improved with Jenkins Shared library.
3. `pom.xml` + `src/` - source code of `Java/Maven` app.
4. `Dockerfile` - to compile an image from my `Java/Maven` app.
5. `Terraform/` - for instance provisioning. Refactored version with modules here [Refactored Terraform](https://github.com/ilovekharkiv/terraform). Planning to refactor main version also.
6. `docker-compose.yaml` - to run the containers form my image on target server.
7. `server-cmds.sh` - to run on the target server. Plannig to upgrade it to `Ansible` playbook. Work in progress link here - [ansible/deploy-docker.yaml](https://github.com/ilovekharkiv/ansible)
