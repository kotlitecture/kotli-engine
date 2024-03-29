## Publish to Maven Central

1. Create Sonatype account here - https://issues.sonatype.org/login.jsp
2. Provide your project details, by contacting central-support@sonatype.com (instruction: https://central.sonatype.org/publish/publish-guide/)
3. https://central.sonatype.org/publish/requirements/gpg/#distributing-your-public-key

## Google Artifact Registry - Maven

Docs - https://cloud.google.com/artifact-registry/docs/java

1. Go to Google Cloud Console and create or select project - https://console.cloud.google.com/home/dashboard
2. Enable Artifact Registry for the project - https://console.cloud.google.com/apis/enableflow?apiid=artifactregistry.googleapis.com
3. Create repository with Maven format - https://console.cloud.google.com/artifacts
4. Check created repository and click on 'ADD PRINCIPAL'
5. Fill the next input fields and then confirm your changes with **Save**: 
   - New principals - **allUsers**
   - Assign roles - **Artifact Registry Reader**
6. Create Service Account for the project with a role 'Artifact Registry Writer' - https://console.cloud.google.com/iam-admin/serviceaccounts
7. Open created service account and create new key with type JSON
8. Install gcloud tools and proceed these steps to generate credentials to publish artifacts - https://cloud.google.com/artifact-registry/docs/java/authentication#pwd-gradle


## Google Artifact Registry - Docker

1. login into gcloud with service account key
   ```
   auth activate-service-account --key-file key.json
   ```
2. retrieve access token from gcloud
   ```
   gcloud auth print-access-token
   ```
3. configure docker with gcloud
   ```
   gcloud auth configure-docker us-docker.pkg.dev
   ```
4. configure bootBuildImage with username/password as oauth2accesstoken/<token>
   
https://cloud.google.com/artifact-registry/docs/docker/authentication
