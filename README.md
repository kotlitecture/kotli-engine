## Publish to Maven Central

1. Create Sonatype account here - https://issues.sonatype.org/login.jsp
2. Provide your project details, by contacting central-support@sonatype.com (instruction: https://central.sonatype.org/publish/publish-guide/)
3. https://central.sonatype.org/publish/requirements/gpg/#distributing-your-public-key

## Google Artifact Registry

Docs - https://cloud.google.com/artifact-registry/docs/java

1. Go to Google Cloud Console and create or select project - https://console.cloud.google.com/home/dashboard
2. Enable Artifact Registry for the project - https://console.cloud.google.com/apis/enableflow?apiid=artifactregistry.googleapis.com
3. Create repository (with Java format) - https://console.cloud.google.com/artifacts
4. Check created repository and click on 'ADD PRINCIPAL'
5. Fill the next input fields and then confirm your changes with **Save**: 
   - New principals - **allUsers**
   - Assign roles - **Artifact Registry Reader**
6. Create Service Account for the project with a role 'Artifact Registry Writer' - https://console.cloud.google.com/iam-admin/serviceaccounts
7. Open created service account and create new key with type JSON
8. Install gcloud tools and proceed these steps to generate credentials to publish artifacts - https://cloud.google.com/artifact-registry/docs/java/authentication#pwd-gradle
