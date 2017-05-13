# BuddyBuild Android Client

### Continuous Integration
[![BuddyBuild](https://dashboard.buddybuild.com/api/statusImage?appID=590e33635ac62a0001c1f1c9&branch=master&build=latest)](https://dashboard.buddybuild.com/apps/590e33635ac62a0001c1f1c9/build/latest?branch=master)

We use BuddyBuild to build the BuddyBuild Android client. What did you expect?

### Test Path
We load .json files from disk during unit tests. When running tests from CL (e.g. `./gradlew test`) the path that the tests is run on is not the same as when running tests from *within* Android Studio. To fix this, we need to tell Android Studio to run tests from the module path for all Android JUnit tests:

![android_studio_set_test_path_screenshot](readme_images/android_studio_set_test_path_screenshot.png)
