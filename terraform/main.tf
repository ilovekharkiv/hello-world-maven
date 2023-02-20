terraform {
    required_version = ">= 1.3"
    backend "s3" {
        bucket = "hello-maven-bucket"
        key = "hello-maven/state.tfstate"
        region = "eu-west-1"
    }
}

provider "aws" {
    region = var.region
}

resource "aws_vpc" "my-vpc" {
    cidr_block = var.vpc_cidr_block
    tags = {
        Name: "${var.env_prefix}-vpc"
    }
}

resource "aws_subnet" "my-subnet-1" {
    vpc_id = aws_vpc.my-vpc.id
    cidr_block = var.subnet_cidr_block
    availability_zone = var.avail_zone
    tags = {
        Name: "${var.env_prefix}-subnet-1"
    }
}

resource "aws_internet_gateway" "my-gateway" {
    vpc_id = aws_vpc.my-vpc.id
    tags = {
        Name: "${var.env_prefix}-gateway"
    }
}

resource "aws_default_route_table" "main-route-table" {
     default_route_table_id = aws_vpc.my-vpc.default_route_table_id
     route {
        cidr_block = "0.0.0.0/0"
        gateway_id = aws_internet_gateway.my-gateway.id
    }
    tags = {
        Name: "${var.env_prefix}-main-route-table"
    }
}

resource "aws_default_security_group" "default-sg" {
    vpc_id = aws_vpc.my-vpc.id

    ingress {
        from_port = 22
        to_port = 22
        protocol = "tcp"
        cidr_blocks = [var.my_ip]
    }
    ingress {
        from_port = 8080
        to_port = 8080
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
    ingress {
        from_port = 80
        to_port = 80
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
    egress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
        prefix_list_ids = []
    }
    tags = {
        Name: "${var.env_prefix}-default-sg"
    }
}

data "aws_ami" "latest-amazon-linux-image" {
    most_recent = true
    owners = ["amazon"]
    filter {
        name = "name"
        values = ["amzn2-ami-hvm-*-x86_64-gp2"]
    }
    filter {
        name = "virtualization-type"
        values = ["hvm"]
    }
}

resource "aws_instance" "development" {
    ami = data.aws_ami.latest-amazon-linux-image.id
    instance_type = var.instance_type

    subnet_id = aws_subnet.my-subnet-1.id
    vpc_security_group_ids = [aws_default_security_group.default-sg.id]
    availability_zone = var.avail_zone
    associate_public_ip_address = true
    key_name = "ansible_server"
     
    tags = {
        Name: "${var.env_prefix}-server"
    }
}

output "ec2_public_ip" {
    value = aws_instance.development.public_ip
}