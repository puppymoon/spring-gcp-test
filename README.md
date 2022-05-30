# Getting Started

```
echo "Creating App Engine app"
gcloud app create --region "us-central"
```

install maven
```
sudo apt-get update
sudo apt-get install git -y
sudo apt-get install -yq maven
```

make bucket
```
echo "Making bucket: gs://$DEVSHELL_PROJECT_ID-storage"
gsutil mb gs://$DEVSHELL_PROJECT_ID-storage
```

set ENV
```
export GCLOUD_PROJECT=[PROJECT_ID]
export GCLOUD_BUCKET=$DEVSHELL_PROJECT_ID-storage
```
