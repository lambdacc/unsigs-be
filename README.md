    ### Running the server with docker

cd $PROJECT_ROOT_DIR - This is the same directory where pom.xml is.

1. Build docker image

   `sudo docker build -t unsigs-be:1.0-RC .`

   Check that the image is built

   `sudo docker images | grep unsigs-be`

1. Run the docker image


   `docker run -p 8088:8088 --name unsigs-be -v $PWD/data-volumes/data:/data -v $PWD/data-volumes/logs:/logs unsigs-be:1.0-RC`


1. To ping the server, hit this url.
   `http://localhost:8088/api/v1/ping/`
   Response will be
   ```
   {
   "response": "Hello world"
   }
   ```
   

1. The data related to offers will be stored as a file based db at the mounted location, here it was `~/data` .

1. Under the `api-doc` directory, you will find api documentation and sample Postman collection



#### Useful docker commands

Remove dangling images:
`sudo docker rmi $(sudo docker images --filter "dangling=true" -q --no-trunc)`

Remove all stopped containers
`sudo docker rm $(sudo docker ps --filter status=exited -q)`
