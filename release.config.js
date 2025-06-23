var publishCmd = `
./gradlew shadowJar || exit 2
./gradlew publishAllPublicationsToProjectLocalRepository zipMavenCentralPortalPublication releaseMavenCentralPortalPublication || exit 3
./gradlew publishKotlinOSSRHPublicationToGithubRepository || true
`
var config = require('semantic-release-preconfigured-conventional-commits');
config.plugins.push(
    [
        "@semantic-release/exec",
        {
            "publishCmd": publishCmd,
        }
    ],
    [
        "@semantic-release/github", {
            "assets": [
                {
                    "path": "build/libs/*-all.jar"
                },
            ]
        }
    ],
    "@semantic-release/git",
)
module.exports = config
