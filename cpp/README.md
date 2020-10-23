This README.md shows how to use the vearch c++ grpc client.

1. Installation grpc
    You can follow the guide(https://github.com/grpc/grpc/tree/master/src/cpp)

2. Set environment variables

	```bash
    grpc_path="your install path of grpc"
  
    export PATH=$grpc_path/bin:$PATH
  
    export LIBRARY_PATH=$LIBRARY_PATH:$grpc_path/lib:$grpc_path/lib64
  
    export CPLUS_INCLUDE_PATH=$CPLUS_INCLUDE_PATH:$grpc_path/include
  
    export PKG_CONFIG_PATH=$PKG_CONFIG_PATH:$grpc_path/lib/pkgconfig:$grpc_path/lib64/pkgconfig
    ```
 
3. How to use

    client.h is the package of vearch c++ client, test_client.cc shows how to use it. You can build the test_client.cc just by: `make`, then you can get the executable file test_client.

