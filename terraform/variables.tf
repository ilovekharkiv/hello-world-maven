variable "vpc_cidr_block" {
    default = "10.0.0.0/16"
}
variable "subnet_cidr_block" {
    default = "10.0.1.0/24"
}
variable "avail_zone" {
    default = "eu-central-1c"
}
variable "env_prefix" {
    default = "dev"
}
variable "my_ip" {
    default = "0.0.0.0/0"
}
variable "instance_type" {
    default = "t2.micro"
}

variable "region" {
    default = "eu-central-1"
}