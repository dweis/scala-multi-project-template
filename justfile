default:
    @just --list

server:
    sbt server:run

server-image:
    sbt server/assembly
    docker build apps/server -t server

cli:
    sbt cli:run

cli-image:
    sbt cli/assembly
    docker build apps/cli -t cli

up:
    docker-compose up

# Auto-format the source tree
fmt:
    treefmt
