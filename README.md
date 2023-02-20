### CI/CD with Jenkins
#### EPAM Cloud & DevOps Fundamentals Autumn 2022
1. `Jenkinsfiles` - basically whole pipeline "logic" described inside.
2. `script.groovy` - included in `Jenkinsfile` just to have it more readable. Could be improved with Jenkins Shared library.
3. `pom.xml` + `src/` - source code of `Java/Maven` app.
4. `Dockerfile` - to compile an image from my `Java/Maven` app.
5. `terraform/` - for instance provisioning. Refactored version with modules here [Refactored Terraform](https://github.com/ilovekharkiv/terraform). Planning to refactor main version also.
6. `ansible/` - `Ansible` playbook to configure target server. 
