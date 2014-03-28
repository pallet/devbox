# devbox

A pallet project to launch development boxes for pairing.

## Installation

Devbox bootstraps itself using the `devbox` shell script; there is no
separate install script. It handles installing its own dependencies,
which means the first run will take longer.

1. Make sure you have a Java JDK version 6 or later.
2. [Download the script](https://raw.github.com/pallet/devbox/stable/bin/devbox).
3. Place it on your `$PATH`. (`~/bin` is a good choice if it is on your path.)
4. Set it to be executable. (`chmod 755 ~/bin/devbox`)

## Usage

Configure your AWS key:

```shell
devbox aws 'AWS-Key' 'AWS-Secret'
```

In a git directory, run:

```shell
devbox rsync github-user1 github-user2 …
```

## Customisation

### AMI
### Region



## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License.
