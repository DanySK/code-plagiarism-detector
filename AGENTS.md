# Agent Instructions

## Workflow
- Use the Gradle wrapper for repository work. Treat `package.json` as release metadata only unless the user explicitly asks for Node-related changes.
- Keep changes scoped. Do not modify generated sources unless the task specifically requires it.

## Formatting And Validation
- Run `./gradlew ktlintFormat` before finishing any Kotlin source change.
- Run `./gradlew build` after formatting to validate the final state.
- If `ktlintFormat` changes files, review the result and rerun `./gradlew build`.

## Build Preconditions
- Check whether `GH_TOKEN` or `GITHUB_TOKEN` is available before tasks that may trigger Apollo schema work.
- If a Gradle task fails because GitHub credentials are missing, report that constraint clearly instead of working around it with ad hoc build changes.

## Warning Suppressions
- Treat warning suppressions as a last resort.
- Add a short justification next to every suppression explaining why it is necessary.
- Avoid blanket or unexplained suppressions.
