## [0.1.3](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/compare/0.1.2...0.1.3) (2022-07-07)


### Build and continuous integration

* create a dispatcher workflow triggering only one CI/CD workflow per update on non-default branches with open PRs ([c85002e](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/c85002eae0073f6eb49cdbc998f9b7c3973d10b9))
* **deps:** bump semantic-release from 19.0.2 to 19.0.3 ([b707f38](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/b707f38e8ea9ef39bac67e68a5fc5f0d65d63e30))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.0.3 ([9b9f604](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/9b9f604ebab44d660686767ed714ad02dbbcaf25))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.0 ([acd04d7](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/acd04d7cc1c17fbb1ed7da615c10be6123bd18f2))
* ignore dependabot's branch builds ([f08a75d](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/f08a75d50e99a95538116e3bcf62ac0b6548ba71))


### Dependency updates

* **core-deps:** update kotlin to v1.7.10 ([c38f7db](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/c38f7dbe9c41d719792b02757e5422cf22e1144f))
* **deps:** update kotest to v5.3.1 ([2b3c1f4](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/2b3c1f472e328c0431e950e5063442ebbcccd369))
* **deps:** update kotest to v5.3.2 ([c0d6c13](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/c0d6c13e355428b897efcae1b7dbffcece20934b))
* **deps:** update plugin dokka to v1.7.0 ([b3e75cc](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/b3e75cc12fcead15139d312caba979fa3346a35e))
* **deps:** update plugin multijvmtesting to v0.4.3 ([6e9bc4c](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/6e9bc4c34829691986c3310e536b6268bb00f30f))
* **deps:** update plugin multijvmtesting to v0.4.4 ([3adee7e](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/3adee7ee1e9e06d1109d36a7e83c2eeefc526f22))
* **deps:** update plugin multijvmtesting to v0.4.5 ([256c682](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/256c682a00f1b34b398cf89333af12539df93f79))
* **deps:** update plugin publishoncentral to v0.8.3 ([e67fbdd](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/e67fbddb932699e2e02dc4b6e755b952e9a6a0db))
* **deps:** update plugin publishoncentral to v1 ([41e2881](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/41e28818f66fc6ebf057e5053609c6e9139a7261))
* **deps:** update plugin publishoncentral to v2 ([120e083](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/120e0830d11f71e20959443394c8524f1a80ca9e))

## [0.1.2](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/compare/0.1.1...0.1.2) (2022-06-10)


### General maintenance

* remove the feature preview configuration of Gradle catalogs (they are stable now) ([a0a9ff7](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/a0a9ff7fb9dd3d6ae82c558caa8b55604dd78af1))


### Build and continuous integration

* **deps:** separate kotlin's and dokka's versions ([e5ee2ba](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/e5ee2ba230015c0e9242bde13752f99a2fcefc08))
* **deps:** update danysk/build-check-deploy-gradle-action action to v1.2.16 ([1cf1909](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/1cf190986fd0326660311186a3caf4a10392fba0))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2 ([cb24a07](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/cb24a07c175b986eb5b6379dc1d85f05a1983539))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.0.1 ([0866849](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/0866849f8d49560d03e16c8386bd5c1641012ce4))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.0.2 ([9c1edea](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/9c1edead12cbd32e109f3072569fe35d43f95009))
* rename ci-complete to ci-success ([7102991](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/71029916c8ceedbe2e7fc44322f6656244c8b5cd))
* run ktlintCheck before allowing to commit ([279a82a](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/279a82a9f8e88ea3afa843e1160eac06a0c2179c))


### Dependency updates

* **core-deps:** update kotlin to v1.7.0 ([2af7f96](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/2af7f96efaf1c8360d98acc4a1536bd455a73c79))
* **deps:** update dependency org.mockito:mockito-core to v4.6.0 ([8bb89c4](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/8bb89c4e1022a8839ad2644420a5290f168209e5))
* **deps:** update dependency org.mockito:mockito-core to v4.6.1 ([d131c3c](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/d131c3ce7f083ff08f36362c8ae6539c3c55170c))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.8 ([b2b2dfa](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/b2b2dfa61a89e1c8c15cd57bbea5280134e53edc))
* **deps:** update kotest to v5 ([9132120](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/9132120b9a73363a0ac05b08912f01ed1677d5e2))
* **deps:** update plugin com.gradle.enterprise to v3.10.2 ([08bc4f9](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/08bc4f997b49ab1d6482e534da630e4219f62f39))
* **deps:** update plugin kotlin-qa to v0.19.0 ([e80e437](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/e80e4376a612c725c24c0036953e78af7ae06ee6))
* **deps:** update plugin kotlin-qa to v0.19.1 ([b00d0ca](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/b00d0ca775fd5d85fff4afb4238c82a65b467e15))
* **deps:** update plugin multijvmtesting to v0.4.0 ([d3f2038](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/d3f203885bc484c8f3d89f00073cfe4395b48a7f))
* **deps:** update plugin multijvmtesting to v0.4.1 ([559459a](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/559459ae1134ec63141d4b9dc249f391cc64333f))
* **deps:** update plugin multijvmtesting to v0.4.2 ([cd00b43](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/cd00b43e1733742efaeddb1532ed6ba66f6eee98))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.12 ([cf018d9](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/cf018d9265bfda67c0015a6aface13fb39669c68))
* **deps:** update plugin publishoncentral to v0.8.0 ([2c6bf5b](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/2c6bf5b3b6f3145cf3df65e43bc4a5e4a0e3cc87))
* **deps:** update plugin publishoncentral to v0.8.2 ([d1fbc24](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/d1fbc24a7de8689f813f95641bfc4dc2a44c9d4e))

## [0.1.1](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/compare/0.1.0...0.1.1) (2022-05-30)


### Build and continuous integration

* '-Xopt-in' is deprecated and will be removed in a future release, use -opt-in instead ([8e338a4](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/8e338a4b8bb2cc9693a298aa4b7597e44773b464))
* **release:** add the configuration for semantic release ([35a19e3](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/35a19e3d4672a8514025615d8ffc04e2ff1ee67f))


### Dependency updates

* **core-deps:** update kotlin to v1.6.21 ([abbdbe5](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/abbdbe5ec0f474f46fb4e18864d1fbee13e90e5e))
* **deps:** update dependency gradle to v7.4.2 ([2b8ceda](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/2b8cedab9e0d4e528ac0b35fb82c88e765a2ae15))
* **deps:** update kotest to v4.6.4 ([079c1ab](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/079c1abfb8500309245bb630a411e086a3265e57))
* **deps:** update plugin com.gradle.enterprise to v3.10.1 ([c617f00](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/c617f004114e944bd37685beb2a792ce2626b9ca))
* **deps:** update plugin com.gradle.enterprise to v3.7.2 ([07bed1d](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/07bed1d24620ad45543daa17790baa060b5a36b0))
* **deps:** update plugin kotlin-qa to v0.2.2 ([9187421](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/91874217f1da7633cab0f274f61c72f2b142ddcc))
* **deps:** update plugin publishoncentral to v0.7.19 ([4e811b2](https://github.com/DanySK/Template-for-Kotlin-JVM-Projects/commit/4e811b2236e83710c056ce8cfe176f565f41853c))
