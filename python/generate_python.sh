#!/usr/bin/env bash

echo $0 "[out_dir [source_dir]]"

PROGRAM=$(basename "$0")

# check protoc exist
command -v protoc >/dev/null 2>&1 || { echo >&2 "ERR: protoc is required but it's not installed.  Aborting."; exit 1; }

#check_protoc_version
function version_gt() { test "$(echo "$@" | tr " " "\n" | sort -V | head -n 1)" != "$1"; }
function version_le() { test "$(echo "$@" | tr " " "\n" | sort -V | head -n 1)" == "$1"; }
function version_lt() { test "$(echo "$@" | tr " " "\n" | sort -rV | head -n 1)" != "$1"; }
function version_ge() { test "$(echo "$@" | tr " " "\n" | sort -rV | head -n 1)" == "$1"; }

cmd_exists () {
    #which "$1" 1>/dev/null 2>&1
    which "$1"
}

check_protoc_version() {
    version=$(protoc --version | awk -F"[ ]" '{print $2}')
    echo "protoc current version is "$version
    if version_lt $version "3.1.0"; then
        echo "Error : version not match, version 3.1.0 or above is needed"
        exit 1
    fi
}

check_protoc_version

gen_out_dir=./vearchpb
if [ ! -d $gen_out_dir ];then
	mkdir $gen_out_dir
fi

proto_dir=../proto
if [ "$2" ]; then
    proto_dir=$2
fi

ret=0
for file in `ls ${proto_dir}/*.proto`
do
	echo $file
	python  -m grpc_tools.protoc -I${proto_dir} --python_out=$gen_out_dir --grpc_python_out=$gen_out_dir ${file}
done
exit $ret
