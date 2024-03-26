## Overview

The plugin is pre-configured to correctly work with Gradle [version catalog toml file](https://docs.gradle.org/current/userguide/platforms.html).
In project this file can be found here: `gradle/libs.versions.toml`.

Detailed documentation: https://github.com/littlerobots/version-catalog-update-plugin?tab=readme-ov-file#getting-started

## Updating the `libs.versions.toml` file

```
./gradlew versionCatalogUpdate (not recommended)
```

This command will update all dependencies automatically.

## Updating the `libs.versions.toml` file in interactive mode (recommended)

```
./gradlew versionCatalogUpdate --interactive
```

Updating all dependencies at once is without testing is generally not recommended. When running `./gradlew versionCatalogUpdate --interactive` the `gradle/libs.versions.toml` file will not be directly be updated, instead a `gradle/libs.versions.updates.toml` file will be created containing the entries that would be updated and any pinned entries that can be updated.
Once this file is generated, you can manually apply all the required changes to the original version catalog file.


