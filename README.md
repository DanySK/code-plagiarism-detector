# Code Plagiarism Detector
A tool for scanning existing projects in search of potential signs of plagiarism.

- [Code Plagiarism Detector](#code-plagiarism-detector)
  - [Getting started](#getting-started)
    - [Prerequisites](#prerequisites)
  - [Usage](#usage)
    - [CLI](#cli)

## Getting started

### Prerequisites
In order to work, the following environment variables must be set:
- `BB_USER` which contains the Bitbucket username;
- `BB_TOKEN` which contains the Bitbucket **app password** (see [Bitbucket documentation](https://support.atlassian.com/bitbucket-cloud/docs/app-passwords/));
- `GH_TOKEN` which contains the GitHub **PAT** (see [GitHub documentation](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)).

## Usage

### CLI

Currently, the detector can be used running the JAR file.
The tool has several options thanks to which is possible to configure it. You can see them specifying the `--help` option:

```text
Usage: cli [OPTIONS] COMMAND [ARGS]...

  code-plagiarism-detector is a command line tool for scanning existing
  projects in search of potential signs of plagiarism.

Tokenization options:
  --language VALUE          Sources code language. Default: Java.
  --minimum-tokens INT      The minimum token length which should be reported
                            as a duplicate. Default: 15.
  --filter-threshold FLOAT  The cutoff threshold used to filter comparison
                            pairs.This could affect the detection
                            effectiveness.

Plain file options:
  --o, --output-dir VALUE  The path of the directory where to store the
                           reports.

Options:
  --technique-type [tokenization]  The technique used to analyze and detect
                                   similarities. Default: tokenization.
  --exporter-type [plain-text]     Output type format. Default: plain-text.
  --min-duplication FLOAT          The percentage of duplicated code in a
                                   source file under which matches are not
                                   reported. Default: 0.3.
  --exclude TEXT                   Comma separated list of files to be
                                   excluded from the check.
  -h, --help                       Show this message and exit

Commands:
  submission  Submission options.
  corpus      Corpus Options.
```

To specify how to retrieve projects two subcommands are provided:
- `submission`: set of projects whose authenticity is to be verified;
- `corpus`: set of projects with which compare each of submission project.

```text
Usage: cli submission [OPTIONS]

  Submission options.

Options to specify for search by criteria:
  --service [github|bitbucket]  The hosting services where are stored the
                                repositories, possibly separated by commas.
  --user TEXT                   The usernames of the repos owners, possibly
                                separated by commas.
  --repository-name TEXT        The names of the searched repositories,
                                possibly separated by commas.

Options:
  --url VALUE  The URL addresses of the repositories to be retrieved, possibly
               separated by commas.
  -h, --help   Show this message and exit
```

For example:
- to search by multiple URL:
  ```bash
  submission --url https://github.com/unibo-oop-projects/Student-Project-OOP-21-Bragari-Mennuti-Violani-Volfgit,https://github.com/unibo-oop-projects/Student-Project-OOP21-Bianchi-Ciccioni-stubborn
  ```
- to search according to the following criteria: all projects with name `oop` owned by `danysk` and `unibo-oop-projects` from, respectively, Github and Bitbucket:
  ```bash
  submission --repository-name oop --user danysk,unibo-oop-projects --service github,bitbucket
  ```
