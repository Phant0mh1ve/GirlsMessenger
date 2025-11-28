cd backend
mvn clean package -DskipTests
cd ..
docker-compose up --build