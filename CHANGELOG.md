## [4.0.1](https://github.com/DanySK/code-plagiarism-detector/compare/4.0.0...4.0.1) (2022-10-24)


### Bug Fixes

* **rkr:** fix bug not considering leftover unmarked tokens which are longer than min search len ([41ff7ea](https://github.com/DanySK/code-plagiarism-detector/commit/41ff7ea3ad2d81e006e5d1bdb485ffa7be6b959a))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.11 ([7c3e527](https://github.com/DanySK/code-plagiarism-detector/commit/7c3e5276090b6986f90f580daee7f73449bec448))


### Dependency updates

* **deps:** update dependency com.github.javaparser:javaparser-core to v3.24.7 ([494f0a2](https://github.com/DanySK/code-plagiarism-detector/commit/494f0a256697cbd354905b002663f2c30ab2a881))
* **deps:** update dependency org.mockito:mockito-core to v4.8.1 ([61e1ac6](https://github.com/DanySK/code-plagiarism-detector/commit/61e1ac6ec111d43097f7bc6e1de6e49ebc1c1781))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.13 ([ac15609](https://github.com/DanySK/code-plagiarism-detector/commit/ac1560980beba9e0697264a9a773f00ceed272ca))


### Refactoring

* **gst:** remove not useful addAll extension function ([01b34dc](https://github.com/DanySK/code-plagiarism-detector/commit/01b34dcb266a82032921a098f1d23322b0290020))


### Tests

* **detector:** rename variables ([57296f3](https://github.com/DanySK/code-plagiarism-detector/commit/57296f309d6bd37816921eba089679cb7d3f9366))


### Style improvements

* **provider:** replace null check with let scope function ([ad84750](https://github.com/DanySK/code-plagiarism-detector/commit/ad84750811e0886936a03546e7fb4717b57c47d0))

## [4.0.0](https://github.com/DanySK/code-plagiarism-detector/compare/3.0.0...4.0.0) (2022-10-17)


### ⚠ BREAKING CHANGES

* **detector:** change to Running Karp Rabin default strategy

### Features

* **detector:** add detector and comparison strategy interfaces ([f5457e1](https://github.com/DanySK/code-plagiarism-detector/commit/f5457e1df648efbb48eba07d9263e7775e3c800e))
* **detector:** add GST algorithm ([e9642c0](https://github.com/DanySK/code-plagiarism-detector/commit/e9642c0668e4bfa2308f503d37f8af968cb8ea65))
* **detector:** add token based detector ([8773d29](https://github.com/DanySK/code-plagiarism-detector/commit/8773d2917f0013a4d05e4e41a33d8285759e0465))
* **detector:** add token comparison result ([727482e](https://github.com/DanySK/code-plagiarism-detector/commit/727482e36b56136d8d0fb317d9ba65bf3a353fd1))
* **detector:** add token match ([00f85ef](https://github.com/DanySK/code-plagiarism-detector/commit/00f85ef5091831ffbcbd5b720bdf37b53ae313b0))
* **rkr-gst:** add Running Karp-Rabin Greedy String Tiling algorithm ([d100f42](https://github.com/DanySK/code-plagiarism-detector/commit/d100f42d91738f894e6bebae9f3889eb21eb285e))


### Bug Fixes

* **analyzer:** remove multiple token of same type in same position ([2b80999](https://github.com/DanySK/code-plagiarism-detector/commit/2b809995604d1f42801d8942d44899af59b35b3d))
* **detector:** use M instead of Match ([b4b3cf0](https://github.com/DanySK/code-plagiarism-detector/commit/b4b3cf0feab8accb1e895dd469732fd3d4de4ba7))
* **gst:** check occlusion on all tokens ([1ab5fb9](https://github.com/DanySK/code-plagiarism-detector/commit/1ab5fb9cd76460cc772bfc41645ea560472dbadd))
* **gst:** fix condition of `isNotUccluded` function ([3d7c22a](https://github.com/DanySK/code-plagiarism-detector/commit/3d7c22a41d4578c80cc167fe9ddf71d978277f3f))
* **gst:** fix recursion when starting over new scanpattern ([66e80bf](https://github.com/DanySK/code-plagiarism-detector/commit/66e80bfd877008f15c5d70f08d1d5eac5e84962d))
* **rkr:** change from strictly greater to greater than or equals ([c58ef7f](https://github.com/DanySK/code-plagiarism-detector/commit/c58ef7fff7f1b6f4f275e8ce69a52219df7e2637))
* **rkr:** do not use indexes to get sequence starting from actual elements ([e9f345a](https://github.com/DanySK/code-plagiarism-detector/commit/e9f345ab4f38f2153cca09b0463f5fbc88c7b27c))


### Build and continuous integration

* **deps:** update actions/checkout action to v3.1.0 ([8e9b471](https://github.com/DanySK/code-plagiarism-detector/commit/8e9b471429d84c2e6dd7576dd5cef62805366a8a))
* **deps:** update danysk/action-checkout action to v0.2.3 ([5564a4c](https://github.com/DanySK/code-plagiarism-detector/commit/5564a4c6fb564f62b156026e873615f2d42e704c))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.10 ([3b366b8](https://github.com/DanySK/code-plagiarism-detector/commit/3b366b885258c5311ff8fff70a886bfd86ca6a25))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.9 ([8a80d44](https://github.com/DanySK/code-plagiarism-detector/commit/8a80d44238feb17f0366dba50b5aa3dbb2681d3e))


### Dependency updates

* **deps:** update dependency com.charleskorn.kaml:kaml to v0.49.0 ([154d48d](https://github.com/DanySK/code-plagiarism-detector/commit/154d48d07f14ebd5021a4653a4c28a60aefa242e))
* **deps:** update dependency io.mockk:mockk to v1.13.2 ([5bb03af](https://github.com/DanySK/code-plagiarism-detector/commit/5bb03afdb77de80951584de28f27e3ea0d7d4640))
* **deps:** update dependency org.kohsuke:github-api to v1.313 ([196de90](https://github.com/DanySK/code-plagiarism-detector/commit/196de906c38658918edb49e30bf68f5537415450))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.3 ([c43f008](https://github.com/DanySK/code-plagiarism-detector/commit/c43f008a14132a10b6ee485323832f7bc3144b4f))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.12 ([156277d](https://github.com/DanySK/code-plagiarism-detector/commit/156277d21ec767e3808014d21910909278e4d75c))
* **deps:** update kotest to v5.5.0 ([bb5ff9c](https://github.com/DanySK/code-plagiarism-detector/commit/bb5ff9c19d0eb6c458286e76253a92b3f866c6b4))
* **deps:** update kotest to v5.5.1 ([9716292](https://github.com/DanySK/code-plagiarism-detector/commit/9716292b3a1aeb09200853f7e58be0dec08f270b))
* **deps:** update kotlin to v1.7.20 ([19c4915](https://github.com/DanySK/code-plagiarism-detector/commit/19c4915376f65210e148d667b93781fdb627897a))
* **deps:** update logback to v1.4.2 ([774b908](https://github.com/DanySK/code-plagiarism-detector/commit/774b90806ab1287676237412270a9899fafbf456))
* **deps:** update logback to v1.4.3 ([d530f80](https://github.com/DanySK/code-plagiarism-detector/commit/d530f80af22f9f3e38da5b255edec5495e975f3b))
* **deps:** update logback to v1.4.4 ([79cb383](https://github.com/DanySK/code-plagiarism-detector/commit/79cb383f696042f753596b21d5108de5c624ed1d))
* **deps:** update node.js to 16.18 ([41ed45c](https://github.com/DanySK/code-plagiarism-detector/commit/41ed45c2febc94666b73614c29ffd0135f23c24d))
* **deps:** update plugin dokka to v1.7.20 ([182ee3a](https://github.com/DanySK/code-plagiarism-detector/commit/182ee3aa8ac15125ba11282ed092a61b9ad811f6))
* **deps:** update plugin kotlin-qa to v0.25.1 ([9a62ea0](https://github.com/DanySK/code-plagiarism-detector/commit/9a62ea0222d1464ed452d000acc5d9b21d1da828))
* **deps:** update plugin kotlin-qa to v0.26.0 ([dd7f877](https://github.com/DanySK/code-plagiarism-detector/commit/dd7f877e9daa2e5c4fb347a6ea19e5d7cd8b1f3b))
* **deps:** update plugin kotlin-qa to v0.26.1 ([76efaf0](https://github.com/DanySK/code-plagiarism-detector/commit/76efaf0a5cd973edd8699a7ec2665d9ef0762acf))
* **deps:** update plugin multijvmtesting to v0.4.10 ([dbb5f42](https://github.com/DanySK/code-plagiarism-detector/commit/dbb5f429a8f4a52bc478ff12864eeaef2e029699))
* **deps:** update plugin multijvmtesting to v0.4.11 ([f735ea0](https://github.com/DanySK/code-plagiarism-detector/commit/f735ea0969c350f69557986d415cdeb719657a3b))
* **deps:** update plugin multijvmtesting to v0.4.12 ([3f5689c](https://github.com/DanySK/code-plagiarism-detector/commit/3f5689ce6e601f389f9240ad12e898f5dcfbcc98))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.21 ([cb9f66b](https://github.com/DanySK/code-plagiarism-detector/commit/cb9f66b973c726968430f8618651d1d14b2e1829))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.22 ([3955fed](https://github.com/DanySK/code-plagiarism-detector/commit/3955fede8a0bbb23274ec35b1c53af3ace6444ee))
* **deps:** update plugin publishoncentral to v2.0.7 ([65889c7](https://github.com/DanySK/code-plagiarism-detector/commit/65889c7cf66063a513de2a87af730b19e2ee97ae))
* **deps:** update plugin publishoncentral to v2.0.8 ([93be325](https://github.com/DanySK/code-plagiarism-detector/commit/93be325a7f200dc53feb318cd2caf8a92a34d5c1))


### Documentation

* **gst:** add class doc ([d2013f5](https://github.com/DanySK/code-plagiarism-detector/commit/d2013f58e0ca778f370dbf822c0f5de6a539e62a))
* **gst:** add documentation ([6c5a974](https://github.com/DanySK/code-plagiarism-detector/commit/6c5a97408e06064e307f4695933252d5f7adcfa1))
* **gst:** add explanatory comment ([1f63098](https://github.com/DanySK/code-plagiarism-detector/commit/1f6309884e6f2b155da7e7cf48ad9fef2fc6f19b))
* **gst:** avoid using unuseful [@param](https://github.com/param) and [@return](https://github.com/return) tags ([05a7399](https://github.com/DanySK/code-plagiarism-detector/commit/05a7399b048a9e5b6601984821d8956d8b36b0d9))
* **gst:** improve doc comments ([315fb50](https://github.com/DanySK/code-plagiarism-detector/commit/315fb5014c0c13f0a40009e18e187b85b1e61fc5))
* **gst:** improve functions doc ([58195be](https://github.com/DanySK/code-plagiarism-detector/commit/58195bebfc9057e013ec118bffc76fa63719879f))
* **match:** add doc details ([ea5cfd4](https://github.com/DanySK/code-plagiarism-detector/commit/ea5cfd48620d2aa0f0c5bae36fca6e118f613630))


### Style improvements

* add trailing commas ([0b591e9](https://github.com/DanySK/code-plagiarism-detector/commit/0b591e9ff72f650990a5e58835281a665f854f9c))
* **detector:** add trailing comma ([d80b779](https://github.com/DanySK/code-plagiarism-detector/commit/d80b779cc5d57f9e4bbbebe1c79777ceb2e81e69))
* first letter uppercase of test description ([ab75f4f](https://github.com/DanySK/code-plagiarism-detector/commit/ab75f4f105fa9462cc9a4b27ee90426e8d0fe6cf))
* **gst:** function reordering ([921e7d0](https://github.com/DanySK/code-plagiarism-detector/commit/921e7d073c2da46b4eb44fc503424df0206119cf))
* **gst:** remove trailing line ([6faa9f4](https://github.com/DanySK/code-plagiarism-detector/commit/6faa9f42c238d84cac319ec29d7f71bd2c04bee5))


### Refactoring

* **detector:** adapt code with collection instead of sequences ([23edd96](https://github.com/DanySK/code-plagiarism-detector/commit/23edd960b972313336317bb6280ac64478e55720))
* **detector:** change to Running Karp Rabin default strategy ([c7c18a9](https://github.com/DanySK/code-plagiarism-detector/commit/c7c18a99389e19231371cb3d0575cfee6d87f32c))
* **detector:** more idiomatic function name ([6a0704c](https://github.com/DanySK/code-plagiarism-detector/commit/6a0704c3bcbb69d93f0b2974002a36bbf24aa793))
* **detector:** move in single file small semantically related interfaces ([22cbf71](https://github.com/DanySK/code-plagiarism-detector/commit/22cbf7117f074f41454efe9d0cab4d6ecfd6867f))
* **detector:** remove println ([94798a2](https://github.com/DanySK/code-plagiarism-detector/commit/94798a2e51b674f253e6865fa6123dd628746e64))
* **detector:** replace sequences with list ([6c8006f](https://github.com/DanySK/code-plagiarism-detector/commit/6c8006fdf0e22862048f55ca730964def10dee28))
* **gst:** add addAll function for marked tokens ([aba092d](https://github.com/DanySK/code-plagiarism-detector/commit/aba092dfb4abe6767731449c4a5a43cdf88cc571))
* **gst:** add base abstract class for common gst code ([31cb858](https://github.com/DanySK/code-plagiarism-detector/commit/31cb858faf6c159fbce7e0fddf5d72efd15d9099))
* **gst:** add minimum matches length constructor parameter ([d40c937](https://github.com/DanySK/code-plagiarism-detector/commit/d40c937d36afb1030411d60f311b3d538d3f0960))
* **gst:** define `scanPattern` in abstract class ([180649b](https://github.com/DanySK/code-plagiarism-detector/commit/180649bacb9c554bb60dda8e167db84b1145e966))
* **gst:** extracted common code in function ([fa36645](https://github.com/DanySK/code-plagiarism-detector/commit/fa3664543230a9046d991e228909a896741db7e1))
* **gst:** extracted common mark function ([1c85e46](https://github.com/DanySK/code-plagiarism-detector/commit/1c85e46b949748ec1106ebbc76e2f888d86909a0))
* **gst:** improve check occlusion of a tile ([c610110](https://github.com/DanySK/code-plagiarism-detector/commit/c610110f40104abe4068d58d259ff3734b7f7ad5))
* **gst:** limit to int hash value ([1fb792a](https://github.com/DanySK/code-plagiarism-detector/commit/1fb792ab860c356fc7bff9b8f03577c875bef0ed))
* **gst:** made `isNotOccluded` function private ([2f10a30](https://github.com/DanySK/code-plagiarism-detector/commit/2f10a30cfe429008477391e8248590b10ddb23df))
* **gst:** more idiomatic names, improve formatting ([70c23bc](https://github.com/DanySK/code-plagiarism-detector/commit/70c23bcf81daf63d3370e8b85293112974bd510f))
* **gst:** more significant function names ([f0158aa](https://github.com/DanySK/code-plagiarism-detector/commit/f0158aa8efd25290d38de27597e5575e3bbd5e6b))
* **gst:** move common code in abstract base class ([898e376](https://github.com/DanySK/code-plagiarism-detector/commit/898e376699dce31c483e9a450fcab32862fc08c8))
* **gst:** remove `updateMatches` function ([cac8662](https://github.com/DanySK/code-plagiarism-detector/commit/cac86621aa5194c19d4cbbdbacb7311ea0865bb1))
* **gst:** replace not useful variables and parameter reordering ([c7eecdb](https://github.com/DanySK/code-plagiarism-detector/commit/c7eecdb756be4cf6c70328fc973b35d88e546d47))
* **gst:** replace reference equality checks with simple ones ([e87fd4c](https://github.com/DanySK/code-plagiarism-detector/commit/e87fd4c445194f9dd3e85daf1c1781a06e24f1f9))
* **gst:** replace rkr algorithm with thread safe version ([104d052](https://github.com/DanySK/code-plagiarism-detector/commit/104d052f241aa9208a175a47be94bdf6a78bce19))
* **gst:** scoreOfSimilarity -> similarity ([cd8f255](https://github.com/DanySK/code-plagiarism-detector/commit/cd8f255bd07d3b864dbf925f136d89c67f09cf79))
* **gst:** simplify type alias ([0ff74e8](https://github.com/DanySK/code-plagiarism-detector/commit/0ff74e8102b3ac2bf8b086b57b84a2b37d0fd214))
* **gst:** use typealias ([aa5b8b7](https://github.com/DanySK/code-plagiarism-detector/commit/aa5b8b7780c94fb8c0aa45429f45e1d4cf7e22ca))
* **rkr:** improve variables name ([5260394](https://github.com/DanySK/code-plagiarism-detector/commit/5260394b0c5971ff02e2931d9a3fbdf7c06103a2))


### Revert previous changes

* Revert "test: more representative tests" ([81e9619](https://github.com/DanySK/code-plagiarism-detector/commit/81e9619b304170edc1d4033596d899276b5dec78))


### General maintenance

* add java sources for tests ([7ad924f](https://github.com/DanySK/code-plagiarism-detector/commit/7ad924f0fa2f4f8ec52dea190b2e1993f16c8d98))
* add java sources for tests ([9d3f235](https://github.com/DanySK/code-plagiarism-detector/commit/9d3f235cea13b3ccd489efad4de9a8e01e220733))
* comments on java files used for testing ([274aac4](https://github.com/DanySK/code-plagiarism-detector/commit/274aac4aeb087f3d35fd89d45a84b89db28db23e))
* rename java test classes file names ([1bc7d03](https://github.com/DanySK/code-plagiarism-detector/commit/1bc7d034e57870d39ef10ba080e7bc674c4ae87a))
* **testing class:** changes in plagiarized source ([1a8359a](https://github.com/DanySK/code-plagiarism-detector/commit/1a8359ab3baf80c9dd5faeb0eea15c18838a6a11))
* **token types:** remove name+simple-name construct ([8694813](https://github.com/DanySK/code-plagiarism-detector/commit/8694813abdd6b1c28fb189a1a97013672838dda4))


### Tests

* change file name ([af6f368](https://github.com/DanySK/code-plagiarism-detector/commit/af6f368d231958dcd48415979821404e240f9de3))
* **detector:** add match equality test across strategies ([0791b8d](https://github.com/DanySK/code-plagiarism-detector/commit/0791b8d0ab4984b2fea484a79700bcb8aa2b9227))
* **detector:** add RKR detector test ([d85126c](https://github.com/DanySK/code-plagiarism-detector/commit/d85126ce20aa2ef275fbcda966dc87ecff345bc1))
* **detector:** add test case ([ba8ec7b](https://github.com/DanySK/code-plagiarism-detector/commit/ba8ec7b6f42c40b1b00783a35d4bf2bf25a2f548))
* **detector:** add test for non-plagiarized classes ([9837e17](https://github.com/DanySK/code-plagiarism-detector/commit/9837e1785a3e505b41ca7bb0ed2a31306dc5f040))
* **detector:** add token based detector test ([26780ac](https://github.com/DanySK/code-plagiarism-detector/commit/26780ac7a9f36d16418aa0ec2154c0719ad54a94))
* **detector:** change toList -> toSet cause order is not important ([f4d01a0](https://github.com/DanySK/code-plagiarism-detector/commit/f4d01a0a2308cb391ef99dd8cc031bd0b5248c2b))
* **detector:** improve test description ([5c73229](https://github.com/DanySK/code-plagiarism-detector/commit/5c73229bb56566687eb5d37f1f2fd527d9e03d9f))
* more representative tests ([b2d6eef](https://github.com/DanySK/code-plagiarism-detector/commit/b2d6eefa3ff681895eb3eaae43c5958e3d669232))

## [3.0.0](https://github.com/DanySK/code-plagiarism-detector/compare/2.0.0...3.0.0) (2022-09-29)


### ⚠ BREAKING CHANGES

* replace require with check

### Features

* add token and gram implementations ([9b9b996](https://github.com/DanySK/code-plagiarism-detector/commit/9b9b996a78d5bebdfa7cb6de06c2be86204911ae))
* add token type ([715de64](https://github.com/DanySK/code-plagiarism-detector/commit/715de643430a232d252f6c8b96fa25541b70d8cc))
* **analyzer:** add analyzer ([c7283c0](https://github.com/DanySK/code-plagiarism-detector/commit/c7283c026cba151e8c43b6259dbd516153eb28f9))
* **analyzer:** add source representation interfaces ([65a67c8](https://github.com/DanySK/code-plagiarism-detector/commit/65a67c837c94726bc998989efc113d36d7a4f412))
* **representation:** add TokenizedSource implementation ([99ab9a3](https://github.com/DanySK/code-plagiarism-detector/commit/99ab9a3382b86c520c9fb1e032ba2002e0cec24c))
* **steps:** add tokenization step handlers ([bb0df46](https://github.com/DanySK/code-plagiarism-detector/commit/bb0df46ebf114e06d6fbdeba949df7457b2fb9b8))
* **token:** add LanguageTokenTypes ([03e0565](https://github.com/DanySK/code-plagiarism-detector/commit/03e0565f17f889f6e71d154270848117d508a009))


### Bug Fixes

* clear list only after returning the tokens ([22b40f4](https://github.com/DanySK/code-plagiarism-detector/commit/22b40f4624bae9abc09c5af3f28285181ce66749))
* disable environment variables test on pull request ([9f59822](https://github.com/DanySK/code-plagiarism-detector/commit/9f59822e2dcc9212193e7e84be518141a04e4ce8))
* file path OS-independent ([b613a4b](https://github.com/DanySK/code-plagiarism-detector/commit/b613a4bdb070a61d97f5db19695ce430e3679485))


### Dependency updates

* **deps:** update dependency io.mockk:mockk to v1.12.8 ([f79cce8](https://github.com/DanySK/code-plagiarism-detector/commit/f79cce8115583bd8adea89e58a8e2757205e5236))
* **deps:** update dependency io.mockk:mockk to v1.13.1 ([6b3ec1e](https://github.com/DanySK/code-plagiarism-detector/commit/6b3ec1ed927717fd2d55c393e0dbfb4a5b76af9c))
* **deps:** update dependency org.eclipse.jgit:org.eclipse.jgit to v6.3.0.202209071007-r ([c6af4bf](https://github.com/DanySK/code-plagiarism-detector/commit/c6af4bf991283d8d6e869db17830fc01c64469a2))
* **deps:** update dependency org.json:json to v20220924 ([059b2f3](https://github.com/DanySK/code-plagiarism-detector/commit/059b2f3ea374b07a52fd3bf4569d26fa7c878f52))
* **deps:** update dependency org.mockito:mockito-core to v4.8.0 ([77a561e](https://github.com/DanySK/code-plagiarism-detector/commit/77a561e6710d4027000f75877b1e56632e0aaf63))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.1 ([5fb2e13](https://github.com/DanySK/code-plagiarism-detector/commit/5fb2e132a41563b37c1d425461052ed459335677))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.2 ([1f80fe7](https://github.com/DanySK/code-plagiarism-detector/commit/1f80fe76d7c0e870a3eeaad22e075afb4e224126))
* **deps:** update logback to v1.3.0 ([5af44d9](https://github.com/DanySK/code-plagiarism-detector/commit/5af44d91b575c4c73e6848b046684f234fe8db90))
* **deps:** update logback to v1.4.0 ([e794039](https://github.com/DanySK/code-plagiarism-detector/commit/e7940397f0c6a8dd69c1deb03b2f110ba8d57f81))
* **deps:** update logback to v1.4.1 ([5a78bcf](https://github.com/DanySK/code-plagiarism-detector/commit/5a78bcf8334d1f70e04a9000895f2c7943b1ffcb))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.20 ([ece3d5b](https://github.com/DanySK/code-plagiarism-detector/commit/ece3d5b947a42a0d458316e5a7fe4fa0fa4b3262))


### General maintenance

* add java token types config file ([0c75099](https://github.com/DanySK/code-plagiarism-detector/commit/0c750991f767fa68f185887545531f0ac205049d))


### Build and continuous integration

* add java-parser library ([d2d48a7](https://github.com/DanySK/code-plagiarism-detector/commit/d2d48a7728d3bb7023edb26452faa56977ec9250))
* add jvm args of test task ([7d653b9](https://github.com/DanySK/code-plagiarism-detector/commit/7d653b9d7bce3b37e471a17e761cdbabb976666e))
* add serialization plugin and kaml dependencies ([49ae6df](https://github.com/DanySK/code-plagiarism-detector/commit/49ae6dfa87297d305365f78e8b56806c62b5d6f0))
* **deps:** update danysk/action-checkout action to v0.2.2 ([5f45595](https://github.com/DanySK/code-plagiarism-detector/commit/5f45595b449d5d996ee8617bef9cc124f4ef8f98))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.6 ([cdcdbda](https://github.com/DanySK/code-plagiarism-detector/commit/cdcdbdac535e325b7b729b7ce64e36c219808ffb))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.7 ([b4bcdaf](https://github.com/DanySK/code-plagiarism-detector/commit/b4bcdaf0075f67c45fe3779e2ce78f763349bd7d))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.8 ([3320ed2](https://github.com/DanySK/code-plagiarism-detector/commit/3320ed223d643332a734ee23dee2c0af2acd893d))


### Tests

* add test for the environment token supplier ([df8405b](https://github.com/DanySK/code-plagiarism-detector/commit/df8405b17d2da43a3ccdbd44eb15671422ed6663))
* add test for tokenized source ([1e71cf4](https://github.com/DanySK/code-plagiarism-detector/commit/1e71cf4cc0a4c3a19d637e5b10a4a326d90d3e92))
* add token types tests ([c8bcff0](https://github.com/DanySK/code-plagiarism-detector/commit/c8bcff08c295ccd1f31d8aedf1509b9ba7459413))
* **analyzer:** add minimal analyzer test ([ec9b903](https://github.com/DanySK/code-plagiarism-detector/commit/ec9b90336fb90771b0d5a46e3c0064c09302ca39))
* replace env variable test with system extensions ([bab3c09](https://github.com/DanySK/code-plagiarism-detector/commit/bab3c098afc001fe06716965af38e158250c0d33))
* update column of token ([2087aad](https://github.com/DanySK/code-plagiarism-detector/commit/2087aad18a376f8bab2a7d889dd0ba4b601e417d))


### Documentation

* add constructor parameter doc ([d907d42](https://github.com/DanySK/code-plagiarism-detector/commit/d907d42a0c8c7f50646cda550aa7b9e62121b894))
* improve constructor doc comment ([3bcd7ca](https://github.com/DanySK/code-plagiarism-detector/commit/3bcd7cae7b56c80bcaed54feffb70f7ee237abff))
* improve doc comments ([fd5e315](https://github.com/DanySK/code-plagiarism-detector/commit/fd5e3152a3d7c8fd37a58471bea03fc732237c10))
* improve doc comments ([1e12186](https://github.com/DanySK/code-plagiarism-detector/commit/1e12186694acd730be471cd15a61a5cbb882f934))
* improve doc comments ([8de3ef4](https://github.com/DanySK/code-plagiarism-detector/commit/8de3ef481beb3f7e732e8bb5b74805eee9fda1e6))
* improve doc comments ([99a0224](https://github.com/DanySK/code-plagiarism-detector/commit/99a022497d6b11db41fa4bc871a4901715bf9a77))
* update doc ([aa852ad](https://github.com/DanySK/code-plagiarism-detector/commit/aa852ad55c1a7b141c00e798e682a62991054f46))


### Style improvements

* add blank lines separator ([e37e8f0](https://github.com/DanySK/code-plagiarism-detector/commit/e37e8f0fb2875b739c75043d7abbae811b47c169))
* add clarifying parenthesis ([1fa68f4](https://github.com/DanySK/code-plagiarism-detector/commit/1fa68f4d191e6d7a497a28d69ecb8ba500e7ff3c))
* add trailing comma ([5c9b193](https://github.com/DanySK/code-plagiarism-detector/commit/5c9b193b5e73fc34f5c8a7b68772c62f1f101944))
* better formatting ([9d7a4e1](https://github.com/DanySK/code-plagiarism-detector/commit/9d7a4e1762928e1ad5b64d3bfc3d9d413a333680))
* function reordering ([8d1061d](https://github.com/DanySK/code-plagiarism-detector/commit/8d1061dad9cbf830005f31e7c3c94d52beceaa06))
* imports reordering ([210e913](https://github.com/DanySK/code-plagiarism-detector/commit/210e913ddbbff83011d940f6e1a4c251252c9179))
* imports reordering ([754d255](https://github.com/DanySK/code-plagiarism-detector/commit/754d25530fabab23e2c2d23ef89b93ef29ae3f25))
* improve code style with run call ([2870194](https://github.com/DanySK/code-plagiarism-detector/commit/2870194cbf2663638c3524e317be21fd6d33f86c))
* improve error message ([86f98a3](https://github.com/DanySK/code-plagiarism-detector/commit/86f98a37c2eba6e16a1aaca66e6b6232dc7073a6))
* inline toString ([44812c2](https://github.com/DanySK/code-plagiarism-detector/commit/44812c22c0c95eac19f23aac60bfbc57992ecae9))
* remove typo ([6de3cff](https://github.com/DanySK/code-plagiarism-detector/commit/6de3cff51e302ac061009dc989e2864ae0d0b598))
* rename functions with more idiomatic names ([fe49f0d](https://github.com/DanySK/code-plagiarism-detector/commit/fe49f0dfcc27a90475d320441c9ce7b061d776a2))
* replace lambda with method reference ([e7fb2f7](https://github.com/DanySK/code-plagiarism-detector/commit/e7fb2f7253fd9141cf498b56f1d6147b8b6c3b83))
* yaml -> yml extension file ([054d461](https://github.com/DanySK/code-plagiarism-detector/commit/054d461fa27dc43111d0b6aac69f41fbd568d5f1))


### Refactoring

* Analyzer extends a function ([a832c2c](https://github.com/DanySK/code-plagiarism-detector/commit/a832c2c4cce74d1748b0d0512b2390149876ac45))
* **analyzer:** add abstract tokenization analyzer ([4166a99](https://github.com/DanySK/code-plagiarism-detector/commit/4166a99478d907a6600bfdba56bb64e6956ba662))
* clone input object before modifying it ([d9c56d4](https://github.com/DanySK/code-plagiarism-detector/commit/d9c56d45e310461b453bdfe16877a73404ed340f))
* extract in a strategy interface token types retrieval ([3fd8769](https://github.com/DanySK/code-plagiarism-detector/commit/3fd876957e60675313de864d0419d1ea47adcfb0))
* improve package structure ([b633257](https://github.com/DanySK/code-plagiarism-detector/commit/b633257b2b052ed06dc1a8193e4c793544810d6d))
* make private const ([6a1d0cf](https://github.com/DanySK/code-plagiarism-detector/commit/6a1d0cfdf5f5ce8c19f02ea5e6efad3a735026cc))
* ovveride hashcode and equals ([08ebe90](https://github.com/DanySK/code-plagiarism-detector/commit/08ebe90ed72048d5f34d3344c11af7464bd21a03))
* packages refactoring ([c2fb787](https://github.com/DanySK/code-plagiarism-detector/commit/c2fb78790fd970a19ee5d7ff5efd40d1494818bf))
* packages refactoring ([705b87d](https://github.com/DanySK/code-plagiarism-detector/commit/705b87d6565e045874c684c8616e7066dcca85df))
* remove group capturing from regex ([113a1dc](https://github.com/DanySK/code-plagiarism-detector/commit/113a1dcd0f2c2a6d4dabf34c6968d174aa6aa02c))
* remove invoke() invocation, add `operator` in `invoke` override declaration ([c925a55](https://github.com/DanySK/code-plagiarism-detector/commit/c925a5504b0c318ae8e2a2d46f20c43f0f83c6b6))
* replace collection with set ([7600bed](https://github.com/DanySK/code-plagiarism-detector/commit/7600bed2f8d433805d06f6b2be35ee0addb57b65))
* replace collection with set ([9fd32c5](https://github.com/DanySK/code-plagiarism-detector/commit/9fd32c50d2271e749f7267e84c960a8650df5227))
* replace getter with visit function ([e1bb7ee](https://github.com/DanySK/code-plagiarism-detector/commit/e1bb7ee15410ac5b1be10b1c7f94d901c88c6a0b))
* replace java token types supplier with a file supplier one ([ce1efe9](https://github.com/DanySK/code-plagiarism-detector/commit/ce1efe93c21fb59de3a92ae2b535ccc0f08dc482))
* replace not-null assertion with elvis operator ([0b7670f](https://github.com/DanySK/code-plagiarism-detector/commit/0b7670f1369bf3740c07fb24e1e5ee39588c04b8))
* replace not-null assertion with elvis operator ([d51f60b](https://github.com/DanySK/code-plagiarism-detector/commit/d51f60b9bb6bc63ca6103b13e4f1837be7c45b3a))
* replace require with check ([8eefd34](https://github.com/DanySK/code-plagiarism-detector/commit/8eefd34a566fb2dcfb2806439d59de81cf1e4eb8))
* replace sequences types with list ([c194d72](https://github.com/DanySK/code-plagiarism-detector/commit/c194d727b1dba6208a3ed826eb0e584714ac9cc2))
* replace StaticJavaParser with a parser instance ([6a43cb8](https://github.com/DanySK/code-plagiarism-detector/commit/6a43cb8fd54f45f281e4c3731e289caac548b43f))
* StepHandler is a typealias for a simple function ([81c9552](https://github.com/DanySK/code-plagiarism-detector/commit/81c9552fd87a94e76268f44e7335fab0f606e157))
* switch to private, clear tokens on new visit ([c47bb5b](https://github.com/DanySK/code-plagiarism-detector/commit/c47bb5b3676239d37e775713aadf817dbca5f253))
* **token:** more meaningful names, add Serializable annotation ([8204024](https://github.com/DanySK/code-plagiarism-detector/commit/8204024bdb95f7baca438d4842a6d7ca6252ffb9))
* **token:** remove not useful sealed declaration ([3543822](https://github.com/DanySK/code-plagiarism-detector/commit/35438222644817902ff863343eefe97e08bac558))

## [2.0.0](https://github.com/DanySK/code-plagiarism-detector/compare/1.0.0...2.0.0) (2022-09-12)


### ⚠ BREAKING CHANGES

* change token names, doc adjustments
* **provider:** launch exceptions when errors occurs
* replace ProjectsProvider with RepositoySearchQuery
* replace collaborators property with owner one
* **repository:** change return type of get sources fun from input stream to file
* now launch an exception if URL is illegal instead of return empty set
* now constructor takes in input a search criteria instead of the username and the repo name to search.

### Features

* add abstract repository ([e88c15c](https://github.com/DanySK/code-plagiarism-detector/commit/e88c15cb7909023715fe69495b6e4fa82d2aee77))
* add bitbucket provider ([c429c07](https://github.com/DanySK/code-plagiarism-detector/commit/c429c0758fe96f88719fe3bcf93df38656914d17))
* add bitbucket repository impl skeleton ([417c836](https://github.com/DanySK/code-plagiarism-detector/commit/417c8367001b64bea67cbf9b06ab7f22082abc06))
* add bitbucket search criteria ([62e5a0c](https://github.com/DanySK/code-plagiarism-detector/commit/62e5a0c92c0a5ece96e41c94dd508838fd2efd37))
* add bitbucket search query impl ([f6f9890](https://github.com/DanySK/code-plagiarism-detector/commit/f6f98904b4d0872cd2cc2e05869b8ed3206ec086))
* add extract sources impl ([b220292](https://github.com/DanySK/code-plagiarism-detector/commit/b22029260b1db012572fb99bd4fe83416edb35d8))
* add github provider ([67dacf2](https://github.com/DanySK/code-plagiarism-detector/commit/67dacf23ae9f7634c1878c4bbbea4fe410f7c52b))
* add github repository implementation ([fdc1bc6](https://github.com/DanySK/code-plagiarism-detector/commit/fdc1bc6da2bd415b1dd1b23da79217d400d2d69a))
* add github search criteria ([032c139](https://github.com/DanySK/code-plagiarism-detector/commit/032c139ca9388c524d086e948e9f2ed8397ab343))
* add github search query implementation ([c9b0a19](https://github.com/DanySK/code-plagiarism-detector/commit/c9b0a19f6eb1ad73f55702c86f8dfd1561664e77))
* add projects provider interface ([0ea70da](https://github.com/DanySK/code-plagiarism-detector/commit/0ea70da3e9689b34632303cd6f0ebf0af91a667e))
* add repository interface ([677f3c2](https://github.com/DanySK/code-plagiarism-detector/commit/677f3c2918c64b2fc691459777b0a8fd17aac148))
* add repository search query interface ([8d98f1d](https://github.com/DanySK/code-plagiarism-detector/commit/8d98f1d8337f050ed450dd1dabccf4305a4a1d96))
* add token supplier ([b8270e9](https://github.com/DanySK/code-plagiarism-detector/commit/b8270e91a1978b9b636de87ad874b27b86aabf0c))


### Bug Fixes

* correct logic error in return statement branching ([140e077](https://github.com/DanySK/code-plagiarism-detector/commit/140e077ee691077273b41029d295d698722f475e))
* fix check repo existence ([dfe39c8](https://github.com/DanySK/code-plagiarism-detector/commit/dfe39c8a4187a85c2398a7f43cbc25ce97bb2a4d))
* throw IllegalArgumentException instead of IllegalState if user not exists ([417af42](https://github.com/DanySK/code-plagiarism-detector/commit/417af42fee4cd5cd69a368d913c22b65af61d90a))


### Dependency updates

* **deps:** update dependency gradle to v7.5.1 ([7b08cc5](https://github.com/DanySK/code-plagiarism-detector/commit/7b08cc5a5c020ea61bb98e6c95bad8d1f1f01ed6))
* **deps:** update dependency org.mockito:mockito-core to v4.7.0 ([9ec02f7](https://github.com/DanySK/code-plagiarism-detector/commit/9ec02f7afbc1c904690dc3fb06fe532a4b578fee))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.10 ([be0bf16](https://github.com/DanySK/code-plagiarism-detector/commit/be0bf16bae7408807a009cb3e5c5741515e5f0a5))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.11 ([1b58137](https://github.com/DanySK/code-plagiarism-detector/commit/1b581374b1cda1ef18f2d062852fc5d1726c23cd))
* **deps:** update kotest to v5.4.2 ([5930c16](https://github.com/DanySK/code-plagiarism-detector/commit/5930c1687f1bb5008b03492204db7849a38ada2d))
* **deps:** update node.js to 16.17 ([f9625ff](https://github.com/DanySK/code-plagiarism-detector/commit/f9625ff8ce618064f005549765ab3f549f902db0))
* **deps:** update plugin com.gradle.enterprise to v3.11 ([f90e042](https://github.com/DanySK/code-plagiarism-detector/commit/f90e0427c2a8aad5c3f34904752499361106a571))
* **deps:** update plugin com.gradle.enterprise to v3.11.1 ([5b95e10](https://github.com/DanySK/code-plagiarism-detector/commit/5b95e109d5719de1a42087c833e2dd15c0323627))
* **deps:** update plugin kotlin-qa to v0.22.1 ([a83d981](https://github.com/DanySK/code-plagiarism-detector/commit/a83d981d39266273f1460de8410de7951f46f600))
* **deps:** update plugin kotlin-qa to v0.22.2 ([1554666](https://github.com/DanySK/code-plagiarism-detector/commit/1554666bb6e174b98f08160e19ac48c2f72857f7))
* **deps:** update plugin kotlin-qa to v0.23.0 ([4b029e2](https://github.com/DanySK/code-plagiarism-detector/commit/4b029e2fb31c01ec502221d43e08ad0e436ed932))
* **deps:** update plugin kotlin-qa to v0.23.1 ([8a65d51](https://github.com/DanySK/code-plagiarism-detector/commit/8a65d5117dbd5db39ae730012a10af350d225f2f))
* **deps:** update plugin kotlin-qa to v0.23.2 ([aaa1988](https://github.com/DanySK/code-plagiarism-detector/commit/aaa1988e855214961b4af5306ec82f9e0a7a347d))
* **deps:** update plugin kotlin-qa to v0.24.0 ([ef3a8c0](https://github.com/DanySK/code-plagiarism-detector/commit/ef3a8c0c5177b08bd69a98d2d531e1369e824001))
* **deps:** update plugin kotlin-qa to v0.25.0 ([e074b6e](https://github.com/DanySK/code-plagiarism-detector/commit/e074b6ebd30505b518aa58abd0fe2ff6cf7b717f))
* **deps:** update plugin multijvmtesting to v0.4.9 ([3a26f86](https://github.com/DanySK/code-plagiarism-detector/commit/3a26f861eb47b8fd1471c179bdbf072ace17ce3a))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.18 ([487a29e](https://github.com/DanySK/code-plagiarism-detector/commit/487a29e3e07608549c3402d1791a4b64f8f99f40))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.19 ([76808fc](https://github.com/DanySK/code-plagiarism-detector/commit/76808fc905e9e9f46c8be945e187a5cbd14801ac))
* **deps:** update plugin publishoncentral to v2.0.6 ([b549767](https://github.com/DanySK/code-plagiarism-detector/commit/b549767b5069956309afb66050e69016f4a14d8f))


### General maintenance

* add languages extensions json file ([e818337](https://github.com/DanySK/code-plagiarism-detector/commit/e818337b8d015064737dc6e35ef44a6d8fd95f74))
* **library:** add github api library ([52a6132](https://github.com/DanySK/code-plagiarism-detector/commit/52a61323b2f7320e21931ef5375d1f5f78699f7e))
* **library:** add unirest and json libraries ([379c51a](https://github.com/DanySK/code-plagiarism-detector/commit/379c51a249324d69ddc3243c09e7b579b5919ff3))


### Revert previous changes

* revert to previous token names ([505eac5](https://github.com/DanySK/code-plagiarism-detector/commit/505eac5d30b5688d53717fe667fb57271cb69506))


### Documentation

* add to readme usage section ([fd56626](https://github.com/DanySK/code-plagiarism-detector/commit/fd566269542d5d3d6bb9d04bdb4ed6fbcb6622ba))
* update doc ([4017700](https://github.com/DanySK/code-plagiarism-detector/commit/40177008ed374293a230378d0181538af6903b9d))


### Tests

* add check owner name ([1b9b4b2](https://github.com/DanySK/code-plagiarism-detector/commit/1b9b4b2452af31bb203abf7f7aa677258184d3a3))
* add logger test, remove not useful afterSpec block ([2950b38](https://github.com/DanySK/code-plagiarism-detector/commit/2950b38dce5a803f8d9b75f22ab125595d506b17))
* added mock providers ([af7747f](https://github.com/DanySK/code-plagiarism-detector/commit/af7747f74eb358a996336b9838bf9d5ff1a8a67f))
* added test cases, removed magic numbers ([45685d3](https://github.com/DanySK/code-plagiarism-detector/commit/45685d3b2e61b0595e5b8974f986871fa0026b1a))
* **provider:** add provider tests ([a59db30](https://github.com/DanySK/code-plagiarism-detector/commit/a59db300f76f6a3df7debcec10fbe9df4c3f076d))
* **provider:** add should throw test ([eae3a05](https://github.com/DanySK/code-plagiarism-detector/commit/eae3a057898357b88e91b7a4a8b92675fc89baf9))
* **provider:** replace mock with anonymous authentication ([9d33a81](https://github.com/DanySK/code-plagiarism-detector/commit/9d33a81463ee4985bfced47f519ef35f752071c1))
* replace repos which may change with ones dedicated to tests ([f00e2df](https://github.com/DanySK/code-plagiarism-detector/commit/f00e2dfe6a3b38ed372933f1ec943e84c84a0e34))
* replace repositories with mocked ones ([28e83eb](https://github.com/DanySK/code-plagiarism-detector/commit/28e83ebb674cf14e6106b707e286aeea53c11c1b))
* **repository:** add repository test ([02eb958](https://github.com/DanySK/code-plagiarism-detector/commit/02eb9581100cd004f9a0e3e4855028e579d85395))


### Build and continuous integration

* add env tokens variables ([02bbe0f](https://github.com/DanySK/code-plagiarism-detector/commit/02bbe0ffa7f3b6a8f6625702852bba38e2391692))
* add env variable BB_USER ([dceb78b](https://github.com/DanySK/code-plagiarism-detector/commit/dceb78bd79d15c339c5ca12d5544274b5926bc2b))
* add jvm version, application plugin; replace hardcoded dependencies ([51dc412](https://github.com/DanySK/code-plagiarism-detector/commit/51dc412c7c1587227cb881ed966bd3b8f857d4ea))
* **dependencies:** add jgit, commons-io, jgit libraries ([d442d6d](https://github.com/DanySK/code-plagiarism-detector/commit/d442d6dfee432a5d4ba9eb98aa5ddcf800589534))
* **dependencies:** add logback-core ([5094116](https://github.com/DanySK/code-plagiarism-detector/commit/5094116e7a564739a7ba587fc45763433f5d363c))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.3 ([c3408ff](https://github.com/DanySK/code-plagiarism-detector/commit/c3408ffe750f05ef044df808b30119002dbb769d))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.4 ([232d52d](https://github.com/DanySK/code-plagiarism-detector/commit/232d52d141a8795da206d8a955398e71cf7e6771))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.5 ([914c9b9](https://github.com/DanySK/code-plagiarism-detector/commit/914c9b90704444e9875912520a6d8645af2dfa1a))
* remove not used library dependency ([3d4b1c2](https://github.com/DanySK/code-plagiarism-detector/commit/3d4b1c2de4a0f738d822f52f8c6887f5781ba630))
* simplify libraries entries ([253a3cd](https://github.com/DanySK/code-plagiarism-detector/commit/253a3cd5a3b2569a2451d0e8ccf2568ff43f16be))


### Refactoring

* add error handling and improve iteration over result pages ([298c4a7](https://github.com/DanySK/code-plagiarism-detector/commit/298c4a789f24401dc5f050f5b2366ace87e6ef98))
* add in and out variance ([25e29d8](https://github.com/DanySK/code-plagiarism-detector/commit/25e29d84bad07415e88aac807a1f36e52c85b057))
* add input param to criteria ([836c61a](https://github.com/DanySK/code-plagiarism-detector/commit/836c61aac987cdb5cba65e3959fd337ac035595c))
* add missing '/' ([10e1fe5](https://github.com/DanySK/code-plagiarism-detector/commit/10e1fe592576a529c9d9af5d178893bd04a5d4b8))
* add source filtering ([8e1fd9f](https://github.com/DanySK/code-plagiarism-detector/commit/8e1fd9fa9465fe8e1f618111de57581052b6b12e))
* add toString, replace getter with backing field ([7c9aaa1](https://github.com/DanySK/code-plagiarism-detector/commit/7c9aaa1d2ac58134da9621e36e0c59baa5eee021))
* change constructor args ([93bc2c6](https://github.com/DanySK/code-plagiarism-detector/commit/93bc2c66e77c4ac1edb11e2cfa71b0dbf75f7984))
* change github api library ([9775fbe](https://github.com/DanySK/code-plagiarism-detector/commit/9775fbe3c06dd373949671f977ed14e6221f563d))
* change token names, doc adjustments ([ee4aac0](https://github.com/DanySK/code-plagiarism-detector/commit/ee4aac034df652e8f851b1b9ce31906207d61c2f))
* **content supplier:** replace cloneRepo fun with initializer block ([55aab06](https://github.com/DanySK/code-plagiarism-detector/commit/55aab0688dd97d3ca6f320c833b36bda5651283f))
* **content supplier:** replace language strings with regex ([e8d0e8a](https://github.com/DanySK/code-plagiarism-detector/commit/e8d0e8ab928f2f550b9ea612042822d69b7b8bfd))
* delete GitHubQueryCriteria, GitHubQueryResult classes and replace byCriteria function ([1e0270c](https://github.com/DanySK/code-plagiarism-detector/commit/1e0270cd4a667a65a6bdb40056d5c34a24647140))
* error handling ([64964c3](https://github.com/DanySK/code-plagiarism-detector/commit/64964c369306dcae01151820f0fdb8b84bca7e31))
* from string to URL ([6cd624f](https://github.com/DanySK/code-plagiarism-detector/commit/6cd624f8e01383633c59bd050a6ddce8595edb01))
* **main:** rename main class ([1d1a567](https://github.com/DanySK/code-plagiarism-detector/commit/1d1a567a0cfb4ffbd1cc5c9da2e7f649023e0762))
* move common code in abstract class, add documentation ([30f33b9](https://github.com/DanySK/code-plagiarism-detector/commit/30f33b9255df2d1f72f61fecb40d78ba142d0991))
* move the repo content retrieval logic into strategy interface ([9f65fbe](https://github.com/DanySK/code-plagiarism-detector/commit/9f65fbe5ab381f7efde1fb368679f9317d841e91))
* move to github-api-for-java library ([b269449](https://github.com/DanySK/code-plagiarism-detector/commit/b269449dab4465cccc72b84e48459ed67dc37f20))
* now launch an exception if URL is illegal instead of return empty set ([db471bf](https://github.com/DanySK/code-plagiarism-detector/commit/db471bf6e87bd8a04b32a902735db46bb388d14b))
* packages refactoring ([8ccd811](https://github.com/DanySK/code-plagiarism-detector/commit/8ccd81185f53d6970a93cb57f3a35c22f26a3b96))
* **provider:** allow anonymous authentication ([3c201e3](https://github.com/DanySK/code-plagiarism-detector/commit/3c201e372409648bb6c2e45d2fcf4e557e62c06a))
* **provider:** launch exceptions when errors occurs ([6424709](https://github.com/DanySK/code-plagiarism-detector/commit/6424709f56b9df2d4fc60c2be9308ae52df58cb4))
* **query:** extract common code in abstract class, change return type of by link function ([33efd2d](https://github.com/DanySK/code-plagiarism-detector/commit/33efd2db402bf69695132954777cc8e30be11c92))
* remove not useful shouldBeNull call ([e6d3edf](https://github.com/DanySK/code-plagiarism-detector/commit/e6d3edf0e11117c0fab1ea8e645cc03ec00fa849))
* rename criteria into searchCriteria ([0f884c5](https://github.com/DanySK/code-plagiarism-detector/commit/0f884c549e22457650f412b6797ce676eaefbc4e))
* rename guard clause with Kotlin’s function call ([3d9651d](https://github.com/DanySK/code-plagiarism-detector/commit/3d9651d07e6138b35e91ebbed6665e1157a72cb6))
* replac RepositoryQueryCriteria with search criteria interface, remove QueryResult interface ([9d3ca76](https://github.com/DanySK/code-plagiarism-detector/commit/9d3ca760486a3f9eacde462570f03576259f2df7))
* replace collaborators property with owner one ([145e2f2](https://github.com/DanySK/code-plagiarism-detector/commit/145e2f2fa1cf268cad83a7b5079d4605bb9f4f3f))
* replace iterable with sequences ([62b1909](https://github.com/DanySK/code-plagiarism-detector/commit/62b1909fc7bc015fd7d1af98beafb87c92cd3933))
* replace ProjectsProvider with RepositoySearchQuery ([b42143f](https://github.com/DanySK/code-plagiarism-detector/commit/b42143fc6914641bc1a13cfb0f0f56634a627f60))
* replace sources property with getter with language parameter ([7d9cc11](https://github.com/DanySK/code-plagiarism-detector/commit/7d9cc1133d57d68d1edf79d57aa8c7cec4f6f80c))
* replace toMutableList with toList ([881825c](https://github.com/DanySK/code-plagiarism-detector/commit/881825cf0df36fd2f650948749e9711d764a2e5a))
* **repositories:** add by lazy to properties ([6955dc5](https://github.com/DanySK/code-plagiarism-detector/commit/6955dc50db0252e9ecd6d9544f6c9d3d44bb717b))
* **repository:** change return type of get sources fun from input stream to file ([772c22f](https://github.com/DanySK/code-plagiarism-detector/commit/772c22f8d50ab48011ab89b628f0a59ec303a890))
* **repository:** code improvements ([f3ebbdb](https://github.com/DanySK/code-plagiarism-detector/commit/f3ebbdb6e450e0b1c97da93a982d37e802f6c701))
* **token supplier:** more readable return ([b51c87b](https://github.com/DanySK/code-plagiarism-detector/commit/b51c87bb7554945f40580c84032306c58da7e9a4))


### Style improvements

* change property visibility to val, error messages ([6e00bf6](https://github.com/DanySK/code-plagiarism-detector/commit/6e00bf664b6d893b4ed5a7971c8f9eedddaf8275))
* enclose constants in companion object ([6bad70f](https://github.com/DanySK/code-plagiarism-detector/commit/6bad70fc141d5dfecfe2a50448ae4af1783326f2))
* extract if condition in explanatory function ([8878209](https://github.com/DanySK/code-plagiarism-detector/commit/8878209893087c68fe1633ab65d989bba113b81c))
* improve names and exception messages ([b6bbc19](https://github.com/DanySK/code-plagiarism-detector/commit/b6bbc192fde960a32b3f3c6a30436d7cfa125200))
* move const in companion object ([63527ae](https://github.com/DanySK/code-plagiarism-detector/commit/63527aee6aba83d670948c706c0f88b89542ff44))
* move const out of companion object ([7f54d4c](https://github.com/DanySK/code-plagiarism-detector/commit/7f54d4c028ec994bde53e3974b7addecf250f6a0))
* remove hard coded string ([4f3d8f7](https://github.com/DanySK/code-plagiarism-detector/commit/4f3d8f76618e4127beb2481343b772660993abda))
* remove not useful + char ([aacd41f](https://github.com/DanySK/code-plagiarism-detector/commit/aacd41ff79998f155520974f4b88023ea3e97810))
* remove star imports ([922f630](https://github.com/DanySK/code-plagiarism-detector/commit/922f63053966623244e9637b09e73eabd33da409))
* remove trailing line, improve toString formatting ([756b40f](https://github.com/DanySK/code-plagiarism-detector/commit/756b40fc27fb6470aea45e2247287a538c362416))
* remove unused import ([bb0e75f](https://github.com/DanySK/code-plagiarism-detector/commit/bb0e75f0e73280f7f25c5887006f7f253425ba03))
* reorganize in package ([71b97c3](https://github.com/DanySK/code-plagiarism-detector/commit/71b97c316f2c8b6c87a2170ad3e7c651705ce971))
* replace lambda with function reference ([bebd91e](https://github.com/DanySK/code-plagiarism-detector/commit/bebd91e7f84f454002d4700442606098d770f00e))
* replace the prefix and suffix removal with regex ([02b3593](https://github.com/DanySK/code-plagiarism-detector/commit/02b3593c93939fe19c4b5193d4899e5261f54d21))
* replace toString call with string template ([9569f1d](https://github.com/DanySK/code-plagiarism-detector/commit/9569f1d65a46f007fab8f326914685011884b264))
* **repo:** switch to return ([fba8cbf](https://github.com/DanySK/code-plagiarism-detector/commit/fba8cbff9cbf0ece6367434616989b8a3da5cd73))
* **test:** fix typo ([2443374](https://github.com/DanySK/code-plagiarism-detector/commit/24433743c30d31071d6d92120ea6b0b2ac375448))
* **token supplier:** reformat chaining calls ([bf97a08](https://github.com/DanySK/code-plagiarism-detector/commit/bf97a087ae8d0660216b6957f3567aa4649c42dd))
* use method reference instead of single expression function ([d7cfee1](https://github.com/DanySK/code-plagiarism-detector/commit/d7cfee1b467c6598348787a74019cce93d93dc09))

## 1.0.0 (2022-07-28)


### Bug Fixes

* **deps:** update kotest to v5.4.1 ([82a08c0](https://github.com/DanySK/code-plagiarism-detector/commit/82a08c008e12c4a52f123d63346ea50a7973ec2d))


### Build and continuous integration

* update the project's name ([9aecef0](https://github.com/DanySK/code-plagiarism-detector/commit/9aecef00d84861cd1fbac45c8f44e99b98bedd97))


### General maintenance

* update descriptions in build.gradle.kts ([bb6bea7](https://github.com/DanySK/code-plagiarism-detector/commit/bb6bea7f6c2c25c31ab253490175e612266106bf))

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
