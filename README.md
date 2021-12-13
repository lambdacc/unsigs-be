### Running the server with docker

cd $PROJECT_ROOT_DIR - This is the same directory where pom.xml is.

1. Build the code base using maven wrapper.

   `./mvnw clean install`

2. Build docker image

   `sudo docker build -t unsigs-be:0.1 .`

   Check that the image is built

   `sudo docker images | grep unsigs-be`

3. Run the docker image

   `sudo docker run -p 8088:8088 -d --name unsigs-be -v ~/data:/data unsigs-be:0.1`

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
