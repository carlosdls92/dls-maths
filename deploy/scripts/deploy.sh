#!/usr/bin/env bash
set -e

APP_DIR="/opt/codedeploy/dls-maths-app"
cd "$APP_DIR"

# Cargar variables
source image.env

echo "IMAGE_URI=$IMAGE_URI" > .env

# Login a ECR (por si el token expiró)
AWS_REGION="$(curl -s http://169.254.169.254/latest/dynamic/instance-identity/document | jq -r .region || echo eu-west-1)"
ACCOUNT_ID="$(aws sts get-caller-identity --query Account --output text)"
aws ecr get-login-password --region "$AWS_REGION" | docker login --username AWS --password-stdin "$ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com"

# Pull & Up
docker compose pull
docker compose up -d --remove-orphans

# Limpieza (opcional)
docker image prune -f

echo "Despliegue completado 🚀"
