var prepareCmd = `
./gradlew shadowJar
`
var publishCmd = `
./gradlew uploadKotlin release || exit 3
./gradlew publishKotlinOSSRHPublicationToGithubRepository || true
`
var config = require('semantic-release-preconfigured-conventional-commits');
config.plugins.push(
    [
        "@semantic-release/exec",
        {
            "prepareCmd": prepareCmd,
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
