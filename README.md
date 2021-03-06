    ### Running the server with docker

cd $PROJECT_ROOT_DIR - This is the same directory where pom.xml is.

1. Build the code base using maven wrapper.

   `./mvnw clean install`

2. Build docker image

   `sudo docker build -t unsigs-be:1.0-RC .`

   Check that the image is built

   `sudo docker images | grep unsigs-be`

3. Run the docker image


   `sudo docker run -p 8088:8088 --name unsigs-be -v ~/data:/data -v ~/logs:/logs unsigs-be:1.0-RC`


4. To ping the server, hit this url.
   `http://localhost:8088/api/v1/ping/`
   Response will be
   ```
   {
   "response": "Hello world"
   }
   ```
   

5. The data related to offers will be stored as a file based db at the mounted location, here it was `~/data` .

6. Under the `api-doc` directory, you will find api documentation and sample Postman collection



#### Useful docker commands

Remove dangling images:
`sudo docker rmi $(sudo docker images --filter "dangling=true" -q --no-trunc)`

Remove all stopped containers
`sudo docker rm $(sudo docker ps --filter status=exited -q)`